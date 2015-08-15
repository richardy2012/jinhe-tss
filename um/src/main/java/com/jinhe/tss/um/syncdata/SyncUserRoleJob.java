package com.jinhe.tss.um.syncdata;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.component.timer.AbstractJob;
import com.jinhe.tss.um.service.IGroupService;
import com.jinhe.tss.util.EasyUtils;

/**
 * 自动同步用户和角色对应关系，这关系可能由某第三方模块维护
 * 
 * com.jinhe.tss.um.syncdata.SyncUserRoleJob | 0 12 * * * ? | 
 * 		select userId user, roleId role1, roleId as role2 from xx_roleuser where updateTime > ?@datasource1
 * 
 */
public class SyncUserRoleJob extends AbstractJob {
	
	IGroupService groupService = (IGroupService) Global.getBean("GroupService");
 
	/* 
	 * jobConfig的格式为 : 
	 * 		sql @ datasource
	 */
	protected void excuteJob(String jobConfig) {
		log.info("开始用户对角色信息同步......");
		
		String info[] = EasyUtils.split(jobConfig, "@");
		if(info.length < 2)  {
			log.info("用户对角色信息同步的配置信息有误。" + jobConfig);
			return;
		}
		 
		String sql = info[0];
		String datasource = info[1];
		
		log.info("完成用户对角色信息同步。");
	}
	 
}
