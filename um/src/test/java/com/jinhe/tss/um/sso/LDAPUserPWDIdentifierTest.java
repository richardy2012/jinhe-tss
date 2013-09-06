package com.jinhe.tss.um.sso;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.sso.IdentityCard;
import com.jinhe.tss.framework.sso.SSOConstants;
import com.jinhe.tss.um.TxSupportTest4UM;

public class LDAPUserPWDIdentifierTest extends TxSupportTest4UM {
	
	@Before
	public void setUp() {
		super.setUp();
		
		request.addParameter(SSOConstants.LOGINNAME_IN_SESSION, "Admin");
		request.addParameter(SSOConstants.USER_PASSWORD, "123456");
		
		Config.setProperty("oa.ldap.url", "no ldap");
	}
	
	@Test
	public void test() {
		LDAPUserPWDIdentifier indentifier = new LDAPUserPWDIdentifier();
		
		IdentityCard card = null;
		try {
			card = indentifier.identify();
		} 
		catch (Exception e) {
			Assert.assertTrue("身份认证失败：" + e.getMessage(), true);
		}
		
		Assert.assertNull(card);
	}
}
