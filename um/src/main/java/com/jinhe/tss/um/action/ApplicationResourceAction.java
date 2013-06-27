package com.jinhe.tss.um.action;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jinhe.tss.framework.exception.BusinessException;
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
import com.jinhe.tss.um.service.IResourceRegisterService;
import com.jinhe.tss.util.EasyUtils;

/**
 * 应用资源管理相关Action对象
 */
@Controller
@RequestMapping("ar")
public class ApplicationResourceAction extends BaseActionSupport {

	@Autowired private IApplicationService applicationService;
 
	/**
	 * 获取所有的Applicaton对象并转换成Tree相应的xml数据格式
	 */
	@RequestMapping("/apps")
	public void getAllApplication2Tree() {
		Object applications = applicationService.findApplicationAndResourceType();
		TreeEncoder treeEncoder = new TreeEncoder(applications, new ApplicationTreeParser());
		treeEncoder.setNeedRootNode(false);
 
		print("AppSource", treeEncoder);
	}
	
	/** 获取认证系统 */
	public String getAuthenticateApp() {		
		// 获得登陆用户可访问的应用系统名称列表		
		List<?> apps = applicationService.getApplications();
		String[] appEditor = EasyUtils.generateComboedit(apps, "applicationId", "name", "|");
		
		StringBuffer sb = new StringBuffer();
	    sb.append("<column name=\"authenticateAppId\" caption=\"认证系统\" mode=\"string\" editor=\"comboedit\" ");
	    sb.append(" editorvalue=\"").append(appEditor[0]).append("\" ");
	    sb.append(" editortext=\"") .append(appEditor[1]).append("\"/>");

		return print("AuthenticateApplication", sb);
	}
	
	/**
	 * 根据资源类型的id获取资源
	 */
	@RequestMapping("/resources/{applicationId}/{resourceTypeId}")
	public void getResourcesByResourceTypeId(String resourceTypeId, String applicationId) {
		List<?> data = applicationService.findResoucrcesByResourceType(resourceTypeId, applicationId);
		TreeEncoder treeEncoder = new TreeEncoder(data, new LevelTreeParser());
		treeEncoder.setNeedRootNode(false);
		
		print("SourceTree", treeEncoder);
	}
	
	/**
	 * 获取一个Application对象的明细信息
	 */
	@RequestMapping(value = "/application/{id}", method = RequestMethod.GET)
	public void getApplicationInfo(Long id) {
		XFormEncoder xformEncoder = null;
 
        Application application = applicationService.getApplicationById(id);
        if(UMConstants.PLATFORM_SYSTEM_APP.equals(application.getApplicationType())){ // 平台应用系统
            xformEncoder = new XFormEncoder(UMConstants.APPLICATION_XFORM, application);                
        } else { // 其他应用系统
            xformEncoder = new XFormEncoder(UMConstants.OTHER_APPLICATION_XFORM, application);              
        }
		print("AppDetail", xformEncoder);
	}
	
	/**
	 * 获取一个ResourceType对象的明细信息
	 */
	@RequestMapping(value = "/resource/{id}", method = RequestMethod.GET)
	public String getResourceTypeInfo(Long id) {
		XFormEncoder resourceTypeXFormEncoder = null;
		
		ResourceType resourceType = applicationService.getResourceTypeById(id);
        ResourceTypeRoot resourceTypeRoot = applicationService.findResourceTypeRoot(resourceType.getApplicationId(), resourceType.getResourceTypeId());
		if( resourceTypeRoot != null) {
			resourceType.setRootId(resourceTypeRoot.getRootId());
		}
		resourceTypeXFormEncoder = new XFormEncoder(UMConstants.RESOURCETYPE_XFORM, resourceType);
		return print("TypeInfo", resourceTypeXFormEncoder);
	}
	
	/**
	 * 获取一个Operation对象的明细信息
	 */
	@RequestMapping(value = "/operation/{id}", method = RequestMethod.GET)
	public void getOperationInfo(Long operationId) {
		// 编辑操作选项
		Operation operation = applicationService.getOperationById(operationId);
		XFormEncoder xformEncoder = new XFormEncoder(UMConstants.OPERATION_XFORM, operation);
		print("PermissionOption", xformEncoder);
	}
	
	/**
	 * 编辑一个Application对象的明细信息
	 */
	@RequestMapping(value = "/application", method = RequestMethod.POST)
	public void editApplication(Application application) {
        boolean isNew = application.getId() == null;
        applicationService.saveApplication(application);   
		doAfterSave(isNew, application, "AppSource");
	}
	
	/**
	 * 编辑一个ResourceType对象的明细信息
	 */
	@RequestMapping(value = "/resource", method = RequestMethod.POST)
	public void editResourceType(ResourceType resourceType) {
        boolean isNew = resourceType.getId() == null;
		if( isNew ) { // 新建
			applicationService.createResourceType(resourceType);			
		}
		else{ // 编辑
			applicationService.updateResourceType(resourceType);	
		}
		doAfterSave(isNew, resourceType, "AppSource");
	}
	
	/**
	 * 编辑一个Operation对象的明细信息
	 */
	@RequestMapping(value = "/operation", method = RequestMethod.POST)
	public void editOperation(Operation operation) {
        boolean isNew = operation.getId() == null;
		if( isNew ) { // 新建，新建的权限选项要将该权限选项赋予管理员角色(id==-1)
			applicationService.saveOperation(operation);
		}
		else { // 编辑
			applicationService.updateOperation(operation);
		}
		doAfterSave(isNew, operation, "AppSource");
	}
	
	/**
	 * 删除应用系统
	 */
	@RequestMapping(value = "/application/{id}", method = RequestMethod.DELETE)
	public void deleteApplication(@PathVariable Long id) {
		applicationService.removeApplication(id);
		printSuccessMessage();
	}
	
	/**
	 * 删除资源类型
	 */
	@RequestMapping(value = "/resource/{id}", method = RequestMethod.DELETE)
	public void deleteResourceType(@PathVariable Long id) {
		applicationService.removeResourceType(id);
        printSuccessMessage();
	}
	
	/**
	 * 删除操作选项
	 */
	@RequestMapping(value = "/operation/{id}", method = RequestMethod.DELETE)
	public void deleteOperation(@PathVariable Long id) {
		applicationService.removeOperation(id);
        printSuccessMessage();
	}
	
	
	private String applicationType;
	
	@Autowired private IResourceRegisterService registerService;
	
	// 返回一个空的无数据的模板
	public String getImportTemplate(String applicationType) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("applicationType", applicationType);
		XFormEncoder encoder = new XFormEncoder(UMConstants.IMPORT_APP_XFORM, map);
		return print("ImportApplication", encoder);
	}
	
	public String registerApplication(@RequestParam File file) {
		if (null == file) {
			throw new BusinessException("没有选择文件，请重新导入！");
		}
		
		if (!file.getName().endsWith(".xml")) {
            return print("SCRIPT", "parent.alert(\"文件格式不正确，请导入xml文件！\");");
		}
		
        try {
            Document doc = new SAXReader().read(file);
            registerService.applicationResourceRegister(doc, applicationType);
        } catch (Exception e) {
            log.error("导入失败，请查看日志信息！", e);
            return print("SCRIPT", "parent.alert(\"导入失败，请查看日志信息！\");");
        }
        return print("SCRIPT", "parent.alert(\"导入成功！\");var ws = parent.$(\"ws\");ws.closeActiveTab();parent.loadInitData();");
	}
}
