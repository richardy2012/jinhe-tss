package com.jinhe.tss.framework.web.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.jinhe.tss.framework.exception.BusinessServletException;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.sso.context.RequestContext;

/**
 * <p>
 * 上下文对象相关过滤器
 * </p>
 *
 * <pre>
 * 初始化及销毁上下文对象（UserContext、RequestContext等）。 <br/>
 * 过滤器顺序：本filter应该配置AutoLoginFilter之前，
 *           后者需要的类似用户名、密码、验证方式、token等信息需要从Context中获取。<br/>
 * 
 * 类似在线用户库远程调用可以跳过本过滤器，以防TSS以外平台系统session注销时
 * 调用TSS配置的/remote/OnlineUserService，又会在UMS产生一个新的session。
 * </pre>
 */
@WebFilter(filterName = "ContextFilter", 
		urlPatterns = {"*.do", "*.action", ".in", ".portal", "/remote/*"}, initParams = {
		@WebInitParam(name="ignoreServletPaths", value="/remote/OnlineUserService")
})
public class ContextFilter implements Filter {
	
	private static Logger log = Logger.getLogger(ContextFilter.class);

    private Set<String> ignoreServletPaths = new HashSet<String>();
     
    public void init(FilterConfig filterConfig) throws ServletException {
        String paths = filterConfig.getInitParameter("ignoreServletPaths");
        if (paths != null) {
            ignoreServletPaths.addAll(Arrays.asList(paths.split(",")));
        }
        log.info("上下文环境初始化完成！appCode=" + Context.getApplicationContext().getCurrentAppCode());
    }
 
    public void destroy() {
        ignoreServletPaths = null;
    }
 
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
        
        String servletPath = RequestContext.getServletPath((HttpServletRequest)request);
        log.debug("当前请求ServletPath: " + servletPath);
        
        if (ignoreServletPaths.contains(servletPath)) {
            chain.doFilter(request, response);
            return;
        }
        
		try {
		    //初始化上下文对象(RequestContext等)
			Context.initRequestContext((HttpServletRequest) request);
			Context.setResponse((HttpServletResponse) response); 
            
			chain.doFilter(request, Context.getResponse()); //使用转换后的response
            
			Context.destroy(); //请求结束后销毁Context
		} 
		catch (Exception e) {
			throw new BusinessServletException(e);
		}
	}
}
