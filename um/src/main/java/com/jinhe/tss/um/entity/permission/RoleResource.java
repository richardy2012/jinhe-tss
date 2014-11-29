package com.jinhe.tss.um.entity.permission;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.permission.AbstractResource;

/**
 * 角色资源视图
 */
@Entity
@Table(name = "view_role_resource")
public class RoleResource extends AbstractResource {

    public String getResourceType() {
		return UMConstants.ROLE_RESOURCE_TYPE_ID;
	}
}
