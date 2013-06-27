package com.jinhe.tss.um.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jinhe.tss.framework.persistence.TreeSupportDao;
import com.jinhe.tss.framework.persistence.pagequery.PageInfo;
import com.jinhe.tss.framework.persistence.pagequery.PaginationQueryByHQL;
import com.jinhe.tss.framework.persistence.pagequery.PaginationQueryBySQL;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.dao.IGroupDao;
import com.jinhe.tss.um.dao.IResourceTypeDao;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.helper.UMQueryCondition;
import com.jinhe.tss.um.helper.dto.OtherUserDTO;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.util.EasyUtils;
 
@Repository("GroupDao")
public class GroupDao extends TreeSupportDao<Group> implements IGroupDao {

    public GroupDao() {
		super(Group.class);
	}

    @Autowired private IResourceTypeDao resourceTypeDao;	

    public Group saveGroup(Group group) {
    	if( group.getId() == null ) {
    		create(group);
    	} 
    	else {
    		update(group);
    	}
    	
		return group;
	}
    
    public Group moveGroup(Group group) {
        update(group);
		return group;
	}
    
	public Group removeGroup(Group group) {
        List<?> groups = getChildrenById(group.getId()); //列表中包含了Group本身
        List<?> users = getUsersByGroupIdDeeply(group.getId());
        
        for(Iterator<?> it = users.iterator(); it.hasNext();){
            User user = (User) it.next();
            deleteAll(getEntities("from GroupUser gu where gu.userId = ?", new Object[]{user.getId()}));
            deleteAll(getEntities("from RoleUser ru where ru.id.userId = ? and ru.strategyId is null", new Object[]{user.getId()}));
        }
        
        for(Iterator<?> it = groups.iterator(); it.hasNext();){
            Group temp = (Group) it.next();
            deleteAll(findGroup2UserByGroupId(temp.getId()));
            deleteAll(findGroup2RoleByGroupId(temp.getId()));
        }
        
        deleteAll(users);
        deleteAll(groups);
        return group;
	}
    
    public Group removeAssistmentGroup(Group group) {
        deleteAll(getChildrenById(group.getId()));
        return group;
    }

	public List<?> findRolesByGroupId(Long groupId) {
		String hql = "select distinct r from RoleGroup rg, Role r where rg.roleId = r.id and rg.groupId = ? and rg.strategyId is null ";
		return getEntities(hql, groupId);
	}

	public List<?> findGroup2UserByGroupId(Long groupId) {
        return getEntities("from GroupUser gu where gu.groupId = ?", groupId);
	}

	public List<?> findGroup2RoleByGroupId(Long groupId) {
        return getEntities("from RoleGroup rg where rg.groupId = ? and rg.strategyId is null ", groupId);
	}
 
	public Group findMainGroupByUserId(Long userId){
        //如此取出来的组唯一，即用户所在的主用户组
		String hql = "select g from GroupUser gu, Group g where gu.groupId = g.id and gu.userId = ? and g.groupType = ? ";
		List<?> list = getEntities(hql, userId, Group.MAIN_GROUP_TYPE);
        return list.size() > 0 ? (Group)list.get(0) : null;
	}
    
    public List<?> findGroupsByUserId(Long userId) {
        String hql = "select distinct g from GroupUser gu, Group g where gu.groupId = g.id and gu.userId = ? ";
        return getEntities(hql, userId);
    }
    
    public List<?> getFatherGroupsByUserId(Long userId){
        Group mainGroup = findMainGroupByUserId(userId);
        return getParentsById(mainGroup.getId(), UMConstants.MAIN_GROUP_ID);
    }
 
	public List<?> getVisibleSubGroups(Long groupId){
		Group group = getEntity(groupId);
        String suppliedTable = resourceTypeDao.getSuppliedTable(UMConstants.TSS_APPLICATION_ID, group.getResourceType());
        
        String hql = PermissionHelper.permissionHQL(entityName, suppliedTable, " and o.decode like ? ", true);
        return getEntities(hql, Environment.getOperatorId(), UMConstants.GROUP_VIEW_OPERRATION, group.getDecode() + "%" );
	}
 
