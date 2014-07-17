package com.jinhe.tss.cms.job;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.entity.Article;
import com.jinhe.tss.cms.service.IChannelService;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.FileHelper;

/** 
 * 文章过期处理（删除已过期的文章生成的xml文件） 
 */
public class ExpireJob extends AbstractCMSJob {

	protected void excuteCMSJob(String jobConfig) {
		
        Long siteId = EasyUtils.convertObject2Long(jobConfig.trim());
        IChannelService channelService = getChannelService();
        
        List<Article> expireList = new ArrayList<Article>();
		List<Long> channelIds = channelService.getAllEnabledChannelIds(siteId);
        for ( Long channelId : channelIds ) {
			expireList.addAll(channelService.getExpireArticlePuburlList(new Date(), channelId));
        }
        
        for ( Article article : expireList ) {
            article.setStatus(CMSConstants.OVER_STATUS);
            
            String pubUrl = article.getPubUrl();
            if ( !EasyUtils.isNullOrEmpty(pubUrl) ) {
            	FileHelper.deleteFile(new File(pubUrl)); // 删除文章生成的xml文件
            }
            
            getArticleService().updateArticle(article, null, null);
        }
        
        log.info("总共有 " + expireList.size() + " 条文章记录被设置为过期状态。");
	}
 
	protected JobStrategy getJobStrategy() {
		return JobStrategy.getExpireStrategy();
	}

}
