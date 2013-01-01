/* ==================================================================   
 * Created [2007-1-3] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2012-2015 
 * ================================================================== 
*/
package com.jinhe.tss.cache.extension.workqueue;

import com.jinhe.tss.cache.Cacheable;
import com.jinhe.tss.cache.CacheCustomizer;
import com.jinhe.tss.cache.TimeWrapper;
import com.jinhe.tss.util.BeanUtil;

/**
 * 任务池自定义类
 * 
 */
public abstract class TaskPoolCustomizer implements CacheCustomizer {
    
    public Cacheable create(Long cycleLife) {
        String taskClassName = getTaskClass();
		Task task = (Task)BeanUtil.newInstanceByName(taskClassName);
		
        String key = TimeWrapper.createRandomKey("Task");
        return new TimeWrapper(key, task, cycleLife);
    }
    
    public Cacheable reloadCacheObject(Cacheable item) {
    	return item;
    }

    public boolean isValid(Cacheable o) {
        return o != null && o.getValue() instanceof Task;
    }

    public void destroy(Cacheable o) {
        o = null;
    }
    
    protected abstract String getTaskClass();
}
