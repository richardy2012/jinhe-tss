package com.jinhe.tss.um.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.jinhe.tss.framework.persistence.TreeSupportDao;
import com.jinhe.tss.um.dao.IApplicationDao;
import com.jinhe.tss.um.entity.Application;
import com.jinhe.tss.util.EasyUtils;

public class ApplicationDao extends TreeSupportDao<Application> implements IApplicationDao {
	
    public ApplicationDao() {
		super(Application.class);
	}
	
	public Application getApplication(String applicationId){
		List<?> list = getEntities("from Application o where o.applicationId = ?", applicationId);
		return list.size() > 0 ? (Application)list.get(0) : null;
	}
	
    public List<?> getApplications(List<?> appIds, String appType) {
        if( EasyUtils.isNullOrEmpty(appIds) ) return new ArrayList<Application>();
        
        String hql = " from Application o where o.applicationType = :appType and o.id in (:appIds) order by o.seqNo";
        return getEntities(hql, new Object[]{"appType", "appIds"}, new Object[]{appType, appIds});
    }
 
    // 删除Operation、ResourceTypeRoot、ResourceType、Application表
	public void clearDirtyData(String applicationId) {
        deleteAll(getEntities("from Operation        where applicationId = ?", applicationId));
        deleteAll(getEntities("from ResourceTypeRoot where applicationId = ?", applicationId));
        deleteAll(getEntities("from ResourceType     where applicationId = ?", applicationId));
        deleteAll(getEntities("from Application      where applicationId = ?", applicationId));
		flush();
	}
 
}