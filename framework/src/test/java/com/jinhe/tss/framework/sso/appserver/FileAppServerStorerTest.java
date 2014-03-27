package com.jinhe.tss.framework.sso.appserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jinhe.tss.framework.exception.BusinessException;

/**
 * 测试应用服务器文件管理器（绝对路径配置方式）
 */
public class FileAppServerStorerTest {
	
    IAppServerStorer storer = new FileAppServerStorer();
 
    @Test
    public void testGetAppServer() {
        AppServer server = storer.getAppServer("TSS");
        assertEquals("TSS", server.getCode());
        assertEquals("http://localhost:8111/tss", server.getBaseURL());
        assertEquals("localhost", server.getDomain());
        assertEquals("TSS", server.getName());
        assertEquals("/tss", server.getPath());
        assertEquals("JSESSIONID", server.getSessionIdName());
    }
 
    @Test
    public void testGetAppServer4Exception() {
        try {
            storer.getAppServer("CMS");
        } catch (BusinessException e) {
            assertTrue(true);
        }
    }
}
