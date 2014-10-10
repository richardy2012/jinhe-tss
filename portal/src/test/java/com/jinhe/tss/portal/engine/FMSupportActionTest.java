package com.jinhe.tss.portal.engine;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import com.jinhe.tss.framework.sso.context.Context;

public class FMSupportActionTest extends FMSupportAction {
	
	@Test
	public void test() {
		Context.setResponse(new MockHttpServletResponse());
		super.printHTML(12L, "${html}", true);
	}
}
