package com.jinhe.tss.um.sso.othersystem;

import java.security.Principal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jinhe.tss.framework.exception.UserIdentificationException;
import com.jinhe.tss.framework.sso.IdentityCard;
import com.jinhe.tss.um.TxSupportTest4UM;

public class LtpaTokenIdentifierTest extends TxSupportTest4UM {
	
	@Before
	public void setUp() {
		super.setUp();
		
		request.addParameter(LtpaTokenIdentifier.LTPA_TOKEN_NAME, "ltpaTokenXXXXXXXX");
		Principal userPrincipal = new Principal() {
			public String getName() {
				return "Admin";
			}
			
		};
		request.setUserPrincipal( userPrincipal );
	}
	
	@Test
	public void test() {
		LtpaTokenIdentifier indentifier = new LtpaTokenIdentifier();
		try {
			IdentityCard card = indentifier.identify();
			Assert.assertNotNull(card);
			Assert.assertEquals("Admin", card.getLoginName());
			Assert.assertNotNull( card.getOperator() );
			Assert.assertNotNull( card.getToken() );
			Assert.assertEquals(false, card.isAnonymous());
		} 
		catch (UserIdentificationException e) {
			Assert.fail("身份认证失败：" + e.getMessage());
		}
	}

}
