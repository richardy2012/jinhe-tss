package com.jinhe.tss.cms;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.jinhe.tss.cms.entity.Attachment;
import com.jinhe.tss.cms.service.IArticleService;
import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.web.servlet.AfterUpload;

public class CreateAttach implements AfterUpload {

	Logger log = Logger.getLogger(this.getClass());

	public String processUploadFile(HttpServletRequest request,
			String filepath, String oldfileName) throws Exception {

		Long articleId = Long.parseLong(request.getParameter("articleId"));
		Long channelId = Long.parseLong(request.getParameter("channelId"));
		int type = Integer.parseInt(request.getParameter("type"));
		String petName = request.getParameter("petName");
		if (petName == null) {
			petName = oldfileName;
		}

		// 保存附件信息
		File targetFile = new File(filepath);
		IArticleService articleService = (IArticleService) Global.getContext().getBean("ArticleService");
		Attachment attachObj = articleService.processFile(targetFile, articleId, channelId, type, petName);

		// 向前台返回成功信息
		String downloadUrl = attachObj.getRelateDownloadUrl();
		Integer seqNo = attachObj.getSeqNo();
		return "<script>parent.addAttachments(" + seqNo + ", " + type + ", '"
				+ downloadUrl + "', " + articleId + ")</script>";
	}
}