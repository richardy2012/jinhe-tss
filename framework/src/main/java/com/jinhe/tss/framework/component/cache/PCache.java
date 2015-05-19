package com.jinhe.tss.framework.component.cache;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jinhe.tss.cache.CacheStrategy;
import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.Pool;
import com.jinhe.tss.framework.component.param.Param;
import com.jinhe.tss.framework.component.param.ParamService;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.util.BeanUtil;

/**
 * 结合Param模块来配置实现缓存池
 */
@Component
public class PCache {
	
	static Logger log = Logger.getLogger(PCache.class);
	
	@Autowired ParamService paramService;
	
	static int count = 0;
	
	public PCache() {
		new Thread() {
        	public void run() {
	            try {
	                while (true) {
                    	/* 定时检查，如果缓存配置有新增或修过，则刷新缓存池 */
                        sleep(180 * 1000); 
                        
                        refresh();
	                }
	            } catch (InterruptedException e) {
	            	throw new BusinessException("初始化PCache时出错！", e);
	            }
            }
        }.start(); 
	}
	
    /**
     * 检查是否有缓存相关的配置存在于系统参数中，有的话对其单独加载。
     * 第一次加载全部，之后只加载新建的缓存池配置。
     */
    public void refresh() {
    	Param cacheGroup = paramService.getParam(CacheHelper.CACHE_PARAM);
    	if(cacheGroup == null) {
    		return;
    	}
    	
    	List<Param> list = paramService.getParamsByParentCode(CacheHelper.CACHE_PARAM);
    	for(Param item : list) {
    		String cacheCode   = item.getCode();
    		String cacheConfig = item.getValue();
    		
    		Pool pool = JCache.pools.get(cacheCode);
			if(pool == null || count == 0) {
				rebuildCache(cacheCode, cacheConfig);
			}
    	}
    	count++;
    }
    
    @SuppressWarnings("unchecked")
    public static void rebuildCache(String cacheCode, String newConfig) {
		CacheStrategy strategy = new CacheStrategy();
		Pool pool = JCache.pools.get(cacheCode);
		if(pool != null) {
			BeanUtil.copy(strategy, pool.getCacheStrategy());
			strategy.pool = null;
		}
		
    	try {  
  			ObjectMapper objectMapper = new ObjectMapper();
  			
			Map<String, String> attrs = objectMapper.readValue(newConfig, Map.class);
			BeanUtil.setDataToBean(strategy, attrs);
		} 
    	catch (Exception e) {  
			log.error("CACHE_PARAM【" + cacheCode + "】的参数配置有误。\n" + newConfig, e);
			return;
  	    } 
    	
    	JCache.pools.put(cacheCode, strategy.getPoolInstance()); // 新建或覆盖
    }
}
