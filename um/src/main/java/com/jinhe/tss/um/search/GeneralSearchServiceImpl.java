package com.jinhe.tss.um.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinhe.tss.framework.persistence.ICommonDao;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.entity.Role;
import com.jinhe.tss.um.entity.SubAuthorize;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.helper.dto.SubAuthorizedUserRoleDTO;
import com.jinhe.tss.um.helper.dto.UserRoleDTO;
import com.jinhe.tss.util.EasyUtils;
 
@Service("GeneralSearchService")
public class GeneralSearchServiceImpl implements GeneralSearchService {

	@Autowired private ICommonDao commonDao;
 
	public List<?> getResourceTypeListByApp(String applicationId) {
        return commonDao.getEntities("from ResourceType o where o.applicationId = ? order by o.seqNo", applicationId);
	}
 
	public List<?> searchOtherUserMappingInfo(Long groupId) {
		List<?> users = searchUsersByGroup(groupId);
		for ( Object temp : users ) {
			User user = (User) temp;
			Long mappedMainUserId = user.getAppUserId();
			if (mappedMainUserId == null) continue;
			
			User mapUser = (User) commonDao.getEntity(User.class, mappedMainUserId);
			if (mapUser != null) {
				user.setAppUserName(mapUser.getLoginName());
			}
			
			String hql = "select g from GroupUser gu, Group g where gu.groupId = g.id and gu.userId = ? and g.groupType = ?";
			List<?> groups = commonDao.getEntities(hql, mappedMainUserId, Group.MAIN_GROUP_TYPE);
			if ( groups.size() > 0 ) {
				Group group = (Group) groups.get(0);
				user.setAppUserGroupName(group.getName());
			}
		}
		return users;
	}
 
	// 一个组下面所有用户的因转授而获得的角色的情况
	public List<SubAuthorizedUserRoleDTO> searchUserSubauthByGroupId(Long groupId){
	    // 先取出组下所有可见的用户
		queryUsersInsertTemp(groupId);
		
		// 再取出这些用户所拥有的转授得到的角色以及相应的转授策略
		
		List<SubAuthorizedUserRoleDTO> result = new ArrayList<SubAuthorizedUserRoleDTO>();
		
		String hsql = "select u, r, s, creator from User u, RoleUser ru, Role r, Strategy s, User creator, Temp t " +
					" where u.id=ru.userId and ru.roleId = r.id and ru.strategyId = s.id and s.creatorId = creator.id and u.id = t.id";
        List<?> list = commonDao.getEntities(hsql);
		for( Object temp : list ){
			Object[] objs = (Object[]) temp;
			User user = (User) objs[0]; 
			Role role = (Role) objs[1];
			SubAuthorize strategy = (SubAuthorize) objs[2];
			User creator = (User) objs[3]; 
			 
			SubAuthorizedUserRoleDTO dto = new SubAuthorizedUserRoleDTO();
			dto.setSubAuthorized2UserId(user.getId());
			dto.setSubAuthorized2UserName(user.getUserName());
			dto.setRoleId(role.getId());
			dto.setRoleName(role.getName());
			dto.setStrategyId(strategy.getId());
			dto.setStrategyName(strategy.getName());
 
			dto.setSubAuthorizedUserId(creator.getId());
			dto.setSubAuthorizedUserName(creator.getUserName());
 
			result.add(dto);
		}
		
		hsql = "select u, r, s, creator, g from User u, GroupUser gu, Group g, RoleGroup rg, Role r, Strategy s, User creator, Temp t " +
		    " where u.id = gu.userId and gu.groupId=g.id and g.id=rg.groupId and rg.roleId = r.id  " +
		    "   and rg.strategyId = s.id and s.creatorId = creator.id and u.id = t.id ";
        list = commonDao.getEntities(hsql);
        for( Object temp : list ){
			Object[] objs = (Object[]) temp;
			User user = (User) objs[0]; 
			Role role = (Role) objs[1];
			SubAuthorize strategy = (SubAuthorize) objs[2];
			User creator = (User) objs[3]; 
			Group group = (Group) objs[4];
			 
			SubAuthorizedUserRoleDTO dto = new SubAuthorizedUserRoleDTO();
			dto.setSubAuthorized2UserId(user.getId());
			dto.setSubAuthorized2UserName(user.getUserName());
			dto.setRoleId(role.getId());
			dto.setRoleName(role.getName());
			dto.setStrategyId(strategy.getId());
			dto.setStrategyName(strategy.getName());
			dto.setSubAuthorized2GroupId(group.getId());
			dto.setSubAuthorized2GroupName(group.getName());
			dto.setSubAuthorizedUserId(creator.getId());
			dto.setSubAuthorizedUserName(creator.getUserName());
 
			result.add(dto);
		}
		
		return result;
	}
	
    public List<UserRoleDTO> searchUserRolesMapping(Long groupId) {
		queryUsersInsertTemp(groupId);
	    
        // 再查出这些用户拥有的角色情况
        String hsql = "select u, r from User u, Role r, ViewRoleUser ru, Temp t " 
        	+ " where u.id = ru.id.userId and ru.id.roleId = r.id and u.id = t.id";
        List<?> list = commonDao.getEntities(hsql);
        
        List<UserRoleDTO> returnList = new ArrayList<UserRoleDTO>();
        for ( Object temp : list ) {
            Object[] objs = (Object[]) temp;
            UserRoleDTO relation = new UserRoleDTO((User) objs[0], (Role) objs[1]);
            if ( !returnList.contains(relation) ) {
                returnList.add(relation);
            }
        }
        return returnList;
    }

	/**
	 *  查出组下用户ID列表并插入临时表
	 */
	private void queryUsersInsertTemp(Long groupId) {
        String hsql = "select distinct u.id from GroupUser gu, User u where gu.userId = u.id and gu.groupId = ? ";
        List<?> list = commonDao.getEntities(hsql, groupId);
        commonDao.insertIds2TempTable(list);
	}

	/** 
	 * 查询角色授予的用户列表（包括直接授予用户的和授予组时组下的所有用户）
	 */
	public List<User> searchUsersByRole(Long roleId){
		String sql = "select u.id, u.userName from um_User u, um_RoleUser ru where u.id=ru.userId and ru.roleId = ? " + 
				    " union" + 
				    " select u.id, u.userName from um_User u, um_GroupUser gu, um_RoleGroup rg where u.id = gu.userId and gu.groupId=rg.groupId and rg.roleId = ?";
		
		List<?> list = commonDao.getEntitiesByNativeSql(sql, roleId, roleId);
		List<User> result = new ArrayList<User>();
		for( Object temp :list ){
			Object[] objs = (Object[]) temp;
			User user = new User();
			user.setId( EasyUtils.convertObject2Long(objs[0]) );
			user.setUserName((String)objs[1]);
			result.add(user);
		}
		return result;
	}
	
	public List<?> searchUsersByGroup(Long groupId){
		String hql = "select distinct u from GroupUser gu, User u where gu.userId = u.id and gu.groupId = ?";
		return commonDao.getEntities(hql, groupId);
	}

}