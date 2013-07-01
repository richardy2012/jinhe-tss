package com.jinhe.tss.um.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.dao.IApplicationDao;
import com.jinhe.tss.um.dao.IResourceTypeDao;
import com.jinhe.tss.um.entity.Application;
import com.jinhe.tss.um.entity.Operation;
import com.jinhe.tss.um.entity.ResourceType;
import com.jinhe.tss.um.entity.ResourceTypeRoot;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.um.permission.PermissionService;
import com.jinhe.tss.um.service.IApplicationService;

@Service("ApplicationService")
public class ApplicationService implements IApplicationService{
	
	@Autowired private IApplicationDao    applicationDao;
	@Autowired private IResourceTypeDao   resourceTypeDao;
	@Autowired private PermissionService  permissionService;

    public Object[] findApplicationAndResourceType() {
        // 应用系统列表
        List<?> apps = getApplications();
        
        // 资源类型列表
        List<?> resourceTypes = resourceTypeDao.getEntities("from ResourceType o order by o.seqNo");    
        
        // 权限选项列表
        List<?> operations = resourceTypeDao.getEntities("from Operation o order by o.seqNo");    
        
        return new Object[]{apps, resourceTypes, operations};
    }

	public Application getApplication(String applicationId){
		return applicationDao.getApplication(applicationId);
	}
    
    public Application getApplicationById(Long id) {
        return applicationDao.getEntity(id);
    }

    public ResourceType getResourceTypeById(Long id) {
        return resourceTypeDao.getEntity(id);
    }

    public ResourceTypeRoot findResourceTypeRoot(String applicationId, String resourceTypeId){
        return resourceTypeDao.getResourceTypeRoot(applicationId, resourceTypeId);
    }
    
    public Operation getOperationById(Long id) {
        return (Operation) resourceTypeDao.getEntity(Operation.class, id);
    }

	public void removeApplication(Long id) {
		Application application = applicationDao.getEntity(id);
		applicationDao.clearDirtyData(application.getApplicationId());		
	}

	public void removeResourceType(Long id) {
        ResourceType resourceType = resourceTypeDao.getEntity(id);
		resourceTypeDao.delete(resourceType);	
		
        // 删除Operation表
		List<?> operationList = resourceTypeDao.getEntities("from Operation o where o.resourceTypeId = ?", resourceType.getResourceTypeId());
		for(Object obj : operationList) {
		    removeOperation(((Operation)obj).getId());
		}
	}

	public void removeOperation(Long id) {
		Operation operation = getOperationById(id);
		resourceTypeDao.delete( operation );
	        
        // 删除权限选项, 连同删除RoleResourceOperation表中相关数据 
        ResourceType resourceType = resourceTypeDao.getResourceType(operation.getApplicationId(), operation.getResourceTypeId());
        String suppliedTable = resourceType.getSuppliedTable();
        String unSuppliedTable = resourceType.getUnSuppliedTable();
        resourceTypeDao.deleteAll(resourceTypeDao.getEntities("from " + suppliedTable   + " o where o.operationId = ?", operation.getOperationId()));
        resourceTypeDao.deleteAll(resourceTypeDao.getEntities("from " + unSuppliedTable + " o where o.operationId = ?", operation.getOperationId()));
	}
    
	public void saveApplication(Application application) {
		if(null == application.getId()){ // 新建
			applicationDao.create(application);
		} 
		else {
			applicationDao.update(application);
		}
	}

    public Object createResourceType(ResourceType resourceType){
        resourceTypeDao.create(resourceType);
		
		// 保存一个应用系统中一种类型的根节点
		ResourceTypeRoot resourceTypeRoot = new ResourceTypeRoot();
		resourceTypeRoot.setApplicationId(resourceType.getApplicationId());
		resourceTypeRoot.setResourceTypeId(resourceType.getResourceTypeId());
		resourceTypeRoot.setRootId(resourceType.getRootId());
		resourceTypeDao.createObject(resourceTypeRoot);
		
		return resourceType;
	}
    
    public Object updateResourceType(ResourceType resourceType){
        String applicationId = resourceType.getApplicationId();
        String resourceTypeId = resourceType.getResourceTypeId();
        ResourceTypeRoot resourceTypeRoot = resourceTypeDao.getResourceTypeRoot(applicationId, resourceTypeId);
        if(null != resourceTypeRoot){
            resourceTypeRoot.setRootId(resourceType.getRootId());
        }
        
        resourceTypeDao.update(resourceType);
        return resourceType;
    }
    
    public Operation saveOperation(Operation operation){
    	resourceTypeDao.createObject(operation);
        String applicationId = operation.getApplicationId();
        String resourceTypeId = operation.getResourceTypeId();
        
        ResourceTypeRoot resourceTypeRoot = resourceTypeDao.getResourceTypeRoot(applicationId, resourceTypeId);
        if( resourceTypeRoot != null ){
            String unSuppliedTable = resourceTypeDao.getUnSuppliedTable(applicationId, resourceTypeId);
            String suppliedTable = resourceTypeDao.getSuppliedTable(applicationId, resourceTypeId);
            String resourceTable = resourceTypeDao.getResourceTable(applicationId, resourceTypeId);
            
            permissionService = PermissionHelper.getPermissionService(applicationId, permissionService);
            
            // 新建的权限选项要将该权限选项赋予管理员角色(id==-1)
            permissionService.saveRoleResourceOperation(UMConstants.ADMIN_ROLE_ID, resourceTypeRoot.getRootId(), 
                    operation.getOperationId(), UMConstants.PERMIT_SUB_TREE, unSuppliedTable, suppliedTable, resourceTable);
        }   
        return operation;
    }
    
    public void updateOperation(Operation operation) {
    	resourceTypeDao.update(operation);
    }

    public List<?> getApplications() {
    	return applicationDao.getEntities("from Application o order by o.id");
    }
}
	