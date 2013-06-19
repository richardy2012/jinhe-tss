package com.jinhe.tss.um.entity.permission.unsupplied;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.permission.AbstractUnSuppliedTable;

/**
 * 角色、主用户组与权限选项关联对象(未补全的表)
 */
@Entity
@Table(name = "um_permission_group")
public class GroupPermissions extends AbstractUnSuppliedTable {

	public Long getParentResourceId() {
		return UMConstants.MAIN_GROUP_ID;
	}
}
