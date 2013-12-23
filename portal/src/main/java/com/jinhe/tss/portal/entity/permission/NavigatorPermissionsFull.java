package com.jinhe.tss.portal.entity.permission;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.permission.AbstractSuppliedTable;

/** 
 * 导航栏资源操作表(补全的表)
 */
@Entity
@Table(name = "portal_permissionfull_navigator")
public class NavigatorPermissionsFull extends AbstractSuppliedTable {
    
}

