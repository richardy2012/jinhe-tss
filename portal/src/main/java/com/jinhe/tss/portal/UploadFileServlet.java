package com.jinhe.tss.portal;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.web.dispaly.XmlPrintWriter;
import com.jinhe.tss.framework.web.dispaly.xmlhttp.XmlHttpEncoder;
import com.jinhe.tss.portal.action.FileAction;
import com.jinhe.tss.util.FileHelper;

@WebServlet(urlPatterns="/auth/portal/file/upload")
@MultipartConfig(location = Config.UPLOAD_PATH, maxFileSize = 1024*1024*20)
public class UploadFileServlet extends HttpServlet {
	
	private static final long serialVersionUID = 3768999662560249210L;
	
	Logger log = Logger.getLogger(this.getClass());
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        XmlHttpEncoder encoder = new XmlHttpEncoder();
        try {
    		Part part = request.getPart("file");
    		
    		// 获取上传的文件真实名字(含后缀)
    		String header = part.getHeader("content-disposition");
    		int fromIndex = header.lastIndexOf("=") + 2;
    		String fileName = header.substring(fromIndex, header.lastIndexOf("\""));
    		
    		String filePath = Config.UPLOAD_PATH + "/" + fileName;
            File targetFile = new File(filePath);
            
    		// 上传文件(写入磁盘)
            if( !targetFile.exists() ) {
            	targetFile.mkdirs();
            }
            part.write(fileName);
            
            String contextPath = request.getParameter("contextPath");
			File baseDir = null;
			if (contextPath != null) {
				contextPath = FileAction.getContextPath(contextPath);
				baseDir = new File(contextPath);
			}

			if (baseDir != null && targetFile != null) {
				FileHelper.copyFile(baseDir, targetFile);
			}
          
            encoder.put("SCRIPT", "window.parent.loadFileTree();");
        } 
        catch (Exception e) {
            log.error("上传失败，请查看日志信息！", e);
            encoder.put("SCRIPT", "alert(\"上传失败，请查看日志信息！\");"); 
        }
        
        encoder.print(new XmlPrintWriter(response.getWriter()));
	}
}
