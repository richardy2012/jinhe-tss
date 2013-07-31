package com.jinhe.tss.portal.sso;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.exception.BusinessServletException;
import com.jinhe.tss.framework.sso.AnonymousOperator;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.sso.context.RequestContext;
import com.jinhe.tss.framework.web.RewriteableHttpServletRequest;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.entity.IssueInfo;
import com.jinhe.tss.portal.entity.Structure;
import com.jinhe.tss.portal.service.IPortalService;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.permission.PermissionHelper;

/**
 * <p>
 * 处理门户是否可以匿名访问的过滤器
 * </p>
 */
@WebFilter(filterName = "PortalPermissionFilter", 
		urlPatterns = {"*.action", ".portal"})
public class Filter3PortalPermission implements Filter {

	/**
	 * 请求中门户编号的属性名
	 */
	private static final String PORTAL_ID_ATTRIBUTE_NAME = "portalId";

	/**
	 * 发布路径的后缀名
	 */
	public static final String PORTAL_REDIRECT_URL_SUFFIX = ".portal";

    /**
     * 预览门户的action地址
     */
    public static final String PORTAL_PREVIEW_ACTION_URL = "portal!previewPortal.action";
    
	/**
	 * 发布信息存储属性名称
	 */
	public static final String PORTAL_ISSUE_INFO = "_portal_issue_info";

    /**
     * 初始化过滤器
     * @param config
     * @throws ServletException
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig config) throws ServletException {

    }
	/**
	 * 过滤器销毁
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * <p> 过滤 </p>
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		RewriteableHttpServletRequest req = null;
		try {
			req = Context.getRequestContext().getRequest();

			String requestURI = req.getRequestURI();
			Long portalId = null;
			if (requestURI.endsWith(PORTAL_REDIRECT_URL_SUFFIX)) {
                // 发布路径访问方式
				IssueInfo issueInfo = getIssueInfo(requestURI);
				req.setAttribute(PORTAL_ISSUE_INFO, issueInfo); // 设置门户真实访问地址，为后续门户发布路径转向过滤器准备相关参数
				portalId = issueInfo.getPortal().getId();
			} 
			else if(requestURI.indexOf(PORTAL_PREVIEW_ACTION_URL) > 0){ 
                // 非发布路径访问方式，portalId参数跟在访问地址后，可从请求中取到
                String portalIdParam = req.getParameter(PORTAL_ID_ATTRIBUTE_NAME);
                if(portalIdParam == null){
                    throw new BusinessServletException("request请求里门户portalId参数为空，请检查地址是否有误。");
                }
				portalId = new Long(portalIdParam);
			}
			if (portalId != null)
                // 检测相应门户是否可以使用匿名用户访问
				if (canPortalBrowseByAnonymous(portalId))
					req.setHeader(RequestContext.ANONYMOUS_REQUEST, Config.TRUE);
            
		} catch (RuntimeException e) {
			throw new BusinessServletException(e);
		}
		chain.doFilter(req, response);
	}
	
    /**
     * 检测匿名用户是否对门户有浏览权限。 
     * 比如管理员专用的后台门户就不允许匿名访问。
     * 
     * @param portalId
     * @return
     */
    private boolean canPortalBrowseByAnonymous(Long portalId){
    	PermissionHelper helper = PermissionHelper.getInstance();
        List<?> list = helper.getEntities("from PortalStructure o where o.portalId = ? and o.type = ?", portalId, Structure.TYPE_PORTAL);
        
        if(list.isEmpty())  return false;

        String application = UMConstants.TSS_APPLICATION_ID;
        String resourceType = PortalConstants.PORTAL_RESOURCE_TYPE;
        String operration = PortalConstants.PORTAL_BROWSE_OPERRATION;
        Long anonymousId = AnonymousOperator.anonymous.getId();
        List<Long> permissons = helper.getResourceIdsByOperation(application, resourceType, operration, anonymousId);
        
        Structure rootPS = (Structure) list.get(0);
        return permissons.contains(rootPS.getId());
    }

	/**
	 * 获取发布信息
	 */
	private IssueInfo getIssueInfo(String uri) {
		String visitUrl = uri.substring(uri.lastIndexOf("/") + 1);
        IssueInfo info = ((IPortalService) Global.getContext().getBean("PortalService")).getIssueInfo(visitUrl);
		return info;
	}
}
