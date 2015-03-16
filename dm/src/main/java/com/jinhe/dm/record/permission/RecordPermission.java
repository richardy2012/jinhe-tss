package com.jinhe.dm.record.permission;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.permission.AbstractPermission;

/** 
 * 数据录入权限
 */
@Entity
@Table(name = "dm_permission_record")
public class RecordPermission extends AbstractPermission {

}