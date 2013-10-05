package com.jinhe.tss.portal;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.jinhe.tss.framework.web.servlet.AfterUpload;
import com.jinhe.tss.portal.action.FileAction;
import com.jinhe.tss.util.FileHelper;

public class MovePortalFile implements AfterUpload {

	Logger log = Logger.getLogger(this.getClass());

	public String processUploadFile(HttpServletRequest request,
			String filepath, String oldfileName) throws Exception {

		String contextPath = request.getParameter("contextPath");
		File baseDir = null;
		if (contextPath != null) {
			contextPath = FileAction.getContextPath(contextPath);
			baseDir = new File(contextPath);
		}

		File targetFile = new File(filepath);
		if (baseDir != null && targetFile != null) {
			FileHelper.copyFile(baseDir, targetFile);
		}

		return "window.parent.loadFileTree();";
	}
}
