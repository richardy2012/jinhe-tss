/* ==================================================================   
 * Created [2007-1-5] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2012-2015 
 * ================================================================== 
*/
package com.jinhe.tss.cache.extension.mixin;

import com.jinhe.tss.cache.Pool;
import com.jinhe.tss.cache.strategy.CacheConstants;

/** 
 * Mixin的接口实现
 */
public class DisableMixin implements IDisable {
    
    private Pool pool;
    
    public DisableMixin(Pool pool){
        this.pool = pool;
    }

    public void stop() {
        pool.getCacheStrategy().setDisabled(CacheConstants.TRUE);
    }

    public void start() {
        pool.getCacheStrategy().setDisabled(CacheConstants.FALSE);
    }
    
    public String toString() {
        return pool.toString();
    }

}

