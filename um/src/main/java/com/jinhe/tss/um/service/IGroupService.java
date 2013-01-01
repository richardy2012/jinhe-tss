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
     * 取制定其他应用下的有操作权限的其他用户组（以及其他用户组根节点、其他应用）
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
	 * @param groupType
	 */
    @Logable(operateTable="用户组织", operateType="启用/停用", 
            operateInfo="启用/停用用户组 (ID: ${args[1]})"
        )
	void startOrStopGroup(String applicationId, Long groupId, Integer disabled, Integer groupType);
	
	/**
	 * <p>
	 * 用户组的移动。
     * 要判断移动到的组的状态,toGroupId如果是停用,移动过来的全部停用,并且还要判断权限。
	 * </p>
	 * @param groupId
	 * @param toGroupId
	 */
    @Logable(operateTable="用户组织", operateType="移动", 
            operateInfo="移动(ID: ${args[0]}) 用户组至 (ID: ${args[1]}) 用户组"
        )
	void moveGroup(Long groupId, Long toGroupId);
	
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
	 * <p>
	 * 拷贝用户组
	 * 拷贝前先判断是否有权限
	 * 1.应该先判断是不是对要拷贝的组下的所有子节点都有拷贝权限	
	 * 2.如果要及联拷贝用户则还要判断是否对所有用户有拷贝权限
	 * </p>
	 * @param groupId
	 * @param toGroupId  
     *                拷贝到的用户组(约束:只能从其他用户组拷贝到主用户组), 如果是复制，则为父节点ID
	 * @param isCascadeUser
     *                只有复制到的时候才用及联复制用户
	 * @return
	 */
    @Logable(operateTable="用户组织", operateType="复制", 
            operateInfo="复制(ID: ${args[0]}) 用户组至 (ID: ${args[1]}) 用户组"
        )
	List<Group> copyGroup( Long groupId, Long toGroupId, boolean isCascadeUser);
	
	/**
	 * <p>
	 * 拷贝用户组到其他应用系统
	 * </p>
	 * @param userId
	 * @param groupId
	 * @param applicationId
	 * @return
	 */
    @Logable(operateTable="用户组织", operateType="复制", 
            operateInfo="复制(ID: ${args[0]}) 用户组至 (ID: ${args[1]}) 应用系统"
        )
	List<?> copyGroup2OtherApp(Long groupId, Long appId);
	
	/**
	 * <p>
	 * 设置用户组的密码策略
	 * </p>
	 * @param groupId
	 * @param ruleId
	 */
    @Logable(operateTable="用户组织", operateType="设置密码策略", 
            operateInfo="设置(ID: ${args[0]}) 用户组的为 ID = ${args[1]} 的密码策略"
        )
	void setPasswordRule(Long groupId, Long ruleId);
    
    /**
     * 将用户组从其它用户组下导入到主用户组下 的 操作前需要用到的数据
     * @param groupId 
     * @param toGroupId
     * @return
     */
    Map<String, Object> getImportGroupData(Long groupId, Long toGroupId);
}
