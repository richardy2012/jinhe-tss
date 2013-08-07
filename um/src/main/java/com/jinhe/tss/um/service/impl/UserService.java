package com.jinhe.tss.um.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.jinhe.tss.um.service.IGroupService;
import com.jinhe.tss.um.service.IUserService;
import com.jinhe.tss.um.sso.UMPasswordIdentifier;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.InfoEncoder;

@Service("UserService")
public class UserService implements IUserService{
	
	@Autowired private IUserDao  userDao;
	@Autowired private IRoleDao  roleDao;
	@Autowired private IGroupDao groupDao;
	@Autowired private IGroupUserDao groupUserDao;
	@Autowired private IGroupService groupService;

	public void deleteUser(Long groupId, Long userId) {
        if(Environment.getOperatorId().equals(userId)) {
            throw new BusinessException("当前用户正在使用中，无法删除！");
        }
        
        Group group = groupDao.getEntity(groupId);
        if(Group.ASSISTANT_GROUP_TYPE.equals(group.getGroupType())){
        	groupUserDao.delete(userDao.getGroup2User(groupId, userId));
        } 
        else {
        	User entity = userDao.getEntity(userId);
        	userDao.removeUser(entity);		
        }
	}

	public User getUserById(Long id) {
		User entity = userDao.getEntity(id);
		userDao.evict(entity);
		return entity;
	}

    public void updateUser(User user) {
        userDao.update(user);
    }

    public User getUserByLoginName(String loginName) {
        return userDao.getUserByLoginName(loginName);
    }
 
    public void initPasswordByGroupId(Long groupId, Long userId, String initPassword) {
        if ( EasyUtils.isNullOrEmpty(initPassword) ) {
            throw new BusinessException("初始化密码不能为空");
        }
        
        List<User> userList;
        
        // 如果指定了用户，则只初始化该用户的密码
        if(userId != null && userId.longValue() > 0) {
        	userList = Arrays.asList(userDao.getEntity(userId));
        }
        else {
        	userList = groupDao.getUsersByGroupIdDeeply(groupId);
        }
       
        for (User user : userList) {
            String md5Password = InfoEncoder.string2MD5(user.getLoginName() + "_" + initPassword);
			user.setPassword(md5Password);  // 主用户组进行密码初始化时加密密码
            userDao.initUser(user);
        }
    }
    
    /**
     * 主用户组下且是新建的用户或密码已修改的用户，则对用户名＋密码进行MD5加密 
     * @param user
     * @param password
     * @return
     */
    private String createUserPwd(User user, String password) {
        // 新建
        if( user.getId() == null ){
            return InfoEncoder.string2MD5(user.getLoginName() + "_" + password);
        } 
                  
        // 编辑：如果密码改变, 则重新加密
        User older = userDao.getEntity(user.getId());
        userDao.evict(older);
        if (!password.equals(older.getPassword())) {
            return InfoEncoder.string2MD5(user.getLoginName() + "_" + password);
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
        user.setAuthenticateMethod(UMPasswordIdentifier.class.getName());

        // 默认有效期三年
        Calendar cl = new GregorianCalendar();
        cl.add(Calendar.YEAR, 3);
        user.setAccountUsefulLife(cl.getTime());
        
        userDao.create(user);
        
        // 自注册用户默认加入到自注册用户组(特殊组)中的未认证用户组
        createUser2Group(user.getId(), UMConstants.SELF_REGISTER_GROUP_ID_NOT_AUTHEN);
    }

    private void checkUserAccout(User user) {
        if(userDao.getUserByLoginName(user.getLoginName()) != null) {
            throw new BusinessException("相同登陆账号已经存在,请更换.");
        }
    }
    
	public void createOrUpdateUser(User user, String groupIdsStr, String roleIdsStr) {
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
        GroupUser user2Group = new GroupUser(userId, groupId);
        groupUserDao.saveGroupUser(user2Group);
    }

    /* 新建用户对角色的关系 */
    private void createUser2Role(Long userId, Long roleId) {
        RoleUser user2Role = new RoleUser();
        user2Role.setRoleId(roleId);
        user2Role.setUserId(userId);
        userDao.createObject(user2Role);
    }
    
	public Map<String, Object> getInfo4CreateNewUser(Long groupId) {
        Group group = groupDao.getEntity(groupId);
        
        List<Group> groups = new ArrayList<Group>();
		groups.add(group);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("User2RoleTree", roleDao.getEditableRoles()); // 操作人拥有的角色列表
        map.put("User2GroupExistTree", groups);
        map.put("User2RoleExistTree", groupDao.findRolesByGroupId(groupId)); // 新建用户继承所在组的角色列表
        map.put("disabled", group.getDisabled());
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
	            groupService.startOrStopGroup(groupId, UMConstants.FALSE);
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
        return groupDao.getUsersByGroup(groupId, pageNum, orderBy);
    }
 
    public PageInfo searchUser(UMQueryCondition qyCondition, Integer pageNum) {
		return groupDao.searchUser(qyCondition, pageNum);
    }
    
    // 可供定时器等对象直接调用。 
	public void overdue() { 
		try {
			Date today = new Date();
			userDao.executeHQL("update User u set u.disabled = 1 where u.accountUsefulLife < ?", today);
			userDao.executeHQL("update Role r set r.disabled = 1 where r.endDate < ?", today);
			userDao.executeHQL("update SubAuthorize s set s.disabled = 1 where s.endDate < ?", today);
		} catch(Exception e) {
			throw new BusinessException("执行停用过期时出错 ", e);
		}
	}
}
