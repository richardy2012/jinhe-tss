package com.jinhe.tss.portal.entity.permission;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.um.permission.AbstractResourcesView;

/** 
 * 菜单资源视图
 */
@Entity
@Table(name = "view_menu_resources")
public class MenuResourceView extends AbstractResourcesView {
    
    public String getResourceType() {
        return PortalConstants.MENU_RESOURCE_TYPE;
    }
}

