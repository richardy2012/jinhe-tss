package com.jinhe.tss.cms.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.entity.Article;
import com.jinhe.tss.cms.entity.Attachment;
import com.jinhe.tss.cms.helper.ArticleHelper;
import com.jinhe.tss.cms.helper.ArticleQueryCondition;
import com.jinhe.tss.cms.helper.parser.ArticleGridParser;
import com.jinhe.tss.cms.service.IArticleService;
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
 
public class ArticleAction extends BaseActionSupport {

	private IArticleService articleService;
    
    private Article article = new Article();    // 文章信息
    private ArticleQueryCondition condition = new ArticleQueryCondition();  // 相关的文章信息
    
	private Long    articleId;
	private Long    channelId;
    private Long    toArticleId;
    private Long    oldChannelId;
    
    private Integer  isTop;            // 前台是否置顶的标记
	private String   articleContent;   // 正文内容
	private String   isCommit;         // 判断修改文章的时候是否是保存并提交
    
	private String  attachList;        // 附件列表
	
	// 分页信息
	private Integer page;
	private String  field;
	private Integer orderType;
	
	/**
	 * <p>
	 * 获取栏目下文章列表
	 * </p>
	 * @return
	 */
	public String getChannelArticles() {
        if (channelId == null) 
            throw new BusinessException("栏目id为空！");
        
        String orderBy;
        if(orderType == null || orderType > 0)  {
            orderBy = field;
        } 
        else {
            orderBy = field + " desc ";
        }

        PageInfo pageInfo = articleService.getChannelArticles(channelId, page, orderBy);

        List<Article> articles = new ArrayList<Article>();
        List<?> list = pageInfo.getItems();
        if ( !EasyUtils.isNullOrEmpty(list) ) {
            for ( Object temp : list ) {
                Article article = ArticleHelper.createArticle((Object[]) temp);
                articles.add(article);
            }
        }
        
		GridDataEncoder gEncoder = new GridDataEncoder(articles, 
		        CMSConstants.GRID_TEMPLATE_ARTICLELIST, new ArticleGridParser());

		return print(new String[]{"ArticleList", "PageList"}, new Object[]{gEncoder, pageInfo});
	} 
	
	/**
	 * 初始化文章新增信息
	 */
	public String initArticleInfo() {
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
        
		return print(new String[]{"ArticleInfo", "ArticleContent", "AttachsUpload", "AttachsList"}, 
                new Object[]{baseXFormEncoder, "<![CDATA[]]>", attachmentXFormEncoder, attachmentGridEncoder});
	}
    
    /**
     * 获取文章信息
     */
    public String getArticleInfo() { 
        Article article = articleService.getArticleById(articleId);
 
        XFormEncoder baseXFormEncoder = new XFormEncoder(CMSConstants.XFORM_ARTICLE, article);
        XFormEncoder uploadXFormEncoder = new XFormEncoder(CMSConstants.XFORM_ARTICLEUPLOAD, article);
        
        List<Attachment> attachmentList = new ArrayList<Attachment>(article.getAttachments().values());
        GridDataEncoder attachmentGridEncoder = new GridDataEncoder(attachmentList, CMSConstants.GRID_ATTACHSLIST);
        
        return print(new String[]{"ArticleInfo", "ArticleContent", "AttachsUpload", "AttachsList"}, 
                new Object[]{baseXFormEncoder, "<![CDATA[" + article.getContent() + "]]>", uploadXFormEncoder, attachmentGridEncoder});
    }
    
	/**
	 * 保存文章。或者是保存并提交文章。
	 */
	public String saveArticleInfo() {
        if( Config.TRUE.equalsIgnoreCase(isCommit) ){
            article.setStatus(CMSConstants.TOPUBLISH_STATUS);
        }
        
        article.setContent(articleContent);
	    if(article.getId() == null || article.getId().longValue() == 0) {
            //新增的时候上传的附件对象以new Date()为主键，此处的"articleId"就是这个值
	        articleService.createArticle(article, channelId, attachList, articleId); 
	    } 
	    else {
	        articleService.updateArticle(article, channelId, attachList);
	    }
	    return printSuccessMessage();
	}

