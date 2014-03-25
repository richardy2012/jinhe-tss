/* ==================================================================   
 * Created [2009-4-27 下午11:32:55] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2009-2012 
 * ================================================================== 
 */

package com.jinhe.tss.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 对缓冲对象及其Key一起进行包装，使其具有生命周期，同时记录点击量等信息。
 * 
 */
public class TimeWrapper implements Cacheable, Serializable {

	private static final long serialVersionUID = 430479804348050166L;

	private Object key, value;
	private long death = 0; // 如果death = 0，则元素永不过期
	private long accessed; // 记录元素最后一次被访问的时间
	private int hit = 0;  // 对象的点击次数

	/**
	 * 创建一个新的wrapped对象.
	 * 
	 * @param id
	 * @param obj
	 * @param cycleLife 存活时间( > 0)
	 */
	public TimeWrapper(Object key, Object value) {
		this(key, value, 0L);
	}

	public TimeWrapper(Object key, Object value, Long cycleLife) {
		this.key = key;
		this.value = value;
		
		accessed = System.currentTimeMillis();
		setCyclelife(cycleLife);
	}

	public Object getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	/**
	 * 设置元素的有效期。
	 * 
	 * @param expiryTime
	 */
	 synchronized void setCyclelife(long expiryTime) {
		if (expiryTime < 0) {
			throw new RuntimeException("缓存项的生命周期值不能小于0");
		} 
		else if (expiryTime > 0) {
			this.death = System.currentTimeMillis() + expiryTime;
		}
		else {
			death = 0;
		}
	}

	/**
	 * 判断元素是否已过期。如果death＝0，则元素永不过期
	 */
	public synchronized boolean isExpired() {
		return death > 0 && System.currentTimeMillis() > death;
	}

	public synchronized void updateAccessed() {
		this.accessed = System.currentTimeMillis();
	}

	public long getAccessed() {
		return accessed;
	}

	public int getHit() {
		return hit;
	}

	public void addHit() {
		hit++;
	}

	public void update(Object value) {
		this.value = value;
	}
	
	private static Map<String, Integer> countsMap = new HashMap<String, Integer>();

	/**
	 * 创建一个指定前缀 + 当前时间 ＋ 随机值 的 key
	 * 
	 * @param prefix
	 * @return
	 */
	public static String createRandomKey(String prefix) {
	    Integer count = countsMap.get(prefix);
	    if(count == null) {
	        countsMap.put(prefix, count = 1);
	    }
	    else {
	        countsMap.put(prefix, count = count + 1);
	    }
	    
		return prefix + "_" + count;
	}
	
	public boolean equals(Object o) {
		if (o instanceof TimeWrapper) {
			TimeWrapper temp = (TimeWrapper) o;
			return this.key != null && this.key.equals(temp.getKey());
		}
		return false;
	}

	public int hashCode() {
		return key.hashCode();
	}
	
	public String toString() {
		return this.getKey().toString();
	}
}