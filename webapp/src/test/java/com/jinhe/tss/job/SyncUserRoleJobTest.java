package com.jinhe.tss.job;

import org.junit.Assert;
import org.junit.Test;

import com.jinhe.dm.DMConstants;
import com.jinhe.dm.data.sqlquery.SQLExcutor;
import com.jinhe.tss.util.EasyUtils;

public class SyncUserRoleJobTest {

	@Test
	public void testJob() {
		
		SQLExcutor.excute("delete from um_roleuser where userId=123", DMConstants.LOCAL_CONN_POOL);
        
        String jobConfig =  "select 123 user, '102,103,104' role1, '121,122,104' as role2 from dual where now() > ?@connectionpool";
        
		try {
			SyncUserRoleJob job = new SyncUserRoleJob();
        	job.excuteJob(jobConfig);
        	
		} catch(Exception e) {
			e.printStackTrace();
			Assert.assertFalse(true);
        }
		
		String sql = "select count(*) num from um_roleuser where userId = ?";
		Object result = SQLExcutor.query(DMConstants.LOCAL_CONN_POOL, sql, 123L).get(0).get("num");
		Assert.assertTrue( EasyUtils.obj2Int(result) > 0 );
	}
	
}
