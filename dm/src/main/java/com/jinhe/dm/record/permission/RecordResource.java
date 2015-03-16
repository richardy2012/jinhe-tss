package com.jinhe.dm.record.permission;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jinhe.dm.record.Record;
import com.jinhe.tss.um.permission.AbstractResource;

/** 
 * 数据录入资源视图 
 */
@Entity
@Table(name = "view_record_resource")
public class RecordResource extends AbstractResource {

	public String getResourceType() {
		return Record.RESOURCE_TYPE;
	}
}