package com.jinhe.tss.um.search;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.action.RoleAction;
import com.jinhe.tss.um.action.SubAuthorizeAction;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.entity.Role;
import com.jinhe.tss.um.entity.SubAuthorize;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.service.IApplicationService;
import com.jinhe.tss.um.service.IRoleService;
import com.jinhe.tss.um.service.ISubAuthorizeService;
import com.jinhe.tss.um.service.IUserService;

/**
 * 授权信息相关搜索测试
 */
public class GeneralSearchTest extends TxSupportTest4UM {
	
    @Autowired GeneralSearchAction action;
    
    @Autowired RoleAction roleAction;
    @Autowired SubAuthorizeAction strategyAction;
    
    @Autowired GeneralSearchService service;
    @Autowired RemoteSearchService remoteService;
    @Autowired IApplicationService applicationService;
    
    @Autowired ISubAuthorizeService strategyService;
    @Autowired IRoleService roleService;
    @Autowired IUserService userService;
 
    @Test
    public void testGeneralSearch() {
        
        String applicationId = "tss";
        String resourceTypeId = UMConstants.GROUP_RESOURCE_TYPE_ID;
    	
    	action.getAllApplications(UMConstants.MAIN_GROUP_ID);
    	
    	action.getResourceTypes(applicationId);
    	
    	action.searchPermission(UMConstants.MAIN_GROUP_ID, applicationId, resourceTypeId);
    	
    	 // 新建一个用户组
        Group mainGroup = new Group();
        mainGroup.setParentId(UMConstants.MAIN_GROUP_ID);
        mainGroup.setName("G_财务部");
        mainGroup.setGroupType( Group.MAIN_GROUP_TYPE );
        groupService.createNewGroup(mainGroup , "", "");
        log.debug(mainGroup);
        
        // 管理员直接在主组下新增用户
        User mainUser = new User();
        mainUser.setLoginName("G_JonKing");
        mainUser.setUserName("G_JK");
        mainUser.setPassword("123456");
        mainUser.setGroupId(mainGroup.getId());
        userService.createOrUpdateUser( mainUser , "" + mainGroup.getId(), "");
        log.debug(mainUser);
 
        // 新建角色
        Role role = new Role();
        role.setIsGroup(0);
        role.setName("G_办公室助理");
        role.setParentId(UMConstants.ROLE_ROOT_ID);
        role.setStartDate(new Date());
        Calendar calendar = new GregorianCalendar();
        calendar.add(UMConstants.ROLE_LIFE_TYPE, UMConstants.ROLE_LIFE_TIME);
        role.setEndDate(calendar.getTime());
        roleAction.saveRole(response, role, "", "");
        Long roleId = role.getId();
        
        // 新建转授策略
        SubAuthorize strategy = new SubAuthorize();
        strategy.setStartDate(new Date());
        calendar = new GregorianCalendar();
        calendar.add(UMConstants.STRATEGY_LIFE_TYPE, UMConstants.STRATEGY_LIFE_TIME);
        strategy.setEndDate(calendar.getTime());
        strategy.setName("G_转授策略一");
        strategyAction.saveSubAuthorizeInfo(response, strategy, mainUser.getId() + "", mainGroup.getId() + "", roleId + "");
        
    	action.searchUserSubauth(mainGroup.getId());
    	
    	action.searchRolesByGroup(roleId);
    }
}
