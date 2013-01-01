package com.jinhe.tss.um.sso;

import java.util.List;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import com.jinhe.tss.framework.sso.SSOConstants;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.um.TxSupportTest4UM;

public class FetchPermissionAfterLoginCustomizerTest extends TxSupportTest4UM {
	
	public void testExcute() {
		 MockHttpSession session = new MockHttpSession();
		 MockHttpServletRequest request = new MockHttpServletRequest(); 
		 request.setSession(session);
		 
		 Context.initRequestContext(request);
		 
		 new FetchPermissionAfterLoginCustomizer().execute();
		 
		 List<?> roleIds = (List<?>)session.getAttribute(SSOConstants.USER_RIGHTS_IN_SESSION);
		 assertTrue( roleIds.size() > 0 );
		 Object loginName = session.getAttribute(SSOConstants.LOGINNAME_IN_SESSION);
		 assertNotNull( loginName );
		 log.debug(loginName);
	}

}
