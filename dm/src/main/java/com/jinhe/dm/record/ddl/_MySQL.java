package com.jinhe.dm.record.ddl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.jinhe.dm.data.sqlquery.SQLExcutor;
import com.jinhe.dm.record.Record;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.util.EasyUtils;

public class _MySQL implements _Database {
	
	Record record;
	
	public _MySQL(Record record) {
		this.record = record;
	}

	@SuppressWarnings("unchecked")
	public void createTable() {
		String datasource = record.getDatasource();
		String tableName = record.getTable();
		String tblDefine = record.getDefine().replaceAll("'", "\"");
		
		List<LinkedHashMap<Object, Object>> fields;
   		try {  
			fields = new ObjectMapper().readValue(tblDefine, List.class);  
   	    } catch (Exception e) {  
   	        throw new BusinessException("数据录入【" + "" + "】的参数配置有误，要求为标准JSON格式。", e);
   	    }  
   		
		StringBuffer createDDL = new StringBuffer("create table if not exists " + tableName + " ( ");
   		for(int i = 0; i < fields.size(); i++) {
        	LinkedHashMap<Object, Object> fDefs = fields.get(i);
        	int index = i + 1;
			String code = (String) fDefs.get("code");
			String type = (String) fDefs.get("type"); // string number date datetime hidden
			String nullable = (String) fDefs.get("nullable");
			String _height = (String) fDefs.get("height");
			
			createDDL.append( "`" + (EasyUtils.isNullOrEmpty(code) ? "f" + index : code) + "` " ); 
			if("number".equals(type)) {
				createDDL.append( " float " ); 
			} else if("date".equals(type) || "datetime".equals(type)) {
				createDDL.append( " TIMESTAMP " ); 
			} else {
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
   		
   		createDDL.append("`createtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, ");
		createDDL.append("`creator` varchar(50) NOT NULL, ");
		createDDL.append("`updatetime` TIMESTAMP, ");
		createDDL.append("`updator` varchar(50), ");
		createDDL.append("`version` int(5), ");
   		createDDL.append("`id` int(11) not null AUTO_INCREMENT, ");
   		createDDL.append( "primary key (id))" );
   		
   		SQLExcutor.excute(createDDL.toString(), datasource);		
	}

	public void updateTable(Record _new) {
		
	}

	public void insert(Map<String, String> valuesMap) {
		
	}

	public void update(Integer id, Map<String, String> valuesMap) {
		
	}

	public void delete(Integer id) {
		
	}
}
