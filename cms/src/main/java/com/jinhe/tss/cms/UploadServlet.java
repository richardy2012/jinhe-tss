package com.jinhe.tss.cms;

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

import com.jinhe.tss.cms.entity.Attachment;
import com.jinhe.tss.cms.service.IArticleService;
import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.web.dispaly.XmlPrintWriter;
import com.jinhe.tss.framework.web.dispaly.xmlhttp.XmlHttpEncoder;

@WebServlet(urlPatterns="/auth/cms/upload")
@MultipartConfig(location = Config.UPLOAD_PATH, maxFileSize = 1024*1024*20)
public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = -6423431960248248353L;
	
	protected Logger log = Logger.getLogger(this.getClass());
	
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
            	part.write(fileName);
            }
            
            Long articleId = Long.parseLong(request.getParameter("articleId"));
			Long channelId = Long.parseLong(request.getParameter("channelId"));
            int type = Integer.parseInt(request.getParameter("type"));
			String petName = request.getParameter("petName");
			if(petName == null) {
				petName = fileName;
			}
			
            // 保存附件信息
            IArticleService articleService = (IArticleService) Global.getContext().getBean("ArticleService");
			Attachment attachObj = articleService.processFile(targetFile, articleId, channelId, type, petName);
            
            // 向前台返回成功信息
    		String downloadUrl = attachObj.getRelateDownloadUrl();
    		Integer seqNo = attachObj.getSeqNo();
			String script = "<script>parent.addAttachments(" + seqNo + ", " + type + ", '" + downloadUrl + "', " + articleId + ")</script>";
            encoder.put("SCRIPT", script);
        } 
        catch (Exception e) {
            log.error("上传失败，请查看日志信息！", e);
            encoder.put("SCRIPT", "alert(\"上传失败，请查看日志信息！\");"); 
        }
        
        encoder.print(new XmlPrintWriter(response.getWriter()));
	}
}