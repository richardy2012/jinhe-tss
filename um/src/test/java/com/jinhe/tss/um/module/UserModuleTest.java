package com.jinhe.tss.um.module;

import static org.junit.Assert.*;
import java.util.List;

import org.junit.Test;
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
    
    Group mainGroup;
    Long mainGroupId;
    
    @Test
    public void testUserModule() {
        mainGroup = new Group();
        mainGroup.setParentId(UMConstants.MAIN_GROUP_ID);
        mainGroup.setName("U_财务部");
        mainGroup.setGroupType( Group.MAIN_GROUP_TYPE );
        groupService.createNewGroup(mainGroup , "", "-1");
        log.debug(mainGroup);
        
        mainGroupId = mainGroup.getId();
        
        // 管理员直接在主组下新增用户
        User user1 = new User();
        user1.setLoginName("U_JonKing");
        user1.setUserName("U_JK");
        user1.setPassword("123456");
		user1.setGroupId(mainGroupId);
        service.createOrUpdateUser(user1 , "" + mainGroupId, "-1");
        log.debug(user1);
        
        service.deleteUser(mainGroupId, user1.getId());
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
        
        user2.setUserName("JK-2");
        service.updateUser(user2);
        
        List<User> mainUsers  = service.getUsersByGroup(mainGroupId);
        assertEquals(1, mainUsers.size());
        
        _testAction(mainUsers);
        
        service.overdue();
        
        assertTrue(TestUtil.printLogs(logService) > 0);
    }
    
    private void _testAction(List<User> mainUsers) {
        
        action.getUserInfoAndRelation(response, UMConstants.DEFAULT_NEW_ID, mainGroupId);
        
        action.getUserInfoAndRelation(response, mainUsers.get(0).getId(), mainGroupId);
        
        action.initAuthenticateMethod(response, mainGroupId);
        
        action.uniteAuthenticateMethod(response, mainGroupId, "com.jinhe.tss.um.sso.UMPasswordIdentifier");
        
        action.startOrStopUser(response, mainGroupId, mainUsers.get(0).getId(), UMConstants.TRUE);
        action.startOrStopUser(response, mainGroupId, mainUsers.get(0).getId(), UMConstants.FALSE);
        
        action.getOnlineUserInfo(response);
        action.getOperatorInfo(response);
        action.getForgetPasswordInfo(response);
        
        action.getUserInfo(response);
        
        action.initPassword(response, mainGroupId, mainUsers.get(0).getId(), "369852");
        
        action.getSelectedUsersByGroupId(response, mainGroupId);
        
        action.getUsersByGroupId(response, "tss", mainGroupId, 1);
        
        UMQueryCondition userQueryCon = new UMQueryCondition();
        userQueryCon.setGroupId(mainGroupId);
		action.searchUser(response, userQueryCon , "tss", 1);
        
    }
}
