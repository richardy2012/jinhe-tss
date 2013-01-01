package com.jinhe.tss.framework;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.jinhe.tss.framework.component.log.BusinessLogTest;
import com.jinhe.tss.framework.component.log.LogActionTest;
import com.jinhe.tss.framework.component.param.ParamActionTest;
import com.jinhe.tss.framework.component.param.ParamServiceTest;
import com.jinhe.tss.framework.component.progress.ProgressTest;
import com.jinhe.tss.framework.license.LicenseTest;
import com.jinhe.tss.framework.mock.UMServiceTest;
import com.jinhe.tss.framework.persistence.entityaop.DecodeInterceptorTest;
import com.jinhe.tss.framework.sso.SSOAllTests;
import com.jinhe.tss.framework.sso.online.database.DBOnlineUserManagerTest;
 
public class AllFrameworkTests {

	public static Test suite() {
	    
		TestSuite suite = new TestSuite("All tests from framework");
		
		suite.addTestSuite(DBOnlineUserManagerTest.class);
		
		suite.addTestSuite(BusinessLogTest.class);
		
		suite.addTestSuite(UMServiceTest.class);
		suite.addTestSuite(DecodeInterceptorTest.class);
		suite.addTestSuite(ParamServiceTest.class);
		
		suite.addTestSuite(ParamActionTest.class);
		suite.addTestSuite(LogActionTest.class);
		
		suite.addTest(SSOAllTests.suite());
		
		suite.addTestSuite(LicenseTest.class);
		suite.addTestSuite(ProgressTest.class);
		
		return suite;
	}

}
