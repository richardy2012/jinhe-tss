package com.jinhe.tss.cms;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.jinhe.tss.cms.service.IRemoteArticleService;
import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.exception.BusinessServletException;
import com.jinhe.tss.util.EasyUtils;

/** 
 * <p> DownloadServlet.java </p> 
 * 下载文章附件。传入文章ID以及附件的序号即可下载该附件。
 * 如果是Portal等其它应用配置该servlet，需要这些应用和CMS部署在同一台机器上才行。
 */
@WebServlet(urlPatterns="*.download")
public class DownloadServlet extends HttpServlet {

	private static final long serialVersionUID = -6788424017181628016L;
    
    private Logger log = Logger.getLogger(this.getClass());
    
    IRemoteArticleService service;
    
    public void init() {
    	service = (IRemoteArticleService) Global.getContext().getBean("RemoteArticleService");
    }

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id;
        Integer seqNo;
        try{
    	    id = new Long(request.getParameter("id"));
    	    seqNo = new Integer(request.getParameter("seqNo"));
        } catch(Exception e) {
            throw new BusinessServletException("下载附件时参数值有误", e);
        }
        
	    AttachmentDTO attachment = service.getAttachmentInfo(id, seqNo);
        if(attachment == null){
        	log.error("附件不存在");
            return;
        }
        
        response.reset(); // 设置附件下载页面

        String docOrPicPath = ""; 
        String fileName = attachment.getFileName();
        String fileExt = attachment.getFileExt();
	    if(attachment.isImage()) { // 相关图片
	    	docOrPicPath = attachment.getBasePath()[2];
	    	 
            if("gif".equals(fileExt)) { response.setContentType("image/gif"); }
            if("jpg".equals(fileExt)) { response.setContentType("image/jpeg"); }
            if("jpeg".equals(fileExt)){ response.setContentType("image/jpeg"); }
            if("png".equals(fileExt)) { response.setContentType("image/png"); }
            if("bmp".equals(fileExt)) { response.setContentType("image/bmp"); }
	    }
	    else if(attachment.isOfficeDoc()){ // 相关附件
            docOrPicPath = attachment.getBasePath()[1];
	        response.setContentType("application/octet-stream");// 设置附件类型
            
            fileName = (fileExt != null && !"".equals(fileExt)) ? (fileName + "." + fileExt) : fileName;
            response.setHeader("Content-Disposition", "attachment; filename=\"" + EasyUtils.toUtf8String(fileName) + "\"");        
	    }
        
        String filePath = attachment.getBasePath()[0] + "/" + docOrPicPath + "/" + attachment.getLocalPath();
        ServletOutputStream out = null;
        FileInputStream stream = null;
        try {
            out = response.getOutputStream();
            File file = new File(filePath);
            stream = new FileInputStream(file);

            int bytesRead = 0;
            final int length = 8192;
            byte[] buffer = new byte[length];
            while ((bytesRead = stream.read(buffer, 0, length)) != -1) {
                out.write(buffer, 0, bytesRead);
                out.flush();
            }
        } catch (IOException e) {
            log.error("下载附件时IO异常，Message:" + e.getMessage(), e);
        } finally {
            if (stream != null) {
                stream.close();
            }
            if (out != null) {
                out.close();
            }
        }		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    doGet(request, response);
	}
}

