package com.jinhe.tss.cms;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.jinhe.tss.cms.module.ArticleModuleTest;
import com.jinhe.tss.cms.module.ChannelModuleTest;
import com.jinhe.tss.cms.module.TimerModuleTest;
import com.jinhe.tss.cms.publish.ArticlePublishTest;
 

@RunWith(Suite.class)
@Suite.SuiteClasses({
	ArticleModuleTest.class,
	ChannelModuleTest.class,
	TimerModuleTest.class,
	ArticlePublishTest.class
})
public class AllCMSTests {
 
}
