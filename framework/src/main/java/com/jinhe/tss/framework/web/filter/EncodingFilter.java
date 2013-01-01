package com.jinhe.tss.framework.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jinhe.tss.framework.Config;
 
/**
 * 设置Http request的字符集
 */
@WebFilter(filterName = "EncodingFilter",
	urlPatterns = {"/*"} , initParams = {
		@WebInitParam(name="encoding", value="GBK")
		})
public class EncodingFilter implements Filter {
    private static final Log log = LogFactory.getLog(EncodingFilter.class);

    /** The default character encoding to set for requests that pass through this filter. */
    protected String encoding;

    /** Take this filter out of service. */
    public void destroy() {
        this.encoding = null;
    }

    /**
     * Select and set (if specified) the character encoding to be used to
     * interpret request parameters for this request.
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        // Conditionally select and set the character encoding to be used
        if ( request.getCharacterEncoding() == null ) {
            if (this.encoding != null) {
                request.setCharacterEncoding(this.encoding);
            }
        }
        HttpServletResponse hsr = (HttpServletResponse) response;
        hsr.setHeader("Cache-Control", "No-Cache");

        // Pass control on to the next filter
        chain.doFilter(request, response);

    }

    /**
     * Place this filter into service.
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.encoding = filterConfig.getInitParameter("encoding");

        log.info("字符编码转换服务初始化完成！appCode=" + Config.getAttribute(Config.APPLICATION_CODE));
    }
}
