package com.jinhe.tss.cms.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.entity.Article;
import com.jinhe.tss.cms.entity.Attachment;
import com.jinhe.tss.cms.helper.ArticleHelper;
import com.jinhe.tss.cms.helper.ArticleQueryCondition;
import com.jinhe.tss.cms.service.IArticleService;
import com.jinhe.tss.cms.service.IRemoteArticleService;
import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.component.param.ParamConstants;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.persistence.pagequery.PageInfo;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.web.dispaly.grid.GridDataEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.util.DateUtil;
import com.jinhe.tss.util.EasyUtils;
 
@Controller
@RequestMapping("/auth/article")
public class ArticleAction extends BaseActionSupport {

	@Autowired private IArticleService articleService;
	
	/**
	 * 获取栏目下文章列表
	 */
	@RequestMapping(value = "/list/{channelId}/{page}")
	public void getChannelArticles(HttpServletResponse response, 
			@PathVariable("channelId") Long channelId, @PathVariable("page") int page) {
	    
        PageInfo pageInfo = articleService.getChannelArticles(channelId, page);

        List<Article> articles = new ArrayList<Article>();
        List<?> list = pageInfo.getItems();
        if ( !EasyUtils.isNullOrEmpty(list) ) {
            for ( Object temp : list ) {
                Article article = ArticleHelper.createArticle((Object[]) temp);
                articles.add(article);
            }
        }
		GridDataEncoder gEncoder = new GridDataEncoder(articles, CMSConstants.GRID_TEMPLATE_ARTICLELIST);

		print(new String[]{"ArticleList", "PageInfo"}, new Object[]{gEncoder, pageInfo});
	} 
	
	/**
	 * 初始化文章新增信息
	 */
	@RequestMapping(value = "/init/{channelId}", method = RequestMethod.GET)
	public void initArticleInfo(HttpServletResponse response, @PathVariable("channelId") Long channelId) {
        Map<String, Object> initMap = new HashMap<String, Object>();
        initMap.put("isTop", ParamConstants.FALSE);
        initMap.put("author", Environment.getUserName()); // 默认作者为登录者，前台可进行修改
        
        // 默认的文章发布日期及过期日期
        initMap.put("issueDate", DateUtil.format(new Date()));
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.YEAR, 1); // 默认一年后过期
        initMap.put("overdueDate", DateUtil.format(calendar.getTime()));
        
        XFormEncoder articleInfoXForm = new XFormEncoder(CMSConstants.XFORM_ARTICLE, initMap);
 
		GridDataEncoder attachGrid = new GridDataEncoder(new ArrayList<Object>(), CMSConstants.GRID_ATTACHSLIST);
        
