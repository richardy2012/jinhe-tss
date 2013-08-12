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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.jinhe.tss.portal.module.ComponentModuleTest;
import com.jinhe.tss.portal.module.NavigatorModuleTest;
import com.jinhe.tss.portal.module.PortalFileOperationTest;
import com.jinhe.tss.portal.module.PortalModuleTest;
 
@RunWith(Suite.class)
@Suite.SuiteClasses({
	ComponentModuleTest.class,
	PortalFileOperationTest.class,
	PortalModuleTest.class,
	NavigatorModuleTest.class
})
public class AllPortalTests {
 
}
