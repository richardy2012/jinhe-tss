package com.jinhe.tss.framework;

import java.util.HashMap;
import java.util.Map;

import com.jinhe.tss.framework.persistence.pagequery.MacrocodeQueryCondition;

public abstract class AbstractSO extends MacrocodeQueryCondition {
	
    private String name;
	
	public Map<String, Object> getConditionMacrocodes() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("${name}", " and o.name = :name");
        
        return map;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
}
