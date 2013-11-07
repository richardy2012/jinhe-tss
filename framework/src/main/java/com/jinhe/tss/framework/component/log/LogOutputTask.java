package com.jinhe.tss.framework.component.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.persistence.connpool.Output2DBTask;

/** 
 *　日志输出任务
 * 
 * @author 金普俊 2007-1-8
 */
public class LogOutputTask extends Output2DBTask {

    protected void createRecords(Connection conn) throws SQLException {
    	String insertSql;
    	if( Config.isOracleDatabase() ) {
            insertSql = "insert into COMPONENT_LOG" +
                "(id, appCode, operatorId, operatorName, operatorIP, operationCode, operateTable, operateTime, content) " +
                "values(log_sequence.nextval, ?, ?, ?, ?, ?, ?, ?, ?)";
        }
    	else { // DEFAULT 主键自增 
            insertSql = "insert into COMPONENT_LOG" +
                "(appCode, operatorId, operatorName, operatorIP, operationCode, operateTable, operateTime, content) " +
                "values(?, ?, ?, ?, ?, ?, ?, ?)"; 
    	}
        PreparedStatement pstmt = conn.prepareStatement(insertSql); 
        for ( Object temp : records ) {
            Log dto = (Log) temp;
            
            int index = 1;
            pstmt.setString(index++, dto.getAppCode());
            pstmt.setLong  (index++, dto.getOperatorId());
            pstmt.setString(index++, dto.getOperatorName());
            pstmt.setString(index++, dto.getOperatorIP());
            pstmt.setString(index++, dto.getOperationCode());
            pstmt.setString(index++, dto.getOperateTable());
            pstmt.setTimestamp(index++, new java.sql.Timestamp(dto.getOperateTime().getTime()));
            pstmt.setString(index++, dto.getContent());
            
            pstmt.execute();
        }
        
        pstmt.close();
    }
}


