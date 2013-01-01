/* ==================================================================   
 * Created [2007-1-9] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2012-2015 
 * ================================================================== 
 */
package com.jinhe.tss.cache;

import com.jinhe.tss.util.BeanUtil;

/**
 * 池容器工厂类。
 * 
 */
public class ContainerFactory {
	
	private static ContainerFactory factory;

	private ContainerFactory() {
	}

	public static ContainerFactory getInstance() {
		if (factory == null) {
			factory = new ContainerFactory();
		}
		return factory;
	}

	public Container create(String className, String containerName) {
		// 调用构造方法：XXContainer(String name)
		Object instance = BeanUtil.newInstanceByName(className,
				new Class[] { String.class }, new Object[] { containerName });

		return (Container) instance;
	}
}
