package com.jinhe.tss.um.module;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.action.GroupAction;
import com.jinhe.tss.um.dao.IGroupDao;
import com.jinhe.tss.um.entity.Application;
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
    
    private void printGroups(int mainGroupNum, int assistGroupNum, int otherGroupNum, int appNum) {
        Object[] data = groupService.findGroups();
        
        List<?> groups = (List<?>) data[0];
        assertEquals(mainGroupNum + assistGroupNum, groups.size());
        for(Object temp : groups) {
            log.debug(temp);
        }
        
        Group otherAppGroupRoot = (Group) data[1];
        assertNotNull(otherAppGroupRoot);
        log.debug(otherAppGroupRoot);
        
        List<?> otherGroups = (List<?>)data[2]; 
        List<?> apps = (List<?>)data[3];
        assertEquals(otherGroupNum, otherGroups.size());
        assertEquals(appNum, apps.size());
        
        for(Object temp : otherGroups) {
            log.debug(temp);
        }
        for(Object temp : apps) {
            log.debug(temp);
        }
        
        log.debug("\n");
    }
    
    public void testGroupModule() {
        _testMainGroupCRUD();
        _testAssistGroupCRUD();
        _testOtherGroupCRUD();
        
        _testAction();
        
        assertTrue(TestUtil.printLogs(logService) > 0);
    }
    
    public void _testMainGroupCRUD() {
        printGroups(4, 1, 0, 0);
        
        Group group1 = new Group();
        group1.setParentId(UMConstants.MAIN_GROUP_ID);
        group1.setName("主用户组一");
        group1.setGroupType( Group.MAIN_GROUP_TYPE );
        group1.setApplicationId(APPLICATION_ID);
        groupService.createNewGroup(group1 , "", "-1");
        log.debug(group1 + "\n");
        
        groupService.deleteGroup(group1.getApplicationId(), group1.getId(), Group.MAIN_GROUP_TYPE);
        group1.setId(null);
        groupService.createNewGroup(group1 , "", "-1");
        
        Group group2 = new Group();
        group2.setParentId(UMConstants.MAIN_GROUP_ID);
        group2.setName("主用户组二");
        group2.setGroupType( Group.MAIN_GROUP_TYPE );
        group2.setApplicationId(APPLICATION_ID);
        groupService.createNewGroup(group2 , "", "-1");
        
        Group group3 = new Group();
        group3.setParentId(group1.getId());
        group3.setName("主用户组一.1");
        group3.setGroupType( Group.MAIN_GROUP_TYPE );
        group3.setApplicationId(APPLICATION_ID);
        groupService.createNewGroup(group3 , "", "-1");
        printGroups(7, 1, 0, 0);
        
        User user1 = new User();
        user1.setApplicationId(group1.getApplicationId());
        user1.setLoginName("JonKing");
        user1.setUserName("JK");
        user1.setPassword("123456");
        user1.setGroupId(group1.getId());
        userService.createOrUpdateUserInfo(user1 , "" + group1.getId(), "-1");
        log.debug(user1 + "\n");
        
        groupService.startOrStopGroup(APPLICATION_ID, group1.getId(), 1);
        groupService.startOrStopGroup(APPLICATION_ID, group1.getId(), 0);
        
        List<Group> groups = groupDao.getGroupsByType(Environment.getOperatorId(), 
        		UMConstants.GROUP_VIEW_OPERRATION, Group.MAIN_GROUP_TYPE);
        for(Group temp : groups) {
        	temp.setName(temp.getName() + "...");
            groupService.editExistGroup(temp, "", "-1");
        }
        
        log.debug("Testing sortGroup......");
        groupService.sortGroup(group1.getId(), group2.getId(), 1);
        printGroups(7, 1, 0, 0);
        
        List<User> users = groupService.findUsersByGroupId(group1.getId());
        assertEquals(1, users.size());
        log.debug(users.get(0) + "\n");
        
        List<?> roles = groupService.findEditableRolesByOperatorId();
        assertEquals(0, roles.size());
        
        roles = groupService.findRolesByGroupId(group1.getId());
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
    
    public void _testAssistGroupCRUD() {
        User user1 = userService.getUserByLoginName("JonKing");
        
        Group group1 = new Group();
        group1.setParentId(UMConstants.ASSISTANT_GROUP_ID);
        group1.setName("辅助组一");
        group1.setGroupType( Group.ASSISTANT_GROUP_TYPE );
        group1.setApplicationId(APPLICATION_ID);
        groupService.createNewGroup(group1 , "" + user1.getId(), "-1");
        
        groupService.deleteGroup(group1.getApplicationId(), group1.getId(), Group.ASSISTANT_GROUP_TYPE);
        group1.setId(null);
        groupService.createNewGroup(group1 , "" + user1.getId(), "-1");
        
        Group group2 = new Group();
        group2.setParentId(UMConstants.ASSISTANT_GROUP_ID);
        group2.setName("辅助组二");
        group2.setGroupType( Group.ASSISTANT_GROUP_TYPE );
        group2.setApplicationId(APPLICATION_ID);
        groupService.createNewGroup(group2 , "", "-1");
        
        Group group3 = new Group();
        group3.setParentId(group1.getId());
        group3.setName("辅助组一.1");
        group3.setGroupType( Group.ASSISTANT_GROUP_TYPE );
        group3.setApplicationId(APPLICATION_ID);
        groupService.createNewGroup(group3 , "", "-1");
        printGroups(7, 4, 0, 0);
        
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
    
    Group otherGroup1;
    
    public void _testOtherGroupCRUD() {
        Application application = new Application();
        application.setApplicationType(UMConstants.OTHER_SYSTEM_APP);
        application.setApplicationId("OA");
        application.setName("JH OA");
        appService.saveApplication(application);
        
        otherGroup1 = new Group();
        otherGroup1.setParentId(UMConstants.OTHER_APPLICATION_GROUP_ID);
        otherGroup1.setName("财务部");
        otherGroup1.setGroupType( Group.OTHER_GROUP_TYPE );
        otherGroup1.setApplicationId(application.getApplicationId());
        groupService.createNewGroup(otherGroup1 , "", "");
        
        groupService.deleteGroup(UMConstants.TSS_APPLICATION_ID, otherGroup1.getId(), Group.OTHER_GROUP_TYPE);
        otherGroup1.setId(null);
        groupService.createNewGroup(otherGroup1 , null, null);
        
        Group group2 = new Group();
        group2.setParentId(UMConstants.OTHER_APPLICATION_GROUP_ID);
        group2.setName("法务部");
        group2.setGroupType( Group.OTHER_GROUP_TYPE );
        group2.setApplicationId(application.getApplicationId());
        groupService.createNewGroup(group2 , "", "");
        
        Group group3 = new Group();
        group3.setParentId(otherGroup1.getId());
        group3.setName("法务部.1");
        group3.setGroupType( Group.OTHER_GROUP_TYPE );
        group3.setApplicationId(application.getApplicationId());
        groupService.createNewGroup(group3 , "", "");
        printGroups(7, 4, 3, 1);
        
        User user1 = new User();
        user1.setApplicationId(otherGroup1.getApplicationId());
        user1.setLoginName("JonKing");
        user1.setUserName("JK");
        user1.setPassword("123456");
        user1.setGroupId(otherGroup1.getId());
        userService.createOrUpdateUserInfo(user1 , "" + otherGroup1.getId(), "");
        log.debug(user1 + "\n");
        
        Object[] result = groupService.getOtherGroupsByOperationId(UMConstants.GROUP_EDIT_OPERRATION);
        Group otherAppGroupRoot = (Group) result[0];
        assertNotNull(otherAppGroupRoot);
        log.debug(otherAppGroupRoot + "\n");
        
        List<?> otherGroups = (List<?>) result[1];
        List<?> apps = (List<?>) result[2];
        assertEquals(3, otherGroups.size());
        assertEquals(1, apps.size());
        for(Object temp : otherGroups) {
            log.debug(temp);
        }
        log.debug("\n");
        for(Object temp : apps) {
            log.debug(temp);
        }
        log.debug("\n");
        
        result = groupService.getGroupsUnderAppByOperationId(UMConstants.GROUP_EDIT_OPERRATION, application.getApplicationId());
        otherAppGroupRoot = (Group) result[0];
        assertNotNull(otherAppGroupRoot);
        log.debug(otherAppGroupRoot + "\n");
        
        otherGroups = (List<?>) result[1];
        apps = (List<?>) result[2];
        assertEquals(3, otherGroups.size());
        assertEquals(1, apps.size());
        for(Object temp : otherGroups) {
            log.debug(temp);
        }
        log.debug("\n");
        for(Object temp : apps) {
            log.debug(temp);
        }
        log.debug("\n");
    }
    
    public void _testAction() {
        action.getAllGroup2Tree();
        
        action.setGroupType(Group.MAIN_GROUP_TYPE);
        action.getAllRoleGroup2Tree();
        action.setResourceId(3L);
        action.getOperation();
        action.setGroupId(3L);
        action.getGroupInfoAndRelation();
        
        action.setGroupType(Group.ASSISTANT_GROUP_TYPE);
        action.getAllRoleGroup2Tree();
        
        action.setGroupType(Group.OTHER_GROUP_TYPE);
        action.getAllRoleGroup2Tree();
        
        action.getAllGroup2Tree();
        
        // 测试其他用户组导入到主用户组
//        action.setGroupId(otherGroup1.getId());
//        action.setToGroupId(UMConstants.MAIN_GROUP_ID);
//        action.importGroup();
 
    }
}
