/* ==================================================================   
 * Created [2006-12-29] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2012-2015 
 * ================================================================== 
 */
package com.jinhe.tss.cache.strategy;

import com.jinhe.tss.cache.AbstractPool;
import com.jinhe.tss.cache.CacheCustomizer;
import com.jinhe.tss.cache.Container;
import com.jinhe.tss.cache.ObjectPool;
import com.jinhe.tss.cache.PoolEvent;
import com.jinhe.tss.cache.Pool;
import com.jinhe.tss.cache.SimplePool;
import com.jinhe.tss.cache.extension.DefaultCustomizer;
import com.jinhe.tss.cache.extension.MapContainer;
import com.jinhe.tss.cache.extension.mixin.Disabler;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.EasyUtils;

/**
 * 缓存策略 
 * <br/>不变区
 * <br/>  code
 * <br/>  name
 * <br/>可变区
 * <br/>  普通，不触发池事件
 * <br/>    accessMethod
 * <br/>    disabled
 * <br/>    interruptTime
 * <br/>    remark
 * <br/>  触发池事件STRATEGY_CHANGED_CYCLELIFE
 * <br/>    cyclelife
 * <br/>  如果变小了则触发池事件STRATEGY_CHANGED_SIZE_REDUCE，处理同PUT_IN事件；大了不管
 * <br/>    poolSize
 * <br/>  严重，触发池事件STRATEGY_CHANGED_RESET，重新初始化池
 * <br/>    poolClass
 * <br/>    poolCollectionClass
 * 
 */
public class CacheStrategy {

	// 缓存模块提供的对象池类型
	static final String SIMPLE_POOL_CLASS = SimplePool.class.getName();
	static final String BASE_POOL_CLASS = ObjectPool.class.getName();

	final static String DEFAULT_CONTAINER  = MapContainer.class.getName();
	final static String DEFAULT_CUSTOMIZER = DefaultCustomizer.class.getName();

	/** 缓存策略的名称 */
	private String name; 
	
	/** 缓存策略的code */
	private String code; 
	
	/** 缓存策略说明 */
	private String remark; 
	
	/** 缓存池的容量（注： size 为零则表示不限定池大小） */
	private Integer poolSize = 0;
	
	/** 初始化缓存池时初始缓存项的个数 */
	private Integer initNum = 0;
	
	/** 池中元素的有效期（生命周期） */
	private Long cyclelife = 0L;
	
	/** 中断时间(取不到缓存项时的等待时间) */
	private Long interruptTime = 0L;
	
	/** 0：启用（用以检测缓存启用和停用时的执行效率） 1:停用 */
	private String disabled = CacheConstants.FALSE;  
													 
	/** 
	 * 是否展示，用以隐藏运行时创建过来的缓存池，像没有名字只有code的那些
	 * 0：隐藏  1:显示 
	 */
	private String visible = CacheConstants.TRUE; 
	
	/**
	 * 池的访问方式 
	 * 1: FIFO (first-in, first-out: a queue). 
	 * 2: LIFO (last-in, first-out: a stack). 
	 * 3: RANDOM (a random item is selected for check-out). 
	 * 4：ACCESS_LRU （最近使用） 
	 * 5：ACCESS_LFU （最不常使用）
	 */
	private Integer accessMethod = Container.ACCESS_RANDOM;

	/**  缓存池实现类 */
	private String poolClass = SIMPLE_POOL_CLASS;
	
	/** 池容器类 */
	private String poolContainerClass = DEFAULT_CONTAINER;

	/** 缓冲池自定义类 */
	private String customizerClass = DEFAULT_CUSTOMIZER;

	/** 缓存策略的名称 */
	private AbstractPool pool; // 缓存策略里定义的缓存池

	public Pool getPoolInstance() {
		if (pool != null && pool.getCacheStrategy().equals(this)) {
			return pool;
		}

		pool = (AbstractPool) BeanUtil.newInstanceByName(poolClass);
		pool.setCacheStrategy(this);
		pool.setCustomizer((CacheCustomizer) BeanUtil.newInstanceByName(customizerClass));
		
		Pool proxyPool = Disabler.disableWrapper(pool);
 
		// init前需要先设置好customizer，因为初始化需要用到customizer.create()来新建缓存项。
		proxyPool.init();

		return proxyPool;
	}