	public List<?> getMainAndAssistantGroups(Long operatorId) {
		List<Group> groups = new ArrayList<Group>(); 
	    groups.addAll(getGroupsByType(operatorId, UMConstants.GROUP_VIEW_OPERRATION, Group.MAIN_GROUP_TYPE));
	    groups.addAll(getGroupsByType(operatorId, UMConstants.GROUP_VIEW_OPERRATION, Group.ASSISTANT_GROUP_TYPE));
		return groups;
	}
	
	public List<Group> getGroupsByType(Long operatorId, String operationId, Integer groupType) {
        String suppliedTable = resourceTypeDao.getSuppliedTable(UMConstants.TSS_APPLICATION_ID, UMConstants.GROUP_RESOURCE_TYPE_ID);
        
        String hql = PermissionHelper.permissionHQL(entityName, suppliedTable);
        List<?> allGroups = getEntities(hql, operatorId, operationId);
        
        List<Group> resultList = new ArrayList<Group>();
        for( Object temp : allGroups ){
            Group group = (Group) temp;
            if( groupType.equals(group.getGroupType()) ) {
            	resultList.add(group);
            }
        }
        return resultList;
	}
	
	public List<?> getParentGroupByGroupIds(List<Long> groupIds, Long operatorId, String operationId) {
        if( EasyUtils.isNullOrEmpty(groupIds)) return new ArrayList<Group>();
        
		String suppliedTable = resourceTypeDao.getSuppliedTable(UMConstants.TSS_APPLICATION_ID, UMConstants.GROUP_RESOURCE_TYPE_ID);
		
		String hql = "select distinct o " + PermissionHelper.formatHQLFrom(entityName, suppliedTable) + " , Group child " +
		        PermissionHelper.permissionConditionII() + " and child.id in (:groupIds) and child.decode like o.decode||'%'" + PermissionHelper.ORDER_BY;
		
		return getEntities(hql, new Object[]{"operatorId", "operationId", "groupIds"}, new Object[]{operatorId, operationId, groupIds});
	}

	public List<?> getVisibleMainUsers(Long operatorId) {
	    // 先查出有查看权限的用户组
	    List<Group> groups = new ArrayList<Group>(); 
	    groups.addAll(getGroupsByType(operatorId, UMConstants.GROUP_VIEW_OPERRATION, Group.MAIN_GROUP_TYPE));
	    groups.addAll(getGroupsByType(operatorId, UMConstants.GROUP_VIEW_OPERRATION, Group.ASSISTANT_GROUP_TYPE));
	    
	    List<Long> groupIds = new ArrayList<Long>();
	    for(Object temp : groups) {
	        groupIds.add(((Group) temp).getId());
	    }
	    
	    if(groupIds.isEmpty()) {
	        return new ArrayList<User>();
	    }
	    
        String hql = " select distinct u from User u, GroupUser gu where u.id = gu.userId  and gu.groupId in (:groupIds) ";
        return getEntities(hql, new Object[] {"groupIds"}, new Object[]{ groupIds });
	}

	public List<?> getUsersAndRelation(Long operatorId) {
        String hql = "select distinct u, gu from User u, GroupUser gu, Group g" +
                " where u.id = gu.userId  and gu.groupId = g.id and g.groupType = " + Group.MAIN_GROUP_TYPE;
        return getEntities(hql);
	}
 
	public List<User> getUsersByGroupIdDeeply(Long groupId){
		List<Group> sonGroups = this.getChildrenById(groupId);
		List<Long> sonGroupIds = new ArrayList<Long>();
		for(Group son : sonGroups) {
			sonGroupIds.add(son.getId());
		}
		
        return getUsersByGroupIds(sonGroupIds);
	}

