package com.jinhe.tss.framework.web.rmi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.springframework.remoting.httpinvoker.AbstractHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.support.RemoteInvocationResult;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.sso.appserver.AppServer;
import com.jinhe.tss.framework.sso.appserver.AppServerStorerFactory;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.sso.context.RequestContext;

/**
 * <p>
 * 客户端配置远程接口用到本对象，配置时将appCode（如：CMS）以及serviceUrl（如：/remote/PermissionService）设置进来。<br>
 * 如此则可以根据appCode值直接取到该应用的BaseURL（如：http://10.100.1.5/cms）值，
 * 然后BaseURL + serviceUrl ==> http://10.100.1.5/cms/remote/PermissionService 就是远程接口真实地址。<br>
 * 
 * 本对象主要目的是避免每次应用IP改变时候都要重新改远程接口的配置。<br>
 * 
 * eg:
 *      HttpInvokerProxyFactory factory = new HttpInvokerProxyFactory();
        factory.setServiceUrl("/remote/PermissionService");
        factory.setServiceInterface(IPermissionService.class);
        factory.setAppCode("CMS");
        
        return (IPermissionService)factory.getObject();
        
 * 即调用远程接口时，在前台发来的请求（request1）的里开启一个新的请求（request2），在JAVA端实现远程调用其他应用里的接口
 * </p>
 *
 */
public class HttpInvokerProxyFactory extends HttpInvokerProxyFactoryBean {

	private String targetAppCode;
	private String serviceUrl;
	
 	/**
	 * <p> 设置应用系统编码 </p>
	 */
	public void setAppCode(String appCode) {
		this.targetAppCode = appCode;
	}
	
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
 
	public void afterPropertiesSet() {
		AppServer targetAppServer = AppServerStorerFactory.newInstance().getAppServerStorer().getAppServer(targetAppCode);
		super.setServiceUrl(targetAppServer.getBaseURL() + serviceUrl);
		setHttpInvokerRequestExecutor(new AutoLoginHttpInvokerRequestExecutor(targetAppServer));
		
		super.afterPropertiesSet();
	}
	
	public Object getObject(){
	    afterPropertiesSet();
		return super.getObject();
	}
	
	/**
	 * <p> AutoLoginHttpInvokerRequestExecutor.java </p>
	 * <p>
	 * 远程调用执行对象，将Requst中的Cookie信息添加到远程调用当中，实现单点登录；
	 * 并将远程调用结束后返回的数据和Cookie等信息返回给客户端。
	 * </p>
	 *
	 */
	class AutoLoginHttpInvokerRequestExecutor extends AbstractHttpInvokerRequestExecutor {
	    
	    private Logger log = Logger.getLogger(AutoLoginHttpInvokerRequestExecutor.class);

	    private AppServer targetAppServer;

	    public AutoLoginHttpInvokerRequestExecutor(AppServer server) {
	        super();
	        this.targetAppServer = server;
	    }
	 
	    protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos)
	            throws IOException, ClassNotFoundException {
	        
	        log.debug(Context.getApplicationContext().getCurrentAppCode() + "【远程调用】调用：" + config.getServiceUrl());
	        
	        /* 调用远程接口时，在前台发来的请求（request1）的里开启一个新的请求（request2 即 HttpClient），在JAVA端实现远程调用其他应用里的接口 */
	        HttpClient client = HttpClientHelper.instance().getHttpClient();
	        
	        HttpState httpState = client.getState();
	        setRequestCookies(httpState);

	        PostMethod httpPost = new PostMethod(config.getServiceUrl());
	        httpPost.addRequestHeader(RequestContext.ANONYMOUS_REQUEST, "true");
	        httpPost.addRequestHeader(RequestContext.USER_CLIENT_IP, Environment.getClientIp());
	        httpPost.addRequestHeader("Content-Type", "application/x-java-serialized-object");
	        httpPost.setRequestEntity(new ByteArrayRequestEntity(baos.toByteArray()));

	        RemoteInvocationResult result = null;
	        try {
	            // 执行HTTP请求，访问远程服务
	            int statusCode = client.executeMethod(httpPost);
	            if (statusCode == HttpStatus.SC_OK) {
	                // 处理返回信息
	                result = super.readRemoteInvocationResult(httpPost.getResponseBodyAsStream(), config.getCodebaseUrl());
	                // 设置单点登录返回的cookie信息
	                HttpClientHelper.transmitReturnCookies(httpState.getCookies(), targetAppServer);
	            } else {
	                throw new BusinessException(targetAppServer.getName() + "（" + targetAppServer.getCode() + "）连接错误，" +
	                		"错误代码：" + statusCode + "\n链接地址：" + config.getServiceUrl());
	            }
	        } catch (RuntimeException e) {
	            throw new BusinessException(targetAppServer.getName() + "（" + targetAppServer.getCode() + "）连接错误，" +
	            		"链接地址：" + config.getServiceUrl(), e);
	        } finally {
	            httpPost.releaseConnection();
	        }
	        return result;
	    }

