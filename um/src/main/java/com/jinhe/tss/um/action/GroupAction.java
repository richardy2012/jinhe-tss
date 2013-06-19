package com.jinhe.tss.um.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jinhe.tss.framework.component.progress.ProgressManager;
import com.jinhe.tss.framework.component.progress.Progressable;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeTranslator;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.ProgressActionSupport;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.entity.Application;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.helper.GroupTreeParser;
import com.jinhe.tss.um.helper.GroupTreeWithAppParser;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.um.service.IApplicationService;
import com.jinhe.tss.um.service.IGroupService;
import com.jinhe.tss.util.EasyUtils;
 
@Controller
@RequestMapping("/group")
public class GroupAction extends ProgressActionSupport {

	@Autowired private IGroupService service;
	@Autowired private IApplicationService appService;
	
//	private Long    groupId;
//    private String  type;   // 1:添加组 /2:添加用户 /3:查看组 /4:其他用户组的“导入到”主用户组下
//	private Long    toGroupId;
//	private String  applicationId;
//	private String  group2UserExistTree;
//	private String  group2RoleExistTree;
//	private Integer disabled = UMConstants.FALSE;
//	private Integer groupType;
//	private int     direction = 0;
//	private Long    resourceId;
//	private boolean isCascadeUser;
//	private String  resourceTypeId;
//	private Long appId;
//	private Long ruleId;
//    
//    private Group group = new Group();

	@RequestMapping("/all")
	public void getAllGroup2Tree() {
		Object groups = service.findGroups();
		TreeEncoder treeEncoder = new TreeEncoder(groups, new GroupTreeParser());
        treeEncoder.setNeedRootNode(false);
		
        print("GroupTree", treeEncoder);
	}

	/**
	 * type 1:添加组 /2:添加用户 /3:查看组 /4:其他用户组的“导入到”主用户组下
	 */
	@RequestMapping("/parents/{groupType}/{type}")
    public void getCanAddedGroup2Tree(int groupType, String type) {
        String operationId;
        if ("3".equals(type)) {
            operationId = UMConstants.GROUP_VIEW_OPERRATION;
        }
        else if ("2".equals(type)) {
            operationId = UMConstants.USER_ADD_OPERRATION;
        }
        else {
            operationId = UMConstants.GROUP_ADD_OPERRATION;
        }
        
        TreeEncoder treeEncoder;
        Object[] objs;
        if (Group.MAIN_GROUP_TYPE.equals(groupType) || "4".equals(type)) {// 主用户组 type=4的时候是其他用户组的“导入到”功能
            objs = service.getMainGroupsByOperationId(operationId); // 将其他组（包括用户）导入到主用户组，并完成用户对应
            treeEncoder = new TreeEncoder(objs[1], new LevelTreeParser());

            final List<?> groupIds = (List<?>) objs[0];
            treeEncoder.setTranslator(new ITreeTranslator() {
                public Map<String, Object> translate(Map<String, Object> attribute) {
                    if (!groupIds.contains(attribute.get("id"))) {
                        attribute.put("canselected", "0");
                    }
                    return attribute;
                }
            });
        }
        else if (Group.ASSISTANT_GROUP_TYPE.equals(groupType)) { // 辅助用户组
            objs = service.getAssistGroupsByOperationId(operationId);
            treeEncoder = new TreeEncoder(objs[1], new LevelTreeParser());
        }
        else if (Group.OTHER_GROUP_TYPE.equals(groupType)) {// 其他应用组
            if ("5".equals(type) || "2".equals(type)) {
                objs = service.getGroupsUnderAppByOperationId(operationId, applicationId);
            }
            else {
                objs = service.getOtherGroupsByOperationId(operationId);
            }
            treeEncoder = new TreeEncoder(objs, new GroupTreeWithAppParser());
        }
        else {
            throw new BusinessException("参数groupType值有误！groupType=" + groupType);
        }
        
        treeEncoder.setNeedRootNode(false);
        print("GroupTree", treeEncoder);
    }
	
