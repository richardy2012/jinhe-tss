package com.jinhe.tss.framework.sso.context;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;

import com.jinhe.tss.framework.sso.AnonymousOperator;
import com.jinhe.tss.framework.sso.IdentityCard;
import com.jinhe.tss.framework.web.filter.ContextFilter;

/**
 * <p>
 * 上下文信息处理Filter测试类
 * </p>
 * 
 * 单个的Mock对象，利用静态导入EasyMock，通过createMock(interfaceName.class)
 * 多个Mock对象，通过ImocksControl管理。
 */
public class ContextFilterTest extends TestCase {

    private ContextFilter filter;

    private IMocksControl mocksControl;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private HttpSession session;

    protected void setUp() throws Exception {
        mocksControl = EasyMock.createControl();
        
        request = mocksControl.createMock(HttpServletRequest.class);
        response = mocksControl.createMock(HttpServletResponse.class);
        session = mocksControl.createMock(HttpSession.class); // EasyMock.createMock(HttpSession.class);
        
        EasyMock.expect(request.getSession()).andReturn(session).times(0, 8);
        EasyMock.expect(request.getHeader(RequestContext.USER_CLIENT_IP)).andReturn("127.0.0.1").times(0, 3);
        EasyMock.expect(request.getContextPath()).andReturn("/tss");
        EasyMock.expect(request.getRequestURI()).andReturn("/tss/login.do");
        
        EasyMock.expect(session.getId()).andReturn("1").times(0, 3);
        
        filter = new ContextFilter();
    }

    protected void tearDown() throws Exception {
        Context.destroy();
    }

    /**
     * Test method for
     * {@link com.jinhe.tss.core.web.filter.ContextFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}.
     */
    public final void testDoFilterNotLogin() throws IOException, ServletException {
        EasyMock.expect(session.getAttribute(RequestContext.USER_TOKEN)).andReturn(null).times(0, 3); // 无token
        EasyMock.expect(session.getAttribute(RequestContext.IDENTITY_CARD)).andReturn(null).atLeastOnce(); // 无card

        mocksControl.replay();

        filter.doFilter(request, response, new FilterChain() {
            // not Login
            public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
                HttpServletRequest httpServletRequest = (HttpServletRequest) request;

                assertTrue(Context.getRequestContext().getRequest() instanceof HttpServletRequest);
                assertEquals(httpServletRequest.getSession(), Context.getRequestContext().getSession());
                assertNull(Context.getRequestContext().getIdentityCard());

            }
        });
        assertNull(Context.getRequestContext());
    }

    /**
     * Test method for
     * {@link com.jinhe.tss.core.web.filter.ContextFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}.
     */
    public final void testDoFilter4Login() throws IOException, ServletException {
        EasyMock.expect(session.getAttribute(RequestContext.USER_TOKEN)).andReturn("token").times(0, 3);
        final IdentityCard identityCard = new IdentityCard("token", AnonymousOperator.anonymous);
        EasyMock.expect(session.getAttribute(RequestContext.IDENTITY_CARD)).andReturn(identityCard).times(0, 3);

        session.setAttribute(RequestContext.IDENTITY_CARD, identityCard);

        mocksControl.replay(); // 让mock 准备重放记录的数据

        filter.doFilter(request, response, new FilterChain() {
            // Login
            public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
                HttpServletRequest httpServletRequest = (HttpServletRequest) request;

                assertTrue(Context.getRequestContext().getRequest() instanceof HttpServletRequest);
                assertEquals(httpServletRequest.getSession(), Context.getRequestContext().getSession());
                assertEquals(identityCard, Context.getRequestContext().getIdentityCard());
            }

        });
        assertNull(Context.getRequestContext());
    }
}
