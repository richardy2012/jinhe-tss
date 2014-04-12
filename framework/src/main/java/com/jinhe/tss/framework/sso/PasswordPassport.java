package com.jinhe.tss.framework.sso;

import com.jinhe.tss.framework.exception.UserIdentificationException;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.sso.context.RequestContext;

/**
 * <p>
 * 账号密码通行证：从Context.getRequestContext() 获取用户的登陆帐号和密码。
 * 
 * 通行证需要经过验证通过后再正式发放IdentityCard（身份证书）。
 * </p>
 */
public class PasswordPassport {

    /** 用户登录名 */
    private String loginName;

    /** 用户密码 */
    private String password;

    /**
     * 默认构造函数
     */
    public PasswordPassport() throws UserIdentificationException {
        RequestContext requestContext = Context.getRequestContext();
        this.loginName = requestContext.getValueFromHeaderOrParameter(SSOConstants.LOGINNAME_IN_SESSION);
        this.password  = requestContext.getValueFromHeaderOrParameter(SSOConstants.USER_PASSWORD);
        if (loginName == null || password == null) {
            throw new UserIdentificationException("账号或密码不能为空，请重新登录。");
        }
    }

    public String getPassword() {
        return password;
    }

    public String getLoginName() {
        return loginName;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n----------- Passport --------------");
        sb.append("\nClassName: ").append(this.getClass().getName());
        sb.append("\nUserName: ").append(this.loginName);
        sb.append("\nPassword: ").append(this.password);
        sb.append("\n------------- End -----------------");
        return sb.toString();
    }
}
