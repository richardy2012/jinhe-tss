package com.jinhe.tss.cms.timer;

import java.text.ParseException;
import java.util.Collection;

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

import com.jinhe.tss.cms.dao.IChannelDao;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.IdentityCard;
import com.jinhe.tss.framework.sso.TokenUtil;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.helper.dto.OperatorDTO;

/** 
 * 定时器调度。
 */
public class SchedulerBean {
    
	protected static Logger log = Logger.getLogger(SchedulerBean.class);
	
    private static Scheduler scheduler;
    
    @Autowired ITimerService timerService;
    @Autowired IChannelDao channelDao;
    
    /**
     * <bean class="com.jinhe.tss.cms.timer.SchedulerManager" init-method="init">
     * 在创建service时指定了调用本方法进行初始化
     */
    public void init() {
        if (scheduler != null) return; 
        
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            
            // 获取所有的定时策略
            Collection<?> strategys = TimerStrategyHolder.getStrategyPool().values();
            for (Object temp : strategys) {
                scheduleJob((TimerStrategy) temp);
            }
            
            log.info("定时器管理对象初始化工作完成");
            
        } catch (SchedulerException e) {
            throw new BusinessException("初始化索引策略出错!", e);
        } 
    }
    
    /** 定时任务。*/
    public class TimerJob implements Job {
        public void execute(JobExecutionContext context) throws JobExecutionException {
            //添加管理员的用户信息
            String token = TokenUtil.createToken("1234567890", UMConstants.ADMIN_USER_ID); 
            IdentityCard card = new IdentityCard(token, OperatorDTO.ADMIN);
            Context.initIdentityInfo(card);
            
            Collection<?> strategys = TimerStrategyHolder.getStrategyPool().values();
            for ( Object temp : strategys) {
                TimerStrategy strategy = (TimerStrategy) temp;
                
                // 执行时间策略下的其他策略
                log.info("定时任务：（" + strategy.name + "）开始执行。");
                SchedulerBean.this.timerService.excuteStrategy(strategy);
                log.info("定时任务：（" + strategy.name + "）执行完成。");
            }
        }
    }
    
	private static String genJobName(Integer timerStrategyId) {
		return "Job" + timerStrategyId;
	}
	
	private static String genTriggerName(Integer timerStrategyId) {
		return "Trigger" + timerStrategyId;
	}
	
	private static JobDetail createJobDetail(TimerStrategy timerStrategy) {
		String jobDetailName = genJobName(timerStrategy.id);
		return new JobDetail(jobDetailName, Scheduler.DEFAULT_GROUP, TimerJob.class);
	}
	
	private static CronTrigger createCronTrigger(TimerStrategy timerStrategy) throws ParseException {
		String triggerName = genTriggerName(timerStrategy.id);
		return new CronTrigger(triggerName, Scheduler.DEFAULT_GROUP, timerStrategy.timeDesc);  // 第三个参数为定时时间
	}
	
	private static void scheduleJob(TimerStrategy timerStrategy) {
		try {
	        JobDetail jobDetail = createJobDetail(timerStrategy);
	        jobDetail.getJobDataMap().put(timerStrategy.id, timerStrategy);
	       
			Trigger trigger = createCronTrigger(timerStrategy);
	       
			scheduler.scheduleJob(jobDetail, trigger);
			
	    } catch (SchedulerException e) {
	        throw new BusinessException("初始化索引策略出错!", e);
	    } catch (java.text.ParseException e) {
	        throw new BusinessException("初始化索引策略出错!", e);
	    }
	}

    /**
     * 修改触发器和定时任务(带有job任务的回滚功能)
     * @param newTS
     * @param oldTS
     */
	public static void editTriggerAndJob(TimerStrategy newTS, TimerStrategy oldTS){
        if(scheduler == null) return;
        
        try {
            scheduleJob(newTS);
            log.info("成功修改定时任务， 定时器名称为：" + newTS.name);
        } catch (java.lang.Exception e) {
            try {
                scheduleJob(oldTS);
            } catch(java.lang.Exception e1) {
                log.error("修改触发器和定时任务时，加入至定时器不成功后回滚又失败", e1);
            }
            
            // job回滚后再抛出异常信息
            throw new BusinessException("修改定时任务时， 发现时间策略【" + newTS.timeDesc + "】不合法!", e);
        }
    }
}
