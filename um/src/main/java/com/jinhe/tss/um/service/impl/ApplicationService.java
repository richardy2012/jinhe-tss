package com.jinhe.tss.um.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.dao.IApplicationDao;
import com.jinhe.tss.um.dao.IResourceTypeDao;
import com.jinhe.tss.um.entity.Application;
import com.jinhe.tss.um.entity.Operation;
import com.jinhe.tss.um.entity.ResourceType;
import com.jinhe.tss.um.entity.ResourceTypeRoot;
import com.jinhe.tss.um.entity.permission.resources.ApplicationResources;
import com.jinhe.tss.um.entity.permission.supplied.ApplicationPermissionsFull;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.um.permission.PermissionService;
import com.jinhe.tss.um.permission.dispaly.ResourceTreeNode;
import com.jinhe.tss.um.service.IApplicationService;

public class ApplicationService implements IApplicationService{
	
	@Autowired private IApplicationDao    applicationDao;
	@Autowired private IResourceTypeDao   resourceTypeDao;
	@Autowired private PermissionService  permissionService;

    public Object[] findApplicationAndResourceType() {
        // 应用系统列表
        List<?> apps = getApplications();
        if( apps == null || apps.size() == 0 ) {
            return new Object[]{apps, new ArrayList<Object>(), new ArrayList<Object>()};
        }
        
        // 资源类型列表
        List<String> applicationIds = new ArrayList<String>();
        for(Object obj : apps){
            applicationIds.add(((Application) obj).getApplicationId());
        }
        String hql = "from ResourceType o where o.applicationId in (:applicationIds)  order by o.seqNo";
        List<?> resourceTypes = resourceTypeDao.getEntities(hql, new Object[]{"applicationIds"}, new Object[]{applicationIds});    
        if(resourceTypes == null || resourceTypes.size() == 0) {
            return new Object[]{apps, new ArrayList<Object>(), new ArrayList<Object>()};
        }
        
        // 权限选项列表
        List<String> resourceTypeIds = new ArrayList<String>();
        for(Object obj : resourceTypes){
            resourceTypeIds.add(((ResourceType) obj).getResourceTypeId());
        }
        hql = "from Operation o where o.resourceTypeId in (:resourceTypeIds) order by o.seqNo";
        List<?> operations = resourceTypeDao.getEntities(hql, new Object[]{"resourceTypeIds"}, new Object[]{resourceTypeIds});    
        
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

	public List<ResourceTreeNode> findResoucrcesByResourceType(String resourceTypeId, String applicationId) {
	    String resourceTable = resourceTypeDao.getResourceTable(applicationId, resourceTypeId);
        String hql = "select distinct o.id, o.parentId, o.name, o.decode from " + resourceTable + " o order by o.decode";
        List<?> resourcesTree = resourceTypeDao.getEntities(hql);  
		return ResourceTreeNode.genResourceTreeNodeList(resourcesTree);
	}
    
	public void saveApplication(Application application) {
		if(null == application.getId()){ // 新建
			Integer nextSeqNo = applicationDao.getNextSeqNo(ApplicationResources.class.getName(), application.getParentId());
			application.setSeqNo(nextSeqNo);
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
 
    public List<?> getOperationsByResourceId(Long appId){
        String suppliedTable = ApplicationPermissionsFull.class.getName();
		return PermissionHelper.getInstance().getOperationsByResource(appId, suppliedTable, ApplicationResources.class);
    }
    
    public void sortApplication(Long appId, Long toAppId, int direction, Long userId){
    	applicationDao.sort(appId, toAppId, direction);
    }
    
    public List<?> getApplications() {
    	return applicationDao.getEntities("from Application o order by o.decode");
    }
}
	