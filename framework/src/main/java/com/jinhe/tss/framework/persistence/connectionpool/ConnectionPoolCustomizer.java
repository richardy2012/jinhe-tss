/* ==================================================================   
 * Created [2007-1-9] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@gmail.com
 * Copyright (c) Jon.King, 2012-2015 
 * ================================================================== 
 */
package com.jinhe.tss.framework.persistence.connectionpool;

import java.sql.Connection;
import java.sql.SQLException;

import com.jinhe.tss.cache.Cacheable;
import com.jinhe.tss.cache.TimeWrapper;
import com.jinhe.tss.cache.extension.DefaultCustomizer;

/**
 * 数据库连接池自定义类。<br/>
 * 
 * 在本类中定义了如何创建、验证、销毁数据库连接。
 * 
 */
public class ConnectionPoolCustomizer extends DefaultCustomizer {
    
	public Cacheable create() {
		Connection conn = _Connection.getInstanse(strategy.paramFile).getConnection();
		String cacheKey = TimeWrapper.createRandomKey("Connection");
		
		// 包装新创建的Connection，赋予其生命周期。
		return  new TimeWrapper(cacheKey, conn, strategy.cyclelife);
	}

	public boolean isValid(Cacheable o) {
		Connection conn = (Connection) o.getValue();
		try {
			return !conn.isClosed();
		} catch (SQLException e) {
			return false;
		}
	}

	public void destroy(Cacheable o) {
		if (o == null)
			return;

		Connection conn = (Connection) o.getValue();
		_Connection.getInstanse(strategy.paramFile).releaseConnection(conn);
		o = null;
	}

	public Cacheable reloadCacheObject(Cacheable item) {
		return create();
	}
}
