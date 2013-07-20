package com.jinhe.tss.cms.module;

import java.util.List;

import com.jinhe.tss.cms.AbstractTestSupport;
import com.jinhe.tss.cms.entity.Article;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.helper.ArticleQueryCondition;
import com.jinhe.tss.framework.test.TestUtil;

/**
 * 文章站点栏目相关模块的单元测试。
 */
public class ArticleModuleTest extends AbstractTestSupport {
 
    public void testArticleModule() {
    	// 新建站点
        Channel site = createSite();
        Long siteId = site.getId();
        
        // 新建栏目
        
        Channel channel1 = super.createChannel("时事评论", site, siteId);
        Channel channel2 = super.createChannel("体育新闻", site, siteId);
        Channel channel3 = super.createChannel("NBA战况", site, channel2.getId());
        Long channelId = channel1.getId();
        
        // 开始测试文章模块
		articleAction.initArticleInfo(channelId);
        
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
        
        articleAction.getArticleInfo(articleId);
        
        // 修改文章
        articleAction.saveArticleInfo(request, channelId, article, "1,2", "false");
        
        List<?> list = getArticleIdByChannelId(channelId);
        assertNotNull(list);
        assertEquals(23, list.size());
        Article article3 = (Article) list.get(2);
        
        // 移动文章
        articleAction.moveArticle(article3.getId(), channel3.getId(), channel2.getId());

        // 锁定、解锁
        articleAction.lockingArticle(articleId);
        articleAction.unLockingArticle(articleId);
        
        // 置顶、解除置顶
        articleAction.doOrUndoTopArticle(articleId);
        articleAction.doOrUndoTopArticle(articleId);
        
        // 获取搜索文章的查询模板
        articleAction.getSearchArticleTemplate();
        
        articleAction.getChannelArticles(channelId, 1);
        
        articleAction.getArticleOperation(channelId);
        
        ArticleQueryCondition condition = new ArticleQueryCondition();
        condition.setTitle("轮回");
		articleAction.getArticleList(condition );
       
        // 最后删除文章、栏目、站点
        articleAction.deleteArticle(articleId);
        
        super.deleteSite(siteId);
        
        assertTrue(TestUtil.printLogs(logService) > 0);
    }
}
