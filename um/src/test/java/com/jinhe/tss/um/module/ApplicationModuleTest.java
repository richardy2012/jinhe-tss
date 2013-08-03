package com.jinhe.tss.um.module;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.um.TxSupportTest4UM;
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
  
    @Test
    public void testApplicationModule() {
        
        Application application = service.getApplication("tss");
        assertNotNull(application);
        
        action.getApplicationInfo(response, application.getId());
        
        Application application2 = new Application();
        BeanUtil.copy(application2, application);
        application2.setId(null);
        application2.setApplicationId("tss2");
        application2.setName("TSS2");
        service.saveApplication(application2);
        
        action.getAllApplication2Tree(response);
        
        List<?> apps = service.getApplications();
        assertTrue(apps.size() >= 2);
        
        action.getResourceTypeInfo(response, 1L);

        action.editApplication(response, application);
        
        ResourceType resourceType = service.getResourceTypeById(1L);
        action.editResourceType(response, resourceType);
        
        action.editOperation(response, service.getOperationById(1L));
        
        action.getOperationInfo(response, 1L);
        action.deleteOperation(response, 1L);
        
        action.deleteResourceType(response, 1L);
        
        action.deleteApplication(response, 1L);
    }

}