	public List<User> getUsersByGroupIds(Collection<Long> groupIds){
        if( EasyUtils.isNullOrEmpty(groupIds) ) return new ArrayList<User>();
        
        String hql = "select distinct u, g.id as groupId, g.name as groupName " +
        		" from User u, GroupUser gu, Group g " + 
        		" where u.id = gu.userId and gu.groupId = g.id and g.id in (:groupIds) ";

        List<?> list = getEntities(hql, new Object[]{"groupIds"}, new Object[]{groupIds});
		return fillGroupInfo2User(list);
	}

	public List<User> getUsersByGroupId(Long groupId) {
		String hql = "select distinct u, g.id as groupId, g.name as groupName" +
				" from User u, GroupUser gu, Group g " +
                " where u.id = gu.userId and gu.groupId = g.id and g.id = ? ";
		return fillGroupInfo2User(getEntities(hql, groupId));
	}

    /**
     * 将用户所属的用户组ID，NAME设置到用户对象中
     * @param users
     * @return
     */
    private List<User> fillGroupInfo2User(List<?> users){
        List<User> result = new ArrayList<User>();
        if( EasyUtils.isNullOrEmpty(users) ) return result;
        
        for (Object temp : users) {
            Object[] objs = (Object[]) temp;
            User user = (User) objs[0];
            user.setGroupId( EasyUtils.convertObject2Long(objs[1]) );
            user.setGroupName((String) objs[2]);
            result.add(user);
        }    
        return result;
    }
    
    private List<OtherUserDTO> fillOtherGroupInfo(List<?> users){
    	List<OtherUserDTO> result = new ArrayList<OtherUserDTO>();
    	 if( EasyUtils.isNullOrEmpty(users) ) return result;
    	 
    	 for (Object temp : users) {
            Object[] objs = (Object[]) temp;
            
			OtherUserDTO otherUserDTO = new OtherUserDTO();
			otherUserDTO.setId( EasyUtils.convertObject2Long(objs[0]) );
			otherUserDTO.setUserName((String)objs[1]);
			otherUserDTO.setLoginName((String)objs[2]);
			otherUserDTO.setGroupId( EasyUtils.convertObject2Long(objs[3]));
			otherUserDTO.setGroupName((String)objs[4]);
			
			if( objs[5] != null ){
    			otherUserDTO.setAppUserId( EasyUtils.convertObject2Long(objs[5]));
    			otherUserDTO.setAppUserName((String)objs[6]);
    			otherUserDTO.setAppLoginName((String)objs[7]);
    			otherUserDTO.setAppGroupId( EasyUtils.convertObject2Long(objs[8]));
    			otherUserDTO.setAppGroupName((String)objs[9]);
			}
			
			result.add(otherUserDTO);
		}
    		
    	return result;
    }

	private List<Long> getChildrenGroupIds(Long groupId){
    	List<?> groups = getChildrenById(groupId);
    	List<Long> groupIds = new ArrayList<Long>();
        for( Object temp : groups ){
            Group group = (Group) temp;
            groupIds.add(group.getId());
        }
        return groupIds;
	}

    public boolean isOperatorInGroup(Long groupId, Long operatorId) {
        List<Long> groupIds = getChildrenGroupIds(groupId);
        if( EasyUtils.isNullOrEmpty(groupIds) ) return false;
        
        String hql = "select distinct gu.id from GroupUser gu where gu.groupId in (:groupIds) and gu.userId = :userId";
        List<?> list = getEntities(hql, new Object[]{"groupIds", "userId"}, new Object[]{groupIds, operatorId});
        return !list.isEmpty();
    }
    
    //******************************************* 按组或按查询条件查询用户 *******************************************

    public PageInfo getUsersByGroup(Long groupId, Integer pageNum, String...orderBy) {
        String hql = "select distinct u, g.id as groupId, g.name as groupName "
        		+ " from User u, GroupUser gu, Group g" 
        		+ " where u.id = gu.userId and gu.groupId = g.id and g.id = :groupId ";
        UMQueryCondition condition = new UMQueryCondition();
        condition.setGroupId(groupId);
        condition.getPage().setPageNum(pageNum);
        
        if(orderBy != null) {
        	condition.getOrderByFields().addAll( Arrays.asList(orderBy) );
        } 

        PaginationQueryByHQL pageQuery = new PaginationQueryByHQL(em, hql, condition);
        PageInfo page = pageQuery.getResultList();
        page.setItems( fillGroupInfo2User(page.getItems()) );
        return page;
    }
    
