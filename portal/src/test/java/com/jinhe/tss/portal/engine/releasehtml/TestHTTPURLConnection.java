package com.jinhe.tss.portal.engine.releasehtml;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
 
public class TestHTTPURLConnection {

    //网络爬虫
    public void netReptile() throws IOException{
        java.net.URL url = new java.net.URL("http://www.google.cn"); 
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.connect(); 
        java.io.InputStream in = conn.getInputStream(); 

        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(in)); 

        String currentLine = ""; 
        String totalString = ""; 
        while ((currentLine = reader.readLine()) != null){ 
            totalString += currentLine + "\n"; 
        } 
        in.close();
        reader.close();
        System.out.println(totalString); 
    }
    
    // 此方法只能用户HTTP协议
    public boolean saveUrlAs(String photoUrl, String fileName) {
        try {
            URL url = new URL(photoUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName));
            byte[] buffer = new byte[4096];
            int count = 0;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            out.close();
            in.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 此方法兼容HTTP和FTP协议 网络爬虫
    public String getDocumentAt(String urlString) {
        StringBuffer document = new StringBuffer();
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                document.append(line + "\n");
            }
            reader.close();
        } catch (MalformedURLException e) {
            System.out.println("Unable to connect to URL: " + urlString);
        } catch (IOException e) {
            System.out.println("IOException when connecting to URL: " + urlString);
        }
        return document.toString();
    }

    public static void main(String[] agrs) throws IOException {
        final TestHTTPURLConnection test = new TestHTTPURLConnection();
        
//        test.netReptile();
//        test.loadPicByURL();
//        test.saveUrlAs("http://www.google.cn/intl/zh-CN/images/logo_cn.gif", "D:/Temp/Portal/logo_cn.gif");
//        System.out.println(test.getDocumentAt("http://www.google.com"));
        
        //第一次调用，先缓存数据
        test.getDocumentAt("http://localhost:8088/pms/foa2.portal");
        for(int i = 0; i < 34; i++){
            final int count = i;
            new Thread(){
                public void run(){
                    test.getDocumentAt("http://localhost:8088/pms/foa2.portal");
                    System.out.println(count);
                    System.gc();
                }
            }.start();
        }
        System.out.println("over---------------------------");
        
        /* 测试结果:
         * 开始-------------------------------------22 
         * issueInfo缓存 ---------------------------25
         * 缓存html页面（执行fremarker之前）-----------34
         * freemarker关掉---------------------------75
         */
    }
}

