/* ==================================================================   
 * Created [2009-4-27 下午11:32:55] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2009-2012 
 * ================================================================== 
 */

package com.jinhe.tss.framework.sso.online.remote;

import java.util.Collection;
import java.util.Set;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.sso.online.IOnlineUserManager;
import com.jinhe.tss.framework.sso.online.OnlineUser;

/**
 * <p> 远程在线用户库 </p>
 *
 * 用于TSS以外的基于TSS框架的其它应用的配置<br/>
 * 
 * OnlineUserManagerFactory先读取application.properties里的class.name.OnlineManager配置，<br/>
 * 如果是RemoteOnlineUserManager，则初始化一个RemoteOnlineUserManager实例，并通过该实例来调用<br/>
 * applicationContext.xml里配置的远程在线用户库。<br/>
 * 
 * TODO 远程在线用户管理实现中解藕远程Service定义名称(Global.getContext().getBean("RemoteOnlineUserManager"))<br/>
 *
 */
public class RemoteOnlineUserManager implements IOnlineUserManager {
 
    public String logout(String appCode, String sessionId) {
        return Global.getRemoteOnlineUserManager().logout(appCode, sessionId);
    }
 
    public boolean isOnline(String token) {
        return Global.getRemoteOnlineUserManager().isOnline(token);
    }
 
    public void register(String token, String appCode, String sessionId, Long userId, String userName) {
        Global.getRemoteOnlineUserManager().register(token, appCode, sessionId, userId, userName);
    }
 
    public Set<OnlineUser> getOnlineUsersByToken(String token) {
        return Global.getRemoteOnlineUserManager().getOnlineUsersByToken(token);
    }

    public Collection<String> getOnlineUserNames() {
        return Global.getRemoteOnlineUserManager().getOnlineUserNames();
    }
}
