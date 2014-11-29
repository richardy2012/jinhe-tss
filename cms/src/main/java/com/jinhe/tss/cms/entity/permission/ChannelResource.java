package com.jinhe.tss.cms.entity.permission;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.um.permission.AbstractResource;

/** 
 * 站点栏目资源视图 
 */
@Entity
@Table(name = "view_channel_resource")
public class ChannelResource extends AbstractResource {

	public String getResourceType() {
		return CMSConstants.RESOURCE_TYPE_CHANNEL;
	}
}

