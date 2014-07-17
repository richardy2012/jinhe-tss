package com.jinhe.tss.cms.job;

import com.jinhe.tss.cms.service.IArticleService;
import com.jinhe.tss.cms.service.IChannelService;
import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.component.timer.AbstractJob;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.IdentityCard;
import com.jinhe.tss.framework.sso.TokenUtil;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.helper.dto.OperatorDTO;
import com.jinhe.tss.util.EasyUtils;

public abstract class AbstractCMSJob extends AbstractJob {
	
	// jobConfig 为 siteIds
	protected void excuteJob(String jobConfig) {
		String token = TokenUtil.createToken("1234567890", UMConstants.ADMIN_USER_ID);
		IdentityCard card = new IdentityCard(token, OperatorDTO.ADMIN);
		Context.initIdentityInfo(card); // 模拟管理员登录
		
		JobStrategy strategy = getJobStrategy();
        log.info("开始执行定时策略【" + strategy.name + "】");
        
        try {
        	String[] jobConfigs = EasyUtils.split(jobConfig, ","); 
        	for(int i = 0; i < jobConfigs.length; i++) {
        		excuteCMSJob(jobConfigs[i]);
    		}
            
        } catch (RuntimeException e) {
            throw new BusinessException("excute Strategy 出错，策略名称 = " + strategy.name, e);
        } 
	}
 
	protected IChannelService getChannelService() {
		return (IChannelService) Global.getContext().getBean("ChannelService");
	}
	
	protected IArticleService getArticleService() {
		return (IArticleService) Global.getContext().getBean("ArticleService");
	}
	
	protected abstract void excuteCMSJob(String jobConfig);
	
	protected abstract JobStrategy getJobStrategy();
}
