package com.jinhe.tss.um.entity.permission;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.permission.AbstractResourcesView;

/**
 * 主用户组资源视图
 * </p>
 */
@Entity
@Table(name = "view_group_resources")
public class GroupResources extends AbstractResourcesView {
 
    public String getResourceType() {
		return UMConstants.GROUP_RESOURCE_TYPE_ID;
	}
}
