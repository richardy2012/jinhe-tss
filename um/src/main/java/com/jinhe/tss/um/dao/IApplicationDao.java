package com.jinhe.tss.um.dao;

import com.jinhe.tss.framework.persistence.IDao;
import com.jinhe.tss.um.entity.Application;

public interface IApplicationDao extends IDao<Application>{
 
    /**
     * <p>
     * 根据应用系统Code(applicationId)获得一个应用系统
     * </p>
     * @param applicationId
     * @return
     */
    Application getApplication(String applicationId);
    
	/**
	 * <p>
	 * 根据应用系统id清除导入过的脏数据
	 * </p>
	 * @param applicationId
	 */
	void clearDirtyData(String applicationId);
 
}


	