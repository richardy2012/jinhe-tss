package com.jinhe.tss.framework.sso.servlet;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.springframework.mock.web.MockHttpServletRequest;

import com.jinhe.tss.framework.web.RewriteableHttpServletRequest;
import com.jinhe.tss.framework.web.wrapper.RewriteableHttpServletRequestWrapper;
 
public class RewriteableRequestWrapperTest extends TestCase {

    public void testAddParameter() {
        
        HttpServletRequest hsq = new MockHttpServletRequest();
        RewriteableHttpServletRequest req = RewriteableHttpServletRequestWrapper.getRewriteableHttpServletRequest(hsq );

        req.addParameter("test", "1");
        req.addParameter("test", "2");
        req.addParameter("name", "3");

        assertEquals(((String[]) (req.getParameterValues("test")))[0], "1");
        assertEquals(((String[]) (req.getParameterValues("test")))[1], "2");
        assertEquals(((String[]) (req.getParameterValues("name")))[0], "3");
        
        System.out.println(req.getCharacterEncoding());
        System.out.println(req.getContentType());
        System.out.println(req.getLocalAddr());
        System.out.println(req.getLocalName());
        System.out.println(req.getLocalPort());
        
        System.out.println(req.getRemoteAddr());
        System.out.println(req.getRequestURI());
        System.out.println(req.getQueryString());
    }

}
