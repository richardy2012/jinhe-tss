package com.jinhe.tss.um.servlet;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.jinhe.tss.um.TxSupportTest4UM;

public class RegisterServletTest extends TxSupportTest4UM {
    
	@Test
    public void testDoPost() {
        MockHttpServletRequest request = new MockHttpServletRequest(); 
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        request.addParameter("loginName", "JinPujun");
        request.addParameter("password", "JinPujun");
        request.addParameter("userName", "JinPujun");
        request.addParameter("employeeNo", "JinPujun");
        request.addParameter("sex", "1");
        request.addParameter("mail", "JinPujun@hotmail.com");
        request.addParameter("birthday", "1983-06-22");
        request.addParameter("address", "hangzhou zhejiang");
        request.addParameter("telephone", "88888888");
        request.addParameter("postalCode", "317000");
        request.addParameter("passwordQuestion", "");
        request.addParameter("passwordAnswer", "");
        request.addParameter("certificateCategory", "");
        request.addParameter("certificateNumber", "");
        
        RegisterServlet registerServlet = new RegisterServlet();
        
        try {
            registerServlet.init();
            registerServlet.doGet(request, response);
            
        } catch (Exception e) {
        	Assert.assertFalse("Test servlet error:" + e.getMessage(), true);
        } finally {
            registerServlet.destroy();
        }
    }
    
}
