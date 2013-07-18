package com.jinhe.tss.cms.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.persistence.pagequery.PageInfo;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.web.dispaly.grid.GridDataEncoder;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.util.EasyUtils;
 
@Controller
@RequestMapping("article")
public class ArticleAction extends BaseActionSupport {

	@Autowired private IArticleService articleService;
	
	/**
	 * 获取栏目下文章列表
	 */
	@RequestMapping(value = "/list/{channelId}/{page}", method = RequestMethod.GET)
	public void getChannelArticles(Long channelId, int page) {
	    
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

		print(new String[]{"ArticleList", "PageList"}, new Object[]{gEncoder, pageInfo});
	} 
	
	/**
	 * 初始化文章新增信息
	 */
	@RequestMapping(value = "/init/{channelId}", method = RequestMethod.POST)
	public void initArticleInfo(Long channelId) {
        Map<String, Object> initMap = new HashMap<String, Object>();
        initMap.put("isTop", CMSConstants.FALSE);
        initMap.put("author", Environment.getUserName()); // 默认作者为登录者，前台可进行修改
        
        XFormEncoder baseXFormEncoder = new XFormEncoder(CMSConstants.XFORM_ARTICLE, initMap);
        
        Long tempArticleId = System.currentTimeMillis();
        
        Map<String, Object> initMap4Upload = new HashMap<String, Object>();	
        initMap4Upload.put("id", tempArticleId);
        initMap4Upload.put("channelId", channelId);
		XFormEncoder attachmentXFormEncoder = new XFormEncoder(CMSConstants.XFORM_ARTICLEUPLOAD, initMap4Upload);
        
		GridDataEncoder attachmentGridEncoder = new GridDataEncoder(new ArrayList<Object>(), CMSConstants.GRID_ATTACHSLIST);
        
		print(new String[]{"ArticleInfo", "ArticleContent", "AttachsUpload", "AttachsList"}, 
                new Object[]{baseXFormEncoder, "<![CDATA[]]>", attachmentXFormEncoder, attachmentGridEncoder});
	}
    
	@RequestMapping(value = "/{articleId}", method = RequestMethod.GET)
    public void getArticleInfo(Long articleId) { 
        Article article = articleService.getArticleById(articleId);
 
        XFormEncoder baseXFormEncoder = new XFormEncoder(CMSConstants.XFORM_ARTICLE, article);
        XFormEncoder uploadXFormEncoder = new XFormEncoder(CMSConstants.XFORM_ARTICLEUPLOAD, article);
        
        List<Attachment> attachmentList = new ArrayList<Attachment>(article.getAttachments().values());
        GridDataEncoder attachmentGridEncoder = new GridDataEncoder(attachmentList, CMSConstants.GRID_ATTACHSLIST);
        
        print(new String[]{"ArticleInfo", "ArticleContent", "AttachsUpload", "AttachsList"}, 
                new Object[]{baseXFormEncoder, "<![CDATA[" + article.getContent() + "]]>", uploadXFormEncoder, attachmentGridEncoder});
    }
    
	/**
	 * 保存文章。
	 */
	@RequestMapping(value = "/{channelId}", method = RequestMethod.POST)
	public void saveArticleInfo(HttpServletRequest request, Long channelId, Article article, String attachList, String isCommit) {
        String articleContent = request.getParameter("articleContent");
        article.setContent(articleContent);
        
        if(article.getId() == null || article.getId().longValue() == 0) {
            // 新增的时候上传的附件对象以new Date()为主键，此处的"articleId"就是这个值
            Long articleId = EasyUtils.convertObject2Long(request.getParameter("articleId"));
	        articleService.createArticle(article, channelId, attachList, articleId); 
	    } 
	    else {
	        articleService.updateArticle(article, channelId, attachList);
	    }
        
        if( Config.TRUE.equalsIgnoreCase(isCommit) ){
            article.setStatus(CMSConstants.TOPUBLISH_STATUS);
        }
        
	    printSuccessMessage();
	}
 
	@RequestMapping(value = "/{articleId}", method = RequestMethod.DELETE)
	public void deleteArticle(Long articleId) {
	    articleService.deleteArticle(articleId);
	    printSuccessMessage("删除文章成功");
	}
	
	/**
	 * 移动文章（跨栏目移动）
	 */
	public void moveArticle(Long articleId, Long oldChannelId, Long channelId) {
	    articleService.moveArticle(articleId, oldChannelId, channelId);
        printSuccessMessage("移动文章成功");
	}
 
	/**
	 * 文章锁定
	 */
	public void lockingArticle(Long articleId) {
	    articleService.lockingArticle(articleId);
        printSuccessMessage("锁定文章成功");
	}
	
	/**
	 * 文章解锁
	 */
	public void unLockingArticle(Long articleId) {
	    articleService.unLockingArticle(articleId);
        printSuccessMessage("解除锁定成功");
	}
    
