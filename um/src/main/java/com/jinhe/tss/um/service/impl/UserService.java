package com.jinhe.tss.um.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.persistence.pagequery.PageInfo;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.dao.IGroupDao;
import com.jinhe.tss.um.dao.IGroupUserDao;
import com.jinhe.tss.um.dao.IRoleDao;
import com.jinhe.tss.um.dao.IUserDao;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.entity.GroupUser;
import com.jinhe.tss.um.entity.RoleUser;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.helper.UMQueryCondition;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.um.service.IGroupService;
import com.jinhe.tss.um.service.IUserService;
import com.jinhe.tss.um.sso.UMSLocalUserPWDIdentifier;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.InfoEncoder;

public class UserService implements IUserService{
	
	@Autowired private IUserDao  userDao;
	@Autowired private IRoleDao  roleDao;
	@Autowired private IGroupDao groupDao;
	@Autowired private IGroupUserDao groupUserDao;
	@Autowired private IGroupService groupService;

	public void deleteUser(Long groupId, Long userId, Integer groupType) {
        if(Environment.getOperatorId().equals(userId)) {
            throw new BusinessException("当前用户正在使用中，无法删除！");
        }
        
        if(Group.ASSISTANT_GROUP_TYPE.equals(groupType)){
        	groupUserDao.delete(userDao.getGroup2User(groupId, userId));
        } 
        else {
        	userDao.removeUser(getUserById(userId));		
        }
	}

	public User getUserById(Long id) {
		return userDao.getEntity(id);
	}

    public void updateUser(User user) {
        userDao.update(user);
    }
    
    public void updateUserPasswordRule(Long userId, Long ruleId){
        User user = userDao.getEntity(userId);
        user.setPasswordRuleId(ruleId);
        userDao.update(user);
    }

    public User getUserByLoginName(String loginName) {
        return userDao.getUserByLoginName(loginName);
    }
 
    public void initPasswordByGroupId(Long groupId, String initPassword) {
        if ( EasyUtils.isNullOrEmpty(initPassword) ) {
            throw new BusinessException("初始化密码不能为空");
        }
        
        List<User> userList = groupDao.getUsersByGroupIdDeeply(groupId);
        for (User user : userList) {
            if(UMConstants.TSS_APPLICATION_ID.equals(user.getApplicationId())){
                //主用户组进行密码初始化时加密密码
                user.setPassword(InfoEncoder.string2MD5(user.getLoginName() + "_" + initPassword)); 
            }
            else{
                user.setPassword(initPassword); //其它用户组 不加密
            }
            userDao.initUser(user);
        }
    }
    
    /**
     * 用户如果是主用户组下且是新建的用户或密码已修改的用户，则对用户名＋密码进行MD5加密，其它应用的用户不加密
     * @param user
     * @param password
     * @return
     */
    private String createUserPwd(User user, String password){
        if(UMConstants.TSS_APPLICATION_ID.equals(user.getApplicationId())){
            // 新建
            if( user.getId() == null ){
                return InfoEncoder.string2MD5(user.getLoginName() + "_" + password);
            } 
                      
            // 编辑：如果密码改变,则重新加密
            User older = userDao.getEntity(user.getId());
            userDao.evict(older);
            if (!password.equals(older.getPassword())) {
                return InfoEncoder.string2MD5(user.getLoginName() + "_" + password);
            }    
        }
        return password;
    }

    public void uniteAuthenticateMethod(Long groupId, String authenticateMethod) {
        List<User> userList = groupDao.getUsersByGroupIdDeeply(groupId);
        for (User user : userList) {
            user.setAuthenticateMethod(authenticateMethod);
            userDao.update(user);
        }           
    }
    
    public void registerUser(User user) {
        checkUserAccout(user);
        
        user.setPassword(InfoEncoder.string2MD5(user.getLoginName() + "_" + user.getPassword()));
        user.setAuthenticateMethod(UMSLocalUserPWDIdentifier.class.getName());

        // 默认有效期三年
        Calendar cl = new GregorianCalendar();
        cl.add(Calendar.YEAR, 3);
        user.setAccountUsefulLife(cl.getTime());
        
        userDao.create(user);
        
        // 自注册用户默认加入到自注册用户组(特殊组)中的未认证用户组
        createUser2Group(user.getId(), UMConstants.SELF_REGISTER_GROUP_ID_NOT_AUTHEN);
    }

    private void checkUserAccout(User user) {
        if(userDao.getUser(user.getApplicationId(), user.getLoginName()) != null) {
            throw new BusinessException("相同登陆账号已经存在,请更换.");
        }
    }
    
