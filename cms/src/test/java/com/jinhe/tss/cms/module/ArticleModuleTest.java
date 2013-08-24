package com.jinhe.tss.cms.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.jinhe.tss.cms.AbstractTestSupport;
import com.jinhe.tss.cms.entity.Article;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.helper.ArticleQueryCondition;
import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.um.UMConstants;

/**
 * 文章站点栏目相关模块的单元测试。
 */
public class ArticleModuleTest extends AbstractTestSupport {
 
	@Test
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
		articleAction.initArticleInfo(response, channelId);
        
        Long tempArticleId = System.currentTimeMillis();
        
        Article article = super.createArticle(channel1, tempArticleId);
        Long articleId = article.getId();
        
        articleAction.getArticleInfo(response, articleId);
        
        // 修改文章
        articleAction.saveArticleInfo(response, request, channelId, article, "", "false");
        
        List<?> list = getArticlesByChannel(channelId);
        assertNotNull(list);
        assertEquals(1, list.size());
        
        // 置顶、解除置顶
        articleAction.doOrUndoTopArticle(response, articleId);
        assertEquals(article.getIsTop(), UMConstants.TRUE);
        
        articleAction.doOrUndoTopArticle(response, articleId);
        assertEquals(article.getIsTop(), UMConstants.FALSE);
        
        // 获取搜索文章的查询模板
        articleAction.getSearchArticleTemplate(response);
        
        articleAction.getChannelArticles(response, channelId, 1);
        
        articleAction.getArticleOperation(response, channelId);
        
        ArticleQueryCondition condition = new ArticleQueryCondition();
        condition.setTitle("轮回");
        condition.setChannelId(channelId);
		articleAction.getArticleList(response, condition);
		
		// 移动文章
        articleAction.moveArticle(response, article.getId(), channel3.getId());
        assertEquals(article.getChannel().getId(), channel3.getId());
       
        // 最后删除文章、栏目、站点
        articleAction.deleteArticle(response, articleId);
        
        deleteSite(siteId);
        
        assertTrue(TestUtil.printLogs(logService) > 0);
    }
}
