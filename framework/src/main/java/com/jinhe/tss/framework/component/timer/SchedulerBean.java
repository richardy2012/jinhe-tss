package com.jinhe.tss.framework.component.timer;

import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

import com.jinhe.tss.framework.component.param.Param;
import com.jinhe.tss.framework.component.param.ParamManager;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.EasyUtils;

/** 
 * 定时器调度。
 */
@Component
public class SchedulerBean {
    
	protected Logger log = Logger.getLogger(this.getClass());
	
	static final String TIMER_PARAM_CODE = "TIMER_PARAM_CODE";
	
    private static Scheduler scheduler;
 
    public SchedulerBean() {
        new Thread() {
        	public void run() {
	            try {
	            	/* 休息会，等其他IOC池里的Bean就位（因依赖ParamManager）  */
	                sleep(1000 * 30); 
	                
	                init();
	                
	            } catch (InterruptedException e) {
	                log.error("初始化SchedulerBean时出错！", e);
	            }
            }
        }.start(); 
    }
    
    public void init() {
    	
        if (scheduler != null) return; 
        
        List<Param> list = ParamManager.getComboParam(TIMER_PARAM_CODE);
        if(list.isEmpty()) return;
        
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            
    		for(Param param : list) {
    			String code  = param.getText();
    			String value = param.getValue();
    			String configs[] = EasyUtils.split(value, "|"); // jobClassName | timeDescr | customizeConfig
    			
    			String jobName = "Job-" + code;
    			Class<?> jobClazz = BeanUtil.createClassByName(configs[0].trim());
    			JobDetail jobDetail = new JobDetail(jobName, Scheduler.DEFAULT_GROUP, jobClazz);
    			jobDetail.getJobDataMap().put(code, configs[2].trim());
    			
    			String triggerName = "Trigger-" + code;
    			Trigger trigger;
				try {
					trigger = new CronTrigger(triggerName, Scheduler.DEFAULT_GROUP, configs[1].trim()); // 第三个参数为定时时间
					scheduler.scheduleJob(jobDetail, trigger);
				} 
				catch (ParseException e) {
					log.error("初始化定时Job【" + jobName + "】失败, config = " + value, e);
				}  
    		}
            
            log.info("定时器管理对象初始化工作完成");
            
        } catch (SchedulerException e) {
            throw new BusinessException("初始化索引策略出错!", e);
        } 
    }
}

