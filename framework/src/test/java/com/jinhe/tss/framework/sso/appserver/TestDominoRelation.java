package com.jinhe.tss.framework.sso.appserver;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;
 
public class TestDominoRelation {
    
    private static final Logger log = Logger.getLogger(TestDominoRelation.class);
    
    public static void main(String args[]) throws HttpException, IOException{
        HttpClient client = new HttpClient();
        // 设置Cookie处理策略
        HttpClientParams params = client.getParams();
        params.setCookiePolicy(CookiePolicy.RFC_2109);
        
        String loginName = "龚英";

       // loginName = "gy_xxzx";
        
       // loginName = java.net.URLEncoder.encode(loginName, "UTF-8");
        log.debug("loginName:" + loginName);
        
        PostMethod login = new PostMethod("http://www.gzcz.com:8081/names.nsf?Login"){
            public String getRequestCharSet() {
                log.debug("RequestCharSet:" + super.getRequestCharSet());
                //return super.getRequestCharSet();
                return "UTF-8";
            }
        };
        login.addParameter("Username", loginName);
        login.addParameter("Password", "4321");
        login.addParameter("RedirectTo", "/app/testxml.nsf/success.xml");
        try {
            int statusCode = client.executeMethod(login);
            log.debug("LoginDomino:Login(" + statusCode + ")");
            
            if (statusCode == HttpStatus.SC_OK) {
                log.info(login.getResponseBodyAsString());
            } else if ((statusCode == HttpStatus.SC_MOVED_TEMPORARILY)
                    || (statusCode == HttpStatus.SC_MOVED_PERMANENTLY)
                    || (statusCode == HttpStatus.SC_SEE_OTHER)
                    || (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
                log.info(login.getResponseBodyAsString());
            }
        } finally {
            login.releaseConnection();
        }
    }
    
    /**
     * 中文转unicode
     * @param str
     * @return 反回unicode编码
     */
    public static String  chinaToUnicode(String str){
        String result = null;
        for (int i = 0; i < str.length(); i++){
            int chr1 = (char) str.charAt(i);
            result  += "\\u" + Integer.toHexString(chr1);            
        }
        return result;
    }
}

