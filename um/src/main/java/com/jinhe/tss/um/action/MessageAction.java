package com.jinhe.tss.um.action;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jinhe.tss.framework.MailUtil;
import com.jinhe.tss.um.service.ILoginService;

@Controller
@RequestMapping("/auth/message")
public class MessageAction {
	
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired ILoginService loginService;
	
    @RequestMapping(value = "/email", method = RequestMethod.POST)
    @ResponseBody
    public void sendEmail(String title, String content, String receivers) {
    	String[] emails = loginService.getEmails(receivers);
    	if(emails != null && emails.length > 0) {
    		MailUtil.send(title, content, emails);
    	}
    }
    
    @RequestMapping(value = "/email/html", method = RequestMethod.POST)
    @ResponseBody
    public void sendHtmlEmail(String title, String content, String receivers) {
    	String[] emails = loginService.getEmails(receivers);
    	if(emails != null && emails.length > 0) {
    		MailUtil.sendHTML(title, content, emails);
    	}
    }
}
