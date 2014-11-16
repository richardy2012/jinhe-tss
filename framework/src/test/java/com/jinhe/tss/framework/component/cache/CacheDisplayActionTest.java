package com.jinhe.tss.framework.component.cache;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.Pool;
import com.jinhe.tss.framework.component.param.TxTestSupportParam;

public class CacheDisplayActionTest extends TxTestSupportParam {
	
	@Autowired private CacheDisplayAction action;
 
    @Test
	public void testCacheAction() {
		action.getAllCacheStrategy4Tree(response);
		
		action.getPoolsGrid(response);
		
		String poolCode = "threadpool";
		action.getCacheStrategyInfo(response, poolCode);
		
		action.releaseCache(response, poolCode);
		action.initPool(response, poolCode);
		
		action.viewCachedItem(response, poolCode, "WorkThread_6");
		action.viewCachedItem(response, poolCode, "not-exist-key");
		
		action.removeCachedItem(response, poolCode, "WorkThread_6");
		action.removeCachedItem(response, poolCode, "not-exist-key-2");
	}
    
    @Test
	public void testModifyCacheConfig() {
		action.init();
		action.modifyCacheConfig(response, "connectionpool", "{\"poolSize\":\"20\", \"initNum\":\"10\"}");
		action.modifyCacheConfig(response, "connectionpool", "{\"poolSize\":\"8\", \"initNum\":\"6\"}");
		action.modifyCacheConfig(response, "connectionpool", "{\"poolSize\":\"8\", \"cyclelife\":\"10000\"}");
		action.modifyCacheConfig(response, "connectionpool", "{\"poolSize\":\"8\", \"cyclelife\":\"10000\", \"poolContainerClass\":\"com.jinhe.tss.cache.extension.EhcacheContainer\"}");
		
		JCache jCache = JCache.getInstance();
		Pool pool = jCache.getConnectionPool();
		for(int i = 0; i < 8; i++) {
			pool.checkOut(0);
		}
		
		try {
			pool.checkOut(0);
			Assert.fail("should throw exception but didn't.");
		} catch(Exception e) {
			Assert.assertTrue("缓存池【服务数据缓存（短期）】已满，且各缓存项都处于使用状态，需要等待。可考虑重新设置缓存策略！", true);
		}
		
		action.modifyCacheConfig(response, "connectionpool", "{\"poolSize\":\"10\", \"initNum\":\"6\"}");
		pool.checkOut(0);
		pool.checkOut(0);
		
		action.modifyCacheConfig(response, "connectionpool", "{\"poolSize\":\"8\", \"cyclelife\":\"10000\", \"poolContainerClass\":\"com.jinhe.tss.cache.extension.MapContainer\"}");
		
		CacheDisplayAction action2 = new CacheDisplayAction();
		action2.paramService = action.paramService;
		
		action2.init();
		jCache.getConnectionPool().checkOut(0);
		
		action2.getCacheStrategyInfo(response, "connectionpool");
	}
    
}
