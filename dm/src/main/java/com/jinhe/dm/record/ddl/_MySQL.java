package com.jinhe.dm.record.ddl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Document;

import com.jinhe.dm.data.sqlquery.SQLExcutor;
import com.jinhe.dm.record.Record;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.XMLDocUtil;

public class _MySQL implements _Database {
	
	String recordName;
	String datasource;
	String table;
	
	List<Map<Object, Object>> fields;
	List<String> fieldCodes;
	List<String> fieldTypes;
	List<String> fieldNames;
	
	String customizeSQL;
	
	public _MySQL(Record record) {
		this.recordName = record.getName();
		this.datasource = record.getDatasource();
		this.table = record.getTable();
		this.fields = parseJson(record.getDefine());
		this.customizeSQL = record.getCustomizeSQL();
		initFieldCodes();
	}
	
	private void initFieldCodes() {
		this.fieldCodes = new ArrayList<String>();
		this.fieldTypes = new ArrayList<String>();
		this.fieldNames = new ArrayList<String>();
		for(Map<Object, Object> fDefs : this.fields) {
			this.fieldCodes.add((String) fDefs.get("code"));
			this.fieldTypes.add((String) fDefs.get("type"));
			this.fieldNames.add((String) fDefs.get("label"));
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<Map<Object, Object>> parseJson(String define) {
		if(EasyUtils.isNullOrEmpty(define)) {
			return new ArrayList<Map<Object,Object>>();
		}
		
		define = define.replaceAll("'", "\"");
		
		try {  
   			List<Map<Object, Object>> list = new ObjectMapper().readValue(define, List.class);  
   			for(int i = 0; i < list.size(); i++) {
   	        	Map<Object, Object> fDefs = list.get(i);
   	        	int index = i + 1;
   	        	
   				String code = (String) fDefs.get("code");
   				code = (EasyUtils.isNullOrEmpty(code) ? "f" + index : code);
   				fDefs.put("code", code);
   			}
   			return list;
   	    } 
   		catch (Exception e) {  
   	        throw new BusinessException("数据录入【" + recordName + "】的参数配置有误，要求为标准JSON格式。", e);
   	    } 
	}
	
	public List<Map<Object, Object>> getFields() {
		return this.fields;
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
   		
   		createDDL.append("`createtime` TIMESTAMP NOT NULL, ");
		createDDL.append("`creator` varchar(50) NOT NULL, ");
		createDDL.append("`updatetime` TIMESTAMP, ");
		createDDL.append("`updator` varchar(50), ");
		createDDL.append("`version` int(5), ");
   		createDDL.append("`id` int(11) not null AUTO_INCREMENT, ");
   		createDDL.append( "primary key (id))" );
   		
   		SQLExcutor.excute(createDDL.toString(), datasource);		
	}

	public void updateTable(Record _new) {
		String datasource = _new.getDatasource();
		String table = _new.getTable();
		this.customizeSQL = _new.getCustomizeSQL();
		
		if(!datasource.equals(this.datasource) || !table.equals(this.table)) {
			this.datasource = datasource;
			this.table = table;
			createTable();
			return;
		}
		
		// 比较新旧字段定义的异同（新增的和删除的，暂时只关心新增的）
		List<Map<Object, Object>> newFields = parseJson(_new.getDefine());
		List<Map<Object, Object>> addFields = new ArrayList<Map<Object,Object>>();
				
		int index = 0;
		for(Map<Object, Object> fDefs1 : newFields) {
			String code = (String) fDefs1.get("code");
			code = (EasyUtils.isNullOrEmpty(code) ? "f" + (++index) : code);
			fDefs1.put("code", code);
			
			boolean exsited = false;
        	for(Map<Object, Object> fDefs2 : this.fields ) {
        		if(code.equals(fDefs2.get("code"))) {
        			exsited = true; 
        			//TODO 进一步判断字段类型及长度，及是否可空等有无发生变化
        		}
        	}
        	
        	if( !exsited ) {
        		addFields.add(fDefs1);
        	}
		}
		
		// 如果原表有数据则重命名原表，没有则删之，然后新建一张新表
		if(addFields.size() > 0) {
			SQLExcutor ex = new SQLExcutor(false);
			ex.excuteQuery("select id from " + this.table, this.datasource);
			if(ex.result.size() == 0 ) {
				SQLExcutor.excute("drop table if exists " + this.table, datasource);
			}
			else {
				String oldTable = this.table + "_old";
				SQLExcutor.excute("drop table if exists " + oldTable, datasource);
				SQLExcutor.excute("alter table " + this.table + " rename to " + oldTable, datasource);
			}
			
			this.fields = newFields;
			initFieldCodes();
			
			createTable();
		}
	}
	
	public void insert(Map<String, String> valuesMap) {
		Map<Integer, Object> paramsMap = new HashMap<Integer, Object>();
		int index = 0;
		String valueTags = "", fieldTags = "";
		for(String field : this.fieldCodes) {
			Object value = _Util.preTreatValue(valuesMap.get(field), fieldTypes.get(index));
			paramsMap.put(++index, value);
			valueTags += "?,";
			fieldTags += field + ",";
		}
		paramsMap.put(++index, new Date());
		paramsMap.put(++index, Environment.getUserCode());
		paramsMap.put(++index, 0);
		
		String insertSQL = "insert into " + this.table + "(" + fieldTags + "createtime,creator,version) " +
				" values (" + valueTags + " ?, ?, ?)";
		SQLExcutor.excute(insertSQL, paramsMap, this.datasource);
	}

	public void update(Integer id, Map<String, String> valuesMap) {
		Map<Integer, Object> paramsMap = new HashMap<Integer, Object>();
		int index = 0, n = 0;
		String tags = "";
		for(String field : this.fieldCodes) {
			if( valuesMap.containsKey(field) ) {
				Object value = _Util.preTreatValue(valuesMap.get(field), fieldTypes.get(n));
				paramsMap.put(++index, value);
				tags += field + "=?, ";
			}
			n++;
		}
		paramsMap.put(++index, new Date());
		paramsMap.put(++index, Environment.getUserCode());
		paramsMap.put(++index, id);
		
		String updateSQL = "update " + this.table + " set " + tags + "updatetime=?, updator=?, version=version+1 where id=?";
		SQLExcutor.excute(updateSQL, paramsMap, this.datasource);
	}

	public void delete(Integer id) {
		String updateSQL = "delete from " + this.table + " where id=" + id;
		SQLExcutor.excute(updateSQL, this.datasource);
	}
	
	public List<Map<String, Object>> select() {
		 return this.select(1, 100);
	}

	public List<Map<String, Object>> select(int page, int pagesize) {
		Map<Integer, Object> paramsMap = new HashMap<Integer, Object>();
		paramsMap.put(1, Environment.getUserCode());
		
		String selectSQL = !EasyUtils.isNullOrEmpty(this.customizeSQL) ? this.customizeSQL :
				"select " + EasyUtils.list2Str(this.fieldCodes) + 
					",createtime,creator,updatetime,updator,version,id from " + this.table + " where creator = ?";
		
		SQLExcutor ex = new SQLExcutor(false);
		ex.excuteQuery(selectSQL, paramsMap, page, pagesize, this.datasource);
		
		return ex.result;
	}

	public Document getGridTemplate() {
		StringBuffer sb = new StringBuffer();
        sb.append("<grid><declare sequence=\"true\">");
        
        int index = 0; 
        for(String filed : fieldNames) {
            sb.append("<column name=\"" + fieldCodes.get(index++) + "\" mode=\"string\" caption=\"" + filed + "\" />");
        }
        sb.append("<column name=\"createtime\" mode=\"string\" caption=\"创建时间\" />");
        sb.append("<column name=\"creator\" mode=\"string\" caption=\"创建人\" />");
        sb.append("<column name=\"updatetime\" mode=\"string\" caption=\"最后更新时间\" />");
        sb.append("<column name=\"updator\" mode=\"string\" caption=\"最后更新人\" />");
        sb.append("<column name=\"version\" mode=\"string\" caption=\"修改次数\" />");
        sb.append("<column name=\"id\" display=\"none\"/>");
        
        sb.append("</declare><data></data></grid>");
        
    	return XMLDocUtil.dataXml2Doc(sb.toString());
	}
}
