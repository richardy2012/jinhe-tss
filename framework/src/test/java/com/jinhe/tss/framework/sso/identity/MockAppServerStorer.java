package com.jinhe.tss.framework.sso.identity;

import java.util.HashMap;
import java.util.Map;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.appserver.AppServer;
import com.jinhe.tss.framework.sso.appserver.IAppServerStorer;

/**
 * 应用服务器管理对象Mock
 */
public class MockAppServerStorer implements IAppServerStorer {

    private Map<String, AppServer> servers;
 
    public MockAppServerStorer() {
        super();
        servers = new HashMap<String, AppServer>();
    }
 
    public MockAppServerStorer(Map<String, AppServer> servers) {
        super();
        this.servers = servers;
    }

    /**
     * 清楚所有应用服务器配置
     */
    public void clear() {
        servers.clear();
    }

    /**
     * 添加应用服务器配置
     */
    public void putAppServer(String code, AppServer server) {
        servers.put(code, server);
    }
 
    public AppServer getAppServer(String code) {
        AppServer appServer = (AppServer) servers.get(code);
        if (appServer == null) {
            throw new BusinessException("系统中没有应用（" + code + "）的相关访问配置信息");
        }
        return appServer;
    }

}
