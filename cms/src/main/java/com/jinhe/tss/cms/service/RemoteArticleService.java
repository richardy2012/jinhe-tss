package com.jinhe.tss.cms.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinhe.tss.cms.AttachmentDTO;
import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.dao.IArticleDao;
import com.jinhe.tss.cms.dao.IChannelDao;
import com.jinhe.tss.cms.entity.Article;
import com.jinhe.tss.cms.entity.Attachment;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.helper.ArticleHelper;
import com.jinhe.tss.cms.helper.ArticleQueryCondition;
import com.jinhe.tss.cms.helper.HitRateManager;
import com.jinhe.tss.cms.lucene.executor.IndexExecutorFactory;
import com.jinhe.tss.cms.timer.TimerStrategy;
import com.jinhe.tss.cms.timer.TimerStrategyHolder;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.persistence.pagequery.PageInfo;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.DateUtil;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.XMLDocUtil;
import com.jinhe.tss.util.XmlUtil;

@Service("RemoteArticleService")
public class RemoteArticleService implements IRemoteArticleService {
    
    protected Logger log = Logger.getLogger(this.getClass());

    @Autowired protected IArticleDao articleDao;
    @Autowired protected IChannelDao channelDao;
    
    public String getArticleListByChannel4Rss(Long channelId, Integer page, Integer pageSize) {
        Channel channel = channelDao.getEntity(channelId);
        if(channel == null) {
            throw new BusinessException("ID为：" + channelId + " 的栏目不存在！");
        }
        
        if( !channelDao.checkBrowsePermission(channelId) ) {
            log.error("用户【" + Environment.getOperatorName() + "】试图访问没有文章浏览权限的栏目【" + channel + "】");
            channelId = channelId * -1; // 置反channelId值，使查询不到结果
        }
        
        ArticleQueryCondition condition = new ArticleQueryCondition();
        condition.setChannelId(channelId);
        condition.getPage().setPageNum(page);
        condition.getPage().setPageSize(pageSize);
        condition.setStatus(CMSConstants.XML_STATUS);
        
        PageInfo pageInfo = articleDao.getChannelPageArticleList(condition);
        String baseUrl = Context.getApplicationContext().getCurrentAppServer().getBaseURL();
            
        Document doc = org.dom4j.DocumentHelper.createDocument();
        Element rssElement = doc.addElement("rss").addAttribute("version", "2.0"); 
        Element channelElement = rssElement.addElement("channel");
        channelElement.addElement("title").setText(channel.getName());
        channelElement.addElement("link").setText(baseUrl + "/index.portal");
        channelElement.addElement("description").setText(channel.getName() + "栏目文章列表");
        
        channelElement.addElement("language").setText("zh");
        channelElement.addElement("copyright").setText("Copyright (c) JinHe S&T Co.ltd ZJ, 2012-2020 ");

        List<?> articleList = pageInfo.getItems();
        if (articleList != null) {
            for (int i = 0; i < articleList.size(); i++) {
                Object[] fields = (Object[]) articleList.get(i);
                Long articleId = (Long) fields[0];

                Element itemElement = channelElement.addElement("item");
                itemElement.addElement("id").setText(articleId == null ? "" : articleId.toString());
                itemElement.addElement("title").setText(fields[2] == null ? "" : fields[2].toString());
                itemElement.addElement("pubDate").setText(fields[5] == null ? "" : DateUtil.format((Date) fields[5]));
                itemElement.addElement("link").setText(baseUrl + "/article.portal?isRobot=true&articleId=" + articleId);
            }
        }
        doc.setXMLEncoding("GBK");
        return doc.asXML();
    }
    
    public String getArticleListXMLByChannel(Long channelId, Integer page, Integer pageSize) { 
        String rssXML = getArticleListByChannel(channelId, page, pageSize, false);
        return "<Response><ArticleList>" + rssXML + "</ArticleList></Response>";
    }
    
    public String getPicArticleListByChannel(Long channelId, Integer page, Integer pageSize) {
        String rssXML = getArticleListByChannel(channelId, page, pageSize, true);
        return "<Response><ArticleList>" + rssXML + "</ArticleList></Response>";
    }

