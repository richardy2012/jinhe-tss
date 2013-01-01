package com.jinhe.tss.framework.web.dispaly.grid;

import com.jinhe.tss.util.EasyUtils;

/** 
 * 对grid的Column的封装对象。
 * 一个GridColumn是一列，一个GridNode是一行
 * 
 */
class GridColumn {
    
	static final String GRID_COLUMN_CLASS_TYPE_STRING  = "String";
	static final String GRID_COLUMN_CLASS_TYPE_INTEGER = "Integer";
	static final String GRID_COLUMN_CLASS_TYPE_DOUBLE  = "Double";
	static final String GRID_COLUMN_CLASS_TYPE_DATE    = "Date";

	private String name;      //列名称
	private String classType; //数据类型，如果为null或""则认为是字符串
	private String pattern;   //数据格式化格式，如果为""或null则不格式化数据
	private boolean isSum;    //是否合计子节点

	public String getClassType() {
		return classType;
	}

	public boolean isSum() {
		return isSum;
	}

	public String getName() {
		return name;
	}

	public String getPattern() {
		return pattern;
	}

	public void setClassType(String classType) {
		if (  EasyUtils.isNullOrEmpty(classType) ) {
			classType = GRID_COLUMN_CLASS_TYPE_STRING;
		}
		this.classType = classType;
	}

	public void setSum(boolean isSum) {
		this.isSum = isSum;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
}
