package com.jinhe.tss.portal.engine.releasehtml;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.LinkRegexFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.jinhe.tss.framework.component.progress.FeedbackProgressable;
import com.jinhe.tss.framework.component.progress.Progress;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.util.FileHelper;

/** 
 * <p> MagicRobot.java </p> 
 * 魔幻发布机器人。 
 * <br/>分两种情况：
 * <br/>1、整站发布：从首页开始发布整个站点。
 * <br/>2、单页发布：根据指定的发布页，发布其及其页面上的所有链接，但页面上链接发布出来的页面上的链接不再继续发布。
 * <br/>(两者都不处理外挂js、css样式以及图片信息，只发布内容)
 * 
 * <br/>为提高性能，避免对一些不变的页面进行不必要的重新发布（比如文章页），特定以下规则：
 * <br/>1、如果已存在的发布页面列表（existsFiles）不包含当前要发布的页面，则发布该页面
 * <br/>2、如果包含，且非文章页，则发布该页面
 * <br/>3、如果包含，且是文章页，且非第一个发布页面，则不再发布该页面
 * <br/>4、如果包含，且是文章页，且是第一个发布页面（单个文章页发布时），则发布该页面
 * <br/>
 * <br/>另外对是否发布页面上链接地址，也定以下规则：
 * <br/>1、如果是整站发布，当前发布页面非文章页，则发布其上所有链接
 * <br/>2、如果是整站发布，当前发布页面为文章页，则不发布其上所有链接
 * <br/>3、如果是单页发布，且当前发布页是指定页本身，则发布其上所有链接
 * <br/>4、如果是单页发布，当前发布页非指定页本身，则不发布其上所有链接
 * <br/>
 */
public class MagicRobot extends SimpleRobot implements FeedbackProgressable {
    
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
    
    /**
     * 发布失败的页面信息
     */
    private Map<String, String> issueFailedUrls = new HashMap<String, String>();
    
    /**
     * 发布完成后返回信息
     */
    private String feedback; 
    
    private Long portalId;
    
    /**
     * 已经存在的发布页面列表
     */
    private List<File> existsFiles; 
    
    /**
     * 本次发布页面的路径，一般根据当天时间 按 年/月/日 格式来命名
     */
    private String pageDir; 
    
    /**
     * 是否是单页发布 true:单页发布 false：整站发布
     */
    private boolean isOnlyPublishPage; 

    /**
     * 发布普通页和普通页的链接，不深入发布
     * @param commonPage 页面动态访问地址
     */
    public MagicRobot(String commonPage){ 
        super(commonPage);
        this.existsFiles = FileHelper.listFilesByTypeDeeply(HTM_FILE_SUFFIX, new File(issuePath));
        this.pageDir = createPageDir(super.issuePath);
        
        this.isOnlyPublishPage = true;
        this.indexPage = commonPage;
    }
    
    /**
     * 发布整个门户
     * @param portalId 门户ID
     * @param indexPage 主页
     */
    public MagicRobot(Long portalId){ 
        this("");
        
        this.portalId = portalId;
        this.isOnlyPublishPage = false;
        this.indexPage = Environment.getContextPath() + "/auth/portal/preview/" + portalId;
    }
    
    private Progress progress = new Progress(-1);

    public void execute(Map<String, Object> params, Progress progress) {
        this.progress = progress;
        start();
    }
    
    private int count = 0;
    
    /**
     * 更新进度信息
     */
    private void updateProgressInfo(){
        if(++count % 30 == 0){
            progress.add(30); //每30个更新一次进度信息
        } 
    }
    
