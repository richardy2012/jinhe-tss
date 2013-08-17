package com.jinhe.tss.cms.timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.service.IChannelService;
import com.jinhe.tss.framework.web.mvc.ProgressActionSupport;
 
@Controller
@RequestMapping("timer")
public class TimerAction extends ProgressActionSupport {

	@Autowired ITimerService timerService;
	@Autowired IChannelService channelService;
	
    /**
     * 即时执行策略
     * @param id
     * @param increment 是否增量操作  0：否  1：是
     */
	@RequestMapping("/excute/{siteId}/{id}/{increment}")
    public void excuteStrategy(Long siteId, Integer strategyId, int increment) {
		Channel site = channelService.getChannelById(siteId);
		
        TimerStrategy strategy = TimerStrategyHolder.getStrategyPool().get(strategyId);
        strategy.isIncrement = (CMSConstants.TRUE == increment);
        strategy.setSite(site);
        
        String code = timerService.excuteStrategy(strategy);
        
        printScheduleMessage(code);  
    }
}

