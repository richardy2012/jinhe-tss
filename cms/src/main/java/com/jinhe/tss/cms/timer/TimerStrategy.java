package com.jinhe.tss.cms.timer;

import com.jinhe.tss.cms.entity.Channel;


/** 
 * 各种定时策略类。
 * 包括有  1 索引策略 2 发布策略 3 过期策略
 * 
 * 其中第一层只能是时间策略，时间策略下可以创建其它三种策略并可以使用即使执行索引功能。
 */
public class TimerStrategy {
	
	Integer id;
    String  name;	     // 策略名称
    Integer type;        // 策略类型：0 时间策略 1 索引策略 2 发布策略 3 过期策略
    String  timeDesc;    // 定时时间描述，如 【0 15 10 * * ? 】 表示每天上午10:15触发
    boolean isIncrement; // 是否增量操作
    
    Channel site;         // 对应站点
	String indexPath;     // 发布 /索引文件存放目录
    String executorClass; // 发布 /索引实现类类名
    
    public boolean isIncrement() {
		return isIncrement;
	}

    public String getExecutorClass() {
		return executorClass;
	}
 
    public String getIndexPath(){
        return site.getPath() + "/" + this.indexPath;
    }
    
	public void setSite(Channel site) {
		this.site = site;
	}
	
	public String key() {
		return this.site.getId() + "-" + this.id;
	}
}

