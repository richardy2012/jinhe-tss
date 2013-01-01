package com.jinhe.tss.cache;

import junit.framework.TestCase;

import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.extension.threadpool.IThreadPool;

public class JCacheTest extends TestCase {
	
	public void testInitCache() {
		JCache cache = JCache.getInstance();
		IThreadPool tpool = cache.getThreadPool();
		assertNotNull(tpool);
	}
	
}
