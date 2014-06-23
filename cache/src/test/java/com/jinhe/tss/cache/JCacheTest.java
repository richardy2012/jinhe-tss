package com.jinhe.tss.cache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JCacheTest {
	
	JCache cache;
			
	@Before
	public void setUp() {
		cache = JCache.getInstance();
	}
	
	/**
	 * 测试简单池
	 */
	@Test
	public void testJCache() {
		Assert.assertTrue( cache.listCachePools().size() == 6 );
		
		Assert.assertNotNull(cache.getThreadPool());
		Assert.assertNull(cache.getConnectionPool());
		Assert.assertNull(cache.getPool(null));
		Assert.assertNull(cache.getPool("NotExsit"));
	}
	
	@Test
	public void testAbstractPool() {
		Pool pool = cache.getTaskPool();
		pool.release(true);
		pool.init();
		
		try {
			Thread.sleep(3000); // 等待初始化完成
		} catch (InterruptedException e) {
		}
		
		Assert.assertTrue( pool.listItems().size() == 10 );
		Assert.assertTrue( pool.listKeys().size() == 10 );
		
		Assert.assertTrue( pool.getHitRate() == 0d );
		Assert.assertTrue( pool.getRequests() == 0 );
		
		for(int i = 0; i < 10; i++) {
			Assert.assertNotNull(pool.remove());
		}
		
		Assert.assertNull(pool.remove());
		
		Assert.assertNull(pool.putObject(null, new Object()));
	}
}
