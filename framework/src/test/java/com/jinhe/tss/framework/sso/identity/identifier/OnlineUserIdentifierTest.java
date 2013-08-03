package com.jinhe.tss.framework.sso.identity.identifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.servlet.http.Cookie;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import com.jinhe.tss.framework.exception.UserIdentificationException;
import com.jinhe.tss.framework.sso.DemoOperator;
import com.jinhe.tss.framework.sso.IUserIdentifier;
import com.jinhe.tss.framework.sso.IdentityCard;
import com.jinhe.tss.framework.sso.TokenUtil;
import com.jinhe.tss.framework.sso.appserver.AppServer;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.sso.context.RequestContext;
import com.jinhe.tss.framework.sso.identifier.OnlineUserIdentifier;
import com.jinhe.tss.framework.sso.identity.MockAppServerStorer;
import com.jinhe.tss.framework.sso.identity.MockApplicationContext;
import com.jinhe.tss.framework.sso.identity.MockIdentityTranslatorFactory;
import com.jinhe.tss.framework.sso.identity.MockOnlineUserManagerFactory;
import com.jinhe.tss.framework.sso.online.OnlineUserManagerFactory;
 
public class OnlineUserIdentifierTest {
    
    private IUserIdentifier identifier;
    private String token;
 
    @Before
    public void setUp() throws Exception {
        MockOnlineUserManagerFactory.init();
        MockIdentityTranslatorFactory.init();
        
        MockAppServerStorer storer = new MockAppServerStorer();
        AppServer server = new AppServer();
        server.setCode("Core");
        server.setName("Core");
        server.setUserDepositoryCode("tss");
        storer.putAppServer("Core", server);
        
        Context.initApplicationContext(new MockApplicationContext(storer, "Core"));
        
        Long userId = 1L;
        MockHttpSession session = new MockHttpSession();
        token = TokenUtil.createToken(session.getId(), userId);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie[] { new Cookie(RequestContext.USER_TOKEN, token) });
        request.setSession(session);
        
        Context.initRequestContext(request);
        
        IdentityCard card = new IdentityCard(token, new DemoOperator(userId));
        Context.initIdentityInfo(card);
        
        identifier = new OnlineUserIdentifier();
    }
 
    @After
    public void tearDown() throws Exception {
        identifier = null;
        token = null;
    }

    @Test
    public void testIdentify4NotLogin() {
        IdentityCard card = null;
        try {
            card = identifier.identify();
        } catch (UserIdentificationException e) {
            fail("UserIdentificationException");
        }
        assertNull(card);
    }

    @Test
    public void testIdentify4Login() {
        MockOnlineUserManagerFactory.getManager().register(token, "Test", "SessionId", new Long(30), null);
        
        IdentityCard card = null;
        try {
            card = identifier.identify();
        } catch (UserIdentificationException e) {
            fail("UserIdentificationException");
        }
        assertNotNull(card);
        assertEquals("JinPujun", card.getUserName());
        assertTrue(OnlineUserManagerFactory.getManager().isOnline(card.getToken()));
    }
}
