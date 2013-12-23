package com.jinhe.tss.um.entity.permission;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.permission.AbstractResourcesView;

/**
 * 角色资源视图
 */
@Entity
@Table(name = "view_role_resources")
public class RoleResources extends AbstractResourcesView {

    public String getResourceType() {
		return UMConstants.ROLE_RESOURCE_TYPE_ID;
	}
}
