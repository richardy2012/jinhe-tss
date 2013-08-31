package com.jinhe.tss.um.syncdata;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.action.GroupAction;

public class SyncdataTest extends TxSupportTest4UM {
	
	@Autowired GroupAction groupAction;
	@Autowired ISyncService syncService;
	
	@Test
	public void testSyncData() {
		// TODO
//		syncService.getCompleteSyncGroupData(mainGroupId, applicationId, dbGroupId);
//		
//		((Progressable)syncService).execute(params, progress);
	}

    @Test
    public void syncData() {
        // 测试用户同步。TODO 进度条需要单独起线程，里面没有事务。
//        groupAction.syncData(response, applicationId, groupId, mode);
    }
}
