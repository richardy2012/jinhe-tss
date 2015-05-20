package com.jinhe.tss.framework.test;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;

import com.jinhe.tss.cache.Cacheable;
import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.Pool;
import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.component.log.LogQueryCondition;
import com.jinhe.tss.framework.component.log.LogService;
import com.jinhe.tss.framework.persistence.IDao;
import com.jinhe.tss.framework.persistence.pagequery.PageInfo;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.URLUtil;

public class TestUtil {
	
	protected static Logger log = Logger.getLogger(TestUtil.class);
	
	static final String PROJECT_NAME = "jinhe-tss";
	
	public static String getTempDir() {
		return System.getProperty("java.io.tmpdir");
	}
	
	public static String getProjectDir() {
        String path = URLUtil.getResourceFileUrl("application.properties").getPath();
        
        int beginIndex = path.startsWith("/") ? 0 : 1; // linux or windows
        return path.substring(beginIndex, path.indexOf(PROJECT_NAME) + PROJECT_NAME.length());
    }
	
    public static String getInitSQLDir() {
    	String dbType = "mysql";
    	if( Config.isH2Database() ) {
    		dbType = "h2";
    	}
        return getProjectDir() + "/webapp/sql/" + dbType;
    }
    
    public static String getSQLDir() {
        return getProjectDir() + "/webapp/sql";
    }
    
    public static void excuteSQL(String sqlDir) {  
    	excuteSQL(sqlDir, true);
    }
    
    public static void excuteSQL(String sqlDir, boolean isTSS) {  
        log.info("正在执行目录：" + sqlDir+ "下的SQL脚本。。。。。。");  
        
        Pool connectionPool = JCache.getInstance().getConnectionPool();
		Cacheable connItem = connectionPool.checkOut(0);
		
        try {  
        	Connection conn = (Connection) connItem.getValue();
            Statement stat = conn.createStatement();  
            
            List<File> sqlFiles = FileHelper.listFilesByTypeDeeply(".sql", new File(sqlDir));
            for(File sqlFile : sqlFiles) {
            	String fileName = sqlFile.getName();
				if(isTSS && "roleusermapping-init.sql".equals(fileName)) {
                    continue; 
                }
            	
            	log.info("开始执行SQL脚本：" + fileName+ "。");  
            	
                String sqls = FileHelper.readFile(sqlFile, "UTF-8");
                String[] sqlArray = sqls.split(";");
                for(String sql : sqlArray) {
                	if( EasyUtils.isNullOrEmpty(sql) ) continue;
                	
                	log.debug(sql);  
                	stat.execute(sql);
                }
				
                log.info("SQL脚本：" + fileName+ " 执行完毕。");  
            }
 
            log.info("成功执行目录：" + sqlDir+ "下的SQL脚本!");
            
            stat.close();  
            conn.close();  
        } catch (Exception e) {  
            throw new RuntimeException("目录：" + sqlDir+ "下的SQL脚本执行出错：", e);
        } finally {
        	connectionPool.checkIn(connItem);
        }
    }
    
    public static int printLogs(LogService logService) {
        try {
            Thread.sleep(1*1000); // 休息一秒，等日志生成
        } catch (InterruptedException e) {
        } 
        
        LogQueryCondition condition = new LogQueryCondition();
        
        PageInfo result = logService.getLogsByCondition(condition);
        List<?> logs = result.getItems();
        Integer logCount = (Integer) result.getTotalRows();
        
        log.debug("本次测试共生成了 " + logCount + " 条日志");
        for(Object temp : logs) {
            log.debug(temp);
        }
        log.debug("\n");
        
        return logs.size();
    }
    
    public static void printEntity(IDao<?> dao, String entity) {
        List<?> list = dao.getEntities("from " + entity );
        
        log.debug("表【" + entity + "】的所有记录:");
        for(Object temp : list) {
            log.debug(temp);
        }
        log.debug("\n");
    }
}
