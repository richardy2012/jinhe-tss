package com.jinhe.tss.framework.web.servlet;

import java.io.File;
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

import com.jinhe.tss.framework.component.param.ParamConfig;
import com.jinhe.tss.framework.exception.ExceptionEncoder;
import com.jinhe.tss.framework.web.dispaly.XmlPrintWriter;
import com.jinhe.tss.framework.web.dispaly.xmlhttp.XmlHttpEncoder;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.FileHelper;

@WebServlet(urlPatterns="/auth/file/upload")
@MultipartConfig(maxFileSize = 1024 * 1024 * 20)
public class Servlet4Upload extends HttpServlet {

	private static final long serialVersionUID = -6423431960248248353L;

	public static final String UPLOAD_PATH = "upload_path";

	Logger log = Logger.getLogger(this.getClass());

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		XmlHttpEncoder encoder = new XmlHttpEncoder();
		try {
	        Part part = request.getPart("file");
			String script = doUpload(request, part); // 自定义输出到指定目录
			if(script != null) {
				encoder.put("script", script);
			}
		} catch (Exception e) {
			log.error("上传失败：" + e.getMessage() + ", " + ExceptionEncoder.getFirstCause(e).getMessage() );
			encoder.put("script", "alert(\"上传失败，请检查文件是否过大，单个文件最大不宜超过3M。如果过大，请压缩处理后再上传。！\");");
		}

		response.setContentType("text/html;charset=utf-8");
		encoder.print(new XmlPrintWriter(response.getWriter()));
	}
	
	String doUpload(HttpServletRequest request, Part part) throws Exception {
		String uploadPath = ParamConfig.getAttribute(UPLOAD_PATH);
		if( uploadPath == null ) { // gets absolute path of the web application
			uploadPath = request.getServletContext().getRealPath("");
		}
		
        String savePath = uploadPath + File.separator + "uploadFile";
   	 
        File fileSaveDir = new File(savePath);
        if ( !fileSaveDir.exists() ) {
            fileSaveDir.mkdirs();
        }
        
		// 获取上传的文件真实名字(含后缀)
		String contentDisp = part.getHeader("content-disposition");
		String fileName = "";
		String[] items = contentDisp.split(";");
		for (String item : items) {
			if (item.trim().startsWith("filename")) {
				fileName = item.substring(item.indexOf("=") + 2, item.length() - 1);
				break;
			}
		}
		
		String subfix = FileHelper.getFileSuffix(fileName);
        String newFileName = System.currentTimeMillis() + "." + subfix;
        String newFilePath = savePath + File.separator + newFileName;
        
        // 自定义输出到指定目录
		InputStream is = part.getInputStream();
		FileOutputStream fos = new FileOutputStream(newFilePath);
		int data = 0;
		while((data = is.read()) != -1) {
		  fos.write(data);
		}
		fos.close();
		is.close();
		
		String afterUploadClass = request.getParameter("afterUploadClass");
		if(afterUploadClass != null) {
			AfterUpload afterUpload = (AfterUpload) BeanUtil.newInstanceByName(afterUploadClass);
			return afterUpload.processUploadFile(request, newFilePath, fileName);
		}
		return null;
	}
}