		print(new String[]{"ArticleInfo", "ArticleContent", "AttachsList"}, 
                new Object[]{articleInfoXForm, "<![CDATA[]]>", attachGrid});
	}
    
	@RequestMapping(value = "/{articleId}", method = RequestMethod.GET)
    public void getArticleInfo(HttpServletResponse response, @PathVariable("articleId") Long articleId) { 
        Article article = articleService.getArticleById(articleId);
        String articleContent = article.getContent();
        Map<String, Object> attributes = article.getAttributes4XForm();
        attributes.remove("Content");
 
        XFormEncoder articleInfoXForm = new XFormEncoder(CMSConstants.XFORM_ARTICLE, attributes);
        
        List<Attachment> attachList = article.getAttachments();
        GridDataEncoder attachGrid = new GridDataEncoder(attachList, CMSConstants.GRID_ATTACHSLIST);
        
        print(new String[]{"ArticleInfo", "ArticleContent", "AttachsList"}, 
                new Object[]{articleInfoXForm, "<![CDATA[" + articleContent + "]]>", attachGrid});
    }
    
	/**
	 * 保存文章。
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void saveArticleInfo(HttpServletResponse response, 
			HttpServletRequest request, Article article) {
		
		String attachList = request.getParameter("attachList");
		String content = request.getParameter("ArticleContent");
		article.setContent(content);
		
        String isCommit = request.getParameter("isCommit");
        if( Config.TRUE.equalsIgnoreCase(isCommit) ){
            article.setStatus(CMSConstants.TOPUBLISH_STATUS);
        }
		
        Long channelId = article.getChannel().getId();
        if(article.getId() == null || article.getId() == 0) {
            // 新增的时候上传的附件对象以System.currentTimeMillis(参见CreateAttach类)为主键，此处的"articleId"就是这个值
            Long articleId = EasyUtils.convertObject2Long(request.getParameter("articleId"));
	        articleService.createArticle(article, channelId, attachList, articleId); 
	    } 
	    else {
	        articleService.updateArticle(article, channelId, attachList);
	    }
        
	    printSuccessMessage();
	}
 
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteArticle(HttpServletResponse response, @PathVariable("id") Long id) {
	    articleService.deleteArticle(id);
	    printSuccessMessage("删除文章成功");
	}
	
	/**
	 * 移动文章（跨栏目移动）
	 */
	@RequestMapping(value = "/move/{articleId}/{channelId}", method = RequestMethod.POST)
	public void moveArticle(HttpServletResponse response, 
			@PathVariable("articleId") Long articleId, @PathVariable("channelId") Long channelId) {
		
	    articleService.moveArticle(articleId, channelId);
        printSuccessMessage("移动文章成功");
	}

	/**
	 *  文章置顶和取消置顶
	 */
	@RequestMapping(value = "/top/{id}", method = RequestMethod.POST)
	public void doOrUndoTopArticle(HttpServletResponse response, @PathVariable("id") Long id) {
	    articleService.doTopArticle(id);
        printSuccessMessage();
	}
 
    /**
     * 获取搜索文章的查询模板
     */
	@RequestMapping(value = "/search/template", method = RequestMethod.GET)
    public void getSearchArticleTemplate(HttpServletResponse response) {
        print("SearchArticle", new XFormEncoder(CMSConstants.XFORM_SEARCH_ARTICLE));
    }
    
	/**
	 *  搜索文章列表
	 */
	@RequestMapping(value = "/search/result", method = RequestMethod.GET)
	public void getArticleList(HttpServletResponse response, ArticleQueryCondition condition) {
        Object[] data = articleService.searchArticleList(condition);
		GridDataEncoder gEncoder = new GridDataEncoder(data[0], CMSConstants.GRID_TEMPLATE_ARTICLELIST);
        print(new String[]{"ArticleList", "PageInfo"}, new Object[]{gEncoder, (PageInfo)data[1]});
	}	
	
	
	/************************** CMS对外Action接口，支持RSS。**************************************
	 ************************** 提供供Portlet等外界应用程序读取的文章列表、文章内容等接口。*********************/
 
    @Autowired private IRemoteArticleService remoteService;
 
    /**
     * 获取栏目的文章列表
     */
    @RequestMapping(value = "/list/xml/{channelId}/{page}/{pageSize}/{needPic}", method = RequestMethod.GET)
    public void getArticleListByChannel(HttpServletResponse response, 
    		@PathVariable("channelId") Long channelId, 
    		@PathVariable("page") int page, 
    		@PathVariable("pageSize") int pageSize, 
    		@PathVariable("needPic") boolean needPic) {
    	
        String returnXML = remoteService.getArticleListByChannel(channelId, page, pageSize, needPic);
        print(returnXML);
    }
 
    /**
     * 根据栏目ids，获取这些栏目下的所有文章列表
     */
    @RequestMapping(value = "/channels/{channelIds}/{page}/{pageSize}", method = RequestMethod.GET)
    public void getArticleListByChannels(HttpServletResponse response, 
    		@PathVariable("channelIds") String channelIds, 
    		@PathVariable("page") int page, 
    		@PathVariable("pageSize") int pageSize) {
    	
        if ( EasyUtils.isNullOrEmpty(channelIds) ) {
            throw new BusinessException("栏目IDs为空");
        }
        String returnXML = remoteService.queryArticlesByChannelIds(channelIds, page, pageSize);
        print(returnXML);
    }

    /**
     * 根据栏目id获取文章列表(深度)，取指定栏目以及该栏目下所有子栏目的所有文章列表
     */
    @RequestMapping(value = "/channelDeeply/{channelId}/{page}/{pageSize}", method = RequestMethod.GET)
    public void getArticleListDeeplyByChannel(HttpServletResponse response, 
    		@PathVariable("channelId") Long channelId,
    		@PathVariable("page") int page, 
    		@PathVariable("pageSize") int pageSize) {
    	
        String returnXML = remoteService.queryArticlesDeeplyByChannelId(channelId, page, pageSize);
        print(returnXML);
    }
    
    /**
     * 根据栏目和日期来获取文章列表。
     * 主要用于期刊类需求。
     * @param channelId
     * @param year
     * @param month
     */
    @RequestMapping(value = "/journal/{channelId}/{year}/{month}", method = RequestMethod.GET)
    public void getArticleListByChannelAndTime(HttpServletResponse response, 
    		@PathVariable("channelId") Long channelId, 
    		@PathVariable("year") String year, 
    		@PathVariable("month") String month) {
    	
        String returnXML = remoteService.getArticleListByChannelAndTime(channelId, year, month);
        print(returnXML);
    }
 
    /**
     * 文章的信息展示，并进行相关文章的动态的处理
     */
    @RequestMapping(value = "/xml/{articleId}", method = RequestMethod.GET)
    public void getArticleXmlInfo(HttpServletResponse response, @PathVariable("articleId") Long articleId) {
        String returnXML = remoteService.getArticleXML(articleId);
        if(returnXML.indexOf(("<Response>")) < 0) {
            returnXML = "<Response>" + returnXML + "</Response>";
        }
        print(returnXML);
    }
    
    /**
     * 第三方文章数据导入.
     */
    @RequestMapping(value = "/import/{channelId}", method = RequestMethod.GET)
    public void importArticle(HttpServletResponse response, HttpServletRequest request, 
    		@PathVariable("channelId") Long channelId) {
    	
    	String articleXml = request.getParameter("articleXml");
    	remoteService.importArticle(articleXml, channelId);
    }
    
    /**
     * 获取栏目树为portlet做展示
     */
    @RequestMapping(value = "/channelTree/{channelId}", method = RequestMethod.GET)
    public void getChannelTreeList4Portlet(HttpServletResponse response, @PathVariable("channelId") Long channelId) {
        print("ChannelTree", remoteService.getChannelTree4Portlet(channelId));
    }
    
    /**
     * 全文检索接口。
     * 供门户网站上通过本接口调用全文搜索。
     */
    @RequestMapping(value = "/search/{siteId}/{searchStr}/{page}/{pageSize}", method = RequestMethod.GET)
    public void search(HttpServletResponse response, HttpServletRequest request, 
    		@PathVariable("siteId") Long siteId, 
    		@PathVariable("page") int page, 
    		@PathVariable("pageSize") int pageSize) {
    	
    	String searchStr = request.getParameter("searchStr"); 
    	try {
            if (searchStr != null) {
                // 处理非法字符
                searchStr = searchStr.replaceAll("]", "").replaceAll("[", ""); // '[' ']'在lucene里为非法字符
                searchStr = searchStr.replaceAll("}", "").replaceAll("{", ""); // '{' '}'在lucene里为非法字符
                searchStr = searchStr.replaceAll(")", "").replaceAll("(", ""); // '(' ')'在lucene里为非法字符
            }
        } catch (Exception e) {
        } 
    	
        String returnXML = remoteService.search(siteId, searchStr, page, pageSize);
        print(returnXML);
    }
}