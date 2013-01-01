package com.jinhe.tss.portal.engine.releasehtml;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jinhe.tss.framework.component.param.ParamConfig;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.URLUtil;

/** 
 * 简单发布机器人。
 * 只发布首页，不发布二级或更深的页面。
 */
public class SimpleRobot implements Robot{
    
    protected Logger log = Logger.getLogger(this.getClass());

    protected final static String HTML_FILE_SUFFIX = ".html"; // 单个发布使用
    
    protected String indexPage;  //首页名称 如：gzcz.portal
    protected String issuePath;  //发布路径 
    protected String contextPath;//服务访问地址 如 http://10.100.1.5:9080/pms

    public SimpleRobot(String indexPage){ 
        this.contextPath = Context.getApplicationContext().getCurrentAppServer().getBaseURL();
        this.indexPage = indexPage;
        this.issuePath = URLUtil.getWebFileUrl("").getPath() + "/html/";
        
        FileHelper.createDir(issuePath);
    }
    
    /**
     * 从首页开始静态发布
     */
    public void start() {
        //执行抓取页面过程
        try{
            //从首页开始发布
            String url = contextPath + "/" + indexPage;
            
            //生成文件名，如 gzcz.portal.html
            String tempFileName = issuePath + "temp/" + genPageName(url, HTML_FILE_SUFFIX);
            //下载页面
            boolean success = IssueHelper.saveUrlAsLocalFile(url, tempFileName);
            if( !success ) return;
            
            // 模板引擎解析没出错才将发布页面正式复制到发布目录下
            File tempFile = new File(tempFileName);
            if(FileHelper.readFile(tempFile).indexOf("执行Freemarker引擎解析时候出错") < 0){
                FileHelper.copyFile(new File(issuePath), tempFile);
            }
        }catch (Exception e) {
            throw new BusinessException("抓取页面时候出错", e);
        }
    }

    /**
     * 根据请求的地址生成html页面的文件名称。
     * 取请求地址最后一个"/"做为名字。如http://ip/pms/gzcz.portal,则取名为gzcz.portal.html。
     * @param url
     * @param suffix
     * @return
     */
    protected String genPageName(String url, String suffix){
        String queryString = url.substring(url.lastIndexOf("/") + 1);
        StringBuffer sb = new StringBuffer();
        char[] c = queryString.toCharArray();
        for(int i = 0; i < c.length; i++){
            if(c[i] == '?' || c[i] == '*' || c[i] == '<' || c[i] == '>' || c[i] == '|' || c[i] == '"') //文件名称里不允许这些字符
                sb.append('_');
            else
                sb.append(c[i]);
        }
        return sb.toString() + suffix;
    }
    
    private final static String INDEX_PAGE_FRESHNESS_TIME_CONFIG = "index_page_freshness_time";
    private final static long DEFAULT_FRESHNESS_TIME = 10 * 60 * 1000; // 默认10分钟更新一次首页
    
    // 记录每个地址的上一次访问时间
    private static Map<String, Long> visitLogsMap = new HashMap<String, Long>();
    
    /**
     * 检查发布的页面的是否新鲜，如果是第一次发布或者离上次发布已经过去10分钟，
     * 则重新发布并更新时间。
     * @param visitUrl
     */
    public static void checkVisitFreshness(Long portalId, String visitUrl){
        long freshnessTime;
        try {
            freshnessTime = Long.parseLong(ParamConfig.getAttribute(INDEX_PAGE_FRESHNESS_TIME_CONFIG));
        } catch (Exception e) {
            freshnessTime = DEFAULT_FRESHNESS_TIME;
        }
        
        Long visitTime = visitLogsMap.get(visitUrl);
        if(visitTime == null || System.currentTimeMillis() - visitTime > freshnessTime){
            visitLogsMap.put(visitUrl, System.currentTimeMillis());
            
            new SimpleRobot(visitUrl).start();
        }
    }
}