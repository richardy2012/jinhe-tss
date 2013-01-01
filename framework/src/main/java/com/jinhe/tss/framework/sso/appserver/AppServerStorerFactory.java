package com.jinhe.tss.framework.sso.appserver;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.sso.SSOConstants;
import com.jinhe.tss.util.BeanUtil;

/** 
 * <p>
 * 应用服务器存储器对象工厂类
 * </p>
 */
public class AppServerStorerFactory {

    private static AppServerStorerFactory factory;

    private static IAppServerStorer appServerStorer;

    private AppServerStorerFactory() {}

    /**
     * <p>
     * 获取应用服务器存储器对象
     * </p>
     * @return
     */
    public IAppServerStorer getAppServerStorer() {
        if (appServerStorer == null) {
            String className = Config.getAttribute(SSOConstants.APPSERVER_STORER);
            if (className != null) {
                appServerStorer = (IAppServerStorer) BeanUtil.newInstanceByName(className);
            }
            
            if(appServerStorer == null) {
                appServerStorer = new FileAppServerStorer();
            }
        }
        return appServerStorer;
    }

    /**
     * <p>
     * 工厂类实例化
     * </p>
     * @return
     */
    public static AppServerStorerFactory newInstance() {
        if (factory == null) {
            factory = new AppServerStorerFactory();
        }
        return factory;
    }
}
