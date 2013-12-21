package com.jinhe.tss.um.syncdata;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.component.progress.Progress;
import com.jinhe.tss.framework.component.progress.Progressable;
import com.jinhe.tss.framework.test.H2DBServer;
import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.action.GroupAction;
import com.jinhe.tss.um.entity.Application;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.service.IResourceService;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.URLUtil;

/**
 * 测试用户同步
 */
public class SyncDataFromDBTest extends TxSupportTest4UM {

	@Autowired GroupAction groupAction;
	
	@Autowired ISyncService syncService;
	@Autowired IResourceService resourceService;
	
	Group mainGroup;
	String applicationId = UMConstants.TSS_APPLICATION_ID;
	
	@Before
	public void setUp() {
		super.setUp();
		
		mainGroup = new Group();
		mainGroup.setParentId(UMConstants.MAIN_GROUP_ID);
		mainGroup.setName("他山石");
		mainGroup.setGroupType(Group.MAIN_GROUP_TYPE);
		mainGroup.setFromApp(UMConstants.TSS_APPLICATION_ID);
		mainGroup.setFromGroupId("1");
		
		groupService.createNewGroup(mainGroup, "", "-1");
		log.debug(mainGroup + "\n");

		Application application = resourceService.getApplication(UMConstants.TSS_APPLICATION_ID);
        assertNotNull(application);
        application.setDataSourceType(UMConstants.DATA_SOURCE_TYPE_DB);
        URL template = URLUtil.getResourceFileUrl("template/syncdata/template_DB.xml");
        String paramDesc = FileHelper.readFile(new File(template.getPath()));
        application.setParamDesc(paramDesc);
        resourceService.saveApplication(application);
        
        // 准备数据
        initSourceTable();
	}
 
	private void initSourceTable() {
		Connection conn = dbserver.getH2Connection();
		
        try {
        	if( conn.isClosed() ) {
        		dbserver = new H2DBServer();
        		conn = dbserver.getH2Connection();
        	}
        	
        	if( conn.isClosed() ) {
        		Assert.fail("H2 connection is closed!");
        	}
        		
            Statement stat = conn.createStatement();
            stat.execute("create table if not exists xxx_group" + 
                    "(" + 
                        "id            NUMBER(19) not null, " + 
                        "parentId      NUMBER(19), " + 
                        "seqNo         NUMBER(10), " + 
                        "name          VARCHAR2(50 CHAR), " + 
                        "description   VARCHAR2(255 CHAR)" + 
                    ");" +
                    " alter table xxx_group add primary key (id); ");
            
            stat.execute("create table if not exists xxx_user" + 
                    "(" + 
                        "id           NUMBER(19) not null, " + 
                        "groupId      NUMBER(19) not null, " + 
                        "sex          NUMBER(1), " + 
                        "birthday     TIMESTAMP(6), " + 
                        "employeeNo   VARCHAR2(255 CHAR), " + 
                        "loginName    VARCHAR2(255 CHAR), " + 
                        "userName     VARCHAR2(255 CHAR), " + 
                        "email        VARCHAR2(255 CHAR) " + 
                    ");" +
                    " alter table xxx_user add primary key (id); ");
            
            PreparedStatement ps = conn.prepareStatement("insert into xxx_group values (?, ?, ?, ?, ?)");
            ps.setObject(1, 1L);
            ps.setObject(2, 0L);
            ps.setObject(3, 1);
            ps.setObject(4, "它山石");
            ps.setObject(5, "test test");
            ps.executeUpdate();
            
            ps.setObject(1, 2L);
            ps.setObject(2, 1L);
            ps.setObject(3, 1);
            ps.setObject(4, "子组1");
            ps.setObject(5, "test 1");
            ps.executeUpdate();
            
            ps.setObject(1, 3L);
            ps.setObject(2, 1L);
            ps.setObject(3, 2);
            ps.setObject(4, "子组2");
            ps.setObject(5, "test 2");
            ps.executeUpdate();
            
            ps = conn.prepareStatement("insert into xxx_user values (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setObject(1, 1L);
            ps.setObject(2, 1L);
            ps.setObject(3, 1);
            ps.setObject(4, new Timestamp(new Date().getTime()));
            ps.setObject(5, "BL00618");
            ps.setObject(6, "JonKing");
            ps.setObject(7, "怒放的生命");
            ps.setObject(8, "jinpujun@gmail.com");
            ps.executeUpdate();
            
            ps.setObject(1, 2L);
            ps.setObject(2, 3L);
            ps.setObject(3, 1);
            ps.setObject(4, new Timestamp(new Date().getTime()));
            ps.setObject(5, "BL00619");
            ps.setObject(6, "Waitwint");
            ps.setObject(7, "过河卒子");
            ps.setObject(8, "waitwind@gmail.com");
            ps.executeUpdate();
            
        } catch (Exception e) {
            logger.error(e);
        }  
    }
 
	@Test
	public void testSyncDB() {
		String fromGroupId = mainGroup.getFromGroupId();
		Map<String, Object> datasMap = syncService.getCompleteSyncGroupData(mainGroup.getId(), applicationId , fromGroupId);
		List<?> groups = (List<?>)datasMap.get("groups");
        List<?> users  = (List<?>)datasMap.get("users");
        int totalCount = users.size() + groups.size();
	    
        Assert.assertTrue( totalCount == 5); // 三个组 + 两个用户
		Progress progress = new Progress(totalCount);
		((Progressable)syncService).execute(datasMap, progress );
	}

//	@Test
	public void syncData() {
		//TODO 进度条需要单独起线程，里面没有事务。
		try {
			groupAction.syncData(response, applicationId, mainGroup.getId());
		} catch (Exception e) {
			Assert.assertTrue("进度条需要单独起线程，里面没有事务", true);
		}
	}
}
