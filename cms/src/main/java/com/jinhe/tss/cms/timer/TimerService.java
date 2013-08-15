package com.jinhe.tss.cms.timer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.dao.IArticleDao;
import com.jinhe.tss.cms.dao.IChannelDao;
import com.jinhe.tss.cms.entity.Article;
import com.jinhe.tss.cms.lucene.ArticleContent;
import com.jinhe.tss.cms.lucene.IndexHelper;
import com.jinhe.tss.cms.publish.PublishManger;
import com.jinhe.tss.framework.component.progress.Progress;
import com.jinhe.tss.framework.component.progress.ProgressManager;
import com.jinhe.tss.framework.component.progress.Progressable;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.FileHelper;

/**
 * 实现了进度条接口，执行创建lucene索引和发布文章时将启动进度条。
 * 
 */
@Service("TimerService")
public class TimerService implements Progressable, ITimerService {

	protected Logger log = Logger.getLogger(TimerService.class);
    
    private final static Set<Object> LOCK = Collections.synchronizedSet(new HashSet<Object>());

    @Autowired private IChannelDao channelDao;
    @Autowired private IArticleDao articleDao;
	@Autowired private PublishManger publishManger;
	
	@SuppressWarnings("unchecked")
	private List<Long> getAllEnabledChannelIds(Long siteId) {
		 String hql = "select o.id from Channel o where o.id <> o.site.id and o.disabled <> 1 and o.site.id = ?";
		return (List<Long>) channelDao.getEntities(hql, siteId);
	}
 
	public String excuteStrategy(TimerStrategy strategy) {
        synchronized (LOCK) {
        	Integer strategyId = strategy.id;
            Integer type = strategy.type;
            
            // 给策略进行加锁控制
            if (LOCK.contains(strategyId)) {
                throw new BusinessException("当前策略正在执行！策略名称 = " + strategy.name);
            }
            LOCK.add(strategyId);
            log.info("开始执行定时策略【" + strategy.name + "】");
            
            int total = 0;
            Map<String, Object> paramsMap = new HashMap<String, Object>();
            try {
            	Long siteId = strategy.site.getId();
				List<Long> channelIds = getAllEnabledChannelIds(siteId);
            	boolean isIncrement = strategy.isIncrement;
            	
                // 执行发布策略，给当前选中的发布策略发布文章
                if (CMSConstants.STRATEGY_TYPE_PUBLISH.equals(type)) {
                    paramsMap.put("type", CMSConstants.STRATEGY_TYPE_PUBLISH);
                    total = publishManger.getPublishableArticleCount4TimerJob(channelIds, paramsMap);
                } 
                
                // 执行索引策略，根据索引策略建立索引文件
                if (CMSConstants.STRATEGY_TYPE_INDEX.equals(type)) { 
                    paramsMap.put("indexStrategy", strategy);
                    paramsMap.put("type", CMSConstants.STRATEGY_TYPE_INDEX);
                    
					Set<ArticleContent> content = IndexHelper.getIndexableArticles(channelIds, isIncrement, channelDao, articleDao);
                    paramsMap.put("articleContentSet", content); // 需要建索引的所有文章地址列表
                        
                    total = content.size();
                } 
                
                // 执行过期策略：直接执行，不启用进度条(进度长度设为0即可)
                if (CMSConstants.STRATEGY_TYPE_EXPIRE.equals(type)) {
                    total = 0;
                    excuteExpireStrategy(strategy); 
                }
                
                // 交有进度条管理类另起线程执行策略的执行工作。
                String progressCode = new ProgressManager(this, total, paramsMap).execute();
				return progressCode;
                
            } catch (RuntimeException e) {
                throw new BusinessException("excute Strategy 出错，策略名称 = " + strategy.name, e);
            } finally {
                LOCK.remove(strategyId);
            }
        }
    }
	
    /* 
     * 执行定时任务时启用进度条。
     */
    @SuppressWarnings("unchecked")
    public void execute(Map<String, Object> params, final Progress progress) {
        Object type = params.get("type");
        
        if(CMSConstants.STRATEGY_TYPE_INDEX.equals(type)) { // 创建索引
            TimerStrategy strategy = (TimerStrategy) params.get("indexStrategy");
            Object content = params.get("articleContentSet");
            IndexHelper.createIndex(strategy, (Set<ArticleContent>)content, progress); 
        } 
        else if(CMSConstants.STRATEGY_TYPE_PUBLISH.equals(type)) {
            List<Long> channelIds = (List<Long>) params.get("channelIds");
            publishManger.publishArticle4TimerJob(channelIds, progress);
        }
        else { 
            // 如果是其它的像过期策略等，进度长度为0，不使用进度条
        }
    }
 
    /** 文章过期处理（删除已过期的文章生成的xml文件） */
    private void excuteExpireStrategy(TimerStrategy strategy) {
        List<Article> expireList = new ArrayList<Article>();
        
        Long siteId = strategy.site.getId();
		List<Long> channelIds = getAllEnabledChannelIds(siteId);
        for ( Long channelId : channelIds ) {
            expireList.addAll(articleDao.getExpireArticlePuburlList(new Date(), channelId));
        }
        
        for ( Article article : expireList ) {
            article.setStatus(CMSConstants.OVER_STATUS);
            
            String pubUrl = article.getPubUrl();
            if ( !EasyUtils.isNullOrEmpty(pubUrl) ) {
            	FileHelper.deleteFile(new File(pubUrl)); // 删除文章生成的xml文件
            }
        }
        articleDao.flush();
        
        log.info("总共有 " + expireList.size() + " 条文章记录被设置为过期状态。");
    }
}