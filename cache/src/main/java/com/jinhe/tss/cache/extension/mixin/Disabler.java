/* ==================================================================   
 * Created [2006-8-13] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2012-2015 
 * ================================================================== 
*/

package com.jinhe.tss.cache.extension.mixin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.jinhe.tss.cache.CacheStrategy;
import com.jinhe.tss.cache.Cacheable;
import com.jinhe.tss.cache.Pool;
import com.jinhe.tss.cache.TimeWrapper;
import com.jinhe.tss.util.proxy.ProxyUtil;

/**
 * <p>
 * Disabler.java
 * </p><p>
 * 用动态代理来控制缓存池的缓存策略停用/启用两种状态下存取数据操作。
 * </p><p>
 * 加入了一个mixin功能，扩展了缓存池的动态代理类的功能，</p><p>
 * 其可以直接停用/启用，而不要调用Cachestrategy.setDisabled()。
 * </p><p>
 * 通过调用本动态代理类，用户可以不去设置IPool接口的各个实现类</p><p>
 * 的getObject，putObject，check-out,check-in操作相应的关于停用/启用的处理。</p>
 */
public class Disabler {
    
    public static Pool disableWrapper(final Pool pool) {

        // IPool的实现类都有一个抽象的父类
        Class<?>[] interfaces = ProxyUtil.getInterfaces(pool.getClass()); 
        
        Class<?>[] temp = new Class[interfaces.length + 1];
        for(int i = 0; i < interfaces.length; i++){
            temp[i] = interfaces[i];
        }
        temp[temp.length - 1] = IDisable.class;  // 将mixin的IDisable的接口也加进来
        interfaces = temp;

        ClassLoader classLoader = pool.getClass().getClassLoader();
		return (Pool) Proxy.newProxyInstance(classLoader, interfaces, new InvocationHandler() {
            
            Object target = pool;

            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                /* 如果调用方法是属于IDisable的，则将目标对象切换new DisableMixin()；*/
                if(method.getDeclaringClass().equals(IDisable.class)){
                    target = new DisableMixin(pool);
                }
                
                /* 
                 * 如果缓存策略已经关闭: 
                 * 取数据时则不从池中获取而重新加载数据；
                 * 存数据则不将数据放到缓存池中，直接返回null 
                 */
                if(CacheStrategy.TRUE.equals(pool.getCacheStrategy().disabled)){
                    //不进行初始化
                    if (method.getName().equals("init")) 
                        return null;
                    
                    //如果是调用getObject方法则调用载入器重新载入。参数args[0]为key值
                    if (method.getName().equals("getObject"))          
                        return pool.reload(new TimeWrapper(args[0], null)); 
                    
                    if (method.getName().equals("putObject"))
                        return null; 
                    
                    //如果是调用checkOut 方法则调用池算法器创建一个
                    if (method.getName().equals("checkOut"))          
                        return pool.getCustomizer().create(); 

                    if (method.getName().equals("checkIn")){
                        pool.getCustomizer().destroy((Cacheable) args[0]);
                        return null; 
                    }
                }
                
                try {
                    return method.invoke(target, args);
                } catch (InvocationTargetException e) {
                    throw e.getTargetException();
                }
            }
        });
    }
}
