package com.jinhe.tss.framework.sso;

import junit.framework.TestCase;

/**
 * 测试用户令牌处理
 */
public class TokenUtilTest extends TestCase {
 
    protected void setUp() throws Exception {
        super.setUp();
    }
 
    protected void tearDown() throws Exception {
        super.tearDown();
    }
 
    public final void testCreateToken() {
        String token = TokenUtil.createToken("", null);
        assertNull(token);
        token = TokenUtil.createToken(null, new Long(0));
        assertNull(token);
        token = TokenUtil.createToken("", new Long(0));
        assertNotNull(token);
    }

    public final void testGetUserIdFromToken() {
        String sessionId = "123123123123sdasd";
        Long userId = new Long(2343);
        String token = TokenUtil.createToken(sessionId, userId);
        Long newUserId = TokenUtil.getUserIdFromToken(token);
        assertEquals(userId, newUserId);
    }

}

	