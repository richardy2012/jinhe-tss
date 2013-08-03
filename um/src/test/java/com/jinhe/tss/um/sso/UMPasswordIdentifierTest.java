package com.jinhe.tss.um.sso;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import com.jinhe.tss.framework.exception.UserIdentificationException;
import com.jinhe.tss.framework.sso.SSOConstants;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.um.TxSupportTest4UM;

public class UMPasswordIdentifierTest extends TxSupportTest4UM {
	
	@Test
	public void testIdentify() {
		MockHttpServletRequest request = new MockHttpServletRequest(); 
		MockHttpSession session = new MockHttpSession();
		request.setSession(session);
		 
        request.addParameter(SSOConstants.LOGINNAME_IN_SESSION, "Admin");
        request.addParameter(SSOConstants.USER_PASSWORD, "123456");
		 
		 Context.initRequestContext(request);
		 
		 try {
			new UMPasswordIdentifier().identify();
			
		} catch (UserIdentificationException e) {
			assertFalse(e.getMessage(), true);
		}
	}

}
