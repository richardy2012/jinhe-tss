package com.jinhe.tss.um.search;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.action.RoleAction;
import com.jinhe.tss.um.action.StrategyAction;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.service.IApplicationService;
import com.jinhe.tss.um.service.IRoleService;
import com.jinhe.tss.um.service.IStrategyService;
import com.jinhe.tss.um.service.IUserService;

/**
 * 授权信息相关搜索测试
 */
public class GeneralSearchTest extends TxSupportTest4UM {
	
	GeneralSearchAction action;
    
    @Autowired GeneralSearchService service;
    @Autowired RemoteSearchService remoteService;
    @Autowired IApplicationService applicationService;
    
    @Autowired IStrategyService strategyService;
    @Autowired IRoleService roleService;
    @Autowired IUserService userService;
    
    public void setUp() throws Exception {
        super.setUp();
        
        action = new GeneralSearchAction();
        action.setService(service);
        action.setRemoteService(remoteService);
        action.setApplicationService(applicationService);
        
        // 初始化虚拟登录用户信息
        login(UMConstants.ADMIN_USER_ID, UMConstants.ADMIN_USER_NAME);
    }
    
    public void testGeneralSearch() {
    	
    	action.setGroupId(UMConstants.MAIN_GROUP_ID);
    	action.getAllApplications();
    	
    	action.setApplicationId("tss");
    	action.getResourceTypes();
    	
    	action.setResourceTypeId("2");
    	action.searchPermission();
    	
    	action.searchMapping();
    	
    	 // 新建一个用户组
        Group mainGroup = new Group();
        mainGroup.setParentId(UMConstants.MAIN_GROUP_ID);
        mainGroup.setName("G_财务部");
        mainGroup.setGroupType( Group.MAIN_GROUP_TYPE );
        mainGroup.setApplicationId(Config.getAttribute(Config.APPLICATION_CODE));
        groupService.createNewGroup(mainGroup , "", "");
        log.debug(mainGroup);
        
        // 管理员直接在主组下新增用户
        User mainUser = new User();
        mainUser.setApplicationId(mainGroup.getApplicationId());
        mainUser.setLoginName("G_JonKing");
        mainUser.setUserName("G_JK");
        mainUser.setPassword("123456");
        mainUser.setGroupId(mainGroup.getId());
        userService.createOrUpdateUserInfo(mainGroup.getId(), mainUser , "" + mainGroup.getId(), "");
        log.debug(mainUser);
 
        // 新建角色
        RoleAction roleAction = new RoleAction();
        roleAction.setRoleService(roleService);
        roleAction.getRole().setIsGroup(0);
        roleAction.getRole().setName("G_办公室助理");
        roleAction.getRole().setParentId(UMConstants.ROLE_ROOT_ID);
        roleAction.getRole().setStartDate(new Date());
        Calendar calendar = new GregorianCalendar();
        calendar.add(UMConstants.ROLE_LIFE_TYPE, UMConstants.ROLE_LIFE_TIME);
        roleAction.getRole().setEndDate(calendar.getTime());
        roleAction.setRole2UserIds("");
        roleAction.setRole2GroupIds("");
        roleAction.saveRole();
        Long roleId = roleAction.getRole().getId();
        
        // 新建转授策略
        StrategyAction strategyAction = new StrategyAction();
        strategyAction.setService(strategyService);
        strategyAction.getStrategy().setStartDate(new Date());
        calendar = new GregorianCalendar();
        calendar.add(UMConstants.STRATEGY_LIFE_TYPE, UMConstants.STRATEGY_LIFE_TIME);
        strategyAction.getStrategy().setEndDate(calendar.getTime());
        strategyAction.getStrategy().setName("G_转授策略一");
        strategyAction.setRule2GroupIds(mainGroup.getId() + "");
        strategyAction.setRule2RoleIds(roleId + "");
        strategyAction.setRule2UserIds(mainUser.getId() + "");
        strategyAction.saveSubAuthorizeStrategy();
        
        action.setGroupId(mainGroup.getId());
    	action.searchUserStrategyInfo();
    	
    	action.setRoleId(roleId);
    	action.searchRolesInfo();
    	
    	action.searchUserInfoByRole();
    	action.searchUsersByGroup();
    	
    }
    
    
}
