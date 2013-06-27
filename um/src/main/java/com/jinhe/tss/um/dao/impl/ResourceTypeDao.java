package com.jinhe.tss.um.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.Pool;
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
	
	
    /** 资源类型对应的"未补全的表"和"补全的表"的缓存 */
    public static final String RES_TYPE_MAPPING_CACHE = "res_type_mapping";
    public static final String RES_TYPE_ROOT_CACHE  = "res_type_root";

    private Pool cache = JCache.getInstance().getCachePool(RES_TYPE_MAPPING_CACHE);
    private Pool rootCache = JCache.getInstance().getCachePool(RES_TYPE_ROOT_CACHE);
    
    private String getCode(String applicationId, String resourceTypeId){
        return applicationId + "_" + resourceTypeId;
    }
    

	public ResourceTypeRoot getResourceTypeRoot(String applicationId,String resourceTypeId){
		String hql = " from ResourceTypeRoot o where upper(o.applicationId) = ? and o.resourceTypeId = ?";
        List<?> list = getEntities(hql, applicationId.toUpperCase(), resourceTypeId );
        return list.size() > 0 ? (ResourceTypeRoot)list.get(0) : null;
	}
	
    public Long getResourceRootId(String applicationId, String resourceTypeId) {
        String code = getCode(applicationId, resourceTypeId);
        if (rootCache.getObject(code) == null) {
            rootCache.putObject(code, getResourceTypeRoot(applicationId, resourceTypeId));
        }
        return ((ResourceTypeRoot) rootCache.getObject(code).getValue()).getRootId();
    }
    
    public ResourceType getResourceType(String applicationId, String resourceTypeId) {
        String code = getCode(applicationId, resourceTypeId);
        if (cache.getObject(code) == null) {
        	String hql = " from ResourceType o where upper(o.applicationId) = ? and o.resourceTypeId = ?";
    		List<?> list = getEntities(hql, applicationId.toUpperCase(), resourceTypeId);
    		if (list.isEmpty()) {
    			throw new BusinessException("未找到 " + applicationId + " 系统中，资源类型为 " + resourceTypeId + " 的资源类型");
    		}
    		cache.putObject(code, list.get(0));
        }
        return (ResourceType) cache.getObject(code).getValue();
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