package com.jinhe.tss.um.sso;

import org.junit.Test;

import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.um.TxSupportTest4UM;

public class UMIdentityTranslatorTest extends TxSupportTest4UM {
	
	@Test
	public void testTranslator() {
		 UMIdentityTranslator translator = new UMIdentityTranslator();
		 translator.translate(Environment.getOperatorId());
		 translator.savePassword(Environment.getOperatorId(), "abcdefg");
	}

}
