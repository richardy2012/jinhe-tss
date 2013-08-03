package com.jinhe.tss.um.action;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.tree.TreeNodeOptionsEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.entity.Role;
import com.jinhe.tss.um.entity.permission.resources.RoleResources;
import com.jinhe.tss.um.entity.permission.supplied.RolePermissionsFull;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.um.permission.PermissionService;
import com.jinhe.tss.um.permission.dispaly.IPermissionOption;
import com.jinhe.tss.um.permission.dispaly.ResourceTreeParser;
import com.jinhe.tss.um.permission.dispaly.TreeNodeOption4Permission;
import com.jinhe.tss.um.service.IRoleService;
import com.jinhe.tss.util.DateUtil;
import com.jinhe.tss.util.EasyUtils;
 
@Controller
@RequestMapping("role")
public class RoleAction extends BaseActionSupport {

	@Autowired private IRoleService roleService;
	@Autowired private PermissionService permissionService;
	
    /**
     * 获取所有的角色（不包系统级的角色）
     */
	@RequestMapping("/")
    public void getAllRole2Tree(HttpServletResponse response) {
        List<?> roles = roleService.getAllVisiableRole();
        TreeEncoder treeEncoder = new TreeEncoder(roles, new LevelTreeParser());
        treeEncoder.setNeedRootNode(false);
        print("RoleGroupTree", treeEncoder);
    }

	/**
	 * 获取用户可见的角色组
	 */
	@RequestMapping("/groups")
	public void getAllRoleGroup2Tree(HttpServletResponse response) {
	    List<?> canAddGroups = roleService.getAddableRoleGroups();
		TreeEncoder treeEncoder = new TreeEncoder(canAddGroups, new LevelTreeParser());
		treeEncoder.setNeedRootNode(false);
		print("GroupTree", treeEncoder);
	}
	
   /**
     * 保存一个Role对象的明细信息、角色对用户信息、角色对用户组的信息
     */
	@RequestMapping(method = RequestMethod.POST)
    public void saveRole(HttpServletResponse response, Role role, String role2UserIds, String role2GroupIds) {
        boolean isNew = (role.getId() == null);

        if(UMConstants.TRUE.equals(role.getIsGroup())) {
        	roleService.saveRoleGroup(role);
        }
        else {
        	roleService.saveRole2UserAndRole2Group(role, role2UserIds, role2GroupIds);
        }
        
        doAfterSave(isNew, role, "RoleGroupTree");
    }
    
    /**
     * 获得角色组信息
     */
	@RequestMapping("/group/{id}/{parentId}")
    public void getRoleGroupInfo(HttpServletResponse response, @PathVariable("id") Long id, @PathVariable("parentId") Long parentId) {
        XFormEncoder xFormEncoder;
        if (UMConstants.IS_NEW.equals(id)) { // 如果是新增，则返回一个空的无数据的模板
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("parentId", parentId);
            map.put("isGroup", UMConstants.TRUE);
            xFormEncoder = new XFormEncoder(UMConstants.ROLEGROUP_XFORM, map);
        }
        else {
            Role role = roleService.getRoleById(id);
            xFormEncoder = new XFormEncoder(UMConstants.ROLEGROUP_XFORM, role);
        }
        print("RoleGroupInfo", xFormEncoder);     
    }
    
    /**
     * 获取一个Role（角色）对象的明细信息、角色对用户组信息、角色对用户信息
     */
	@RequestMapping("/{id}/{parentId}")
    public void getRoleInfo(HttpServletResponse response, @PathVariable("id") Long id, @PathVariable("parentId") Long parentId) {        
        if ( UMConstants.IS_NEW.equals(id) ) { // 新建角色
            getNewRoleInfo(parentId);
        } 
        else { // 编辑角色
            getEditRoleInfo(id);
        }
    }

    private void getNewRoleInfo(Long parentId) {
        XFormEncoder roleXFormEncoder;
        TreeEncoder usersTreeEncoder;
        TreeEncoder groupsTreeEncoder;
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parentId", parentId);
        map.put("isGroup", UMConstants.FALSE);
        
        // 默认的有效时间
        map.put("startDate", DateUtil.format(new Date()));
        Calendar calendar = new GregorianCalendar();
        calendar.add(UMConstants.ROLE_LIFE_TYPE, UMConstants.ROLE_LIFE_TIME);
        map.put("endDate", DateUtil.format(calendar.getTime()));
        
        // 如果是新增,则返回一个空的无数据的模板
        roleXFormEncoder = new XFormEncoder(UMConstants.ROLE_XFORM, map);
       
        Map<String, Object> data = roleService.getInfo4CreateNewRole();

        usersTreeEncoder = new TreeEncoder(data.get("Role2UserTree"));
        usersTreeEncoder.setNeedRootNode(false);
    
        groupsTreeEncoder = new TreeEncoder(data.get("Role2GroupTree"), new LevelTreeParser());
        groupsTreeEncoder.setNeedRootNode(false);
        
        TreeEncoder roleToUserTree = new TreeEncoder(null);
        TreeEncoder roleToGroupTree = new TreeEncoder(null);

        print(new String[]{"RoleInfo", "Role2GroupTree", "Role2UserTree", "Role2GroupExistTree", "Role2UserExistTree"}, 
                new Object[]{roleXFormEncoder, groupsTreeEncoder, usersTreeEncoder, roleToGroupTree, roleToUserTree});
    }

