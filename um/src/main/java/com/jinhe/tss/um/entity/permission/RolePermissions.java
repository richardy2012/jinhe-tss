package com.jinhe.tss.um.entity.permission;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.permission.AbstractUnSuppliedTable;

/**
 * 角色、角色与权限选项关联对象(未补全的表)
 */
@Entity
@Table(name = "um_permission_role")
public class RolePermissions extends AbstractUnSuppliedTable {

}
