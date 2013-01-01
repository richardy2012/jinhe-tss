package com.jinhe.tss.um.dao;

import com.jinhe.tss.framework.persistence.ITreeSupportDao;
import com.jinhe.tss.um.entity.GroupUser;

/**
 * 新增一个GroupUser，或删除一个GroupUser的dao操作
 */
public interface IGroupUserDao extends ITreeSupportDao<GroupUser> {

	/**
     * 新增一个GroupUser，
     * 同时进行相应的组、用户、角色之间关系的设置(即与用户资源相关的授权信息)。
	 * @param obj
	 * @return
	 */
	public GroupUser saveGroupUser(GroupUser obj);

	/**
     * 删除一个GroupUser，（***移动用户***）的时候会调用，删除用户并不会调用。
     * 同时删除相应的组、用户、角色之间关系的设置（即用户在补齐表中所有授权信息）。
	 * @param obj
	 */
	public void deleteGroupUser(GroupUser obj);
}

	