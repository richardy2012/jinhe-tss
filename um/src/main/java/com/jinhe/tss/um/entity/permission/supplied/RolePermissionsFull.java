package com.jinhe.tss.um.entity.permission.supplied;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.permission.AbstractSuppliedTable;

/**
 * 角色应用资源操作表(补全的表)
 */
@Entity
@Table(name = "um_permissionFull_role")
public class RolePermissionsFull extends AbstractSuppliedTable {

}
