package com.jinhe.tss.framework.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.exception.convert.ExceptionConvertorFactory;
import com.jinhe.tss.framework.exception.convert.IExceptionConvertor;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.sso.context.RequestContext;
import com.jinhe.tss.framework.web.dispaly.ErrorMessageEncoder;
import com.jinhe.tss.framework.web.dispaly.XmlPrintWriter;

/**
 * 异常信息编码器
 * 
 */
public class ExceptionEncoder {
    private static Logger log = Logger.getLogger(ExceptionEncoder.class);
    
    /** 系统异常发生时，如果自定义了异常处理，则将异常对象以此名称存储到请求属性中 */
    public static final String BUSINESS_EXCEPTION = "businessException";

    private static IExceptionConvertor convertor = ExceptionConvertorFactory.getConvertor();

    public static void encodeException(ServletResponse response, Exception be) throws IOException {
        try {
            RequestContext requestContext = Context.getRequestContext();
            if (!response.isCommitted() && !requestContext.isMultiRequest()) {
                response.resetBuffer();
            }
            ErrorMessageEncoder errorMessageEncoder = new ErrorMessageEncoder(convertor.convert(be));
            
            if(be instanceof IBusinessException){
                IBusinessException e = (IBusinessException) be;
                if(e.getRelogin() == 0){ // 如果是提示登录相关的异常（getRelogin = 1 or 2），不需要在控制台打印出来。
                    printErrorMessage(be);
                    //输出调试信息
                    log.debug("-----------------------  Exception  -----------------------");
                    log.debug("AppCode: " + Config.getAttribute(Config.APPLICATION_CODE));
                    log.debug(errorMessageEncoder.toXml());
                    log.debug("--------------------- End of Exception --------------------");
                }
            }
            if (requestContext.isXmlhttpRequest()) {
                response.setContentType("text/html;charset=gbk");
                XmlPrintWriter writer = new XmlPrintWriter(response.getWriter());
                //XMLHTTP，返回XML格式错误信息
                errorMessageEncoder.print(writer);
            } else {
                //HTTP，返回HTML格式
                HttpServletRequest request = requestContext.getRequest();
                String errorHandle = Config.getAttribute(Config.ERROR_HANDLE);
                if (errorHandle != null) {
                    request.setAttribute(BUSINESS_EXCEPTION, be);
                    RequestDispatcher rd = request.getRequestDispatcher(errorHandle);
                    rd.forward(request, response);
                } else {
                    response.setContentType("text/html;charset=gbk");
                    XmlPrintWriter writer = new XmlPrintWriter(response.getWriter());
                    writer.println(errorMessageEncoder.toHTML());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            be.printStackTrace();
        }
    }

    /**
     * <p>
     * 打印详细错误信息到日志中
     * </p>
     * @param be
     */
    private static void printErrorMessage(Throwable be) {
        Throwable first = null;
        Throwable cause = be.getCause();
        while (cause != null) {
            first = cause;
            cause = cause.getCause();
        }
        
        // 过滤掉不需要输出到控制台（或日志）的异常，比如SocketException等
        if(first != null && first instanceof SocketException) {
            return; 
        }
        
        printStackTrace(be);
        if (first != null && first != be) {
            printStackTrace(first);
        }
    }

    /**
     * <p>
     * 打印错误的堆栈信息到错误日志中
     * </p>
     * 
     * @param be
     */
    private static void printStackTrace(Throwable be) {
        StringWriter writer = new StringWriter();
        be.printStackTrace(new PrintWriter(writer)); // 将异常信息输出至 字符串流StringWriter
        log.error(writer.toString());
    }
}