	public void createOrUpdateUserInfo(Long mainGroupId, User user, String groupIdsStr, String roleIdsStr) {
        /* 
         * 当一个用户即对应主用户组又对应辅助用户组时,要判断对应的那个主用户组的停用状态,
         * 如果该用户对应的主用户组是停用,则该用户也设为停用状态
         */
        if ( mainGroupId != null ) {
            Group mainGroup = groupDao.getEntity(mainGroupId);
            if(UMConstants.TRUE.equals(mainGroup.getDisabled())) {
                user.setDisabled(UMConstants.TRUE);
            }
        }
        
        // 加密密码
        user.setPassword(createUserPwd(user, user.getPassword())); 
        
        if( user.getId()== null ) {
            checkUserAccout(user);  //新建用户需要检测登陆名是否重复
            user = userDao.create(user);
        } else {
            userDao.update(user);
        }
        
        saveUser2Group(user.getId(), groupIdsStr);
        saveUser2Role (user.getId(), roleIdsStr);
	}
    
    /** 用户对组 */
    private void saveUser2Group(Long userId, String groupIdsStr) {
        List<?> user2Groups = userDao.findUser2GroupByUserId(userId);
        Map<Long, Object> historyMap = new HashMap<Long, Object>(); //把老的组对用户记录做成一个map，以"userId"为key
        for (Object temp : user2Groups) {
            GroupUser groupUser = (GroupUser) temp;
            historyMap.put(groupUser.getGroupId(), groupUser);
        }
        
        if ( !EasyUtils.isNullOrEmpty(groupIdsStr) ) {
            String[] groupIds = groupIdsStr.split(",");
            for (String temp : groupIds) {
                // 如果historyMap里面没有，则新增用户组对用户的关系；如果historyMap里面有，则从历史记录中移出；剩下的将被删除
                Long groupId = Long.valueOf(temp);
                if (historyMap.remove(groupId) == null) { 
                    createUser2Group(userId, groupId); // 如果历史数据里面没有，则新增
                } 
            }
        }
        
        // historyMap中剩下的就是该删除的了
        userDao.deleteAll(historyMap.values());
    }

    /* 用户对角色 */
    private void saveUser2Role(Long userId, String roleIdsStr) {
        List<?> user2Roles = userDao.findUser2RoleByUserId(userId);
        Map<Long, Object> historyMap = new HashMap<Long, Object>(); //把老的组对用户记录做成一个map，以"userId"为key
        for (Object temp : user2Roles) {
            RoleUser roleUser = (RoleUser) temp;
            historyMap.put(roleUser.getRoleId(), roleUser);
        }
        
        if ( !EasyUtils.isNullOrEmpty(roleIdsStr) ) {
            String[] roleIds = roleIdsStr.split(",");
            for (String temp : roleIds) {
                // 如果historyMap里面没有，则新增用户组对用户的关系；如果historyMap里面有，则从历史记录中移出；剩下的将被删除
                Long roleId = Long.valueOf(temp);
                if (historyMap.remove(roleId) == null) { 
                    createUser2Role(userId, roleId); 
                } 
            }
        }
        
        // historyMap中剩下的就是该删除的了
        userDao.deleteAll(historyMap.values());
    }

    /* 新建用户对组的关系 */
    private void createUser2Group(Long userId, Long groupId) {
        Integer nextUserOrder = groupUserDao.getNextSeqNo(groupId);
        GroupUser user2Group = new GroupUser(userId, groupId, nextUserOrder);
        groupUserDao.saveGroupUser(user2Group);
    }

    /* 新建用户对角色的关系 */
    private void createUser2Role(Long userId, Long roleId) {
        RoleUser user2Role = new RoleUser();
        user2Role.setRoleId(roleId);
        user2Role.setUserId(userId);
        userDao.createObject(user2Role);
    }

	public void editManualMappingInfo(Long userId, Long appUserId, String applicationId) {
		User appUser = userDao.getAppUser(appUserId, applicationId);
		if( appUser != null ){
			appUser.setAppUserId(null);
			userDao.update(appUser);
		}

		User user = userDao.getEntity(userId);
		user.setAppUserId(appUserId);
		userDao.update(user);
    }

	public void editAutoMappingInfo(Long groupId, Long toGroupId, Integer mappingColumn, Integer mapMode) {
		List<User> appUserList;
		List<User> mainUserList;
		if(new Integer(0).equals(mapMode)){
			appUserList = groupDao.getUsersByGroupId(groupId);
			mainUserList = groupDao.getUsersByGroupId(toGroupId);
		} else {
			appUserList= groupDao.getUsersByGroupIdDeeply(groupId);
			mainUserList= groupDao.getUsersByGroupIdDeeply(toGroupId);
		}
        
        Map<String, User> userMap = new HashMap<String, User>();
        for ( User user : mainUserList ) {
            userMap.put(getMappingField(mappingColumn, user), user);
        } 
        
        for ( User appUser : appUserList ) {
            User mainUser = userMap.get(getMappingField(mappingColumn, appUser));
            if ( mainUser != null ) {
                mainUser.setOtherAppUserId(appUser.getId().toString()); // 保存外部用户的ID到主用户的OtherAppUserId字段
                appUser.setAppUserId(mainUser.getId()); // 保存主用户的ID到外部用户的AppUserId字段
            }
        }
        userDao.flush();
    }

