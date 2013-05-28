package com.jinhe.tss.um.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.web.dispaly.grid.GridDataEncoder;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.framework.web.rmi.HttpInvokerProxyFactory;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.service.IApplicationService;
import com.jinhe.tss.util.EasyUtils;

public class GeneralSearchAction extends BaseActionSupport {

	private String applicationId;
	private String resourceTypeId;
	private Long groupId;
	private Long roleId;
	
	private GeneralSearchService service;
	private RemoteSearchService remoteService;
	private IApplicationService applicationService;
	
	public String getAllApplications(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupId", groupId);	
		
		XFormEncoder xFormEncoder = new XFormEncoder(UMConstants.SEARCH_PERMISSION_XFORM, map);
        String[] applicationOptions = EasyUtils.generateComboedit(applicationService.getApplications(), "applicationId", "name", "|");
		xFormEncoder.setColumnAttribute("applicationId", "editortext", applicationOptions[1]);
		xFormEncoder.setColumnAttribute("applicationId", "editorvalue",	applicationOptions[0]);

        return print("GeneralSearchPermissionInfo", xFormEncoder);
	}
	
	/**
	 * <p>
	 * 根据应用id获得资源类型
	 * </p>
	 * @return
	 */
	public String getResourceTypes(){
		StringBuffer sb = new StringBuffer();
        sb.append("<column name=\"resourceTypeId\" caption=\"资源类型\" mode=\"string\" editor=\"comboedit\" ");
        
        String[] resourceTypeOptions = EasyUtils.generateComboedit(service.getResourceTypeListByApp(applicationId), "resourceTypeId", "name", "|");
        sb.append(" editorvalue=\"").append(resourceTypeOptions[0]).append("\" ");
        sb.append(" editortext=\"").append(resourceTypeOptions[1]).append("\"/>");
        
        return print("ResourceType", sb);
	}
	
	/**
	 * <p>
	 * 查询授权信息
	 * </p>
	 * @return
	 */
	public String searchPermission() {
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

        return print("GeneralSearchPermissionList", treeEncoder);
    }
	
	/**
	 * <p>
	 * 查询一个其他用户组下面的用户和主用户组的对应关系
	 * </p>
	 * @return
	 */
	public String searchMapping(){
		List<?> users = service.searchOtherUserMappingInfo(groupId);
		GridDataEncoder gridEncoder = new GridDataEncoder(users, UMConstants.GENERAL_SEARCH_MAPPING_GRID);

        return print("GeneralSearchMappingGrid", gridEncoder);
	}
 
	/**
	 * <p>
	 * 一个组下面所有用户的因转授而获得的角色的情况
	 * </p>
	 * @return
	 */
	public String searchUserStrategyInfo(){
		List<?> list = service.searchUserStrategyInfoByGroupId(groupId);
		GridDataEncoder gridEncoder = new GridDataEncoder(list, UMConstants.GENERAL_SEARCH_STRATEGY_GRID);
				
        return print("GeneralSearchUserStrategyInfoGrid", gridEncoder);
	}
	
	/**
	 * 根据用户组查询组下用户（需是登陆用户可见的用户）的角色授予情况
	 * @return
	 */
	public String searchRolesInfo(){
		List<?> list = service.searchUserRolesMapping(groupId);
		GridDataEncoder gridEncoder = new GridDataEncoder(list, UMConstants.GENERAL_SEARCH_ROLE_GRID);

		return print("GeneralSearchRoleGrid", gridEncoder);
	}
	
	/**
	 * 拥有同一个角色的所有用户列表
	 * @return
	 */
	public String searchUserInfoByRole(){
		List<?> list = service.searchUsersByRole(roleId);
		GridDataEncoder gridEncoder = new GridDataEncoder(list,UMConstants.GENERAL_SEARCH_USER_GRID);

        return print("GeneralSearchUserGrid",gridEncoder);
	}
	
	public String searchUsersByGroup(){
		List<?> list = service.searchUsersByGroup(groupId);
		GridDataEncoder gridEncoder = new GridDataEncoder(list,UMConstants.GENERAL_SYN_GRID);

        return print("GeneralSearchSyncGrid", gridEncoder);
	}


	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public void setResourceTypeId(String resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
    
    public void setService(GeneralSearchService service) { 
        this.service = service; 
    }

	public void setRemoteService(RemoteSearchService remoteService) {
		this.remoteService = remoteService;
	}

	public void setApplicationService(IApplicationService applicationService) {
		this.applicationService = applicationService;
	}
}