	/**
	 * 得到操作权限
	 */
	public String getOperation() {
		// 资源类型是应用系统
		if ( UMConstants.APPLICATION_RESOURCE_TYPE_ID.equals(resourceTypeId) ) {
		    // 选中其他用户组的根节点,只判断有没有新建应用的权限(即对应用有管理权限)
		    if (UMConstants.OTHER_APPLICATION_GROUP_ID.equals(resourceId)) {
				resourceId = Long.valueOf(UMConstants.OTHER_SYSTEM_APP);
                List<?> list = appService.getOperationsByResourceId(resourceId);
                
                return print("Operation", "p1,p2," + EasyUtils.list2Str(list));
			} 
			else { // 选中其他应用系统,则判断用户对其他用户组根节点的权限
                groupType = new Integer(resourceTypeId);
			}
		} 
 
        String resourceTypeId = Group.getResourceType(groupType);
        if (Group.SELF_REGISTER_GROUP_TYPE.equals(groupType)) {// 自注册用户组类型:没有任何菜单
            return "p1,p2";
        } 
        else if (Group.SELF_REGISTER_GROUP_NOT_AUTHEN_TYPE .equals(groupType)) {// 自注册用户组类型(未认证)
            return "p1,p2, ug17"; // ug17:设置认证方式
        }
        else if (Group.SELF_REGISTER_GROUP_AUTHEN_TYPE.equals(groupType)) { // 自注册用户组类型(已认证)
            resourceTypeId = UMConstants.APPLICATION_RESOURCE_TYPE_ID;
        } 
        
        PermissionHelper ph = PermissionHelper.getInstance();
        Long operatorId = Environment.getOperatorId();
        
        List<?> operations = ph.getOperationsByResource(resourceTypeId, resourceId, operatorId);
        String resultStr = EasyUtils.list2Str(operations);

        // 如果对父节点有新增权限，则对该节点有复制权限
        List<?> parentOperations = null;
        if (UMConstants.APPLICATION_RESOURCE_TYPE_ID.equals(resourceTypeId)) {
            parentOperations = ph.getOperationsByResource(resourceTypeId, UMConstants.OTHER_APPLICATION_GROUP_ID, operatorId);
        } 
        else {
            Group group = service.getGroupById(resourceId);
            parentOperations = ph.getOperationsByResource(resourceTypeId, group.getParentId(), operatorId);
        }
        
        if (parentOperations.contains(UMConstants.GROUP_ADD_OPERRATION)) {
            resultStr += "," + UMConstants.GROUP_COPY_OPERRATION;
        }
        
        resultStr += ",ug16";  // TODO 默认添加综合查询功能, 没有判断是否有权限
        
        //加入“授予角色”菜单
        if( ph.getHighOperationsByResource(resourceTypeId, resourceId, operatorId).size() > 0) {
            resultStr += ",_1";
        }
        
        return print("Operation", "p1,p2," + resultStr);
    }
	