    /**
     * 从首页开始静态发布
     */
    public void start() {
        //执行抓取页面过程
        //从首页开始发布
        excute(this.indexPage, null); 

        // 替换页面中的链接
        for( String htmlFilePath : htmlFiles ){
            File htmlFile = new File(htmlFilePath);
            
            // 如果是上次已经发布的文章页，则无需再替换页面里的地址
            String fileName = htmlFile.getName();
            if(isArticlePage(fileName) && getExsitingFile(fileName) != null){
                continue;
            }
            replaceUrl(htmlFile); // 替换页面中的链接
        }
        
        if(!issueFailedUrls.isEmpty()) {
            StringBuffer sb = new StringBuffer("发布结束，但有页面发布出错。具体错误信息如下：\n");
            for( Entry<String, String> entry : issueFailedUrls.entrySet() ){
                sb.append("地址（").append(entry.getKey()).append("）").append(entry.getValue()).append("\n");
            }
            feedback = sb.toString();
            log.info(feedback);
        } else {
            feedback = "静态发布成功";
        }
        // 如果发布结束了进度还没有完成
        if(!progress.isCompleted()) {
            progress.add(8888888); // 通过设置一个大数（远大于总数）来使进度完成
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
     *   
     * @param href
     * @param parentHref
     * @param issueDeeply 是否发布页面里的链接地址
     */
    private void excute(String href, String parentHref) {
        boolean isFirstHref = (parentHref == null); //是否是本次发布的第一个页面，即进入页
        boolean issueDeeply = isFirstHref || !isOnlyPublishPage; // 单页发布时首次发布 或 整站发布， 则都进行深度发布
        
        if(href == null || href.length() <= 6 
                || (href.startsWith("http://") && !isFirstHref) // 非单页发布时链接地址以http://打头的一般为外部网站地址，不做处理
                || href.startsWith("javascript") || href.startsWith("#") 
                || href.startsWith("mailto") || href.indexOf(".htm") > 0) {
            return;
        }
        
        // 如果当前地址已经发布过（不管发布成功或失败），则不再发布
        if (urlMapping.containsKey(href) || issueFailedUrls.containsKey(href))
            return;

        //将要发布的地址设置为正在发布状态，如此其它地方有相同的地址时可以跳过，以免重复发布
        urlMapping.put(href, PAGE_ISSUING_TAG);
        
        String url;
        if(!href.startsWith(Environment.getContextPath()))
            url = contextPath + Environment.getContextPath() + "/" + href;
        else
            url = contextPath + href;
        
        // 生成文件名
        String pageName;
        if(portalId != null && (isFirstHref || indexPage.equals(href))){ // 判断是否首页
            pageName = "index" + this.portalId + HTM_FILE_SUFFIX;
        } else {
            pageName = genPageName(url, HTM_FILE_SUFFIX);
        }
        
        boolean isArticlePage = isArticlePage(pageName);
        String htmlFileName = getExsitingFile(pageName);
        boolean isExsitingFile = htmlFileName != null;
        
        if(!isExsitingFile) {
            //文章页面放到当天发布路径下，以防止单一路径下文件数量过多
            htmlFileName = issuePath + (isArticlePage ? pageDir + pageName : pageName);
        }
        
        // 判断页面是否已经发布过了 以及 是否是文章页。文章页如果已经发布则无需再发布一边，文章列表页则每次都需要重新发布
        // 如果是单页发布，则存在的文章页也重新发布
        if(!isExsitingFile || isFirstHref || (isExsitingFile && !isArticlePage) || isOnlyPublishPage){
            // 下载页面
            boolean success = ReleaseHelper.saveUrlAsLocalFile(url, htmlFileName);
            if(!success) {
                issueFailedUrls.put(href, "发布页面时抓取HTML内容出错，位于页面：" + parentHref);
                return;
            }
            // 如果是非文章页，或是单页发布，且是深度发布，则解析里面的地址。非单页发布文章时文章页没必要再解析了
            if((!isArticlePage || isOnlyPublishPage )&& issueDeeply){
                try {
                    //处理页面里的动态地址，发布出相应的静态页面。
                    Parser parser = new Parser(htmlFileName);
                    parser.setEncoding("GBK");
                    NodeList list = parser.parse(new LinkRegexFilter(""));
                    for (NodeIterator it = list.elements(); it.hasMoreNodes();) {
                        LinkTag linkNode = (LinkTag) it.nextNode();
                        excute(linkNode.getAttribute("href"), href);
                    }
                } catch (Exception e){
                    issueFailedUrls.put(href, "发布页面时解析出错，位于页面：" + parentHref);
                    return;
                }
            }
        }
        
        htmlFiles.add(htmlFileName);
        String relativeUrl = htmlFileName.substring(htmlFileName.indexOf("html") - 1); //取相对于html目录的地址 如：/html/index62.htm
        urlMapping.put(href, Environment.getContextPath() + relativeUrl); // /pms/html/index62.htm
        updateProgressInfo();
    }
    
    private String getExsitingFile(String fileName){
        for( File htmlFile : existsFiles ){
            if(htmlFile.getName().equals(fileName)) 
                return htmlFile.getPath();
        }
        return null;
    }

    /**
     * 替换各种类型的地址，包括<a href="">
     * @param htmlFile
     * @throws ParserException 
     */
    private void replaceUrl(File htmlFile) {
        StringBuffer sb = new StringBuffer();

        try {
            Parser parser = new Parser (htmlFile.getPath());
            parser.setEncoding("GBK");
            NodeList list = parser.parse(null);
            for(NodeIterator outIter = list.elements(); outIter.hasMoreNodes();){
                Node bigNode = outIter.nextNode();
                replaceDynamicUrl(bigNode);
                sb.append(bigNode.toHtml());
            }
        } catch (ParserException e){
            issueFailedUrls.put(htmlFile.getPath(), "处理地址出错：" + e.getCause());
            return;
        }
        
        //TODO 此处做法比较垃圾，有可能的话再改掉            
        // 因为用htmlParser解析html时，如果js里有html标签，会自动加上一个</script>，需要将其去掉，
        // 但万一js里没有，不能影响到body里的
        String htmlContent = sb.toString();
        int bodyIndex = htmlContent.indexOf("<body");
        int scriptIndex = htmlContent.indexOf("</script></");
        if(scriptIndex <  bodyIndex){
            htmlContent =  htmlContent.replaceFirst("</script></", "</"); 
        }
        
        FileHelper.writeFile(htmlFile, htmlContent);
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
     * 根据页面名称判断是否是文章页
     * @param pageName
     * @return
     */
    private boolean isArticlePage(String pageName){
        return pageName.indexOf("articleId") > -1 || pageName.indexOf("articlePage.portal") > -1;
    }
    
    /**
     * <p>
     * 生成文章页面的发布路径 (根据创建时间确定发布路径)
     * </p>
     * @param createTime
     * @return  格式： 2008/8/20
     */
    protected String createPageDir(String issuePath) {
        // 得到当前年月日
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String now = sdf.format(new Date());
        int index = now.length();
        String year = now.substring(0, index - 4);
        String month = now.substring(index - 4, index - 2);
        String day = now.substring(index - 2, index);
        
        String pageDirPath = year + "/" + month + "/" + day + "/"; // 发布路径 = 发布目录/年/月/日
        FileHelper.createDir(issuePath + pageDirPath);
        
        return pageDirPath;
    }

    public String getFeedback() {
        return feedback;
    }
    
    public int getExsitFilesNum() {
        return this.existsFiles.size();
    }
}