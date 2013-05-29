package com.jinhe.tss.cms.module;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.cms.AbstractTestSupport;
import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.entity.TimerStrategy;
import com.jinhe.tss.cms.timer.TimerAction;
import com.jinhe.tss.cms.timer.TimerService;

/**
 * 定时策略相关模块的单元测试。
 */
public class TimerModuleTest extends AbstractTestSupport {
    
	TimerAction timerAction;
	
    @Autowired private TimerService   timerService;
    
    public void setUp() throws Exception {
        super.setUp();
        timerAction = new TimerAction();
        timerAction.setTimerService(timerService);
    }
 
    public void testArticleModule() {
        // 新建站点、栏目
        Channel site = createSite();
        Long siteId = site.getId();
        Channel channel1 = super.createChannel("时事评论", site, siteId);
        Channel channel2 = super.createChannel("环球新闻", site, siteId);
        
        TimerStrategy timerStrategy = new TimerStrategy();
        timerStrategy.setName("定时策略");
        timerStrategy.setType(CMSConstants.TACTIC_TIME_TYPE);
        timerStrategy.setContent("0 15 10 * * ?"); // 每天上午10:15触发
        timerStrategy.setIndexPath("d:/temp/cms/");
        timerAction.getCondition().setStrategy(timerStrategy);
        timerAction.addTacticTime();
        timerAction.updateTacticTime();
        
        TimerStrategy publishStrategy = new TimerStrategy();
        publishStrategy.setName("发布策略");
        publishStrategy.setType(CMSConstants.TACTIC_PUBLISH_TYPE);
        publishStrategy.setParentId(timerStrategy.getId());
        publishStrategy.setContent(channel1.getId() + "," + channel2.getId());
        timerAction.getCondition().setParentId(timerStrategy.getId());
        timerAction.getCondition().setStrategy(publishStrategy);
        timerAction.addTacticIndexAndPublish();
        timerAction.updateTacticIndexAndPublish();
        
        TimerStrategy indexStrategy = new TimerStrategy();
        indexStrategy.setName("索引策略");
        indexStrategy.setType(CMSConstants.TACTIC_INDEX_TYPE);
        indexStrategy.setParentId(timerStrategy.getId());
        indexStrategy.setContent(channel1.getId() + "," + channel2.getId());
        timerAction.getCondition().setParentId(timerStrategy.getId());
        timerAction.getCondition().setStrategy(indexStrategy);
        timerAction.addTacticIndexAndPublish();
        
        TimerStrategy expireStrategy = new TimerStrategy();
        expireStrategy.setName("文章过期策略");
        expireStrategy.setType(CMSConstants.TACTIC_EXPIRE_TYPE);
        expireStrategy.setParentId(timerStrategy.getId());
        expireStrategy.setContent(channel1.getId() + "," + channel2.getId());
        timerAction.getCondition().setParentId(timerStrategy.getId());
        timerAction.getCondition().setStrategy(expireStrategy);
        timerAction.addTacticIndexAndPublish();
        
        timerAction.initTacticIndex(); //读取定时策略树
        
        timerAction.getCondition().setTacticId(publishStrategy.getId());
        timerAction.getCondition().setType(CMSConstants.TACTIC_PUBLISH_TYPE);
        timerAction.getTacticIndexAndPublish();
        
        timerAction.getCondition().setTacticId(publishStrategy.getId());
        timerAction.getCondition().setType(CMSConstants.TACTIC_INDEX_TYPE);
        timerAction.getTacticIndexAndPublish();
        
        timerAction.getCondition().setTacticId(publishStrategy.getId());
        timerAction.getCondition().setType(CMSConstants.TACTIC_EXPIRE_TYPE);
        timerAction.getTacticIndexAndPublish();
        
        timerAction.getCondition().setTacticId(timerStrategy.getId());
        timerAction.getTacticTime();
        
        timerAction.getCondition().setTacticId(timerStrategy.getId());
        timerAction.startTacticTime();
        timerAction.stopTacticTime();
        timerAction.startTacticTime();
        
        timerAction.getCondition().setTacticId(publishStrategy.getId());
        timerAction.startTacticIndexAndPublish();
        timerAction.stopTacticIndexAndPublish();
        timerAction.startTacticIndexAndPublish();
        
        // 即时执行策略
        timerAction.getCondition().setTacticId(publishStrategy.getId());
        timerAction.instantTactic(); 
        timerAction.getCondition().setTacticId(indexStrategy.getId());
        timerAction.instantTactic(); 
        timerAction.getCondition().setTacticId(expireStrategy.getId());
        timerAction.instantTactic(); 
        
        timerAction.getCondition().setTacticId(publishStrategy.getId());
        timerAction.removeTacticIndexAndPublish();
        timerAction.getCondition().setTacticId(timerStrategy.getId());
        timerAction.removeTacticTime();
        
        super.deleteSite(siteId);
    }
    
}
