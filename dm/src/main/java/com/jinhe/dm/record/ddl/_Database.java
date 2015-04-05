package com.jinhe.dm.record.ddl;

import java.util.Map;

import com.jinhe.dm.record.Record;

public interface _Database {
	
	void createTable();
	
	void updateTable(Record _new);
	
	void insert(Map<String, String> valuesMap);
	
	void update(Integer id, Map<String, String> valuesMap);
	
	void delete(Integer id);
}
