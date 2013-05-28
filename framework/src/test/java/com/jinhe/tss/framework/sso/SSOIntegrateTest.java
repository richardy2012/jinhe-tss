package com.jinhe.tss.framework.sso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;

import com.jinhe.tss.framework.sso.context.RequestContext;
import com.jinhe.tss.framework.sso.servlet.JustRedirectServlet;
import com.jinhe.tss.framework.sso.servlet.MultiRequestServletTest.SimpleRequestServlet;
import com.jinhe.tss.framework.web.filter.Filter4AutoLogin;
import com.jinhe.tss.framework.web.filter.Filter2CatchException;
import com.jinhe.tss.framework.web.filter.Filter3Context;
import com.jinhe.tss.framework.web.filter.Filter5HttpProxy;
import com.jinhe.tss.framework.web.filter.Filter1Encoding;
import com.jinhe.tss.framework.web.filter.Filter6XmlHttpDecode;
import com.jinhe.tss.framework.web.listener.SessionDestroyedListener;
import com.jinhe.tss.framework.web.servlet.EmptyServlet;
import com.jinhe.tss.framework.web.servlet.LoginServlet;
import com.jinhe.tss.framework.web.servlet.LogoutServlet;

/**
 * <p>
 * 单点登录集成测试
 * </p>
 */
public class SSOIntegrateTest extends TestCase {
    
    protected Logger log = Logger.getLogger(this.getClass());
    
    protected Server tssServer;
    protected Server cmsServer;
    
    protected void setUp() throws Exception {
        super.setUp();
 
        tssServer = startOneServer(8083, "/tss"); 
        cmsServer = startOneServer(8081, "/cms"); 
    }
    
    private Server startOneServer(int port, String contextPath) throws Exception {
        Server server = new Server(port); // 设置监听端口为port
        
        Context context = new Context(server, contextPath, Context.SESSIONS);
        
        context.addFilter(Filter1Encoding.class, "/*", Handler.DEFAULT).setInitParameter("encoding", "UTF-8");
        context.addFilter(Filter2CatchException.class, "*", Handler.DEFAULT);
        context.addFilter(Filter3Context.class, "/*", Handler.DEFAULT);
        context.addFilter(Filter4AutoLogin.class, "/*", Handler.DEFAULT);
        context.addFilter(Filter5HttpProxy.class, "/*", Handler.DEFAULT);
        context.addFilter(Filter6XmlHttpDecode.class, "*", Handler.DEFAULT);
        
        context.getSessionHandler().addEventListener(new SessionDestroyedListener());
        
        context.addServlet(LoginServlet.class, "/login.do"); 
        context.addServlet(LogoutServlet.class, "/logout.do"); 
        context.addServlet(JustRedirectServlet.class, "/rd.do"); 
        context.addServlet(SimpleRequestServlet.class, "/simple.do"); 
        context.addServlet(EmptyServlet.class, "/*"); // 对应的servlet类，/* 表示任意的url都可以触发
        
        server.start();
        return server;
    }
 
    protected void tearDown() throws Exception {
        super.tearDown();
        tssServer.stop();
        cmsServer.stop();
    }
    
    public final void testSSO() throws ServletException, IOException, InterruptedException {
        HttpClient client = new HttpClient(); 
        
        System.out.println("");
        log.info("---------------------------------1、先测试匿名访问------------------------------------------------------");
        PostMethod httppost = new PostMethod("http://localhost:8083/tss/index.html");
        httppost.setRequestHeader(RequestContext.ANONYMOUS_REQUEST, "true");
        excuteRequest(client, httppost);
        
        System.out.println("");
        log.info("--------------------------------- 2、测试首次登录 ------------------------------------------------------");
        httppost = new PostMethod("http://localhost:8083/tss/login.do");
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
        
        System.out.println("\n");
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
        
        System.out.println("\n");
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
        
        System.out.println("");
        log.info("--------------------------------- 5、测试退出登录(tss/cms里全注销掉) ------------------------------------------------------");
        httppost = new PostMethod("http://localhost:8083/tss/logout.do");
        httppost.setRequestHeader("appCode", "CMS");
        excuteRequest(client, httppost);
        
        httppost = new PostMethod("http://localhost:8083/tss/logout.do");
        excuteRequest(client, httppost);
        
        System.out.println("\n");
        log.info("--------------------------------- 6、测试註銷后访问，提示重新登录 ------------------------------------------------------");
        httppost = new PostMethod("http://localhost:8083/tss/index.html");
        httppost.setRequestHeader("REQUEST-TYPE", "xmlhttp");
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
