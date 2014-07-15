package com.jinhe.tss.framework.component.timer;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public abstract class AbstractJob implements Job {
	
	protected Logger log = Logger.getLogger(this.getClass());
	
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	JobDetail jobDetail = context.getJobDetail();
    	String jobName = jobDetail.getName();
    	
    	JobDataMap dataMap = jobDetail.getJobDataMap();
        
        log.info("定时任务：（" + jobName + "）开始执行。");
        
        excuteJob((String) dataMap.get(jobName));
        
        log.info("定时任务：（" + jobName + "）执行完成。");
    }
    
    protected abstract void excuteJob(String jobConfig);
}
