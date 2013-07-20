package com.jinhe.tss.cms.module;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.cms.AbstractTestSupport;
import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.action.TimerAction;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.entity.TimerStrategy;

/**
 * 定时策略相关模块的单元测试。
 */
public class TimerModuleTest extends AbstractTestSupport {
    
	@Autowired TimerAction timerAction;
 
    public void testArticleModule() {
        // 新建站点、栏目
        Channel site = createSite();
        Long siteId = site.getId();
        Channel channel1 = super.createChannel("时事评论", site, siteId);
        Channel channel2 = super.createChannel("环球新闻", site, siteId);
        
        TimerStrategy timerStrategy = new TimerStrategy();
        timerStrategy.setName("定时策略");
        timerStrategy.setType(CMSConstants.STRATEGY_TYPE_TIME);
        timerStrategy.setContent("0 15 10 * * ?"); // 每天上午10:15触发
        timerStrategy.setIndexPath("d:/temp/cms/");
        timerAction.addTimeStrategy(timerStrategy);
        timerAction.updateTimeStrategy(timerStrategy);
        
        Long timerStrategyId = timerStrategy.getId();
        
        TimerStrategy publishStrategy = new TimerStrategy();
        publishStrategy.setName("发布策略");
        publishStrategy.setType(CMSConstants.STRATEGY_TYPE_PUBLISH);
		publishStrategy.setParentId(timerStrategyId);
        publishStrategy.setContent(channel1.getId() + "," + channel2.getId());
        timerAction.addStrategy(publishStrategy);
        timerAction.updateStrategy(publishStrategy);
        
        Long publishStrategyId = publishStrategy.getId();
        
        TimerStrategy indexStrategy = new TimerStrategy();
        indexStrategy.setName("索引策略");
        indexStrategy.setType(CMSConstants.STRATEGY_TYPE_INDEX);
        indexStrategy.setParentId(timerStrategyId);
        indexStrategy.setContent(channel1.getId() + "," + channel2.getId());
        timerAction.addStrategy(indexStrategy);
        
        Long indexStrategyId = indexStrategy.getId();
        
        TimerStrategy expireStrategy = new TimerStrategy();
        expireStrategy.setName("文章过期策略");
        expireStrategy.setType(CMSConstants.STRATEGY_TYPE_EXPIRE);
        expireStrategy.setParentId(timerStrategyId);
        expireStrategy.setContent(channel1.getId() + "," + channel2.getId());
        timerAction.addStrategy(expireStrategy);
        
        Long expireStrategyId = expireStrategy.getId();
        
        
        timerAction.initIndexStrategy(); //读取定时策略树
        
		timerAction.getStrategy(publishStrategyId);
        
        timerAction.getTimeStrategy(timerStrategyId);
        
        timerAction.startTimeStrategy(timerStrategyId);
        timerAction.stopTimeStrategy(timerStrategyId);
        timerAction.startTimeStrategy(timerStrategyId);
        
        timerAction.disableStrategy(publishStrategyId);
        timerAction.enableStrategy(publishStrategyId);
        timerAction.disableStrategy(publishStrategyId);
        
        // 即时执行策略
        timerAction.instantStrategy(publishStrategyId, 1); 
		timerAction.instantStrategy(indexStrategyId, 0); 
		timerAction.instantStrategy(expireStrategyId, 0); 
        
        timerAction.removeStrategy(publishStrategyId);
        timerAction.removeTimeStrategy(timerStrategyId);
        
        super.deleteSite(siteId);
    }
    
}
