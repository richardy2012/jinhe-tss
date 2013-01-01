package com.jinhe.tss.um.search;

import java.util.List;

public interface RemoteSearchService {

	/**
	 * <p>
	 * 查询一个组下每个用户拥有的资源权限列表
	 * </p>
	 * @param groupId
	 * @param applicationId
	 * @param resourceTypeId
	 * @return
	 */
	List<?> searchPermission(Long groupId, String applicationId, String resourceTypeId);
}