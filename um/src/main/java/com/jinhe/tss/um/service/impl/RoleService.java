package com.jinhe.tss.um.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinhe.tss.framework.component.param.ParamConstants;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.dao.IGroupDao;
import com.jinhe.tss.um.dao.IResourceTypeDao;
import com.jinhe.tss.um.dao.IRoleDao;
import com.jinhe.tss.um.entity.Role;
import com.jinhe.tss.um.entity.RoleGroup;
import com.jinhe.tss.um.entity.RoleUser;
import com.jinhe.tss.um.permission.ResourcePermission;
import com.jinhe.tss.um.service.IRoleService;
import com.jinhe.tss.util.EasyUtils;
 
@Service("RoleService")
public class RoleService implements IRoleService {

	@Autowired private IRoleDao roleDao;
	@Autowired private IGroupDao groupDao; 
	@Autowired private IResourceTypeDao resourceTypeDao;
	@Autowired private ResourcePermission resourcePermission;
    
    public Role getRoleById(Long id) {
        return (Role) roleDao.getEntity(id);
    }
    
    public List<?> getAllVisiableRole() {
        return roleDao.getEntities("from Role r where r.id <> -1 order by r.decode");    
    }

    public List<?> getPlatformApplication() {
		String hql = "from Application o where o.applicationType = ? ";
		return roleDao.getEntities(hql, UMConstants.PLATFORM_SYSTEM_APP);
    }

    public List<?> getResourceTypeByAppId(String applicationId) {
		String hql = "from ResourceType a where a.applicationId = ? order by a.seqNo";
		return resourceTypeDao.getEntities(hql, applicationId);
    }
    
    public List<?> getAddableRoleGroups() {
        return roleDao.getEntities("from Role r where r.id > 0 and r.isGroup = 1 order by r.decode");      
    }
 
	public void delete(Long roleId) {
        // 如果将要操作的数量==能够操作的数量, 说明对所有组都有操作权限 
        String applicationId  = UMConstants.TSS_APPLICATION_ID;
        String resourceTypeId = UMConstants.ROLE_RESOURCE_TYPE_ID;
        String operationId    = UMConstants.ROLE_EDIT_OPERRATION;
        Long operatorId = Environment.getOperatorId();
        List<?> permitedSubNodeIds = resourcePermission.getSubResourceIds(applicationId, resourceTypeId, roleId, operationId, operatorId);
        List<?> allSubNodes = roleDao.getChildrenById(roleId);
		if ( allSubNodes.size() < permitedSubNodeIds.size() ) {
			throw new BusinessException("没有删除角色组权限，不能删除此节点！");
		}
		
		roleDao.removeRole(roleDao.getEntity(roleId));
	}
    
	public void disable(Long id, Integer disabled) {
        String appId = UMConstants.TSS_APPLICATION_ID;
        String resourceTypeId = UMConstants.ROLE_RESOURCE_TYPE_ID;
        Long operatorId = Environment.getOperatorId();
        
        List<Role> list;
        if (disabled.equals(ParamConstants.FALSE)) {  
	        list = roleDao.getParentsById(id); // 启用一个节点上的所有父节点
	        
	        // 如果将要操作的数量 == 能够操作的数量, 说明对所有组都有操作权限
	        String operationId = UMConstants.ROLE_EDIT_OPERRATION;
	        List<?> temp = resourcePermission.getParentResourceIds(appId, resourceTypeId, id, operationId, operatorId);
	        if (temp.size() < list.size()) {
	            throw new BusinessException("节点之上有角色没有启用操作权限，不能启用此节点！");
	        }
		} 
		else { 
            list = roleDao.getChildrenById(id); // 停用一个节点下的所有节点
            
            // 如果将要操作的数量==能够操作的数量, 说明对所有组都有操作权限,则返回true
            String operationId = UMConstants.ROLE_EDIT_OPERRATION;
            List<?> temp = resourcePermission.getSubResourceIds(appId, resourceTypeId, id, operationId, operatorId);
            if (temp.size() < list.size()) {
                throw new BusinessException("节点下有角色没有停用操作权限，不能停用此节点！");
            }
		}
		
        for (Role role : list) {
            if(role.getDisabled().equals(disabled)) continue;
            
            if (ParamConstants.FALSE.equals(disabled) 
                    && (role.getEndDate() != null && role.getEndDate().getTime() < System.currentTimeMillis()) ) {
				throw new BusinessException(role.getName() + " 已过期，不能启用");
            }
			role.setDisabled(disabled);
			roleDao.update(role);
        }
	}
 
