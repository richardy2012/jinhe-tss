package com.jinhe.tss.portal.entity.permission;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.permission.AbstractUnSuppliedTable;

/** 
 * 导航栏、权限选项、角色关联对象(未补全的表)
 */
@Entity
@Table(name = "portal_permission_navigator")
public class NavigatorPermissions extends AbstractUnSuppliedTable {
    
}

