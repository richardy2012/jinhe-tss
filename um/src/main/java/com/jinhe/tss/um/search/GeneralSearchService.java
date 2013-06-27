package com.jinhe.tss.um.search;

import java.util.List;

import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.helper.dto.UserRoleDTO;

public interface GeneralSearchService {
 
	/**
	 * <p>
	 * 查询一个其他用户组里面的用户对应的
	 * 主用户组下里面的用户的情况
	 * </p>
	 * @return
	 */
	List<?> searchOtherUserMappingInfo(Long groupId);

	/**
	 * <p>
	 * 根据应用系统id获得该应用的所有资源类型
	 * </p>
	 * @param applicationId
	 * 			应用系统id
	 * @return
	 */	
	List<?> getResourceTypeListByApp(String applicationId);
 
	/**
	 * <p>
	 * 一个组下面所有用户的因转授而获得的角色的情况
	 * </p>
	 * @param groupId
	 * @return
	 */
	List<?> searchUserSubauthByGroupId(Long groupId);
	
	/**
	 * 根据用户组查询组下用户（需是登陆用户可见的用户）的角色授予情况
	 * @param groupId
	 * @return
	 */
	List<UserRoleDTO> searchUserRolesMapping(Long groupId);

	/** 
     * 查询角色授予的用户列表（包括直接授予用户的和授予组时组下的所有用户）
	 * @param roleId
	 * @return
	 */
	List<User> searchUsersByRole(Long roleId); 
	
	/**
	 * 查询组下用户列表
	 * @param groupId
	 * @return
	 */
	List<?> searchUsersByGroup(Long groupId);
}
