package com.jinhe.tss.framework.sso.identifier;

import com.jinhe.tss.framework.exception.UserIdentificationException;
import com.jinhe.tss.framework.sso.AnonymousOperator;
import com.jinhe.tss.framework.sso.IOperator;
import com.jinhe.tss.framework.sso.context.Context;

/**
 * <p>
 * 匿名用户身份认证器
 * </p>
 */
public class AnonymousUserIdentifier extends BaseUserIdentifier {

    protected IOperator validate() throws UserIdentificationException {
        if (Context.getRequestContext().canAnonymous()) {
            return AnonymousOperator.anonymous; 
        } 
        else {
            throw new UserIdentificationException("系统要求身份认证，请重新登录！");
        }
    }

}
