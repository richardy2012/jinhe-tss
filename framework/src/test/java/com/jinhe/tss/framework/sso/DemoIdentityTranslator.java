package com.jinhe.tss.framework.sso;


public class DemoIdentityTranslator implements IdentityTranslator{
    
    public IOperator translate(Long standardUserId) {
        return new DemoOperator(standardUserId);
    }

    public IOperator translate(Long standardUserId, String targetAppCode) {
        return new DemoOperator(standardUserId);
    }

    public void savePassword(Long userId, String password) {

    }
}
