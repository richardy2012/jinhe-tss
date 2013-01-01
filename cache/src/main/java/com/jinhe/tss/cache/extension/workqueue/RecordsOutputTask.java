/* ==================================================================   
 * Created [2007-2-15] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2012-2015 
 * ================================================================== 
 */
package com.jinhe.tss.cache.extension.workqueue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.jinhe.tss.cache.Cacheable;
import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.Pool;

/**
 * 输出记录到数据库的任务抽象超类。<br/>
 * 类似日志输出、访问量输出可以通过继承该超类实现。
 * 
 */
public abstract class RecordsOutputTask implements Task {

	protected Logger log = Logger.getLogger(this.getClass());

	private static int count = 0;
	private String name;

	public RecordsOutputTask() {
		count ++;
		String className = this.getClass().getName();
		name = className.substring(className.lastIndexOf(".") + 1) + "-" + count;
	}

	public String toString() {
		return name;
	}

	protected List<Object> records;

	public void fill(List<Object> records) {
		this.records = records;
	}

	public void recycle() throws Exception {
		this.records = null;
	}

	public void excute() {
		if (records == null)
			return;

		Pool connectionPool = JCache.getInstance().getConnectionPool();
		Cacheable connItem = connectionPool.checkOut(0);
		Connection conn = (Connection) connItem.getValue();
		try {
			createRecords(conn);
		} catch (SQLException e) {
			log.error("写入记录到数据库时候出错", e);
		} finally {
			connectionPool.checkIn(connItem);
		}
	}

	protected abstract void createRecords(Connection conn) throws SQLException;

}
