package com.jinhe.tss.framework;

import org.junit.Test;

import com.jinhe.tss.framework.MailUtil;

public class MailUtilTest {
	
	@Test
	public void test1() {
		try {
			MailUtil.send("test", "test");
		} catch(Exception e) {
			
		}
	}
	
	@Test
	public void test2() {
		try {
			MailUtil.send("test", "test", null);
		} catch(Exception e) {
		}
		
		try {
			MailUtil.send("test", "test", new String[]{});
		} catch(Exception e) {
		}
	}

}
