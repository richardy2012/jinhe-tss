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

	public UserRoleDTO() {
	}

	public UserRoleDTO(User user, Role role) {
		this.userId = user.getId();
		this.userName = user.getUserName();
		this.roleId = role.getId();
		this.roleName = role.getName();
		this.description = role.getDescription();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public GridAttributesMap getAttributes(GridAttributesMap map) {
		map.put("userId", userId);
		map.put("roleId", roleId);
		map.put("userName", userName);
		map.put("roleName", roleName);
		map.put("description", description);
		return map;
	}

	public boolean equals(Object obj) {
		if (obj instanceof UserRoleDTO) {
			UserRoleDTO object = (UserRoleDTO) obj;
			return this.userId.equals(object.getUserId())
					&& this.roleId.equals(object.getRoleId());
		}
		return false;
	}

	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + this.roleId.hashCode();
		hash = hash * 31 + this.userId.hashCode();

		return hash;
	}
}
