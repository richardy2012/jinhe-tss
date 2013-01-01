package com.jinhe.tss.framework.sso.identity;

import com.jinhe.tss.framework.sso.DemoOperator;
import com.jinhe.tss.framework.sso.IOperator;
import com.jinhe.tss.framework.sso.IdentityTranslator;
import com.jinhe.tss.framework.sso.IdentityTranslatorFactory;
 
public class MockIdentityTranslatorFactory extends IdentityTranslatorFactory {

    public static void init() {
        translator = new IdentityTranslator(){
                
                public IOperator translate(Long standardUserId) {
                    return new DemoOperator(standardUserId);
                }
    
                public IOperator translate(Long standardUserId, String targetAppCode) {
                    return new DemoOperator(standardUserId);
                }
    
                public void savePassword(Long userId, String password) {
    
                }
            };
    }

}
