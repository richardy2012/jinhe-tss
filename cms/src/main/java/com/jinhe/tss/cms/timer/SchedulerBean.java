package com.jinhe.tss.cms.timer;

import java.text.ParseException;
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

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.entity.TimerStrategy;
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
	
	private static final String TIME_STRATEGY_NAME = "timerStrategy";
    
    @Autowired private TimerService timerService;
    
    private static Scheduler scheduler;
    
    /** 定时任务。*/
    public class TimerJob implements Job {
        public void execute(JobExecutionContext context) throws JobExecutionException {
            //添加管理员的用户信息
            String token = TokenUtil.createToken("1234567890", UMConstants.ADMIN_USER_ID); 
            IdentityCard card = new IdentityCard(token, OperatorDTO.ADMIN);
            Context.initIdentityInfo(card);
            
            //定时功能
            TimerStrategy timerStrategy = (TimerStrategy) context.getMergedJobDataMap().get(TIME_STRATEGY_NAME);
            log.info("定时任务：（" + timerStrategy.getName() + "）开始执行。");
            
            // 给当前启用状态下时间策略包括的索引策略创建索引文件
            String hql = "from TimerStrategy t where t.status = ? and t.parentId = ? order by t.id";
            List<?> strategyList = SchedulerBean.this.timerService.getEntities(hql, CMSConstants.STATUS_START, timerStrategy.getId());
            for ( Object temp : strategyList) {
                TimerStrategy strategy = (TimerStrategy) temp;
                
                // 取父节点（即定时策略）的indexPath做为索引/或发布路径
                strategy.setIndexPath(timerStrategy.getIndexPath());  
                
                // 执行时间策略下的其他策略
                SchedulerBean.this.timerService.excuteStrategy(strategy);
            }
            log.info("定时任务：（" + timerStrategy.getName() + "）执行完成。");
        }
    }
    
	private static String genJobName(Long timerStrategyId) {
		return "Job" + timerStrategyId;
	}
	
	private static String genTriggerName(Long timerStrategyId) {
		return "Trigger" + timerStrategyId;
	}
	
	private static JobDetail createJobDetail(TimerStrategy timerStrategy) {
		String jobDetailName = genJobName(timerStrategy.getId());
		return new JobDetail(jobDetailName, Scheduler.DEFAULT_GROUP, TimerJob.class);
	}
	
	private static CronTrigger createCronTrigger(TimerStrategy timerStrategy) throws ParseException {
		String triggerName = genTriggerName(timerStrategy.getId());
		return new CronTrigger(triggerName, Scheduler.DEFAULT_GROUP, timerStrategy.getContent());  // 第三个参数为定时时间
	}
	
	private static void scheduleJob(TimerStrategy timerStrategy) {
		try {
	        JobDetail jobDetail = createJobDetail(timerStrategy);
	        jobDetail.getJobDataMap().put(TIME_STRATEGY_NAME, timerStrategy);
	       
			Trigger trigger = createCronTrigger(timerStrategy);
	       
			scheduler.scheduleJob(jobDetail, trigger);
			
	    } catch (SchedulerException e) {
	        throw new BusinessException("初始化索引策略出错!", e);
	    } catch (java.text.ParseException e) {
	        throw new BusinessException("初始化索引策略出错!", e);
	    }
	}
	
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
            String hql = "from TimerStrategy t where t.type = ? and t.status <> 1";
            List<?> timerStrategys = timerService.getEntities(hql, CMSConstants.TACTIC_TIME_TYPE); 
            for (Object temp : timerStrategys) {
                TimerStrategy timerStrategy = (TimerStrategy) temp;
                
                scheduleJob(timerStrategy);
				
                if (CMSConstants.STATUS_STOP.equals(timerStrategy.getStatus())) {
                    scheduler.pauseTrigger(genTriggerName(timerStrategy.getId()), Scheduler.DEFAULT_GROUP);
                }
            }
            
            log.info("定时器管理对象初始化工作完成");
            
        } catch (SchedulerException e) {
            throw new BusinessException("初始化索引策略出错!", e);
        } 
    }
    
    /**
     * 启用一个定时器。
     */
    public static void startScheduler(Long timerStrategyId) {
        if(scheduler == null) return;
        
        try {
            scheduler.resumeTrigger(genTriggerName(timerStrategyId), Scheduler.DEFAULT_GROUP);
            log.info("成功启用定时任务， 定时器ID为：" + timerStrategyId);
        } catch (Exception e) {
            throw new BusinessException("启用定时任务时出错!可能是时间策略不合法,请正确设置时间策略", e);
        } 
    }
    
    /**
     * 停用一个定时器。
     */
    public static void stopScheduler(Long timerStrategyId) {
        if(scheduler == null) return;
        
        try {
            scheduler.pauseTrigger(genTriggerName(timerStrategyId), Scheduler.DEFAULT_GROUP);
            log.debug("成功停用定时任务， 定时器ID为：" + timerStrategyId);
        } catch (SchedulerException e) {
            throw new BusinessException("停用定时任务时出错!", e);
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
            log.info("成功修改定时任务， 定时器名称为：" + newTS.getName());
        } catch (java.lang.Exception e) {
            try {
                scheduleJob(oldTS);
            } catch(java.lang.Exception e1) {
                log.error("修改触发器和定时任务时，加入至定时器不成功后回滚又失败", e1);
            }
            
            // job回滚后再抛出异常信息
            throw new BusinessException("修改定时任务时， 发现时间策略（" + newTS.getContent() + "）不合法!", e);
        }
    }
    
    /**
     * 新建触发器和定时任务
     */
    public static void addTriggerAndJob(TimerStrategy timerStrategy) {
        if(scheduler == null) return;
        
        try {
            scheduleJob(timerStrategy);
            log.info("成功新增定时任务， 定时器名称为：" + timerStrategy.getName());
        } catch (Exception e) {
            throw new BusinessException("新增定时任务时 时间策略（" + timerStrategy.getContent() + "）不合法!", e);
        } 
    }
    
    /**
     * 删除触发器和定时任务
     */
    public static void removeTriggerAndJob(Long timerStrategyId) {
        if(scheduler == null) return;
        
        try {
            scheduler.unscheduleJob( genTriggerName(timerStrategyId), Scheduler.DEFAULT_GROUP );
            scheduler.deleteJob( genJobName(timerStrategyId), Scheduler.DEFAULT_GROUP );
            
            log.info("成功删除定时任务， 定时器ID为：" + timerStrategyId);
        } catch (SchedulerException e) {
            throw new BusinessException("删除定时任务错误!", e);
        }
    }
}
