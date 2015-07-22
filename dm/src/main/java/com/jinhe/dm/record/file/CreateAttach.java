package com.jinhe.dm.record.file;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.jinhe.dm.record.RecordService;
import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.web.servlet.AfterUpload;
import com.jinhe.tss.util.FileHelper;

public class CreateAttach implements AfterUpload {

	Logger log = Logger.getLogger(this.getClass());

	public String processUploadFile(HttpServletRequest request,
			String filepath, String oldfileName) throws Exception {

		Long recordId  = Long.parseLong(request.getParameter("recordId"));
		Long itemId = Long.parseLong(request.getParameter("itemId"));
		int type = Integer.parseInt(request.getParameter("type"));
		
		int separatorIndex = oldfileName.lastIndexOf("\\");
		if(separatorIndex > 0) {
			oldfileName = oldfileName.substring(separatorIndex + 1);
		}
		separatorIndex = oldfileName.lastIndexOf("/");
		if(separatorIndex > 0) {
			oldfileName = oldfileName.substring(separatorIndex + 1);
		}

		// 保存附件信息
		File targetFile = new File(filepath);
		RecordAttach attachObj = saveAttach(targetFile, recordId, itemId, type, oldfileName);

		// 向前台返回成功信息
		String downloadUrl = attachObj.getDownloadUrl();
		Integer seqNo = attachObj.getSeqNo();
		String fileName = attachObj.getFileName();
		String fileExt = attachObj.getFileExt();
		
		return "parent.addAttach(" + seqNo + ", '" + fileName + "', '" 
				+ fileExt + "', '" + oldfileName + "', '" + downloadUrl + "')";
	}
	
	private RecordAttach saveAttach(File file, Long recordId, Long itemId, int type, String oldfileName) {
		RecordService recordService = (RecordService) Global.getBean("RecordService");
		
		// 保存附件信息对象
		RecordAttach attach = new RecordAttach();
		attach.setType(type);
		attach.setName(oldfileName);
		attach.setRecordId(recordId);
		attach.setItemId(itemId);
		attach.setSeqNo(recordService.getAttachSeqNo(recordId, itemId));
		attach.setUploadDate(new Date());
		attach.setUploadUser(Environment.getUserId());
		
        String attachDir = RecordAttach.getAttachDir(recordId, itemId);
        File rootDir = new File(attachDir);
        
        // 将附件从上传临时目录剪切到站点指定的附件目录里
        String fileName = FileHelper.copyFile(rootDir, file); 
		String fileSuffix = FileHelper.getFileSuffix(fileName);
		
		// file指向剪切后的地址
		file = new File(rootDir + "/" +fileName); 
		
		// 对附件进行重命名
		fileName = System.currentTimeMillis() + "." + fileSuffix;
		String newPath = file.getParent() + "/" + fileName;
        file.renameTo(new File(newPath));
        
        attach.setFileName(fileName);
        attach.setFileExt(fileSuffix.toLowerCase());
		
        recordService.createAttach(attach);

		return attach;
	}
}