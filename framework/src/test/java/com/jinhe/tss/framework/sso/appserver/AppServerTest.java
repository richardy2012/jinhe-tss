package com.jinhe.tss.framework.sso.appserver;

import junit.framework.TestCase;
 
public class AppServerTest extends TestCase {
 
    public final void testGetDomain() {
        AppServer server = new AppServer();
        server.setBaseURL("http://localhost:8088");
        assertEquals("localhost", server.getDomain());
        
        server.setBaseURL("http://localhost:8088/");
        assertEquals("localhost", server.getDomain());
        
        server.setBaseURL("http://localhost");
        assertEquals("localhost", server.getDomain());
        
        server.setBaseURL("http://localhost/");
        assertEquals("localhost", server.getDomain());
        
        server.setBaseURL("http://127.0.0.1:8088");
        assertEquals("127.0.0.1", server.getDomain());
        
        server.setBaseURL("http://127.0.0.1:8088/tss");
        assertEquals("127.0.0.1", server.getDomain());
    }

}

