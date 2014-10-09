package com.jinhe.tss.um.permission;

import java.util.List;

/**
 * 操作资源表时相关的补全操作和删除操作
 */
public interface ResourcePermission {
    
    /**
     * <p>
     * 添加资源时的根据其父亲节点的权限补全当前新增资源节点的权限信息，将父节点的权限信息一模一样复制过来。<br/>
     * 
     * 补全过程是这样的：<br/>
     * 1.找到资源的最近父节点<br/>
     * 2.从"补全表"里面查找该父节点的授权信息，并一模一样复制一份给新增的资源。<br/>
     * 3.如果父节点某个授权信息的permissionState==1，则相应的在"未补全表"里插入一条新增资源的该项授权信息<br/>
     *  （删除的时候用到，参考com.jinhe.tss.um.permission.impl.PermissionDao.deleteOldPermission()方法）<br/>
     * </p>
     * @param resourceId     资源
     * @param resourceTypeId 资源类型
     */
    void addResource(Long resourceId, String resourceTypeId);
    
	/**
	 * <p>
	 * 删除资源时的补全过程。
     * 只需处理删除节点本身的授权信息即可
     *（通常应用在删除节点的时候会一块删除其子节点，每删一次都要调用本方法一次的）
	 * </p>
	 * @param resourceId     资源
	 * @param resourceTypeId 资源类型
	 */
	void delResource(Long resourceId, String resourceTypeId);
	
	/**
	 * <p>
	 * 改变资源的父节点的补全操作
	 * </p>
	 * @param resourceId     资源
	 * @param resourceTypeId 资源类型
	 */
	void updateResource(Long resourceId, String resourceTypeId);
 
	/**
	 * <p>
	 * 获取用户对一个应用中的一种资源类型的一个资源的所有“父节点”的某个权限选项所拥有的资源ID集合
	 * </p>
	 * @param applicationId  应用
	 * @param resourceTypeId 资源类型
	 * @param resourceId     资源ID
	 * @param operationId    权限选项
	 * @param operatorId     登录用户
	 * @return
	 */
	List<?> getParentResourceIds(String applicationId, String resourceTypeId, Long resourceId, String operationId, Long operatorId);

	/**
	 * <p>
	 * 获取用户对一个应用中的一种资源类型的一个资源下的“子节点”的某个权限选项所拥有的资源ID集合
	 * </p>
     * @param applicationId  应用
     * @param resourceTypeId 资源类型
     * @param resourceId     资源ID
     * @param operationId    权限选项
     * @param operatorId     登录用户
	 * @return
	 */
	List<?> getSubResourceIds(String applicationId, String resourceTypeId, Long resourceId, String operationId, Long operatorId);
}
