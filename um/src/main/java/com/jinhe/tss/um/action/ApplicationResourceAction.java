package com.jinhe.tss.um.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.entity.Application;
import com.jinhe.tss.um.entity.Operation;
import com.jinhe.tss.um.entity.ResourceType;
import com.jinhe.tss.um.entity.ResourceTypeRoot;
import com.jinhe.tss.um.helper.ApplicationTreeParser;
import com.jinhe.tss.um.service.IApplicationService;
import com.jinhe.tss.util.EasyUtils;

/**
 * 应用资源管理相关Action对象
 */
public class ApplicationResourceAction extends BaseActionSupport {

	private IApplicationService applicationService;
	
	private Long   appId;         //应用ID
	private Long   typeId;        //资源类型ID
	private Long   operationId ;  //操作ID
	private String applicationId; //应用Code
	private String resourceTypeId;//资源类型Code
	private String applicationType;
	private Long   toAppId;
	private int    direction;
    
	private Application  application = new Application();
	private ResourceType resourceType = new ResourceType();
	private Operation    operation = new Operation();
	
	/**
	 * <p>
	 * 获取所有的Applicaton对象并转换成Tree相应的xml数据格式
	 * </p>
	 */
	public String getAllApplication2Tree() {
		Object applications = applicationService.findApplicationAndResourceType();
		TreeEncoder treeEncoder = new TreeEncoder(applications, new ApplicationTreeParser());
		treeEncoder.setNeedRootNode(false);
 
		return print("AppSource", treeEncoder);
	}
	
	/**
	 * <p>
	 * 根据资源类型的id获取资源
	 * </p>
	 */
	public String getResourcesByResourceTypeId() {
		List<?> data = applicationService.findResoucrcesByResourceType(resourceTypeId, applicationId);
		TreeEncoder treeEncoder = new TreeEncoder(data, new LevelTreeParser());
		treeEncoder.setNeedRootNode(false);
		
		return print("SourceTree", treeEncoder);
	}
	
	/**
	 * <p>
	 * 获取一个Application对象的明细信息
	 * </p>
	 * @return String
	 */
	public String getApplicationInfo() {
		XFormEncoder applicationXFormEncoder = null;
		if ( UMConstants.IS_NEW.equals(appId) ) { // 新建应用
		    Map<String, Object> map = new HashMap<String, Object>();
            map.put("applicationType", applicationType);
            if(UMConstants.PLATFORM_SYSTEM_APP.equals(applicationType)) {// 平台应用系统
                applicationXFormEncoder = new XFormEncoder(UMConstants.APPLICATION_XFORM, map);                
            } else { // 其他应用系统
                applicationXFormEncoder = new XFormEncoder(UMConstants.OTHER_APPLICATION_XFORM, map);              
            }
		} else { // 编辑应用
            Application application = applicationService.getApplicationById(appId);
            if(UMConstants.PLATFORM_SYSTEM_APP.equals(application.getApplicationType())){// 平台应用系统
                applicationXFormEncoder = new XFormEncoder(UMConstants.APPLICATION_XFORM, application);                
            }else{// 其他应用系统
                applicationXFormEncoder = new XFormEncoder(UMConstants.OTHER_APPLICATION_XFORM, application);              
            }
		}
		return print("AppDetail", applicationXFormEncoder);
	}
	
	/**
	 * <p>
	 * 获取一个ResourceType对象的明细信息
	 * </p>
	 * @return String
	 */
	public String getResourceTypeInfo() {
		XFormEncoder resourceTypeXFormEncoder = null;
		if ( UMConstants.IS_NEW.equals(typeId) ) {
			//新建资源类型
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("applicationId", applicationId);
			resourceTypeXFormEncoder = new XFormEncoder(UMConstants.RESOURCETYPE_XFORM, map);
		} 
		else {
			//编辑资源类型
			ResourceType resourceType = applicationService.getResourceTypeById(typeId);
            ResourceTypeRoot resourceTypeRoot = applicationService.findResourceTypeRoot(resourceType.getApplicationId(), resourceType.getResourceTypeId());
			if(null != resourceTypeRoot){
				resourceType.setRootId(resourceTypeRoot.getRootId());
			}
			resourceTypeXFormEncoder = new XFormEncoder(UMConstants.RESOURCETYPE_XFORM, resourceType);
		}
		return print("TypeInfo", resourceTypeXFormEncoder);
	}
	
