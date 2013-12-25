/* ==================================================================   
 * Created [2007-1-3] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2012-2015 
 * ================================================================== 
*/

package com.jinhe.tss.framework.persistence.connpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.jinhe.tss.util.ConfigurableContants;

/** 
 * 数据库相关常见操作的帮助类。
 */
public class DBHelper extends ConfigurableContants {

    public static final String DB_CONNECTION_DRIVER_CLASS = "db.connection.driver_class";
    public static final String DB_CONNECTION_URL      = "db.connection.url";
    public static final String DB_CONNECTION_USERNAME = "db.connection.username";
    public static final String DB_CONNECTION_PASSWORD = "db.connection.password";

    static {
        // 默认载入application.properties
        properties = init(DEFAULT_PROPERTIES);
    }
    
    public static Connection getConnection() {
        return getConnection(properties);
    }
    
    public static Connection getConnection(Properties p) {
        Connection conn = null;
        String url = p.getProperty(DB_CONNECTION_URL);
        try {
            Class.forName(p.getProperty(DB_CONNECTION_DRIVER_CLASS));
			conn = DriverManager.getConnection(url,
                    p.getProperty(DB_CONNECTION_USERNAME),
                    p.getProperty(DB_CONNECTION_PASSWORD));
            
        } catch (Exception e) {
            throw new RuntimeException("创建数据库连接时候出错，url：" + url, e);
        } 
        return conn;
    }
    
    public static long executeCountSQL(Connection connection, String sql, Object...params) {
    	return (Long) executeQuerySQL(connection, sql, params);
    }
    
    public static Object executeQuerySQL(Connection connection, String sql, Object...params) {
    	PreparedStatement st = null;
        try {
            st = connection.prepareStatement(sql);
            
            if(params != null) {
            	int index = 1;
            	for(Object param : params) {
            		st.setObject(index++, param);
            	}
            }
            
            ResultSet rs = st.executeQuery();
            rs.next();
            return rs.getObject(1);
            
        } catch (Exception e) {
            throw new RuntimeException("执行SQL语句时出错，sql：" + sql, e);
        } finally {
            try {
            	if( st != null) {
            		st.close();
            	}
			} catch (SQLException e) {
				log.error("执行完SQL后关闭PreparedStatement出错，sql：" + sql, e);
			}
        }
    }
}
