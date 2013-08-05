package com.jinhe.tss.um.permission;

import java.util.List;

/**
 * <p>
 * 获取注册资源未补齐表，补齐表，资源视图，资源根节点等帮助类接口。
 * 提供接口主要为授权时提供本接口实现类对象的远程访问(供其他应用调用)。
 * </p>
 */
public interface RemoteResourceTypeDao {
	
	/**
	 * 获取未补全表名
	 * 
	 * @param applicationId
	 * @param resourceTypeId
	 * @return
	 */
	String getUnSuppliedTable(String applicationId, String resourceTypeId);
	
	/**
	 * 获取补全表名
	 * 
	 * @param applicationId
	 * @param resourceTypeId
	 * @return
	 */
	String getSuppliedTable(String applicationId, String resourceTypeId);
	
	/**
	 * 获取资源表名
	 * 
	 * @param applicationId
	 * @param resourceTypeId
	 * @return
	 */
	String getResourceTable(String applicationId, String resourceTypeId);
	
	/**
	 * 获取资源根节点
	 * 
	 * @param applicationId
	 * @param resourceTypeId
	 * @return
	 */
	Long getResourceRootId(String applicationId, String resourceTypeId);
    
    /**
     * 查找同一类资源的权限选项。只要operationId
     * 
     * @param applicationId
     * @param resourceTypeId
     * @return
     */
    List<?> getOperationIds(String applicationId, String resourceTypeId);

    /**
     * <p>
     * 查找同一类资源的权限选项
     * </p>
     * @param applicationId
     *              应用系统id
     * @param resourceTypeId
     *              资源类型id
     * @return
     */
    List<?> getOperations(String applicationId, String resourceTypeId);
}

	