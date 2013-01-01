package com.jinhe.tss.um.dao.impl;

import java.util.Iterator;
import java.util.List;

import com.jinhe.tss.framework.persistence.BaseDao;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.dao.IUserDao;
import com.jinhe.tss.um.entity.GroupUser;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.util.EasyUtils;

public class UserDao extends BaseDao<User> implements IUserDao {

    public UserDao() {
		super(User.class);
	}
    
    public User initUser(User obj) {
		return create(obj);
	}
	
	public User removeUser(User user) {
		delete(user);

        Long deletedUserId = user.getId();
        deleteAll(findUser2GroupByUserId(deletedUserId)); // 用户对组
        deleteAll(findUser2RoleByUserId(deletedUserId));  // 用户对角色
        
        /* 如果删除的是主用户组用户，则需要其在其他用户组里的用户对应关系。 */
        List<?> users = getEntities("from User o where o.appUserId = " + deletedUserId);
		if(null != users && !users.isEmpty()){
            for(Iterator<?> it = users.iterator(); it.hasNext();){
                User temp = (User)it.next();
                temp.setAppUserId(null);
                update(temp);
            }
		}
		
		return user;
	}

	public User getUserByLoginName(String loginName) {
	    String hql = "from User o where o.loginName = ? and o.applicationId = ? ";
	    List<?> users = getEntities(hql, loginName, UMConstants.TSS_APPLICATION_ID);
        return users.size() > 0 ? (User) users.get(0) : null;
	}

	public User getUser(String applicationId, String loginName){
		String hql = "from User o where o.applicationId = ? and o.loginName = ? "; 
		List<?> users = getEntities(hql, applicationId, loginName);
	    return users.size() > 0 ? (User) users.get(0) : null;
	}

	public List<?> findUser2GroupByUserId(Long userId) {
		return getEntities("from GroupUser o where o.userId = ? ", userId);
	}

	public List<?> findUser2RoleByUserId(Long userId) {
		return getEntities("from RoleUser o where o.id.userId = ? and o.strategyId is null", userId);
	}

	public List<?> findRolesByUserId(Long userId) {
		String hql = "select distinct r from RoleUser ru, Role r where ru.id.roleId = r.id and ru.id.userId = ? and ru.strategyId is null ";
		return getEntities(hql, userId);
	}
 
	public GroupUser getGroup2User(Long groupId, Long userId) {
        List<?> list = getEntities("from GroupUser o where o.groupId = ? and o.userId = ?", groupId, userId);
		return !list.isEmpty() ? (GroupUser)list.get(0) : null;
	}
 
	public User getAppUser(Long appUserId, String userDepositoryCode) {
		List<?> users = getEntities("from User o where o.appUserId = ? and o.applicationId = ?", appUserId, userDepositoryCode);
		if(!EasyUtils.isNullOrEmpty(users)){
			return (User) users.get(0);
		}
		return null;
	}
}