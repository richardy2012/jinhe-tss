package com.jinhe.tss.um.sso;

import org.junit.Test;

import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.sso.IdentityGetter;
import com.jinhe.tss.um.TxSupportTest4UM;

public class UMIdentityGetterTest extends TxSupportTest4UM {
	
	@Test
	public void testTranslator() {
		 IdentityGetter translator = new UMIdentityGetter();
		 translator.getOperator(Environment.getUserId());
	}

}
