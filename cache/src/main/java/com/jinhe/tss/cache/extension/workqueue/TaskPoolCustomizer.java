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
import com.jinhe.tss.cache.TimeWrapper;
import com.jinhe.tss.cache.extension.DefaultCustomizer;
import com.jinhe.tss.util.BeanUtil;

/**
 * 任务池自定义类
 * 
 */
public abstract class TaskPoolCustomizer extends DefaultCustomizer {
    
    public Cacheable create() {
        String taskClassName = getTaskClass();
		Task task = (Task)BeanUtil.newInstanceByName(taskClassName);
		
        String key = TimeWrapper.createSequenceKey("Task");
        return new TimeWrapper(key, task, strategy.cyclelife);
    }
    
    public Cacheable reloadCacheObject(Cacheable item) {
    	return create();
    }

    public boolean isValid(Cacheable o) {
        return o != null && o.getValue() instanceof Task;
    }

    public void destroy(Cacheable o) {
        o = null;
    }
    
    protected abstract String getTaskClass();
}
