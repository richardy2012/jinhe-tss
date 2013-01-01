/* ==================================================================   
 * Created [2007-1-8] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2012-2015 
 * ================================================================== 
*/

package com.jinhe.tss.cache.extension.workqueue;

import com.jinhe.tss.cache.Reusable;

/** 
 * 可执行且可回收的任务的接口
 * 
 */
public interface Task extends Reusable {

    /**
     * 执行任务
     */
    void excute();
    
}

