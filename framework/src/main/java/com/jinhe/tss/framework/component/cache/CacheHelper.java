package com.jinhe.tss.framework.component.cache;

import java.util.Set;

import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.Pool;

public class CacheHelper {
	
	public final static String CACHE_PARAM = "CACHE_PARAM";
	
	public static Pool getShorterCache() {
		return JCache.getInstance().getPool(CacheLife.SHORTER.toString());
	}
	
	public static Pool getShortCache() {
		return JCache.getInstance().getPool(CacheLife.SHORT.toString());
	}
	
	public static Pool getLongCache() {
		return JCache.getInstance().getPool(CacheLife.LONG.toString());
	}
	
	public static Pool getLongerCache() {
		return JCache.getInstance().getPool(CacheLife.LONGER.toString());
	}
	
	public static Pool getNoDeadCache() {
		return JCache.getInstance().getPool(CacheLife.NODEAD.toString());
	}
	
	public static void flushLongCache(String likeKey) {
		Pool pool = CacheHelper.getLongCache();
		Set<Object> keys = pool.listKeys();
		for(Object _key : keys) {
			if(_key.toString().indexOf(likeKey) >= 0) {
				pool.removeObject(_key);
			}
		}
	}
}
