package com.jinhe.tss.um.module;

import static org.junit.Assert.*;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.action.GroupAction;
import com.jinhe.tss.um.dao.IGroupDao;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.service.IApplicationService;
import com.jinhe.tss.um.service.IUserService;

/**
 * 用户组织相关模块的单元测试。
 * 
 * 注：排序组时，事务内组表的decode发生了变化，但资源视图内的相应decode值取出来还是排序以前的值，即视图的数据未更新。
 *    TODO 需要排查上述情况出现的原因，是H2数据库问题？抑或其他原因？
 */
public class GroupModuleTest extends TxSupportTest4UM {
    
	static final String APPLICATION_ID = Config.getAttribute(Config.APPLICATION_CODE).toLowerCase();

	@Autowired GroupAction action;
    
	@Autowired IGroupDao groupDao;
    @Autowired IUserService userService;
    @Autowired IApplicationService appService;
    
    private void printGroups(int mainGroupNum, int assistGroupNum) {
        List<?> groups = groupService.findGroups();
        assertEquals(mainGroupNum + assistGroupNum, groups.size());
        for(Object temp : groups) {
            log.debug(temp);
        }
        
        log.debug("\n");
    }
    
    @Test
    public void testGroupModule() {
        _testMainGroupCRUD();
        _testAssistGroupCRUD();
        _testAction();
        
        assertTrue(TestUtil.printLogs(logService) > 0);
    }
    
    Group mainGroup1;
    
    private void _testMainGroupCRUD() {
        printGroups(4, 1);
        
        mainGroup1 = new Group();
        mainGroup1.setParentId(UMConstants.MAIN_GROUP_ID);
        mainGroup1.setName("主用户组一");
        mainGroup1.setGroupType( Group.MAIN_GROUP_TYPE );
        groupService.createNewGroup(mainGroup1 , "", "-1");
        log.debug(mainGroup1 + "\n");
        
        Group group2 = new Group();
        group2.setParentId(UMConstants.MAIN_GROUP_ID);
        group2.setName("主用户组二");
        group2.setGroupType( Group.MAIN_GROUP_TYPE );
        groupService.createNewGroup(group2 , "", "-1");
        
        groupService.deleteGroup(group2.getId());
        group2.setId(null);
        groupService.createNewGroup(group2 , "", "-1");
        
        Group group3 = new Group();
        group3.setParentId(mainGroup1.getId());
        group3.setName("主用户组一.1");
        group3.setGroupType( Group.MAIN_GROUP_TYPE );
        groupService.createNewGroup(group3 , "", "-1");
        printGroups(7, 1);
        
        User user1 = new User();
        user1.setLoginName("JonKing");
        user1.setUserName("JK");
        user1.setPassword("123456");
        user1.setGroupId(mainGroup1.getId());
        userService.createOrUpdateUser(user1 , "" + mainGroup1.getId(), "-1");
        log.debug(user1 + "\n");
        
        groupService.startOrStopGroup( mainGroup1.getId(), 1);
        groupService.startOrStopGroup( mainGroup1.getId(), 0);
        
        List<Group> groups = groupDao.getGroupsByType(Environment.getOperatorId(), 
        		UMConstants.GROUP_VIEW_OPERRATION, Group.MAIN_GROUP_TYPE);
        for(Group temp : groups) {
        	temp.setName(temp.getName() + "...");
            groupService.editExistGroup(temp, "", "-1");
        }
        
        log.debug("Testing sortGroup......");
        groupService.sortGroup(mainGroup1.getId(), group2.getId(), 1);
        printGroups(7, 1);
        
        List<User> users = groupService.findUsersByGroupId(mainGroup1.getId());
        assertEquals(1, users.size());
        log.debug(users.get(0) + "\n");
        
        List<?> roles = groupService.findEditableRoles();
        assertEquals(0, roles.size());
        
        roles = groupService.findRolesByGroupId(mainGroup1.getId());
        assertEquals(1, roles.size());
        log.debug(roles.get(0) + "\n");
        
        Object[] result = groupService.getMainGroupsByOperationId(UMConstants.GROUP_EDIT_OPERRATION);
        List<?> groupIds = (List<?>) result[0];
        List<?> mainGroups = (List<?>) result[1];
        for(Object temp : groupIds) {
            log.debug(temp);
        }
        for(Object temp : mainGroups) {
            log.debug(temp);
        }
        log.debug("\n");
     }
    
    private void _testAssistGroupCRUD() {
        User user1 = userService.getUserByLoginName("JonKing");
        
        Group group1 = new Group();
        group1.setParentId(UMConstants.ASSISTANT_GROUP_ID);
        group1.setName("辅助组一");
        group1.setGroupType( Group.ASSISTANT_GROUP_TYPE );
        groupService.createNewGroup(group1 , "" + user1.getId(), "-1");
        
        groupService.deleteGroup(group1.getId());
        group1.setId(null);
        groupService.createNewGroup(group1 , "" + user1.getId(), "-1");
        
        Group group2 = new Group();
        group2.setParentId(UMConstants.ASSISTANT_GROUP_ID);
        group2.setName("辅助组二");
        group2.setGroupType( Group.ASSISTANT_GROUP_TYPE );
        groupService.createNewGroup(group2 , "", "-1");
        
        Group group3 = new Group();
        group3.setParentId(group1.getId());
        group3.setName("辅助组一.1");
        group3.setGroupType( Group.ASSISTANT_GROUP_TYPE );
        groupService.createNewGroup(group3 , "", "-1");
        printGroups(7, 4);
        
        List<User> users = groupService.findUsersByGroupId(group1.getId());
        assertEquals(1, users.size());
        log.debug(users.get(0) + "\n");
        
        List<?> roles = groupService.findRolesByGroupId(group1.getId());
        assertEquals(1, roles.size());
        log.debug(roles.get(0) + "\n");
        
        Object[] result = groupService.getAssistGroupsByOperationId(UMConstants.GROUP_EDIT_OPERRATION);
        List<?> groupIds = (List<?>) result[0];
        List<?> assistGroups = (List<?>) result[1];
        for(Object temp : groupIds) {
            log.debug(temp);
        }
        for(Object temp : assistGroups) {
            log.debug(temp);
        }
        log.debug("\n");
        
    }
    
    private void _testAction() {
        action.getAllGroup2Tree(response);
        
        action.getCanAddedGroup2Tree(response, Group.MAIN_GROUP_TYPE);
        action.getCanAddedGroup2Tree(response, Group.ASSISTANT_GROUP_TYPE);
        
        action.getOperation(response, Group.MAIN_GROUP_TYPE, -2L);
        
        action.getUserByGroupId(response, UMConstants.MAIN_GROUP_ID);
        
        action.getGroupInfo(response, UMConstants.MAIN_GROUP_ID, UMConstants.DEFAULT_NEW_ID, Group.MAIN_GROUP_TYPE);
        action.getGroupInfo(response, UMConstants.MAIN_GROUP_ID, mainGroup1.getId(), Group.MAIN_GROUP_TYPE);
        
        action.startOrStopGroup(response, mainGroup1.getId(), 1);
        action.startOrStopGroup(response, mainGroup1.getId(), 0);
        
        action.sortGroup(response, mainGroup1.getId(), mainGroup1.getId() + 2, 1);
        
//        action.editGroup(response, mainGroup1, "", "-1");
        
        action.deleteGroup(response, mainGroup1.getId());
        
        action.getAllGroup2Tree(response);
        
        // 测试用户同步。TODO 进度条需要单独起线程，里面没有事务。
//        action.syncData(response, applicationId, groupId, mode);
 
    }
}
