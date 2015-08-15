package com.jinhe.tss.um.action;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jinhe.tss.framework.MailUtil;
import com.jinhe.tss.framework.persistence.pagequery.PageInfo;
import com.jinhe.tss.framework.web.dispaly.grid.GridDataEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.um.entity.Message;
import com.jinhe.tss.um.helper.MessageQueryCondition;
import com.jinhe.tss.um.service.ILoginService;
import com.jinhe.tss.um.service.IMessageService;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.XMLDocUtil;

@Controller
@RequestMapping("/auth/message")
public class MessageAction extends BaseActionSupport {
	
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
    
    @RequestMapping(value = "/email2", method = RequestMethod.POST)
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
    
    @RequestMapping(value = "/list/{page}")
    public void listMessages(HttpServletResponse response, MessageQueryCondition condition, @PathVariable int page) {
        condition.getPage().setPageNum(page);
        PageInfo pi = messageService.getInboxList(condition);
        
        GridDataEncoder gridEncoder = new GridDataEncoder(pi.getItems(), XMLDocUtil.createDoc("template/grid/message_grid.xml"));
        print( new String[]{"MsgList", "PageInfo"}, new Object[]{gridEncoder, pi} );
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Message getMessage(@PathVariable("id") Long id) {
    	return messageService.viewMessage(id);
    }
    
    @RequestMapping(value = "/more/{id}", method = RequestMethod.GET)
    @ResponseBody
    public void viewMore(@PathVariable("id") String id) {
    	messageService.viewMore(id);
    }
    
    @RequestMapping(value = "/num", method = RequestMethod.GET)
    @ResponseBody
    public int getNewMessageNum() {
    	return messageService.getNewMessageNum();
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteMessage(@PathVariable("id") String id) {
    	messageService.deleteMessage(id);
    }
}
