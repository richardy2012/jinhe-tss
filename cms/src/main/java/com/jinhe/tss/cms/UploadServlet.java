package com.jinhe.tss.cms;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

import com.jinhe.tss.cms.entity.Article;
import com.jinhe.tss.cms.entity.Attachment;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.helper.ArticleHelper;
import com.jinhe.tss.cms.helper.ImageProcessor;
import com.jinhe.tss.cms.service.IArticleService;
import com.jinhe.tss.cms.service.IChannelService;
import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.web.dispaly.XmlPrintWriter;
import com.jinhe.tss.framework.web.dispaly.xmlhttp.XmlHttpEncoder;
import com.jinhe.tss.util.FileHelper;

@WebServlet(urlPatterns="/auth/cms/upload")
@MultipartConfig(location = Config.UPLOAD_PATH, maxFileSize = 1024*1024*20)
public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = -6423431960248248353L;
	
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        XmlHttpEncoder encoder = new XmlHttpEncoder();
        try {
    		Part part = request.getPart("file");
    		
    		// 获取上传的文件真实名字(含后缀)
    		String header = part.getHeader("content-disposition");
    		int fromIndex = header.lastIndexOf("=") + 2;
    		String fileName = header.substring(fromIndex, header.lastIndexOf("\""));
    		
    		// 上传文件(写入磁盘)
    		part.write(fileName);
    		
    		String filePath = Config.UPLOAD_PATH + "/" + fileName;
            File targetFile = new File(filePath);
            
            Attachment attachObj = processFile(file, articleId, type, kidName);
            
            
            IArticleService articleService = (IArticleService) Global.getContext().getBean("ArticleService");
            
            articleService.getArticleById(articleId).;
            Channel  site = channel.getSite();
            return ArticleHelper.getAttachmentPath(site, type);
            
            // 向前台返回成功信息
    		String uploadName = attachObj.getRelateDownloadUrl();

    		String returnStr = "<script>parent.addAttachments("
    				+ attachObj.getType() + "\",\"" + attachObj + "\","
    				+ articleId + ")</script>";
            encoder.put("SCRIPT", returnStr);
        } 
        catch (Exception e) {
            log.error("上传失败，请查看日志信息！", e);
            encoder.put("SCRIPT", "alert(\"上传失败，请查看日志信息！\");"); 
        }
        
        encoder.print(new XmlPrintWriter(response.getWriter()));
	}
	
	private String getPath(Channel channel, int type) {
		Channel  site = channel.getSite();
        return ArticleHelper.getAttachmentPath(site, type);
	}
	
	private Attachment processFile(File file, Long articleId, int type, String kidName) {
		String fileName = file.getName();
		String fileSuffix = FileHelper.getFileSuffix(fileName);
		
		// 保存附件信息对象
		Attachment attachment = new Attachment();
		attachment.setName(kidName);
		attachment.setType(type);
		attachment.setFileName(FileHelper.getFileNameNoSuffix(fileName));
		attachment.setFileExt(fileSuffix);
		attachment.setUrl(CMSConstants.DOWNLOAD_SERVLET_URL);
		
		Article article = new Article();
		article.setId(articleId);
		attachment.setArticle(article);
		
		// 对附件进行重命名
		fileName = System.currentTimeMillis() + fileSuffix;
		
		FileHelper.renameFile(file.getPath(), fileName);
		String newPath = file.getParent() + "/" +fileName;
		file = new File(newPath);

		if (attachment.isImage()) { // 如果是图片，则为其制作缩略图
			try {
				newPath = new ImageProcessor(newPath).resize(0.68);
			} catch (Exception e) {
				log.error("制作附近图片的缩略图失败。", e);
				newPath = file.getPath(); // 如果缩略失败，则还是采用原图片
			}
		}

		String year = new SimpleDateFormat("yyyy").format(new Date());
		int index = newPath.indexOf(year);
		attachment.setLocalPath(newPath.substring(index).replaceAll("\\\\", "/"));
		attachment.setUploadDate(new Date());

		return attachment;
	}
}