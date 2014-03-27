package com.jinhe.tss.framework.sso;


public class DemoIdentityGetter implements IdentityGetter{
    
    public IOperator getOperator(Long standardUserId) {
        return new DemoOperator(standardUserId);
    }
}
