package com.jinhe.tss.framework.sso.appserver;

import junit.framework.TestCase;

import com.jinhe.tss.framework.exception.BusinessException;

/**
 * 测试应用服务器文件管理器（绝对路径配置方式）
 */
public class FileAppServerStorerTest extends TestCase {
    protected IAppServerStorer storer = null;
 
    protected void setUp() throws Exception {
        super.setUp();
        storer = new FileAppServerStorer();
    }
 
    protected void tearDown() throws Exception {
        super.tearDown();
        storer = null;
    }
 
    public final void testGetAppServer() {
        AppServer server = storer.getAppServer("TSS");
        assertEquals("TSS", server.getCode());
        assertEquals("http://localhost:8083/tss", server.getBaseURL());
        assertEquals("localhost", server.getDomain());
        assertEquals("TSS", server.getName());
        assertEquals("/tss", server.getPath());
        assertEquals("JSESSIONID", server.getSessionIdName());
        assertEquals("tss", server.getUserDepositoryCode());
    }
 
    public final void testGetAppServer4Exception() {
        try {
            storer.getAppServer("CMS");
//            fail("应该有异常抛出，但未抛出");
        } catch (BusinessException e) {
            assertTrue(true);
        }
    }
}