	/**
	 * 获取一个Group对象的明细信息、用户组对用户信息、用户组对角色的信息
	 */
	public String getGroupInfoAndRelation() {
        Map<String, Object> groupAttributes;
		boolean isNew = UMConstants.IS_NEW.equals(groupId);
        if(isNew){
        	groupAttributes = new HashMap<String, Object>();
            groupAttributes.put("parentId", toGroupId);
            groupAttributes.put("groupType", groupType);
            groupAttributes.put("applicationId", applicationId);
        } 
        else {
            groupAttributes = service.getGroupById(groupId).getAttributesForXForm();
        }
        
        List<?> users = service.findUsersByGroupId(groupId);
        TreeEncoder usersTreeEncoder = new TreeEncoder(users);
        
        List<?> roles = null;
        if( !Group.OTHER_GROUP_TYPE.equals(groupType) ) {
            roles = service.findRolesByGroupId(isNew ? toGroupId : groupId); // 如果是新建则找到父组对应的角色，如此新建的子组可以继承父组角色
        }
        TreeEncoder rolesTreeEncoder = new TreeEncoder(roles);
        
        String groupXForm = null;
        if(Group.MAIN_GROUP_TYPE.equals(groupType)) {
            groupXForm = UMConstants.GROUP_MAIN_XFORM;      // 主用户组
        }
        if(Group.ASSISTANT_GROUP_TYPE.equals(groupType)) {
            groupXForm = UMConstants.GROUP_ASSISTANT_XFORM; // 辅助用户组
        }
        if(Group.OTHER_GROUP_TYPE.equals(groupType)) {
            groupXForm = UMConstants.GROUP_OTHER_XFORM;     // 其他用户组
        }
 
        XFormEncoder groupEncoder = new XFormEncoder(groupXForm, groupAttributes);
 
        if(Group.OTHER_GROUP_TYPE.equals(groupType)){ // 其他用户组
            applicationId = isNew ? applicationId : service.getGroupById(groupId).getApplicationId();
            Application app = appService.getApplication(applicationId);
            groupEncoder.setColumnAttribute("applicationId", "editorvalue", applicationId);
            groupEncoder.setColumnAttribute("applicationId", "editortext",  app.getName());
            
            return print(new String[]{"GroupInfo", "Group2UserExistTree"}, new Object[]{groupEncoder, usersTreeEncoder});
        }
        
        return print(new String[]{"GroupInfo", "Group2RoleTree", "Group2RoleExistTree", "Group2UserExistTree"}, 
                new Object[]{groupEncoder, getRolesTreeEncoder(), rolesTreeEncoder, usersTreeEncoder});
	}

	/** 根据UserId获取角色  */
	private TreeEncoder getRolesTreeEncoder() {
		List<?> allRoles = service.findEditableRolesByOperatorId();
		TreeEncoder treeEncoder = new TreeEncoder(allRoles, new LevelTreeParser());
		treeEncoder.setNeedRootNode(false);
		return treeEncoder;
	}
    
    /**
     * 编辑一个Group对象的明细信息、用户组对用户信息、用户组对角色的信息
     */
    public String editGroup() {
        boolean isNew = group.getId() == null;
        if (group.getId() == null) {// 新建
            service.createNewGroup(group, group2UserExistTree, group2RoleExistTree);
        } else {// 编辑
            service.editExistGroup(group, group2UserExistTree, group2RoleExistTree);
        }
        return doAfterSave(isNew, group, "GroupTree");
    }
    
    /**
     * 启用或者停用用户组
     */
    public String startOrStopGroup() {  
        service.startOrStopGroup(UMConstants.TSS_APPLICATION_ID, groupId, disabled, groupType);
        return printSuccessMessage();
    }

    /**
     * 导入其它用户组的下的子组和用户到主用户组
     */
    @RequestMapping(value = "/import/{groupId}/{targetId}")
    public void importGroup(Long groupId, Long targetId) {
    	Map<String, Object> data = service.getImportGroupData(groupId, targetId);
        
        List<?> groups = (List<?>)data.get("groups");
        List<?> users  = (List<?>)data.get("users");
        int totalItem = users.size() + groups.size();
        
        // 因为导入数据到主用户组下会启用进度条中的线程（独立于当前线程的另一线程）进行，
        // 所以需要在action中启动，而不是在service，在service的话会导致事务提交不了。  
        ProgressManager manager = new ProgressManager((Progressable) service, totalItem, data);
        String code = manager.execute();
    	printScheduleMessage(code);
    }
    
    /**
     * 删除用户组
     */
    @RequestMapping("/rule/{groupId}/{ruleId}")
    public void deleteGroup(Long groupId, int groupType) {
        service.deleteGroup(UMConstants.TSS_APPLICATION_ID, groupId, groupType);     
        printSuccessMessage();
    }
    
    /**
     * 用户组的排序
     */
    @RequestMapping(value = "/sort/{groupId}/{targetId}/{direction}")
    public void sortGroup(Long groupId, Long targetId, int direction) {
        service.sortGroup(groupId, targetId, direction);
        printSuccessMessage();
    }
	
    @RequestMapping("/rule/{groupId}/{ruleId}")
	public void setPasswordRule(Long groupId, Long ruleId) {
		service.setPasswordRule(groupId, ruleId);
		printSuccessMessage("设置成功!");
	}
    
}
