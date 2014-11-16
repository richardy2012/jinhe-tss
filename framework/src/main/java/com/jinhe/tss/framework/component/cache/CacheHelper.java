package com.jinhe.tss.framework.component.cache;

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

}
