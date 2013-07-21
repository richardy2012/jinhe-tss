package com.jinhe.tss.portal.engine;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.component.param.Param;
import com.jinhe.tss.framework.component.param.ParamConfig;
import com.jinhe.tss.framework.component.param.ParamManager;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.URLUtil;

import freemarker.template.TemplateException;

/** 
 * 支持freemarker引擎解析的Action基类。
 * 
 * 可用于组件的预览等功能。
 * 
 */
public abstract class FreeMarkerSupportAction extends BaseActionSupport {
    
    /**  用户自定义的参数（在参数模块中，名称为userdefinedParams）  */
    private static final String USER_DEFINED_PARAMS = "userdefinedParams";

	private static final String USE_FREEMARKER = "useFreemarker"; // 是否解析

	protected Logger log = Logger.getLogger(this.getClass());
    
    protected Long portalId; 

    public void setPortalId(Long portalId) {
        this.portalId = portalId;
    }
    
    /**
     * 解析Freemarker模板
     * @param template
     * @return
     */
    protected void printHTML(String template){
        printHTML(template, true);
    }
    
    protected void printHTML(String template, boolean parseTwice){
        if( !Config.TRUE.equalsIgnoreCase(ParamConfig.getAttribute(USE_FREEMARKER)) ){
            print(template);
            return;
        }
        
        try {
            FreemarkerParser parser = getFreemarkerParser();
            if(parser.isFtlTemplateReady){
                HttpServletResponse response = Context.getResponse();
                response.setContentType("text/html;charset=GBK");
                if(parseTwice) { // 是否需要解析两次
                    parser.parseTemplateTwice(template, response.getWriter());
                } else {
                    parser.parseTemplate(template, response.getWriter());
                }
                return;
            }
        } catch (Exception e) {
            log.error("执行Freemarker引擎解析时候出错", e);
            String errorInfo = "执行Freemarker引擎解析时候出错,请联系管理员！<br/>错误信息:(" + e.getMessage() + ")<br/>";
            if(e instanceof TemplateException){
                print(errorInfo + ((TemplateException)e).getFTLInstructionStack() + template);
            }else {
                print(errorInfo + e);
            }
            return;
        } 
        
        print(template);
    }
    
	protected FreemarkerParser getFreemarkerParser(){
        HttpServletRequest  request = Context.getRequestContext().getRequest();
        
        //设置门户资源路进为Freemarker模板路径
        FreemarkerParser parser = new FreemarkerParser(getPortalResourcesPath());
        if(parser.isFtlTemplateReady){
        	
            // 将本次http请求中带的参数放入到freemarker数据模型中，
        	// 以方便调用，因为是每次请求都新建一个FreemarkerParser，所以不会存在多线程问题。
            parser.putParameters(request.getParameterMap()); 
            
            parser.putParameters(getUserDefineParams()); 
        }
        return parser;
    }
    
    protected File getPortalResourcesPath(){
        URL url = URLUtil.getWebFileUrl(PortalConstants.PORTAL_MODEL_DIR);
        List<File> list = FileHelper.listSubDir(new File(url.getFile()));
        for( File file : list ){
            if(file.getName().endsWith("_" + portalId)) //判断文件夹路径是否以 "_12"这样结尾，是的话表示是当前门户的资源文件夹
                return file;
        }
        return null;
    }
    
    // 获取用户自定义的参数（在参数模块中，名称为userdefinedParams）
    protected Map<String, String> getUserDefineParams(){
        Map<String, String> paramsMap = new HashMap<String, String>();
        try{
            List<Param> params = ParamManager.getComboParam(USER_DEFINED_PARAMS);
            if(params != null){
                for( Param p : params ){
                    paramsMap.put(p.getText(), p.getValue());
                }
            }
        } catch(Exception e) {
            //do nothing
        }
        return paramsMap;
    }

}

