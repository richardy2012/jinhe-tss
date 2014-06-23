package com.jinhe.tss.um.sso;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import com.jinhe.tss.framework.exception.UserIdentificationException;
import com.jinhe.tss.framework.sso.IdentityCard;
import com.jinhe.tss.framework.sso.SSOConstants;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.service.IUserService;

public class UMPasswordIdentifierTest extends TxSupportTest4UM {
	
	@Autowired IUserService userService;
	
	@Before
	public void setUp() {
		super.setUp();
		
		User admin = userService.getUserById(-1L);
		admin.setPassword("123456");
		userService.createOrUpdateUser(admin, "-2", "-1");
	}
	
	@Test
	public void testIdentifyInUM() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpSession session = new MockHttpSession();
		request.setSession(session);

		request.addParameter(SSOConstants.LOGINNAME_IN_SESSION, "Admin");
		request.addParameter(SSOConstants.USER_PASSWORD, "123456");

		Context.initRequestContext(request);
		UMPasswordIdentifier indentifier = new UMPasswordIdentifier();

		try {
			IdentityCard card = indentifier.identify();
			
			Long userId = card.getOperator().getId();
			indentifier.modifyPTUserPassword(userId, "abcdef");
			
			request.addParameter(SSOConstants.USER_PASSWORD, "abcdef");
			indentifier.identify();

		} catch (UserIdentificationException e) {
			assertFalse(e.getMessage(), true);
		}
	}
	
	@Test
	public void testIdentifyInOA() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpSession session = new MockHttpSession();
		request.setSession(session);

		request.addParameter(SSOConstants.LOGINNAME_IN_SESSION, "Admin");
		request.addParameter(SSOConstants.USER_PASSWORD, "wrongpassword");

		Context.initRequestContext(request);
		UMPasswordIdentifier indentifier = new UMPasswordIdentifier();
		
		try {
			indentifier.identify();

		} catch (Exception e) {
			assertTrue(e.getMessage(), true);
		}
	}

}
