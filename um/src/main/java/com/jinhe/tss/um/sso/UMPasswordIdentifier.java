package com.jinhe.tss.um.sso;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.component.param.ParamConfig;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.IOperator;
import com.jinhe.tss.framework.sso.IPWDOperator;
import com.jinhe.tss.framework.sso.PasswordPassport;
import com.jinhe.tss.framework.sso.appserver.AppServer;
import com.jinhe.tss.framework.sso.identifier.BaseUserIdentifier;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.service.ILoginService;
import com.jinhe.tss.util.EasyUtils;

/**
 * <p>
 * UM本地用户密码身份认证器<br>
 * 根据用户帐号、密码等信息，通过UM本地数据库进行身份认证
 * </p>
 */
public class UMPasswordIdentifier extends BaseUserIdentifier {
    
    protected Logger log = Logger.getLogger(this.getClass());
    
    ILoginService service = (ILoginService) Global.getContext().getBean("LoginService");
    
    protected IOperator validate() throws BusinessException {
        PasswordPassport passport = new PasswordPassport();
        IPWDOperator operator = null;
        try {
            operator = service.getOperatorDTOByLoginName(passport.getLoginName());
        } catch (BusinessException e) {
        	throw new BusinessException(e.getMessage(), false);
        }
        
        String password = User.encodePassword(passport.getLoginName(), passport.getPassword());
        if (password.equals(operator.getPassword())) {
            return operator;
        } 
        else {
            /*
             *  判断用户输入的密码是否和OA密码的一致，如果是，则将用户的平台里的密码也设置为该密码，并完成本次登录
             *  (适用于UM的用户从第三方导入的情况，因密码是加密的，无法直接导入过来)
             */
            if(checkPWDInLDAP(operator.getId(), passport.getPassword())) {
                return operator;
            }
            
            throw new BusinessException("用户密码不正确，请重新登录", false);
        }
    }
    
    /**
     * 判断用户输入的密码是否和LDAP中的密码一致，如果是，则将用户的平台里的密码也设置为该密码。
     * 注 ： 需要在相应的系统参数管理模块里增加 oa.ldap.url 参数。
     * 
     * @param userId
     * @param password
     * @return
     */
    boolean checkPWDInLDAP(Long userId, String password){
        log.debug("用户登陆时密码在主用户组中验证不通过，转向LDAP进行再次验证。");
        
        // 取主用户的对应用户    
        IPWDOperator oaUser = service.getOperatorDTOByID(userId);
        String oaLdapUrl = ParamConfig.getAttribute("oa.ldap.url");
        
        // 初始化参数设置
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.PROVIDER_URL, EasyUtils.convertObject2String(oaLdapUrl));
        env.put(Context.SECURITY_PRINCIPAL, oaUser.getLoginName());
        env.put(Context.SECURITY_CREDENTIALS, password);

        // 连接到数据源
        DirContext ctx = null;
        try {
            ctx = new InitialDirContext(env);
            log.info("用户【" + oaUser.getLoginName() + "】的密码在LDAP中验证通过。");
            
            modifyPTUserPassword(userId, password);
            
            return true; // 如果连接成功则返回True
        } 
        catch (Exception e) {
            log.error("用户【" + oaUser.getLoginName() + "】的密码在LDAP中验证不通过。");
            return false;
        } finally {
            if(ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException e) {
                }
            }
        }
    }

    /**
     * 调用UM里修改密码的接口（resetPassword.in）来修改用户在平台下（主用户组和其他用户组）的密码。
     * @param userId
     * @param password
     */
    void modifyPTUserPassword(Long userId, String password) {
        try {
            HttpClient httpClient = new HttpClient(); // 构造HttpClient的实例
            
            AppServer appServer = com.jinhe.tss.framework.sso.context.Context.getApplicationContext().getAppServer("TSS");
            PostMethod postMethod = new PostMethod(appServer.getBaseURL() + "/resetPassword.in");
            // 填入各个表单域的值
            NameValuePair[] params = { new NameValuePair("userId", userId.toString()), 
                                       new NameValuePair("checkOldPassword", "false"), 
                                       new NameValuePair("password", password), 
                                       new NameValuePair("newPassword", password) };
            // 将表单的值放入postMethod中
            postMethod.setRequestBody(params);
            // 执行postMethod
            try {
                int statusCode = httpClient.executeMethod(postMethod);
                if(statusCode == HttpStatus.SC_OK){
                    // 读取内容
                    byte[] responseBody = postMethod.getResponseBody();
                    log.info(new String(responseBody));
                }
            } catch (Exception e) {
                log.error("执行请求修改密码的Servlet时出错！", e);
            } finally {
                postMethod.releaseConnection();
            }
        } catch (Exception e) {
            log.error("UMPasswordIdentifier 执行 checkPWDInOA 方法时出错！", e);
        } 
    }
}
