package com.jinhe.tss.framework.component.param;

import junit.framework.TestCase;

public class ParamConfigTest extends TestCase {
	
	public void testGetAttribute() {
		 assertEquals("TSS", ParamConfig.getAttribute("application.code"));
	}

}
