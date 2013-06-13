package com.jinhe.tss.framework.web;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.jinhe.tss.framework.Global;

/**
 * <p>
 * 支持Global对象的Spring Context对象，主要实现非Spring Context中对象能直接访问Spring Context中对象。
 * 使用时需要在web.xml中加入一个配置项，如下：
 *  <context-param>
 *     <param-name>contextClass</param-name>
 *     <param-value>
 *           com.jinhe.tss.framework.web.GlobalSuportXmlWebApplicationContext
 *     </param-value>
 *  </context-param>
 *  
 *  如此web.xml中配置的org.springframework.web.context.ContextLoaderListener就会用上面自定义的contextClass来加载
 *  applicationContext.xml文件，而加载完后直接将context对象本身设置到Global中，如此调用Global读取对象的
 *  时候就不用再加载一遍applicationContext.xml了。
 *  
 * </p>
 *
 */
public class GlobalSuportXmlWebApplicationContext extends XmlWebApplicationContext {

    /**
	 * <p>
	 * 覆盖原先的BeanFactory创建方法，在创建BeanFactory后，初始化Global
	 * </p>
	 * @return
	 * @see org.springframework.context.support.AbstractRefreshableApplicationContext#createBeanFactory()
	 */
	protected DefaultListableBeanFactory createBeanFactory() {
		Global.setContext(this);
		return super.createBeanFactory();
	}

	/**
	 * @see org.springframework.context.support.AbstractApplicationContext#close()
	 */
	public void close() {
		Global.destroyContext();
		super.close();
	}

}
