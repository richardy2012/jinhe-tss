package com.jinhe.tss.demo.resource;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.tss.um.permission.AbstractResource;

/** 
 * 数据报表资源视图 
 */
@Entity
@Table(name = "view_xx_resource")
public class XXResource extends AbstractResource {

	public String getResourceType() {
		return XX.RESOURCE_TYPE;
	}
}