package com.jinhe.tss.um.syncdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinhe.tss.framework.component.progress.Progress;
import com.jinhe.tss.framework.component.progress.Progressable;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.persistence.ICommonDao;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.dao.IApplicationDao;
import com.jinhe.tss.um.dao.IGroupDao;
import com.jinhe.tss.um.entity.Application;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.entity.GroupUser;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.helper.dto.GroupDTO;
import com.jinhe.tss.um.helper.dto.UserDTO;
import com.jinhe.tss.um.permission.ResourcePermission;
import com.jinhe.tss.util.XMLDocUtil;
 
@Service("SyncData")
public class SyncService implements ISyncService, Progressable {
	
    @Autowired private ICommonDao commonDao;
    @Autowired private IGroupDao  groupDao;
    @Autowired private IApplicationDao  applicationDao;
    @Autowired private ResourcePermission resourcePermission;

    public Map<String, Object> getCompleteSyncGroupData(Long groupId, String dbGroupId, String applicationId) {
        return getDataFromOtherApp(groupId, dbGroupId, applicationId);
    }

    public Map<String, Object> getUniDirectionalSyncGroupData(Long groupId, String dbGroupId, String applicationId) {
        // 保存UMS组对其它应用组 的 对应的关系 key:dbGroupId -- group
        Map<String, Group> groupsMap = new HashMap<String, Group>();
        List<?> exsitGroups = getMappedGroups(applicationId, groupId); // "同步节点"下面的所有用户组
        for(Iterator<?> it = exsitGroups.iterator();it.hasNext();){
            Group group = (Group)it.next();
            groupsMap.put(group.getDbGroupId(), group);
        }

        // 保存UMS用户对其它应用用户 的 ID对应的关系 key:otherAppUserId -- value:user
        Map<String, User> usersMap = new HashMap<String, User>();
        List<?> exsitMappedUsers = getMappedUsers(applicationId, groupId); // ”同步节点“下面的所有用户
        for (Iterator<?> it = exsitMappedUsers.iterator(); it.hasNext();) {
            User user = (User) it.next();
            usersMap.put(user.getOtherAppUserId(), user);
        }
        
        // 保存内部组用户对应的关系 key:groupId_userId -- value:groupUser
        Map<String, GroupUser> groupUsersMap = new HashMap<String, GroupUser>();
        List<?> mappedGroupUsers = getMappedGroupUsers(applicationId, groupId); // ”同步节点“下面的所有用户对应组的所有关系
        for (Iterator<?> it = mappedGroupUsers.iterator(); it.hasNext();) {
            GroupUser groupUser = (GroupUser) it.next();
            groupUsersMap.put(groupUser.getGroupId() + "_" + groupUser.getUserId(), groupUser);
        }
        
        Map<String, Object> paramsMap = getDataFromOtherApp(groupId, dbGroupId, applicationId);
        paramsMap.put("groupsMap", groupsMap);
        paramsMap.put("usersMap", usersMap);
        paramsMap.put("groupUsersMap", groupUsersMap);
        
        return paramsMap;
    }

    private Map<String, Object> getDataFromOtherApp(Long groupId, String dbGroupId, String applicationId){
        Application application = applicationDao.getApplication(applicationId);
        if(application == null) {
            throw new BusinessException("未找到其它应用系统（" + applicationId + ")配置信息");
        }
        
        // 保存UMS组对其它应用组 的 ID对应的关系 key:dbGroupId -- value:groupId
        Map<String, Long> groupIdsMap = new HashMap<String, Long>();
        List<?> allGroups = getMappedGroups(applicationId); // 取出同步节点所在应用的所有，设置父子节点关系时用到（其实只需”同步节点“的父节点 ＋ 同步枝）
        for(Iterator<?> it = allGroups.iterator();it.hasNext();){
            Group group = (Group)it.next();
            groupIdsMap.put(group.getDbGroupId(), group.getId());
        }

        Map<String, String> appParams = initParam(application.getParamDesc());
        
        List<?> groups = getGroups(application.getDataSourceType(), appParams, dbGroupId); //从其它系统获取需要同步的所有用户组
        List<?> users  = getUsers (application.getDataSourceType(), appParams, dbGroupId); //从其它系统获取需要同步的所有用户

        // 检查是否存在同名用户。
        checkMultiLoginName(applicationId, users);
        
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("applicationId", applicationId);
        paramsMap.put("groupId", groupId);
        paramsMap.put("groups", groups);
        paramsMap.put("users", users);
        paramsMap.put("groupIdsMap", groupIdsMap);

        return paramsMap;
    }
    
