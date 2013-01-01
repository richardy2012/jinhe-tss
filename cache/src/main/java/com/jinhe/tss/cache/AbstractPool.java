/* ==================================================================   
 * Created [2007-1-3] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2012-2015 
 * ================================================================== 
*/

package com.jinhe.tss.cache;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.jinhe.tss.cache.strategy.CacheStrategy;

/** 
 * 对象池抽象基类，定义通用的方法。
 * 
 */
public abstract class AbstractPool implements Pool {
    protected Logger log = Logger.getLogger(this.getClass());

    // 对象池属性
    
    /** 请求数 */
    protected long requests;
    
    /** 命中数 */
    protected long hits;
    
    /** 是否已释放 */
    protected boolean released = false;
    
    /** 是否异步销毁 */
    protected boolean asyncDestroy = false;
    
    /** 缓存策略 */
    protected CacheStrategy strategy;
    
    /** 缓存自定义类 */
    protected CacheCustomizer customizer;

    /** 监听器列表 */
    protected Set<Listener> listeners = new HashSet<Listener>();
    
    public String getName() { 
    	return strategy.getName(); 
    }
    
	/**
	 * 返回空闲状态的对象池
	 */
    public abstract Container getFree();

	/**
	 * 返回使用状态的对象池
	 */
	public abstract Container getUsing();
	
	public String toString() {
		String display = "缓存池【" + this.getName() + "】的当前快照：";
		if( getFree() != null ) {
            display += getFree();
        }
		if( getUsing() != null ) {
		    display += getUsing();
		}
        return display;
	}
 
    public Cacheable getObject(Object key) {
        if(key == null) { 
            return null;
        }
        
        Cacheable item = getObjectOnly(key);
        addRequests();
        if(item != null) { // 命中则增加命中数
            addHits(); 
            item.addHit();
            item.updateAccessed();
        } 
        else {
            // 调用ICacheCustomizer来载入需要的缓存项，取到则放入缓存池中
            item = reload(new TimeWrapper(key, null));
            if(item != null) {
                putObject(item.getKey(), item.getValue());
            }
        }       
        return item;
    }
    
	/**
	 * 根据key值从缓存池中获取一个对象，不会增加点击率、请求数等信息。<br>
	 * 因为getObject(Object key)方法会增加缓存项的点击率，所以实现本方法以供缓存池内部维护调用。<br>
	 * 
	 * @param key
	 * @return
	 */
    protected Cacheable getObjectOnly(Object key) {
        if (released) {
            log.error("缓存池【" + getName() + "】已经被释放，所有缓存项都已经被清空!");
        }
        
        Cacheable item = getFree().get(key);
        if( item == null ) {
        	item = getUsing().get(key);
        }
		return item; 
    }

    public Cacheable putObject(Object key, Object value) {
		if (key == null) {
			return null;
		}

		Cacheable oldItem = getObjectOnly(key);
		if (oldItem != null) {
			oldItem.update(value);
			return oldItem;
		} else {
			// 缓存项放入缓存池的同时也设置了其生命周期
			Cacheable newItem = new TimeWrapper(key, value, strategy.getCyclelife());
			newItem = getFree().put(key, newItem);
			
			// 事件监听器将唤醒所有等待中的线程，包括cleaner线程，checkout，remove等方法的等待线程
			firePoolEvent(PoolEvent.PUT_IN);
			return newItem;
		}
	}

    public Cacheable removeObject(Object key) {
        Cacheable item = getFree().remove(key);
        
        firePoolEvent(PoolEvent.REMOVE);
        return item;
    }

    public void flush() {
        release(true);
        resetHitCounter();
        log.debug("已经清除池中所有的缓存项。");
    }
    
    public Cacheable reload(final Cacheable obj) throws RuntimeException {
        Cacheable newObj = customizer.reloadCacheObject(obj);
        
        // 如果重新加载的缓存项为空，则将原先的缓存项从缓存池中移除，否则则覆盖原有的缓存项。
        Object key = obj.getKey();
        if(newObj == null) {
			removeObject(key);
        } else {
            newObj = putObject(key, newObj.getValue());
        }
        return newObj;
    }
    
    /**
     * 销毁指定对象（如果有必要的话可采用异步）；
     */
    public void destroyObject(final Cacheable o) {
        if (o == null)
            return;
        
        // 异步销毁
        if ( asyncDestroy ) {
            Thread t = new Thread(new Runnable() {
                public void run() { 
                    customizer.destroy(o); 
                }
            });
            t.start();
        } 
        else {
            customizer.destroy(o);
        }

    }
    
    public final void releaseAsync(final boolean forced) {
        Thread t = new Thread(new Runnable(){
            public void run() { 
                release(forced); 
            }
        });
        t.start();
    }
    
    protected Cacheable checkOut() {
        Cacheable item = getFree().getByAccessMethod(strategy.getAccessMethod());
        if(item != null) {
            item.addHit();
            getFree().remove(item.getKey());
            getUsing().put(item.getKey(), item);
        }
        return item;
    }
    
