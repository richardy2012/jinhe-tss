package com.jinhe.tss.framework;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTransactionManager;

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

	private static String defaultContextPath = "spring/applicationContext.xml";

	private static void initContext(String contextPath) {
		_ctx = new ClassPathXmlApplicationContext(contextPath);
	}
 
	public static synchronized ApplicationContext getContext() {
		if (_ctx == null) {
			String contextPath = Config.getAttribute(Config.SPRING_CONTEXT_PATH);
			if (contextPath != null) {
				defaultContextPath = contextPath;
			}
			initContext(defaultContextPath);
		}
		return _ctx;
	}

	public static HibernateTransactionManager getTransactionManager() {
		return (HibernateTransactionManager) getContext().getBean("transactionManager");
	}

	public static Object getBaseTxProxy() {
		return getContext().getBean("baseTxProxy");
	}

	public static Object getOnlineUserService() {
		return getContext().getBean("OnlineUserService");
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