    /**
     * 检查是否存在登录名重名，如果有，抛出异常提示重名的用户信息。
     *      除了判断是否和已有用户重名，还应判断是否是同个组下的。同个组下的重名用户会自动覆盖，无需报错。
     *      如需要检查一个组里是否有重名，则检查users列表是否有重名的。
     */
    private void checkMultiLoginName(String applicationId, List<?> users){

    }

    @SuppressWarnings("unchecked")
	public void execute(Map<String, Object> paramsMap, Progress progress) {
        String applicationId = (String)paramsMap.get("applicationId");
        Long groupId = (Long)paramsMap.get("groupId");
        List<?> groups = (List<?>)paramsMap.get("groups");
        List<?> users  = (List<?>)paramsMap.get("users");
        Map<String, Long> groupIdsMap = (Map<String, Long>)paramsMap.get("groupIdsMap");
        
        if(UMConstants.ALL_SYNC.equals(paramsMap.get("mode"))){
            deleteDataInSyncGroup(applicationId, groupId); // 删除um系统中同步组下的用户组、用户、以及GroupUser（包括已对应和未对应的）
            syncGroups(applicationId, groups, groupIdsMap, progress);
            syncUsers (applicationId, users, groupIdsMap, progress);
        }else {
            Map<String, Group> groupsMap = (Map<String, Group>)paramsMap.get("groupsMap");
            Map<String, User> usersMap  = (Map<String, User>)paramsMap.get("usersMap");
            Map<String, GroupUser> groupUsersMap = (Map<String, GroupUser>)paramsMap.get("groupUsersMap");
            syncGroups(applicationId, groupId, groups, groupIdsMap, groupsMap, progress);
            syncUsers (applicationId, users, groupIdsMap, usersMap, groupUsersMap, progress);
        }
    }

    private void syncGroups(String applicationId, List<?> otherGroups, Map<String, Long> groupIdsMap, Progress progress) {
        for (int i = 0; i < otherGroups.size(); i++) {
            GroupDTO groupDto = (GroupDTO) otherGroups.get(i);
            Group group = new Group();
            group.setApplicationId(applicationId);
            SyncDataHelper.setGroupByDTO(group, groupDto);
            group.setDbGroupId(groupDto.getId());
            
            Long parentId = (Long)groupIdsMap.get(groupDto.getParentId()); //获取其它应用组的父组对应UMS中组的ID
            parentId = (parentId == null) ? UMConstants.OTHER_APPLICATION_GROUP_ID : parentId;
            group.setParentId(parentId);
            group.setSeqNo(groupDao.getNextSeqNo(parentId));
            
            commonDao.create(group);
            resourcePermission.addResource(group.getId(), group.getResourceType());
            groupIdsMap.put(groupDto.getId(), group.getId()); // 保存对应结果
                
            updateProgressInfo(progress, otherGroups.size(), i);
        }
    }
    
    private void syncUsers(String applicationId, List<?> otherUsers, Map<String, Long> groupIdsMap, Progress progress) {
        List<String> temp = new ArrayList<String>();
        for (int i = 0; i < otherUsers.size(); i++) {
            UserDTO userDto = (UserDTO) otherUsers.get(i);
            Long umsGroupId = (Long) groupIdsMap.get(userDto.getGroupId());
            
            //如果用户登陆名相同，只保存第一个
            if(temp.contains(userDto.getLoginName()))
                continue;
            //如果用户所属的组不存在，则不导入该用户
            if(umsGroupId == null)
                continue;

            User user = new User();
            user.setApplicationId(applicationId);
            SyncDataHelper.setUserByDTO(user, userDto);
            user.setGroupId(umsGroupId);
            commonDao.create(user);
            
            commonDao.create(new GroupUser(user.getId(), umsGroupId));
            
            temp.add(user.getLoginName());
                
            updateProgressInfo(progress, otherUsers.size(), i);
        }
    }

