package com.jinhe.tss.um.syncdata;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URL;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
public class SyncDataTest extends TxSupportTest4UM {

	@Autowired GroupAction groupAction;
	
	@Autowired ISyncService syncService;
	@Autowired IResourceService resourceService;
 
//	@Test
	public void testSyncLDAP() {
		// 准备数据
		
		Group mainGroup = new Group();
		mainGroup.setParentId(UMConstants.MAIN_GROUP_ID);
		mainGroup.setName("他山石");
		mainGroup.setGroupType(Group.MAIN_GROUP_TYPE);
		mainGroup.setDbGroupId("");
		
		groupService.createNewGroup(mainGroup, "", "-1");
		log.debug(mainGroup + "\n");
		Long mainGroupId = mainGroup.getId();

		Application application = resourceService.getApplication(UMConstants.TSS_APPLICATION_ID);
        assertNotNull(application);
        application.setDataSourceType(UMConstants.DATA_SOURCE_TYPE_LDAP);
        URL template = URLUtil.getResourceFileUrl("template/syscdata/template_LDAP.xml");
        String paramDesc = FileHelper.readFile(new File(template.getPath()));
        application.setParamDesc(paramDesc);
        
        String applicationId = application.getApplicationId();
		String dbGroupId = mainGroup.getDbGroupId();
		syncService.getCompleteSyncGroupData(mainGroupId, applicationId , dbGroupId);
	}
	
	@Test
	public void testSyncDB() {
		
	}

	@Test
	public void syncData() {
		// 测试用户同步。TODO 进度条需要单独起线程，里面没有事务。
//		 groupAction.syncData(response, applicationId, groupId, mode);
	}
}
