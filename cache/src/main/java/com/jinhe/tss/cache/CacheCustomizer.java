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
 * <br/>缓存池自定义扩展接口。
 * <br/>本接口提供了扩充缓存池机制的功能。
 * <br/>通过实现本接口，用户可以各种定制各种特殊的缓存要求。
 * <br/>具体操作包括：缓存项创建、销毁以及合法性验证
 */
public interface CacheCustomizer {

	/**
	 * 创建对象,将对象放入池中。<br/>
	 * 本方法在cache check-out方法调用时，池中没有可用对象的时候被调用用来生成新的对象。
	 * 
	 * @param cyclelife 对象的生命周期
	 * @return
	 */
	Cacheable create(Long cyclelife);
	
	/**
	 * 重新载入缓存项
	 * 
	 * @param item
	 * @return
	 */
	Cacheable reloadCacheObject(Cacheable item);

	/**
	 * <br/>检测对象是否正常。
	 * <br/>当对象被check-out的时候本方法被调用来判断检出对象是否可以使用。
	 * <br/>实现类实现该方法用以确定check-out的对象不会存在问题而影响使用。
	 * 
	 * @param o
	 * @return
	 */
	boolean isValid(Cacheable o);

	/**
	 * <br/>销毁对象。
	 * <br/>本方法会在池需要整理/清除，或者释放的时候被调用。
	 * 
	 * @param o
	 */
	void destroy(Cacheable o);
}
