package com.jinhe.tss.framework.sso;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.sso.context.RequestContext;
import com.jinhe.tss.util.InfoEncoder;

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
    public PasswordPassport() throws BusinessException {
        RequestContext requestContext = Context.getRequestContext();
        String loginName = requestContext.getValueFromHeaderOrParameter(SSOConstants.LOGINNAME_IN_SESSION);
        String password  = requestContext.getValueFromHeaderOrParameter(SSOConstants.USER_PASSWORD);
        if (loginName == null || password == null) {
            throw new BusinessException("账号或密码不能为空，请重新登录。");
        }
        
        Integer randomKey = (Integer) requestContext.getSession().getAttribute(SSOConstants.RANDOM_KEY);
        if(randomKey == null) {
        	randomKey = 100;
        }
        
        this.loginName = InfoEncoder.simpleEncode(loginName, randomKey);
        this.password = InfoEncoder.simpleEncode(password, randomKey);
    }

    public String getPassword() {
        return password;
    }

    public String getLoginName() {
        return loginName;
    }
}
