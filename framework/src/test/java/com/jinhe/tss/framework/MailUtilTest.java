package com.jinhe.tss.framework;

import org.junit.Test;

import com.jinhe.tss.framework.MailUtil;

public class MailUtilTest {
	
	@Test
	public void test() {
		try {
			MailUtil.send("test", "test");
		} catch(Exception e) {
			
		}
		
	}

}
