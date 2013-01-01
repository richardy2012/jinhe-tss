package com.jinhe.tss.framework.sso.identity;

import com.jinhe.tss.framework.sso.online.OnlineUserManagerFactory;
import com.jinhe.tss.framework.sso.online.cache.CacheOnlineUserManager;

public class MockOnlineUserManagerFactory extends OnlineUserManagerFactory {

    public static void init() {
        manager = new CacheOnlineUserManager();
    }
}
