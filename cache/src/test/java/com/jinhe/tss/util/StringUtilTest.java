package com.jinhe.tss.util;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilTest {

	@Test
	public void test() {
		String str = "Portletyyy~!@#$%^&*()_+-=[]{}\\|;':\",./<>?原型实现模型訾鄣迂蟓";

		Assert.assertEquals(str, StringUtil.GBKToUTF8(StringUtil.UTF8ToGBK(str)));
		System.out.println(StringUtil.UTF8ToGBK(StringUtil.GBKToUTF8(str)));
	}

}
