package com.jinhe.tss.framework.component.cache;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.TxTestSupport;

public class CacheDisplayActionTest extends TxTestSupport {
	
	@Autowired private CacheDisplayAction action;

    @Test
	public void testCacheAction() {
		action.getAllCacheStrategy4Tree(response);
		
		String poolCode = "threadpool";
		action.getCacheStrategyInfo(response, poolCode);
		action.getCacheStrategyInfo(response, "not-exist-key");
		
		action.releaseCache(response, poolCode);
		action.initPool(response, poolCode);
		
		action.viewCachedItem(response, poolCode, "WorkThread_3");
		
		action.viewCachedItem(response, poolCode, "not-exist-key");
	}
    
}