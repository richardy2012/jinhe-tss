package com.jinhe.tss.um.service;

import java.util.List;
import java.util.Map;

import com.jinhe.tss.framework.component.log.Logable;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.entity.User;

public interface IGroupService {
	
	/** 根据ID查询用户组 */
	Group getGroupById(Long id);

	/**
	 * <p>
	 * 根据用户组的id获取所在用户组的用户
	 * </p>
	 * @param groupId
	 * @return
	 */
	List<User> findUsersByGroupId(Long groupId);

	/**
	 * <p>
	 * 根据用户组的id获取此用户组拥有的角色
	 * </p>
	 * @param groupId
	 * @return List
	 */
	List<?> findRolesByGroupId(Long groupId);
	
	/**
	 * <p>
	 * 查询操作用户拥有编辑权限所有角色
	 * </p>
	 * @return List
	 */
	List<?> findEditableRolesByOperatorId();
	
    /**
     * 获取有操作权限的主用户组（多用于移动时取目标用户组）
     * @param operationId
     * @return
     */
    Object[] getMainGroupsByOperationId(String operationId);
    
    /**
     * 获取有操作权限的辅助用户组
     * @param operationId
     * @return
     */
    Object[] getAssistGroupsByOperationId(String operationId);
    
    /**
     * 取制定有操作权限的其他用户组（以及其他用户组根节点、其他应用列表）
     * @param operationId
     * @return
     */
    Object[] getOtherGroupsByOperationId(String operationId);
    
    /**
     * 取指定的其他应用下的有操作权限的其他用户组（以及其他用户组根节点、其他应用）
     * @param operationId
     * @param applicationId
     * @return
     */
    Object[] getGroupsUnderAppByOperationId(String operationId, String applicationId);

	/**
	 * <p>
	 * 编辑一个Group对象的明细信息、用户组对用户信息、用户组对角色的信息.
	 * </p>
	 * @param group
	 * @param userIdsStr
	 * @param roleIdsStr
	 */
    @Logable(operateTable="用户组织", operateType="修改", 
            operateInfo="编辑 ${args[0]} 用户组的明细信息（用户组对用户信息: ${args[1]} 、用户组对角色的信息: ${args[2]}）"
        )
	void editExistGroup(Group group, String userIdsStr, String roleIdsStr);
	
	/**
	 * <p>
	 * 新建一个Group对象的明细信息、用户组对用户信息、用户组对角色的信息
	 * </p>
	 * @param group
	 * @param userIdsStr
	 * @param roleIdsStr
	 */
    @Logable(operateTable="用户组织", operateType="新建", 
            operateInfo="新建 ${args[0]} 用户组 （用户组对用户信息: ${args[1]} 、用户组对角色的信息: ${args[2]}）"
        )
	void createNewGroup(Group group, String userIdsStr, String roleIdsStr);
	
	/**
	 * <p>
	 * 启用或者停用用户组
	 * </p>
	 * @param groupId
	 * @param disabled
	 */
    @Logable(operateTable="用户组织", operateType="启用/停用", 
            operateInfo="启用/停用用户组 (ID: ${args[1]})"
        )
	void startOrStopGroup(String applicationId, Long groupId, Integer disabled);
	
	/**
	 * <p>
	 * 用户组的排序
	 * </p>
	 * @param groupId
	 * @param toGroupId
	 * @param direction
	 * 			+1/向下
	 * 			-1/向上
	 */
    @Logable(operateTable="用户组织", operateType="排序", 
            operateInfo="排序(ID: ${args[0]}) 用户组至 (ID: ${args[1]}) 用户组"
        )
	void sortGroup(Long groupId, Long toGroupId, int direction);
	
	/**
	 * <p>
	 * 获取应用系统和用户组
	 * </p>
	 * @param userId
	 * @return 
	 */
	Object[] findGroups();

	/**
	 * <p>
	 * 删除用户组
	 * </p>
	 * @param applicationId
	 * @param groupId
	 * @param groupType 用户组类型
	 */
    @Logable(operateTable="用户组织", operateType="删除", 
            operateInfo="删除 (ID: ${args[1]}) 用户组" )
	void deleteGroup(String applicationId, Long groupId, Integer groupType);
	
    /**
     * 将用户组从其它用户组下导入到主用户组下 的 操作前需要用到的数据
     * @param groupId 
     * @param toGroupId
     * @return
     */
    Map<String, Object> getImportGroupData(Long groupId, Long toGroupId);
}
