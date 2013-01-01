/* ==================================================================   
 * Created [2009-4-27 下午11:32:55] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2009-2012 
 * ================================================================== 
 */

package com.jinhe.tss.framework.sso.online.database;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.TxTestSupport;
import com.jinhe.tss.framework.sso.online.IOnlineUserManager;
import com.jinhe.tss.framework.sso.online.OnlineUser;

public class DBOnlineUserManagerTest extends TxTestSupport { 
    
    @Autowired IOnlineUserManager manager;
    
    public final void testDBOnlineUserManager() {
        Collection<String> onlineUserNames = manager.getOnlineUserNames();
        for(String name : onlineUserNames) {
            log.debug(name);
        }
        
        manager.register("token", "TSS",  "sessionId",  new Long(1), "Jon.King");
        manager.register("token", "TSS1", "sessionId1", new Long(1), "Jon.King");
        manager.register("token", "TSS2", "sessionId2", new Long(2), "Jon.King2");
        manager.register("token3", "TSS", "sessionId3", new Long(3), "Jon.King3");

        Set<OnlineUser> userSet = manager.getOnlineUsersByToken("token");
        assertNotNull(userSet);
        assertEquals(3, userSet.size());
        
        onlineUserNames = manager.getOnlineUserNames();
        for(String name : onlineUserNames) {
            log.debug(name);
        }
        
        // testGetAllOnlineInfos4Token
        Set<OnlineUser> userInfos = manager.getOnlineUsersByToken("token");
        assertEquals(3, userInfos.size());
        assertTrue(userInfos.contains(new OnlineUser(new Long(1), "TSS", "sessionId", "token")));
        assertTrue(userInfos.contains(new OnlineUser(new Long(2), "TSS2", "sessionId2", "token")));
        assertTrue(userInfos.contains(new OnlineUser(new Long(1), "TSS1", "sessionId1", "token")));
        
        assertTrue(manager.getOnlineUsersByToken("notToken").isEmpty());
        
        // testIsOnline
        assertTrue(manager.isOnline("token"));
        assertFalse(manager.isOnline("NotLoginToken"));
        
        // testDelete
        assertEquals(3, manager.getOnlineUsersByToken("token").size());

        manager.logout("TSS1", "sessionId1");
        assertEquals(2, manager.getOnlineUsersByToken("token").size());
        
        manager.logout("TSS", "sessionId3");
        assertEquals(2, manager.getOnlineUsersByToken("token").size());
    }

}
