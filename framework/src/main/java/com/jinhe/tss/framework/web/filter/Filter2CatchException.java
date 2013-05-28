package com.jinhe.tss.framework.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.apache.log4j.Logger;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.exception.ExceptionEncoder;

/**
 * <p> 异常过滤器 </p>
 * 
 * 捕获异常并转换成XML输出
 * 
 */
@WebFilter(filterName = "CatchExceptionFilter", urlPatterns = {"/*"})
public class Filter2CatchException implements Filter {
	
    Logger log = Logger.getLogger(Filter2CatchException.class);
 
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            ExceptionEncoder.encodeException(response, e); // 捕获异常并转换成XML输出
        }
    }
 
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("CatchExceptionFilter init! appCode=" + Config.getAttribute(Config.APPLICATION_CODE));
    }
    
    public void destroy() {
    }
}
