package com.jinhe.tss.um.syncdata;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.service.IGroupService;

/**
 * 授权信息相关搜索测试
 */
public class SyncDataTest extends TxSupportTest4UM {
	
	SyncAction action;
    
    @Autowired ISyncService service;
    @Autowired IGroupService groupService;
    
    public void setUp() throws Exception {
        super.setUp();
        
        action = new SyncAction();
        action.setSyncService(service);
        action.setGroupService(groupService);
        
        // 初始化虚拟登录用户信息
        login(UMConstants.ADMIN_USER_ID, UMConstants.ADMIN_USER_NAME);
    }
    
    public void testSyncData() {
//    	action.setGroupId(groupId);
//    	action.setMode(mode);
//    	action.setApplicationId(applicationId);
//    	action.syncData();
//    	
//    	action.setUserId(userId);
//    	action.syncUser();
    }
}
