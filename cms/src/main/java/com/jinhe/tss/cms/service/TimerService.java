package com.jinhe.tss.cms.service;

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
import com.jinhe.tss.cms.entity.TimerStrategy;
import com.jinhe.tss.cms.lucene.ArticleContent;
import com.jinhe.tss.cms.lucene.IndexHelper;
import com.jinhe.tss.cms.publish.PublishManger;
import com.jinhe.tss.cms.timer.SchedulerBean;
import com.jinhe.tss.framework.component.progress.Progress;
import com.jinhe.tss.framework.component.progress.ProgressManager;
import com.jinhe.tss.framework.component.progress.Progressable;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.persistence.ICommonDao;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.FileHelper;

/**
 * 实现了进度条接口，执行创建lucene索引和发布文章时将启动进度条。
 * 
 */
@Service("TimerService")
public class TimerService implements ITimerService, Progressable {

	protected Logger log = Logger.getLogger(TimerService.class);
    
    private final static Set<Long> LOCK = Collections.synchronizedSet(new HashSet<Long>());

    @Autowired private ICommonDao commonDao;
    @Autowired private IChannelDao channelDao;
    @Autowired private IArticleDao articleDao;
	@Autowired private PublishManger publishManger;
 
	public String excuteStrategy(TimerStrategy strategy) {
        synchronized (LOCK) {
            Long strategyId = strategy.getId();
            Integer type = strategy.getType();
            
            // 给策略进行加锁控制
            if (LOCK.contains(strategyId)) {
                throw new BusinessException("当前策略正在执行！策略名称 = " + strategy.getName());
            }
            LOCK.add(strategyId);
            log.info("开始执行定时策略【" + strategy.getName() + "】");
            
            int total = 0;
            Map<String, Object> paramsMap = new HashMap<String, Object>();
            try {
                // 执行发布策略，给当前选中的发布策略发布文章
                if (CMSConstants.STRATEGY_TYPE_PUBLISH.equals(type)) {
                    paramsMap.put("type", CMSConstants.STRATEGY_TYPE_PUBLISH);
                    
                    String channelIds = strategy.getContent();
                    total = publishManger.getPublishableArticleCount4TimerJob(channelIds, paramsMap);
                } 
                
                // 执行索引策略，根据索引策略建立索引文件
                if (CMSConstants.STRATEGY_TYPE_INDEX.equals(type)) { 
                    paramsMap.put("indexStrategy", strategy);
                    paramsMap.put("type", CMSConstants.STRATEGY_TYPE_INDEX);
                    
                    Set<ArticleContent> content = IndexHelper.getIndexableArticles4Lucene(strategy, channelDao, articleDao);
                    paramsMap.put("articleContentSet", content); // 需要建索引的所有文章地址列表
                        
                    total = content.size();
                } 
                
                // 执行过期策略：直接执行，不启用进度条(进度长度设为0即可)
                if (CMSConstants.STRATEGY_TYPE_EXPIRE.equals(type)) {
                    total = 0;
                    excuteExpireStrategy(strategy); 
                }
                
                // 交有进度条管理类另起线程执行策略的执行工作。
                return new ProgressManager(this, total, paramsMap).execute();
                
            } catch (RuntimeException e) {
                throw new BusinessException("excute Strategy 出错，策略名称 = " + strategy.getName(), e);
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
        
        String[] channelIds = strategy.getContent().split(",");
        for ( String temp : channelIds ) {
            Long channelId = new Long(temp);
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
    
    //*-------------------------------- 定时策略维护（增删改停启用等）----------------------------------------

    public List<?> getAllTimerStrategy() {
        return commonDao.getEntities("from TimerStrategy order by id");
    }
    
    public TimerStrategy getStrategyById(Long strategyId) {
        Object strategy = commonDao.getEntity(TimerStrategy.class, strategyId);
        if (strategy == null) {
            throw new BusinessException("策略ID(" + strategyId + ")在数据库中不存在！");
        }
        
        return (TimerStrategy)strategy;
    }
    
    public TimerStrategy addTimeStrategy(TimerStrategy strategy) {
        String indexPath = strategy.getIndexPath();
        if( !EasyUtils.isNullOrEmpty(indexPath)) {
        	checkPath(indexPath);
        }
        
        strategy.setParentId( CMSConstants.HEAD_NODE_ID );
        strategy.setStatus( CMSConstants.STATUS_STOP );
        strategy.setType( CMSConstants.STRATEGY_TYPE_TIME );
        TimerStrategy timerStrategy = (TimerStrategy) commonDao.create(strategy);
        if (timerStrategy == null) {
            throw new BusinessException("新建时间策略不成功！");
        }
        
        if (CMSConstants.STRATEGY_TYPE_TIME.equals(timerStrategy.getType())) {
            // 创建触发器和定时任务
            SchedulerBean.addTriggerAndJob(timerStrategy);
        }
        return timerStrategy;
    }
    
    /**
     * 检测输入路径的正确性
     */
    private void checkPath(String indexPath) {
        File file = new File(indexPath);
        if ( file.exists() && file.isDirectory() && file.canWrite()) return;
        if( !file.exists() && file.mkdirs())  return;
        
        throw new BusinessException("索引策略的索引路径填写错误，不能生成相应的索引文件路径！");
    }
    
    public void removeTimeStrategy(Long strategyId) {
        TimerStrategy strategy = getStrategyById(strategyId);
        commonDao.delete(strategy);
        
        SchedulerBean.removeTriggerAndJob(strategyId);
        
        //删除时间索引下的其它类型索引
        commonDao.deleteAll(getAllStrategysByTimerStrategy(strategyId));
    }
    
    public void updateTimeStrategy(TimerStrategy timeStrategy) {
        TimerStrategy oldTimeStrategy = getStrategyById(timeStrategy.getId()); 

        if (CMSConstants.STRATEGY_TYPE_TIME.equals(timeStrategy.getType())) {
            SchedulerBean.removeTriggerAndJob(timeStrategy.getId());
            
            // 新建时间策略失败后，新建以前的时间策略
            SchedulerBean.editTriggerAndJob(timeStrategy, oldTimeStrategy);     
        }
        
        oldTimeStrategy.setName(timeStrategy.getName());
        oldTimeStrategy.setIndexPath(timeStrategy.getIndexPath());
        oldTimeStrategy.setContent(timeStrategy.getContent()); // 定时时间
        
        commonDao.update(oldTimeStrategy);
    }
    
    public void startTimeStrategy(Long strategyId) {
        changeTimeStrategyStatus(strategyId, CMSConstants.STATUS_START);
        SchedulerBean.startScheduler(strategyId);
    }
    
    public void stopTimeStrategy(Long strategyId) {
        changeTimeStrategyStatus(strategyId, CMSConstants.STATUS_STOP);
        SchedulerBean.stopScheduler(strategyId);
    }
    
    private void changeTimeStrategyStatus(Long timeStrategyId, Integer status){
        TimerStrategy timeStrategy = getStrategyById(timeStrategyId);
        timeStrategy.setStatus(status);
        commonDao.update(timeStrategy);
        
        List<?> strategyList = getAllStrategysByTimerStrategy(timeStrategy.getId());
        for (int i = 0; i < strategyList.size(); i++) {
            TimerStrategy strategy = (TimerStrategy) strategyList.get(i);
            if ( !status.equals(strategy.getStatus()) ) {
                strategy.setStatus(status);
                commonDao.update(strategy);
            }
        }
    }
    
    /**
     * 获取定时策略下挂着的所有策略。
     */
    private List<?> getAllStrategysByTimerStrategy(Long timerStrategyId) {
        return commonDao.getEntities("from TimerStrategy t where t.parentId = ? order by t.id", timerStrategyId);
    }
 
    // -----------------------------   以下操作的是定时策略的子策略，比如索引策略、发布策略、过期策略等 -----------------------------
    public TimerStrategy addStrategy(TimerStrategy strategy) {
        TimerStrategy timeStrategy = getStrategyById(strategy.getParentId());
        strategy.setStatus(timeStrategy.getStatus());
        strategy.setParentId(timeStrategy.getId());
 
        strategy = (TimerStrategy) commonDao.create(strategy);
        if (strategy == null) {
            throw new BusinessException("新建索引策略不成功！");
        }

        return strategy;
    }
 
    public void removeStrategy(Long id) {
        commonDao.delete(getStrategyById(id));
    }
 
    public void updateStrategy(TimerStrategy newStrategy) {
		TimerStrategy strategy = getStrategyById(newStrategy.getId());
        strategy.setName(newStrategy.getName());
        strategy.setIndexExecutorClass(newStrategy.getIndexExecutorClass());
        strategy.setRemark(newStrategy.getRemark());
        
        if(newStrategy.getContent() != null){
            strategy.setContent(newStrategy.getContent());
        }
        commonDao.update(strategy);
    }
 
    public void enableStrategy(Long strategyId) {
        TimerStrategy strategy = getStrategyById(strategyId);
        TimerStrategy timeStrategy = getStrategyById(strategy.getParentId());
        if (CMSConstants.STATUS_STOP.equals(timeStrategy.getStatus())) {
            throw new BusinessException("策略所属的时间策略处于停用状态，请先启用时间策略！");
        }
        
        strategy.setStatus(CMSConstants.STATUS_START);
        commonDao.update(strategy);
    }
 
    public void disableStrategy(Long strategyId) {
        TimerStrategy strategy = getStrategyById(strategyId);
        strategy.setStatus(CMSConstants.STATUS_STOP);
        commonDao.update(strategy);
    }
 
    public Object[] getStrategyAndChannels(Long strategyId) {
        TimerStrategy strategy;
        if(strategyId != null)
            strategy = getStrategyById(strategyId);
        else
            strategy = new TimerStrategy();
        
        return new Object[]{strategy, channelDao.getAllStartedSiteChannelList()};
    }
    
    public List<?> getEntities(String hql, Object...conditionValues) {
        return commonDao.getEntities(hql, conditionValues);
    }
}