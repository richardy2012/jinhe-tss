package com.jinhe.tss.portal.entity.permission;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.permission.AbstractSuppliedTable;

/** 
 * 门户结构授权表(补全的表)
 */
@Entity
@Table(name = "pms_permissionFull_Portal")
public class PortalPermissionsFull extends AbstractSuppliedTable {
    
}

