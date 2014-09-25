package com.jinhe.tss.framework.persistence.connpool;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.jinhe.tss.cache.Cacheable;
import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.extension.ReusablePool;
import com.jinhe.tss.framework.persistence.connpool._Connection.DatasourceConnectionProvider;
import com.jinhe.tss.framework.persistence.connpool._Connection.IConnectionProvider;

public class ConnpoollTest {
	
	@Test
	public void test() {
		ReusablePool connpool = (ReusablePool) JCache.getInstance().getConnectionPool();
		
		Cacheable connItem = connpool.checkOut(0);
		
		connpool.destroyObject(connItem);
		connpool.destroyObject(null);
		
		Assert.assertTrue(connpool.getCustomizer().isValid(connItem) == false);
		
		connpool.reload(connItem);
		
		Assert.assertTrue(connpool.getCustomizer().isValid(connItem) == true);
		
		IConnectionProvider provider = new DatasourceConnectionProvider();
		try {
			provider.getConnection(new Properties());
		} catch(Exception e) {
		}
	}

}
