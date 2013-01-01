package com.jinhe.tss.um.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.dao.IGroupDao;
import com.jinhe.tss.um.dao.IRoleDao;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.entity.RoleGroup;
import com.jinhe.tss.um.entity.RoleUser;
import com.jinhe.tss.um.entity.Strategy;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.service.IStrategyService;
import com.jinhe.tss.util.EasyUtils;

public class StrategyService implements IStrategyService {

	@Autowired private IRoleDao  roleDao;
	@Autowired private IGroupDao groupDao;	

	public void deleteStrategy(Long id) {
		roleDao.deleteStrategy((Strategy) roleDao.getEntity(Strategy.class, id));
	}

	public void disable(Long id, Integer disabled) {
		Strategy strategy = (Strategy) roleDao.getEntity(Strategy.class, id);
		if(UMConstants.FALSE.equals(disabled) && strategy.getEndDate().getTime() < System.currentTimeMillis()) {
			throw new BusinessException(strategy.getName() + " 已过期，不能启用");
		}
		strategy.setDisabled(disabled);
		roleDao.update(strategy);
	}

	public List<User> getUsersByGroupId(Long groupId) {
		return groupDao.getUsersByGroupId(groupId);
	}

	public Map<String, Object> getStrategyInfo4Create() {
	    Long operatorId = Environment.getOperatorId();
	    
		List<Object> groups = new ArrayList<Object>();
		groups.addAll(groupDao.getGroupsByType(Group.MAIN_GROUP_TYPE, operatorId)); // 主用户组
		groups.addAll(groupDao.getGroupsByType(Group.ASSISTANT_GROUP_TYPE, operatorId)); // 辅助用户组
		
        Map<String, Object> map = new HashMap<String, Object>();
		map.put("Rule2GroupTree", groups);
		map.put("Rule2UserTree", groupDao.getVisibleMainUsers(operatorId));
		map.put("Rule2RoleTree", roleDao.getSubAuthorizeableRoles(operatorId));
		return map;
	}

	public Map<String, Object> getStrategyInfo4Update(Long strategyId) {
	    Long operatorId = Environment.getOperatorId();
	    
        Map<String, Object> map = new HashMap<String, Object>();
		map.put("RuleInfo", roleDao.getEntity(Strategy.class, strategyId));
		map.put("Rule2RoleTree", roleDao.getSubAuthorizeableRoles(operatorId));
		map.put("Rule2GroupTree", groupDao.getGroupsByType(Group.MAIN_GROUP_TYPE, operatorId)); // 主用户组
		map.put("Rule2GroupExistTree", roleDao.getGroupsByStrategy(strategyId));
		map.put("Rule2UserExistTree",  roleDao.getUsersByStrategy(strategyId));
		map.put("Rule2RoleExistTree",  roleDao.getRolesByStrategy(strategyId));
		
		return map;
	}

	public List<?> getStrategyByCreator() {
	    return roleDao.getEntities("from Strategy o where o.creatorId = ?" , Environment.getOperatorId());
	}

	public void saveStrategy(Strategy strategy, String userIds, String groupIds, String roleIds) {
		if(strategy.getId() == null) {
		    roleDao.createObject(strategy);
		} 
		else {
		    roleDao.update(strategy);
		}
        
		// 在角色用户关系表中保存 策略对用户，策略对角色的信息 在角色用户组关系表中保存 策略对用户组，策略对角色的信息
        saveRule2Group(strategy, roleIds, groupIds);
        saveRule2User(strategy, roleIds, userIds);
	}
    
