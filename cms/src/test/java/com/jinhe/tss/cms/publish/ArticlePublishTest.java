package com.jinhe.tss.cms.publish;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.cms.AbstractTestSupport;
import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.entity.Article;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.lucene.ArticleContent;
import com.jinhe.tss.cms.lucene.IndexHelper;
import com.jinhe.tss.cms.timer.TimerAction;
import com.jinhe.tss.cms.timer.TimerStrategy;
import com.jinhe.tss.cms.timer.TimerStrategyHolder;
import com.jinhe.tss.framework.component.progress.Progress;
import com.jinhe.tss.framework.test.TestUtil;

/**
 * 文章发布相关模块的单元测试。
 */
public class ArticlePublishTest extends AbstractTestSupport {
	
	@Autowired TimerAction timerAction;
    
	@Test
    public void testArticlePublish() {
    	// 新建站点
        Channel site = createSite();
        Long siteId = site.getId();
        
        // 新建栏目
        Channel channel1 = super.createChannel("时事评论", site, siteId);
        Long channel1Id = channel1.getId();
        Channel channel2 = super.createChannel("环球新闻", site, channel1Id);
        
        // 开始测试文章模块
        Long tempArticleId = System.currentTimeMillis();
        
        Long articleId = super.createArticle(channel1, tempArticleId).getId();
        
//      Long channel1Id = channel1.getId();
//      String filePath = site.getPath() + "/" + site.getImagePath() + "/1.jpg";
//      super.uploadAttachment(channel1Id, tempArticleId, filePath, "JPG附件", CMSConstants.ATTACHMENTTYPE_PICTURE);
//      
//      filePath = site.getPath() + "/" + site.getDocPath() + "/1.docx";
//      super.uploadAttachment(channel1Id, tempArticleId, filePath, "Office附件", CMSConstants.ATTACHMENTTYPE_OFFICE);
        
        TestUtil.printEntity(super.permissionHelper, "Attachment"); 

        List<?> list = getArticlesByChannel(channel1Id);
 
        for(Object temp : list) {
            // 编辑 ---> 待发布
            Article tempArticle = (Article)temp;
            tempArticle.setStatus(CMSConstants.TOPUBLISH_STATUS);
        }
        
        // 站点栏目文章发布 category 1:增量发布 2:完全发布   2012.2.2 
        publishArticle(channel1Id, CMSConstants.PUBLISH_ADD);
        publishArticle(siteId, CMSConstants.PUBLISH_ADD);
        
        publishArticle(channel1Id, CMSConstants.PUBLISH_ALL);
        publishArticle(siteId, CMSConstants.PUBLISH_ALL);
        
        // 测试 articleAction 2012.2.2
        articleAction.getArticleListByChannel(channel1Id, 1, 12);
        articleAction.getPicArticleListByChannel(channel1Id, 1, 12);
        articleAction.getArticleListByChannelRss(channel1Id, 1, 12);
        articleAction.getArticleListDeeplyByChannel(channel1Id, 1, 12);
        
        articleAction.getArticleListByChannelAndTime(channel1Id, "2012", "02");
        
        String channelIds = channel1Id + "," + channel2.getId();
        articleAction.getArticleListByChannels(channelIds, 1, 12);
        
        articleAction.getArticleXmlInfo(articleId);
        
        articleAction.getChannelTreeList4Portlet(siteId);
        
        // 创建索引
        timerAction.excuteStrategy(response, siteId, strategyId, increment);
        
        TimerStrategy strategy = TimerStrategyHolder.getIndexStrategy();
        strategy.setSite(site);
        
        List<Long> channelIdList = Arrays.asList(channel1Id, channel2.getId());
        Set<ArticleContent> content = IndexHelper.getIndexableArticles(channelIdList, false, channelDao, articleDao);
        IndexHelper.createIndex(strategy, content, new Progress(list.size()));
        
        content = IndexHelper.getIndexableArticles(channelIdList, true, channelDao, articleDao);
        IndexHelper.createIndex(strategy, content, new Progress(list.size()));
        
        // 测试检索文章
        articleAction.search(siteId, "矛盾", 1, 10);
        articleAction.search(siteId, "过河卒子", 1, 10);
        articleAction.search(siteId, "技术创新", 1, 10); // 搜索附件
        
        // 最后删除文章、栏目、站点
        super.deleteSite(siteId);
        
        assertTrue(TestUtil.printLogs(logService) > 0);
    }
    
    private void publishArticle(Long channelId, String category) {
        // 判断是否对该栏目有发布权限
        publishManger.checkPublishPermission(channelId);
        
        int totalRows = channelService.getTotalRows4Publish(channelId, category);       
        int totalPageNum = totalRows / PublishManger.PAGE_SIZE ;
        if( totalRows % PublishManger.PAGE_SIZE > 0 ) {
            totalPageNum = totalPageNum + 1;
        }
            
        // 分页发布文章
        for (int k = 0; k < totalPageNum; k++) {
            List<Article> pageArticleList = channelService.getPageArticleList(channelId, k + 1, PublishManger.PAGE_SIZE, category);
            channelService.publishArticle(pageArticleList);
        }
    }
    
}
