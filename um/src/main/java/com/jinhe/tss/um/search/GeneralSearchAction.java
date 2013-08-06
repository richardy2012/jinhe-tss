package com.jinhe.tss.um.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jinhe.tss.framework.web.dispaly.grid.GridDataEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.um.UMConstants;

@Controller
@RequestMapping("/auth/search")
public class GeneralSearchAction extends BaseActionSupport {
	
	@Autowired private GeneralSearchService service;
 
	/**
	 * 一个组下面所有用户的因转授而获得的角色的情况
	 */
	@RequestMapping("/subauth/{groupId}")
	public void searchUserSubauth(Long groupId) {
		List<?> list = service.searchUserSubauthByGroupId(groupId);
		GridDataEncoder gridEncoder = new GridDataEncoder(list, UMConstants.GENERAL_SEARCH_STRATEGY_GRID);
				
        print("UserSubauthGrid", gridEncoder);
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

        print("RoleUserGrid", gridEncoder);
	}
}