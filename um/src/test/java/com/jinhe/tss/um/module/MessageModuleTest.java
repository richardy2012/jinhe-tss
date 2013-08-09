package com.jinhe.tss.um.module;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.action.MessageAction;
import com.jinhe.tss.um.entity.Message;
import com.jinhe.tss.um.service.IMessageService;

/**
 * 站内消息相关模块的单元测试
 */
public class MessageModuleTest extends TxSupportTest4UM {
    
	@Autowired MessageAction action;
    @Autowired IMessageService service;
 
    @Test
    public void testMesseag() {
    	Message message = new Message();
    	
        message.setTitle("好消息");
        message.setContent("好消息！");
        message.setReceiverIds("-1");
        message.setReceiver("Admin");
        action.sendMessage(response, message);
        
        List<Message> inboxList = service.getInboxList();
        assertTrue(inboxList.size() > 0);
        Long messageId = inboxList.get(0).getId();
        
        action.getMessage4Reply(response, messageId);
        
        action.viewMessage(response, messageId);
        
        action.getMessageList(response, 2);
        action.getMessageList(response, 3);
        action.getMessageList(response, 4);
        
        action.deleteMessage(response, messageId);
 
    }

}
