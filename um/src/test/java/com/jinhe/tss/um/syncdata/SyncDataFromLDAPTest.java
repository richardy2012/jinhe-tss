package com.jinhe.tss.um.syncdata;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.schema.SchemaManager;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.partition.Partition;
import org.apache.directory.server.ldap.LdapServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.component.progress.Progress;
import com.jinhe.tss.framework.component.progress.Progressable;
import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.entity.Application;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.ldap.ApacheDS;
import com.jinhe.tss.um.ldap.LdapUtils;
import com.jinhe.tss.um.service.IResourceService;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.URLUtil;

/**
 * 测试用户同步
 */
public class SyncDataFromLDAPTest extends TxSupportTest4UM {

	@Autowired ISyncService syncService;
	@Autowired IResourceService resourceService;

	ApacheDS apacheDS;
	LdapServer server;
	DirectoryService service;
 
	@Before
	public void setUp() {
		super.setUp();
		
		apacheDS = new ApacheDS();
		try {
			server = apacheDS.startServer();
		} catch (Exception e) {
			log.error("start apache LDAP error:", e);
		} 
		service = server.getDirectoryService();
	}

	@After
	public void teardown() throws Exception {
		apacheDS.destroy();
		super.tearDown();
	}
 
	@Test
	public void testSyncLDAP() {
		try {
			prepareData();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		Group mainGroup = new Group();
		mainGroup.setParentId(UMConstants.MAIN_GROUP_ID);
		mainGroup.setName("金禾");
		mainGroup.setGroupType(Group.MAIN_GROUP_TYPE);
		mainGroup.setDbGroupId("o=JinHe");
		
		groupService.createNewGroup(mainGroup, "", "-1");
		Long mainGroupId = mainGroup.getId();
		log.debug(mainGroup + "\n");

		Application application = resourceService.getApplication(UMConstants.TSS_APPLICATION_ID);
        Assert.assertNotNull(application);
        application.setDataSourceType(UMConstants.DATA_SOURCE_TYPE_LDAP);
        
        URL template = URLUtil.getResourceFileUrl("template/syncdata/template_LDAP.xml");
        String paramDesc = FileHelper.readFile(new File(template.getPath()));
        application.setParamDesc(paramDesc);
        
        resourceService.saveApplication(application);
        
        String applicationId = application.getApplicationId();
		String dbGroupId = mainGroup.getDbGroupId();
		Map<String, Object> datasMap = syncService.getCompleteSyncGroupData(mainGroupId, applicationId , dbGroupId);
		List<?> groups = (List<?>)datasMap.get("groups");
        List<?> users  = (List<?>)datasMap.get("users");
        int totalCount = users.size() + groups.size();
        
        for(Object temp : groups) {
        	log.debug(temp);
        }
        for(Object temp : users) {
        	log.debug(temp);
        }
	    
        log.debug("totalCount = " + totalCount);
		Progress progress = new Progress(totalCount);
		((Progressable)syncService).execute(datasMap, progress );
	}
	
	private void prepareData() throws Exception {
    	SchemaManager schemaManager = service.getSchemaManager();
    	Partition store = apacheDS.addPartition("TSS", "o=JinHe", false);
    	
        Dn suffixDn = new Dn( schemaManager, "o=JinHe" );
        long index = 1L;

        // 公司
        Entry entry = new DefaultEntry( schemaManager, suffixDn,
            "objectClass: organization",
            "o: JinHe",
            "postalCode: 1",
            "postOfficeBox: 1" );
        LdapUtils.injectEntryInStore( store, entry, index++ );

        // 销售部
        Dn dn = new Dn( schemaManager, "ou=Sales,o=JinHe" );
        entry = new DefaultEntry( schemaManager, dn,
            "objectClass: top",
            "objectClass: organizationalUnit",
            "ou: Sales",
            "postalCode: 1",
            "postOfficeBox: 1" );
        LdapUtils.injectEntryInStore( store, entry, index++ );

        // 研发部
        dn = new Dn( schemaManager, "ou=RD2,o=JinHe" );
        entry = new DefaultEntry( schemaManager, dn,
            "objectClass: top",
            "objectClass: organizationalUnit",
            "ou: RD2",
            "postalCode: 2",
            "postOfficeBox: 2" );
        LdapUtils.injectEntryInStore( store, entry, index++ );

        // RD2下的IT部
        dn = new Dn( schemaManager, "ou=IT,ou=RD2,o=JinHe" );
        entry = new DefaultEntry( schemaManager, dn,
            "objectClass: top",
            "objectClass: organizationalUnit",
            "ou: IT",
            "postalCode: 3",
            "description: test and test!" );
        LdapUtils.injectEntryInStore( store, entry, index++ );

        // 销售部员工
        dn = new Dn( schemaManager, "cn=JIM BEAN,ou=Sales,o=JinHe" );
        entry = new DefaultEntry( schemaManager, dn,
            "objectClass: top",
            "objectClass: person",
            "objectClass: organizationalPerson",
            "ou: Sales",
            "cn: JIM BEAN",
            "SN: JIM");
        LdapUtils.injectEntryInStore( store, entry, index++ );
        
        // RD2的员工
        dn = new Dn( schemaManager, "cn=Jack Daniels,ou=RD2,o=JinHe" );
        entry = new DefaultEntry( schemaManager, dn,
            "objectClass: top",
            "objectClass: person",
            "objectClass: organizationalPerson",
            "ou: RD2",
            "cn: Jack Daniels",
            "SN: Jack",
            "uid: Jack");
        LdapUtils.injectEntryInStore( store, entry, index++ );
        
        Dn adminDn = new Dn( schemaManager, "cn=admin2,ou=RD2,o=JinHe" );
        entry = new DefaultEntry( schemaManager, adminDn,
            "objectClass: top",
            "objectClass: person",
            "objectClass: organizationalPerson",
            "ou: RD2",
            "cn: admin2",
            "SN: admin2",
            "userPassword: 123456");
        LdapUtils.injectEntryInStore( store, entry, index++ );
    }
}
