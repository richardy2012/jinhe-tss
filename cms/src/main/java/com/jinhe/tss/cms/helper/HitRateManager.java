package com.jinhe.tss.cms.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.jinhe.tss.cache.extension.workqueue.OutputRecordsManager;
import com.jinhe.tss.cache.extension.workqueue.RecordsOutputTask;

/** 
 * 文章点击率统计
 * 
 */
public class HitRateManager extends OutputRecordsManager{
    
    private static HitRateManager manager;
    
    private HitRateManager() { }
    
    public static HitRateManager getInstanse(){
        if(manager == null) {
            manager = new HitRateManager();
        }
        return manager;
    }
    
    protected void excuteTask(List<Object> temp) {
        RecordsOutputTask task = new RecordsOutputTask() {
            protected void createRecords(Connection conn) throws SQLException {
                String sql = "update cms_article t set t.hitCount = t.hitCount + 1 where t.id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                for ( Object temp : records ) {
                    pstmt.setLong(1, (Long) temp );
                    pstmt.execute();
                }
                pstmt.close();
            }
        };
        task.fill(temp);

        tpool.excute(task);
    }
    
    public void output(Object record){
        super.output(record);
    }
    
    protected int getMaxSize(){ return 32; }
}

