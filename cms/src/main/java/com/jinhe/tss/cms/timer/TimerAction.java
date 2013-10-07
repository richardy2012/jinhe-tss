package com.jinhe.tss.cms.timer;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.service.IChannelService;
import com.jinhe.tss.framework.web.mvc.ProgressActionSupport;
 
@Controller
@RequestMapping("/auth/timer")
public class TimerAction extends ProgressActionSupport {

	@Autowired ITimerService timerService;
	@Autowired IChannelService channelService;
	
    /**
     * 即时执行策略
     * @param id
     * @param increment 是否增量操作  0：否  1：是
     */
	@RequestMapping("/excute/{siteId}/{strategyId}/{increment}")
    public void excuteStrategy(HttpServletResponse response, 
    		@PathVariable("siteId") Long siteId, 
    		@PathVariable("strategyId") int strategyId, 
    		@PathVariable("increment") int increment) {
		
		Channel site = channelService.getChannelById(siteId);
		
        TimerStrategy strategy = TimerStrategyHolder.getStrategyPool().get(strategyId);
        strategy.isIncrement = (CMSConstants.TRUE == increment);
        strategy.setSite(site);
        
        String code = timerService.excuteStrategy(strategy);
        
        printScheduleMessage(code);  
    }
}