    private void getEditRoleInfo(Long id) {
        Map<String, Object> data = roleService.getInfo4UpdateExistRole(id);
        
        Role role = (Role)data.get("RoleInfo");         
        XFormEncoder roleXFormEncoder = new XFormEncoder(UMConstants.ROLE_XFORM, role);
    
        TreeEncoder usersTreeEncoder = new TreeEncoder(data.get("Role2UserTree"));
        usersTreeEncoder.setNeedRootNode(false);
    
        TreeEncoder groupsTreeEncoder = new TreeEncoder(data.get("Role2GroupTree"), new LevelTreeParser());
        groupsTreeEncoder.setNeedRootNode(false);
        
        TreeEncoder roleToGroupTree = new TreeEncoder(data.get("Role2GroupExistTree"));
        TreeEncoder roleToUserTree = new TreeEncoder(data.get("Role2UserExistTree"));

        print(new String[]{"RoleInfo", "Role2GroupTree", "Role2UserTree", "Role2GroupExistTree", "Role2UserExistTree"}, 
                new Object[]{roleXFormEncoder, groupsTreeEncoder, usersTreeEncoder, roleToGroupTree, roleToUserTree});  
    }
	
	/**
	 * 删除角色
	 */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(HttpServletResponse response, @PathVariable("id") Long id) {
		roleService.delete(id);
		printSuccessMessage();
	}
	
	/**
	 * 停用/启用角色
	 */
    @RequestMapping(value = "/disable/{id}/{state}")
	public void disable(HttpServletResponse response, @PathVariable("id") Long id, @PathVariable("state") int state) {
		roleService.disable(id, state);
        printSuccessMessage();
	}
 
	/**
	 * 移动
	 */
    @RequestMapping(value = "/move/{id}/{toGroupId}", method = RequestMethod.POST)
	public void move(HttpServletResponse response, @PathVariable("id") Long id, @PathVariable("toGroupId") Long toGroupId) {
		roleService.move(id, toGroupId);        
        printSuccessMessage();
	}
	
	@RequestMapping("/operations/{id}")
	public void getOperation(HttpServletResponse response, @PathVariable("id") Long id) {
		id = (id == null) ? UMConstants.ROLE_ROOT_ID : id;
        
        // 角色（组）树上： 匿名角色节点只需一个“角色权限设置”菜单即可
        if(id.equals(UMConstants.ANONYMOUS_ROLE_ID)) {
        	print("Operation", "p1,p2," + UMConstants.ROLE_EDIT_OPERRATION);
        }
        else {
        	List<?> list = PermissionHelper.getInstance().getOperationsByResource(id, RolePermissionsFull.class.getName(), RoleResources.class);
        	print("Operation", "p1,p2," + EasyUtils.list2Str(list));
        }
	}
	
	/**
	 * 查询应用系统列表，根据登录用户ID过滤。
	 * 现在没有过滤(性能不允许),显示用户能够看到的所有应用， 没有授权权限的是不能进行授权的,所以这里不过滤没有大碍
	 */
	public void getApplications(HttpServletResponse response, Long roleId, String isRole2Resource) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleId", roleId);
		map.put("isRole2Resource", isRole2Resource);

		XFormEncoder xFormEncoder = new XFormEncoder(UMConstants.SERACH_PERMISSION_XFORM, map);
		
		List<?> apps = roleService.getPlatformApplication();
		String[] appEditor = EasyUtils.generateComboedit(apps, "applicationId", "name", "|");
		xFormEncoder.setColumnAttribute("applicationId", "editorvalue", appEditor[0]);
		xFormEncoder.setColumnAttribute("applicationId", "editortext",  appEditor[1]);

