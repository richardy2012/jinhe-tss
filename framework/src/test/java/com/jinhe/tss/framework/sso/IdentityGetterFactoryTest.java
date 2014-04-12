package com.jinhe.tss.framework.sso;

import junit.framework.Assert;

import org.junit.Test;

public class IdentityGetterFactoryTest {
	
	@Test
	public void test() {
		IdentityGetter instance = IdentityGetterFactory.getTranslator();
		Assert.assertNotNull(instance);
	}

}
