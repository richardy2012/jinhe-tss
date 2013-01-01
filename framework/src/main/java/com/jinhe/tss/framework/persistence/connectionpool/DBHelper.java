/* ==================================================================   
 * Created [2007-1-3] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2012-2015 
 * ================================================================== 
*/

package com.jinhe.tss.framework.persistence.connectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.jinhe.tss.util.ConfigurableContants;

/** 
 * 数据库相关常见操作的帮助类。
 * 
 */
public class DBHelper extends ConfigurableContants {

    public static final String DB_CONNECTION_DRIVER_CLASS = "db.connection.driver_class";
    public static final String DB_CONNECTION_URL      = "db.connection.url";
    public static final String DB_CONNECTION_USERNAME = "db.connection.username";
    public static final String DB_CONNECTION_PASSWORD = "db.connection.password";

    public static Connection getConnection() {
        return getConnection(properties);
    }
    
    public static Connection getConnection(Properties p) {
        Connection conn = null;
        try {
            Class.forName(p.getProperty(DB_CONNECTION_DRIVER_CLASS));
            conn = DriverManager.getConnection(p.getProperty(DB_CONNECTION_URL),
                    p.getProperty(DB_CONNECTION_USERNAME),
                    p.getProperty(DB_CONNECTION_PASSWORD));
            
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("创建数据库连接时候出错", e);
        } catch (SQLException e) {
            throw new RuntimeException("创建数据库连接时候出错", e);
        }
        return conn;
    }
    
    public static long executeSQL(Connection connection, String sql) {
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            try {
                ResultSet resultSet = st.executeQuery();
                resultSet.next();
                return resultSet.getLong(1);
            } catch (SQLException sqle) {
                throw new RuntimeException("执行SQL语句时出错！", sqle);
            } finally {
                st.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("执行SQL语句时出错！", e);
        }
    }
}
