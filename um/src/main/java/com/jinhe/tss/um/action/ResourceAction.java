package com.jinhe.tss.um.action;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.entity.Application;
import com.jinhe.tss.um.entity.Operation;
import com.jinhe.tss.um.entity.ResourceType;
import com.jinhe.tss.um.entity.ResourceTypeRoot;
import com.jinhe.tss.um.helper.ApplicationTreeParser;
import com.jinhe.tss.um.service.IResourceService;

/**
 * 应用资源管理相关Action对象
 */
@Controller
@RequestMapping("/auth/resource")
public class ResourceAction extends BaseActionSupport {

	@Autowired private IResourceService resourceService;
 
	/**
	 * 获取所有的Applicaton对象并转换成Tree相应的xml数据格式
	 */
	@RequestMapping("/apps")
	public void getAllApplication2Tree(HttpServletResponse response) {
		Object applications = resourceService.findApplicationAndResourceType();
		TreeEncoder treeEncoder = new TreeEncoder(applications, new ApplicationTreeParser());
		treeEncoder.setNeedRootNode(false);
 
		print("AppSource", treeEncoder);
	}
	
	/**
	 * 获取一个Application对象的明细信息
	 */
	@RequestMapping(value = "/app/{id}", method = RequestMethod.GET)
	public void getApplicationInfo(HttpServletResponse response, @PathVariable("id") Long id) {
		XFormEncoder xformEncoder = null;
 
        Application application = resourceService.getApplicationById(id);
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
	@RequestMapping(value = "/resourceType/{id}", method = RequestMethod.GET)
	public void getResourceTypeInfo(HttpServletResponse response, @PathVariable("id") Long id) {
		ResourceType resourceType = resourceService.getResourceTypeById(id);
        String applicationId  = resourceType.getApplicationId();
		String resourceTypeId = resourceType.getResourceTypeId();
		ResourceTypeRoot resourceTypeRoot = resourceService.findResourceTypeRoot(applicationId, resourceTypeId);
		if( resourceTypeRoot != null) {
			resourceType.setRootId(resourceTypeRoot.getRootId());
		}
		
		XFormEncoder xformEncoder = new XFormEncoder(UMConstants.RESOURCETYPE_XFORM, resourceType);
		print("ResourceTypeDetail", xformEncoder);
	}
	
	/**
	 * 获取一个Operation对象的明细信息
	 */
	@RequestMapping(value = "/operation/{id}", method = RequestMethod.GET)
	public void getOperationInfo(HttpServletResponse response, @PathVariable("id") Long id) {
		// 编辑操作选项
		Operation operation = resourceService.getOperationById(id);
		XFormEncoder xformEncoder = new XFormEncoder(UMConstants.OPERATION_XFORM, operation);
		print("PermissionOption", xformEncoder);
	}
	
	/**
	 * 编辑一个Application对象的明细信息
	 */
	@RequestMapping(value = "/app", method = RequestMethod.POST)
	public void editApplication(HttpServletResponse response, Application application) {
        boolean isNew = application.getId() == null;
        resourceService.saveApplication(application);   
		doAfterSave(isNew, application, "AppSource");
	}
	
	/**
	 * 编辑一个ResourceType对象的明细信息
	 */
	@RequestMapping(value = "/resource", method = RequestMethod.POST)
	public void editResourceType(HttpServletResponse response, ResourceType resourceType) {
        boolean isNew = resourceType.getId() == null;
		if( isNew ) { // 新建
			resourceService.createResourceType(resourceType);			
		}
		else{ // 编辑
			resourceService.updateResourceType(resourceType);	
		}
		doAfterSave(isNew, resourceType, "AppSource");
	}
	
	/**
	 * 编辑一个Operation对象的明细信息
	 */
	@RequestMapping(value = "/operation", method = RequestMethod.POST)
	public void editOperation(HttpServletResponse response, Operation operation) {
        boolean isNew = operation.getId() == null;
		if( isNew ) { // 新建，新建的权限选项要将该权限选项赋予管理员角色(id==-1)
			resourceService.saveOperation(operation);
		}
		else { // 编辑
			resourceService.updateOperation(operation);
		}
		doAfterSave(isNew, operation, "AppSource");
	}
	
	/**
	 * 删除应用系统
	 */
	@RequestMapping(value = "/application/{id}", method = RequestMethod.DELETE)
	public void deleteApplication(HttpServletResponse response, @PathVariable Long id) {
		resourceService.removeApplication(id);
		printSuccessMessage();
	}
	
	/**
	 * 删除资源类型
	 */
	@RequestMapping(value = "/resource/{id}", method = RequestMethod.DELETE)
	public void deleteResourceType(HttpServletResponse response, @PathVariable Long id) {
		resourceService.removeResourceType(id);
        printSuccessMessage();
	}
	
	/**
	 * 删除操作选项
	 */
	@RequestMapping(value = "/operation/{id}", method = RequestMethod.DELETE)
	public void deleteOperation(HttpServletResponse response, @PathVariable Long id) {
		resourceService.removeOperation(id);
        printSuccessMessage();
	}
	
	
	// 返回一个空的无数据的模板
	@RequestMapping("/import/template")
	public void getImportTemplate(HttpServletResponse response) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("applicationType", UMConstants.PLATFORM_SYSTEM_APP);
		XFormEncoder encoder = new XFormEncoder(UMConstants.IMPORT_APP_XFORM, map);
		print("ImportApplication", encoder);
	}
	
	@RequestMapping("/register")
	public void registerApplication(HttpServletResponse response, 
			String applicationType, @RequestParam File file) {
		
		if (null == file) {
			throw new BusinessException("没有选择文件，请重新导入！");
		}
		
		if (!file.getName().endsWith(".xml")) {
            print("SCRIPT", "parent.alert(\"文件格式不正确，请导入xml文件！\");");
            return;
		}
		
        try {
            Document doc = new SAXReader().read(file);
            resourceService.applicationResourceRegister(doc, applicationType);
        } catch (Exception e) {
            log.error("导入失败，请查看日志信息！", e);
            print("SCRIPT", "parent.alert(\"导入失败，请查看日志信息！\");");
            return;
        }
        print("SCRIPT", "parent.alert(\"导入成功！\");var ws = parent.$(\"ws\");ws.closeActiveTab();parent.loadInitData();");
	}
}
