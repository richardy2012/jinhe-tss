package com.jinhe.tss.um.servlet;

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
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.web.dispaly.XmlPrintWriter;
import com.jinhe.tss.framework.web.dispaly.xmlhttp.XmlHttpEncoder;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.service.IResourceService;

@WebServlet(urlPatterns="/auth/importapp")
@MultipartConfig(location = Config.UPLOAD_PATH, maxFileSize = 1024*1024*20)
public class ImportAppServlet extends HttpServlet {

	private static final long serialVersionUID = 111131240580072842L;

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
            Document doc = new SAXReader().read(targetFile);
            
            IResourceService resourceService = (IResourceService) Global.getContext().getBean("ResourceService");
            resourceService.applicationResourceRegister(doc, UMConstants.PLATFORM_SYSTEM_APP);
            
            encoder.put("SCRIPT", "alert(\"导入成功！\");loadInitData();");
        } 
        catch (Exception e) {
            log.error("导入失败，请查看日志信息！", e);
            encoder.put("SCRIPT", "alert(\"导入失败，请查看日志信息！\");"); 
        }
        
        encoder.print(new XmlPrintWriter(response.getWriter()));
	}
}