    private void syncGroups(String applicationId, Long groupId, List<?> otherGroups, Map<String, Long> groupIdsMap, Map<String, Group> groupsMap, Progress progress) {
        for (int i = 0; i < otherGroups.size(); i++) {
            GroupDTO groupDto = (GroupDTO) otherGroups.get(i);
            String appGroupId = groupDto.getId(); //Group在其它应用中的ID
            String appParentId = groupDto.getParentId();
            Long umsParentId = (Long) groupIdsMap.get(appParentId);
            
            if (groupsMap.containsKey(appGroupId)) {// 如果组已经存在，则更新组
                Group group = (Group) groupsMap.remove(appGroupId); // 从groupsMap中删除
                groupDto.setSeqNo(group.getSeqNo());
                SyncDataHelper.setGroupByDTO(group, groupDto);
                
                //Group在其它应用里父节点有可能变了，parentId以及seqNo需要重新设置
                if (!groupId.equals(group.getId()) && !group.getParentId().equals(umsParentId)){ // 第一个节点parentId保持不变
                    group.setParentId(umsParentId);
                    group.setSeqNo(groupDao.getNextSeqNo(group.getParentId()));
                }
                commonDao.create(group);
            } else {
                Group group = new Group();
                group.setApplicationId(applicationId);
                SyncDataHelper.setGroupByDTO(group, groupDto);
                group.setDbGroupId(appGroupId);
                umsParentId = umsParentId != null ? umsParentId : UMConstants.OTHER_APPLICATION_GROUP_ID;
                group.setParentId(umsParentId);
                group.setSeqNo(groupDao.getNextSeqNo(group.getParentId()));
                commonDao.create(group);
                resourcePermission.addResource(group.getId(), group.getResourceType());
                groupIdsMap.put(appGroupId, group.getId());  // 保存对应结果
            }
            updateProgressInfo(progress, otherGroups.size(), i);
        }
    }
    
    private void syncUsers(String applicationId, List<?> otherUsers, Map<String, Long> groupIdsMap, Map<String, User> usersMap, Map<String, GroupUser> groupUsersMap, Progress progress) {
        for (int i = 0; i < otherUsers.size(); i++) {
            UserDTO userDto = (UserDTO) otherUsers.get(i);
            Long umsGroupId = (Long)groupIdsMap.get(userDto.getGroupId()); // 获取用户组 对应 在UMS中用户组的ID
            if(usersMap.containsKey(userDto.getId())){// 更新内部用户
                User user = (User)usersMap.remove(userDto.getId()); // 从内部用户中删除
                SyncDataHelper.setUserByDTO(user, userDto);
                if (!groupUsersMap.containsKey(umsGroupId + "_" + user.getId())) {
                    // 用户对组的关系已经改变,重新维护用户对组的关系
                    commonDao.deleteAll(commonDao.getEntities("from GroupUser o where o.userId = ?", new Object[]{user.getId()}));
                    commonDao.create(new GroupUser(user.getId(), umsGroupId));    
                }
                commonDao.create(user);             
            }else{// 新增内部用户
                User user = new User();
                user.setApplicationId(applicationId);
                SyncDataHelper.setUserByDTO(user, userDto);
                user.setGroupId(umsGroupId);
                commonDao.create(user);
                commonDao.create(new GroupUser(user.getId(), umsGroupId));    
            }
                
            updateProgressInfo(progress, otherUsers.size(), i);
        }
    }
    
    /**
     * 更新进度信息
     */
    private void updateProgressInfo(Progress progress, long total, int index){
        commonDao.flush();
        
        index = index + 1; //index 从0开始计数
        if(index % 20 == 0)
            progress.add(20); //每同步20个更新一次进度信息
        else if(index == total)
            progress.add(index % 20); //如果已经同步完，则将总数除以20取余数做为本次完成个数来更新进度信息
    }
        
   //----------------------------------------- 单个用户同步 ---------------------------------------------------------------
    
    public void syncUser(Long groupId, Long userId){
        User user = (User) commonDao.getEntity(User.class, userId);
        String otherAppUserId = user.getOtherAppUserId();
		if( otherAppUserId == null ) {
            throw new BusinessException("当前用户没有外部应用系统中对应用户（otherAppUserId==null），无法进行同步！");
        }
        
        UserDTO otherAppUser = getUserFromOtherApp(user.getApplicationId(), otherAppUserId);
        if( otherAppUser == null ) {
            // 如果为空，说明用户在其它应用不存在，可能已经被删除，则将之从UMS中也删除
            commonDao.delete(user);
            return;
        }
        SyncDataHelper.setUserByDTO(user, otherAppUser);
        commonDao.create(user);
    }
    
    private UserDTO getUserFromOtherApp(String applicationId, String otherAppUserId){
        Application app = applicationDao.getApplication(applicationId);
        Map<String, String> param = initParam(app.getParamDesc());
        return SyncDataHelper.getOutDataDao(app.getDataSourceType()).getUser(param, otherAppUserId);
    }

    //---------------------------------------------------------------------------------------------------------------------

    /**
     * 删除同步组下所有数据，完全同步用
     * 
     * @param applicationId
     * @param groupId
     */
    private void deleteDataInSyncGroup(String applicationId, Long groupId) {
        commonDao.deleteAll(getNotMappedGroups(applicationId, groupId));
        commonDao.deleteAll(getNotMappedGroupUsers(applicationId, groupId));
        commonDao.deleteAll(getNotMappedUsers(applicationId, groupId));
        
        commonDao.deleteAll(getMappedGroups(applicationId, groupId));
        commonDao.deleteAll(getMappedGroupUsers(applicationId, groupId));
        commonDao.deleteAll(getMappedUsers(applicationId, groupId));
    }

