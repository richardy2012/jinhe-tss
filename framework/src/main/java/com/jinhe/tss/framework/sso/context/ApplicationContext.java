package com.jinhe.tss.framework.sso.context;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.sso.appserver.AppServer;
import com.jinhe.tss.framework.sso.appserver.AppServerStorerFactory;
import com.jinhe.tss.framework.sso.appserver.IAppServerStorer;

/**
 * <p>
 * 应用系统上下文信息对象
 * </p>
 */
public class ApplicationContext {
	
    protected IAppServerStorer storer;

    /**
     * 默认构造函数
     */
    public ApplicationContext() {
        storer = AppServerStorerFactory.newInstance().getAppServerStorer();
    }

    /**
     * <p>
     * 获取当前系统编号
     * </p>
     * @return
     */
    public String getCurrentAppCode() {
        return Config.getAttribute(Config.APPLICATION_CODE);
    }

    /**
     * <p>
     * 获取当前系统对应用户库编号
     * </p>
     * @return
     */
    public String getUserDepositoryCode() {
        return getUserDepositoryCodeByAppCode(getCurrentAppCode());
    }

    /**
     * <p>
     * 根据应用系统编号获取对应用户库编号
     * </p>
     * @param appCode
     * @return
     */
    public String getUserDepositoryCodeByAppCode(String appCode) {
        return getAppServer(appCode).getUserDepositoryCode();
    }

    /**
     * <p>
     * 根据应用系统编号获取应用系统配置信息
     * </p>
     * @param appCode
     * @return
     */
    public AppServer getAppServer(String appCode) {
        return storer.getAppServer(appCode);
    }

    /**
     * <p>
     * 获取当前系统访问配置信息
     * </p>
     * @return
     */
    public AppServer getCurrentAppServer() {
        return getAppServer(getCurrentAppCode());
    }
}
