/* ==================================================================   
 * Created [2006-12-29] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2012-2015 
 * ================================================================== 
 */

package com.jinhe.tss.cache;

/**
 * 缓存池监听器接口。
 * 
 */
public interface Listener {

	/**
	 * 缓存池事件被触发后执行相应的操作
	 * 
	 * @param poolEvent
	 */
	void dealwithPoolEvent(PoolEvent poolEvent);
}
