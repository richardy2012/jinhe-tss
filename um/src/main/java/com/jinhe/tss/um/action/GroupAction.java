package com.jinhe.tss.um.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.helper.GroupTreeParser;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.um.service.IGroupService;
import com.jinhe.tss.um.syncdata.ISyncService;
import com.jinhe.tss.util.EasyUtils;
 
@Controller
@RequestMapping("/group")
public class GroupAction extends ProgressActionSupport {

	@Autowired private IGroupService service;

	@RequestMapping("/all")
	public void getAllGroup2Tree() {
		Object groups = service.findGroups();
		TreeEncoder treeEncoder = new TreeEncoder(groups, new GroupTreeParser());
        treeEncoder.setNeedRootNode(false);
		
        print("GroupTree", treeEncoder);
	}

	@RequestMapping("/parents/{groupType}/{type}")
    public void getCanAddedGroup2Tree(int groupType) {
        String operationId = UMConstants.GROUP_EDIT_OPERRATION;
        
        TreeEncoder treeEncoder;
        if ( Group.MAIN_GROUP_TYPE.equals(groupType) ) {
        	// 用户可能只对某些子组有权限，需要把这些子组的父节点也找出来，以组成一棵完成的组织结构树
        	Object[] objs = service.getMainGroupsByOperationId(operationId); 
            treeEncoder = new TreeEncoder(objs[1], new LevelTreeParser());

            final List<?> groupIds = (List<?>) objs[0];
            treeEncoder.setTranslator(new ITreeTranslator() {
                public Map<String, Object> translate(Map<String, Object> attribute) {
                    if ( !groupIds.contains(attribute.get("id")) ) {
                        attribute.put("canselected", "0");
                    }
                    return attribute;
                }
            });
        }
        else if (Group.ASSISTANT_GROUP_TYPE.equals(groupType)) { // 辅助用户组
        	Object[] objs = service.getAssistGroupsByOperationId(operationId);
            treeEncoder = new TreeEncoder(objs[1], new LevelTreeParser());
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
	public void getOperation(Integer groupType, Long resourceId) {
		// 自注册用户组类型:没有任何菜单
        if (UMConstants.SELF_REGISTER_GROUP_ID.equals(resourceId) || UMConstants.SELF_REGISTER_GROUP_ID_NOT_AUTHEN.equals(resourceId)) {
        	return; 
        } 
        
        Long operatorId = Environment.getOperatorId();
        String resourceTypeId = UMConstants.GROUP_RESOURCE_TYPE_ID;
        List<?> operations = PermissionHelper.getInstance().getOperationsByResource(resourceTypeId, resourceId, operatorId);
 
        print("Operation", "p1,p2," + EasyUtils.list2Str(operations));
    }
	
	/**
	 * 根据用户组id查找用户列表
	 */
	@RequestMapping(value = "/users/{groupId}")
	public void getUserByGroupId(@PathVariable("groupId") Long groupId) {
		List<?> list = service.findUsersByGroupId(groupId);
		print("Group2UserListTree", new TreeEncoder(list));
	}
	
	/**
	 * 获取一个Group对象的明细信息、用户组对用户信息、用户组对角色的信息
	 */
	public void getGroupInfo(Long parentId, Long groupId, int groupType) {
        Map<String, Object> groupAttributes;
		boolean isNew = UMConstants.IS_NEW.equals(groupId);
        if(isNew) {
        	groupAttributes = new HashMap<String, Object>();
            groupAttributes.put("parentId", parentId);
            groupAttributes.put("groupType", groupType);
        } 
        else {
            Group group = service.getGroupById(groupId);
            groupAttributes = group.getAttributesForXForm();
        }
        
        List<?> users = service.findUsersByGroupId(groupId);
        TreeEncoder usersTreeEncoder = new TreeEncoder(users);
        
        String groupXForm = null;
        if(Group.MAIN_GROUP_TYPE.equals(groupType)) {
            groupXForm = UMConstants.GROUP_MAIN_XFORM;      // 主用户组
        }
        if(Group.ASSISTANT_GROUP_TYPE.equals(groupType)) {
            groupXForm = UMConstants.GROUP_ASSISTANT_XFORM; // 辅助用户组
        }
 
        XFormEncoder groupEncoder = new XFormEncoder(groupXForm, groupAttributes);
 
    	// 如果是新建则找到父组对应的角色，如此新建的子组可以继承父组角色
    	List<?> roles = service.findRolesByGroupId(isNew ? parentId : groupId); 
        TreeEncoder rolesTreeEncoder = new TreeEncoder(roles);
        
        TreeEncoder editableRolesTree = new TreeEncoder(service.findEditableRoles(), new LevelTreeParser());
		editableRolesTree.setNeedRootNode(false);
        
    	print(new String[]{"GroupInfo", "Group2RoleTree", "Group2RoleExistTree", "Group2UserExistTree"}, 
                new Object[]{groupEncoder, editableRolesTree, rolesTreeEncoder, usersTreeEncoder});
	}
    
    /**
     * 编辑一个Group对象的明细信息、用户组对用户信息、用户组对角色的信息
     */
    public void editGroup(Group group, String group2UserExistTree, String group2RoleExistTree) {
        boolean isNew = group.getId() == null;
        if (group.getId() == null) { // 新建
            service.createNewGroup(group, group2UserExistTree, group2RoleExistTree);
        } else {// 编辑
            service.editExistGroup(group, group2UserExistTree, group2RoleExistTree);
        }
        doAfterSave(isNew, group, "GroupTree");
    }
    
    /**
     * 启用或者停用用户组
     */
    @RequestMapping(value = "/disable/{id}/{disabled}")
    public void startOrStopGroup(Long id, int disabled) {  
        service.startOrStopGroup(id, disabled);
        printSuccessMessage();
    }
    
    /**
     * 删除用户组
     */
    @RequestMapping(value = "/{groupId}/", method = RequestMethod.DELETE)
    public void deleteGroup(Long groupId) {
        service.deleteGroup(groupId);     
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
    
    
    @Autowired private ISyncService  syncService;
    
    @RequestMapping("/{groupId}")
    public void syncData(String applicationId, Long groupId, int mode) {
        Group group = service.getGroupById(groupId);
        String dbGroupId = group.getDbGroupId();
        if ( EasyUtils.isNullOrEmpty(dbGroupId) ) {
            throw new BusinessException("导入组的对应外部应用组的ID（dbGroupId）为空");
        }
        
        Map<String, Object> datasMap =  syncService.getCompleteSyncGroupData(groupId, applicationId, dbGroupId);
        
        List<?> groups = (List<?>)datasMap.get("groups");
        List<?> users  = (List<?>)datasMap.get("users");
        int totalCount = users.size() + groups.size();
        
        // 因为同步数据会启用进度条中的线程进行，所以需要在action中启动，而不是在service，在service的话会导致事务提交不了
        ProgressManager manager = new ProgressManager((Progressable) syncService, totalCount, datasMap);
        String code = manager.execute(); 
        
        printScheduleMessage(code);
    }
}
