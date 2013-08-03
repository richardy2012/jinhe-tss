package com.jinhe.tss.cache;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jinhe.tss.cache.extension.threadpool.IThreadPool;

public class JCacheTest {
	
	@Test
	public void testInitCache() {
		JCache cache = JCache.getInstance();
		IThreadPool tpool = cache.getThreadPool();
		assertNotNull(tpool);
	}
	
}
