package com.jinhe.tss.um.module;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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
    
    public void testUserModule() {
        mainGroup1 = new Group();
        mainGroup1.setParentId(UMConstants.MAIN_GROUP_ID);
        mainGroup1.setName("U_财务部");
        mainGroup1.setGroupType( Group.MAIN_GROUP_TYPE );
        groupService.createNewGroup(mainGroup1 , "", "-1");
        log.debug(mainGroup1);
        
        // 管理员直接在主组下新增用户
        User user1 = new User();
        user1.setLoginName("U_JonKing");
        user1.setUserName("U_JK");
        user1.setPassword("123456");
        user1.setGroupId(mainGroup1.getId());
        service.createOrUpdateUser(user1 , "" + mainGroup1.getId(), "-1");
        log.debug(user1);
        
        service.deleteUser(mainGroup1.getId(), user1.getId());
        user1.setId(null);
        service.createOrUpdateUser(user1 , "" + mainGroup1.getId(), "-1");
        log.debug(user1);
        
        // 注册一个用户
        User user2 = new User();
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
        
        List<User> mainUsers  = service.getUsersByGroup(mainGroup1.getId());
        assertEquals(2, mainUsers.size());
        
        _testAction(mainUsers);
        
        service.overdue();
        
        assertTrue(TestUtil.printLogs(logService) > 0);
    }
    
    public void _testAction(List<User> mainUsers) {
        
        action.getUserInfoAndRelation(UMConstants.IS_NEW, mainGroup1.getId());
        
        action.getUserInfoAndRelation(mainUsers.get(0).getId(), mainGroup1.getId());
        
        action.initAuthenticateMethod(mainGroup1.getId());
        
        action.uniteAuthenticateMethod(mainGroup1.getId(), "com.jinhe.tss.um.sso.UMPasswordIdentifier");
        
        action.startOrStopUser(mainGroup1.getId(), mainUsers.get(0).getId(), UMConstants.TRUE);
        action.startOrStopUser(mainGroup1.getId(), mainUsers.get(0).getId(), UMConstants.FALSE);
        
        action.getOnlineUserInfo();
        action.getOperatorInfo();
        action.getForgetPasswordInfo();
        
        action.getUserInfo();
        
        action.initPassword(mainGroup1.getId(), mainUsers.get(0).getId(), "369852");
        
        action.getSelectedUsersByGroupId(mainGroup1.getId());
        
        action.getUsersByGroupId("tss", mainGroup1.getId(), 1);
        
        UMQueryCondition userQueryCon = new UMQueryCondition();
        userQueryCon.setGroupId(mainGroup1.getId());
		action.searchUser(userQueryCon , "tss", 1);
        
    }
}