	public Map<String, Object> getInfo4CreateNewRole() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Role2UserTree", groupDao.getVisibleMainUsers(Environment.getOperatorId()));
		map.put("Role2GroupTree", getVisibleGroups());
		return map;
	}

	public Map<String, Object> getInfo4UpdateExistRole(Long roleId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("RoleInfo", getRoleById(roleId));
	    map.put("Role2UserTree", groupDao.getVisibleMainUsers(Environment.getOperatorId()));
	    map.put("Role2GroupTree", getVisibleGroups());
		map.put("Role2GroupExistTree", roleDao.getGroupsByRoleId(roleId));
		map.put("Role2UserExistTree", roleDao.getUsersByRoleId(roleId));
		return map;
	}

    private List<?> getVisibleGroups() {
        return groupDao.getMainAndAssistantGroups(Environment.getOperatorId());
    }

	public void move(Long id, Long targetId) {
		Role movedRole = roleDao.getEntity(id);
		
		// 向自己的父节点移动，等于没有移动
		if (targetId.equals(movedRole.getParentId())) return;  
		
		// 节点向自己或者自己的子节点
        if(roleDao.getParentsById(targetId).contains(movedRole)) {
            throw new BusinessException("不能向自己里面的枝节点移动"); 
        }
        
        movedRole.setSeqNo(roleDao.getNextSeqNo(targetId));
        movedRole.setParentId(targetId);
        
        roleDao.moveRole(movedRole); //被拦截调整整个移动枝的decode值, 同时拦截资源补齐调整
        
        // 如果移动到的组是停用状态，那么被移动的组也需要停用
        Role targetRole = roleDao.getEntity(targetId);
        if(targetRole != null && targetRole.getDisabled().equals(ParamConstants.TRUE)) {
            List<?> list = roleDao.getChildrenById(id);
            for (Object temp : list) {
                Role role = (Role) temp;
                role.setDisabled(ParamConstants.TRUE);
            }
        } 
	}

    public Role saveRoleGroup(Role entity) {
        if (entity.getId() == null) {
            entity.setSeqNo(roleDao.getNextSeqNo(entity.getParentId()));
            return roleDao.create(entity);
        }
            
        roleDao.update(entity);
        return entity;
    }

    public void saveRole2UserAndRole2Group(Role role, String userIdsStr, String groupIdsStr) {
        if (role.getId() == null) { // 新建
            if(role.getParentId() == null){
                role.setParentId(UMConstants.ROLE_ROOT_ID);
            }
            role.setSeqNo(roleDao.getNextSeqNo(role.getParentId()));
            role = roleDao.create(role);
        } 
        else {
//            roleDao.update(role);
        }
        
        saveRole2User(role.getId(), userIdsStr);   // 角色对用户
        saveRole2Group(role.getId(), groupIdsStr); // 角色对组
    }

	private void saveRole2Group(Long roleId, String groupIdsStr) {
	    // 根据角色id找到角色-用户组的list（只涉及授权信息，不涉及转授）
		List<?> roleGroups = roleDao.getEntities("from RoleGroup o where o.roleId = ? and o.strategyId is null", roleId);
		Map<Long, RoleGroup> historyMap = new HashMap<Long, RoleGroup>(); // 以"groupId"为key
		for (Object temp : roleGroups) {
		    RoleGroup roleGroup = (RoleGroup) temp;
            historyMap.put(roleGroup.getGroupId(), roleGroup);
        }
 
        if ( !EasyUtils.isNullOrEmpty(groupIdsStr) ) {
            String[] groupIds = groupIdsStr.split(",");
            for (String temp : groupIds) {
                // 如果historyMap里面没有，则新增用户组对用户的关系; 有则从historyMap里remove掉，historyMap剩下的将被delete掉
                Long groupId = Long.valueOf(temp);
                if (historyMap.remove(groupId) == null) { 
                    RoleGroup roleGroup = new RoleGroup();
                    roleGroup.setRoleId(roleId);
                    roleGroup.setGroupId(groupId);
                    roleDao.createObject(roleGroup);
                } 
            }
        }
        
        // historyMap中剩下的就是该删除的了
        roleDao.deleteAll(historyMap.values());
        for(RoleGroup roleGroup : historyMap.values()) {
        	// 判断是否转授出来的
    		if (roleGroup.getStrategyId() == null) {
    			roleDao.deleteGroupSubAuthorizeInfo(roleGroup.getGroupId(), roleGroup.getRoleId());
    		}
        }
	}

	private void saveRole2User(Long roleId, String userIdsStr) {
	    // 根据角色id找到角色-用户的list（只涉及授权信息，不涉及转授
		List<?> roleUsers = roleDao.getEntities("from RoleUser o where o.roleId = ? and o.strategyId is null", roleId);
		Map<Long, RoleUser> historyMap = new HashMap<Long, RoleUser>(); // 以"groupId"为key
        for (Object temp : roleUsers) {
            RoleUser roleUser = (RoleUser) temp;
            historyMap.put(roleUser.getUserId(), roleUser);
        }
 
        if ( !EasyUtils.isNullOrEmpty(userIdsStr) ) {
            String[] userIds = userIdsStr.split(",");
            for (String temp : userIds) {
                // 如果historyMap里面没有，则新增用户组对用户的关系; 有则从historyMap里remove掉，historyMap剩下的将被delete掉
                Long userId = Long.valueOf(temp);
                if (historyMap.remove(userId) == null) { 
                    RoleUser roleUser = new RoleUser();
                    roleUser.setRoleId(roleId);
                    roleUser.setUserId(userId);
                    roleDao.createObject(roleUser);
                } 
            }
        }
		
        // historyMap中剩下的就是该删除的了
        roleDao.deleteAll(historyMap.values());
        for(RoleUser roleUser : historyMap.values()) {
			// 判断是否转授出来的,转授出来的删除后不收回，因为没有二级转授
			if (roleUser.getStrategyId() == null) {
				roleDao.deleteUserSubAuthorizeInfo(roleUser.getUserId(), roleUser.getRoleId());
			}
        }
	}
    
    // ===========================================================================
    // 展示外部资源的授权信息时需要的操作
    // 1.从um中取到当前用户的角色信息
    // ===========================================================================
 
    @SuppressWarnings("unchecked")
	public List<Long[]> getRoles4Permission(){
        String hql = "select distinct t.id.userId, t.id.roleId from ViewRoleUser t where t.id.userId = ?";
        return (List<Long[]>) roleDao.getEntities(hql, Environment.getOperatorId() );  
    }
}
