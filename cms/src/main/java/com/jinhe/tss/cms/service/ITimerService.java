package com.jinhe.tss.cms.service;

import java.util.List;

import com.jinhe.tss.cms.entity.TimerStrategy;

/**
 * 全文检索服务接口
 */
public interface ITimerService {
	
	/**
	 * 新建定时策略
	 */
	TimerStrategy addTimeStrategy(TimerStrategy strategy);

	/**
	 * 新建非定时策略，比如索引策略、发布策略、过期策略等
	 */
	TimerStrategy addStrategy(TimerStrategy strategy);

	/**
	 * 删除定时策略
	 * 
	 * @param id
	 */
	void removeTimeStrategy(Long id);

	/**
	 * 删除非定时策略，比如索引策略、发布策略、过期策略等
	 * 
	 * @param id
	 */
	void removeStrategy(Long id);

	/**
	 * 更新定时策略
	 */
	void updateTimeStrategy(TimerStrategy strategy);

	/**
	 * 更新非定时策略，比如索引策略、发布策略、过期策略等
	 */
	void updateStrategy(TimerStrategy strategy);

	/**
	 * 启用非定时策略，比如索引策略、发布策略、过期策略等
	 * 
	 * @param id
	 */
	void enableStrategy(Long id);

	/**
	 * 停用非定时策略，比如索引策略、发布策略、过期策略等
	 * 
	 * @param id
	 */
	void disableStrategy(Long id);

	/**
	 * 启用定时策略
	 * 
	 * @param id
	 */
	void startTimeStrategy(Long id);

	/**
	 * 停用定时策略
	 * 
	 * @param id
	 */
	void stopTimeStrategy(Long id);

	/**
	 * 初始化索引策略树
	 * 
	 * @return
	 */
	List<?> getAllTimerStrategy();

	/**
	 * 获取定时策略对象
	 * 
	 * @param id
	 * @return
	 */
	TimerStrategy getStrategyById(Long id);

	/**
	 * 获取非定时策略，比如索引策略、发布策略、过期策略等
	 * 
	 * @param id
	 * @return
	 */
	Object[] getStrategyAndChannels(Long id);

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
	 * @param strategy
	 */
	String excuteStrategy(TimerStrategy strategy);
}