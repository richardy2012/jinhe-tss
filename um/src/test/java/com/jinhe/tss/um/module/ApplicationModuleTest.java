package com.jinhe.tss.um.module;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.action.ApplicationResourceAction;
import com.jinhe.tss.um.entity.Application;
import com.jinhe.tss.um.entity.ResourceType;
import com.jinhe.tss.um.service.IApplicationService;
import com.jinhe.tss.util.BeanUtil;

/**
 * 系统、资源、权限项相关模块的单元测试
 */
public class ApplicationModuleTest extends TxSupportTest4UM {
    
    @Autowired ApplicationResourceAction action;
    @Autowired IApplicationService service;
  
    public void testApplicationModule() {
        
        Application application = service.getApplication("tss");
        assertNotNull(application);
        
        action.getApplicationInfo(application.getId());
        
        Application application2 = new Application();
        BeanUtil.copy(application2, application);
        application2.setId(null);
        application2.setApplicationId("tss2");
        application2.setName("TSS2");
        service.saveApplication(application2);
        
        action.getAllApplication2Tree();
        
        List<?> apps = service.getApplications();
        assertTrue(apps.size() >= 2);
        
        action.getResourceTypeInfo(1L);

        action.editApplication(application);
        
        ResourceType resourceType = service.getResourceTypeById(1L);
        action.editResourceType(resourceType);
        
        action.editOperation(service.getOperationById(1L));
        
        action.getOperationInfo(1L);
        action.deleteOperation(1L);
        
        action.deleteResourceType(1L);
        
        action.deleteApplication(1L);
    }

}
