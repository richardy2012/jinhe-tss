package com.jinhe.tss.framework.component.cache;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.TxTestSupport;

public class CacheDisplayActionTest extends TxTestSupport {
	
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
    
}