	    /**
	     * <p> 设置用户身份相关Cookies：token、sessionId等到HttpClient的HttpState中 </p>
	     * 调用远程接口时，在前台发来的请求（request1）的里开启一个新的请求（request2 = HttpClient.executeMethod(HttpPost)），
	     * 此处将request1的cookie信息设置给request2，一般情况下，转发的Cookie包括: 
	     *     token = ****；JSessionId = 目标应用的sessionId；currentCode = 当前应用的sessionId；等
	     *
	     * @param client
	     */
	    private void setRequestCookies(HttpState initialState) {
	        String cookieDomain = targetAppServer.getDomain();
	        String cookiePath   = targetAppServer.getPath();
	        
            boolean secure = false;
            HttpServletRequest request = null;
            RequestContext requestContext = Context.getRequestContext();
            if (requestContext != null) {
                request = requestContext.getRequest();
            }
            
            if (request != null) {
                secure = request.isSecure(); //是否加密请求（https）
                
                /* 设置当前应用SessionId Cookies信息，如果session不存在（比如：注销应用后调用UMS的在线用户库），则不再新创建session */
                HttpSession session = request.getSession(false);
                if (session != null) { 
                    /* 将当前应用的sessionId命名为currentAppCode的Cookie设置到目标应用的request中（eg：TSS = 123456789 path=/cms）
                     * 目的是为在目标应用里回访当前应用时使用，那时在当前应用无需新建一个session*/
                    String currentAppCode = Context.getApplicationContext().getCurrentAppCode();
                    initialState.addCookie(new Cookie(cookieDomain, currentAppCode, session.getId(), cookiePath, null, secure));
                }
                
                javax.servlet.http.Cookie[] cookies = request.getCookies();
                if(cookies != null) {
                    for (int i = 0; i < cookies.length; i++) {
                        String cookieName = cookies[i].getName();
                        String sessionIdName = Context.getApplicationContext().getCurrentAppServer().getSessionIdName();
                        
                        /* “转出应用”的 JSESSIONID 和 token 两cookie不转发到“转入应用”中。待下面处理后才转发 */
                        if (cookieName.equals(sessionIdName) || cookieName.equals(RequestContext.USER_TOKEN)) continue;  
                        
                        /* 
                         * 将目标应用的sessionId设置到本次请求中来。
                         * 如：TSS先转发请求到CMS，则CMS的cookie里保留了TSS的JSESSIONID cookie，只是名字由JSESSIONID改为TSS，CMS里再转发请求回TSS的时候，
                         * 再把该cookie重新改名为JSESSIONID（TSS = 123456789 ==》 JSESSIONID = 123456789），这样在TSS端就无需新建一个session了
                         */
                        if (cookieName.equals(targetAppServer.getCode())) {
                            cookieName = targetAppServer.getSessionIdName();
                        }
                        
                        initialState.addCookie(new Cookie(cookieDomain, cookieName, cookies[i].getValue(), cookiePath, null, secure)); 
                    }
                }
            }
            
            // 设置User Token Cookies信息; TODO 为何不直接转发？是担心request.cookie里不一定有，所以这里统一转发？
            String token = Context.getToken();
            if (token != null) {
                Cookie cookie = new Cookie(cookieDomain, RequestContext.USER_TOKEN, token, cookiePath, null, secure);
                initialState.addCookie(cookie);
            }
	    }
	}
}
