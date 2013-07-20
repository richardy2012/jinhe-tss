package com.jinhe.tss.framework.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.log4j.Logger;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.component.log.LogQueryCondition;
import com.jinhe.tss.framework.component.log.LogService;
import com.jinhe.tss.framework.persistence.IDao;
import com.jinhe.tss.framework.persistence.connpool.DBHelper;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.URLUtil;

public class TestUtil {
	
	protected static Logger log = Logger.getLogger(TestUtil.class);
	
	static final String PROJECT_NAME = "jinhe-daodao";
	
	public static String getProjectDir() {
        String path = URLUtil.getResourceFileUrl("com/jinhe/tss").getPath();
        
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
        log.info("正在执行目录：" + sqlDir+ "下的SQL脚本。。。。。。");  
        try {  
            Connection conn = DBHelper.getConnection();
            Statement stat = conn.createStatement();  
            
            List<File> sqlFiles = FileHelper.listFilesByTypeDeeply(".sql", new File(sqlDir));
            for(File sqlFile : sqlFiles) {
            	String fileName = sqlFile.getName();
				if("roleusermapping-init.sql".equals(fileName)) {
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
            e.printStackTrace();  
            throw new RuntimeException("目录：" + sqlDir+ "下的SQL脚本执行出错：", e);
        }
    }
    
    public static int printLogs(LogService logService) {
        try {
            Thread.sleep(1*1000); // 休息一秒，等日志生成
        } catch (InterruptedException e) {
        } 
        
        LogQueryCondition condition = new LogQueryCondition();
        Object[] result = logService.getLogsByCondition(condition);
        
        List<?> logs = (List<?>) result[0];
        Integer logCount = (Integer) result[1];
        
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
    
    public static void excuteMethod(HttpMethod method) throws IOException {  
        method.setRequestHeader("Content-Type", "application/xml");    
        
        HttpClient httpClient = new HttpClient();  
        int statusCode = httpClient.executeMethod(method);  
        if (statusCode == HttpStatus.SC_NO_CONTENT) {  
            System.out.print("No Content 没有新文档，浏览器应该继续显示原来的文档。");
        }
        if (statusCode != HttpStatus.SC_OK) {  
            System.out.println("Method failed: " + method.getStatusLine());  
            return;
        } 
        
        byte[] responseBody = method.getResponseBody();  
        System.out.println(new String(responseBody)); 
    }  
    
    
    public static void doGet(String url) throws IOException {
        GetMethod method = new GetMethod(url);
        excuteMethod(method);
    }
    
    public static void doDelete(String url) throws IOException {
        DeleteMethod method = new DeleteMethod(url);
        excuteMethod(method);
    }
    
    public static void doPut(String url, String paramData) throws IOException {
        PutMethod method = new PutMethod(url);
        
        // 设置请求内容，将原请求中的数据流转给新的请求
        byte[] b = paramData.getBytes("UTF-8");  
        InputStream is = new ByteArrayInputStream(b, 0, b.length);  
        RequestEntity requestEntity = new InputStreamRequestEntity(is, 
                "application/xop+xml; charset=UTF-8; type=\"text/xml\"");
        method.setRequestEntity(requestEntity);
        
        excuteMethod(method);
    }  

    public static void doPost(String url, String paramData) throws IOException {
        PostMethod method = new PostMethod(url);
        
        // 设置请求内容，将原请求中的数据流转给新的请求
        byte[] b = paramData.getBytes("UTF-8");  
        InputStream is = new ByteArrayInputStream(b, 0, b.length);  
        RequestEntity requestEntity = new InputStreamRequestEntity(is, 
                "application/xml; charset=UTF-8; type=\"text/xml\"");
        method.setRequestEntity(requestEntity);
        
        excuteMethod(method);
    } 
}
