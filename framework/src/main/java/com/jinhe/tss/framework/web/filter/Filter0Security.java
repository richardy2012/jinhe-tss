package com.jinhe.tss.framework.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.component.param.ParamConfig;
import com.jinhe.tss.framework.sso.SSOConstants;
import com.jinhe.tss.framework.web.wrapper.SecurityUtil;
import com.jinhe.tss.util.EasyUtils;

/**
 * 安全过滤器，防止无权限(匿名访问 或 自注册用户)直接访问后台服务地址
 */
@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})
public class Filter0Security implements Filter {
	
    Logger log = Logger.getLogger(Filter0Security.class);
    
    /** 
     * 不用登陆既能访问的白名单，可配置在application.properties 或 系统参数 
     * url.white.list = /version,.in,.do,.portal,login.html,404.html,version.html,redirect.html,_forget.html,_register.html
     */
    private static final String WHITE_LIST = "url.white.list";
    private static final String THE_404_URL = "/tss/404.html";
 
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
    	
    	HttpServletRequest req = (HttpServletRequest) request;
        
        // 防止盗链
        String referer    = req.getHeader("referer");
        String serverName = req.getServerName(); // 网站的域名
        if(referer != null && !referer.contains(serverName)) {
        	((HttpServletResponse)response).sendRedirect(THE_404_URL);
        	return;
        } 
        
        // 检查是否忽略权限
        String servletPath = req.getServletPath();
        if( !isNeedPermission(servletPath) ) {
        	chain.doFilter(request, response);
            return;
        }
         
        // 检测权限
        List<Object> userRights = new ArrayList<Object>();
        try {
        	HttpSession session = req.getSession(false);
            if(session != null) {
                List<?> list = (List<?>) session.getAttribute(SSOConstants.USER_RIGHTS_IN_SESSION);
				userRights.addAll( list );
            }
        } catch(Exception e) {  }
        
        log.debug("权限检测开始：" + servletPath);
        if ( !checkPermission(userRights, servletPath) ) {
            log.debug("权限检测失败");
            ((HttpServletResponse)response).sendRedirect(THE_404_URL);
            return;
        }
        
        log.debug("权限检测通过");
        chain.doFilter(request, response);
    }
 
    private boolean checkPermission(List<?> userRights, String servletPath) {
    	if( userRights.size() > 1 ) {
    		return true; // 匿名角色共有，所以登录用户必须要有2个角色或以上
    	}
		return false;
	}
    
    private boolean isNeedPermission(String servletPath) {
    	if( EasyUtils.isNullOrEmpty(servletPath) ) return false;
    	
    	String whiteListConfig = ParamConfig.getAttribute(WHITE_LIST);
    	List<String> whiteList = new ArrayList<String>();
    	if( !EasyUtils.isNullOrEmpty(whiteListConfig) ) {
    		whiteList.addAll( Arrays.asList( whiteListConfig.split(",") ) );
    	}
    	
    	for(String whiteItem : whiteList) {
    		if(servletPath.indexOf( whiteItem.trim() ) >= 0) {
    			return false;
    		}
    	}
    	
    	if( servletPath.indexOf(".htm") >= 0 ) {
    		return true;
    	}
    	
    	if( servletPath.indexOf(".") < 0   // 无后缀，一般restful地址 或 /download
    			&& SecurityUtil.getSecurityLevel() >= 3 ) {
    		return true;
    	}
    	return false;
    }

	public void init(FilterConfig filterConfig) throws ServletException {
        log.info("SecurityFilter init! appCode=" + Config.getAttribute(Config.APPLICATION_CODE));
    }
    
    public void destroy() { }
}