    private String getArticleListByChannel(Long channelId, Integer page, Integer pageSize, boolean isNeedPic) {
        Channel channel = channelDao.getEntity(channelId);
        if(channel == null) {
            throw new BusinessException("ID为：" + channelId + " 的栏目不存在！");
        }
        
        if( !channelDao.checkBrowsePermission(channelId) ) {
            log.error("用户【" + Environment.getOperatorName() + "】试图访问没有文章浏览权限的栏目【" + channelId + "】");
            channelId = channelId * -1; // 置反channelId值，使查询不到结果
        }
        
        ArticleQueryCondition condition = new ArticleQueryCondition();
        condition.setChannelId(channelId);
        condition.getPage().setPageNum(page);
        condition.getPage().setPageSize(pageSize);
        condition.setStatus(CMSConstants.XML_STATUS);
        
        PageInfo pageInfo = articleDao.getChannelPageArticleList(condition);
            
        Document doc = org.dom4j.DocumentHelper.createDocument();
        Element channelElement = doc.addElement("rss").addAttribute("version", "2.0");
        
        channelElement.addElement("channelName").setText(channel.getName()); 
        channelElement.addElement("totalPageNum").setText(String.valueOf(pageInfo.getTotalPages()));
        channelElement.addElement("totalRows").setText(String.valueOf(pageInfo.getTotalRows()));
        channelElement.addElement("currentPage").setText(page.toString());
        List<?> articleList = pageInfo.getItems();
        if (articleList != null) {
            for (int i = 0; i < articleList.size(); i++) {
                Object[] fields = (Object[]) articleList.get(i);
                Long articleId = (Long) fields[0];
                
                Element itemElement = createArticleElement(channelElement, articleId, (String) fields[1], (String) fields[2], 
                        (Date) fields[3], (String) fields[4], (Integer) fields[5]);
                
                if(isNeedPic){
                    Map<String, Attachment> attachments = articleDao.getArticleAttachments(articleId);
                    ArticleHelper.addPicListInfo(itemElement, attachments.values());
                }
            }
        }
        return channelElement.asXML();
    }

    public String queryArticlesByChannelIds(String channelIdStr, Integer page, Integer pageSize){
        ArticleQueryCondition condition = new ArticleQueryCondition();
        condition.getPage().setPageNum(page);
        condition.getPage().setPageSize(pageSize);
        condition.setStatus(CMSConstants.XML_STATUS);
        
        List<Long> channelIds = new ArrayList<Long>();
        String[] strIdArray = channelIdStr.split(",");
        for( String temp : strIdArray) {
            Long channelId = Long.valueOf(temp);
            if( channelDao.checkBrowsePermission(channelId) ) {
                channelIds.add(channelId);
            }
        }
        condition.setChannelIds(channelIds);
        
        PageInfo pageInfo = articleDao.getArticlesByChannelIds(condition);
        return "<Response><ArticleList>" + createReturnXML(pageInfo, channelIds.get(0)) + "</ArticleList></Response>";
    }
    
    public String queryArticlesDeeplyByChannelId(Long channelId, Integer page, Integer pageSize){
        ArticleQueryCondition condition = new ArticleQueryCondition();
        condition.getPage().setPageNum(page);
        condition.getPage().setPageSize(pageSize);
        condition.setStatus(CMSConstants.XML_STATUS);
        
        List<Channel> subChannels = channelDao.getChildrenById(channelId, CMSConstants.OPERATION_VIEW);
        List<Long> channelIds = new ArrayList<Long>();
        for(Channel temp : subChannels) {
            if( channelDao.checkBrowsePermission(temp.getId()) ) {
                channelIds.add(temp.getId());
            }
        }
        condition.setChannelIds(channelIds);
        
        PageInfo pageInfo = articleDao.getArticlesByChannelIds(condition);
        return "<Response><ArticleList>" + createReturnXML(pageInfo, channelId) + "</ArticleList></Response>";
    }
    
