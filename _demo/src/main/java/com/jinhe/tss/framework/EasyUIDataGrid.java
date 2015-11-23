package com.jinhe.tss.framework;

import java.util.List;

import com.jinhe.tss.framework.persistence.pagequery.PageInfo;

public class EasyUIDataGrid {
	
	public int total;
	
	public List<?> rows;
	
	public EasyUIDataGrid(PageInfo pi) {
		this.total = pi.getTotalRows();
		this.rows  = pi.getItems();
	}

}