    public PageInfo getUsersByOtherGroupNoPermission(Long groupId, Integer pageNum, String...orderBy) {
    	String sql = "select distinct u.id, u.userName, u.loginName, g.id as groupId, g.name as groupName, "
        	+ " u1.id as appUserId, u1.userName as appUserName,u1.loginName as appLoginName, g1.id as appGroupId, g1.name as appGroupName "
    		+ " from um_user u,  um_groupuser gu,  um_group g, "
    		+ "      um_user u1, um_groupuser gu1, um_group g1"
    		+ " where u.id = gu.userId and gu.groupId = g.id and g.id = :groupId " 
    		+ " and u.appUserId = u1.id(+) "
    		+ " and u1.id = gu1.userId(+) and gu1.groupId = g1.id(+) ";
    	UMQueryCondition condition = new UMQueryCondition();
        condition.setGroupId(groupId);
        condition.getPage().setPageNum(pageNum);
        
        if(orderBy != null) {
        	condition.getOrderByFields().addAll( Arrays.asList(orderBy) );
        } 

        PaginationQueryBySQL pageQuery = new PaginationQueryBySQL(em, sql, condition);
        PageInfo page = pageQuery.getResultList();
        page.setItems(fillOtherGroupInfo(page.getItems()));
        return page;
    }
    
    public PageInfo searchOtherUser(UMQueryCondition condition, Integer pageNum){
        List<Long> groupIds = getChildrenGroupIds(condition.getGroupId());
        if( EasyUtils.isNullOrEmpty(groupIds) ) return null;
 
        String sql = "select distinct u.id, u.userName, u.loginName, g.id as groupId, g.name as groupName, "
        	+ " u1.id as appUserId, u1.userName as appUserName,u1.loginName as appLoginName, g1.id as appGroupId, g1.name as appGroupName "
    		+ " from um_user u,  um_groupuser gu,  um_group g, "
    		+ "      um_user u1, um_groupuser gu1, um_group g1"
    		+ " where u.id = gu.userId and gu.groupId = g.id and g.id = :groupId " 
    		+ " and u.appUserId = u1.id(+) "
    		+ " and u1.id = gu1.userId(+) and gu1.groupId = g1.id(+) "
        	+ " ${loginName} ${userName} ${employeeNo} ${sex} ${birthday} ${certificateNumber} ";
        
        condition.getPage().setPageNum(pageNum);
        condition.setGroupIds(groupIds);

        Set<String> set = condition.getIgnoreProperties();
        set.add("userId");
        set.add("groupType");
        set.add("groupId");
        
        PaginationQueryBySQL pageQuery = new PaginationQueryBySQL(em, sql, condition);
        PageInfo page = pageQuery.getResultList();
        page.setItems(fillOtherGroupInfo(page.getItems()));
        return page;
    }
    
    public PageInfo searchUser(UMQueryCondition condition, Integer pageNum) {
        List<Long> groupIds = getChildrenGroupIds(condition.getGroupId());
        if( EasyUtils.isNullOrEmpty(groupIds) ) return null;
        
        String hql = "select distinct u, g.id as groupId, g.name as groupName "
            + " from User u, GroupUser gu, Group g " 
            + " where u.id = gu.userId and gu.groupId = g.id and g.id in (:groupIds) " 
            + " ${loginName} ${userName} ${employeeNo} ${sex} ${birthday} ${certificateNumber} ";
        
        condition.getPage().setPageNum(pageNum);
        condition.setGroupIds(groupIds);
 
        Set<String> set = condition.getIgnoreProperties();
        set.add("userId");
        set.add("groupType");
        set.add("groupId");
        
        PaginationQueryByHQL pageQuery = new PaginationQueryByHQL(em, hql, condition);
        PageInfo page = pageQuery.getResultList();
        page.setItems(fillGroupInfo2User(page.getItems()));
        return page;
    }
}
