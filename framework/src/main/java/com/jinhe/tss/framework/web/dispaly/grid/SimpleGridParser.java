package com.jinhe.tss.framework.web.dispaly.grid;

import java.util.Iterator;
import java.util.List;

/** 
 * 简易Grid解析器
 * 
 */
public class SimpleGridParser extends GridParser {
    
	/**
	 * 解析Grid数据
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public GridNode parse(Object data, int dataType) {
		if (data == null) {
			return null;
		}
		
		GridNode root = new GridNode();
		
		Iterator<?> iter = ((List<?>) data).iterator();
		while (iter.hasNext()) {
			root.addChild(new GridNode(iter.next(), super.columns, dataType));
		}
		return root;
	}

}
