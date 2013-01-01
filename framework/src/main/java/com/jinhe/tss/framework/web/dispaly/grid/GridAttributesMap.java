package com.jinhe.tss.framework.web.dispaly.grid;

import java.util.HashMap;
import java.util.Map;

/**
 * 放置Grid的属性值。
 * 
 */
public class GridAttributesMap {

	private Map<String, Object> gripAttibutes = new HashMap<String, Object>();

	private String[] columns;

	public GridAttributesMap(String[] columns) {
		this.columns = columns;
	}

	public void put(String key, Object value) {
		gripAttibutes.put(key, value);
	}

	public void putAll(Map<String, Object> map) {
		gripAttibutes.putAll(map);
	}

	public Object[] getValues() {
		Object[] values = new Object[columns.length];
		for (int i = 0; i < columns.length; i++) {
			values[i] = gripAttibutes.get(columns[i]);
		}
		return values;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, Object> entry : gripAttibutes.entrySet()) {
			sb.append(" ").append(entry.getKey()).append("=\"");
			sb.append(gripAttibutes.get(entry.getValue())).append("\"");
		}
		return sb.toString();
	}
}
