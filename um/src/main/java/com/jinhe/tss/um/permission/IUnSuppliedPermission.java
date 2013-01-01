package com.jinhe.tss.um.permission;

/**
 * (未补全的)角色资源权限表需要实现的接口
 */
public interface IUnSuppliedPermission {
	
	/** 资源id */
	Long getResourceId();
	
	/** 权限选项id  */
	String getOperationId();
	
	/** 角色id */
	Long getRoleId();
	
	/** 权限维护状态(1-仅此节点，2-该节点及所有下层节点) */
	Integer getPermissionState();
	
	/** 是否可授权  */
	Integer getIsGrant();
	
	/** 是否可传递 */
	Integer getIsPass();

	void setRoleId(Long roleId);

	void setResourceId(Long resourceId);

	void setOperationId(String operationId);

	void setPermissionState(Integer permissionState);

	void setIsGrant(Integer isGrant);

	void setIsPass(Integer isPass);
}

	