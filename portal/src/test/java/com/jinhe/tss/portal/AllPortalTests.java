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

import com.jinhe.tss.portal.module.DecoratorModuleTest;
import com.jinhe.tss.portal.module.ElementGroupModuleTest;
import com.jinhe.tss.portal.module.LayoutModuleTest;
import com.jinhe.tss.portal.module.MenuModuleTest;
import com.jinhe.tss.portal.module.PortalFileOperationTest;
import com.jinhe.tss.portal.module.PortalModuleTest;
import com.jinhe.tss.portal.module.PortletModuleTest;
 
public class AllPortalTests {

	public static Test suite() {
        TestSuite suite = new TestSuite("All tests from Portal");
		
        suite.addTestSuite(ElementGroupModuleTest.class);
		suite.addTestSuite(LayoutModuleTest.class);
		suite.addTestSuite(DecoratorModuleTest.class);
		suite.addTestSuite(PortletModuleTest.class);
		
		suite.addTestSuite(PortalFileOperationTest.class);
		
		suite.addTestSuite(PortalModuleTest.class);
		suite.addTestSuite(MenuModuleTest.class);
		
		return suite;
	}

}
