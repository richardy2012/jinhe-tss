package com.jinhe.tss.um.sso;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.component.param.ParamConfig;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.exception.UserIdentificationException;
import com.jinhe.tss.framework.sso.IOperator;
import com.jinhe.tss.framework.sso.IPWDOperator;
import com.jinhe.tss.framework.sso.PasswordPassport;
import com.jinhe.tss.framework.sso.identifier.BaseUserIdentifier;
import com.jinhe.tss.um.service.ILoginService;

/**
 * <p>
 * 基于LDAP用户库的 用户名/密码 身份认证器<br>
 * 根据用户帐号、密码等信息，通过LDAP用户库进行身份认证，如果认证通过则从um主用户组里获取该用户（同步时导入到主用户组下的)DTO。
 * 登录时需要登录请求里传递identifier参数（值：LDAPUserPWDIdentifier）。
 * </p>
 */
public class LDAPUserPWDIdentifier extends BaseUserIdentifier {
    
    ILoginService service = (ILoginService) Global.getContext().getBean("LoginService");
    
    protected IOperator validate() throws UserIdentificationException {
        PasswordPassport passport = new PasswordPassport();
        
        String loginName = passport.getLoginName();
        if (checkPWDInOA(loginName, passport.getPassword())) {
            IPWDOperator operator = service.getOperatorDTOByLoginName(loginName);
            if(operator == null){
                throw new BusinessException("用户：" + loginName + "在UMS用户库的主用户组里不存在，请先注册或联系管理员同步用户后再进行登录。");
            }
            return operator;
        } 
        else {
            throw new UserIdentificationException("用户密码不正确，请重新登录");
        }
    }
    
    /**
     * 通过尝试连接LDAP服务器来判断用户输入的密码是否和OA密码的一致，如果是，则返回true。
     * @param loginName
     * @param password
     * @return
     */
    boolean checkPWDInOA(String loginName, String password){
        // 初始化参数设置
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.PROVIDER_URL, ParamConfig.getAttribute("oa.ldap.url"));
        env.put(Context.SECURITY_PRINCIPAL, loginName);
        env.put(Context.SECURITY_CREDENTIALS, password);

        // 连接到数据源
        DirContext ctx = null;
        try {
            ctx = new InitialDirContext(env);
            return true; // 如果连接成功则返回True
        } catch (Exception e) {
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
}
