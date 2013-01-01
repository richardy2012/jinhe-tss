package com.jinhe.tss.framework.web.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.web.dispaly.XmlPrintWriter;

/** 
 *  所有的Action必须继承此Action，以统一的方式输出响应内容。
 */
public abstract class BaseActionSupport {
    
    protected Logger log = Logger.getLogger(this.getClass());

    /** 请求返回纯xml数据的返回页面 */
    public static final String XML = "XML";

    private XmlPrintWriter writer;

    /**
     * 数据流方式向客户端返回数据
     * 
     * @param xml
     * @throws IOException
     */
    protected void print(Object xml) {
        getWriter().append(xml);
    }

    /**
     * 获取输出流
     * 
     * @return
     * @throws IOException
     */
    protected XmlPrintWriter getWriter() {
        if (writer == null) {
            /* 初始化数据输出流  */
            HttpServletResponse response = Context.getResponse();
            response.setContentType("text/html;charset=GBK");
            try {
                writer = new XmlPrintWriter(response.getWriter());
            } catch (Exception e) {
                throw new BusinessException("初始化数据输出流失败", e);
            }
        }
        return writer;
    }

}