	/**
	 * 根据对栏目的权限过滤对文章的权限
	 */
	public void getArticleOperation(Long channelId) {
        PermissionHelper permissionHelper = PermissionHelper.getInstance();
        List<?> operations = permissionHelper.getOperationsByResource(CMSConstants.RESOURCE_TYPE_CHANNEL, channelId, Environment.getOperatorId());
        
        String permissionAll = "p1,p2,";
		for ( Object operation : operations ) {
			permissionAll += "a_" + operation + ",";
		}
        permissionAll += "cd1,cd2,cd3,cd4,cd5,ca1,ca2,ca3,ca4,ca5";
	    print("Operation", permissionAll);
	}
	
	/**
	 *  文章置顶和取消置顶
	 */
	public void doOrUndoTopArticle(Long articleId) {
	    articleService.doTopArticle(articleId);
        printSuccessMessage();
	}
	
	/**
	 *  获得栏目的文章列表以建立起关联关系
	 */
	public void getPageArticlesByChannel(Long channelId, int page) {
	    PageInfo pageInfo = articleService.getChannelArticles(channelId, page);
	    
	    List<Article> articles = new ArrayList<Article>();
	    List<?> list = pageInfo.getItems();
        if ( !EasyUtils.isNullOrEmpty(list) ) {
            for ( Object temp : list ) {
                Article article = ArticleHelper.createArticle((Object[]) temp);
                articles.add(article);
            }
        }
        print("AssociateArticleList", new TreeEncoder( articles ));
	}
	
    /**
     * 获取搜索文章的查询模板
     */
    public void getSearchArticleTemplate() {
        print("SearchArticle", new XFormEncoder(CMSConstants.XFORM_SEARCH_ARTICLE));
    }
    
	/**
	 *  搜索文章列表
	 */
	public void getArticleList(ArticleQueryCondition condition) {
        Object[] data = articleService.searchArticleList(condition);
		GridDataEncoder gEncoder = new GridDataEncoder(data[0], CMSConstants.GRID_TEMPLATE_ARTICLELIST);
        print(new String[]{"ArticleList", "PageList"}, new Object[]{gEncoder, (PageInfo)data[1]});
	}	
	
	
	/************ CMS对外Action接口，支持RSS。提供供Portlet等外界应用程序读取的文章列表、文章内容等接口。**************/
 
    @Autowired private IRemoteArticleService remoteService;
    
    /**
     * 获取栏目列表并且带有附件信息
     */
    public void getPicArticleListByChannel(Long channelId, int page, int pageSize) {
        String returnXML = remoteService.getPicArticleListByChannel(channelId, page, pageSize);
        print(returnXML);
    }
    
    /**
     * 获取栏目的文章列表
     */
    public void getArticleListByChannel(Long channelId, int page, int pageSize) {
        String returnXML = remoteService.getArticleListXMLByChannel(channelId, page, pageSize);
        print(returnXML);
    }

    /**
     * 获取栏目的文章列表。RSS2.0数据格式
     * http://localhost:8088/cms/cms!getArticleListByChannelRss.action?channelId=12&anonymous=true
     */
    public void getArticleListByChannelRss(Long channelId, int page, int pageSize) {
        String returnXML = remoteService.getArticleListByChannel4Rss(channelId, page, pageSize);
        print(returnXML);
    }

    /**
     * 根据栏目ids，获取这些栏目下的所有文章列表
     */
    public void getArticleListByChannels(String channelIds, int page, int pageSize) {
        if ( EasyUtils.isNullOrEmpty(channelIds) ) {
            throw new BusinessException("栏目IDs为空");
        }
        String returnXML = remoteService.queryArticlesByChannelIds(channelIds, page, pageSize);
        print(returnXML);
    }

    /**
     * 根据栏目id获取文章列表(深度)，取指定栏目以及该栏目下所有子栏目的所有文章列表
     */
    public void getArticleListDeeplyByChannel(Long channelId, int page, int pageSize) {
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
    public void getArticleListByChannelAndTime(Long channelId, String year, String month) {
        String returnXML = remoteService.getArticleListByChannelAndTime(channelId, year, month);
        print(returnXML);
    }
 
    /**
     * 文章的信息展示，并进行相关文章的动态的处理
     */
    public void getArticleXmlInfo(Long articleId) {
        String returnXML = remoteService.getArticleXML(articleId);
        if(returnXML.indexOf(("<Response>")) < 0) {
            returnXML = "<Response>" + returnXML + "</Response>";
        }
        print(returnXML);
    }
    
    /**
     * 获取栏目树为portlet做展示
     */
    public void getChannelTreeList4Portlet(Long channelId) {
        print("DownloadChannelTree", remoteService.getChannelTree4Portlet(channelId));
    }
    
    /**
     * 全文检索接口。
     * 供门户网站上通过本接口调用全文搜索。
     */
    public void search(Long tacticId, String searchStr, int page, int pageSize) {
    	try {
            if (searchStr != null) {
                // 处理非法字符
                searchStr = searchStr.replaceAll("]", "").replaceAll("[", ""); // '[' ']'在lucene里为非法字符
                searchStr = searchStr.replaceAll("}", "").replaceAll("{", ""); // '{' '}'在lucene里为非法字符
                searchStr = searchStr.replaceAll(")", "").replaceAll("(", ""); // '(' ')'在lucene里为非法字符
            }
        } catch (Exception e) {
        } 
    	
        String returnXML = remoteService.search(tacticId, searchStr, page, pageSize);
        print(returnXML);
    }
}