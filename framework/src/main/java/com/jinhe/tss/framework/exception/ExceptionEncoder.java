package com.jinhe.tss.framework.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.MailUtil;
import com.jinhe.tss.framework.component.log.IBusinessLogger;
import com.jinhe.tss.framework.component.log.Log;
import com.jinhe.tss.framework.component.param.ParamConfig;
import com.jinhe.tss.framework.exception.convert.ExceptionConvertorFactory;
import com.jinhe.tss.framework.exception.convert.IExceptionConvertor;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.sso.context.RequestContext;
import com.jinhe.tss.framework.web.dispaly.ErrorMessageEncoder;
import com.jinhe.tss.framework.web.dispaly.IDataEncoder;
import com.jinhe.tss.framework.web.dispaly.XmlPrintWriter;
import com.jinhe.tss.util.EasyUtils;

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
            
            boolean needRelogin = false;
            if(be instanceof IBusinessException){
                IBusinessException e = (IBusinessException) be;
                needRelogin = e.needRelogin();
                
                long theadId = Thread.currentThread().getId();
                String userName = Environment.getUserName();
                
				if( e.needPrint() ) {
					if( !needRelogin ) {
						log.warn( "出现异常, 当前登陆用户【" + userName + "】, " + theadId);
	                }
                    printErrorMessage(be);
                    log.debug("-----------------------  Exception  -----------------------");
                    log.debug("AppCode: " + Config.getAttribute(Config.APPLICATION_CODE));
                    log.debug(errorMsgEncoder.toXml());
                    log.debug("--------------------- End of Exception --------------------");
                }
				else {
					log.warn("出现异常, 当前登陆用户【" + userName + "】, " + be.getMessage() 
							+ ", request url:" + requestContext.getRequest().getServletPath() + ", " + theadId);
				}
            }
            
            // XMLHTTP，tssJS发出的ajax请求, 返回XML格式错误信息
            if ( requestContext.isXmlhttpRequest() ) { 
                response.setContentType("text/html;charset=UTF-8");
                XmlPrintWriter writer = new XmlPrintWriter(response.getWriter());
                errorMsgEncoder.print(writer);
            } 
            else { // HTTP JSON: 默认用json格式往response写入异常信息
            	response.setContentType("application/json;charset=UTF-8");
            	response.getWriter().println("{\"errorMsg\": \"" + getFirstCause(be).getMessage() + "\"}");
            }
            // 注: 上传导入附件抛出的异常统一在 Servlet4Upload 里处理。不会流到此处。
        } 
    	catch (Exception e) {
            log.error("ExceptionEncoder.encodeException时出错：" + e.getMessage());
        }
    }

    /**
     * 打印详细错误信息到日志中
     * @param be
     */
    private static void printErrorMessage(Throwable be) {
        Throwable first = getFirstCause(be);
        
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
     * 读取到最里面一级的异常对象。
     * 异常经过层层重新抛出，但只有最里面一级才是引起本次异常的根本原因。
     * 
     * @param be
     * @return
     */
    public static Throwable getFirstCause(Throwable be) {
        Throwable first = null;
        Throwable cause = be.getCause();
        while (cause != null) {
            first = cause;
            cause = cause.getCause();
        }
        
        return first != null ? first : be;
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
        String stackTrace = writer.toString();
		log.error(stackTrace);
		
		// 记录异常信息到日志里
		String errorMessage = be.getMessage();
		Log excuteLog = new Log(errorMessage, stackTrace);
    	excuteLog.setOperateTable("系统异常");
        ((IBusinessLogger) Global.getBean("BusinessLogger")).output(excuteLog);
        
        // 对指定了关键字的错误异常进行邮件提醒
        String errorKeyword = ParamConfig.getAttribute(ERROR_KEYWORD);
        if( EasyUtils.isNullOrEmpty(errorKeyword) ) {
        	errorKeyword = "java.lang.OutOfMemoryError";
        } else {
        	errorKeyword += ",java.lang.OutOfMemoryError";
        }
        List<String> errorKeywords = Arrays.asList(errorKeyword.split(","));
        
        boolean hitted = false;
        for(String ek : errorKeywords) {
        	if(stackTrace.indexOf(ek) >= 0) {
        		hitted = true;
        		break;
        	}
        }
        
        if(hitted) {
        	MailUtil.send("紧急情况，请速查看详细日志：" + errorMessage, stackTrace);
        }
    }
    
    public final static String ERROR_KEYWORD = "error.keyword";
    
}
