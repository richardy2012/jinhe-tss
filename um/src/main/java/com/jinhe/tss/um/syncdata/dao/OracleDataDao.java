package com.jinhe.tss.um.syncdata.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.um.syncdata.SyncDataHelper;

/** 
 *  从Oracle数据里同步用户组织信息
 */
public class OracleDataDao extends BaseDBDataDao{
 
    protected Connection getConnection(Map<String, String> map){
        Connection conn = null;
        String url = (String) map.get(SyncDataHelper.URL);
        String userName = (String) map.get(SyncDataHelper.USERNAME);
        String password = (String) map.get(SyncDataHelper.PASSWORD);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            throw new BusinessException("连接外部Oracle数据库失败,请检查连接参数", e);
        } catch (ClassNotFoundException e) {
            throw new BusinessException("连接外部Oracle数据库失败,驱动程序找不到", e);
        }
        return conn;
    }
}

