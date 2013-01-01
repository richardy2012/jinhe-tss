package com.jinhe.tss.um.servlet;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.jinhe.tss.um.TxSupportTest4UM;

public class ResetPasswordServletTest extends TxSupportTest4UM {
    
    public void testDoPost() {
        MockHttpServletRequest request = new MockHttpServletRequest(); 
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        request.addParameter("userId", "-1");
        request.addParameter("password", "********");
        request.addParameter("newPassword", "123456789");
        request.addParameter("type", "not-reset");
         
        ResetPasswordServlet servlet = new ResetPasswordServlet();
        try {
            servlet.init();
            try {
                servlet.doPost(request, response);
            } catch (Exception e) {
                assertTrue("旧密码输入不正确", true);
            } 
            
            request.removeParameter("password");
            request.removeParameter("type");
            request.addParameter("password", "123456");
            request.addParameter("type", "reset");
            servlet.doPost(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            servlet.destroy();
        }
    }
    
}
