package com.jinhe.tss.um.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.persistence.IEntity;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.dao.IApplicationDao;
import com.jinhe.tss.um.dao.IResourceTypeDao;
import com.jinhe.tss.um.entity.Application;
import com.jinhe.tss.um.entity.Operation;
import com.jinhe.tss.um.entity.ResourceType;
import com.jinhe.tss.um.entity.ResourceTypeRoot;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.um.permission.PermissionService;
import com.jinhe.tss.um.service.IResourceRegisterService;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.XMLDocUtil;
 
public class ResourceRegisterService implements IResourceRegisterService{
	
	@Autowired private IApplicationDao    applicationDao;
	@Autowired private IResourceTypeDao   resourceTypeDao;
	@Autowired private PermissionService  permissionService;
	
	/**
	 * 如果是UMS在进行初始化操作，则permissionService取applicationContext.xml里配置的UMS本地PermissionService
     * 否则，permissionService取各应用里配置的PermissionService，比如导入CMS资源配置文件时，则取CMS的PermissionService
	 */
	private boolean initial = false; 
	public void setInitial(boolean initial) { this.initial = initial; }
	
    public void applicationResourceRegister(Document doc, String applicationType) {
       
        List<ResourceType> resourceTypeList = new ArrayList<ResourceType>();
        List<ResourceTypeRoot> resourceTypeRootList = new ArrayList<ResourceTypeRoot>();
        List<Operation> operationList = new ArrayList<Operation>();
        
        // 解析应用
        Application application = new Application();
        Element appNode = (Element) doc.selectSingleNode("/application");
        BeanUtil.setDataToBean(application, XMLDocUtil.dataNode2Map(appNode));
        
        String applicationId = application.getApplicationId();
        
        // 解析资源类型
        List<Element> nodeList = XMLDocUtil.selectNodes(appNode, "resourceType");
        for (Element resourceTypeNode : nodeList) {
            ResourceType resourceType = new ResourceType();
            BeanUtil.setDataToBean(resourceType, XMLDocUtil.dataNode2Map(resourceTypeNode));
            resourceType.setApplicationId(applicationId);
            resourceTypeList.add(resourceType);
            
            // 解析资源操作选项
            List<Element> operationNodeList = XMLDocUtil.selectNodes(resourceTypeNode, "operation");
            for (Element operationNode : operationNodeList) {
                Operation operation = new Operation();
                BeanUtil.setDataToBean(operation, XMLDocUtil.dataNode2Map(operationNode));
                operation.setApplicationId(applicationId);
                operation.setResourceTypeId(resourceType.getResourceTypeId());
                operationList.add(operation);
            }
        }
        
        // 解析资源类型根节点
        nodeList = XMLDocUtil.selectNodes(appNode, "resourceTypeRoot");
        for (Element resourceTypeRootNode : nodeList) {
            ResourceTypeRoot resourceTypeRootId = new ResourceTypeRoot();
            BeanUtil.setDataToBean(resourceTypeRootId, XMLDocUtil.dataNode2Map(resourceTypeRootNode));
            resourceTypeRootList.add(resourceTypeRootId);
        }           
        
        if(!EasyUtils.isNullOrEmpty(applicationType)){
            application.setApplicationType(applicationType);            
        }
        
        /*****************************  仅仅把外部资源注册进来,不进行补全操作 *******************************/
        
        // 根据应用删除上次因导入失败产生的脏数据
        applicationDao.clearDirtyData(applicationId);
        
        for(IEntity resourceTypeRoot : resourceTypeRootList) {
        	resourceTypeDao.createObject(resourceTypeRoot); // 初始化资源类型根节点
        }
                  
        for (ResourceType resourceType : resourceTypeList) {
            String resourceTypeId = resourceType.getResourceTypeId();
            ResourceTypeRoot resourceTypeRoot = resourceTypeDao.getResourceTypeRoot(applicationId, resourceTypeId);
            if( resourceTypeRoot != null) {
                resourceType.setRootId(resourceTypeRoot.getRootId());
            }
            resourceTypeDao.create(resourceType); // 初始化资源类型
        }
        
        for (Operation operation : operationList) {     
        	resourceTypeDao.createObject(operation); // 初始化权限选项
        }       
        
        /*****************************  对外部已经注册的资源进行补全操作 ************************************/
        if(!initial){
            permissionService = PermissionHelper.getPermissionService(applicationId, permissionService);      
        }
        
        // 初始化资源类型          
        for (ResourceType resourceType : resourceTypeList) {
            /* 保存资源类型，同时还要为该类型资源建立一个根节点，以资源类型名字作为根节点名字 */  
            String resourceTypeId = resourceType.getResourceTypeId();
            
            String unSuppliedTable = resourceTypeDao.getUnSuppliedTable(applicationId, resourceTypeId);
            String suppliedTable = resourceTypeDao.getSuppliedTable(applicationId, resourceTypeId);
            
            String initPermission = Config.getAttribute("initPermission");
            if(Config.TRUE.equalsIgnoreCase(initPermission)) {
                permissionService.clearPermissionData(unSuppliedTable);
                permissionService.clearPermissionData(suppliedTable);    
            }
        }
        
        // 初始化权限选项
        for (Operation operation : operationList) {         
            /* 让管理员角色拥有新添加的权限选项，即:让管理员拥有对资源 根节点 有permissionState=2的权限 */
            String resourceTypeId = operation.getResourceTypeId();
            ResourceTypeRoot resourceTypeRoot = resourceTypeDao.getResourceTypeRoot(applicationId, resourceTypeId);
            if( resourceTypeRoot != null ) {
                String unSuppliedTable = resourceTypeDao.getUnSuppliedTable(applicationId, resourceTypeId);
                String suppliedTable   = resourceTypeDao.getSuppliedTable(applicationId, resourceTypeId);
                String resourceTable   = resourceTypeDao.getResourceTable(applicationId, resourceTypeId);
                permissionService.saveRoleResourceOperation(UMConstants.ADMIN_ROLE_ID, resourceTypeRoot.getRootId(), 
                        operation.getOperationId(), UMConstants.PERMIT_SUB_TREE, 
                        unSuppliedTable, suppliedTable, resourceTable);
            }
        }
        
        // 初始化平台应用系统，应用系统作为一类资源，需要做补全操作的，所以最后保存
        applicationDao.create(application); 
    }
 
}	