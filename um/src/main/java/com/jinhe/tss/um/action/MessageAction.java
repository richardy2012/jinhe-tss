package com.jinhe.tss.um.action;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jinhe.tss.framework.MailUtil;
import com.jinhe.tss.um.entity.Message;
import com.jinhe.tss.um.service.ILoginService;
import com.jinhe.tss.um.service.IMessageService;
import com.jinhe.tss.util.EasyUtils;

@Controller
@RequestMapping("/auth/message")
public class MessageAction {
	
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired ILoginService loginService;
	@Autowired IMessageService messageService;
	
    @RequestMapping(value = "/email", method = RequestMethod.POST)
    @ResponseBody
    public void sendEmail(String title, String content, String receivers) {
    	String[] emails = loginService.getContactInfos(receivers, false);
    	if(emails != null && emails.length > 0) {
    		MailUtil.send(title, content, emails);
    	}
    }
    
    @RequestMapping(value = "/email/html", method = RequestMethod.POST)
    @ResponseBody
    public void sendHtmlEmail(String title, String content, String receivers) {
    	String[] emails = loginService.getContactInfos(receivers, false);
    	if(emails != null && emails.length > 0) {
    		MailUtil.sendHTML(title, content, emails);
    	}
    }
    
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void sendMessage(String title, String content, String receivers) {
    	String[] ids = loginService.getContactInfos(receivers, true);
    	if(ids != null && ids.length > 0) {
    		Message message = new Message();
    		message.setTitle(title);
    		message.setContent(content);
    		message.setReceiverIds(EasyUtils.list2Str(Arrays.asList(ids)));
			messageService.sendMessage(message );
    	}
    }
    
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<Message> listMessages() {
    	return messageService.getInboxList();
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Message getMessage(@PathVariable("id") Long id) {
    	return messageService.viewMessage(id);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteMessage(@PathVariable("id") Long id) {
    	messageService.deleteMessage(id);
    }
}
