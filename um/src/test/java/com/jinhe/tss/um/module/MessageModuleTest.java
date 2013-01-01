package com.jinhe.tss.um.module;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.action.MessageAction;
import com.jinhe.tss.um.entity.Message;
import com.jinhe.tss.um.service.IMessageService;

/**
 * 站内消息相关模块的单元测试
 */
public class MessageModuleTest extends TxSupportTest4UM {
    
	MessageAction action;
    
    @Autowired IMessageService service;
    
    public void setUp() throws Exception {
        super.setUp();
        
        action = new MessageAction();
        action.setService(service);
        
        // 初始化虚拟登录用户信息
        login(UMConstants.ADMIN_USER_ID, UMConstants.ADMIN_USER_NAME);
    }
    
    public void testCRUD() {
        action.getMessageInfo();
        
        action.getMessage().setTitle("好消息");
        action.getMessage().setContent("好消息！");
        action.getMessage().setReceiverIds("-1");
        action.getMessage().setReceiver("Admin");
        action.setMode("save");
        action.saveMessage();
        
        action.setMode("");
        action.saveMessage();
        
        action.setId(action.getMessage().getId());
        
        List<Message> inboxList = service.getInboxList();
        assertTrue(inboxList.size() > 0);
        action.setId(inboxList.get(0).getId());
        action.setType("reply");
        action.getMessageInfo();
        
        action.setType("forward");
        action.getMessageInfo();
        
        action.viewMessage();
        
        action.setBoxId(2);
        action.getMessageList();
        action.setBoxId(3);
        action.getMessageList();
        action.setBoxId(4);
        action.getMessageList();
        
        action.deleteMessage();
        
        action.getCondition().setGroupId(UMConstants.MAIN_GROUP_ID);
        action.searchUsers();
        action.getSearchUserInfo();
        
        action.getGroupTree();
    }

}
