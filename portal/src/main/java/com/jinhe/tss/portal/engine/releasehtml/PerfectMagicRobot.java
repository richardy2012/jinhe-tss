package com.jinhe.tss.portal.engine.releasehtml;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.LinkRegexFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.util.FileHelper;

/** 
 * <p> IssueRobot.java </p> 
 * 发布机器人。
 * 从首页开始发布整个站点。
 * 
 * TODO 存在的问题：1、页面多了会缓存溢出，考虑多线程
 *             2、新开一个HttpUrlConnection，里面的token值如何去掉？
 */
public class PerfectMagicRobot extends SimpleRobot{
    
    private final static String RESOURCES_PATH_NAME = "resources";
    
    // 整站发布时后缀统一为htm，防止和单个发布出来的页面冲突
    protected final static String HTM_FILE_SUFFIX = ".htm";  
    
    private final static String PAGE_ISSUING_TAG = "processing";
    
    /**
     *  Portal页面访问路径 对 发布后的静态路径，发布过程中（发布路径未生成）前，值一律为”processing“
     */
    private Map<String, String> urlMapping = new HashMap<String, String>(); 
  
    /**
     * 发布成功生成的html页面名
     */
    private List<String> htmlFiles = new ArrayList<String>(); 

    public PerfectMagicRobot(Long portalId, String indexPage){ 
        super(indexPage);
    }
    
    /**
     * 从首页开始静态发布
     */
    public void start() {
        //执行抓取页面过程
        try{
            //从首页开始发布
            excute(Environment.getContextPath() + "/" + indexPage); 
            
            for( String htmlFile : htmlFiles ){
                replaceUrl(htmlFile); // 替换页面中的链接
            }
        }catch (ParserException e) {
            throw new BusinessException("用htmlParser解析时候出错", e);
        }
    }

    /**
     * 执行页面的抓取。
     * 首先抓取页面内容到本地，然后再将页面中在抓取页面中的地址。
     * 递归循环，一直抓取下去。
     * 
     * 以下几种情况的链接地址除外:
     *   1."http://"打头的，这个一般为门户以外的链接
     *   2、javascript打头的，这种目前难以处理
     *   3、"#"打头的或者为空的
     * @param href
     * @throws ParserException 
     */
    private void excute(String href) throws ParserException {
        if(href == null || href.startsWith("http://")
                || href.startsWith("javascript")|| href.startsWith("#"))
            return;
        
        String pageUrl = (String) urlMapping.get(href);
        if (pageUrl != null)
            return;

        //将要发布的地址设置为正在发布状态，如此其它地方有相同的地址时可以跳过，以免重复发布
        urlMapping.put(href, PAGE_ISSUING_TAG);
        
        String url;
        if(!href.startsWith(Environment.getContextPath()))
            url = contextPath + Environment.getContextPath() + "/" + href;
        else
            url = contextPath + href;
        
        //生成文件名
        String htmlFileName = issuePath + genPageName(url, HTML_FILE_SUFFIX);
        //下载页面
        ReleaseHelper.saveUrlAsLocalFile(url, htmlFileName);
        
        //处理页面里的动态地址，发布出相应的静态页面。
        Parser parser = new Parser(htmlFileName);
        parser.setEncoding("GBK");
        NodeList list = parser.parse(new LinkRegexFilter(""));
        for (NodeIterator it = list.elements(); it.hasMoreNodes();) {
            LinkTag linkNode = (LinkTag) it.nextNode();
            excute(linkNode.getAttribute("href"));
        }
        
        htmlFiles.add(htmlFileName);
        urlMapping.put(href, htmlFileName);
    }

    /**
     * 替换各种类型的地址，包括<a href=""></a>, <img src=""/>, script, css, 普通link等
     * @param htmlFilePath
     * @throws ParserException 
     */
    private void replaceUrl(String htmlFilePath) throws ParserException{
        StringBuffer sb = new StringBuffer();

        Parser parser = new Parser (htmlFilePath);
        parser.setEncoding("GBK");
        NodeList list = parser.parse(null);
        for(NodeIterator outIter = list.elements(); outIter.hasMoreNodes();){
            Node bigNode = outIter.nextNode();

            replaceDynamicUrl(bigNode);
            replaceOtherResourcesUrl(bigNode);
            
            sb.append(bigNode.toHtml());
        }

        if(sb.indexOf("<style>") == -1)
            return;

        /**
         * <style>
         * ......
         * #subMenu {
         *    BACKGROUND-IMAGE: url(/pms/pms/model/portlet/bannerjdh30/submenu_bg.gif); BORDER-BOTTOM: #fff
         * }
         * ......
        </style>
         */
        String styleCode = replaceUrlInStyleCode(sb.substring(sb.indexOf("<style>") + 8, sb.indexOf("</style>")));
        sb.delete(sb.indexOf("<style>") + 8, sb.indexOf("</style>"));
        sb.insert(sb.indexOf("<style>") + 8, styleCode);
        
        FileHelper.writeFile(new File(htmlFilePath), sb.toString());
    }

    /**
     * 动态内容发布成静态后，将html中的动态的链接地址换成相应生成html页面地址。
     * @param htmlFilePath
     */
    private void replaceDynamicUrl(Node bigNode) throws ParserException{
        NodeList linkElements = new NodeList();
        bigNode.collectInto(linkElements, new LinkRegexFilter(""));
        for(NodeIterator inIter = linkElements.elements(); inIter.hasMoreNodes();){
            LinkTag linkNode = (LinkTag) inIter.nextNode();
            String url = linkNode.getAttribute("href");
            
            String pageUrl = (String) urlMapping.get(url);
            if(pageUrl != null){
                linkNode.setAttribute("href", pageUrl);
            }
        }
    }
    