    private String createReturnXML(PageInfo pageInfo, Long channelId){
        Channel channel = channelDao.getEntity(channelId);
        List<?> articleList = pageInfo.getItems();
        
        Document doc = DocumentHelper.createDocument();
        Element channelElement = doc.addElement("rss").addAttribute("version", "2.0");
        
        channelElement.addElement("channelName").setText(channel == null ? "栏目" : channel.getName()); //多个栏目一起查找，取第一个栏目
        channelElement.addElement("totalRows").setText(String.valueOf(pageInfo.getTotalRows()));
        channelElement.addElement("totalPageNum").setText(String.valueOf(pageInfo.getTotalPages()));
        channelElement.addElement("currentPage").setText(String.valueOf(pageInfo.getPageNum()));
        if (articleList != null) {
            for (int i = 0; i < articleList.size(); i++) {
                Object[] fields = (Object[]) articleList.get(i);
                createArticleElement(channelElement, (Long) fields[0], (String) fields[1], (String) fields[2], 
                        (Date) fields[3], (String) fields[4], (Integer) fields[5]);
            }
        }
        return channelElement.asXML();
    }
    
    private Element createArticleElement(Element channelElement, Object articleId, String title, String author, 
            Date issueDate, String summary, Integer hitCount) {
        
        Element itemElement = channelElement.addElement("item");
        itemElement.addElement("id").setText(articleId == null ? "" : articleId.toString());
        itemElement.addElement("title").setText(title == null ? "" : title);
        itemElement.addElement("author").setText(author == null ? "" : author);
        itemElement.addElement("issueDate").setText(issueDate == null ? "" : DateUtil.format(issueDate));
        itemElement.addElement("summary").setText(summary == null ? "" : summary);
        itemElement.addElement("hitCount").setText(hitCount == null ? "" : hitCount.toString());
        
        return itemElement;
    }
    
    public String getChannelTree4Portlet(Long channelId) {
        List<Channel> list = channelDao.getChildrenById(channelId, CMSConstants.OPERATION_VIEW);
        TreeEncoder encoder = new TreeEncoder(list, new LevelTreeParser());
        encoder.setNeedRootNode(false);
        return encoder.toXml();
    }
    
    public String getArticleXML(Long articleId) {
        Article article = articleDao.getEntity(articleId);
        Document articleDoc;
        String pubUrl = article.getPubUrl();
        try{
            articleDoc = XMLDocUtil.createDocByAbsolutePath(pubUrl);
        } catch(Exception e){
            String fileContent = FileHelper.readFile(new File(pubUrl), "UTF-8");
            articleDoc = XMLDocUtil.dataXml2Doc(XmlUtil.stripNonValidXMLCharacters(fileContent));
        }
        Element articleElement = articleDoc.getRootElement();
        Element hitRateNode = (Element) articleElement.selectSingleNode("//hitCount");
        hitRateNode.setText(article.getHitCount().toString()); // 更新点击率
        
        Document doc = org.dom4j.DocumentHelper.createDocument();
        Element articleInfoElement = doc.addElement("Response").addElement("ArticleInfo");
        articleInfoElement.addElement("rss").addAttribute("version", "2.0").add(articleElement);

        //添加文章点击率;
        HitRateManager.getInstanse().output(articleId);
        
        return doc.asXML();
    }

    public void addArticle(String articleXml, Long channelId) {
        Document doc = XMLDocUtil.dataXml2Doc(articleXml);
        Element articleNode = (Element) doc.selectSingleNode("//ArticleInfo/Article");
        Article article = new Article();
        BeanUtil.setDataToBean(article, XMLDocUtil.dataNodes2Map(articleNode));
        
        Channel channel = channelDao.getEntity(channelId);
        article.setChannel(channel);
        article.setSeqNo(articleDao.getChannelArticleNextOrder(channelId));
         
        //设置过期时间
        article.setOverdueDate(ArticleHelper.calculateOverDate(article, channel));
        
        articleDao.saveArticle(article);
    }
    
    protected static final Date DEFAULT_START_DATE = DateUtil.parse("2000-1-1");
    protected static final Date DEFAULT_END_DATE = DateUtil.parse("2099-12-31");
    
