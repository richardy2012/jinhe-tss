package com.jinhe.tss.framework;

import java.util.HashMap;
import java.util.Map;

import com.jinhe.tss.framework.persistence.pagequery.MacrocodeQueryCondition;

public abstract class AbstractSO extends MacrocodeQueryCondition {
	
	/**
	 * 所属站点
	 * 1、网点用户只能查看自身网点的数据，默认取登陆系统后设置到session里网点信息
	 * 2、网管等管理员，无权查看网点的客户、财务、人员等信息
	 */
    private Long ownerSiteId;

	public Long getOwnerSiteId() {
		return ownerSiteId;
	}

	public void setOwnerSiteId(Long ownerSiteId) {
		this.ownerSiteId = ownerSiteId;
	}
	
	public Map<String, Object> getConditionMacrocodes() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("${ownerSiteId}", " and o.ownerSite.id = :ownerSiteId");
        
        return map;
    }
    
}
