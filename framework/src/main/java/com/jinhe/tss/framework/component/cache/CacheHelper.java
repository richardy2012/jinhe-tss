package com.jinhe.tss.framework.component.cache;

import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.Pool;

public class CacheHelper {
	
	public static Pool getShortCache() {
		return JCache.getInstance().getPool(CacheLife.SHORT.toString());
	}
	
	public static Pool getLongCache() {
		return JCache.getInstance().getPool(CacheLife.LONG.toString());
	}
	
	public static Pool getNoDeadCache() {
		return JCache.getInstance().getPool(CacheLife.NODEAD.toString());
	}

}
