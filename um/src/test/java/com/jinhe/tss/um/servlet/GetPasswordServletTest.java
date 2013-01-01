package com.jinhe.tss.um.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.jinhe.tss.framework.sso.SSOConstants;
import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.service.IUserService;

public class GetPasswordServletTest extends TxSupportTest4UM {
    
    @Autowired IUserService userService;
    
    public void testDoPost() {
        MockHttpServletRequest request = new MockHttpServletRequest(); 
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        request.addParameter(SSOConstants.LOGINNAME_IN_SESSION, "Admin001");
        request.addParameter("passwordAnswer", "?");
        request.addParameter("passwordQuestion", "!");
        
        GetPasswordServlet getPasswordServlet = new GetPasswordServlet();
        
        try {
            getPasswordServlet.doPost(request, response);
            
            request.removeParameter(SSOConstants.LOGINNAME_IN_SESSION);
            request.addParameter(SSOConstants.LOGINNAME_IN_SESSION, "Admin");
            getPasswordServlet.doPost(request, response);
            
            User user = userService.getUserByLoginName("Admin");
            user.setPasswordAnswer("?");
            user.setPasswordQuestion("!");
            getPasswordServlet.doPost(request, response);
            
            request.removeParameter("passwordAnswer");
            request.addParameter("passwordAnswer", "************");
            getPasswordServlet.doPost(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
