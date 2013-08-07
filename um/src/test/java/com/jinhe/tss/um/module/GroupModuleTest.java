package com.jinhe.tss.um.module;

import static org.junit.Assert.*;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.action.GroupAction;
import com.jinhe.tss.um.dao.IGroupDao;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.service.IResourceService;
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
    @Autowired IResourceService appService;
    
    Group mainGroup1;
    Long mainGroupId;
    
    @Before
    public void setUp() {
    	Global.setContext(super.applicationContext);
        
    	request = new MockHttpServletRequest();
		Context.setResponse(response = new MockHttpServletResponse());
        
        // 初始化虚拟登录用户信息
        login(UMConstants.ADMIN_USER_ID, UMConstants.ADMIN_USER_NAME);
    
        init();
        
    	// 检查初始化的组是否存在
    	List<?> groups = groupService.findGroups();
    	printGroups(groups, 4, 1);
    	
        mainGroup1 = new Group();
        mainGroup1.setParentId(UMConstants.MAIN_GROUP_ID);
        mainGroup1.setName("主用户组一");
        mainGroup1.setGroupType( Group.MAIN_GROUP_TYPE );
        groupService.createNewGroup(mainGroup1 , "", "-1");
        log.debug(mainGroup1 + "\n");
        
        mainGroupId = mainGroup1.getId();
        
        User user1 = new User();
        user1.setLoginName("JonKing");
        user1.setUserName("JK");
        user1.setPassword("123456");
        user1.setGroupId(mainGroupId);
        userService.createOrUpdateUser(user1 , "" + mainGroupId, "-1");
        log.debug(user1 + "\n");
        
        List<User> users = groupService.getUsersByGroupId(mainGroupId);
        assertEquals(1, users.size());
        log.debug(users.get(0) + "\n");
    }
    
    @After
    public void tearDown() {
    	assertTrue(TestUtil.printLogs(logService) > 0);
    	super.tearDown();
    }
    
    static void printGroups(List<?> groups, int mainGroupNum, int assistGroupNum) {
//        assertEquals(mainGroupNum + assistGroupNum, groups.size());
        for(Object temp : groups) {
            log.debug(temp);
        }
        log.debug("\n");
    }
    
    @Test
    public void testStartOrStopGroup() {
        action.startOrStopGroup(response, mainGroupId, 1);
        action.startOrStopGroup(response, mainGroupId, 0);
    }
 
    @Test
    public void testGroupRoles() {
    	List<?> roles = groupService.findEditableRoles();
        assertEquals(0, roles.size());
        
        roles = groupService.findRolesByGroupId(mainGroupId);
        assertEquals(1, roles.size());
        log.debug(roles.get(0) + "\n");
    }
    
    @Test
    public void testMainGroupCRUD() {
    	
    	request.addParameter("Group2UserExistTree", "");
    	request.addParameter("Group2RoleExistTree", "-1");
    	action.saveGroup(response, request, mainGroup1);
    	
        Group group2 = new Group();
        group2.setParentId(UMConstants.MAIN_GROUP_ID);
        group2.setName("主用户组二");
        group2.setGroupType( Group.MAIN_GROUP_TYPE );
        groupService.createNewGroup(group2 , "", "-1");
        
        action.deleteGroup(response, group2.getId());
        group2.setId(null);
        groupService.createNewGroup(group2 , "", "-1");
        
        Group group3 = new Group();
		group3.setParentId(mainGroupId);
        group3.setName("主用户组一.1");
        group3.setGroupType( Group.MAIN_GROUP_TYPE );
        groupService.createNewGroup(group3 , "", "-1");
 
        List<Group> groups = groupDao.getGroupsByType(Environment.getOperatorId(), 
        		UMConstants.GROUP_VIEW_OPERRATION, Group.MAIN_GROUP_TYPE);
        for(Group temp : groups) {
        	temp.setName(temp.getName() + "...");
            groupService.editExistGroup(temp, "", "-1");
        }
        
        log.debug("Testing sort group......");
        action.sortGroup(response, mainGroupId, group2.getId(), 1);
        
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
    
    @Test
    public void testAssistGroupCRUD() {
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
        
        List<User> users = groupService.getUsersByGroupId(group1.getId());
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
        
        action.getCanAddedGroup2Tree(response, Group.ASSISTANT_GROUP_TYPE);
    }
    
    @Test
    public void testGetGroupTree() {
        action.getAllGroup2Tree(response);
        
        action.getCanAddedGroup2Tree(response, Group.MAIN_GROUP_TYPE);
        action.getCanAddedGroup2Tree(response, Group.ASSISTANT_GROUP_TYPE);
    }
    
    @Test
    public void testGetGroupInfo() {
        action.getGroupInfo(response, UMConstants.MAIN_GROUP_ID, UMConstants.DEFAULT_NEW_ID, Group.MAIN_GROUP_TYPE);
        action.getGroupInfo(response, UMConstants.MAIN_GROUP_ID, mainGroupId, Group.MAIN_GROUP_TYPE);
    }
    
    @Test
    public void testGetOperation() {
    	action.getOperation(response, mainGroupId);
    }
    
    @Test
    public void getUserByGroupId() {
    	action.getUserByGroupId(response, UMConstants.MAIN_GROUP_ID);
    }
    
    @Test
    public void syncData() {
        // 测试用户同步。TODO 进度条需要单独起线程，里面没有事务。
//        action.syncData(response, applicationId, groupId, mode);
    }
}
