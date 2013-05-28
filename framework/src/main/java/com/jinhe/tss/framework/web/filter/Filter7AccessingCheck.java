package com.jinhe.tss.framework.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;

import com.jinhe.tss.framework.component.param.ParamManager;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.exception.BusinessServletException;
import com.jinhe.tss.framework.sso.SSOConstants;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.XMLDocUtil;

/** 
 * 应用地址访问权限控制检测过滤器
 * 
 */
@WebFilter(filterName = "AccessingCheckFilter", urlPatterns = {"*.htm", "*.action", ".html"})
public class Filter7AccessingCheck implements Filter {
    
    Log log = LogFactory.getLog(Filter7AccessingCheck.class);

    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("AccessingCheckFilter init! appCode=" + Context.getApplicationContext().getCurrentAppCode());
    }
 
    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if(!checkIp(request.getRemoteAddr())) {
            log.info("黑名单（" + request.getRemoteAddr() + "）用户试图访问系统被拒绝。");
            throw new BusinessServletException("您的ip" + request.getRemoteAddr() + " 地址在黑名单里，如有疑问请联系管理员！");
        }
        
        HttpSession session = req.getSession(false);
        if(session == null){
            chain.doFilter(request, response);
            return;
        }
        //检测权限
        List<Object> userRights = new ArrayList<Object>();
        try {
            List<?> userRightsInSession = (List<?>) session.getAttribute(SSOConstants.USER_RIGHTS_IN_SESSION);
            if(userRightsInSession != null) {
                userRights.addAll(userRightsInSession);
            }
            
            String operatorName = (String) session.getAttribute(SSOConstants.LOGINNAME_IN_SESSION);
            if(operatorName != null) {
                userRights.add(operatorName);
            }
        }catch(Exception e){
        }
        
        String servletPath = req.getRequestURI().substring(req.getContextPath().length());
        AccessingChecker checker = AccessingChecker.getInstance();
        
        log.info("权限检测：" + servletPath);
        if (!checker.checkPermission(userRights, servletPath)) {
            log.info("权限检测失败");
            
            if(checker.get404URL() != null){
                ((HttpServletResponse)response).sendRedirect(checker.get404URL());
                return;
            } 
            throw new BusinessServletException("访问控制检测失败，您无权访问本页面");
        }
        log.info("权限检测通过");
        chain.doFilter(request, response);
    }

    public final static String LIMITED_IP_LIST = "limited_ip_list";
    
    /**
     * 检查ip地址是否在受限的黑名单里。
     * @param remoteAddr
     * @return 
     *        不受限：true  受限：false
     */
    private boolean checkIp(String remoteAddr) {
        String limitedIPs; 
        try{
            limitedIPs = ParamManager.getValue(LIMITED_IP_LIST); // 格式为ip1,ip2,ip3....
        } catch (Exception e){
            limitedIPs = null;
        }
        return limitedIPs == null || limitedIPs.indexOf(remoteAddr) == -1;
    }
}

/** 
 * <p> PermissionChecker.java </p> 
 * 
 * 权限检测器。
 * 
 * 权限配置文件格式： 
 * <rightConfig>
 *      <servlet name="test.do" right="权限ID1,权限ID2,Admin"/>
 *      <servlet name="param!*.action" right="权限ID1,权限ID3,Admin,JonKing"/>
 * </rightConfig> 
 * 
 * @author Jon.King 2008/12/19 10:59:06
 */
class AccessingChecker {

    /** 权限配置文件 */
    private static final String RIGHT_CONFIG_FILE_NAME = "META-INF/right-config.xml";
    
    private static Map<String, Set<String>> rightsMap;
    
    private static String THE_404_URL; 
    
    private AccessingChecker() {
        try {
            rightsMap = parser(RIGHT_CONFIG_FILE_NAME);
        } catch (Exception e) {
            throw new BusinessException("权限配置初始化失败！", e);
        }
    }
    
    private static AccessingChecker instance;
    
    public static AccessingChecker getInstance(){
        if(instance == null){
            instance = new AccessingChecker();
        }
        return instance;
    }
        
    /**
     * 检查用户是否拥有相应的权限。
     * 分两步：
     * 1、检查用户拥有的角色是否足够（即是否在应用程序的权限角色之中）
     * 2、检查用户的账号是否在应用程序允许访问的用户名单中
     * 两者有一个通过即可。
     * 注：账号和角色都放userRights里了，
     * 
     * @param userRigths
     *            用户拥有的权限
     * @param servletPath
     *            检测的请求路径
     * @return
     * @throws Exception
     */
    public boolean checkPermission(List<Object> userRights, String servletPath) {
        if (rightsMap == null || rightsMap.isEmpty()) {
            return true;
        }

        Set<String> rights = rightsMap.get(servletPath);
       
        // 如果访问权限控制配置为空，则不需要控制，直接放行。
        if (rights == null || rights.isEmpty()) {
            return true;
        }
        
        // 访问权限控制配置不为空，则用户必须拥有足够的权限才能访问
        if( !EasyUtils.isNullOrEmpty(userRights)) {
            for (Object temp : userRights) {
                // 用户角色ID有可能为Long类型，需要toString一下，因为rights里根据配置文件解析出来的都是String形式。
                if (temp != null && rights.contains(temp.toString()))
                    return true;
            }
        }
        return false;
    }
    
    public String get404URL(){
        return THE_404_URL;
    }

    /**
     * 解析应用程序权限访问控制配置文件。
     * 把应用程序有权限访问的角色ID 以及 账号统一放在Set里。
     * 
     * @param configFile
     * @return
     * @throws Exception
     */
    private Map<String, Set<String>> parser(String configFile) throws Exception {
        Map<String, Set<String>> rightsMap = new HashMap<String, Set<String>>();

        if (configFile == null || "".equals(configFile)) {
            return rightsMap;
        }
        
        Document doc = XMLDocUtil.createDoc(configFile);
        Element root = doc.getRootElement();
        THE_404_URL = root.attributeValue("url_404");
        
        for (Iterator<?> it = root.elementIterator("servlet"); it.hasNext();) {
            Element servletNode = (Element) it.next();
            
            String name = servletNode.attributeValue("name");
            if (name == null || "".equals(name)) {
                continue;
            }
            
            String role = servletNode.attributeValue("right");
            if ( !EasyUtils.isNullOrEmpty(role) ) {
                Set<String> rightSet = new HashSet<String>();
                String[] rights = role.split(",");
                for (int i = 0; i < rights.length; i++) {
                    rightSet.add(rights[i]);
                }
                rightsMap.put(name, rightSet);
            }
        }
        return rightsMap;
    }
}
