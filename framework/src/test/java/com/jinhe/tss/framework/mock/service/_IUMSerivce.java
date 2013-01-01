package com.jinhe.tss.framework.mock.service;

import java.util.List;

import com.jinhe.tss.framework.component.log.Logable;
import com.jinhe.tss.framework.mock._UMCondition;
import com.jinhe.tss.framework.mock.model._Group;
import com.jinhe.tss.framework.mock.model._GroupRole;
import com.jinhe.tss.framework.mock.model._Role;
import com.jinhe.tss.framework.mock.model._User;

public interface _IUMSerivce {
    
    @Logable(operateTable="用户", operateType="新增", operateInfo="新建了用户：${args[0]?default(\"\")}")
    void createUser(_User user);

    void deleteUser(_User user);

    void updateUser(_User user);

    List<_User> queryAllUsers();
    
    _User getUser(Long id);
    
    @Logable(operateTable="用户组", operateType="新增", operateInfo="新建了用户组：${returnVal?default(\"\")}")
    _Group createGroup(_Group group);

    void deleteGroup(_Group group);

    void updateGroup(_Group group);

    List<_Group> queryAllGroups();
    
    List<_Group> queryGroups(String hql, Object...args);
    
    _Group getGroup(Long id);
    
    
    void createRole(_Role role);

    void deleteRole(_Role role);

    void updateRole(_Role role);

    List<_Role> queryAllRoles();
    
    _Role getRole(Long id);
    
    
    void createGroupRole(_GroupRole gr);

    void deleteGroupRole(_GroupRole gr);

    List<_GroupRole> queryGroupRole(_UMCondition condition);
 
    List<_Group> getRelationsNodeWhenSort(Long parentId, Integer sourceOrder, Integer targetOrder);
    List<_Group> getChildrenByDecode(String decode);
    List<_Group> getChildrenById(Long id);
    List<_Group> getParentsById(Long id);
}