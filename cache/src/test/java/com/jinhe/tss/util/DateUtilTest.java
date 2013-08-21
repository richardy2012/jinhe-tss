package com.jinhe.tss.util;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

public class DateUtilTest {
	
	@Test
	public void test() {
		
		Date date = new Date();
		
		String formatStr = DateUtil.format(date);
		System.out.println(formatStr);
		
		String formatStr2 = DateUtil.format(date, "yyyy-MM-dd HH:mm");
		System.out.println(formatStr2);
		
		Assert.assertTrue(formatStr2.startsWith(formatStr));
		
		formatStr = DateUtil.formatCare2Second(date);
		System.out.println(formatStr);
		Assert.assertTrue(formatStr.startsWith(formatStr2));
		
		Assert.assertTrue(date.after(DateUtil.parse(formatStr)));
		
	}

}
