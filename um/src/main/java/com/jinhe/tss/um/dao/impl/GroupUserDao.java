package com.jinhe.tss.um.dao.impl;

import org.springframework.stereotype.Repository;

import com.jinhe.tss.framework.persistence.BaseDao;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.dao.IGroupUserDao;
import com.jinhe.tss.um.entity.GroupUser;
import com.jinhe.tss.um.entity.User;
 
@Repository("GroupUserDao")
public class GroupUserDao extends BaseDao<GroupUser> implements IGroupUserDao {

    public GroupUserDao() {
		super(GroupUser.class);
	}

	public void deleteGroupUser(GroupUser gr) {
        delete(gr);
    }
    
    public GroupUser saveGroupUser(GroupUser gr) {
        User user = (User) getEntity(User.class, gr.getUserId());
        if( user == null || !UMConstants.TSS_APPLICATION_ID.equals(user.getApplicationId()) ) {
            return gr; // 非TSS用户不维护GroupUser关系 ？
        }
 
        return (GroupUser) create(gr);
    }
}