	/**
	 * <p>
	 * 获取一个Operation对象的明细信息
	 * </p>
	 * 
	 * @return String
	 */
	public String getOperationInfo() {
		XFormEncoder operationXFormEncoder = null;
		if ( UMConstants.IS_NEW.equals(operationId) ) {
			// 新建操作选项
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("applicationId", applicationId);
			map.put("resourceTypeId", resourceTypeId);
			operationXFormEncoder = new XFormEncoder(UMConstants.OPERATION_XFORM, map);
		} 
		else {
			// 编辑操作选项,操作选项的id不允许修改
			Operation operation = applicationService.getOperationById(operationId);
			operationXFormEncoder = new XFormEncoder(UMConstants.OPERATION_XFORM, operation);
		}
		return print("PermissionOption", operationXFormEncoder);
	}
	
	/**
	 * <p>
	 * 编辑一个Application对象的明细信息
	 * </p>
	 * 
	 * @return String
	 */
	public String editApplication() {
        boolean isNew = application.getId() == null;
		if( isNew ){// 新建
			// 其他应用系统
			if(UMConstants.OTHER_SYSTEM_APP.equals(application.getApplicationType())) {
				application.setParentId(new Long(UMConstants.OTHER_SYSTEM_APP));
			}
			
			// 平台应用系统
			if(UMConstants.PLATFORM_SYSTEM_APP.equals(application.getApplicationType())) {
				application.setParentId(new Long(UMConstants.PLATFORM_SYSTEM_APP));
			}
		}
        applicationService.saveApplication(application);   
		return doAfterSave(isNew, application, "AppSource");
	}
	
	/**
	 * <p>
	 * 编辑一个ResourceType对象的明细信息
	 * </p>
	 * 
	 * @return String
	 */
	public String editResourceType() {
        boolean isNew = resourceType.getId() == null;
		if( isNew ) { // 新建
			applicationService.createResourceType(resourceType);			
		}
		else{ // 编辑
			applicationService.updateResourceType(resourceType);	
		}
		return doAfterSave(isNew, resourceType, "AppSource");
	}
	
	/**
	 * <p>
	 * 编辑一个Operation对象的明细信息
	 * </p>
	 * 
	 * @return String
	 */
	public String editOperation() {
        boolean isNew = operation.getId() == null;
		if( isNew ) { // 新建，新建的权限选项要将该权限选项赋予管理员角色(id==-1)
			applicationService.saveOperation(operation);
		}
		else { // 编辑
			applicationService.updateOperation(operation);
		}
		return doAfterSave(isNew, operation, "AppSource");
	}
	
	/**
	 * <p>
	 * 删除应用系统
	 * </p>
	 * @return String
	 */
	public String deleteApplication() {
		applicationService.removeApplication(appId);
		return printSuccessMessage();
	}
	
	/**
	 * <p>
	 * 删除资源类型
	 * </p>
	 * @return String
	 */
	public String deleteResourceType() {
		applicationService.removeResourceType(typeId);
        return printSuccessMessage();
	}
	
	/**
	 * <p>
	 * 删除操作选项
	 * </p>
	 * @return String
	 */
	public String deleteOperation() {
		applicationService.removeOperation(operationId);
        return printSuccessMessage();
	}

	/**
	 * <p>
	 * 获得对资源的操作权限选项
	 * </p>
	 * @return
	 */
	public String getOperationList(){
        if( !EasyUtils.isNullOrEmpty(applicationId) ) {
            appId = applicationService.getApplication(applicationId).getId();
        }
		return print("Operation", "p1,p2," + EasyUtils.list2Str(applicationService.getOperationsByResourceId(appId)));
	}
	
	/**
	 * <p>
	 * 对应用系统进行排序
	 * </p>
	 * @return
	 */
	public String sortApplication(){
		applicationService.sortApplication(appId, toAppId, direction, Environment.getOperatorId());
		return printSuccessMessage();
	}
	

	public void setApplicationService(IApplicationService service) {
		this.applicationService = service;
	}

	public void setResourceTypeId(String resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public void setOperationId(Long operationId) {
		this.operationId = operationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public Application getApplication() {
		return application;
	}

	public Operation getOperation() {
		return operation;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void setToAppId(Long toAppId) {
		this.toAppId = toAppId;
	}
}
