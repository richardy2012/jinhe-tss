package com.jinhe.tss.cms.entity.permission;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.permission.AbstractUnSuppliedTable;

/** 
 * 站点栏目资源权限未补齐表
 */
@Entity
@Table(name = "cms_permission_channel")
public class ChannelPermissions extends AbstractUnSuppliedTable {

}