		print("SearchPermission", xFormEncoder);
	}
	
	/**
	 * 根据应用id获得资源类型。 做 应用系统/资源类型/授权级别 三级下拉框时用
	 */
	public void getResourceTypes(String applicationId) {
		StringBuffer sb = new StringBuffer();
        sb.append("<column name=\"resourceType\" caption=\"资源类型\" mode=\"string\" editor=\"comboedit\" ");
        
		List<?> types = roleService.getResourceTypeByAppId(applicationId);
		String[] resourceTypeEditor = EasyUtils.generateComboedit(types, "resourceTypeId", "name", "|");
        sb.append(" editorvalue=\"").append(resourceTypeEditor[0]).append("\" ");
        sb.append(" editortext=\"").append(resourceTypeEditor[1]).append("\"/>");

		print("ResourceType", sb);
	}

	public void initSetPermission(HttpServletResponse response, Long roleId, String isRole2Resource, String applicationId, String  resourceType) {
		if( isRole2Resource != null && "1".equals(isRole2Resource) ){
			getApplications(response, roleId, isRole2Resource);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		applicationId = applicationId == null ? PermissionHelper.getApplicationID() : applicationId;
        map.put("applicationId", applicationId);
		map.put("resourceType", resourceType);
		map.put("roleId", roleId);
		map.put("isRole2Resource", isRole2Resource);

		XFormEncoder xFormEncoder = new XFormEncoder(UMConstants.SERACH_PERMISSION_XFORM, map);

		xFormEncoder.setColumnAttribute("applicationId", "editortext", applicationId);
		xFormEncoder.setColumnAttribute("applicationId", "editorvalue", applicationId);

		xFormEncoder.setColumnAttribute("resourceType", "editortext", resourceType);
		xFormEncoder.setColumnAttribute("resourceType", "editorvalue", resourceType);

		print("SearchPermission", xFormEncoder);
	}
	
	// ===========================================================================
	// 授权相关方法
	// ===========================================================================	
	
	/**
	 * 获取授权用的矩阵图
	 */
	public void getPermissionMatrix(HttpServletResponse response, String permissionRank, String isRole2Resource, String applicationId, String resourceType, Long roleId) {  
	    if( EasyUtils.isNullOrEmpty(permissionRank) ){
            throw new BusinessException("请选择授权级别");
        }
	    
	    List<Long[]> roleUsers = roleService.getRoles4Permission();
	    Object[] matrixInfo;
	    
	    //  角色对资源授权（“角色维护”菜单，多个资源授权给单个角色）时，生成 资源－操作选项 矩阵
        if( "1".equals(isRole2Resource) ) {
            if( EasyUtils.isNullOrEmpty(applicationId) ){
                throw new BusinessException("请选择应用系统");
            }
            if( EasyUtils.isNullOrEmpty(resourceType) ){
                throw new BusinessException("请选择资源类型");
            }
            
            matrixInfo = permissionService.genResource2OperationMatrix(applicationId, resourceType, 
                    roleId, permissionRank, roleUsers);
        } 
        // 资源对角色授权（“资源授予角色”菜单，单个资源授权给多个角色）时，生成 角色－操作选项 矩阵。
        else {
            if( applicationId == null ) {
                applicationId = PermissionHelper.getApplicationID();
            }
 
            permissionService = PermissionHelper.getPermissionService(applicationId, permissionService);
            matrixInfo = permissionService.genRole2OperationMatrix(applicationId, resourceType, 
                    roleId, permissionRank, roleUsers); // 此时roleId其实是资源ID（resourceId）
        }
        
        TreeNodeOptionsEncoder treeNodeOptionsEncoder = new TreeNodeOptionsEncoder();
        List<?> operations = (List<?>) matrixInfo[1];
        if(null != operations){
            for ( Object temp : operations ) {
                treeNodeOptionsEncoder.add(new TreeNodeOption4Permission((IPermissionOption) temp));
            }
        }       
        
        TreeEncoder rolesTreeEncoder = new TreeEncoder(matrixInfo[0], new ResourceTreeParser());
        rolesTreeEncoder.setOptionsEncoder(treeNodeOptionsEncoder);
        rolesTreeEncoder.setNeedRootNode(false);
        
        print("setPermission", rolesTreeEncoder);
	}
	
	/**
	 * permissionRank  授权级别(1:普通(10)，2/3:可授权，可授权可传递(11))
	 * permissions   角色资源权限选项的集合, 当资源对角色授权时:  role1|2224,role2|4022
	 */
	public void savePermission(HttpServletResponse response, String permissionRank, String isRole2Resource, String applicationId, String resourceType, Long roleId, String permissions) {
	    if( applicationId == null ) {
            applicationId = PermissionHelper.getApplicationID();
        }
	    
        permissionService = PermissionHelper.getPermissionService(applicationId, permissionService);
        
	    // 角色对资源授权（“角色维护”菜单，多个资源授权给单个角色）
        if( "1".equals(isRole2Resource) ) {
            permissionService.saveResources2Role(applicationId, resourceType, roleId, permissionRank, permissions);
        } 
        // 资源对角色授权（“资源授予角色”菜单，单个资源授权给多个角色）
        else {
            permissionService.saveResource2Roles(applicationId, resourceType, roleId, permissionRank, permissions);
        }
        
        printSuccessMessage();
	}
}
