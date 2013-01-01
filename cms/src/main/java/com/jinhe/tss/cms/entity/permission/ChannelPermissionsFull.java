package com.jinhe.tss.cms.entity.permission;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.permission.AbstractSuppliedTable;

/** 
 * 站点栏目资源权限补齐表
 */
@Entity
@Table(name = "cms_permissionFull_Channel")
public class ChannelPermissionsFull extends AbstractSuppliedTable {

}

