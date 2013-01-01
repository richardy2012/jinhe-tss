package com.jinhe.tss.cms.timer;

import java.util.List;

import com.jinhe.tss.cms.entity.TimerStrategy;

/**
 * 全文检索服务接口
 */
public interface TimerService {
	
	/**
	 * 新建定时策略
	 * 
	 * @param condition
	 * @return
	 */
	TimerStrategy addTimeStrategy(TimerCondition condition);

	/**
	 * 新建非定时策略，比如索引策略、发布策略、过期策略等
	 * 
	 * @param condition
	 * @return
	 */
	TimerStrategy addStrategy(TimerCondition condition);

	/**
	 * 删除定时策略
	 * 
	 * @param tacticId
	 */
	void removeTimeStrategy(Long tacticId);

	/**
	 * 删除非定时策略，比如索引策略、发布策略、过期策略等
	 * 
	 * @param tacticId
	 */
	void removeStrategy(Long tacticId);

	/**
	 * 更新定时策略
	 * 
	 * @param condition
	 */
	void updateTimeStrategy(TimerCondition condition);

	/**
	 * 更新非定时策略，比如索引策略、发布策略、过期策略等
	 * 
	 * @param condition
	 */
	void updateTacticIndexAndPublish(TimerCondition condition);

	/**
	 * 启用非定时策略，比如索引策略、发布策略、过期策略等
	 * 
	 * @param tacticId
	 */
	void startStrategy(Long tacticId);

	/**
	 * 停用非定时策略，比如索引策略、发布策略、过期策略等
	 * 
	 * @param tacticId
	 */
	void stopStrategy(Long tacticId);

	/**
	 * 启用定时策略
	 * 
	 * @param tacticId
	 */
	void startTimeStrategy(Long tacticId);

	/**
	 * 停用定时策略
	 * 
	 * @param tacticId
	 */
	void stopTimeStrategy(Long tacticId);

	/**
	 * 初始化索引策略树
	 * 
	 * @return
	 */
	List<?> getAllTimerStrategy();

	/**
	 * 获取定时策略对象
	 * 
	 * @param tacticId
	 * @return
	 */
	TimerStrategy getStrategyById(Long tacticId);

	/**
	 * 获取非定时策略，比如索引策略、发布策略、过期策略等
	 * 
	 * @param tacticId
	 * @return
	 */
	Object[] getStrategyAndChannels(Long tacticId);

	/**
	 * 执行HQL语句
	 * 
	 * @param hql
	 * @param objects
	 * @return
	 */
	List<?> getEntities(String hql, Object...conditionValues);

	/**
	 * 执行策略，供定时器管理调用。
	 * 
	 * @param tacticIndex
	 */
	String excuteStrategy(TimerStrategy tacticIndex);
}