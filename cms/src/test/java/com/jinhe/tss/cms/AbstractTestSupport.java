package com.jinhe.tss.cms;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.cms.action.ArticleAction;
import com.jinhe.tss.cms.action.ChannelAction;
import com.jinhe.tss.cms.entity.Article;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.publish.PublishManger;
import com.jinhe.tss.cms.service.IArticleService;
import com.jinhe.tss.util.BeanUtil;

public class AbstractTestSupport extends TxSupportTest4CMS {
    
    @Autowired protected ChannelAction channelAction;
    @Autowired protected ArticleAction articleAction;
    
    @Autowired protected IArticleService articleService;
    @Autowired protected PublishManger   publishManger;
    
    // 新建站点
    protected Channel createSite() {
        Channel site = new Channel();
        site.setParentId(CMSConstants.HEAD_NODE_ID);
        site.setName("我的门户" + System.currentTimeMillis());
        site.setPath("d:/Temp/cms");
        site.setDocPath("doc");
        site.setImagePath("img");
        site = channelService.createChannel(site);
        Long siteId = site.getId();
        assertNotNull(siteId);
        
        return site;
    }
    
    // 新建栏目
    protected Channel createChannel(String name, Channel site, Long parentId) {
        Channel channel = new Channel();
        BeanUtil.copy(channel, site, new String[]{"id", "name"});
        channel.setName(name);
        channel.setParentId(parentId);
        channel = channelService.createChannel(channel);
        
        return channel;
    }
    
    protected Article createArticle(Channel channel, Long tempArticleId) {
        Article article = new Article();
        article.setTitle("历史的轮回怪圈");
        article.setAuthor("Jon.King");
        article.setSubtitle("对于当前中国社会矛盾的历史思考");
        article.setKeyword("历史 轮回 社会矛盾");
        article.setSummary("历史 轮回 社会矛盾");
        article.setOverdueDate(new Date(System.currentTimeMillis() + 1000*60*60*24*365));
        
        String content = " 最近几年以来，社会矛盾逐渐成为了中国公众瞩目的焦点---" +
                "官员腐败，贫富差距扩大化，三农问题，仇富心态，教育改革，房产价格，医疗社保等一系列问题犹如走马灯" +
                "一般纷纷闯入人们的视野，同时，许多富于代表性的事件也先后挑战着中国人眼球和心理的承受能力。" +
                "我们的社会怎么了？相信只要稍微有点头脑的人都会问这个问题，也有很多人提出了解答。其中，有一种最为" +
                "通行的说法就是:历史的轮回。具体地说来，历朝历代的统治时期大致可以分为三个阶段：" +
                "1。天下创世 2。矛盾丛生3。总崩溃    个别王朝由于在中晚期实行了一些改革，缓和了社会矛盾，" +
                "从而延长了统治时间。这是中国历史所特有的周期性振荡。综观国史，史实让笔者也不得不承认上述说法的合理性。" +
                "那么，目前的中国是否在重新陷入这个历史的轮回怪圈呢？这倒的确是一个值得商榷的问题。";
        
		request.addParameter("articleContent", content);
		request.addParameter("articleId", tempArticleId.toString());
        
		articleAction.saveArticleInfo(request, channel.getId(), article, "1,2", "false");
        Long articleId = article.getId();
        assertNotNull(articleId);
        
        return article;
    }
    
    protected void deleteSite(Long siteId) {
        channelService.deleteChannel(siteId);
        List<?> list = channelService.getAllChannels();
        assertNotNull(list);
        assertTrue(list.isEmpty());
        
        String hql = "from Article a where a.channel.site.id=? ";
        List<?> articles = channelDao.getEntities(hql, siteId);
        assertTrue(articles.isEmpty());
    }
    
    protected List<?> getArticleIdByChannelId(Long channelId) {
        String hql = "from Article a where a.channel.id = ? ";
        return channelDao.getEntities(hql, channelId);
    }

}
