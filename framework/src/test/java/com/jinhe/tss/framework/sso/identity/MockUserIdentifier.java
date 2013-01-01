package com.jinhe.tss.framework.sso.identity;

import com.jinhe.tss.framework.exception.UserIdentificationException;
import com.jinhe.tss.framework.sso.IOperator;
import com.jinhe.tss.framework.sso.identifier.BaseUserIdentifier;
 

public class MockUserIdentifier extends BaseUserIdentifier {
 
    protected IOperator validate() throws UserIdentificationException {
        throw new UserIdentificationException();
    }

}
