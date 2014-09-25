package com.jinhe.tss.framework;

import org.junit.Assert;
import org.junit.Test;

public class GlobalTest {
	
	@Test
	public void test() {
		Assert.assertNotNull(Global.getContext());
		
		try {
			Global.getRemoteOnlineUserManager();
		} catch(Exception e) {
			Assert.assertTrue("不存在", true);
		}
		
		Global.destroyContext();
	}
}
