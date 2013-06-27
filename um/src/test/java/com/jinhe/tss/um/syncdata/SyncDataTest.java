package com.jinhe.tss.um.syncdata;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.service.IGroupService;

/**
 * 同步数据测试
 */
public class SyncDataTest extends TxSupportTest4UM {
	
	@Autowired SyncAction action;
    
    @Autowired ISyncService service;
    @Autowired IGroupService groupService;
    
    public void setUp() throws Exception {
        super.setUp();
        
        // 初始化虚拟登录用户信息
        login(UMConstants.ADMIN_USER_ID, UMConstants.ADMIN_USER_NAME);
    }
    
    public void testSyncData() {
 
    }
}
