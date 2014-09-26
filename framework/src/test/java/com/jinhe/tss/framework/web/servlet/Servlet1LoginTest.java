package com.jinhe.tss.framework.web.servlet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartHttpServletRequest;

public class Servlet1LoginTest {
	private MockMultipartHttpServletRequest request;
	private MockHttpServletResponse response;

	@Before
	public void setUp() throws Exception {
	    request = new MockMultipartHttpServletRequest();
	    response = new MockHttpServletResponse();
	}
	
	@Test
    public void testDoPost() {
		Servlet1Login servlet = new Servlet1Login();
        try {
        	servlet.doPost(request, response);
        	
        	request.addParameter("sso", new String[] {"xxx"});
        	servlet.doPost(request, response);
        	
            Servlet2Logout servlet2 = new Servlet2Logout();
            servlet2.doGet(request, response);
            
        } catch (Exception e) {
        	Assert.assertFalse("Test servlet error:" + e.getMessage(), true);
        } finally {
        	servlet.destroy();
        }
    }
}
