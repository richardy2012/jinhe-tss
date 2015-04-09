package com.jinhe.dm.record.ddl;

import java.util.Map;

import com.jinhe.dm.data.sqlquery.SQLExcutor;
import com.jinhe.dm.record.Record;
import com.jinhe.tss.util.EasyUtils;

public class _MySQL extends _Database {
	
	public _MySQL(Record record) {
		super(record);
	}
	
	public void createTable() {
		if(this.fields.isEmpty()) return;
		
		StringBuffer createDDL = new StringBuffer("create table if not exists " + table + " ( ");
   		for(Map<Object, Object> fDefs : fields) {
			String code = (String) fDefs.get("code");
			String type = (String) fDefs.get("type"); // string number date datetime hidden
			String nullable = (String) fDefs.get("nullable");
			String _height = (String) fDefs.get("height");
			
			createDDL.append( "`" + code + "` " ); 
			if("number".equals(type)) {
				createDDL.append( " float " ); 
			} else if("datetime".equals(type)) {
				createDDL.append( " datetime " ); 
			} else if("date".equals(type)) {
				createDDL.append( " date " ); 
			}
			else {
				int height = 255;
				if( !EasyUtils.isNullOrEmpty(_height) ) {
					height = Math.max(1, Integer.parseInt(_height.replace("px", ""))/18) * 255;
				}
				createDDL.append( " varchar(" + height + ") " ); 
			}
			
			if("false".equals(nullable)) {
				createDDL.append( " NOT NULL " ); 
			}
			createDDL.append( ", " );
   		}
   		
   		createDDL.append("`createtime` TIMESTAMP NOT NULL, ");
		createDDL.append("`creator` varchar(50) NOT NULL, ");
		createDDL.append("`updatetime` TIMESTAMP null, ");
		createDDL.append("`updator` varchar(50), ");
		createDDL.append("`version` int(5), ");
   		createDDL.append("`id` int(11) not null AUTO_INCREMENT, ");
   		createDDL.append( "primary key (id))" );
   		
   		SQLExcutor.excute(createDDL.toString(), datasource);		
	}

	public void dropTable(String table, String datasource) {
		SQLExcutor.excute("drop table if exists " + this.table, datasource);
	}
}
