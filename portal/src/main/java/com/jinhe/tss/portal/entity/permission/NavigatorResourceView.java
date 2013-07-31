package com.jinhe.tss.portal.entity.permission;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.um.permission.AbstractResourcesView;

/** 
 * 导航栏资源视图
 */
@Entity
@Table(name = "view_navigator_resources")
public class NavigatorResourceView extends AbstractResourcesView {
    
    public String getResourceType() {
        return PortalConstants.NAVIGATOR_RESOURCE_TYPE;
    }
}

