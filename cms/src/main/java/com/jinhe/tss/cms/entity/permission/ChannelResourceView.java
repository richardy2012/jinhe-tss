package com.jinhe.tss.cms.entity.permission;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.um.permission.AbstractResourcesView;

/** 
 * 站点栏目资源视图 
 */
@Entity
@Table(name = "view_channel_resources")
public class ChannelResourceView extends AbstractResourcesView {

	public String getResourceType() {
		return CMSConstants.RESOURCE_TYPE_CHANNEL;
	}
}

