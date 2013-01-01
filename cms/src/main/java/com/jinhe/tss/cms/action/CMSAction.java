package com.jinhe.tss.cms.action;

import com.jinhe.tss.cms.service.IRemoteArticleService;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.web.mvc.PTActionSupport;
import com.jinhe.tss.util.EasyUtils;

/** 
 * CMS对外Action接口，提供供Portlet等外界应用程序读取的文章列表、文章内容、评分、评论等接口。
 * 
 * 支持RSS。
 * 
 */
public class CMSAction extends PTActionSupport {
 
	private Long    articleId;
	private Long    channelId;
    private String  channelIds; // 栏目ids 可选多个(逗号隔开)
    private Integer page = 1;
    private Integer pageSize = 12;
    
    private String  month;
    private String  year;
   
    private Long    tacticId;  // 全文检索索引ID
    private String  searchStr; // 检索关键字
    
    private IRemoteArticleService remoteService;
    
    /**
     * 获取栏目列表并且带有附件信息
     * @return
     */
    public String getPicArticleListByChannel() {
        String returnXML = remoteService.getPicArticleListByChannel(channelId, page, pageSize);
        print(returnXML);
        return XML;
    }
    
    /**
     * 获取栏目的文章列表
     * @return
     */
    public String getArticleListByChannel() {
        String returnXML = remoteService.getArticleListXMLByChannel(channelId, page, pageSize);
        print(returnXML);
        return XML;
    }

    /**
     * 获取栏目的文章列表。RSS2.0数据格式
     * http://localhost:8088/cms/cms!getArticleListByChannelRss.action?channelId=12&anonymous=true
     * 
     * @return
     */
    public String getArticleListByChannelRss() {
        String returnXML = remoteService.getArticleListByChannel4Rss(channelId, page, pageSize);
        print(returnXML);
        return XML;
    }

    /**
     * 根据栏目ids，获取这些栏目下的所有文章列表
     * @return
     */
    public String getArticleListByChannels() {
        if ( EasyUtils.isNullOrEmpty(channelIds) ) {
            throw new BusinessException("栏目IDs为空");
        }
        String returnXML = remoteService.queryArticlesByChannelIds(channelIds, page, pageSize);
        print(returnXML);
        return XML;
    }

    /**
     * 根据栏目id获取文章列表(深度)，取指定栏目以及该栏目下所有子栏目的所有文章列表
     * @return
     */
    public String getArticleListDeeplyByChannel() {
        String returnXML = remoteService.queryArticlesDeeplyByChannelId(channelId, page, pageSize);
        print(returnXML);
        return XML;
    }
    
    /**
     * 根据栏目和日期来获取文章列表。
     * 主要用于期刊类需求。
     * @param channelId
     * @param year
     * @param month
     * @return
     */
    public String getArticleListByChannelAndTime() {
        String returnXML = remoteService.getArticleListByChannelAndTime(channelId, year, month);
        print(returnXML);
        return XML;
    }
 
    /**
     * 文章的信息展示，并进行相关文章的动态的处理
     * @return
     */
    public String getArticleXmlInfo() {
        String returnXML = remoteService.getArticleXML(articleId);
        if(returnXML.indexOf(("<Response>")) < 0) {
            returnXML = "<Response>" + returnXML + "</Response>";
        }
        print(returnXML);
        return XML;
    }
    
    /**
     * 获取栏目树为portlet做展示
     * @return
     */
    public String getChannelTreeList4Portlet() {
        return print("DownloadChannelTree", remoteService.getChannelTree4Portlet(channelId));
    }
    
    /**
     * 全文检索action接口。
     * 供门户网站上通过本接口调用全文搜索。
     */
    public String search() {
        String returnXML = remoteService.search(tacticId, searchStr, page, pageSize);
        print(returnXML);
        return XML;
    }
    
    public void setChannelIds(String channelIds) {
        this.channelIds = channelIds;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }
    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
    public void setPage(Integer page) {
        this.page = page;
    }
    public void setMonth(String month) {
        this.month = month;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public void setTacticId(Long tacticId) {
        this.tacticId = tacticId;
    }
    public void setSearchStr(String searchStr) {
        try {
            if (searchStr != null) {
                // 处理非法字符
                searchStr = searchStr.replaceAll("]", "").replaceAll("[", ""); // '[' ']'在lucene里为非法字符
                searchStr = searchStr.replaceAll("}", "").replaceAll("{", ""); // '{' '}'在lucene里为非法字符
                searchStr = searchStr.replaceAll(")", "").replaceAll("(", ""); // '(' ')'在lucene里为非法字符
            }
        } catch (Exception e) {
            this.searchStr = searchStr;
        } finally {
            this.searchStr = searchStr;
        }
    }
    
    public void setRemoteService(IRemoteArticleService remoteService) {
        this.remoteService = remoteService;
    }
}