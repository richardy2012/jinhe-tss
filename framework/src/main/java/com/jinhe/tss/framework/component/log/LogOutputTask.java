package com.jinhe.tss.framework.component.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.jinhe.tss.cache.extension.workqueue.RecordsOutputTask;
import com.jinhe.tss.framework.Config;

/** 
 *　日志输出任务
 * 
 * @author 金普俊 2007-1-8
 */
public class LogOutputTask extends RecordsOutputTask {

    protected void createRecords(Connection conn) throws SQLException {
    	String insertSql;
    	if( Config.isH2Database() ) {
    		// H2：跑单元测试用，无需关心ID，DEFAULT nextval('pms_flowrate_sequence') 
    		insertSql = "insert into COMPONENT_LOG" +
	    		"(appCode, operatorId, operatorName, operatorIP, operationCode, operateTable, operateTime, content) " +
	    		"values(?, ?, ?, ?, ?, ?, ?, ?)"; 
    	} 
    	else {
    		// Oracle or 其他正式数据库
    		insertSql = "insert into COMPONENT_LOG" +
	            "(id, appCode, operatorId, operatorName, operatorIP, operationCode, operateTable, operateTime, content) " +
	            "values(log_sequence.nextval, ?, ?, ?, ?, ?, ?, ?, ?)";
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


