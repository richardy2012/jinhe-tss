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
		
		Assert.assertEquals("", DateUtil.formatCare2Second(null));
		Assert.assertTrue( DateUtil.format(date, "").length() > 0 );
		Assert.assertEquals("", DateUtil.format(null, "yyyy-MM-dd HH:mm"));
		Assert.assertEquals("", DateUtil.formatCare2Second(null));
		
		Assert.assertNotNull(DateUtil.parse("2013-11-07 11:26:05"));
		Assert.assertNotNull(DateUtil.parse("2013/11/07 11:26:05"));
		Assert.assertNotNull(DateUtil.parse("2013-11-07"));
		Assert.assertNotNull(DateUtil.parse("2013/11/07"));
		
		try {
			DateUtil.parse("2013/11-07");
		} catch (Exception e) {
			Assert.assertTrue("非法日期字符串，解析失败", true);
		}
		
	}

}
