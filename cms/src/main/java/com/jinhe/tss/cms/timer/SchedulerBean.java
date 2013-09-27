package com.jinhe.tss.cms.timer;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.dao.IChannelDao;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.IdentityCard;
import com.jinhe.tss.framework.sso.TokenUtil;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.helper.dto.OperatorDTO;

/** 
 * 定时器调度。
 */
@Component
public class SchedulerBean {
    
	protected static Logger log = Logger.getLogger(SchedulerBean.class);
	
    private static Scheduler scheduler;
    
    @Autowired ITimerService timerService;
    @Autowired IChannelDao channelDao;
    
    /**
     * 在创建schedulerBean时指定了调用本方法进行初始化
     */
    public void init() {
        if (scheduler != null) return; 
        
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            
            // 获取所有的定时策略
            Collection<?> strategys = TimerStrategyHolder.getStrategyPool().values();
            
            // 给每个站点单独创建一组定时器
            List<?> sites = channelDao.getEntities("from Channel o where o.id = o.site.id");
            for(Object site : sites) {
            	for (Object temp : strategys) {
                    TimerStrategy strategy = (TimerStrategy) temp;
                    strategy.setSite((Channel) site);
					scheduleJob(strategy);
                }
            }
            
            log.info("定时器管理对象初始化工作完成");
            
        } catch (SchedulerException e) {
            throw new BusinessException("初始化索引策略出错!", e);
        } 
    }
    
	private static void scheduleJob(TimerStrategy strategy) {
		try {
	        JobDetail jobDetail = createJobDetail(strategy);
	        jobDetail.getJobDataMap().put(strategy.key(), strategy);
			Trigger trigger = createCronTrigger(strategy);
	       
			scheduler.scheduleJob(jobDetail, trigger);
			
	    } catch (Exception e) {
	        throw new BusinessException("初始化索引策略出错!", e);
	    } 
	}
	
	private static CronTrigger createCronTrigger(TimerStrategy strategy) throws ParseException {
		String triggerName = "Trigger" + strategy.key();
		return new CronTrigger(triggerName, Scheduler.DEFAULT_GROUP, strategy.timeDesc);  // 第三个参数为定时时间
	}
	
	private static JobDetail createJobDetail(TimerStrategy Strategy) {
		String jobDetailName = "Job" + Strategy.key();
		Class<?> jobClazz;
		if(CMSConstants.STRATEGY_TYPE_PUBLISH.equals( Strategy.type) ) {
			jobClazz = PublishTimerJob.class;
		}
		else if(CMSConstants.STRATEGY_TYPE_INDEX.equals( Strategy.type) ) {
			jobClazz = PublishTimerJob.class;
		}
		else {
			jobClazz = ExpireTimerJob.class;
		}
		return new JobDetail(jobDetailName, Scheduler.DEFAULT_GROUP, jobClazz);
	}
	
	   /** 定时任务。*/
    public class PublishTimerJob implements Job {
        public void execute(JobExecutionContext context) throws JobExecutionException {
        	excuteJob(TimerStrategyHolder.DEFAULT_PUBLISH_STRATEGY_ID);
        }
    }
    public class IndexTimerJob implements Job {
        public void execute(JobExecutionContext context) throws JobExecutionException {
        	excuteJob(TimerStrategyHolder.DEFAULT_INDEX_STRATEGY_ID);
        }
    }
    public class ExpireTimerJob implements Job {
        public void execute(JobExecutionContext context) throws JobExecutionException {
        	excuteJob(TimerStrategyHolder.DEFAULT_EXPIRE_STRATEGY_ID);
        }
    }
    
    private void excuteJob(int strategyId) {
        String token = TokenUtil.createToken("1234567890", UMConstants.ADMIN_USER_ID); 
        IdentityCard card = new IdentityCard(token, OperatorDTO.ADMIN);
        Context.initIdentityInfo(card); // 模拟管理员登录
        
        TimerStrategy strategy = TimerStrategyHolder.getStrategyPool().get(strategyId);
        
        log.info("定时任务：（" + strategy.name + "）开始执行。");
        
        this.timerService.excuteStrategy(strategy);
        
        log.info("定时任务：（" + strategy.name + "）执行完成。");
    }
}
