package com.jinhe.tss.framework;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jinhe.tss.framework.sso.online.IOnlineUserManager;

/**
 * <p>
 * 加载spring配置文件，以调用配置文件中配置的对象。
 * </p>
 * 
 * @author Jon.King 2006-6-19
 */
public class Global {
	
	private static ApplicationContext _ctx;

	private static String defaultContextPath = "META-INF/spring.xml";
 
	public static synchronized ApplicationContext getContext() {
		if (_ctx == null) {
			String contextPath = Config.getAttribute(Config.SPRING_CONTEXT_PATH);
			if (contextPath != null) {
				defaultContextPath = contextPath;
			}
			_ctx = new ClassPathXmlApplicationContext(defaultContextPath);
		}
		return _ctx;
	}
 
	public static IOnlineUserManager getRemoteOnlineUserManager() {
		return (IOnlineUserManager) getContext().getBean("RemoteOnlineUserManager");
	}

	public static synchronized void setContext(ApplicationContext context) {
		_ctx = context;
	}

	public static synchronized void destroyContext() {
		_ctx = null;
	}
}
