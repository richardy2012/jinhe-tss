package com.jinhe.dm.record.ddl;

import java.util.Map;

import com.jinhe.dm.data.sqlquery.SQLExcutor;
import com.jinhe.dm.record.Record;
import com.jinhe.tss.util.EasyUtils;

public class _Oracle extends _Database {
	
	public _Oracle(Record record) {
		super(record);
	}
	
	public void createTable() {
		if(this.fields.isEmpty()) return;
 
		StringBuffer createDDL = new StringBuffer("create table " + this.table + " ( ");
   		for(Map<Object, Object> fDefs : fields) {
			String code = (String) fDefs.get("code");
			String type = (String) fDefs.get("type"); // string number date datetime hidden
			String nullable = (String) fDefs.get("nullable");
			String _height = (String) fDefs.get("height");
			
			createDDL.append( code ); 
			if("number".equals(type)) {
				createDDL.append( " float " ); 
			} else if("datetime".equals(type)) {
				createDDL.append( " date " ); 
			} else if("date".equals(type)) {
				createDDL.append( " date " ); 
			}
			else {
				int length = 255;
				if( !EasyUtils.isNullOrEmpty(_height) ) {
					length = Math.max(1, Integer.parseInt(_height.replace("px", ""))/18) * 255;
				}
				createDDL.append( " varchar(" + length + ") " ); 
			}
			
			if("false".equals(nullable)) {
				createDDL.append( " NOT NULL " ); 
			}
			createDDL.append( ", " );
   		}
   		
   		createDDL.append("createtime date NOT NULL, ");
		createDDL.append("creator varchar(50) NOT NULL, ");
		createDDL.append("updatetime date null, ");
		createDDL.append("updator varchar(50), ");
		createDDL.append("version NUMBER(5), ");
   		createDDL.append("id NUMBER(11) not null");
   		createDDL.append( ")" );
   		
   		SQLExcutor.excute(createDDL.toString(), datasource);	
   		SQLExcutor.excute("alter table " + this.table + " add primary key (id)", datasource);	
   		
		try {
			SQLExcutor.excute("create sequence " + getSeq() + " increment by 1 start with 1", datasource); 
		} catch(Exception e) {
		}
	}
	
	private String getSeq() {
		return this.table + "_seq";
	}
	
	protected String createInsertSQL(String valueTags, String fieldTags) {
		String insertSQL = "insert into " + this.table + "(" + fieldTags + "createtime,creator,version,id) " +
				" values (" + valueTags + " ?, ?, ?, " + getSeq() + ".nextval)";
		return insertSQL;
	}
}
