package com.jinhe.tss.framework.sso;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jinhe.tss.framework.sso.context.RequestContext;
import com.jinhe.tss.framework.sso.servlet.JustRedirectServlet;
import com.jinhe.tss.framework.sso.servlet.SimpleRequestServlet;
import com.jinhe.tss.framework.web.filter.Filter1Encoding;
import com.jinhe.tss.framework.web.filter.Filter2CatchException;
import com.jinhe.tss.framework.web.filter.Filter3Context;
import com.jinhe.tss.framework.web.filter.Filter4AutoLogin;
import com.jinhe.tss.framework.web.filter.Filter5HttpProxy;
import com.jinhe.tss.framework.web.filter.Filter6XmlHttpDecode;
import com.jinhe.tss.framework.web.filter.Filter7AccessingCheck;
import com.jinhe.tss.framework.web.listener.SessionDestroyedListener;
import com.jinhe.tss.framework.web.servlet.Servlet1Login;
import com.jinhe.tss.framework.web.servlet.Servlet2Logout;
import com.jinhe.tss.framework.web.servlet.Servlet8Empty;

/**
 * <p>
 * 单点登录集成测试
 * </p>
 */
public class SSOIntegrateTest {
    
    protected Logger log = Logger.getLogger(this.getClass());
    
    protected Server tssServer;
    protected Server cmsServer;
    protected Server xxxServer;
    
    HttpClient client; 
    
    @Before
    public void setUp() throws Exception {
    	try {
    		tssServer = startOneServer(8083, "/tss"); 
            cmsServer = startOneServer(8081, "/cms"); 
            xxxServer = startOneServer(8082, "/xxx"); 
            
    	} catch(Exception e) {
    	}
    	
    	client = new HttpClient(); 
    }
    
    private Server startOneServer(int port, String contextPath) throws Exception {
        Server server = new Server(port); // 设置监听端口为port
 
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(Servlet1Login.class, "/login.do");
        context.addServlet(Servlet2Logout.class, "/logout.in"); 
        context.addServlet(JustRedirectServlet.class, "/rd.do"); 
        context.addServlet(SimpleRequestServlet.class, "/simple.do"); 
        context.addServlet(Servlet8Empty.class, "/*"); // 对应的servlet类，/* 表示任意的url都可以触发
        
        context.getSessionHandler().addEventListener(new SessionDestroyedListener());
        
        context.addFilter(Filter1Encoding.class, "/*", null).setInitParameter("encoding", "UTF-8");
        context.addFilter(Filter2CatchException.class, "*", null);
        context.addFilter(Filter3Context.class, "/*", null);
        context.addFilter(Filter4AutoLogin.class, "/*", null);
        context.addFilter(Filter5HttpProxy.class, "/*", null);
        context.addFilter(Filter6XmlHttpDecode.class, "*", null);
        context.addFilter(Filter7AccessingCheck.class, "*", null);
        
        context.setContextPath(contextPath);
        
        server.setHandler(context);
        
        server.start();
        return server;
    }
 
    @After
    public void tearDown() throws Exception {
        tssServer.stop();
        cmsServer.stop();
        xxxServer.stop();
    }
    
    @Test
    public void testAnonymous() throws ServletException, IOException, InterruptedException {
        log.info("---------------------------------1、先测试匿名访问------------------------------------------------------");
        PostMethod httppost = new PostMethod("http://localhost:8083/tss/index.html");
        httppost.setRequestHeader(RequestContext.ANONYMOUS_REQUEST, "true");
        excuteRequest(client, httppost);
    }
    
