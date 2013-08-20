package com.jinhe.tss.cms.module;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.cms.AbstractTestSupport;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.timer.SchedulerBean;
import com.jinhe.tss.cms.timer.TimerAction;
import com.jinhe.tss.cms.timer.TimerStrategyHolder;

/**
 * 定时策略相关模块的单元测试。
 */
public class TimerModuleTest extends AbstractTestSupport {
    
	@Autowired TimerAction timerAction;
	
	@Autowired SchedulerBean schedulerBean;
 
	@Test
    public void testTimer() {
        // 新建站点、栏目
        Channel site = createSite();
        Long siteId = site.getId();
        Channel channel1 = super.createChannel("时事评论", site, siteId);
        Channel channel2 = super.createChannel("环球新闻", site, siteId);
        
        Long tempArticleId = System.currentTimeMillis();
		super.createArticle(channel1, tempArticleId );
		super.createArticle(channel2, tempArticleId );
         
        // 即时执行策略
        timerAction.excuteStrategy(siteId, TimerStrategyHolder.DEFAULT_PUBLISH_STRATEGY_ID, 1); 
		timerAction.excuteStrategy(siteId, TimerStrategyHolder.DEFAULT_INDEX_STRATEGY_ID, 0); 
		timerAction.excuteStrategy(siteId, TimerStrategyHolder.DEFAULT_EXPIRE_STRATEGY_ID, 0); 
		
		// 测试定时器
		schedulerBean.init();
        
        super.deleteSite(siteId);
    }
    
}
