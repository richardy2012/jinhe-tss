package com.jinhe.tss.framework.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.h2.tools.Server;
import org.springframework.stereotype.Component;

@Component
public class H2DBServer implements IH2DBServer{  
    
    protected Logger log = Logger.getLogger(this.getClass());
    
    private Server server;  
    
    public String URL = "jdbc:h2:mem:h2db;DB_CLOSE_DELAY=-1"; // Connection关闭时不停用H2 server
    public String user = "sa";  
    public String password = "123";  
    public static int PORT = 9081;  
    
    boolean isPrepareed = false;
    
    Connection conn;
    
    public H2DBServer() {
        try {  
            log.info("正在启动H2 database...");  
            
            // 此时H2数据库只起来了服务，没有实例
            server = Server.createTcpServer(new String[] { "-tcpPort", (PORT++) + ""}).start();  
            
            // 在以URL取得连接以后，数据库实例h2db才创建完成
            Class.forName("org.h2.Driver");  
            conn = DriverManager.getConnection(URL, user, password);  
            
        } catch (Exception e) {  
            log.info("启动H2 database出错：" + e.toString());  
        }  
    }
 
    
    public void stopServer() {  
        if (server != null) {  
            log.info("正在关闭H2 database...");  
            
            try {
                conn.close();
            } catch (SQLException e) {
                log.info("关闭H2 database连接出错：" + e.toString());  
                throw new RuntimeException(e);  
            }  
            server.shutdown();
            server.stop();
            
            log.info("关闭H2 database成功.");  
        }  
    }  
    
    public static void main(String[] args) {  
        H2DBServer h2 = new H2DBServer();  
        h2.stopServer();  
    }

    public boolean isPrepareed() {
        return isPrepareed;
    }

    public void setPrepareed(boolean isPrepareed) {
        this.isPrepareed = isPrepareed;
    }  
}  
