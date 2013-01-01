package com.jinhe.tss.cms.module;

import java.util.List;

import com.jinhe.tss.cms.CMSCommonOperation4TestSupport;
import com.jinhe.tss.cms.entity.Article;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.framework.test.TestUtil;

/**
 * 文章站点栏目相关模块的单元测试。
 */
public class ArticleModuleTest extends CMSCommonOperation4TestSupport {
 
    public void testArticleModule() {
    	// 新建站点
        Channel site = createSite();
        Long siteId = site.getId();
        
        // 新建栏目
        
        Channel channel1 = super.createChannel("时事评论", site, siteId);
        Channel channel2 = super.createChannel("体育新闻", site, siteId);
        Channel channel3 = super.createChannel("NBA战况", site, channel2.getId());
        
        // 开始测试文章模块
        articleAction.setChannelId(channel1.getId());
        articleAction.initArticleInfo();
        
        Long tempArticleId = System.currentTimeMillis();
        
//        Long channel1Id = channel1.getId();
//        String filePath = site.getPath() + "/" + site.getImagePath() + "/1.jpg";
//        super.uploadAttachment(channel1Id, tempArticleId, filePath, "JPG附件", CMSConstants.ATTACHMENTTYPE_PICTURE);
//        
//        filePath = site.getPath() + "/" + site.getDocPath() + "/1.docx";
//        super.uploadAttachment(channel1Id, tempArticleId, filePath, "Office附件", CMSConstants.ATTACHMENTTYPE_OFFICE);
        
        TestUtil.printEntity(permissionHelper, "Attachment"); 
        
        Article article = super.createArticle(channel1, tempArticleId);
        Long articleId = article.getId();
        
        TestUtil.printEntity(permissionHelper, "Attachment"); 
        
        articleAction.setArticleId(articleId);
        articleAction.getArticleInfo();
        
        // 修改文章
        articleAction.saveArticleInfo();
        
        List<?> list = getArticleIdByChannelId(channel1.getId());
        assertNotNull(list);
        assertEquals(23, list.size());
        Article article2 = (Article) list.get(1);
        Article article3 = (Article) list.get(2);
        
        // 移动文章
        articleAction.setArticleId(article3.getId());
        articleAction.setOldChannelId(channel3.getId());
        articleAction.setChannelId(channel2.getId());
        articleAction.moveArticle();
        
        // 上下排序文章
        articleAction.setArticleId(articleId);
        articleAction.setToArticleId(article2.getId());
        articleAction.setChannelId(channel1.getId());
        articleAction.moveArticleDownOrUp();
        
        // 锁定、解锁
        articleAction.setArticleId(articleId);
        articleAction.lockingArticle();
        articleAction.unLockingArticle();
        
        // 置顶、解除置顶
        articleAction.setArticleId(articleId);
        articleAction.setChannelId(channel1.getId());
        articleAction.setIsTop(1);
        articleAction.doOrUndoTopArticle();
        
        articleAction.setIsTop(0);
        articleAction.doOrUndoTopArticle();
        
        // 获取搜索文章的查询模板
        articleAction.getSearchArticleTemplate();
        
        articleAction.setChannelId(channel1.getId());
        articleAction.setOrderType(1);
        articleAction.setField("createTime");
        articleAction.getChannelArticles();
        
        articleAction.setChannelId(channel1.getId());
        articleAction.getArticleOperation();
        
        articleAction.getCondition().setChannelId(channel1.getId());
        articleAction.getCondition().setTitle("轮回");
        articleAction.getArticleList();
       
        // 最后删除文章、栏目、站点
        articleAction.setArticleId(articleId);
        articleAction.setChannelId(channel1.getId());
        articleAction.deleteArticle();
        
        super.deleteSite(siteId);
        
        assertTrue(TestUtil.printLogs(logService) > 0);
    }
}
