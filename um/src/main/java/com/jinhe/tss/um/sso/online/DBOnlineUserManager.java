package com.jinhe.tss.um.sso.online;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinhe.tss.framework.persistence.ICommonDao;
import com.jinhe.tss.framework.sso.online.IOnlineUserManager;
import com.jinhe.tss.framework.sso.online.OnlineUser;

/**
 * <p> 在线用户库（数据库） </p>
 * 
 */
@Service("DBOnlineUserManagerService")
public class DBOnlineUserManager implements IOnlineUserManager {
	
	@Autowired private ICommonDao dao;
 
    /*
     * 根据 SessionId，应用Code 找到用户并将用户的sessionId置为Null，表示已经注销。
     * 不删除用户记录，方便以后查询用户的登录历史。
     */
    public String logout(String appCode, String sessionId) {
    	String hql = " from DBOnlineUser o where o.appCode = ? and o.sessionId = ?  ";
        List<?> entityList = dao.getEntities(hql, new Object[] {appCode, sessionId});
        
        String token = null;
    	for(Object entity : entityList) {
    		DBOnlineUser ou = (DBOnlineUser) dao.delete(entity);
        	token = ou.getToken();
    	}
    	
    	// 将三天前的在线信息删除（应该都是漏删除的了）
    	long nowLong = new Date().getTime(); 
        Date time = new Date(nowLong - (long) (72 * 60 * 60 * 1000)); 
    	dao.deleteAll(dao.getEntities(" from DBOnlineUser o where o.loginTime < ?", time));
    	
		return token;
    }

    public boolean isOnline(String token) {
        String hql = " from DBOnlineUser o where o.token = ? ";
        List<?> list = dao.getEntities(hql, new Object[] {token});
		return !(list == null || list.isEmpty());
    }

    public Set<OnlineUser> getOnlineUsersByToken(String token) {
    	String hql = " from DBOnlineUser o where o.token = ? ";
    	
    	List<?> list = dao.getEntities(hql, new Object[] {token});
    	
    	Set<OnlineUser> onlineUsers = new HashSet<OnlineUser>();
    	for(Object entity : list) {
    		onlineUsers.add((OnlineUser) entity);
    	}
    	
        return onlineUsers;
    }

    @SuppressWarnings("unchecked")
	public Collection<String> getOnlineUserNames() {
    	String hql = "select distinct o.userName from DBOnlineUser o";
        return (Collection<String>) dao.getEntities(hql);
    }

    /*     
     * 如果在线用户库中没有相同的用户存在， 则在在线用户库中添加此记录
     */
    public void register(String token, String appCode, String sessionId, Long userId, String userName) {
    	DBOnlineUser entity = new DBOnlineUser(userId, sessionId, appCode, token, userName);
    	dao.create(entity);       
    }

}
