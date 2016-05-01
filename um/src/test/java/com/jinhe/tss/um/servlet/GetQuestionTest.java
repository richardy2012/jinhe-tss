package com.jinhe.tss.um.servlet;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.jinhe.tss.framework.sso.SSOConstants;
import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.service.IUserService;

public class GetQuestionTest extends TxSupportTest4UM {
    
    @Autowired IUserService userService;
    
    @Test
    public void testDoPost() {
        MockHttpServletRequest request = new MockHttpServletRequest(); 
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        request.addParameter(SSOConstants.LOGINNAME_IN_SESSION, "Admin001");
        
        GetQuestion servlet = new GetQuestion();
        
        try {
            servlet.doPost(request, response);
            
            request.removeParameter(SSOConstants.LOGINNAME_IN_SESSION);
            request.addParameter(SSOConstants.LOGINNAME_IN_SESSION, "Admin");
            servlet.doPost(request, response);
            
            User user = userService.getUserByLoginName("Admin");
            user.setPasswordQuestion("1+1=?");
            user.setPasswordAnswer("=2");
            servlet.doPost(request, response);
            
            user.setPasswordQuestion(null);
            user.setPasswordAnswer(null);
            userService.updateUser(user);
            servlet.doGet(request, response);
            
        } catch (Exception e) {
        	Assert.assertFalse("Test servlet error:" + e.getMessage(), true);
        }
    }
    
}
