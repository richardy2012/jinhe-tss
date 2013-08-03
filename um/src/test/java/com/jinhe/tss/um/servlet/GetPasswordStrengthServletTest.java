package com.jinhe.tss.um.servlet;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.jinhe.tss.framework.sso.SSOConstants;
import com.jinhe.tss.um.TxSupportTest4UM;

public class GetPasswordStrengthServletTest extends TxSupportTest4UM {
    
	@Test
    public void testDoPost() {
        MockHttpServletRequest request = new MockHttpServletRequest(); 
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        request.addParameter(SSOConstants.LOGINNAME_IN_SESSION, "Admin");
        request.addParameter(SSOConstants.USER_PASSWORD, "123456");
        
        try {
            new GetPasswordStrengthServlet().doPost(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
