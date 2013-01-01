package com.jinhe.tss.um.entity.permission.resources;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.permission.AbstractResourcesView;

/**
 * 其他用户组资源视图
 */
@Entity
@Table(name = "view_OtherGroup_resources")
public class OtherGroupResources extends AbstractResourcesView {

    public String getResourceType() {
		return UMConstants.OTHERAPPGROUP_RESOURCE_TYPE_ID;
	}
}
