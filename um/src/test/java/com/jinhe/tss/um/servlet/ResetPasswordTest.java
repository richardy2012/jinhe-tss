package com.jinhe.tss.um.servlet;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.jinhe.tss.um.TxSupportTest4UM;

public class ResetPasswordTest extends TxSupportTest4UM {
    
	@Test
    public void testDoPost() {
        MockHttpServletRequest request = new MockHttpServletRequest(); 
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        request.addParameter("userId", "-1");
        request.addParameter("password", "********");
        request.addParameter("newPassword", "123456789");
        request.addParameter("type", "not-reset");
         
        ResetPassword servlet = new ResetPassword();
        try {
            servlet.init();
            try {
                servlet.doPost(request, response);
                Assert.fail("should throw exception but didn't.");
            } catch (Exception e) {
                assertTrue("旧密码输入不正确", true);
            } 
            
            request.removeParameter("password");
            request.removeParameter("type");
            request.addParameter("password", "123456");
            request.addParameter("type", "reset");
            servlet.doGet(request, response);
            
        } catch (Exception e) {
            Assert.assertFalse("Test servlet error:" + e.getMessage(), true);
        } finally {
            servlet.destroy();
        }
    }
    
}
