package com.jinhe.tss.um.dao.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jinhe.tss.framework.component.param.ParamConstants;
import com.jinhe.tss.framework.persistence.TreeSupportDao;
import com.jinhe.tss.um.dao.IRoleDao;
import com.jinhe.tss.um.entity.Role;
import com.jinhe.tss.um.entity.SubAuthorize;
 
@Repository("RoleDao")
public class RoleDao extends TreeSupportDao<Role> implements IRoleDao {
	
	public RoleDao() {
		super(Role.class);
	}

	public Role moveRole(Role role) {
		return create(role);
	}
    
    public void removeRole(Role role){
        List<?> roles = getChildrenById(role.getId());
        for(Iterator<?> it = roles.iterator(); it.hasNext(); ){
            Role temp = (Role) it.next();
            if(ParamConstants.FALSE.equals(role.getIsGroup())){
                Long roleId = temp.getId();
                deleteAll(getEntities("from RoleUser  ru where ru.roleId = ? ", roleId));
                deleteAll(getEntities("from RoleGroup rg where rg.roleId = ? ", roleId));
                
                //-- 补全表 --
                deleteAll(getEntities("from GroupPermissionsFull   where roleId = ? ", roleId));
                deleteAll(getEntities("from RolePermissionsFull    where roleId = ? ", roleId));
                
                //-- 未全表 --
                deleteAll(getEntities("from GroupPermissions   where roleId = ? ", roleId));
                deleteAll(getEntities("from RolePermissions    where roleId = ? ", roleId));
                
                // TODO Portal、CMS、其他基于平台应用的相关授权也需一并删除
            }
        }
        deleteAll(roles);
    }
 
	public List<?> getUsersByRoleId(Long roleId) {
		String hql = "select distinct u from RoleUser ru, User u where ru.id.userId = u.id and ru.id.roleId = ? and ru.strategyId is null ";
		return getEntities( hql, roleId );
	}

	public List<?> getGroupsByRoleId(Long roleId) {
		String hql = "select distinct g from RoleGroup rg, Group g where rg.groupId = g.id and rg.roleId = ? and rg.strategyId is null order by g.decode";
		return getEntities( hql, roleId );
	}
 
	public List<?> getEditableRoles() {
	    return getEntities("from Role r where r.id > 0 order by r.decode");      
	}
 
	// ===========================================================================================================
    // 按策略转授角色的相关数据库操作
    // ===========================================================================================================

    public void deleteStrategy(SubAuthorize strategy) {
        delete(strategy);
        
        //清除RoleUser， RoleGroup中的记录
        deleteAll(getRoleUserByStrategy(strategy.getId()));
        deleteAll(getRoleGroupByStrategy(strategy.getId()));
    }

    public List<?> getRoleUserByStrategy(Long strategyId){
        return getEntities("from RoleUser o where o.strategyId = ?", strategyId);
    }
    
    public List<?> getRoleGroupByStrategy(Long strategyId){
        return getEntities("from RoleGroup o where o.strategyId = ?", strategyId);  
    }
    
    public List<?> getUsersByStrategy(Long strategyId) {
        String hql = "select distinct u from RoleUser ru, User u where ru.id.userId = u.id and ru.strategyId = ? ";
        return getEntities(hql, strategyId);
    }

    public List<?> getGroupsByStrategy(Long strategyId) {
        String hql = "select distinct g from RoleGroup rg, Group g " +
        		" where rg.groupId = g.id and rg.strategyId = ? order by g.levelNo, g.seqNo";
        return getEntities(hql, strategyId);
    }
    
    public List<?> getRolesByStrategy(Long strategyId) {
        String hql = "select distinct r from RoleUser o, Role r where o.roleId = r.id and o.strategyId = ?";
        return getEntities(hql, strategyId);
    }
    
    public List<?> getSubAuthorizeableRoles(Long userId){
        String hql = "select distinct r from Role r, ViewRoleUser4SubAuthorize ru " +
        		" where r.id = ru.id.roleId and ru.id.userId = ? order by r.decode";
        return getEntities(hql, userId);
    }
    
    // ===========================================================================================================
    // 用户的授权信息变动时，拦截器需要调用来收回转授权限的方法
    // ===========================================================================================================
    public void deleteGroupSubAuthorizeInfo(Long groupId, Long roleId){
        String hql = "select distinct userId from GroupUser where groupId = ? ";
        List<?> userIds = getEntities( hql, groupId );
        
        if( userIds.isEmpty() ) return;
        
        for( Object userId : userIds){
            /* 获取用户非转授所得的角色（用户自身拥有(非转授)的角色），如果目标角色不再其列，则删除该角色 */
            hql = "select r.id from RoleUser ru, Role r where ru.userId = ? and ru.roleId = r.id and ru.strategyId is null";
            if( !getEntities( hql, userId ).contains(roleId) ) {
            	deleteUserSubAuthorizeInfo((Long) userId, roleId);
            }
        }
    }

    // 当用户不再拥有的某个角色，则收回这个用户转授出去的授权信息 
    public void deleteUserSubAuthorizeInfo(Long userId, Long roleId){ 
    	/* 根据创建者获取转授策略ID集合 */
    	List<?> strategyIds = getEntities( "select id from SubAuthorize where creatorId = ? ", userId ); 
        for(Iterator<?> it = strategyIds.iterator(); it.hasNext();){
            Long strategyId = (Long)it.next();
           
            /* 删除角色用户关系 */
            executeHQL( "delete RoleUser r where r.roleId = ? and r.strategyId = ?", roleId, strategyId ); 
        }
    }
}
