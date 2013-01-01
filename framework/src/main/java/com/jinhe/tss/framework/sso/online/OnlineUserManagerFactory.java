package com.jinhe.tss.framework.sso.online;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.sso.SSOConstants;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.sso.online.cache.CacheOnlineUserManager;
import com.jinhe.tss.util.BeanUtil;

/** 
 * <p>
 * 在线用户管理对象工厂类
 * </p>
 */
public class OnlineUserManagerFactory {
	
    protected static IOnlineUserManager manager = null;

    private static final Log log = LogFactory.getLog(OnlineUserManagerFactory.class);

    public static IOnlineUserManager getManager() {
        if (manager == null) {
            String className = Config.getAttribute(SSOConstants.ONLINE_MANAGER);
            if (className == null) {
				manager = new CacheOnlineUserManager(); // 默认使用Cache在线用户库
			} 
            else {
				manager = (IOnlineUserManager) BeanUtil.newInstanceByName(className);
			}
            
            log.info("应用【" + Context.getApplicationContext().getCurrentAppCode() + "】里在线用户库【" + manager.getClass().getName() + "】初始化成功！");
        }
        
        return manager;
    }
}
