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
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.service.IRoleService;
import com.jinhe.tss.um.service.IUserService;

/**
 * 角色相关模块的单元测试
 */
public class RoleModuleTest extends TxSupportTest4UM {
    
	RoleAction action = new RoleAction();
    
    @Autowired IRoleService service;
    @Autowired IUserService userService;
    
    public void setUp() throws Exception {
        super.setUp();
        resetAction();
    }
    
    public void resetAction() {
        action = new RoleAction();
        action.setRoleService(service);
        action.setPermissionService(permissionService);
    }
    
    public void testRoleModule() {
        // 新建一个用户组
        Group mainGroup = new Group();
        mainGroup.setParentId(UMConstants.MAIN_GROUP_ID);
        mainGroup.setName("财务部");
        mainGroup.setGroupType( Group.MAIN_GROUP_TYPE );
        mainGroup.setApplicationId(Config.getAttribute(Config.APPLICATION_CODE));
        groupService.createNewGroup(mainGroup , "", "");
        log.debug(mainGroup);
        
        // 新建一个用户组子组
        Group childGroup = new Group();
        childGroup.setParentId(mainGroup.getId());
        childGroup.setName("财务一部");
        childGroup.setGroupType( mainGroup.getGroupType() );
        childGroup.setApplicationId(mainGroup.getApplicationId());
        groupService.createNewGroup(childGroup , "", "");
        log.debug(childGroup);
        
        // 管理员直接在主组下新增用户
        User mainUser = new User();
        mainUser.setApplicationId(mainGroup.getApplicationId());
        mainUser.setLoginName("JonKing");
        mainUser.setUserName("JK");
        mainUser.setPassword("123456");
        mainUser.setGroupId(mainGroup.getId());
        userService.createOrUpdateUserInfo(mainGroup.getId(), mainUser , "" + mainGroup.getId(), "");
        log.debug(mainUser);
        
        // 新建角色组
        action.getRole().setIsGroup(1);
        action.getRole().setName("角色组一");
        action.getRole().setParentId(UMConstants.ROLE_ROOT_ID);
        action.saveRoleGroupInfo();
        Long roleGroupId = action.getRole().getId();
        
        // 新建角色
        resetAction();
        action.getRole().setIsGroup(0);
        action.getRole().setName("办公室助理");
        action.getRole().setParentId(roleGroupId);
        action.getRole().setStartDate(new Date());
        Calendar calendar = new GregorianCalendar();
        calendar.add(UMConstants.ROLE_LIFE_TYPE, UMConstants.ROLE_LIFE_TIME);
        action.getRole().setEndDate(calendar.getTime());
        action.setRole2UserIds(UMConstants.ADMIN_USER_ID + "," + mainUser.getId());
        action.setRole2GroupIds("" + mainGroup.getId());
        action.saveRole();
        Long roleId = action.getRole().getId();
        
        // 读取修改角色组的模板
        action.setRoleId(roleGroupId);
        action.getRoleGroupInfo();
        
        // 读取新增或修改角色的模板
        action.setIsNew(1);
        action.setParentRoleId(UMConstants.ROLE_ROOT_ID);
        action.getRoleInfoAndRelation();
        
        action.setIsNew(null);
        action.setRoleId(roleId);
        action.getRoleInfoAndRelation();
        
        // 读取角色树形结构
        action.getAllRole2Tree();
        action.getAllRoleGroup2Tree();
       
        // 停用角色组
        action.setRoleId(roleGroupId);
        action.setRoleState(UMConstants.TRUE);
        action.disable();
        
        // 启用角色
        action.setRoleId(roleId);
        action.setRoleState(UMConstants.FALSE);
        action.disable();
        
        // 再新建一个角色
        resetAction();
        action.getRole().setIsGroup(0);
        action.getRole().setName("部门经理");
        action.getRole().setParentId(roleGroupId);
        action.getRole().setStartDate(new Date());
        action.getRole().setEndDate(calendar.getTime());
        action.setRole2UserIds(UMConstants.ADMIN_USER_ID + "," + mainUser.getId());
        action.setRole2GroupIds("" + mainGroup.getId());
        action.saveRole();
        Long secondRoleId = action.getRole().getId();
        action.getAllRole2Tree();
        
        // 对组进行排序
        action.setRoleId(roleId);
        action.setTargetId(secondRoleId);
        action.setDirection(1);
        action.sort(); 
        action.getAllRole2Tree();
        
        // 对角色进行移动
        action.setTargetId(UMConstants.ROLE_ROOT_ID);
        action.move();
        
        action.getOperation();
        
        action.setGroupId(mainGroup.getId());
        action.getUserByGroupId();
        
        action.initSetPermission();
        action.setIsRole2Resource("1");
        action.getApplications();
        action.setIsRole2Resource("0");
        action.getApplications();
        action.setApplicationId("tss");
        action.getResourceTypes();
        
        // 授权测试 PermissionRank
//      LOWER_PERMISSION            = "1";  普通授权
//      AUTHORISE_PERMISSION        = "2";  可授权授权
//      PASSON_AUTHORISE_PERMISSION = "3";  可传递授权
//      SUB_AUTHORISE_PERMISSION    = "4";  权限转授
        log.debug("====================== 开始测试授权 ============================");
        
        //一、 多个资源授权给单个角色
        action.setIsRole2Resource("1");
        action.setResourceType(UMConstants.MAINGROUP_RESOURCE_TYPE_ID);
        action.setRoleId(roleId);
        action.setPermissionRank("2");
        action.getPermissionMatrix();
        
        // 授权内容, 当多个资源对一个角色授权时:  resource1|2224, resource2|4022
   	    // 竖线后面为各个权限选项的打勾情况【0: 没打勾, 1: 仅此节点，虚勾 2: 此节点及所有子节点，实勾 3:禁用未选中 4:禁用已选中】
        action.setSetPermission(mainGroup.getId() + "|222222222222");
        action.savePermission();
        action.getPermissionMatrix();
        
        TestUtil.printEntity(super.permissionHelper, "MainGroupPermissions");
        TestUtil.printEntity(super.permissionHelper, "MainGroupPermissionsFull");
        
        // 二、单个资源授权给多个角色
        action.setIsRole2Resource(null);
        action.setRoleId(mainGroup.getId()); // 当资源对角色进行授权时, 表示resourceId
        action.setPermissionRank("2");
        action.getPermissionMatrix();
        
        // 授权内容, 当单个资源对多个角色授权时:  roleId1|2224, roleId2|4022
   	    // 竖线后面为各个权限选项的打勾情况【0: 没打勾, 1: 仅此节点，虚勾 2: 此节点及所有子节点，实勾 3:禁用未选中 4:禁用已选中】
        action.setSetPermission(secondRoleId + "|222222222222");
        action.savePermission();
        action.getPermissionMatrix();
        
        TestUtil.printEntity(super.permissionHelper, "MainGroupPermissions");
        TestUtil.printEntity(super.permissionHelper, "MainGroupPermissionsFull");
        
        login(mainUser.getId(), mainUser.getLoginName()); // 更好登录用户，看其权限
        action.getPermissionMatrix();
        TestUtil.printEntity(super.permissionHelper, "RoleUserMapping");
        printVisibleMainGroups();
        
        login(UMConstants.ADMIN_USER_ID, UMConstants.ADMIN_USER_NAME); // 换回Admin登录
        
        // 删除角色组
        action.setRoleId(roleGroupId);
        action.delete();
        action.getAllRole2Tree();
    }

	private void printVisibleMainGroups() {
		Object[] data = groupService.findGroups();
        List<?> groups = (List<?>) data[0];
        for(Object temp : groups) {
            log.debug(temp);
        }
        assertEquals(3, groups.size()); // 主用户组  财务部 财务一部
	}
}
