package com.jinhe.tss.cms.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jinhe.tss.cms.lucene.ArticleContent;
import com.jinhe.tss.cms.lucene.IndexHelper;
import com.jinhe.tss.framework.component.progress.Progress;
import com.jinhe.tss.framework.component.progress.ProgressManager;
import com.jinhe.tss.framework.component.progress.Progressable;
import com.jinhe.tss.util.EasyUtils;

public class IndexJob extends AbstractCMSJob implements Progressable {

	protected void excuteCMSJob(String jobConfig) {
        Long siteId = EasyUtils.convertObject2Long(jobConfig.trim());
		Set<ArticleContent> data = getData(siteId, true);
                
		JobStrategy strategy = getJobStrategy();
		strategy.site = getChannelService().getChannelById(siteId);
		IndexHelper.createIndex(strategy, data, new Progress(data.size())); 
	}
	
	// 需要建索引的所有文章地址列表
    private Set<ArticleContent> getData(Long siteId, boolean isIncrement) {
    	List<Long> channelIds = getChannelService().getAllEnabledChannelIds(siteId);
    	return getChannelService().getIndexableArticles(channelIds, isIncrement);
    }

	protected JobStrategy getJobStrategy() {
		return JobStrategy.getIndexStrategy();
	}
    
    public String createIndex(Long siteId, boolean isIncrement) {
    	Set<ArticleContent> data = getData(siteId, isIncrement);
    	
    	Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("data", data); // 需要建索引的所有文章地址列表
        paramsMap.put("siteId", siteId);
    	
    	String progressCode = new ProgressManager(this, data.size(), paramsMap).execute();
    	return progressCode;
    }
    
    /* 
     * 执行定时任务时启用进度条。
     */
    @SuppressWarnings("unchecked")
    public void execute(Map<String, Object> params, final Progress progress) {
	    JobStrategy strategy = getJobStrategy();
	    strategy.site = getChannelService().getChannelById((Long) params.get("siteId"));
	    
	    Object data = params.get("data");
	    IndexHelper.createIndex(strategy, (Set<ArticleContent>)data, progress); 
    }
}
