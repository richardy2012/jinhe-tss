package com.jinhe.tss.um.syncdata;

import org.apache.directory.server.annotations.CreateLdapServer;
import org.apache.directory.server.annotations.CreateTransport;
import org.apache.directory.server.core.integ.AbstractLdapTestUnit;
import org.apache.directory.server.core.integ.FrameworkRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.um.service.IResourceService;

/**
 * 测试用户同步
 */
@RunWith(FrameworkRunner.class)
@CreateLdapServer(transports = { @CreateTransport(protocol = "LDAP"),
		@CreateTransport(protocol = "LDAPS") })
public class SyncDataFromLDAPTest extends AbstractLdapTestUnit {

	ISyncService syncService;
	IResourceService resourceService;
	
	@Before
	public void setUp() {
		syncService = (ISyncService) Global.getContext().getBean("SyncData");
		resourceService = (IResourceService) Global.getContext().getBean("ResourceService");;
	}
 
	@Test
	public void testSyncLDAP() {
		// 准备数据
		
//		Group mainGroup = new Group();
//		mainGroup.setParentId(UMConstants.MAIN_GROUP_ID);
//		mainGroup.setName("他山石");
//		mainGroup.setGroupType(Group.MAIN_GROUP_TYPE);
//		mainGroup.setDbGroupId("");
//		
//		groupService.createNewGroup(mainGroup, "", "-1");
//		log.debug(mainGroup + "\n");
//		Long mainGroupId = mainGroup.getId();
//
//		Application application = resourceService.getApplication(UMConstants.TSS_APPLICATION_ID);
//        assertNotNull(application);
//        application.setDataSourceType(UMConstants.DATA_SOURCE_TYPE_LDAP);
//        URL template = URLUtil.getResourceFileUrl("template/syscdata/template_LDAP.xml");
//        String paramDesc = FileHelper.readFile(new File(template.getPath()));
//        application.setParamDesc(paramDesc);
//        resourceService.saveApplication(application);
//        
//        String applicationId = application.getApplicationId();
//		String dbGroupId = mainGroup.getDbGroupId();
//		syncService.getCompleteSyncGroupData(mainGroupId, applicationId , dbGroupId);
	}
}
