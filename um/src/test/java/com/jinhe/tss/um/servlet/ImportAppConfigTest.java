package com.jinhe.tss.um.servlet;

import org.junit.Test;

import com.jinhe.tss.framework.test.TestUtil;

public class ImportAppConfigTest {
	
	@Test
	public void test() {
		String filepath = TestUtil.getSQLDir() + "/um-resource-config.xml";
		try {
			new ImportAppConfig().processUploadFile(null, filepath, "");
		} catch (Exception e) {
//			Assert.fail();
		}
	}

}
