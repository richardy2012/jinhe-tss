/* ==================================================================   
 * Created [2007-1-9] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2012-2015 
 * ================================================================== 
*/
package com.jinhe.tss.cache.extension.threadpool;

import org.apache.log4j.Logger;

import com.jinhe.tss.cache.CacheCustomizer;
import com.jinhe.tss.cache.Cacheable;
import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.TimeWrapper;

/** 
 * 线程池自定义类。
 * 创建、验证、销毁工作线程。
 */
public class ThreadPoolCustomizer implements CacheCustomizer {
    
    protected Logger log = Logger.getLogger(this.getClass());
  
    public Cacheable create(Long cyclelife) {
        IThreadPool tpool = JCache.getInstance().getThreadPool();
        Thread thread = tpool.createWorkThread();
        thread.start();
        return new TimeWrapper(thread.getName(), thread, cyclelife);
    }

    public boolean isValid(Cacheable o) {
        return ((Thread)o.getValue()).isAlive(); 
    }

    public void destroy(Cacheable o) {
        Thread poolWorker = (Thread) o.getValue();
        try {
            poolWorker.join(50); //等待线程死亡
        } catch (InterruptedException e) {
            log.error("停止initer线程时被中断", e);
        }
        poolWorker = null;
    }

	public Cacheable reloadCacheObject(Cacheable item) {
		return item;
	}
}