    public String search(Long siteId, String searchStr, Integer pageNum, Integer pagesize) {
        Date startDate = DEFAULT_START_DATE;
        Date endDate = DEFAULT_END_DATE;
        boolean filterByTime = false; // 是否需要对查询结果集进行按时间段过滤，高级查询的时候用到
        String[] field = null;
        if(searchStr != null && searchStr.indexOf("@advancedSearch") >= 0){
            // 查询条件（searchStr）格式为 ： + 杭州 + 零售信贷 , 2000-1-1, 2099-12-31, @advancedSearch, title:杭州&author:零售信贷&
            String[] condition = searchStr.split(",");
            try {
                searchStr = condition[0]; 
                startDate = DateUtil.parse(condition[1].trim());
                endDate = DateUtil.parse(condition[2].trim());
                filterByTime = !DEFAULT_START_DATE.equals(startDate) || !DEFAULT_END_DATE.equals(endDate);
                if(condition[4] != null){
                    field = condition[4].split("&");
                }
            } catch (Exception e) {
                filterByTime = false;
            }
        }
        
        Channel site = channelDao.getEntity(siteId);
        TimerStrategy tacticIndex = TimerStrategyHolder.getIndexStrategy();
        tacticIndex.setSite(site);
        
        String indexPath = tacticIndex.getIndexPath();
        if (!new File(indexPath).exists() || searchStr == null || "".equals(searchStr.trim()))
            return "<Response><ArticleList><rss version=\"2.0\"><channel/></rss></ArticleList></Response>";
        
        org.dom4j.Document doc = DocumentHelper.createDocument();
        
        // 生成rss格式的xml文件的Head部分
        Element channelElement = doc.addElement("rss").addAttribute("version", "2.0");
        try {
            IndexSearcher searcher = new IndexSearcher(indexPath);
            Query query = IndexExecutorFactory.create(tacticIndex.getExecutorClass()).createIndexQuery(searchStr);
            Hits hits = searcher.search(query, new Sort(new SortField("createTime", SortField.STRING, true))); // 按创建时间排序
            
            // 先遍历一边查询结果集，对其按时间段以及权限进行过滤，将过滤后的结果集放入到一个临时list中。
            List<org.apache.lucene.document.Document> list = new ArrayList<org.apache.lucene.document.Document>();
            for (Iterator<?> it = hits.iterator(); it.hasNext(); ) {
                Hit hit = (Hit) it.next();
                org.apache.lucene.document.Document document = hit.getDocument();
                
                // 按时间段对查询结果集进行过滤
                if(filterByTime){
                    Date createTime = document.get("createTime") == null ? null : DateUtil.parse(document.get("createTime"));
                    startDate = startDate == null ? DEFAULT_START_DATE : startDate;
                    endDate = endDate == null ? DEFAULT_END_DATE : endDate;
                    if(createTime == null || createTime.before(startDate) || createTime.after(endDate)){
                       continue;
                    }
                }
                boolean checkByField = true;
                if(field != null){
                    for(int i = 0; i < field.length; i++){
                        String nv = field[i];
                        try {
                            String fieldName  = nv.substring(0, nv.indexOf(":")).trim();
                            String fieldValue = nv.substring(nv.indexOf(":") + 1).trim();
                            // 如果搜索的字段在索引里没有，或者没有包含搜索字段的关键字，则过滤掉
                            if(document.get(fieldName) == null || document.get(fieldName).indexOf(fieldValue) == -1){
                                checkByField = false;
                                break;
                            }
                        }catch(Exception e){
                        }
                    }
                }
                if(!checkByField){
                    continue;
                }
                
                list.add(document);
            }

            int page = pageNum.intValue();
            int pageSize = pagesize.intValue();
            int totalRows = list.size();
            int totalPage = totalRows % pageSize != 0 ? totalRows / pageSize + 1 : totalRows / pageSize;

            channelElement.addElement("totalPageNum").setText(String.valueOf(totalPage));
            channelElement.addElement("totalRows").setText(totalRows + "");
            channelElement.addElement("currentPage").setText(pageNum.toString());
            for (int i = (page - 1) * pageSize; i < totalRows && i < page * pageSize; i++) {
                org.apache.lucene.document.Document document = list.get(i);
                
                // 生成rss格式的xml文件的搜索出来的内容
                Long articleId = document.get("id") == null ? null : Long.valueOf(document.get("id"));
                Date issueDate = document.get("issueDate") == null ? null : DateUtil.parse(document.get("issueDate"));
                createArticleElement(channelElement, articleId, document.get("title"), document.get("author"), 
                        issueDate, document.get("summary"), 0);
            }
            searcher.close();
        } catch (Exception e) {
            throw new BusinessException("搜索出错!", e);
        } 
        return "<Response><ArticleList>" + channelElement.asXML() + "</ArticleList></Response>";
    }
 
