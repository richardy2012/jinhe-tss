package com.jinhe.tss.framework.web.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.Document;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.exception.BusinessServletException;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.web.dispaly.xmlhttp.XmlHttpDecoder;
import com.jinhe.tss.util.XMLDocUtil;
import com.jinhe.tss.util.XmlUtil;

/**
 * <p> XmlHttp请求数据解码 </p>
 * <pre>
 * 将XmlHttp的XML数据解析成Request中的属性。
 * 传入的XMLHttp的XML数据格式，如：
 * <Request><Param><Name><![CDATA[resourceId]]></Name><Value><![CDATA[2]]></Value></Param></Request>，
 * 需要解析成 request.put("resourceId", 2);
 * </pre>
 */
@WebFilter(filterName = "XmlHttpDecodeFilter", 
		urlPatterns = {"*.do", "*.action", ".in"} )
public class XmlHttpDecodeFilter implements Filter {
    private static final Logger log = Logger.getLogger(XmlHttpDecodeFilter.class);
 
    public void init(FilterConfig arg0) throws ServletException {
        log.info("XMLHTTP请求解析服务初始化完成！appCode=" + Config.getAttribute(Config.APPLICATION_CODE));
    }

    /**
     * 通过可重写的Request对象，将xml数据流解析成名值对，重写入Request对象中。
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            if (Context.getRequestContext().isXmlhttpRequest()) {
                Document doc = null;
                ServletInputStream is = null;
                String requestBody = null;
                try {
                    requestBody = getRequestBody(is = request.getInputStream());
                    doc = XMLDocUtil.dataXml2Doc(requestBody);
                } catch (IOException e) {
                    throw new BusinessException("获取请求数据流失败", e);
                } catch (Exception e) {
                    try {
                        doc = XMLDocUtil.dataXml2Doc(XmlUtil.stripNonValidXMLCharacters(requestBody));
                    } catch (Exception e1) {
                        throw new BusinessException("解析xml请求数据流失败", e1);
                    }
                } finally {
                    try {
                        is.close();
                    } catch (Exception e) {
                        throw new BusinessException("关闭请求数据流失败", e);
                    } 
                }
                
                httpRequest = XmlHttpDecoder.decode(doc.getRootElement(), httpRequest); 
            }
            
            chain.doFilter(httpRequest, response);
        } 
        catch (RuntimeException e) {
            throw new BusinessServletException(e);
        }
    }
    
    private static String getRequestBody(ServletInputStream sis) throws IOException, UnsupportedEncodingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int bLen = 0;
        byte[] buffer = new byte[8 * 1024];
        while ((bLen = sis.read(buffer)) > 0) {
            baos.write(buffer, 0, bLen);
        }
        return new String(baos.toByteArray(), "UTF-8");
    } 
 
    public void destroy() {
        
    }
}
