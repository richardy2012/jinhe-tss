package com.jinhe.tss.um.sso;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import com.jinhe.tss.framework.sso.SSOConstants;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.service.IUserService;
import com.jinhe.tss.util.InfoEncoder;

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

		request.addParameter(SSOConstants.LOGINNAME_IN_SESSION, InfoEncoder.simpleEncode("Admin", 100));
		request.addParameter(SSOConstants.USER_PASSWORD, InfoEncoder.simpleEncode("123456", 100));

		Context.initRequestContext(request);
		UMPasswordIdentifier indentifier = new UMPasswordIdentifier();
		
		try {
			indentifier.identify();

		} catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		} 
	}
	
	@Test
	public void testIdentifyInOA() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpSession session = new MockHttpSession();
		request.setSession(session);

		request.addParameter(SSOConstants.LOGINNAME_IN_SESSION, InfoEncoder.simpleEncode("Admin", 100));
		request.addParameter(SSOConstants.USER_PASSWORD, InfoEncoder.simpleEncode("wrongpassword", 100));

		Context.initRequestContext(request);
		UMPasswordIdentifier indentifier = new UMPasswordIdentifier();
		
		try {
			indentifier.identify();
			Assert.fail("should throw exception but didn't.");
		} catch (Exception e) {
			assertTrue(e.getMessage(), true);
		}
	}

}