    private List<?> getGroups(Integer dataSourceType, Map<String, String> appParams, String groupId){
        String sql = (String)appParams.get(SyncDataHelper.QUERY_GROUP_SQL_NAME);
        return SyncDataHelper.getOutDataDao(dataSourceType).getOtherGroups(appParams, sql, groupId);
    }

    private List<?> getUsers(Integer dataSourceType, Map<String, String> appParams, String groupId){
        String sql = (String)appParams.get(SyncDataHelper.QUERY_USER_SQL_NAME);
        return SyncDataHelper.getOutDataDao(dataSourceType).getOtherUsers( appParams, sql, groupId );
    }

    private Map<String, String> initParam(String paramDescXML){
        Map<String, String> param = new HashMap<String, String>();
        if (null == paramDescXML || "".equals(paramDescXML)) 
            return param;
        
        Document doc = XMLDocUtil.dataXml2Doc(paramDescXML);
        for (Iterator<?> it = doc.getRootElement().elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            param.put(element.getName(), element.getTextTrim());
        }
        return param;
    }
    
    
    static String queryChildrenByGroupIdSQL = "select g.id from um_group g, (select t.decode from um_group t where t.id = ?) m "
			+ " where g.applicationId = ? and g.decode like m.decode||'%'";

    //取一个应用系统已经同步的用户组
	private List<?> getMappedGroups(String applicationId) {
		String sql = "select t.* from um_group t where t.applicationId = ? and t.dbGroupId is not null ";
		return commonDao.getEntitiesByNativeSql(sql, Group.class, applicationId);
	}
	
    // 取一个应用系统的一个组下面已经同步的用户组
	private List<?> getMappedGroups(String applicationId, Long groupId) {
		String sql = "select t.* from um_group t, (select g.decode from um_group g where g.id = ?) m "
				+ " where t.applicationId = ? and t.decode like m.decode||'%' and t.dbGroupId is not null ";
		return commonDao.getEntitiesByNativeSql(sql, Group.class, groupId, applicationId);
	}

	// 取一个应用系统里面一个组下面所有未同步的组
	private List<?> getNotMappedGroups(String applicationId, Long groupId) {
		String sql = "select t.* from um_group t, (select g.decode from um_group g where g.id = ?)m "
				+ " where t.applicationId = ?  and t.decode like m.decode||'%' and (t.dbGroupId = '' or t.dbGroupId is null)";
		return commonDao.getEntitiesByNativeSql(sql, Group.class, groupId, applicationId);
	}

	// 取一个应用系统的一个组下面未同步的用户
	private List<?> getNotMappedUsers(String applicationId, Long groupId) {
		String sql = "select u.* from um_user u, ("
				+ queryChildrenByGroupIdSQL
				+ " ) x, um_groupuser gu"
				+ " where x.id = gu.groupid and u.id = gu.userid and (u.otherAppUserId = '' or u.otherAppUserId is null)";
		return commonDao.getEntitiesByNativeSql(sql, User.class, groupId, applicationId);
	}

	//  取一个应用系统的一下组下已经同步的用户
	private List<?> getMappedUsers(String applicationId, Long groupId) {
		String sql = "select u.* from um_user u, ("
				+ queryChildrenByGroupIdSQL
				+ " ) x, um_groupuser gu"
				+ " where x.id = gu.groupid and u.id = gu.userid and u.otherAppUserId is not null";
		return commonDao.getEntitiesByNativeSql(sql, User.class, groupId, applicationId);
	}

	// 取一个应用系统里一个组下面所有没有同步的用户和组的关系
	private List<?> getNotMappedGroupUsers(String applicationId, Long groupId) {
		String sql = "select gu.* from um_groupuser gu, ("
				+ queryChildrenByGroupIdSQL
				+ " ) x, um_user u "
				+ " where gu.groupid = x.id and u.id = gu.userId and (u.otherAppUserId = '' or u.otherAppUserId is null)";
		return commonDao.getEntitiesByNativeSql(sql, GroupUser.class, groupId, applicationId);
	}

	//  取一个应用系统的一下组下面已经同步的用户的组的关系
	private List<?> getMappedGroupUsers(String applicationId, Long groupId) {
		String sql = "select gu.* from um_groupuser gu, ("
				+ queryChildrenByGroupIdSQL
				+ " ) x, um_user u  "
				+ " where gu.groupid = x.id and u.id = gu.userId and u.otherAppUserId is not null";
		return commonDao.getEntitiesByNativeSql(sql, GroupUser.class, groupId, applicationId);
	}
}
