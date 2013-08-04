package com.jinhe.tss.um.module;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.action.UserAction;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.helper.UMQueryCondition;
import com.jinhe.tss.um.service.IApplicationService;
import com.jinhe.tss.um.service.IUserService;

/**
 * 用户相关模块的单元测试
 */
public class UserModuleTest extends TxSupportTest4UM {
    
	@Autowired UserAction action;
    
    @Autowired IUserService service;
    @Autowired IApplicationService appService;
    
    Group mainGroup1;
    Long mainGroupId;
    User user1;
    
    @Before
    public void setUp() {
    	Global.setContext(super.applicationContext);
        
        response = new MockHttpServletResponse();
		Context.setResponse(response);
        
        // 初始化虚拟登录用户信息
        login(UMConstants.ADMIN_USER_ID, UMConstants.ADMIN_USER_NAME);
    
        init();
        
    	// 检查初始化的组是否存在
    	List<?> groups = groupService.findGroups();
    	GroupModuleTest.printGroups(groups, 4, 1);
    	
        mainGroup1 = new Group();
        mainGroup1.setParentId(UMConstants.MAIN_GROUP_ID);
        mainGroup1.setName("主用户组一");
        mainGroup1.setGroupType( Group.MAIN_GROUP_TYPE );
        groupService.createNewGroup(mainGroup1 , "", "-1");
        log.debug(mainGroup1 + "\n");
        
        mainGroupId = mainGroup1.getId();
        
        // 管理员直接在主组下新增用户
        user1 = new User();
        user1.setLoginName("U_JonKing");
        user1.setUserName("U_JK");
        user1.setPassword("123456");
		user1.setGroupId(mainGroupId);
        service.createOrUpdateUser(user1 , "" + mainGroupId, "-1");
        log.debug(user1);
        
        List<User> users = groupService.findUsersByGroupId(mainGroupId);
        assertEquals(1, users.size());
        log.debug(users.get(0) + "\n");
    }
    
    @After
    public void tearDown() {
    	assertTrue(TestUtil.printLogs(logService) > 0);
    	super.tearDown();
    }
    
    @Test
    public void testOverdue() {
    	service.overdue();
    }
    
    @Test
    public void getUsersByGroup() {
    	action.getUsersByGroupId(response, "tss", mainGroupId, 1);
    	
    	action.getUsersByGroup(response, mainGroupId);
    	
    	List<User> mainUsers  = service.getUsersByGroup(mainGroupId);
        assertEquals(1, mainUsers.size());
        
        UMQueryCondition userQueryCon = new UMQueryCondition();
        userQueryCon.setGroupId(mainGroupId);
		action.searchUser(response, userQueryCon , "tss", 1);
    }
    
    @Test
    public void testUserCRUD() {
    	// 删除用户
		service.deleteUser(mainGroupId, user1.getId());
		
		// 新增用户
		user1.setId(null);
		service.createOrUpdateUser(user1 , "" + mainGroupId, "-1");
		log.debug(user1);
        
        // 注册一个用户
        User user2 = new User();
        user2.setLoginName("U_JonKing-R");
        user2.setUserName("U_JK-R");
        user2.setPassword("123456");
        user2.setGroupId(UMConstants.SELF_REGISTER_GROUP_ID_NOT_AUTHEN);
        service.registerUser(user2);
        log.debug(user2);
        
        // 修改用户
        user2.setUserName("JK-2");
        service.updateUser(user2);
    }
  
    @Test
    public void getUserInfo() {
    	action.getUserInfoAndRelation(response, UMConstants.DEFAULT_NEW_ID, mainGroupId); // 获取新增用户模板
        action.getUserInfoAndRelation(response, user1.getId(), mainGroupId); // 获取编辑用户模板
        
        action.getOnlineUserInfo(response);
        action.getOperatorInfo(response);
        action.getUserInfo(response);
        
        action.getForgetPasswordInfo(response);
    }
    
    @Test
    public void startOrStopUser() {
    	action.startOrStopUser(response, mainGroupId, user1.getId(), UMConstants.TRUE);
        action.startOrStopUser(response, mainGroupId, user1.getId(), UMConstants.FALSE);
    }
    
    @Test
    public void initUserInfo() {
		 action.initAuthenticateMethod(response, mainGroupId);
		 action.uniteAuthenticateMethod(response, mainGroupId, "com.jinhe.tss.um.sso.UMPasswordIdentifier");
		
		 action.initPassword(response, mainGroupId, user1.getId(), "369852");
		 action.initPassword(response, mainGroupId, 0L, "369852");
    }
}
