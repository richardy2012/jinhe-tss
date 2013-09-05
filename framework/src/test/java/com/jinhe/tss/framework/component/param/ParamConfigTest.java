package com.jinhe.tss.framework.component.param;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ParamConfigTest {
	
	@Test
	public void testGetAttribute() {
		 assertEquals("TSS", ParamConfig.getAttribute("application.code"));
	}
	
	@Test
	public void testParamManager() {
//		 ParamManager.getComboParam(code);
//		 ParamManager.getSimpleParam(code);
//		 ParamManager.getTreeParam(code);
//		 
//		 ParamManager.getValue(code);
//		 ParamManager.getValueNoSpring(code);
	}

}
