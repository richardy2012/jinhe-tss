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
import com.jinhe.tss.cache.CacheCustomizer;
import com.jinhe.tss.cache.TimeWrapper;

/**
 * 数据库连接池自定义类。<br/>
 * 
 * 在本类中定义了如何创建、验证、销毁数据库连接。
 * 
 */
public class ConnectionPoolCustomizer implements CacheCustomizer {

	public Cacheable create(Long cyclelife) {
		Connection conn = _Connection.getInstanse().getConnection();
		String cacheKey = TimeWrapper.createRandomKey("Connection");
		
		// 包装新创建的Connection，赋予其生命周期。
		return  new TimeWrapper(cacheKey, conn, cyclelife);
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
		_Connection.getInstanse().releaseConnection(conn);
		o = null;
	}

	public Cacheable reloadCacheObject(Cacheable item) {
		Long cyclelife = item.getCyclelife();
		return create(cyclelife);
	}
}
