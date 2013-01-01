package com.jinhe.tss.cms.timer;

import com.jinhe.tss.cms.entity.TimerStrategy;

/**
 * 定时策略相关帮助Condition类。
 */
public class TimerCondition {

	private Long 	tacticId;  // 策略id
	private Long 	parentId;  // 父节点id
	private Integer type;      // 类型
	
	/** 索引策略 */
	private TimerStrategy strategy = new TimerStrategy(); 
	
	/** 是否增量操作  0：否  1：是 */
	private Integer increment; 

	public Long getTacticId() {
		return tacticId;
	}

	public void setTacticId(Long tacticId) {
		this.tacticId = tacticId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public TimerStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(TimerStrategy strategy) {
		this.strategy = strategy;
	}
	
	public Integer getIncrement() {
		return increment;
	}

	public void setIncrement(Integer increment) {
		this.increment = increment;
	}
}