    public AttachmentDTO getAttachmentInfo(Long articleId, Integer seqNo) {
        Attachment att = articleDao.getAttachment(articleId, seqNo);
        if (att == null) {
            log.error("数据库中没有相应的附件信息！文章ID：" + articleId + ", 序号：" + seqNo);
            return null;
        }
        
        // 通过文章id获取栏目id
        Channel site = att.getArticle().getChannel().getSite(); 
        
        AttachmentDTO dto = new AttachmentDTO(att.getType(), att.getName(), att.getFileName(), att.getFileExt(),
                att.getLocalPath(), new String[]{site.getPath(), site.getDocPath(), site.getImagePath()});
        
        return dto;
    }
    
    public String getArticleListByChannelAndTime(Long channelId, String year, String month) {
        if(channelId == null){
            throw new BusinessException("栏目ID不能为空!");
        }
        if(year == null || month == null){
            throw new BusinessException("年度或月份不能为空!");
        }
        
        Channel channel = channelDao.getEntity(channelId);
        if(channel == null) {
            throw new BusinessException("栏目不存在!");
        }
        Channel site = channel.getSite();
        String publishBaseDir = site.getPath();
       
        month = (month.length() == 1 ? "0" + month : month);
        String publishDir = publishBaseDir + "/" + year + "/" + month;
        List<File> xmlFiles = FileHelper.listFilesByTypeDeeply(".xml", new File(publishDir));
      
        Document doc = org.dom4j.DocumentHelper.createDocument();
        Element channelElement = doc.addElement("rss").addAttribute("version", "2.0");
 
        channelElement.addElement("channelName").setText(channel.getName()); 
        channelElement.addElement("totalPageNum").setText("1");
        channelElement.addElement("totalRows").setText("100");
        channelElement.addElement("currentPage").setText("1");
        for( File xmlFile : xmlFiles ){
            if(xmlFile.getName().startsWith(channelId + "_")){
                Document articleDoc;
                try{
                    articleDoc = XMLDocUtil.createDocByAbsolutePath(xmlFile.getPath());
                } 
                catch(Exception e){
                    String fileContent = FileHelper.readFile(xmlFile, "UTF-8");
                    articleDoc = XMLDocUtil.dataXml2Doc(XmlUtil.stripNonValidXMLCharacters(fileContent));
                }
                Element articleElement = articleDoc.getRootElement();
                Element idNode = (Element) articleElement.selectSingleNode("//id");
                Element titleNode = (Element) articleElement.selectSingleNode("//title");
                Element authorNode = (Element) articleElement.selectSingleNode("//author");
                Element issueDateNode = (Element) articleElement.selectSingleNode("//issueDate");
                Element summaryNode = (Element) articleElement.selectSingleNode("//summary");
                
                createArticleElement(channelElement,
                        idNode == null ? null : idNode.getText(), 
                		titleNode == null ? null : titleNode.getText(), 
                		authorNode == null ? null : authorNode.getText(), 
                		issueDateNode == null ? null : DateUtil.parse(issueDateNode.getText()), 
                        summaryNode == null ? null : summaryNode.getText(), 
                        null);
            }
        }
        return "<Response><ArticleList>" + channelElement.asXML() + "</ArticleList></Response>";
    }
}
