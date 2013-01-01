package com.jinhe.tss.cms;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.jinhe.tss.cms.module.ArticleModuleTest;
import com.jinhe.tss.cms.module.ChannelModuleTest;
import com.jinhe.tss.cms.module.TimerModuleTest;
import com.jinhe.tss.cms.publish.ArticlePublishTest;
 
public class AllCMSTests {

	public static Test suite() {
 
        TestSuite suite = new TestSuite("All tests from CMS");
		
		suite.addTestSuite(ArticleModuleTest.class);
		suite.addTestSuite(ChannelModuleTest.class);
		
		suite.addTestSuite(TimerModuleTest.class);
		suite.addTestSuite(ArticlePublishTest.class);
		
		return suite;
	}

}