	/**
	 * 删除文章
	 */
	public String deleteArticle() {
        if (articleId == null) 
            throw new BusinessException("文章id为空！");
        
	    articleService.deleteArticle(articleId);
	    return printSuccessMessage("删除文章成功");
	}
	
	/**
	 * 移动文章（跨栏目移动）
	 */
	public String moveArticle() {
        if (channelId == null || oldChannelId == null) 
            throw new BusinessException("栏目id为空！");
        if (articleId == null)
            throw new BusinessException("文章id为空！");
        
	    articleService.moveArticle(articleId, oldChannelId, channelId);
        return printSuccessMessage("移动文章成功");
	}
	
	/**
	 * 文章排序
	 */
	public String moveArticleDownOrUp() {
	    articleService.moveArticleDownOrUp(articleId, toArticleId, channelId);
        return printSuccessMessage("文章排序成功");
	}
    
	/**
	 * 文章锁定
	 */
	public String lockingArticle() {
	    articleService.lockingArticle(articleId);
        return printSuccessMessage("锁定文章成功");
	}
	/**
	 * 文章解锁
	 */
	public String unLockingArticle() {
	    articleService.unLockingArticle(articleId);
        return printSuccessMessage("解除锁定成功");
	}
    
	/**
	 * 根据对栏目的权限过滤对文章的权限
	 */
	public String getArticleOperation() {
        PermissionHelper permissionHelper = PermissionHelper.getInstance();
        List<?> operations = permissionHelper.getOperationsByResource(CMSConstants.RESOURCE_TYPE_CHANNEL, channelId, Environment.getOperatorId());
        
        String permissionAll = "p1,p2,";
		for ( Object operation : operations ) {
			permissionAll += "a_" + operation + ",";
		}
        permissionAll += "cd1,cd2,cd3,cd4,cd5,ca1,ca2,ca3,ca4,ca5";
	    return print("Operation", permissionAll);
	}
	
	/**
	 *  文章置顶和取消置顶
	 */
	public String doOrUndoTopArticle(){
	    if(CMSConstants.TRUE.equals(isTop)){
	        articleService.undoTopArticle(articleId);
	        return printSuccessMessage("置顶成功");
	    }
	    
	    articleService.doTopArticle(articleId);
        return printSuccessMessage("取消置顶成功");
	}
	
	/**
	 *  获得栏目的文章列表以建立起关联关系
	 */
	public String getPageArticlesByChannel() {
	    PageInfo pageInfo = articleService.getChannelArticles(channelId, page);
	    
	    List<Article> articles = new ArrayList<Article>();
	    List<?> list = pageInfo.getItems();
        if ( !EasyUtils.isNullOrEmpty(list) ) {
            for ( Object temp : list ) {
                Article article = ArticleHelper.createArticle((Object[]) temp);
                articles.add(article);
            }
        }
        return print("AssociateArticleList", new TreeEncoder( articles ));
	}
	
    /**
     * 获取搜索文章的查询模板
     * @return
     */
    public String getSearchArticleTemplate() {
        XFormEncoder xEncoder = new XFormEncoder(CMSConstants.XFORM_SEARCH_ARTICLE);
        return print("SearchArticle", xEncoder);
    }
	/**
	 *  搜索文章列表
	 */
	public String getArticleList() {
	    Object[] data = articleService.searchArticleList(condition);
		GridDataEncoder gEncoder = new GridDataEncoder(data[0], CMSConstants.GRID_TEMPLATE_ARTICLELIST, new ArticleGridParser());
        return print(new String[]{"ArticleList", "PageList"}, new Object[]{gEncoder, (PageInfo)data[1]});
	}	

    public Article getArticle() {
        return article;
    }
    public ArticleQueryCondition getCondition() {
        return condition;
    }
    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }
    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }
    public void setAttachList(String attachList) {
        this.attachList = attachList;
    }
    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
    public void setField(String field) {
        this.field = field;
    }
    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }
    public void setToArticleId(Long newArticleId) {
        this.toArticleId = newArticleId;
    }
    public void setOldChannelId(Long oldChannelId) {
        this.oldChannelId = oldChannelId;
    }
    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }
    public void setPage(Integer page) {
        this.page = page;
    }
    public void setIsCommit(String isCommit) {
        this.isCommit = isCommit;
    }

    public void setService(IArticleService service) {
        this.articleService = service;
    }
}