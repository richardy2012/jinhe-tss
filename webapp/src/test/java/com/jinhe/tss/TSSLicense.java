package com.jinhe.tss;

import junit.framework.Assert;

import org.junit.Test;

import com.jinhe.tss.framework.Framework;

public class TSSLicense {
 
	@Test
	public void testLiceence() {
		Assert.assertTrue(Framework.validateTSS());
	}

}
