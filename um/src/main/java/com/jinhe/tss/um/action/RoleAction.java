package com.jinhe.tss.um.action;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 
public class RoleAction extends BaseActionSupport {

	private IRoleService roleService;
	private PermissionService permissionService;

	private Long    roleId;     //当资源对角色进行授权时, 表示resourceId
	private String  role2UserIds;
	private String  role2GroupIds;
	private Long    parentRoleId;
	private Integer roleState = UMConstants.FALSE;
	private Long    groupId;
	private Long    targetId;        // 移动或者排序的目标节点ID
	private int     direction;      // 分＋1（向下），和－1（向上）
	private String  applicationId; // 应用id
	private String  resourceType; // 资源类型id
	private String  permissionRank;   // 授权级别(1:普通(10)，2/3:可授权，可授权可传递(11))
	private String  setPermission;	 // 角色资源权限选项的集合, 当资源对角色授权时:  role1|2224,role2|4022
	
	private Role   role = new Role();
	private String isRole2Resource;
	
    /**
     * 获取所有的角色（不包系统级的角色）
     */
    public String getAllRole2Tree() {
        List<?> roles = roleService.getAllVisiableRole();
        TreeEncoder treeEncoder = new TreeEncoder(roles, new LevelTreeParser());
        treeEncoder.setNeedRootNode(false);
        return print("RoleGroupTree", treeEncoder);
    }

	/**
	 * 获取用户可见的角色组
	 */
	public String getCanAddedGroup2Tree(){
	    List<?> canAddGroups = roleService.getAddableRoleGroups();
		TreeEncoder treeEncoder = new TreeEncoder(canAddGroups, new LevelTreeParser());
		treeEncoder.setNeedRootNode(false);
		return print("GroupTree", treeEncoder);
	}
	
   /**
     * 保存一个Role对象的明细信息、角色对用户信息、角色对用户组的信息
     */
    public String saveRole() {
        boolean isNew = (role.getId() == null);
        roleService.saveRole2UserAndRole2Group(role, role2UserIds, role2GroupIds);
        
        return doAfterSave(isNew, role, "RoleGroupTree");
    }

    /**
     * 保存角色组信息
     */
    public String saveRoleGroupInfo(){
        boolean isNew = (role.getId() == null);
        roleService.saveRoleGroup(role);
        
        return doAfterSave(isNew, role, "RoleGroupTree");
    }
    
    /**
     * 获得角色组信息
     */
    public String getRoleGroupInfo(){
        XFormEncoder xFormEncoder;
        if (isCreateNew()) { // 如果是新增，则返回一个空的无数据的模板
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("parentId", parentRoleId);
            map.put("isGroup", UMConstants.TRUE);
            xFormEncoder = new XFormEncoder(UMConstants.ROLEGROUP_XFORM, map);
        }
        else {
            Role role = roleService.getRoleById(roleId);
            xFormEncoder = new XFormEncoder(UMConstants.ROLEGROUP_XFORM, role);
        }
        return print("RoleGroupInfo",xFormEncoder);     
    }
    
    /**
     * 获取一个Role（角色）对象的明细信息、角色对用户组信息、角色对用户信息
     */
    public String getRoleInfoAndRelation() {        
        if (isCreateNew()) { // 新建角色
            return getNewRoleInfoAndRelation();
        } 
        else { // 编辑角色
            return getEditRoleInfoAndRelation();
        }
    }

    private String getNewRoleInfoAndRelation(){
        XFormEncoder roleXFormEncoder;
        TreeEncoder usersTreeEncoder;
        TreeEncoder groupsTreeEncoder;
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parentId", parentRoleId);
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

        return print(new String[]{"RoleInfo", "Role2GroupTree", "Role2UserTree", "Role2GroupExistTree", "Role2UserExistTree"}, 
                new Object[]{roleXFormEncoder, groupsTreeEncoder, usersTreeEncoder, roleToGroupTree, roleToUserTree});
    }

    private String getEditRoleInfoAndRelation(){
        Map<String, Object> data = roleService.getInfo4UpdateExistRole(roleId);
        
        Role role = (Role)data.get("RoleInfo");         
        XFormEncoder roleXFormEncoder = new XFormEncoder(UMConstants.ROLE_XFORM, role);
    
        TreeEncoder usersTreeEncoder = new TreeEncoder(data.get("Role2UserTree"));
        usersTreeEncoder.setNeedRootNode(false);
    
        TreeEncoder groupsTreeEncoder = new TreeEncoder(data.get("Role2GroupTree"), new LevelTreeParser());
        groupsTreeEncoder.setNeedRootNode(false);
        
        TreeEncoder roleToGroupTree = new TreeEncoder(data.get("Role2GroupExistTree"));
        TreeEncoder roleToUserTree = new TreeEncoder(data.get("Role2UserExistTree"));

        return print(new String[]{"RoleInfo", "Role2GroupTree", "Role2UserTree", "Role2GroupExistTree", "Role2UserExistTree"}, 
                new Object[]{roleXFormEncoder, groupsTreeEncoder, usersTreeEncoder, roleToGroupTree, roleToUserTree});  
    }
	
