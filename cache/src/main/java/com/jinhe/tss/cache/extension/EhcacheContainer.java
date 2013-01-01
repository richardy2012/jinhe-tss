/* ==================================================================   
 * Created [2006-12-31] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2009-2012 
 * ================================================================== 
 */
package com.jinhe.tss.cache.extension;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;

import com.jinhe.tss.cache.AbstractContainer;
import com.jinhe.tss.cache.Cacheable;

/**
 * 使用本类来缓存的对象的key,value必须继承Serializable,这是因为ehcache这么要求.
 * 
 */
public class EhcacheContainer extends AbstractContainer {

	protected Logger log = Logger.getLogger(this.getClass());

	private Cache cache;

	public EhcacheContainer(String name) {
		super(name);
		try {
			CacheManager.getInstance().addCache(name);
			cache = CacheManager.getInstance().getCache(name);
		} catch (IllegalStateException e) {
			log.error("EhcachePool->EhcacheContainer:IllegalStateException!", e);
		} catch (CacheException e) {
			log.error("EhcachePool->EhcacheContainer:CacheException!", e);
		}
	}

	public Cacheable get(Object key) {
		checkKey(key);
		try {
			Element e = cache.get((Serializable) key);
			if (e != null) {
				return (Cacheable) e.getValue();
			}
		} catch (IllegalStateException e) {
			log.error("EhcachePool->get:IllegalStateException!", e);
		} catch (CacheException e) {
			log.error("EhcachePool->get:CacheException!", e);
		}
		return null;
	}

	public Cacheable put(Object key, Cacheable value) {
		checkKey(key);
		checkKey(value);
		Element e = new Element((Serializable) key, (Serializable) value);
		cache.put(e);

		return value;
	}

	public Cacheable remove(Object key) {
		Cacheable item = get(key);
		checkKey(key);
		cache.remove((Serializable) key);
		return item;
	}

	@SuppressWarnings("unchecked")
	public Set<Object> keySet() {
		try {
			return new HashSet<Object>(cache.getKeys());
		} catch (IllegalStateException e) {
			log.error("EhcachePool->getKeys:IllegalStateException!", e);
		} catch (CacheException e) {
			log.error("EhcachePool->getKeys:CacheException!", e);
		}
		return null;
	}

	public void clear() {
		try {
			cache.removeAll();
		} catch (IllegalStateException e) {
			log.error("EhcachePool->clear:IllegalStateException!", e);
		}
	}

	private void checkKey(Object key) {
		if (!(key instanceof Serializable)) {
			throw new RuntimeException("类" + key.getClass() + "没有实现Serializable接口，不能被EHCache缓存！");
		}
	}

	public int size() {
		try {
			return cache.getSize();
		} catch (Exception e) {
			throw new RuntimeException("获取EHCache缓存的大小时失败！", e);
		}
	}

	public Set<Cacheable> valueSet() {
		Set<Cacheable> values = new HashSet<Cacheable>();
		for (Object key : keySet()) {
			values.add( get(key) );
		}
		return values;
	}
}
