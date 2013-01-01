package com.jinhe.tss.um.entity.permission.resources;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.permission.AbstractResourcesView;


/**
 * 应用资源视图 
 */
@Entity
@Table(name = "view_application_resources")
public class ApplicationResources extends AbstractResourcesView {

    public String getResourceType() {
		return UMConstants.APPLICATION_RESOURCE_TYPE_ID;
	}
}