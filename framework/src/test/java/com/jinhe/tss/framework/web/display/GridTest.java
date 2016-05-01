package com.jinhe.tss.framework.web.display;

import org.junit.Test;

import com.jinhe.tss.framework.web.dispaly.grid.DefaultGridNode;
import com.jinhe.tss.framework.web.dispaly.grid.GridValueFilter;

public class GridTest {
	
	@Test
	public void test() {
		
		GridValueFilter filter = new GridValueFilter() {
			public Object pretreat(Object key, Object value) {
				return value;
			}
		};
		new DefaultGridNode(filter);
		
	}

}
