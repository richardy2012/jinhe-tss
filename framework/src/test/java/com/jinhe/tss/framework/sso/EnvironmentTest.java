package com.jinhe.tss.framework.sso;

import org.junit.Assert;
import org.junit.Test;

import com.jinhe.tss.framework.sso.context.Context;

public class EnvironmentTest {
	
	@Test
	public void test() {
		Context.destroy();
		
		Assert.assertNull(Environment.getClientIp());
		Assert.assertEquals("/tss", Environment.getContextPath());
		Assert.assertNull(Environment.getOperatorName());
		Assert.assertNull(Environment.getSessionId());
		
		Assert.assertNull(Environment.getUserName());
		Assert.assertNull(Environment.getOperatorId());
		
		Assert.assertNull(TokenUtil.getUserIdFromToken(null));
		
		Assert.assertNull(Context.getIdentityCard());
	}

}
