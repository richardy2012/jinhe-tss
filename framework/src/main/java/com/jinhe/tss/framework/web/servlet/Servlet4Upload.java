package com.jinhe.tss.framework.web.servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.FileHelper;

@WebServlet(urlPatterns="/auth/file/upload")
@MultipartConfig(maxFileSize = 1024 * 1024 * 20)
public class Servlet4Upload extends HttpServlet {

	private static final long serialVersionUID = -6423431960248248353L;

	Logger log = Logger.getLogger(this.getClass());

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String afterUploadClass = request.getParameter("afterUploadClass");
		response.setContentType("text/html;charset=utf-8");
		XmlHttpEncoder encoder = new XmlHttpEncoder();
		
		try {
			String uploadPath = Config.UPLOAD_PATH;
			if( uploadPath == null ) {
				// gets absolute path of the web application
				uploadPath = request.getServletContext().getRealPath("");
			}
	        
	        String savePath = uploadPath + File.separator + "uploadFile";
	 
	        File fileSaveDir = new File(savePath);
	        if ( !fileSaveDir.exists() ) {
	            fileSaveDir.mkdir();
	        }
	 
	        Part part = request.getPart("file");
            String fileName = extractFileName(part);
            String subfix = FileHelper.getFileSuffix(fileName);
            String newFileName = System.currentTimeMillis() + "." + subfix;
            String newFilePath = savePath + File.separator + newFileName;
            
	        part.write(newFilePath); // 写到指定的目录下,jetty先失败
//			part.write(newFileName); // 写到默认上传目录下,jetty下成功
			
			writePart(part, newFilePath); // 自定义输出到指定目录
	        
			String script = "alert('upload sucess!')";
			if(afterUploadClass != null) {
				AfterUpload afterUpload = (AfterUpload) BeanUtil.newInstanceByName(afterUploadClass);
				script = afterUpload.processUploadFile(request,newFilePath, fileName);
			}
	        
			encoder.put("SCRIPT", script);
			
		} catch (Exception e) {
			log.error("上传失败，请查看日志信息！", e);
			encoder.put("SCRIPT", "alert(\"上传失败，请查看日志信息！\");");
		}

		encoder.print(new XmlPrintWriter(response.getWriter()));
	}

	private void writePart(Part part, String newFilePath) throws IOException, FileNotFoundException {
		InputStream is = part.getInputStream();
		FileOutputStream fos = new FileOutputStream(newFilePath);
		int data = 0;
		while((data = is.read()) != -1) {
		  fos.write(data);
		}
		fos.close();
		is.close();
	}

	// 获取上传的文件真实名字(含后缀)
	private String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String item : items) {
			if (item.trim().startsWith("filename")) {
				return item.substring(item.indexOf("=") + 2, item.length() - 1);
			}
		}
		return "";
	}
}