	public boolean equals(Object o) {
		if (o instanceof CacheStrategy) {
			CacheStrategy strategy = (CacheStrategy) o;
			return this.code.equals(strategy.getCode());
		}
		return false;
	}

	/**
	 * 缓存策略修改时重新设置各个策略项的值，同时触发相应的事件。
	 * 
	 * @param c
	 */
	public void fireEventIfChanged(CacheStrategy c) {
		// 缓存项生命周期发生了改变时：
		if ( !this.cyclelife.equals(c.cyclelife) ) {
			this.cyclelife = c.cyclelife;
			pool.firePoolEvent(PoolEvent.STRATEGY_CHANGED_CYCLELIFE);
		}

		if ( this.poolSize < c.poolSize ) { // 池变小
			this.poolSize = c.poolSize;
			pool.firePoolEvent(PoolEvent.STRATEGY_CHANGED_SIZE_REDUCE);
		}

		if ( !this.poolClass.equals(c.poolClass)
				|| !this.poolContainerClass.equals(c.poolContainerClass)) {
			
			setPoolClass(c.poolClass);
			setPoolContainerClass(c.poolContainerClass);
			pool.firePoolEvent(PoolEvent.STRATEGY_CHANGED_RESET);
		}

		setCustomizerClass(c.customizerClass);
		this.initNum = c.initNum;
		this.poolSize = c.poolSize;
		this.accessMethod = c.accessMethod;
		this.disabled = c.disabled;
		this.interruptTime = c.interruptTime;
		this.remark = c.remark;
	}

	/**
	 * 设置缓存策略的自定义类。同时改变池的customizer对象，如果池已经存在的话。
	 */
	public void setCustomizerClass(String customizerClass) {
		this.customizerClass = EasyUtils.isNullOrEmpty(customizerClass) ? 
				DEFAULT_CUSTOMIZER : customizerClass;
	}

	/**
	 * 设置缓存池类型
	 */
	public void setPoolClass(String poolClass) {
		this.poolClass = EasyUtils.isNullOrEmpty(poolClass) ? 
				SIMPLE_POOL_CLASS : poolClass;
	}

	/**
	 * 设置缓存池的容器类
	 */
	public void setPoolContainerClass(String poolContainerClass) {
		this.poolContainerClass = EasyUtils.isNullOrEmpty(poolContainerClass) ? 
				DEFAULT_CONTAINER : poolContainerClass;
	}

	public void setCyclelife(Long cyclelife) {
		this.cyclelife = cyclelife == null ? 0 : cyclelife;
	}

	public String getCustomizerClass() {
		return customizerClass;
	}
 
	public String getCode() {
		return code;
	}

	public Long getCyclelife() {
		return cyclelife;
	}

	public String getDisabled() {
		return disabled;
	}

	public Long getInterruptTime() {
		return interruptTime;
	}

	public String getName() {
		return name;
	}

	public Integer getPoolSize() {
		return poolSize;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public void setInterruptTime(Long interruptTime) {
		this.interruptTime = interruptTime == null ? 0 : interruptTime;
	}

	public void setName(String name) {
		this.name = name == null ? code : name;
	}

	public void setPoolSize(Integer poolSize) {
		if (poolSize != null) {
			int size = Math.max(0, poolSize); // 确保 size 不小于 0
			this.poolSize = new Integer(size);
		}
	}

	public String getPoolClass() {
		return poolClass;
	}

	public String getPoolContainerClass() {
		return poolContainerClass;
	}

	public Integer getInitNum() {
		return initNum;
	}

	public void setInitNum(Integer initNum) {
		this.initNum = (initNum == null ? 0 : initNum);
	}

	public Integer getAccessMethod() {
		return accessMethod;
	}

	public void setAccessMethod(Integer method) {
		this.accessMethod = method;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}
}
