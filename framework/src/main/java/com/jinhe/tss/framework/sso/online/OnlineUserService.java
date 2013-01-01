package com.jinhe.tss.framework.sso.online;

import java.util.Collection;
import java.util.Set;

/**
 * <p> 远程在线用户管理服务 </p>
 * <p>
 * <br/>将本地配置的在线用户管理服务转换为远程服务。
 * 
 * <br/>注：平台框架里本类将做为在线用户库service发布在TSS应用里， 
 * <br/>  &lt bean id="online.OnlineUserService" class="com.jinhe.tss.core.sso.online.OnlineUserService"/&gt
 * <br/>它将读取TSS的application.properties里：
 * <br/>  class.name.OnlineManager = com.jinhe.tss.core.sso.online.cache.CacheOnlineUserManager
 * <br/>配置项来初始化并操作在线用户库（上面指定的是"缓存式在线用户库"）.
 * <br/> 
 * <br/>本来可以直接将online.OnlineUserService配置为CacheOnlineUserManager，但通过RemoteOnlineUserManagerService来配置
 * <bt/>则使得在线用户库的配置更加灵活，当需要将CacheOnlineUserManager换成DBOnlineUserManager时，
 * <br/>只需要改动application.properties里的class.name.OnlineManager配置。
 * 
 * </p>
 */
public class OnlineUserService implements IOnlineUserManager {

    public String logout(String appCode, String sessionId) {
        return OnlineUserManagerFactory.getManager().logout(appCode, sessionId);
    }

    public boolean isOnline(String token) {
        return OnlineUserManagerFactory.getManager().isOnline(token);
    }

    public void register(String token, String appCode, String sessionId, Long userId, String userName) {
        OnlineUserManagerFactory.getManager().register(token, appCode, sessionId, userId, userName);
    }

    public Set<OnlineUser> getOnlineUsersByToken(String token) {
        return OnlineUserManagerFactory.getManager().getOnlineUsersByToken(token);
    }

    public Collection<String> getOnlineUserNames() {
        return OnlineUserManagerFactory.getManager().getOnlineUserNames();
    }

}
