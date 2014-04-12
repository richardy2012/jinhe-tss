package com.jinhe.tss.framework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Servlet4UploadTest {
	
	private IMocksControl mocksControl;

	private HttpServletRequest request;
	private HttpServletResponse response;

	@Before
	public void setUp() throws Exception {
	    mocksControl =  EasyMock.createControl();
	    request = mocksControl.createMock(HttpServletRequest.class);
	    response = mocksControl.createMock(HttpServletResponse.class);
	}
	
	@Test
    public void testDoPost() {
  
		EasyMock.expect(request.getParameter("afterUploadClass")).andReturn("xxx");
         
        Servlet4Upload servlet = new Servlet4Upload();
        
        try {
        	
        	servlet.doPost(request, response);
            
        } catch (Exception e) {
        	Assert.assertFalse("Test servlet error:" + e.getMessage(), true);
        } finally {
        	servlet.destroy();
        }
    }
}
