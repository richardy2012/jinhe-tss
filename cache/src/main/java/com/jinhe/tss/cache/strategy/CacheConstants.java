package com.jinhe.tss.cache.strategy;

/**
 * 常量定义
 * 
 */
public interface CacheConstants {

	public final static String TRUE = "1";
	public final static String FALSE = "0";

	/**
	 * 已经初始化好的各缓存池key
	 */
	public final static String CONNECTION_POOL = "connectionpool";
	public final static String THREAD_POOL = "threadpool";
	public final static String TASK_POOL = "taskpool";
	
	public final static String XFORM_TEMPLATE_POOL = "xform_template";
	public final static String GRID_TEMPLATE_POOL = "grid_template";

	/**
	 * 缓存策略文件目录
	 */
	public final static String STRATEGY_PATH = "META-INF/CacheStrategy.xml";
	
	/**
	 * 缓存策略节点名称
	 */
	public final static String STRATEGY_NODE_NAME = "/strategies/strategy";
	
	/**
	 * 缓存策略模板目录
	 */
	public final static String CACHESTRATEGY_XFORM_TEMPLET = "template/cache/xform/CacheStrategyXForm.xml";
}
