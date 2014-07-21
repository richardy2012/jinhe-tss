package com.jinhe.tss.um.syncdata;

import java.util.Map;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.component.progress.Progress;
import com.jinhe.tss.framework.component.progress.Progressable;
import com.jinhe.tss.framework.component.timer.AbstractJob;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.service.IGroupService;
import com.jinhe.tss.util.EasyUtils;

/**
 * 自动同步用户
 * com.jinhe.tss.um.syncdata.SyncUserJob | 0 06 * * * ? | 4,V5
 * 
 */
public class SyncUserJob extends AbstractJob {
 
	/* 
	 * jobConfig的格式为 : 
	 * 		groupId1,applicationId1
	 * 		groupId2,applicationId2
	 */
	protected void excuteJob(String jobConfig) {
		log.info("开始用户信息自动同步......");
		
		ISyncService syncService = (ISyncService) Global.getContext().getBean("SyncService");
		IGroupService groupService = (IGroupService) Global.getContext().getBean("GroupService");
		
		String[] jobConfigs = EasyUtils.split(jobConfig, "\n");
		
		for(int i = 0; i < jobConfigs.length; i++) {
			String info[] = EasyUtils.split(jobConfigs[i], ",");
			if(info.length < 2) continue;
			 
			Long groupId = EasyUtils.convertObject2Long(info[0]);
			Group group = groupService.getGroupById(groupId);
	        String fromGroupId = group.getFromGroupId();
	        if ( EasyUtils.isNullOrEmpty(fromGroupId) ) {
	            log.error("自动同步用户时，组【" + group.getName() + "】的对应外部应用组的ID（fromGroupId）为空。");
	            continue;
	        }
	        
	        Map<String, Object> datasMap = syncService.getCompleteSyncGroupData(groupId, info[1], fromGroupId);
	        ((Progressable) syncService).execute(datasMap, new Progress(10000));
		}
		
		log.info("完成用户信息自动同步。");
	}
	 
}
