/* ==================================================================   
 * Created [2009-4-27 下午11:32:55] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2009-2012 
 * ================================================================== 
*/

package com.jinhe.tss.portal;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.jinhe.tss.portal.module.ComponentModuleTest;
import com.jinhe.tss.portal.module.NavigatorModuleTest;
import com.jinhe.tss.portal.module.PortalFileOperationTest;
import com.jinhe.tss.portal.module.PortalModuleTest;
 
public class AllPortalTests {

	public static Test suite() {
        TestSuite suite = new TestSuite("All tests from Portal");
		
		suite.addTestSuite(ComponentModuleTest.class);
		suite.addTestSuite(PortalFileOperationTest.class);
		suite.addTestSuite(PortalModuleTest.class);
		suite.addTestSuite(NavigatorModuleTest.class);
		
		return suite;
	}
}
