package com.jinhe.tss.portal.sso;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.jinhe.tss.framework.exception.BusinessServletException;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.portal.engine.releasehtml.SimpleRobot;
import com.jinhe.tss.portal.entity.ReleaseConfig;

/** 
 * <p> 门户发布地址转发器 </p> 
 * <pre>
 * 门户发布地址以.portal结尾，本servlet拦截所有此类请求，将其转到真正的门户地址进行访问。
 * 
 * 2008-08-30：新增了匿名访问时根据发布地址直接转到静态发布页面的功能。
 *           （非匿名的话各登录用户权限不同，页面也不同，不做静态发布）
 * 如果是匿名用户且不是发布机器人（Robot）发过来的请求，则直接转向静态页面地址。
 * 在这过程中Robot会检查页面是否是最近发布的（可以设定一个页面过期时间），如果页面过期了则重新发布。
 * 此时发布机器人会重新请求该访问地址并带上isRobot=true的参数，过滤器查到该参数则直接将请求地址转换成真实门户浏览地址。
 * </pre>
 */
@WebServlet(urlPatterns="*.portal")
public class PortalDispatcher extends HttpServlet {
 
    static final String IS_ROBOT = "isRobot";

    private static final long serialVersionUID = -5610690924047339502L;
    
    Logger log = Logger.getLogger(this.getClass());
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse)response;
            ServletContext servletContext = req.getSession().getServletContext();
            
            ReleaseConfig issueInfo = (ReleaseConfig) req.getAttribute(Filter8PortalPermission.PORTAL_ISSUE_INFO);
            if (issueInfo == null) {
                return;
            }
            String isRobot = Context.getRequestContext().getValueFromRequest(IS_ROBOT); // = "true";
            String redirectPage;
            if(Context.getIdentityCard().isAnonymous() && isRobot == null){
                String visitUrl = issueInfo.getVisitUrl();
                log.debug("匿名访问门户页面:" + visitUrl + " 被自动转向到静态页面，isRobot = " + isRobot);
                
                SimpleRobot.checkVisitFreshness(issueInfo.getPortal().getId(), visitUrl);
                redirectPage = "/html/" + visitUrl + ".html";
            } else {
                redirectPage = req.getContextPath() + getRedirectPath(issueInfo);
                log.debug("访问门户发布地址被转向至真实地址:" + redirectPage + "，isRobot = " + isRobot);
            }
            RequestDispatcher rd = servletContext.getRequestDispatcher(redirectPage);
            rd.forward(req, res); // 控制权转发
            
            // res.sendRedirect(redirectPage);
            /*RequestDispatcher.forward()方法和HttpServletResponse.sendRedirect()方法的区别是：
             * 前者仅是容器中控制权的转向，在客户端浏览器地址栏中不会显示出转向后的地址；后者则是完全的跳转，
             * 浏览器将会得到跳转的地址，并重新发送请求链接。这样，从浏览器的地址栏中可以看到跳转后的链接地址。
             * 所以，前者更加高效，在前者可以满足需要时，尽量使用Request Dispatcher.forward()方法，并且，
             * 这样也有助于隐藏实际的链接。在有些情况下，比如，需要跳转到一个其它服务器上的资源，则必须使用 
             * HttpServletResponse.sendRedirect()方法。
             */
        } catch (RuntimeException e) {
            throw new BusinessServletException(e);
        }
    }
    
    /**
     * <p>
     * 获取发布信息对应的真实地址
     * </p>
     * @param issueInfo
     * @return
     */
    private Object getRedirectPath(ReleaseConfig issueInfo) {
        Long portalId = issueInfo.getPortal().getId();
        String redirectPage = "/portal/preview/browse/" + portalId + "?";
        if (issueInfo.getPage() != null) {
            redirectPage += "&id=" + issueInfo.getPage().getId();
        }
        if (issueInfo.getTheme() != null) {
            redirectPage += "&themeId=" + issueInfo.getTheme().getId();
        }
        return redirectPage;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}