    private String getMappingField(Integer mappingColumn, User user) {
        String key;
        switch(mappingColumn){
        case 0:
            key = user.getLoginName();  // 按照登陆帐号对应
            break;
        case 1:
            key = user.getEmployeeNo(); // 按照工号对应
            break;
        case 2:
            key = user.getCertificateNumber(); // 按照证件号码对应
            break;
        case 3:
            key = user.getUserName(); //按照姓名对应
            break;
        default:
            throw new BusinessException("对应字段(1-按照登陆帐号对应 2-按照工号对应 3-按照证件号码对应 4-按照姓名对应) 值有误。mappingColumn=" + mappingColumn);
        }
        return key;
    }
    
	public Map<String, Object> getInfo4CreateNewUser(Long groupId) {
        List<Group> groups = new ArrayList<Group>();
        groups.add(groupDao.getEntity(groupId));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("User2RoleTree", roleDao.getEditableRoles()); // 操作人拥有的角色列表
        map.put("User2GroupExistTree", groups);
        map.put("User2RoleExistTree", groupDao.findRolesByGroupId(groupId)); // 新建用户继承所在组的角色列表
        return map;
    }

    public Map<String, Object> getInfo4UpdateExsitUser(Long userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("UserInfo", userDao.getEntity(userId));
        map.put("User2RoleTree", roleDao.getEditableRoles());
        map.put("User2GroupExistTree", groupDao.findGroupsByUserId(userId));
        map.put("User2RoleExistTree", userDao.findRolesByUserId(userId));
        return map;
    }

	public List<User> getManualMappingInfo(Long groupId) {
		List<User> users = groupDao.getUsersByGroupId(groupId);
		for (User user : users) {
			Long mapUserId = user.getAppUserId();
			if (mapUserId == null) {
				continue;
			}
			
            User mapUser = userDao.getEntity(mapUserId);
            if (mapUser != null) {
                user.setAppUserName(mapUser.getLoginName());
            }

            Group group = groupDao.findMainGroupByUserId(mapUserId);
            if(group != null) {
                user.setAppUserGroupName(group.getName());
            }
		}
		return users;
	}

	public void moveUser(Long groupId, Long toGroupId, Long userId) {
        Group group;
        if(toGroupId == null || (group = groupDao.getEntity(toGroupId)) == null) {
            throw new BusinessException("移动的目标用户组Id为null或者是已经被删除，移动失败！");
        }
            
		// 用户移动前先判断关系是否被改变,如果在移动时发现用户已经被移动到其他组,则取消移动.抛出异常
        GroupUser groupUser = userDao.getGroup2User(groupId, userId);
        if(groupUser == null) {
			throw new BusinessException("用户已经被移出该用户组，移动失败");
        }
            
		// 用户移动时，当移动到的用户组是停用时，那么这个用户立即停用。辅助用户组除外，因为辅助用户组停用是不影响下面的用户的。
	    // 如果该主用户组或者其他用户组是停用，则该用户也设为停用状态.
		if(!Group.ASSISTANT_GROUP_TYPE.equals(group.getGroupType()) && UMConstants.TRUE.equals(group.getDisabled())){
			User user = userDao.getEntity(userId);
			user.setDisabled(UMConstants.TRUE);
			userDao.update(user);
		}	
		
		// 维护关系时此处做法:  1.先删除原有的组对用户的关系  2.再添加的组对用户的关系
        groupUserDao.deleteGroupUser(groupUser);
		if( userDao.getGroup2User(toGroupId, userId) == null ) {
			createUser2Group(userId, toGroupId); // 添加新的组对用户的关系,如果已经存在则不重复保存
		}
	}
	
	//用户导入到
	public void importUser( Long groupId, Long toGroupId, Long appUserId ) {
		if( toGroupId == null || (groupDao.getEntity(toGroupId)) == null) {
			throw new BusinessException("导入的目标用户组Id为null或者是已经被删除，导入失败！");   
		}
		
		// 用户导入前先判断关系是否被改变，如果在导入时发现用户已经被移动到其他组，则取消导入。抛出异常
        GroupUser groupUser = userDao.getGroup2User(groupId, appUserId);
        if(groupUser == null) {
			throw new BusinessException("用户已经被移出该用户组，导入失败！");
        }
        
        // 新建主用户组用户
        User appUser = userDao.getEntity(appUserId);
        User mainUser = new User();
        BeanUtil.copy(mainUser, appUser);
        mainUser.setId(null);
        mainUser.setApplicationId(UMConstants.TSS_APPLICATION_ID);
        mainUser.setAppUserId(null);
        mainUser.setPassword(createUserPwd(mainUser, mainUser.getPassword())); //加密密码
        mainUser = userDao.create(mainUser);
        
        // 维护映射关系
        Long mainUserId = mainUser.getId();
        appUser.setAppUserId(mainUserId);
        userDao.update(appUser);
        
        Integer nextSeqNo = groupUserDao.getNextSeqNo(toGroupId);
        GroupUser mainGroupUser = new GroupUser(mainUserId, toGroupId, nextSeqNo);
        groupUserDao.saveGroupUser(mainGroupUser);
	}

