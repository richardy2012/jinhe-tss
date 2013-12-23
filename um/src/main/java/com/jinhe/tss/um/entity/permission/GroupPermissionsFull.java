package com.jinhe.tss.um.entity.permission;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.permission.AbstractSuppliedTable;

/**
 * 角色主用户组资源操作表(补全的表) 
 */
@Entity
@Table(name = "um_permissionfull_group")
public class GroupPermissionsFull extends AbstractSuppliedTable {

}