    public Cacheable checkOut(long timeout) {
        if(timeout <= 0) {
            timeout = strategy.getInterruptTime();
        }
        
        long time = System.currentTimeMillis();
        Cacheable item =  checkOut();
        synchronized(this) {
            while (item == null  &&  (System.currentTimeMillis() - time < timeout)) {
                try {
                    log.debug("缓存池【" + getName() + "】中没有可用的缓存项......等待 " + timeout + "（毫秒）");
                    wait(timeout);
                    item = checkOut();
                } catch (InterruptedException e) { 
                    log.error("检出时等待被中断", e); 
                }
            }
        }
        
        if(item == null) {
            String errorMsg = "缓存池【" + getName() + "】已满，且各缓存项都处于使用状态，需要等待。可考虑重新设置缓存策略！";
            log.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        return item;
    }

    public void checkIn(Cacheable item) {
        if (item == null) {
            log.error("试图返回空的缓存项。");
            return;
        }
        Object key = item.getKey();
        
        // 判断对象是否存在using池中，是的话将对象从using池中移出，否则抛出异常
		Cacheable temp = getUsing().remove(key);
		if( !item.equals(temp) ) {
            String errorMsg = "试图返回不是using池中对象到free中，返回失败！ " + getName();
			log.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
		
        Object value = item.getValue();
        
        //如果池已满，则销毁对象，否则则放回池中
        int maxSize = strategy.getPoolSize();
        if (maxSize > 0 && size() >= maxSize) {
            destroyObject(item);
        } 
        else {
            try{ 
                // 如果对象实现了Reusable接口，则执行重置操作
                if(value instanceof Reusable) {
                    ((Reusable)value).recycle(); 
                }
                
                // 需重新放入free池中, 其点击率等属性已改变
                getFree().put(key, item); 
                
                //事件监听器将唤醒所有等待中的线程，包括cleaner线程，checkout，remove等操作的等待线程
                firePoolEvent(PoolEvent.CHECKIN);
                log.debug(" 缓存项【" + item.getKey() + "】已经被回收（Check in）！");
            } 
            catch (Exception e) {        
            	// 如果不能回收则销毁
                destroyObject(item); 
                log.error("无法回收缓存项，已销毁！",  e);
            }
        }
    }   
    
    public Cacheable remove() {
        Cacheable item = getFree().getByAccessMethod(strategy.getAccessMethod());
        
        //如果free池中取不到，则要等using池中的缓存对象返回到free中。线程等待
        long timeout = strategy.getInterruptTime();
        long time = System.currentTimeMillis();
        synchronized(this) {
            while (item == null  &&  (System.currentTimeMillis() - time < timeout)) {
                try {
                    log.debug("池【" + this.getName() + "】Free容器中没有可用的项......等待【" + timeout + "】ms");
                    wait(timeout);
                    item = checkOut();
                } 
                catch (InterruptedException e) { 
                    log.error("等待移除缓存项时线程被中断，移除失败。", e); 
                }
            }
        }
        
        if( item != null ) {
        	removeObject(item.getKey());        
        }
        
        return item;
    }
    
    public boolean purge() {
        log.debug("开始清除 【" + this.getName() + "】 池中过期的缓存项 ....... ");
       
        int count = 0;
        for (Cacheable item : getFree().valueSet()) {
            if ( item != null && item.isExpired() ) {
                removeObject(item.getKey());
                destroyObject(item);
                count++;
            }
        }
        
        if(count > 0) {
        	log.debug("共清除了【" + this.getName() + "】 池中【" + count + "】个缓存对象。");
        }
        log.debug("清除结束");
        
        return getFree().size() > 0  ||  count > 0;
    }
    
    public long getRequests() {
        return requests;
    }
    
    /**
     * 池请求数加一
     */
    protected void addRequests() {  
    	requests++; 
    }

    /**
     * 池命中数加一
     */
    protected void addHits() { 
    	hits++; 
    }
    
    /**
     * 重置请求数和点击数
     * requests = hits = 0
     */
    protected final void resetHitCounter() { 
    	requests = hits = 0; 
    }  
    
    public final float getHitRate() {
        return (requests == 0) ? 0 : (((float) hits / requests) * 100f);
    }
    
    public Set<Cacheable> listItems() {
        Set<Cacheable> values = new HashSet<Cacheable>();
        if(getFree() != null) {
            values.addAll(getFree().valueSet());
        }
        if(getUsing() != null) {
            values.addAll(getUsing().valueSet());
        }
        
        return values;
    }
    
    public Set<Object> listKeys() {
        Set<Object> keys = new HashSet<Object>();
        if(getFree() != null) {
            keys.addAll(getFree().keySet());
        }
        if(getUsing() != null) {
            keys.addAll(getUsing().keySet());
        }
        
        return keys;
    }
    
    public CacheStrategy getCacheStrategy() { 
    	return this.strategy; 
    }
    
    /**
     * 判断是否是修改缓存策略还是在初始化缓存池，初始化的话strategy为null
     * @param strategy
     */
    public void setCacheStrategy(CacheStrategy strategy) { 
        if( this.strategy != null ) {
        	// 缓存策略改变则触发事件
            this.strategy.fireEventIfChanged(strategy); 
        }
        this.strategy = strategy; 
    }
    
    public void setCustomizer(CacheCustomizer customizer) { 
    	this.customizer = customizer; 
    }

    public CacheCustomizer getCustomizer(){ 
    	return this.customizer; 
    }

	/**
	 * 设置是异步还是同步销毁对象。 
	 * 如果设置为true，那么之后每次销毁对象（过期的对象，或者是最终释放整个池）的时候，允许方法立即返回。
	 * 当销毁对象很耗费时间的情况下，这种方式将会非常有用。
	 */
    public final void setAsyncDestroy(boolean b) { 
    	asyncDestroy = b; 
    }
    
    public final void firePoolEvent(int eventType) {
        if (listeners.isEmpty()) return;
        
        PoolEvent poolEvent = new PoolEvent(this, eventType);
        for ( Listener listener : listeners){
        	listener.dealwithPoolEvent(poolEvent);
        }
    }
    
    public final void addObjectPoolListener(Listener x){
        listeners.add(x);
    }

    public final void removeObjectPoolListener(Listener x){
        listeners.remove(x);
    }
}