    /**
     * <p>
     * 在角色用户关系表中保存 策略对用户，策略对角色的信息。
     * 策略可以授予用户、用户组、也可以授予角色，或者三者兼有。
     * </p>
     * @param strategy
     * @param roleIdsStr
     * @param userIdsStr
     */
    private void saveRule2User(Strategy strategy, String roleIdsStr, String userIdsStr) {
        List<?> roleUsers = roleDao.getRoleUserByStrategy(strategy.getId());
        Map<String, RoleUser> historyMap = new HashMap<String, RoleUser>(); // 把老的转授记录放入一个map, 以"roleId_userId"为key
        for (Object temp : roleUsers) { 
            RoleUser roleUser = (RoleUser) temp;
            historyMap.put(roleUser.getRoleId() + "_" + roleUser.getUserId(), roleUser);
        }
        
        String[] roleIds = EasyUtils.isNullOrEmpty(roleIdsStr) ? new String[0] : roleIdsStr.split(",");
        String[] userIds = EasyUtils.isNullOrEmpty(userIdsStr) ? new String[0] : userIdsStr.split(",");
 
        if (userIds.length > 0 && roleIds.length > 0) {
            for (String roleId : roleIds) {
                for (String userId : userIds) {
                    saveRoleUser(historyMap, roleId, userId, strategy);
                }
            }
        } else if (userIds.length == 0) {
            for (String roleId : roleIds) {
                saveRoleUser(historyMap, roleId, null, strategy);
            }
        }
        else if (roleIds.length == 0) {
            for (String userId : userIds) {
                saveRoleUser(historyMap, null, userId, strategy);
            }
        }
        
        //老的转授记录中剩下的就是该删除的了
        roleDao.deleteAll(historyMap.values());
    }
    
    /** roleId, userId 之一有可能为null */
    private void saveRoleUser(Map<String, RoleUser> historyMap, String roleId, String userId, Strategy strategy){
        // 如果老的转授记录里面有，则从历史记录中移出
        RoleUser roleUser = historyMap.remove(roleId + "_" + userId); 
        
        //如果老的转授记录里面没有，则新增
        if (roleUser == null) { 
            roleUser = new RoleUser();
            roleUser.setRoleId(Long.valueOf(roleId));
            roleUser.setUserId(Long.valueOf(userId));
            roleUser.setStrategyId(strategy.getId());
            roleDao.createObject(roleUser);
        } 
    }

	/**
	 * <p>
	 * 在角色用户组关系表中保存 策略对用户组，策略对角色的信息
	 * </p>
	 * @param strategy
	 * @param roleIdsStr
	 * @param groupIdsStr
	 */
	private void saveRule2Group(Strategy strategy, String roleIdsStr, String groupIdsStr) {
		List<?> roleGroups = roleDao.getRoleGroupByStrategy(strategy.getId());
		Map<String, RoleGroup> historyMap = new HashMap<String, RoleGroup>(); // 把老的转授记录做成一个map， 以"roleId_groupId"为key
		 for (Object temp : roleGroups) { 
			RoleGroup roleGroup = (RoleGroup) temp;
			historyMap.put(roleGroup.getRoleId() + "_" + roleGroup.getGroupId(), roleGroup);
		}
		 
        String[] roleIds  = EasyUtils.isNullOrEmpty(roleIdsStr) ? new String[0] : roleIdsStr.split(",");
        String[] groupIds = EasyUtils.isNullOrEmpty(groupIdsStr) ? new String[0] : groupIdsStr.split(",");
        
        if (groupIds.length > 0 && roleIds.length > 0) {
            for (String roleId : roleIds) {
                for (String groupId : groupIds) {
                    saveRoleGroup(historyMap, roleId, groupId, strategy);
                }
            }
        } else if (groupIds.length == 0) {
            for (String roleId : roleIds) {
                saveRoleGroup(historyMap, roleId, null, strategy);
            }
        }
        else if (roleIds.length == 0) {
            for (String groupId : groupIds) {
                saveRoleGroup(historyMap, null, groupId, strategy);
            }
        }
 
        // 老的转授记录中剩下的就是该删除的了
        roleDao.deleteAll(historyMap.values());
	}
    
    /** roleId, groupId 之一有可能为null */
    private void saveRoleGroup(Map<String, RoleGroup> historyMap, String roleId, String groupId, Strategy strategy){
        // 如果老的转授记录里面有，则从老的转授记录中移出
        RoleGroup roleGroup = historyMap.remove(roleId + "_" + groupId); 
        
        //如果老的转授记录里面没有，则新增
        if (roleGroup == null) { 
            roleGroup = new RoleGroup();
            roleGroup.setRoleId(Long.valueOf(roleId));
            roleGroup.setGroupId(Long.valueOf(groupId));
            roleGroup.setStrategyId(strategy.getId());
            roleDao.createObject(roleGroup);
        } 
    }
}
