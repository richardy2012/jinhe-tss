package com.jinhe.tss.um.sso;

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.component.param.ParamConfig;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.exception.UserIdentificationException;
import com.jinhe.tss.framework.sso.IOperator;
import com.jinhe.tss.framework.sso.IPWDOperator;
import com.jinhe.tss.framework.sso.PasswordPassport;
import com.jinhe.tss.framework.sso.appserver.AppServer;
import com.jinhe.tss.framework.sso.identifier.BaseUserIdentifier;
import com.jinhe.tss.um.service.ILoginService;
import com.jinhe.tss.util.InfoEncoder;

/**
 * <p>
 * UMS本地用户密码身份认证器<br>
 * 根据用户帐号、密码等信息，通过UMS本地数据库进行身份认证
 * </p>
 */
public class UMSLocalUserPWDIdentifier extends BaseUserIdentifier {
    
    protected Logger log = Logger.getLogger(this.getClass());
    
    ILoginService service = (ILoginService) Global.getContext().getBean("LoginService");
    
    protected IOperator validate() throws UserIdentificationException {
        PasswordPassport passport = new PasswordPassport();
        IPWDOperator operator = null;
        try {
            operator = service.getOperatorDTOByLoginName(passport.getLoginName());
        } catch (BusinessException e) {
            throw new UserIdentificationException(e.getMessage()); // 转换为 UserIdentificationException 抛出，防止在日志里输出
        }
        
        String password = InfoEncoder.string2MD5(passport.getLoginName() + "_" + passport.getPassword());
        if (password.equals(operator.getPassword())) {
            return operator;
        } else {
              // 判断用户输入的密码是否和OA密码的一致，如果是，则将用户的平台里的密码也设置为该密码，并完成本次登录
//            if(checkPWDInOA(operator.getId(), passport.getPassword())) {
//                return operator;
//            }
            throw new UserIdentificationException("用户密码不正确，请重新登录");
        }
    }
    
    /**
     * 判断用户输入的密码是否和OA密码的一致，如果是，则将用户的平台里的密码也设置为该密码。
     * 注 ： 需要在相应的应用里（UMS、PMS、CMS）的系统参数管理模块里增加 oa.ldap.url 参数。
     * 
     * @param userId
     * @param password
     * @return
     */
    boolean checkPWDInOA(Long userId, String password){
        log.debug("用户登陆时密码在主用户组中验证不通过，转向LDAP进行再次验证。");
        
        // 取主用户的对应用户    
        IPWDOperator oaUser = service.translateUser(userId, "OA");
        
        // 初始化参数设置
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.PROVIDER_URL, ParamConfig.getAttribute("oa.ldap.url"));
        env.put(Context.SECURITY_PRINCIPAL, oaUser.getLoginName());
        env.put(Context.SECURITY_CREDENTIALS, password);

        // 连接到数据源
        DirContext ctx = null;
        try {
            ctx = new InitialDirContext(env);
            log.debug("用户【" + oaUser.getLoginName() + "】的密码在LDAP中验证通过。");
            
            modifyPTUserPassword(userId, password);
            
            return true; // 如果连接成功则返回True
        } catch (Exception e) {
            log.debug("用户【" + oaUser.getLoginName() + "】的密码在LDAP中验证不通过。");
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
     * 调用UMS里修改密码的接口（resetPassword.in）来修改用户在平台下（主用户组和其他用户组）的密码。
     * @param userId
     * @param password
     */
    private void modifyPTUserPassword(Long userId, String password) {
        try {
            HttpClient httpClient = new HttpClient(); //构造HttpClient的实例
            
            AppServer appServer = com.jinhe.tss.framework.sso.context.Context.getApplicationContext().getAppServer("UMS");
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
            } catch (HttpException e) {
                log.error("执行请求修改密码的Servlet时，修改密码servlet的连接地址可能有误！", e);
            } catch (IOException e) {
                log.error("执行请求修改密码的Servlet时，出现IO异常！", e);
            } finally {
                postMethod.releaseConnection();
            }
        }catch (Exception e) {
            log.error("UMSLocalUserPWDIdentifier 执行 checkPWDInOA 方法时出错！", e);
        } 
    }
}
