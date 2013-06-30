package com.jinhe.tss.um.module;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.action.UserAction;
import com.jinhe.tss.um.entity.Application;
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
    
    public void testUserModule() {
        mainGroup1 = new Group();
        mainGroup1.setParentId(UMConstants.MAIN_GROUP_ID);
        mainGroup1.setName("U_财务部");
        mainGroup1.setGroupType( Group.MAIN_GROUP_TYPE );
        mainGroup1.setApplicationId(Config.getAttribute(Config.APPLICATION_CODE));
        groupService.createNewGroup(mainGroup1 , "", "-1");
        log.debug(mainGroup1);
        
        // 管理员直接在主组下新增用户
        User user1 = new User();
        user1.setApplicationId(mainGroup1.getApplicationId());
        user1.setLoginName("U_JonKing");
        user1.setUserName("U_JK");
        user1.setPassword("123456");
        user1.setGroupId(mainGroup1.getId());
        service.createOrUpdateUserInfo(user1 , "" + mainGroup1.getId(), "-1");
        log.debug(user1);
        
        service.deleteUser(mainGroup1.getId(), user1.getId());
        user1.setId(null);
        service.createOrUpdateUserInfo(user1 , "" + mainGroup1.getId(), "-1");
        log.debug(user1);
        
        // 注册一个用户
        User user2 = new User();
        user2.setApplicationId(UMConstants.TSS_APPLICATION_ID);
        user2.setLoginName("U_JonKing-R");
        user2.setUserName("U_JK-R");
        user2.setPassword("123456");
        user2.setGroupId(UMConstants.SELF_REGISTER_GROUP_ID_NOT_AUTHEN);
        service.registerUser(user2);
        log.debug(user2);
        
        // 管理员将注册移动至主组（认证该用户通过）
        service.moveUser(UMConstants.SELF_REGISTER_GROUP_ID_NOT_AUTHEN, mainGroup1.getId(), user2.getId());
        
        user2.setUserName("JK-2");
        service.updateUser(user2);
        
        Group otherGroup = _testOtherGroupCRUD();
        
        List<User> mainUsers  = service.getUsersByGroup(mainGroup1.getId());
        List<User> otherUsers = service.getUsersByGroup(otherGroup.getId());
        assertEquals(2, mainUsers.size());
        assertEquals(2, otherUsers.size());
        
        // 导入其他用户组用户到主用户组下
        service.importUser(otherGroup.getId(), mainGroup1.getId(), otherUsers.get(1).getId());
        
        mainUsers  = service.getUsersByGroup(mainGroup1.getId());
        assertEquals(3, mainUsers.size());
        
        _testAction();
        
        service.overdue();
        
        assertTrue(TestUtil.printLogs(logService) > 0);
    }
    
    public Group _testOtherGroupCRUD() {
        Application application = new Application();
        application.setApplicationType(UMConstants.OTHER_SYSTEM_APP);
        application.setApplicationId("U_OA");
        application.setName("U_JH OA");
        appService.saveApplication(application);
        
        Group group1 = new Group();
        group1.setParentId(UMConstants.OTHER_APPLICATION_GROUP_ID);
        group1.setName("U_财务部");
        group1.setGroupType( Group.OTHER_GROUP_TYPE );
        group1.setApplicationId(application.getApplicationId());
        groupService.createNewGroup(group1 , "", "");
        
        User user1 = new User();
        user1.setApplicationId(group1.getApplicationId());
        user1.setLoginName("U_JonKing");
        user1.setUserName("U_JK");
        user1.setPassword("123456");
        user1.setGroupId(group1.getId());
        service.createOrUpdateUserInfo(user1 , "" + group1.getId(), "");
        log.debug(user1 + "\n");
        
        User user2 = new User();
        user2.setApplicationId(group1.getApplicationId());
        user2.setLoginName("JonKing3");
        user2.setUserName("JK3");
        user2.setPassword("123456");
        user2.setGroupId(group1.getId());
        service.createOrUpdateUserInfo(user2 , "" + group1.getId(), "");
        log.debug(user2 + "\n");
        
        return group1;
    }
    
    public void _testAction() {
        List<User> mainUsers  = service.getUsersByGroup(mainGroup1.getId());
        assertEquals(3, mainUsers.size());
        
        action.getUserInfoAndRelation(UMConstants.IS_NEW, mainGroup1.getId());
        
        action.getUserInfoAndRelation(mainUsers.get(0).getId(), mainGroup1.getId());
        
        action.initAuthenticateMethod(mainGroup1.getId());
        
        action.uniteAuthenticateMethod(mainGroup1.getId(), "com.jinhe.tss.um.sso.UMSLocalUserPWDIdentifier");
        
        action.startOrStopUser(mainGroup1.getId(), mainUsers.get(0).getId(), UMConstants.TRUE);
        action.startOrStopUser(mainGroup1.getId(), mainUsers.get(0).getId(), UMConstants.FALSE);
        
        action.getOnlineUserInfo();
        action.getOperatorInfo();
        action.getForgetPasswordInfo();
        
        action.getUserInfo();
        
        action.initPassword(mainGroup1.getId(), mainUsers.get(0).getId(), "369852");
        
        action.getSelectedUsersByGroupId(mainGroup1.getId());
        
        action.getOperation(mainGroup1.getId());
        
        action.getUsersByGroupId("tss", mainGroup1.getId(), 1);
        
        UMQueryCondition userQueryCon = new UMQueryCondition();
        userQueryCon.setGroupId(mainGroup1.getId());
		action.searchUser(userQueryCon , "tss", 1);
        
    }
}
