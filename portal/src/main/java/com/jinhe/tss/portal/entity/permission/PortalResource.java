package com.jinhe.tss.portal.entity.permission;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.um.permission.AbstractResource;

/** 
 * 门户结构资源视图
 */
@Entity
@Table(name = "view_portal_resource")
public class PortalResource extends AbstractResource {
    
    public String getResourceType() {
        return PortalConstants.PORTAL_RESOURCE_TYPE;
    }

}

