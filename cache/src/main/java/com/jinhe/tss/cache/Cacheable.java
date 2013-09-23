/* ==================================================================   
 * Created [2006-12-28] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2012-2015 
 * ================================================================== 
 */

package com.jinhe.tss.cache;

/**
 * 可缓存对象对象接口。<br/>
 * 
 * 对缓存进来的对象做了进一步的包装，为其设置生命周期、点击率等信息。
 */
public interface Cacheable {

	/**
	 * 获取对象在缓存池中的key值
	 * 
	 * @return
	 */
	Object getKey();

	/**
	 * 获取对象值
	 * 
	 * @return
	 */
	Object getValue();

	/**
	 * 判断对象是否已经过期
	 * 
	 * @return
	 */
	boolean isExpired();

	/**
	 * 获取缓存项最后一次被访问的时间
	 * 
	 * @return
	 */
	long getAccessed();

	/**
	 * 更新缓存项最后一次被访问的时间,同时更新对象的死亡时间
	 */
	void updateAccessed();

	/**
	 * 获取对象的点击次数
	 * 
	 * @return
	 */
	int getHit();

	/**
	 * 每点击一次,点击率加1
	 */
	void addHit();

	/**
	 * 更新缓存对象
	 * 
	 * @param value
	 */
	void update(Object value);
}
