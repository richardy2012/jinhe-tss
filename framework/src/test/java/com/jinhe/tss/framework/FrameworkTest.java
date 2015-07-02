package com.jinhe.tss.framework;

import junit.framework.Assert;

import org.junit.Test;

public class FrameworkTest {
	
	@Test
	public void testGetVersion() {
		Object[] result = new Framework().getVersion(null, null);
		Assert.assertEquals("dev", result[1]);
	}
	
	@Test
	public void testLiceence() {
		Assert.assertTrue(Framework.validateTSS());
	}

}
