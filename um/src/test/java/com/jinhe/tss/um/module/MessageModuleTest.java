package com.jinhe.tss.um.module;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.action.MessageAction;
import com.jinhe.tss.um.entity.Message;
import com.jinhe.tss.um.helper.UMQueryCondition;
import com.jinhe.tss.um.service.IMessageService;

/**
 * 站内消息相关模块的单元测试
 */
public class MessageModuleTest extends TxSupportTest4UM {
    
	@Autowired MessageAction action;
    
    @Autowired IMessageService service;
    
    public void setUp() throws Exception {
        super.setUp();
        
        // 初始化虚拟登录用户信息
        login(UMConstants.ADMIN_USER_ID, UMConstants.ADMIN_USER_NAME);
    }
    
    public void testMesseag() {
    	Message message = new Message();
    	
        message.setTitle("好消息");
        message.setContent("好消息！");
        message.setReceiverIds("-1");
        message.setReceiver("Admin");
        action.sendMessage(message);
        
        List<Message> inboxList = service.getInboxList();
        assertTrue(inboxList.size() > 0);
        Long messageId = inboxList.get(0).getId();
        
        action.getMessage4Reply(messageId);
        
        action.viewMessage(messageId);
        
        action.getMessageList(2);
        action.getMessageList(3);
        action.getMessageList(4);
        
        action.deleteMessage(messageId);
        
        UMQueryCondition condition = new UMQueryCondition();
        condition.setGroupId(UMConstants.MAIN_GROUP_ID);
        action.searchUsers(condition);
        
        action.getSearchUserInfo();
        action.getGroupTree();
    }

}
