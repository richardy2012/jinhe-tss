package com.jinhe.tss.um.entity.permission.unsupplied;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.permission.AbstractUnSuppliedTable;

/**
 * 应用、角色与权限选项关联对象(未补全的表)
 */
@Entity
@Table(name = "um_permission_application")
public class ApplicationPermissions extends AbstractUnSuppliedTable {

	public Long getParentResourceId() {
		return UMConstants.APPLICATION_RESOURCE_ROOT_ID;
	}
}
