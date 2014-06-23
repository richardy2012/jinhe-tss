package com.jinhe.tss.framework.web.servlet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public class Servlet4UploadTest {

	private MockMultipartHttpServletRequest request;
	private MockHttpServletResponse response;

	@Before
	public void setUp() throws Exception {
	    request = new MockMultipartHttpServletRequest();
	    response = new MockHttpServletResponse();
	    
	    request.addParameter("afterUploadClass", new String[] {"xxx"});
	    
	    MultipartFile file = new MockMultipartFile("file", "test file".getBytes());
		request.addFile(file);
	}
	
	@Test
    public void testDoPost() {
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
