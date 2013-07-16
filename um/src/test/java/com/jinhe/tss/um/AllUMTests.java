package com.jinhe.tss.um;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.jinhe.tss.um.module.ApplicationModuleTest;
import com.jinhe.tss.um.module.GroupModuleTest;
import com.jinhe.tss.um.module.MessageModuleTest;
import com.jinhe.tss.um.module.RoleModuleTest;
import com.jinhe.tss.um.module.SubAuthorizeModuleTest;
import com.jinhe.tss.um.module.UserModuleTest;
import com.jinhe.tss.um.search.GeneralSearchTest;
import com.jinhe.tss.um.servlet.GetLoginInfoServletTest;
import com.jinhe.tss.um.servlet.GetPasswordServletTest;
import com.jinhe.tss.um.servlet.GetPasswordStrengthServletTest;
import com.jinhe.tss.um.servlet.RegisterServletTest;
import com.jinhe.tss.um.servlet.ResetPasswordServletTest;
import com.jinhe.tss.um.sso.FetchPermissionAfterLoginCustomizerTest;
import com.jinhe.tss.um.sso.UMPasswordIdentifierTest;
import com.jinhe.tss.um.sso.UMSIdentityTranslatorTest;
 
public class AllUMTests {

	public static Test suite() {
 
        TestSuite suite = new TestSuite("All tests from UM") ;
		
		suite.addTestSuite(GroupModuleTest.class);
		suite.addTestSuite(UserModuleTest.class);
		suite.addTestSuite(RoleModuleTest.class);
		suite.addTestSuite(SubAuthorizeModuleTest.class);
		suite.addTestSuite(MessageModuleTest.class);
		suite.addTestSuite(ApplicationModuleTest.class);
		
		suite.addTestSuite(FetchPermissionAfterLoginCustomizerTest.class);
		suite.addTestSuite(UMPasswordIdentifierTest.class);
		suite.addTestSuite(UMSIdentityTranslatorTest.class);
		
		suite.addTestSuite(GetLoginInfoServletTest.class);
		suite.addTestSuite(GetPasswordServletTest.class);
		suite.addTestSuite(GetPasswordStrengthServletTest.class);
		suite.addTestSuite(RegisterServletTest.class);
		suite.addTestSuite(ResetPasswordServletTest.class);
		
		suite.addTestSuite(GeneralSearchTest.class);

		return suite;
	}

}