	public void sortUser(Long groupId, Long userId, Long toUserId, int direction) {
	    Group group = groupDao.getEntity(groupId);
	    List<Long> sortableGroupIds = PermissionHelper.getInstance().getResourceIdsByOperation(
	            PermissionHelper.getApplicationID(), group.getResourceType(), UMConstants.GROUP_SORT_OPERRATION);
	    if( !sortableGroupIds.contains(groupId) ) {
	        throw new BusinessException("您对该用户所在组没有排序权限，操作失败！");
	    }
	    
		GroupUser startGroupUser  = userDao.getGroup2User(groupId, userId);
		GroupUser targetGroupUser = userDao.getGroup2User(groupId, toUserId);;
        
		Integer startUserOrder  = startGroupUser.getSeqNo();
		Integer targetUserOrder = targetGroupUser.getSeqNo();
		int setp = startUserOrder.compareTo(targetUserOrder) < 0 ? 1 : -1;
		startGroupUser.setSeqNo(targetUserOrder + (direction - setp) / 2);
		targetGroupUser.setSeqNo(targetUserOrder - (direction + setp) / 2);
		groupUserDao.update(startGroupUser);
		groupUserDao.update(targetGroupUser);
	}

	public void startOrStopUser(Long userId, Integer disabled, Long groupId) {
		User user = userDao.getEntity(userId);
		if ( UMConstants.FALSE.equals(disabled) ) { // 启用用户
            if( isOverdue(userId) ) {
            	throw new BusinessException("该用户已经过期，不能启用！");
            }
            
			// 同时也启用用户组
		    Group group = groupDao.getEntity(groupId);
		    
		    // 辅助用户组（当选中辅助用户下的停用用户启用时）
	        if(Group.ASSISTANT_GROUP_TYPE.equals(group.getGroupType())){ 
	            group = groupDao.findMainGroupByUserId(userId); // 找到用户所在主用户组
	        }
	        
	        // 如果主用户组状态为停用的话，则向上启用该主用户组及其所有父节点
	        if(UMConstants.TRUE.equals(group.getDisabled())) {
	            groupService.startOrStopGroup(group.getApplicationId(), groupId, UMConstants.FALSE, group.getGroupType());
		    }
		}
		 
        user.setDisabled(disabled);
        userDao.update(user);
	}
	
	// 判断用户是否过期
	private boolean isOverdue(Long userId){
		List<?> list = userDao.getEntities(" from User o where o.id=? and o.accountUsefulLife < ?", userId, new Date());
		return !EasyUtils.isNullOrEmpty(list);
	}
	
    public PageInfo getUsersByGroupId(Long groupId, Integer pageNum) {
        return groupDao.getUsersByGroup(groupId, pageNum);
    }

    public PageInfo getUsersByGroupId(Long groupId, Integer pageNum, String orderBy) {
    	Group group = groupDao.getEntity(groupId);
    	if(Group.OTHER_GROUP_TYPE.equals(group.getGroupType())){
    		return groupDao.getUsersByOtherGroupNoPermission(groupId, pageNum, orderBy);
    	} else {
            return groupDao.getUsersByGroup(groupId, pageNum, orderBy);
    	}
    }
 
    public PageInfo searchUser(UMQueryCondition qyCondition, Integer pageNum) {
    	if(Group.OTHER_GROUP_TYPE.equals(qyCondition.getGroupType())){
    		return groupDao.searchOtherUser(qyCondition, pageNum);
    	} else {
    		return groupDao.searchUser(qyCondition, pageNum);
    	}
    }
    
    public List<User> getUsersByGroup(Long groupId) {
        return groupDao.getUsersByGroupId(groupId);
    }
    
    // 可供定时器等对象直接调用。 
	public void overdue(){ 
		try{
			Date today = new Date();
			userDao.executeHQL("update User u set u.disabled = 1 where u.accountUsefulLife < ?", today);
			userDao.executeHQL("update Role r set r.disabled = 1 where r.endDate < ?", today);
			userDao.executeHQL("update Strategy s set s.disabled = 1 where s.endDate < ?", today);
		}catch(Exception e){
			throw new BusinessException("执行停用过期时出错 ", e);
		}
	}
}
