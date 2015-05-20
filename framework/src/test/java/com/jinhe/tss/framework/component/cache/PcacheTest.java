package com.jinhe.tss.framework.component.cache;

import org.junit.Assert;
import org.junit.Test;

import com.jinhe.tss.cache.Cacheable;
import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.Pool;
import com.jinhe.tss.framework.component.param.Param;
import com.jinhe.tss.framework.component.param.ParamConstants;
import com.jinhe.tss.framework.component.param.TxTestSupportParam;

public class PcacheTest extends TxTestSupportParam {
	
	String poolConfig1 = "{" +
			"\"customizerClass\":\"com.jinhe.tss.framework.persistence.connpool.ConnPoolCustomizer\"," +
			"\"poolClass\":\"com.jinhe.tss.cache.extension.ReusablePool\"," +
			"\"code\":\"pool_1\"," +
			"\"name\":\"DB连接池-1\"," +
			"\"cyclelife\":\"180000\"," +
			"\"paramFile\":\"H2.properties\"," +
			"\"interruptTime\":\"1000\"," +
			"\"poolSize\":\"10\"" +
			"}";
	
	String poolConfig2 = "{" +
			"\"customizerClass\":\"com.jinhe.tss.framework.persistence.connpool.ConnPoolCustomizer\"," +
			"\"poolClass\":\"com.jinhe.tss.cache.extension.ReusablePool\"," +
			"\"code\":\"pool_2\"," +
			"\"name\":\"DB连接池-2\"," +
			"\"cyclelife\":\"180000\"," +
			"\"paramFile\":\"org.h2.Driver,jdbc:h2:mem:h2db;DB_CLOSE_DELAY=-1,sa,123\"," +
			"\"interruptTime\":\"1000\"," +
			"\"poolSize\":\"10\"" +
			"}";
	
	@Test
	public void test() {
		Param cacheGroup = paramService.getParam(CacheHelper.CACHE_PARAM);
		if(cacheGroup == null) {
			cacheGroup = new Param();
			cacheGroup.setName("缓存池配置");
			cacheGroup.setCode(CacheHelper.CACHE_PARAM);
			cacheGroup.setParentId(ParamConstants.DEFAULT_PARENT_ID);
			cacheGroup.setType(ParamConstants.GROUP_PARAM_TYPE);
	        paramService.saveParam(cacheGroup);
		}
		
		Long parentId = cacheGroup.getId();
		
		Param param1 = addSimpleParam(parentId, "pool_1", "pool_1", poolConfig1);
		Param param2 = addSimpleParam(parentId, "pool_2", "pool_2", poolConfig2);
		
		Pool pool1 = JCache.getInstance().getPool("pool_1");
		Pool pool2 = JCache.getInstance().getPool("pool_2");
		
		Assert.assertNotNull(pool1);
		Assert.assertNotNull(pool2);
		
		Cacheable connItem = pool1.checkOut(0);
		Assert.assertNotNull(connItem);
		Assert.assertNotNull(connItem.getValue());
		
		connItem = pool2.checkOut(0);
		Assert.assertNotNull(connItem);
		Assert.assertNotNull(connItem.getValue());
		
		paramService.saveParam(param1);
		paramService.delete(param2.getId());
	}
}
