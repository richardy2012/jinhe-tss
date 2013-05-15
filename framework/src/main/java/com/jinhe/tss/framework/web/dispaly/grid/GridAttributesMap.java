package com.jinhe.tss.framework.web.dispaly.grid;

import java.util.HashMap;
import java.util.Map;

/**
 * 放置Grid的属性值。
 */
public class GridAttributesMap {

	private Map<String, Object> gridAttibutes = new HashMap<String, Object>();

	private String[] columns;

	public GridAttributesMap(String[] columns) {
		this.columns = columns;
	}

	public void put(String key, Object value) {
		gridAttibutes.put(key, value);
	}

	public void putAll(Map<String, Object> map) {
		gridAttibutes.putAll(map);
	}

	public Object[] getValues() {
		Object[] values = new Object[columns.length];
		for (int i = 0; i < columns.length; i++) {
			values[i] = gridAttibutes.get(columns[i]);
		}
		return values;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, Object> entry : gridAttibutes.entrySet()) {
			sb.append(" ").append(entry.getKey()).append("=\"");
			sb.append(gridAttibutes.get(entry.getValue())).append("\"");
		}
		return sb.toString();
	}
}
