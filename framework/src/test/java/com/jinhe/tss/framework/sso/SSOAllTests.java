package com.jinhe.tss.framework.sso;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.jinhe.tss.framework.sso.appserver.FileAppServerStorerTest;
import com.jinhe.tss.framework.sso.context.ContextFilterTest;
import com.jinhe.tss.framework.sso.context.ContextTest;
import com.jinhe.tss.framework.sso.context.RequestContextTest;
import com.jinhe.tss.framework.sso.identity.AutoLoginFilterTest;
import com.jinhe.tss.framework.sso.identity.identifier.AnonymousUserIdentifierTest;
import com.jinhe.tss.framework.sso.identity.identifier.OnlineUserIdentifierTest;
import com.jinhe.tss.framework.sso.online.cache.CacheOnlineUserManagerTest;
import com.jinhe.tss.framework.sso.servlet.MultiRequestServletTest;

/**
 * <p>
 * 单点登录部分所有测试
 * </p>
 */
public class SSOAllTests {
    public static Test suite() {
        TestSuite suite = new TestSuite("All tests from SSO ");
        
        suite.addTestSuite(FileAppServerStorerTest.class);
        suite.addTestSuite(SSOIntegrateTest.class);
        
        suite.addTestSuite(CacheOnlineUserManagerTest.class);
        
        suite.addTestSuite(RequestContextTest.class);
        suite.addTestSuite(ContextFilterTest.class);
        suite.addTestSuite(ContextTest.class);
        
        suite.addTestSuite(MultiRequestServletTest.class);
        
        suite.addTestSuite(TokenUtilTest.class);
        suite.addTestSuite(OnlineUserIdentifierTest.class);
        suite.addTestSuite(AnonymousUserIdentifierTest.class);
        suite.addTestSuite(AutoLoginFilterTest.class);
        
        return suite;
    }

}