    @Test
    public void testLoginFirstTime() throws ServletException, IOException, InterruptedException {
        log.info("--------------------------------- 2、测试首次登录 ------------------------------------------------------");
        PostMethod httppost = new PostMethod("http://localhost:8083/tss/login.do");
        httppost.setRequestHeader("REQUEST-TYPE", "xmlhttp");
        
        // 由于XmlHttpDecodeFilter配置在AutoLoginFilter之后，所以登录信息需要放在header里传递
        httppost.setRequestHeader("loginName", "Jon.King");
        httppost.setRequestHeader("password", "123456");
        httppost.setRequestHeader("identifier", "com.jinhe.tss.framework.sso.DemoUserIdentifier");
        
        String body = "<Request>" +
                        "<Param><Name><![CDATA[loginName]]></Name><Value><![CDATA[Jon.King]]></Value></Param>" +
                        "<Param><Name><![CDATA[password]]></Name><Value><![CDATA[123456]]></Value></Param>" +
                      "</Request>";
        httppost.setRequestEntity(new StringRequestEntity(body, null, null));
        excuteRequest(client, httppost);
 
        log.info("--------------------------------- 3、测试登录后访问 ------------------------------------------------------");
        httppost = new PostMethod("http://localhost:8083/tss/index.html");
        httppost.setRequestHeader("REQUEST-TYPE", "xmlhttp");
        body = "<Request><Param><Name><![CDATA[resourceId]]></Name><Value><![CDATA[10000]]></Value></Param></Request>";
        httppost.setRequestEntity(new StringRequestEntity(body, null, null)); //设置请求内容
        excuteRequest(client, httppost);
        /*  POST /tss/admin.html HTTP/1.1
            User-Agent: Jakarta Commons-HttpClient/3.1
            Host: localhost:8083
            Content-Length: 101
            REQUEST-TYPE: xmlhttp
            Cookie: $Version=0; JSESSIONID=uxnvs54t4l5g14yxlx7wuufs6; $Path=/tss
            Cookie: $Version=0; token=+y69xSIFzvaUClV6fMkrA3Fp2EQB9GnWn7Nd1Pv4Fqk4Sd9eEXwHICyxJPD86/KY; $Path=/tss */
    }
    
    @Test
    public void testCrossApp() throws ServletException, IOException, InterruptedException {
    	 PostMethod httppost = new PostMethod("http://localhost:8083/tss/login.do");
         httppost.setRequestHeader("REQUEST-TYPE", "xmlhttp");
         
         // 由于XmlHttpDecodeFilter配置在AutoLoginFilter之后，所以登录信息需要放在header里传递
         httppost.setRequestHeader("loginName", "Jon.King");
         httppost.setRequestHeader("password", "123456");
         httppost.setRequestHeader("identifier", "com.jinhe.tss.framework.sso.DemoUserIdentifier");
         
         String body = "<Request/>";
         httppost.setRequestEntity(new StringRequestEntity(body, null, null));
         excuteRequest(client, httppost);
         
        log.info("--------------------------------- 4、测试跨应用访问 ------------------------------------------------------");
        httppost = new PostMethod("http://localhost:8083/tss/index.html");
        httppost.setRequestHeader("appCode", "CMS");
        httppost.setRequestHeader("Content-Type", " text/html");
        excuteRequest(client, httppost);
        /*  POST /cms/cms.html HTTP/1.1
            Content-Length: 0
            User-Agent: Jakarta Commons-HttpClient/3.1
            Content-Type: text/html
            token: +y69xSIFzvaUClV6fMkrA3Fp2EQB9GnWn7Nd1Pv4Fqk4Sd9eEXwHICyxJPD86/KY
            clientIp: 127.0.0.1
            Host: localhost:8081
            Cookie: $Version=0; token=+y69xSIFzvaUClV6fMkrA3Fp2EQB9GnWn7Nd1Pv4Fqk4Sd9eEXwHICyxJPD86/KY
            Cookie: $Version=0; TSS=uxnvs54t4l5g14yxlx7wuufs6 */
        
        httppost = new PostMethod("http://localhost:8083/tss/rd.do");  // dealWithRedirect
        httppost.setRequestHeader("appCode", "CMS");
        excuteRequest(client, httppost);
    }
    
    @Test
    public void testLogout() throws ServletException, IOException, InterruptedException {
        log.info("--------------------------------- 5、测试退出登录(tss/cms里全注销掉) ------------------------------------------------------");
        PostMethod httppost = new PostMethod("http://localhost:8083/tss/logout.do");
        httppost.setRequestHeader("appCode", "CMS");
        excuteRequest(client, httppost);
        
        httppost = new PostMethod("http://localhost:8083/tss/logout.do");
        excuteRequest(client, httppost);
 
        log.info("--------------------------------- 6、测试註銷后访问，提示重新登录 ------------------------------------------------------");
        httppost = new PostMethod("http://localhost:8083/tss/index.html");
        httppost.setRequestHeader("REQUEST-TYPE", "xmlhttp");
        String body = "<Request><Param><Name><![CDATA[resourceId]]></Name><Value><![CDATA[10000]]></Value></Param></Request>";
        httppost.setRequestEntity(new StringRequestEntity(body, null, null)); //设置请求内容
        excuteRequest(client, httppost);
    }

    protected void excuteRequest(HttpClient client, PostMethod httppost) throws IOException, HttpException {
        try {
            client.executeMethod(httppost);
            int statusCode = httppost.getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(httppost.getResponseBodyAsStream()));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = in.readLine()) != null){
                  buffer.append(line);
                }
                System.out.println("response:" + buffer.toString());
            } else {
                fail("请求连接失败");
            }
        } finally {
            httppost.releaseConnection();
        }
    }
}
