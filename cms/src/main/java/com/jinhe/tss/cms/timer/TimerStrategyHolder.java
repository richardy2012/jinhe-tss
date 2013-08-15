package com.jinhe.tss.cms.timer;

import java.util.HashMap;
import java.util.Map;

import com.jinhe.tss.cms.CMSConstants;

public class TimerStrategyHolder {
	
	public static int DEFAULT_PUBLISH_STRATEGY_ID = 1;
	public static int DEFAULT_INDEX_STRATEGY_ID   = 2;
	public static int DEFAULT_EXPIRE_STRATEGY_ID  = 3;
	
	private static Map<Object, TimerStrategy> holder;
	
	private TimerStrategyHolder() {
	}
	
	public static Map<Object, TimerStrategy> getStrategyPool() {
		if(holder == null) {
			holder = new HashMap<Object, TimerStrategy>();
			
	        TimerStrategy publishStrategy = new TimerStrategy();
	        publishStrategy.id = DEFAULT_PUBLISH_STRATEGY_ID;
	        publishStrategy.name = "发布策略";
	        publishStrategy.type = CMSConstants.STRATEGY_TYPE_PUBLISH;
	        publishStrategy.timeDesc = "0 15 06 * * ?";
	        publishStrategy.indexPath = "publish";
	        
	        TimerStrategy indexStrategy = new TimerStrategy();
	        indexStrategy.id = DEFAULT_INDEX_STRATEGY_ID;
	        indexStrategy.name = "索引策略";
	        indexStrategy.type = CMSConstants.STRATEGY_TYPE_INDEX;
	        indexStrategy.timeDesc = "0 15 07 * * ?";
	        indexStrategy.indexPath = "index";

	        TimerStrategy expireStrategy = new TimerStrategy();
	        expireStrategy.id = DEFAULT_EXPIRE_STRATEGY_ID;
	        expireStrategy.name = "文章过期策略";
	        expireStrategy.type = CMSConstants.STRATEGY_TYPE_EXPIRE;
	        expireStrategy.timeDesc = "0 15 08 * * ?";
	        
	        holder.put(publishStrategy.id, publishStrategy);
	        holder.put(indexStrategy.id, indexStrategy);
	        holder.put(expireStrategy.id, expireStrategy);
		}
		
		return holder;
	}
	
	public static TimerStrategy getIndexStrategy() {
		return getStrategyPool().get(DEFAULT_INDEX_STRATEGY_ID);
	}
	
	public static TimerStrategy getPublishStrategy() {
		return getStrategyPool().get(DEFAULT_PUBLISH_STRATEGY_ID);
	}
	
	public static TimerStrategy getExpireStrategy() {
		return getStrategyPool().get(DEFAULT_EXPIRE_STRATEGY_ID);
	}
}
