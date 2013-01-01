package com.jinhe.tss.um.dao;

import com.jinhe.tss.framework.persistence.IDao;
import com.jinhe.tss.um.entity.ResourceType;
import com.jinhe.tss.um.entity.ResourceTypeRoot;
import com.jinhe.tss.um.permission.RemoteResourceTypeDao;
 
public interface IResourceTypeDao extends IDao<ResourceType>, RemoteResourceTypeDao {

	/**
	 * <p>
	 * 获得一个应用系统的一个资源类型的根节点
	 * </p>
	 * @param applicationId
	 * @param resourceTypeId
	 * @return
	 */
    ResourceTypeRoot getResourceTypeRoot(String applicationId, String resourceTypeId);

	/**
	 * <p>
	 * 获取资源类型
	 * </p>
	 * @param applicationId
	 * 			应用系统id
	 * @param resourceTypeId
	 * 			资源类型id
	 * @return
	 */
	ResourceType getResourceType(String applicationId, String resourceTypeId);
	
}


	