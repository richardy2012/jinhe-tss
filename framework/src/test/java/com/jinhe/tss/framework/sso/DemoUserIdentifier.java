package com.jinhe.tss.framework.sso;

import com.jinhe.tss.framework.exception.UserIdentificationException;
import com.jinhe.tss.framework.sso.AnonymousOperator;
import com.jinhe.tss.framework.sso.IOperator;
import com.jinhe.tss.framework.sso.identifier.BaseUserIdentifier;

/** 
 * <p>
 * 虚拟身份认证器：以匿名身份登录
 * </p>
 */
public class DemoUserIdentifier extends BaseUserIdentifier {

    protected IOperator validate() throws UserIdentificationException {
        return AnonymousOperator.anonymous;
    }

}
