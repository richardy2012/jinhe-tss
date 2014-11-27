package com.jinhe.tss.framework.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketException;

import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.exception.convert.ExceptionConvertorFactory;
import com.jinhe.tss.framework.exception.convert.IExceptionConvertor;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.sso.context.RequestContext;
import com.jinhe.tss.framework.web.dispaly.ErrorMessageEncoder;
import com.jinhe.tss.framework.web.dispaly.IDataEncoder;
import com.jinhe.tss.framework.web.dispaly.XmlPrintWriter;

/**
 * 异常信息编码器
 * 
 */
public class ExceptionEncoder {
    private static Logger log = Logger.getLogger(ExceptionEncoder.class);
 
    static IExceptionConvertor convertor = ExceptionConvertorFactory.getConvertor();

    public static void encodeException(ServletResponse response, Exception be) {
    	RequestContext requestContext = Context.getRequestContext();
        if( requestContext == null ) return;
    	
    	try {
            if (!response.isCommitted() && !requestContext.isMultiRequest()) {
                response.resetBuffer();
            }
            IDataEncoder errorMsgEncoder = new ErrorMessageEncoder(convertor.convert(be));
            
            if(be instanceof IBusinessException){
                IBusinessException e = (IBusinessException) be;
                
                long theadId = Thread.currentThread().getId();
                String userName = Environment.getUserName();
				if(userName != null) {
					log.warn(theadId + "出现异常, 当前登陆用户【" + userName + "】");
                }
                
				if( e.needPrint() ) {
                    printErrorMessage(be);
                    log.debug("-----------------------  Exception  -----------------------");
                    log.debug("AppCode: " + Config.getAttribute(Config.APPLICATION_CODE));
                    log.debug(errorMsgEncoder.toXml());
                    log.debug("--------------------- End of Exception --------------------");
                }
				else {
					log.warn(be.getMessage() + ", request url:" + requestContext.getRequest().getServletPath() + ", " + theadId);
				}
            }
            
            if (requestContext.isXmlhttpRequest()) {
                response.setContentType("text/html;charset=UTF-8");
                XmlPrintWriter writer = new XmlPrintWriter(response.getWriter());
                // XMLHTTP，返回XML格式错误信息
                errorMsgEncoder.print(writer);
            } 
            else { // HTTP 
            	response.getWriter().println(be);
            }
        } catch (Exception e) {
            log.error("ExceptionEncoder.encodeException时出错：" + e.getMessage());
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
