package com.jinhe.dm.record.ddl;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;

import com.jinhe.dm.record.Record;

public interface _Database {
	
	void createTable();
	
	void updateTable(Record _new);
	
	void insert(Map<String, String> valuesMap);
	
	void update(Integer id, Map<String, String> valuesMap);
	
	void delete(Integer id);
	
	List<Map<String, Object>> select();
	
	List<Map<String, Object>> select(int page, int pagesize);

	Document getGridTemplate();
	
	List<Map<Object, Object>> getFields();
}
