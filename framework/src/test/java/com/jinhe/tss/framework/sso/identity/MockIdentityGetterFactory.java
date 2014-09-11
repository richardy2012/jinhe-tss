package com.jinhe.tss.framework.sso.identity;

import com.jinhe.tss.framework.sso.DemoOperator;
import com.jinhe.tss.framework.sso.IOperator;
import com.jinhe.tss.framework.sso.IPWDOperator;
import com.jinhe.tss.framework.sso.IdentityGetter;
import com.jinhe.tss.framework.sso.IdentityGetterFactory;
 
public class MockIdentityGetterFactory extends IdentityGetterFactory {

    public static void init() {
        getter = new IdentityGetter(){
                
                public IOperator getOperator(Long standardUserId) {
                    return new DemoOperator(standardUserId);
                }

				public boolean indentify(IPWDOperator operator, String password) {
					return false;
				}
 
            };
    }

}
