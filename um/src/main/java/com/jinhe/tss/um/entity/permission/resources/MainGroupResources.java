package com.jinhe.tss.um.entity.permission.resources;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.permission.AbstractResourcesView;

/**
 * 主用户组资源视图
 * </p>
 */
@Entity
@Table(name = "view_MAINGROUP_resourcs")
public class MainGroupResources extends AbstractResourcesView {
 
    public String getResourceType() {
		return UMConstants.MAINGROUP_RESOURCE_TYPE_ID;
	}
}
