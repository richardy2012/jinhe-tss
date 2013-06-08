package com.jinhe.tss.portal.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import com.jinhe.tss.cache.extension.workqueue.OutputRecordsManager;
import com.jinhe.tss.cache.extension.workqueue.RecordsOutputTask;
import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.persistence.connpool.DBHelper;
import com.jinhe.tss.portal.entity.FlowRate;

/** 
 * <p> FlowrateManager.java </p> 
 * 页面流量访问统计
 */
public class FlowrateManager extends OutputRecordsManager{
    
    private static FlowrateManager manager;
    
    private FlowrateManager(){
    }
    
    public static FlowrateManager getInstanse(){
        if(manager == null)
            manager = new FlowrateManager();
        return manager;
    }
    
    protected void excuteTask(List<Object> temp) {
        OutputFlowrateTask task = new OutputFlowrateTask();
        task.fill(temp);

        tpool.excute(task);
    }
    
    public void output(Object record){
        super.output(record);
        //super.flush();
    }
    
    public static class OutputFlowrateTask extends RecordsOutputTask {

        protected void createRecords(Connection conn) throws SQLException {
        	String insertSql;
        	if( Config.isOracleDatabase() ) {
        	    insertSql = "insert into pms_flowrate(id, pagedId, ip, visitTime) values(pms_flowrate.nextval, ?, ?, ?)";
        	} 
        	else {
        	    insertSql = "insert into pms_flowrate(pageId, ip, visitTime) values(?, ?, ?)";
        	}
            
            PreparedStatement pstmt = conn.prepareStatement(insertSql);
            for (Iterator<?> it = records.iterator(); it.hasNext();) {
                FlowRate temp = (FlowRate) it.next();
                
                if(checkIsTheSameVisitor(conn, temp))
                    continue;
                
                int index = 1;
                pstmt.setLong(index++, temp.getPageId());
                pstmt.setString(index++, temp.getIp());
                pstmt.setTimestamp(index++, new Timestamp(temp.getVisitTime().getTime()));
                
                pstmt.execute();
            }
            pstmt.close();
        }

        /**
         * 如果数据库中已经有这样一条记录：和当前访问相同的页面且相同的IP，并且相隔时间在三分钟内
         * @param conn
         * @param temp
         * @return
         */
        private boolean checkIsTheSameVisitor(Connection conn, FlowRate temp) {
            String sql = "select count(*) from pms_flowrate t where t.ip ='"
                    + temp.getIp() + "' and t.pageId = " + temp.getPageId() 
                    + " and t.visitTime between sysdate - 1/480 and sysdate ";
            return DBHelper.executeSQL(conn, sql) != 0;
        }
    }
}

