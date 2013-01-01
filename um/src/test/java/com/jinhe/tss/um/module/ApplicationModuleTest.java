package com.jinhe.tss.um.module;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.action.ApplicationResourceAction;
import com.jinhe.tss.um.entity.Application;
import com.jinhe.tss.um.service.IApplicationService;
import com.jinhe.tss.util.BeanUtil;

/**
 * 系统、资源、权限项相关模块的单元测试
 */
public class ApplicationModuleTest extends TxSupportTest4UM {
    
    ApplicationResourceAction action;
    
    @Autowired IApplicationService service;
    
    public void setUp() throws Exception {
        super.setUp();
        
        action = new ApplicationResourceAction();
        action.setApplicationService(service);
        
        // 初始化虚拟登录用户信息
        login(UMConstants.ADMIN_USER_ID, UMConstants.ADMIN_USER_NAME);
    }
    
    public void testCRUD() {
        action.setApplicationType(UMConstants.PLATFORM_SYSTEM_APP);
        action.setAppId(-10L);
        action.getApplicationInfo();
        action.setAppId(1L);
        action.getApplicationInfo();
        
        Application application = service.getApplication("tss");
        assertNotNull(application);
        
        Application application2 = new Application();
        BeanUtil.copy(application2, application);
        application2.setId(null);
        application2.setApplicationId("tss2");
        application2.setName("TSS2");
        service.saveApplication(application2);
        
        action.getAllApplication2Tree();
        
        action.setAppId(1L);
        action.setToAppId(application2.getId());
        action.setDirection(1);
        action.sortApplication();
        
        List<?> apps = service.getApplications();
        assertTrue(apps.size() >= 2);
        
        action.setTypeId(-10L);
	    action.getResourceTypeInfo();
        action.setTypeId(1L);
	    action.getResourceTypeInfo();
	    
	    action.setApplicationId("tss");
	    action.setResourceTypeId("1");
	    action.getResourcesByResourceTypeId();
	    
        action.setOperationId(-10L);
	    action.getOperationInfo();
        action.setOperationId(2L);
        action.getOperationInfo();
        action.deleteOperation();
        
        action.setApplicationId("tss");
        action.getOperationList();
        action.setTypeId(1L);
        action.deleteResourceType();
        
        action.setAppId(application2.getId());
        action.deleteApplication();
    }

}
