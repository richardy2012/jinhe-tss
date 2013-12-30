package com.jinhe.tss.framework.sso.identity;

import com.jinhe.tss.framework.sso.online.CacheOnlineUserManager;
import com.jinhe.tss.framework.sso.online.OnlineUserManagerFactory;

public class MockOnlineUserManagerFactory extends OnlineUserManagerFactory {

    public static void init() {
        manager = new CacheOnlineUserManager();
    }
}
