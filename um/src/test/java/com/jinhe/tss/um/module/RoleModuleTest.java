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
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.entity.Role;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.service.IRoleService;
import com.jinhe.tss.um.service.IUserService;

/**
 * 角色相关模块的单元测试
 */
public class RoleModuleTest extends TxSupportTest4UM {
    
	@Autowired RoleAction action;
    
    @Autowired IRoleService service;
    @Autowired IUserService userService;
    
    @Test
    public void testRoleModule() {
        // 新建一个用户组
        Group mainGroup = new Group();
        mainGroup.setParentId(UMConstants.MAIN_GROUP_ID);
        mainGroup.setName("财务部");
        mainGroup.setGroupType( Group.MAIN_GROUP_TYPE );
        groupService.createNewGroup(mainGroup , "", "");
        log.debug(mainGroup);
        
        // 新建一个用户组子组
        Group childGroup = new Group();
        childGroup.setParentId(mainGroup.getId());
        childGroup.setName("财务一部");
        childGroup.setGroupType( mainGroup.getGroupType() );
        groupService.createNewGroup(childGroup , "", "");
        log.debug(childGroup);
        
        // 管理员直接在主组下新增用户
        User mainUser = new User();
        mainUser.setLoginName("JonKing");
        mainUser.setUserName("JK");
        mainUser.setPassword("123456");
        mainUser.setGroupId(mainGroup.getId());
        userService.createOrUpdateUser(mainUser , "" + mainGroup.getId(), "");
        log.debug(mainUser);
        
        // 新建角色组
        Role roleGroup1 = new Role();
        roleGroup1.setIsGroup(1);
        roleGroup1.setName("角色组一");
        roleGroup1.setParentId(UMConstants.ROLE_ROOT_ID);
        action.saveRole(response, roleGroup1, null, null);
        Long roleGroupId = roleGroup1.getId();
        
        // 新建角色
        Role role2 = new Role();
        role2.setIsGroup(0);
        role2.setName("办公室助理");
        role2.setParentId(roleGroupId);
        role2.setStartDate(new Date());
        Calendar calendar = new GregorianCalendar();
        calendar.add(UMConstants.ROLE_LIFE_TYPE, UMConstants.ROLE_LIFE_TIME);
        role2.setEndDate(calendar.getTime());
        action.saveRole(response, role2, UMConstants.ADMIN_USER_ID + "," + mainUser.getId(), "" + mainGroup.getId());
        Long roleId = role2.getId();
        
        // 读取修改角色组的模板
        action.getRoleGroupInfo(response, roleGroupId, null);
        
        // 读取新增或修改角色的模板
        action.getRoleInfo(response, UMConstants.DEFAULT_NEW_ID, UMConstants.ROLE_ROOT_ID);
        action.getRoleInfo(response, roleGroupId, null);
        
        // 读取角色树形结构
        action.getAllRole2Tree(response);
        action.getAllRoleGroup2Tree(response);
       
        // 停用角色组
        action.disable(response, roleGroupId, UMConstants.TRUE);
        
        // 启用角色
        action.disable(response, roleId, UMConstants.FALSE);
        
        // 再新建一个角色
        Role role3 = new Role();;
        role3.setIsGroup(0);
        role3.setName("部门经理");
        role3.setParentId(roleGroupId);
        role3.setStartDate(new Date());
        role3.setEndDate(calendar.getTime());
        action.saveRole(response, role3, UMConstants.ADMIN_USER_ID + "," + mainUser.getId(), "" + mainGroup.getId());
        Long secondRoleId = role3.getId();
        action.getAllRole2Tree(response);
        
        // 对角色进行移动
        action.move(response, secondRoleId, UMConstants.ROLE_ROOT_ID);
        
        action.getOperation(response, secondRoleId);
        
        action.initSetPermission(response, roleId, "1", "tss", UMConstants.GROUP_RESOURCE_TYPE_ID);
        action.getApplications(response, roleId, "1");
        action.getApplications(response, roleId, "0");
        
        action.getResourceTypes(response, "tss");
        
        // 授权测试 PermissionRank
//      LOWER_PERMISSION            = "1";  普通授权
//      AUTHORISE_PERMISSION        = "2";  可授权授权
//      PASSON_AUTHORISE_PERMISSION = "3";  可传递授权
//      SUB_AUTHORISE_PERMISSION    = "4";  权限转授
        log.debug("====================== 开始测试授权 ============================");
        
        //一、 多个资源授权给单个角色
        action.getPermissionMatrix(response, "2", "1", "tss", UMConstants.GROUP_RESOURCE_TYPE_ID, roleId);
        
        // 授权内容, 当多个资源对一个角色授权时:  resource1|2224, resource2|4022
   	    // 竖线后面为各个权限选项的打勾情况【0: 没打勾, 1: 仅此节点，虚勾 2: 此节点及所有子节点，实勾 3:禁用未选中 4:禁用已选中】
        String permissions = mainGroup.getId() + "|22";
        action.savePermission(response, "2", "1", "tss", UMConstants.GROUP_RESOURCE_TYPE_ID, roleId, permissions);
        action.getPermissionMatrix(response, "2", "1", "tss", UMConstants.GROUP_RESOURCE_TYPE_ID, roleId);
        
        TestUtil.printEntity(super.permissionHelper, "GroupPermissions");
        TestUtil.printEntity(super.permissionHelper, "GroupPermissionsFull");
        
        // 二、单个资源授权给多个角色。当资源对角色进行授权时, roleId表示resourceId
        Long resourceId = mainGroup.getId(); // 当资源对角色进行授权时, roleId表示resourceId
        action.getPermissionMatrix(response, "2", "0", "tss", UMConstants.GROUP_RESOURCE_TYPE_ID, resourceId);
        
        // 授权内容, 当单个资源对多个角色授权时:  roleId1|2224, roleId2|4022
   	    // 竖线后面为各个权限选项的打勾情况【0: 没打勾, 1: 仅此节点，虚勾 2: 此节点及所有子节点，实勾 3:禁用未选中 4:禁用已选中】
        permissions = secondRoleId + "|22";
        action.savePermission(response, "2", "0", "tss", UMConstants.GROUP_RESOURCE_TYPE_ID, resourceId, permissions);
        action.getPermissionMatrix(response, "2", "0", "tss", UMConstants.GROUP_RESOURCE_TYPE_ID, resourceId);
        
        TestUtil.printEntity(super.permissionHelper, "GroupPermissions");
        TestUtil.printEntity(super.permissionHelper, "GroupPermissionsFull");
        
        login(mainUser.getId(), mainUser.getLoginName()); // 更好登录用户，看其权限
        action.getPermissionMatrix(response, "2", "1", "tss", UMConstants.GROUP_RESOURCE_TYPE_ID, roleId);
        TestUtil.printEntity(super.permissionHelper, "RoleUserMapping");
        printVisibleMainGroups();
        
        login(UMConstants.ADMIN_USER_ID, UMConstants.ADMIN_USER_NAME); // 换回Admin登录
        
        // 删除角色组
        action.delete(response, roleGroupId);
        action.getAllRole2Tree(response);
    }

	private void printVisibleMainGroups() {
        List<?> groups = groupService.findGroups();
        for(Object temp : groups) {
            log.debug(temp);
        }
        assertEquals(3, groups.size()); // 主用户组  财务部 财务一部
	}
}
