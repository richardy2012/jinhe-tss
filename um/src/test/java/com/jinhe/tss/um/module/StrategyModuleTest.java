package com.jinhe.tss.um.module;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.action.RoleAction;
import com.jinhe.tss.um.action.StrategyAction;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.service.IRoleService;
import com.jinhe.tss.um.service.IStrategyService;
import com.jinhe.tss.um.service.IUserService;

/**
 * 角色转授策略相关模块的单元测试
 */
public class StrategyModuleTest extends TxSupportTest4UM {
    
	StrategyAction action;
    
    @Autowired IStrategyService service;
    @Autowired IRoleService roleService;
    @Autowired IUserService userService;
    
    public void setUp() throws Exception {
        super.setUp();
        action = new StrategyAction();
        action.setService(service);
    }
    
    public void testCRUD() {
        // 新建一个用户组
        Group mainGroup = new Group();
        mainGroup.setParentId(UMConstants.MAIN_GROUP_ID);
        mainGroup.setName("R_财务部");
        mainGroup.setGroupType( Group.MAIN_GROUP_TYPE );
        mainGroup.setApplicationId(Config.getAttribute(Config.APPLICATION_CODE));
        groupService.createNewGroup(mainGroup , "", "");
        log.debug(mainGroup);
        
        // 新建一个用户组子组
        Group childGroup = new Group();
        childGroup.setParentId(mainGroup.getId());
        childGroup.setName("R_财务一部");
        childGroup.setGroupType( mainGroup.getGroupType() );
        childGroup.setApplicationId(mainGroup.getApplicationId());
        groupService.createNewGroup(childGroup , "", "");
        log.debug(childGroup);
        
        // 管理员直接在主组下新增用户
        User mainUser = new User();
        mainUser.setApplicationId(mainGroup.getApplicationId());
        mainUser.setLoginName("R_JonKing");
        mainUser.setUserName("R_JK");
        mainUser.setPassword("123456");
        mainUser.setGroupId(mainGroup.getId());
        userService.createOrUpdateUserInfo(mainGroup.getId(), mainUser , "" + mainGroup.getId(), "");
        log.debug(mainUser);
 
        // 新建角色
        RoleAction roleAction = new RoleAction();
        roleAction.setRoleService(roleService);
        roleAction.getRole().setIsGroup(0);
        roleAction.getRole().setName("R_办公室助理");
        roleAction.getRole().setParentId(UMConstants.ROLE_ROOT_ID);
        roleAction.getRole().setStartDate(new Date());
        Calendar calendar = new GregorianCalendar();
        calendar.add(UMConstants.ROLE_LIFE_TYPE, UMConstants.ROLE_LIFE_TIME);
        roleAction.getRole().setEndDate(calendar.getTime());
        roleAction.setRole2UserIds("");
        roleAction.setRole2GroupIds("");
        roleAction.saveRole();
        Long roleId = roleAction.getRole().getId();
        
        login(mainUser.getId(), mainUser.getLoginName()); // 更好登录用户，看其权限
        printUserRoleMapping(mainUser.getId(), 1); // 默认只有一个匿名角色
        
        // 开始测试转授策略模块的功能
        action.setIsNew(1);
        action.getSubAuthorizeStrategyInfo();
 
        action.getStrategy().setStartDate(new Date());
        calendar = new GregorianCalendar();
        calendar.add(UMConstants.STRATEGY_LIFE_TYPE, UMConstants.STRATEGY_LIFE_TIME);
        action.getStrategy().setEndDate(calendar.getTime());
        action.getStrategy().setName("转授策略一");
        action.setRule2GroupIds(mainGroup.getId() + "," + childGroup.getId());
        action.setRule2RoleIds(roleId + "");
        action.setRule2UserIds(mainUser.getId() + "");
        action.saveSubAuthorizeStrategy();
        
        Long strategyId = action.getStrategy().getId();
        
        action.setIsNew(null);
        action.setStrategyId(strategyId);
        action.getSubAuthorizeStrategyInfo();
        
        login(mainUser.getId(), mainUser.getLoginName()); // 更好登录用户，看其权限
        printUserRoleMapping(mainUser.getId(), 2); // 匿名角色 + 转授所得角色
        
        action.setDisabled(1);
        action.disable();
        
        login(mainUser.getId(), mainUser.getLoginName()); // 更好登录用户，看其权限
        printUserRoleMapping(mainUser.getId(), 1); // 匿名角色 （转授策略停用了）
        
        action.setDisabled(0);
        action.disable();
        
        login(mainUser.getId(), mainUser.getLoginName()); // 更好登录用户，看其权限
        printUserRoleMapping(mainUser.getId(), 2); // 匿名角色 + 转授所得角色（转授策略重新启用）
        
        action.getSubAuthorizeStrategys2Tree();
        
        action.delete();
        TestUtil.printEntity(super.permissionHelper, "RoleGroup");
        TestUtil.printEntity(super.permissionHelper, "RoleUser");
        
        login(mainUser.getId(), mainUser.getLoginName()); // 更好登录用户，看其权限
        printUserRoleMapping(mainUser.getId(), 1); // 匿名角色 （转授策略删除了）
        
        action.getSubAuthorizeStrategys2Tree();
        
        action.setGroupId(mainGroup.getId());
        action.getUsersByGroupId();
    }
    
    protected void printUserRoleMapping(Long userId, int count) {
        List<?> list = permissionHelper.getEntities("from RoleUserMapping where userId=?", userId);
        assertEquals(count, list.size());
        
        log.debug("表【RoleUserMapping】的所有记录:");
        for(Object temp : list) {
            log.debug(temp);
        }
        log.debug("\n");
    }
}
