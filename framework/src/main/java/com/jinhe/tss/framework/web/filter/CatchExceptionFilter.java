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
 * <p>
 * 异常过滤器
 * </p>
 * 
 * 捕获异常并转换成XML输出
 * 
 */
@WebFilter(filterName = "CatchExceptionFilter", urlPatterns = {"*.do", "*.action", ".in", ".portal"})
public class CatchExceptionFilter implements Filter {
	
    private static Logger log = Logger.getLogger(CatchExceptionFilter.class);
 
    public void destroy() {
    }
 
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            ExceptionEncoder.encodeException(response, e); // 捕获异常并转换成XML输出
        }
    }
 
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("异常处理过滤器初始化完成！appCode=" + Config.getAttribute(Config.APPLICATION_CODE));
    }

}