    /**
     * 将html中所有的图片、js、css、flash等链接地址改掉，同时将相应的文件复制到发布的文件夹的资源文件夹下
     * css: <link href="/pms/pms/model/portal/11804954453901181187246562_41/css.css" rel="stylesheet" type="text/css">
     * js : <script language="javascript" src=""/pms/core/js/common.js""></script>
     * pic: <img src="/pms/pms/model/decorator/tb74/bg_menu.jpg" border="0"> 
     * or 
     * js:  picTitle.src="/pms/pms/model/decorator/tb74/bg_menu.jpg";
     * or
     * css: url("/pms/pms/model/portlet/zb70/bg_login.jpg");
     * 
     * CMS: 
     *   <IMG src="http://localhost:8088/cms/download.fun?id=123&amp;seqNo=1">
     *   <PARAM NAME="Movie" VALUE="http://localhost:8088/cms/download.fun?id=124&amp;seqNo=1">
     * 
     * 处理js，css文件地址
     * 处理pms组件用到的图片，flash等地址
     * 处理CMS文章的图片以及附件地址（包括CMS本地的和网络上的地址）
     * 
     * TODO 处理UMS（可能比如用户头像等）的地址 及
     * <td style="background-image:url(/pms/pms/model/decorator/shyzhjwzhlbxshq28/title_tbbd.jpg)>
     * 
     * @param bigNode
     * @throws ParserException 
     */
    private void replaceOtherResourcesUrl(Node bigNode) throws ParserException {
        List<String> list = doReplace(bigNode, "link", "href");
        doReplace(bigNode, "script", "src");
        doReplace(bigNode, "img", "src");
        doReplace(bigNode, "td", "background");
        doReplace(bigNode, "tr", "background");
        doReplace(bigNode, "table", "background");
        doReplace(bigNode, "PARAM", "VALUE");
        doReplace(bigNode, "embed", "src");
        
        //处理外挂css文件中引用到的图片地址
        for( String cssFilePath : list ){
            File cssFile = new File(cssFilePath);
			String cssContent = FileHelper.readFile(cssFile);
            FileHelper.writeFile(cssFile, replaceUrlInStyleCode(cssContent));
        }
    }
    
    /**
     * 处理html中指定名称的标签下的相应属性名（该属性和文件相关）。
     * 下载属性对应的附近，并替换成下载后的地址。
     * 
     * @param bigNode  html页面
     * @param tagName  标签，想table，td，tr，script，img等。
     * @param attributeName 标签的属性名 像background、src、href、VALUE等
     * @return
     * @throws ParserException
     */
    private List<String> doReplace(Node bigNode, String tagName, String attributeName) throws ParserException{
        List<String> list = new ArrayList<String>();
        
        NodeList linkElements = new NodeList();
        bigNode.collectInto(linkElements, new TagNameFilter(tagName));
        for(NodeIterator iter = linkElements.elements(); iter.hasMoreNodes();){
            TagNode tagNode = (TagNode) iter.nextNode();

            String src = tagNode.getAttribute(attributeName);
            if(src == null)
                continue;
            
            String fileName = null, sourceUrl = null;
            if(src.startsWith("/")){ //PMS应用里的文件
                fileName = src.substring(1);
                sourceUrl = contextPath + "/" + fileName;
            }else if(src.startsWith("http://")){ //PMS应用外的文件
                fileName = System.currentTimeMillis() + src.substring(src.lastIndexOf("."));
                sourceUrl = src;
            }
            String localFile = issuePath + RESOURCES_PATH_NAME + "/" + fileName;
            if(ReleaseHelper.saveUrlAsLocalFile(sourceUrl, localFile))
                tagNode.setAttribute(attributeName, RESOURCES_PATH_NAME + "/" + fileName);
            
            list.add(localFile);
        }
        return list;
    }
    
    /**
     * 替换页面中样式style code中的用到的文件（一般为图片）的地址，需要遍历整段style代码。
     * 格式为 BACKGROUND-IMAGE: url(/pms/pms/model/portlet/bannerjdh30/submenu_bg.gif);
     * 
     * TODO 当链接过来的css外挂脚本中可能是类似：url(menu_bg.gif)，也就是图片根css文件同目录下的。
     * 
     * @param styleCode
     * @return
     */
    private String replaceUrlInStyleCode(String styleCode){
        StringBuffer sb = new StringBuffer(); 
        int previewIndex = 0;
        int currentIndex = 0;
        while((currentIndex = styleCode.indexOf("url(", previewIndex)) != -1){
            currentIndex += 4;
            sb.append(styleCode.substring(previewIndex, currentIndex));
            previewIndex = styleCode.indexOf(")", currentIndex);
            String url = styleCode.substring(currentIndex, previewIndex);
            //去掉url中头和尾的单引号或双引号
            if(url.startsWith("'") || url.startsWith("\"")){
                url = url.substring(1, url.length() - 1);
            }
            String localFile = issuePath + "/" + RESOURCES_PATH_NAME + "/" + url;
            ReleaseHelper.saveUrlAsLocalFile(contextPath + "/" + url, localFile);
            sb.append(RESOURCES_PATH_NAME + "/" + url);
        }
        sb.append(styleCode.substring(previewIndex));
        return sb.toString();
    }
}