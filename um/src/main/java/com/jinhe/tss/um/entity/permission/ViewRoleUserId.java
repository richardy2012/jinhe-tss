package com.jinhe.tss.um.entity.permission;

import java.io.Serializable;

import javax.persistence.Embeddable;
 
@Embeddable
public class ViewRoleUserId implements Serializable {

	private static final long serialVersionUID = -1721497183867041324L;

	private Long userId; // 用户id
	private Long roleId; // 角色id
 
	public Long getRoleId() {
		return roleId;
	}
 
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean equals(Object obj) {
		ViewRoleUserId object = (ViewRoleUserId) obj;
		return this.roleId.equals(object.getRoleId())
				&& this.userId.equals(object.getUserId());
	}

	public int hashCode() {
		return (this.roleId + "_" + this.userId).hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("roleId=").append(roleId).append(
				",userId=").append(userId);
		return sb.toString();
	}
}

	