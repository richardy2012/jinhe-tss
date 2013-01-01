package com.jinhe.tss.portal.entity.permission;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.permission.AbstractSuppliedTable;

/** 
 * 角色应用资源操作表(补全的表)
 */
@Entity
@Table(name = "pms_permissionFull_menu")
public class MenuPermissionsFull extends AbstractSuppliedTable {
    
}

