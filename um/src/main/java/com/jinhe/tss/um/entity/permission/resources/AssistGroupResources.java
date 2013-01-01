package com.jinhe.tss.um.entity.permission.resources;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.permission.AbstractResourcesView;

/**
 * 辅助用户组资源视图
 */
@Entity
@Table(name = "view_AssistGroup_resources")
public class AssistGroupResources extends AbstractResourcesView {

    public String getResourceType() {
		return UMConstants.ASSISTANTGROUP_RESOURCE_TYPE_ID;
	}

}
