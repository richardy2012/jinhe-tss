/* ==================================================================   
 * Created [2009-4-27 下午11:32:55] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2009-2012 
 * ================================================================== 
*/

package com.jinhe.tss.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllUtilTests {
    public static Test suite() {
        TestSuite suite = new TestSuite("All tests from util of cache");
        suite.addTestSuite(EncryptTest.class);
        suite.addTestSuite(InfoEncoderTest.class);
        
        suite.addTestSuite(URLUtilTest.class);
        suite.addTestSuite(XmlUtilTest.class);
        suite.addTestSuite(XMLDocUtilTest.class);
        
        suite.addTestSuite(MacrocodeCompilerTest.class);
        
        return suite;
    }
}
