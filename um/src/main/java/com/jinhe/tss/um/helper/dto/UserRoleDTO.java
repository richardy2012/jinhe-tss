package com.jinhe.tss.um.helper.dto;

import com.jinhe.tss.framework.web.dispaly.grid.GridAttributesMap;
import com.jinhe.tss.framework.web.dispaly.grid.IGridNode;
import com.jinhe.tss.um.entity.Role;
import com.jinhe.tss.um.entity.User;

public class UserRoleDTO implements IGridNode {
    
	public Long userId;
	public Long roleId;
	public String userName;
	public String roleName;
	public String description;
 
	public UserRoleDTO(User user, Role role) {
		this.userId = user.getId();
		this.userName = user.getUserName();
		this.roleId = role.getId();
		this.roleName = role.getName();
		this.description = role.getDescription();
	}

	public GridAttributesMap getAttributes(GridAttributesMap map) {
		map.put("userId", userId);
        map.put("roleId", roleId);
        map.put("userName", userName);
        map.put("roleName", roleName);
        map.put("description", description);
		return map;
	}
	
}
