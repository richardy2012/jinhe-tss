package com.jinhe.tss.cms.helper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.dao.IArticleDao;
import com.jinhe.tss.cms.entity.Article;
import com.jinhe.tss.cms.entity.Attachment;
import com.jinhe.tss.framework.exception.BusinessException;
 
// TODO　重新实现附件上传功能
public class ArticleAttachExtender {

    protected final Logger log = Logger.getLogger(getClass());
    
	@Autowired private IArticleDao articleDao;

	public Object excute(File destFile, Map<String, String> params) {
		// 得到相应参数
		Long articleId = Long.valueOf( params.get("articleId") );
        Integer type = Integer.valueOf( params.get("type") );
		String attachmentName = params.get("fileName");
        String name = params.get("name");
        if (articleId == null) 
            throw new BusinessException("没有取到文章id!");
        
        //保存附件信息对象
		Attachment attachment = new Attachment();
		attachment.setName(attachmentName);
		attachment.setType(type);
		attachment.setFileName(name.substring(0, name.lastIndexOf(".")));
        attachment.setFileExt(name.substring(name.lastIndexOf(".") + 1 ));
        attachment.setUrl(CMSConstants.DOWNLOAD_SERVLET_URL);
        
        Integer seqNo = articleDao.getAttachmentNextOrder(articleId);
        attachment.setSeqNo(seqNo);
        
        Article article = new Article();
        article.setId(articleId);
        attachment.setArticle(article);
        
        String localPath = destFile.getPath();
        if(attachment.isImage()){ // 如果是图片，则为其制作缩略图
            try {
                localPath = new ImageProcessor(localPath).resize(0.68);
            } catch(Exception e) {
                log.error("制作附近图片的缩略图失败。", e);
                localPath = destFile.getPath(); // 如果缩略失败，则还是采用原图片
            }
        }
        
        String year = new SimpleDateFormat("yyyy").format(new Date());
        int index = localPath.indexOf(year);
        attachment.setLocalPath(localPath.substring(index).replaceAll("\\\\", "/"));
        attachment.setUploadDate(new Date());
        articleDao.createObject(attachment);

        //向前台返回成功信息
		String uploadName = attachment.getRelateDownloadUrl();
            
        String returnStr = "<script>parent.addAttachments(" 
                + seqNo + ",\"" + attachment.getType() + "\",\"" + uploadName + "\"," + articleId + ")</script>";
		
        return returnStr;
	}
}
