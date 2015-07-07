package com.jinhe.tss.um.module;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.MailUtil;
import com.jinhe.tss.framework.component.param.ParamConfig;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.action.MessageAction;
import com.jinhe.tss.um.entity.Message;

public class MessageActionTest extends TxSupportTest4UM {
	
	@Autowired MessageAction messageAction;
	
	@Test
	public void test() {
		messageAction.sendEmail("test", "<html><body><h3>生命变的厚重</h3></body></html>", ParamConfig.getAttribute(MailUtil.SEND_TO));
	}
	
	@Test
	public void test2() {
		messageAction.sendHtmlEmail("test", "<html><body><h3>生命变的厚重</h3></body></html>", ParamConfig.getAttribute(MailUtil.SEND_TO));
	}
	
	@Test
	public void test3() {
		messageAction.sendMessage("test", "生命变的厚重", "Admin");
		List<Message> list = messageAction.listMessages();
		Assert.assertTrue(list.size() > 0);
		
		Message message1 = list.get(0);
		Assert.assertEquals(UMConstants.ADMIN_USER_ID, message1.getReceiverId());
		Assert.assertEquals("test", message1.getTitle());
		Assert.assertEquals("生命变的厚重", message1.getContent());
		Assert.assertEquals(Environment.getUserId(), message1.getSenderId());
		Assert.assertEquals(Environment.getUserName(), message1.getSender());
		Assert.assertNotNull(message1.getSendTime());
		Assert.assertNull(message1.getReadTime());
		
		Long message1Id = message1.getId();
		message1 = messageAction.getMessage(message1Id);
		Assert.assertNotNull(message1.getReadTime());
		
		messageAction.deleteMessage(message1Id);
		
		list = messageAction.listMessages();
		Assert.assertTrue(list.size() == 0);
	}

}
