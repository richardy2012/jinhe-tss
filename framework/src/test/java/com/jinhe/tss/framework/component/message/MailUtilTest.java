package com.jinhe.tss.framework.component.message;

import org.junit.Test;

import com.jinhe.tss.framework.component.message.MailUtil;

public class MailUtilTest {
	
	@Test
	public void test() {
		try {
			MailUtil.send("test", "test");
		} catch(Exception e) {
			
		}
		
	}

}
