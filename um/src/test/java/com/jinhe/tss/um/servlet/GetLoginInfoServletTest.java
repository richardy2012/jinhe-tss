package com.jinhe.tss.um.servlet;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.jinhe.tss.framework.sso.SSOConstants;
import com.jinhe.tss.um.TxSupportTest4UM;

public class GetLoginInfoServletTest extends TxSupportTest4UM {
    
	@Test
    public void testDoPost() {
        MockHttpServletRequest request = new MockHttpServletRequest(); 
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        request.addParameter(SSOConstants.LOGINNAME_IN_SESSION, "Admin");
        
        try {
            GetLoginInfoServlet servlet = new GetLoginInfoServlet();
			servlet.doGet(request, response);
            
        } catch (Exception e) {
        	e.printStackTrace();
        	Assert.assertFalse("Test servlet error:" + e.getMessage(), true);
        }
    }
    
}
