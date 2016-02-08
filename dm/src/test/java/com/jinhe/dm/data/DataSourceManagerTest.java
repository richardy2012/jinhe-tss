package com.jinhe.dm.data;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.dm.TxTestSupport4DM;

public class DataSourceManagerTest extends TxTestSupport4DM {
	
	@Autowired DataSourceManager dsManager;

	@Test
	public void test() {

		dsManager.getCacheConfigs();
		
		String code = "connpool-h2";
		String name = "测试数据源";
		String value = "{\"customizerClass\": \"com.jinhe.tss.framework.persistence.connpool.ConnPoolCustomizer\",  " +
				" \"poolClass\": \"com.jinhe.tss.cache.extension.ReusablePool\",   " +
				" \"code\": \"connpool-h2\",  " +
				" \"name\": \"测试数据源\",   " +
				" \"cyclelife\": \"180000\",  " +
				" \"paramFile\": \"org.h2.Driver,jdbc:h2:mem:h2db;DB_CLOSE_DELAY=-1;LOCK_MODE=0,sa,123\",   " +
				" \"interruptTime\": \"1000\",    \"initNum\":\"0\",    \"poolSize\": \"10\"}";
		
		// create
		dsManager.configConnpool(code, name, value);
		
		// update
		dsManager.configConnpool(code, name, value);
		
		String driver = "org.h2.Driver";
		String url  = "jdbc:h2:mem:h2db;DB_CLOSE_DELAY=-1;LOCK_MODE=0";
		String user = "sa";
		String pwd  = "123";
		dsManager.testConn(driver, url, user, pwd);
	}
	
    protected String getDefaultSource(){
    	return "connectionpool";
    }
}
