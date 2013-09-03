package com.jinhe.tss.cache;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.jinhe.tss.cache.extension.mixin.IDisable;
import com.jinhe.tss.cache.extension.threadpool.IThreadPool;

public class JCacheTest {
	
	JCache cache;
			
	@Before
	public void setUp() {
		cache = JCache.getInstance();
	}
	
	@Test
	public void testGetThreadPool() {
		IThreadPool tpool = cache.getThreadPool();
		assertNotNull(tpool);
	}
	
	@Test
	public void testGetTaskPool() {
		Pool taskpool = cache.getTaskPool();
		Cacheable taskItem = taskpool.checkOut(0);
		assertNotNull(taskItem.getAccessed());
		assertEquals(1, taskItem.getHit());
		assertEquals(false, taskItem.isExpired());
		
		Object key = taskItem.getKey();
		assertNotNull(key);
		assertNotNull(taskItem.getValue());
		
		taskpool.checkIn(taskItem);
		
		taskItem = taskpool.getObject(key);
		
		taskItem = taskpool.removeObject(key);
		
		assertTrue(taskpool.getHitRate() > 0);
	    
		// test Mixin
		IDisable poolMixin = (IDisable)taskpool;
		poolMixin.stop();
		
		taskItem = taskpool.checkOut(0);
		assertNotNull(taskItem.getAccessed());
		assertEquals(0, taskItem.getHit());
		assertEquals(false, taskItem.isExpired());
		
		key = taskItem.getKey();
		assertNotNull(key);
		assertNotNull(taskItem.getValue());
		
		taskpool.checkIn(taskItem);
		
		taskItem = taskpool.getObject(key);
		
		taskpool.putObject(key, taskItem.getValue());
		
		taskpool.init();
		
		poolMixin.start();
	}
}
