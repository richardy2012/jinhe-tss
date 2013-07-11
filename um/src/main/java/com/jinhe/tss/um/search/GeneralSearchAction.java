package com.jinhe.tss.um.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.web.dispaly.grid.GridDataEncoder;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.framework.web.rmi.HttpInvokerProxyFactory;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.service.IApplicationService;
import com.jinhe.tss.util.EasyUtils;

@Controller
@RequestMapping("search")
public class GeneralSearchAction extends BaseActionSupport {
	
	@Autowired private GeneralSearchService service;
	@Autowired private RemoteSearchService remoteService;
	@Autowired private IApplicationService applicationService;
	
	@RequestMapping("/apps/{groupId}")
	public void getAllApplications(Long groupId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupId", groupId);	
		
		XFormEncoder xFormEncoder = new XFormEncoder(UMConstants.SEARCH_PERMISSION_XFORM, map);
        String[] applicationOptions = EasyUtils.generateComboedit(applicationService.getApplications(), "applicationId", "name", "|");
		xFormEncoder.setColumnAttribute("applicationId", "editortext", applicationOptions[1]);
		xFormEncoder.setColumnAttribute("applicationId", "editorvalue",	applicationOptions[0]);

        print("GeneralSearchPermissionInfo", xFormEncoder);
	}
	
	/**
	 * 根据应用id获得资源类型
	 */
	@RequestMapping("/resources/{applicationId}")
	public void getResourceTypes(String applicationId) {
		StringBuffer sb = new StringBuffer();
        sb.append("<column name=\"resourceTypeId\" caption=\"资源类型\" mode=\"string\" editor=\"comboedit\" ");
        
        String[] resourceTypeOptions = EasyUtils.generateComboedit(service.getResourceTypeListByApp(applicationId), "resourceTypeId", "name", "|");
        sb.append(" editorvalue=\"").append(resourceTypeOptions[0]).append("\" ");
        sb.append(" editortext=\"").append(resourceTypeOptions[1]).append("\"/>");
        
        print("ResourceType", sb);
	}
	
	/**
	 * 查询授权信息
	 */
	@RequestMapping("/permission/{groupId}/{applicationId}/{resourceTypeId}")
	public void searchPermission(Long groupId, String applicationId, String resourceTypeId) {
        if ( EasyUtils.isNullOrEmpty(applicationId) ) throw new BusinessException("请选择应用系统");
        if ( EasyUtils.isNullOrEmpty(resourceTypeId) ) throw new BusinessException("请选择资源类型");

        RemoteSearchService localService;
        if (UMConstants.TSS_APPLICATION_ID.equals(applicationId)) {
            localService = remoteService; // 调用本地的RemoteSearchService
        } 
        else {
        	// 调用其他系统的GeneralSearchService
        	HttpInvokerProxyFactory factory = new HttpInvokerProxyFactory();
            factory.setAppCode(applicationId.toUpperCase());
            factory.setServiceUrl(UMConstants.GENERAL_SEARCH_SERVICE_URL);
            factory.setServiceInterface(RemoteSearchService.class);
            localService = (RemoteSearchService) factory.getObject();
        }
        
		List<?> data = localService.searchPermission(groupId, applicationId, resourceTypeId);
        TreeEncoder treeEncoder = new TreeEncoder(data);
        treeEncoder.setNeedRootNode(false);

        print("GeneralSearchPermissionList", treeEncoder);
    }
 
	/**
	 * 一个组下面所有用户的因转授而获得的角色的情况
	 */
	@RequestMapping("/subauth/{groupId}")
	public void searchUserSubauth(Long groupId) {
		List<?> list = service.searchUserSubauthByGroupId(groupId);
		GridDataEncoder gridEncoder = new GridDataEncoder(list, UMConstants.GENERAL_SEARCH_STRATEGY_GRID);
				
        print("GeneralSearchUserSubauthGrid", gridEncoder);
	}
	
	/**
	 * 根据用户组查询组下用户（需是登陆用户可见的用户）的角色授予情况
	 */
	@RequestMapping("/roles/{groupId}")
	public void searchRolesByGroup(Long groupId) {
		List<?> list = service.searchUserRolesMapping(groupId);
		GridDataEncoder gridEncoder = new GridDataEncoder(list, UMConstants.GENERAL_SEARCH_ROLE_GRID);

		print("GeneralSearchRoleGrid", gridEncoder);
	}
	
	/**
	 * 拥有同一个角色的所有用户列表
	 */
	@RequestMapping("/role/users/{roleId}")
	public void searchUsersByRole(Long roleId) {
		List<?> list = service.searchUsersByRole(roleId);
		GridDataEncoder gridEncoder = new GridDataEncoder(list, UMConstants.GENERAL_SEARCH_USER_GRID);

        print("GeneralSearchUserGrid",gridEncoder);
	}
}