package com.jinhe.tss.um.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.persistence.BaseDao;
import com.jinhe.tss.um.dao.IResourceTypeDao;
import com.jinhe.tss.um.entity.ResourceType;
import com.jinhe.tss.um.entity.ResourceTypeRoot;

@Repository("ResourceTypeDao")
public class ResourceTypeDao extends BaseDao<ResourceType> implements IResourceTypeDao {
 
	public ResourceTypeDao() {
		super(ResourceType.class);
	}
 
	public ResourceTypeRoot getResourceTypeRoot(String applicationId,String resourceTypeId){
		String hql = " from ResourceTypeRoot o where upper(o.applicationId) = ? and o.resourceTypeId = ?";
        List<?> list = getEntities(hql, applicationId.toUpperCase(), resourceTypeId );
        return list.size() > 0 ? (ResourceTypeRoot)list.get(0) : null;
	}
 
    public ResourceType getResourceType(String applicationId, String resourceTypeId) {
    	String hql = " from ResourceType o where upper(o.applicationId) = ? and o.resourceTypeId = ?";
		List<?> list = getEntities(hql, applicationId.toUpperCase(), resourceTypeId);
		if (list.isEmpty()) {
			throw new BusinessException("未找到 " + applicationId + " 系统中，资源类型为 " + resourceTypeId + " 的资源类型");
		}
        return (ResourceType) list.get(0);
    }
 
    public String getUnSuppliedTable(String applicationId, String resourceTypeId) {
        return getResourceType(applicationId, resourceTypeId).getUnSuppliedTable();
    }
 
    public String getSuppliedTable(String applicationId, String resourceTypeId) {
        return getResourceType(applicationId, resourceTypeId).getSuppliedTable();
    }
 
    public String getResourceTable(String applicationId, String resourceTypeId) {
        return getResourceType(applicationId, resourceTypeId).getResourceTable();
    }
 
    public List<?> getOperationIds(String applicationId, String resourceTypeId) {
        String hql = "select t.operationId from Operation t where upper(t.applicationId) = ? and t.resourceTypeId = ? order by t.seqNo";
        return getEntities(hql, applicationId.toUpperCase(), resourceTypeId);
    }
 
    public List<?> getOperations(String applicationId, String resourceTypeId) {
        String hql = " from Operation t where upper(t.applicationId) = ? and t.resourceTypeId = ? order by t.seqNo";
        return getEntities(hql, applicationId.toUpperCase(), resourceTypeId);
    }
}