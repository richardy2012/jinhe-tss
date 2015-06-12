package com.jinhe.tss.framework.component.message;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/message")
public class MessageAction {
	
	protected Logger log = Logger.getLogger(this.getClass());
	
    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public void sendEmail(String title, String content, String receivers) {
    	MailUtil.send(title, content, receivers.split(","));
    }
}