	/**
	 * 删除角色
	 */
	public String delete(){
		roleService.delete(roleId);
		return printSuccessMessage();
	}
	
	/**
	 * 停用/启用角色
	 */
	public String disable(){
		roleService.disable(roleId, roleState);
        return printSuccessMessage();
	}
	
	/**
	 * 排序
	 */
	public String sort(){
		roleService.sort(roleId, targetId, direction);        
        return printSuccessMessage();
	}
	
	/**
	 * 移动
	 */
	public String move(){
		roleService.move(roleId, targetId);        
        return printSuccessMessage();
	}
	
	/**
	 * 得到操作权限
	 */
	public String getOperation() {
        roleId = (roleId == null) ? UMConstants.ROLE_ROOT_ID : roleId;
        
        // 角色（组）树上： 匿名角色节点只需一个“角色权限设置”菜单即可
        if(roleId.equals(UMConstants.ANONYMOUS_ROLE_ID)) {
        	return print("Operation", "p1,p2," + UMConstants.PERMISSION_SET_OPERRATION);
        }
        else {
        	List<?> list = PermissionHelper.getInstance().getOperationsByResource(roleId, RolePermissionsFull.class.getName(), RoleResources.class);
        	return print("Operation", "p1,p2," + EasyUtils.list2Str(list));
        }
        	
	}
	
	/**
	 * 根据用户组id查找用户列表
	 */
	public String getUserByGroupId(){
		List<?> list = roleService.getUsersByGroupId(groupId);
		return print("Group2UserListTree", new TreeEncoder(list));
	}

	/**
	 * 查询应用系统列表，根据登录用户ID过滤。
	 * 现在没有过滤(性能不允许),显示用户能够看到的所有应用， 没有授权权限的是不能进行授权的,所以这里不过滤没有大碍
	 */
	public String getApplications(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleId", roleId);
		map.put("isRole2Resource", isRole2Resource);

		XFormEncoder xFormEncoder = new XFormEncoder(UMConstants.SERACH_PERMISSION_XFORM, map);
		
		List<?> apps = roleService.getPlatformApplication();
		String[] appEditor = EasyUtils.generateComboedit(apps, "applicationId", "name", "|");
		xFormEncoder.setColumnAttribute("applicationId", "editorvalue", appEditor[0]);
		xFormEncoder.setColumnAttribute("applicationId", "editortext",  appEditor[1]);

		return print("SearchPermission", xFormEncoder);
	}
	
	/**
	 * 根据应用id获得资源类型。 做 应用系统/资源类型/授权级别 三级下拉框时用
	 */
	public String getResourceTypes(){
		StringBuffer sb = new StringBuffer();
        sb.append("<column name=\"resourceType\" caption=\"资源类型\" mode=\"string\" editor=\"comboedit\" ");
        
		List<?> types = roleService.getResourceTypeByAppId(applicationId);
		String[] resourceTypeEditor = EasyUtils.generateComboedit(types, "resourceTypeId", "name", "|");
        sb.append(" editorvalue=\"").append(resourceTypeEditor[0]).append("\" ");
        sb.append(" editortext=\"").append(resourceTypeEditor[1]).append("\"/>");

		return print("ResourceType", sb);
	}

	public String initSetPermission(){
		if( isRole2Resource != null && "1".equals(isRole2Resource) ){
			return getApplications();
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

		return print("SearchPermission", xFormEncoder);
	}
	
	// ===========================================================================
	// 授权相关方法
	// ===========================================================================	
	
	/**
	 * 获取授权用的矩阵图
	 */
	public String getPermissionMatrix() {  
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
        
        return print("setPermission", rolesTreeEncoder);
	}
	
	public String savePermission() {
	    if( applicationId == null ) {
            applicationId = PermissionHelper.getApplicationID();
        }
	    
        permissionService = PermissionHelper.getPermissionService(applicationId, permissionService);
        
	    // 角色对资源授权（“角色维护”菜单，多个资源授权给单个角色）
        if( "1".equals(isRole2Resource) ) {
            permissionService.saveResources2Role(applicationId, resourceType, roleId, permissionRank, setPermission);
        } 
        // 资源对角色授权（“资源授予角色”菜单，单个资源授权给多个角色）
        else {
            permissionService.saveResource2Roles(applicationId, resourceType, roleId, permissionRank, setPermission);
        }
        
        return printSuccessMessage();
	}

	public void setRoleService(IRoleService service) {
		this.roleService = service;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public void setParentRoleId(Long parentRoleId) {
		this.parentRoleId = parentRoleId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public void setRole2GroupIds(String role2GroupIds) {
		this.role2GroupIds = role2GroupIds;
	}

	public void setRole2UserIds(String role2UserIds) {
		this.role2UserIds = role2UserIds;
	}

	public void setRoleState(Integer roleState) {
		this.roleState = roleState;
	}
 
	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public void setPermissionRank(String permissionRank) {
		this.permissionRank = permissionRank;
	}

	public Role getRole() {
		return this.role;
	}

	public void setPermissionService(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

	public void setSetPermission(String setPermission) {
	    this.setPermission = setPermission;
	}

	public void setIsRole2Resource(String isRole2Resource) {
		this.isRole2Resource = isRole2Resource;
	}
}
