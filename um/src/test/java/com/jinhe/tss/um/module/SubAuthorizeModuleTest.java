package com.jinhe.tss.um.module;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.action.RoleAction;
import com.jinhe.tss.um.action.SubAuthorizeAction;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.entity.Role;
import com.jinhe.tss.um.entity.SubAuthorize;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.service.IRoleService;
import com.jinhe.tss.um.service.ISubAuthorizeService;
import com.jinhe.tss.um.service.IUserService;

/**
 * 角色转授策略相关模块的单元测试
 */
public class SubAuthorizeModuleTest extends TxSupportTest4UM {
    
	@Autowired SubAuthorizeAction action;
	@Autowired RoleAction roleAction;
    
    @Autowired ISubAuthorizeService service;
    @Autowired IRoleService roleService;
    @Autowired IUserService userService;
    
    @Test
    public void testCRUD() {
        // 新建一个用户组
        Group mainGroup = new Group();
        mainGroup.setParentId(UMConstants.MAIN_GROUP_ID);
        mainGroup.setName("R_财务部");
        mainGroup.setGroupType( Group.MAIN_GROUP_TYPE );
        groupService.createNewGroup(mainGroup , "", "");
        log.debug(mainGroup);
        Long mainGroupId = mainGroup.getId();
        
        // 新建一个用户组子组
        Group childGroup = new Group();
		childGroup.setParentId(mainGroupId);
        childGroup.setName("R_财务一部");
        childGroup.setGroupType( mainGroup.getGroupType() );
        groupService.createNewGroup(childGroup , "", "");
        log.debug(childGroup);
        
        // 管理员直接在主组下新增用户
        User mainUser = new User();
        mainUser.setLoginName("R_JonKing");
        mainUser.setUserName("R_JK");
        mainUser.setPassword("123456");
        mainUser.setGroupId(mainGroupId);
        userService.createOrUpdateUser(mainUser , "" + mainGroupId, "");
        log.debug(mainUser);
 
        // 新建角色
        Role role = new Role();
        role.setIsGroup(0);
        role.setName("R_办公室助理");
        role.setParentId(UMConstants.ROLE_ROOT_ID);
        role.setStartDate(new Date());
        Calendar calendar = new GregorianCalendar();
        calendar.add(UMConstants.ROLE_LIFE_TYPE, UMConstants.ROLE_LIFE_TIME);
        role.setEndDate(calendar.getTime());
        roleAction.saveRole(response, request, role);
        Long roleId = role.getId();
        
        login(mainUser.getId(), mainUser.getLoginName()); // 更好登录用户，看其权限
        printUserRoleMapping(mainUser.getId(), 1); // 默认只有一个匿名角色
        
        // 开始测试转授策略模块的功能
        action.getSubAuthorizeStrategyInfo(response, UMConstants.DEFAULT_NEW_ID);
 
        SubAuthorize strategy = new SubAuthorize();
        strategy.setStartDate(new Date());
        calendar = new GregorianCalendar();
        calendar.add(UMConstants.STRATEGY_LIFE_TYPE, UMConstants.STRATEGY_LIFE_TIME);
        strategy.setEndDate(calendar.getTime());
        strategy.setName("转授策略一");
        request.addParameter("rule2UserIds", mainUser.getId() + "");
        request.addParameter("rule2GroupIds", mainGroupId + "," + childGroup.getId());
        request.addParameter("rule2RoleIds", roleId + "");
        action.saveSubAuthorizeInfo(response, request, strategy);
        
        Long strategyId = strategy.getId();
        action.getSubAuthorizeStrategyInfo(response, strategyId);
        
        login(mainUser.getId(), mainUser.getLoginName()); // 更好登录用户，看其权限
        printUserRoleMapping(mainUser.getId(), 2); // 匿名角色 + 转授所得角色
        
        action.disable(response, strategyId, 1);
        
        login(mainUser.getId(), mainUser.getLoginName()); // 更好登录用户，看其权限
        printUserRoleMapping(mainUser.getId(), 1); // 匿名角色 （转授策略停用了）
        
        action.disable(response, strategyId, 0);
        
        login(mainUser.getId(), mainUser.getLoginName()); // 更好登录用户，看其权限
        printUserRoleMapping(mainUser.getId(), 2); // 匿名角色 + 转授所得角色（转授策略重新启用）
        
        action.getSubAuthorizeStrategys2Tree(response);
        
        action.delete(response, strategyId);
        
        TestUtil.printEntity(super.permissionHelper, "RoleGroup");
        TestUtil.printEntity(super.permissionHelper, "RoleUser");
        
        login(mainUser.getId(), mainUser.getLoginName()); // 更好登录用户，看其权限
        printUserRoleMapping(mainUser.getId(), 1); // 匿名角色 （转授策略删除了）
        
        action.getSubAuthorizeStrategys2Tree(response);
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
