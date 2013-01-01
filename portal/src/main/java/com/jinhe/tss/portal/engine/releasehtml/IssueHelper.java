package com.jinhe.tss.portal.engine.releasehtml;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jinhe.tss.framework.Config;
 
public class IssueHelper {
    
    private static Logger log = Logger.getLogger(IssueHelper.class);
    
    /**
     * 创建文件夹，如果父级目录不存在，则创建父级目录
     * @param file
     */
    static void makeDir(File file){
        List<File> parents = new ArrayList<File>();
        File temp = file.getParentFile();
        while(!temp.exists()){
            parents.add(temp);
            temp = temp.getParentFile();
        }
        
        for(int i = parents.size() - 1; i >= 0; i--){
            parents.get(i).mkdir();
        }
        
        if( !file.exists() ) {
            file.mkdir();
        }
    }
    
    /**
     * 将网络上的资源保存到本机。
     * 
     * 此方法只能用于HTTP协议，不适用于FTP协议
     * 
     * @param pageUrl
     * @param fileName
     * @return
     */
    public static boolean saveUrlAsLocalFile(String pageUrl, String fileName) {
        if(pageUrl == null) return false;
        
        if(pageUrl.indexOf("?") >= 0)
            pageUrl = pageUrl + "&isRobot=true";
        else
            pageUrl = pageUrl + "?isRobot=true";
        
        makeDir(new File(fileName).getParentFile());
        InputStream in = null;
        OutputStream out = null;
        try {
            URL url = new URL(pageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("isRobot", Config.TRUE); // 设置为发布机器人访问
            
            in = new DataInputStream(conn.getInputStream());
            out = new DataOutputStream(new FileOutputStream(fileName));
            byte[] buffer = new byte[4096];
            int count = 0;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
        } catch (Exception e) {
            log.error("将页面（" + pageUrl + "）保存到文件（" + fileName + "）时出错了。", e);
            return false;
        } finally {
            try {
                if(out != null) out.close();
                if(in != null) in.close();
            } catch (IOException e) {
            }
        }
        return true;
    }
}

