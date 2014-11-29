package com.jinhe.tss.um.entity.permission;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.permission.AbstractPermission;

/**
 * 角色主用户组资源操作表(补全的表) 
 */
@Entity
@Table(name = "um_permission_group")
public class GroupPermission extends AbstractPermission {

}
