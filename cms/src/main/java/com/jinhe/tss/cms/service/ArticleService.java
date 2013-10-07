package com.jinhe.tss.cms.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.dao.IArticleDao;
import com.jinhe.tss.cms.dao.IChannelDao;
import com.jinhe.tss.cms.entity.Article;
import com.jinhe.tss.cms.entity.Attachment;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.helper.ArticleHelper;
import com.jinhe.tss.cms.helper.ArticleQueryCondition;
import com.jinhe.tss.cms.helper.ImageProcessor;
import com.jinhe.tss.framework.persistence.pagequery.PageInfo;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.FileHelper;
 
@Service("ArticleService")
public class ArticleService implements IArticleService {
    
    protected final Logger log = Logger.getLogger(getClass());

	@Autowired private IArticleDao articleDao;
	@Autowired private IChannelDao channelDao;
 
    public Article getArticleById(Long articleId) {
        Article article = getArticleOnly(articleId);
        
        List<Attachment> attachments = articleDao.getArticleAttachments(articleId);
        article.getAttachments().addAll(attachments);
        return article;
    }
    
    public Attachment processFile(File file, Long articleId, Long channelId, int type, String kidName) {
        Channel site = channelDao.getEntity(channelId).getSite();
        String siteRootPath =  ArticleHelper.getAttachmentPath(site, type);
        File siteRootDir = new File(siteRootPath);
        
        // 将附件从上传临时目录剪切到站点指定的附件目录里
        String fileName = FileHelper.copyFile(siteRootDir, file); 
		String fileSuffix = FileHelper.getFileSuffix(fileName);
		
		// file指向剪切后的地址
		file = new File(siteRootPath + "/" +fileName); 
		
		// 保存附件信息对象
		Attachment attachment = new Attachment();
		attachment.setSeqNo(articleDao.getAttachmentIndex(articleId));
		attachment.setName(kidName);
		attachment.setType(type);
		attachment.setFileName(FileHelper.getFileNameNoSuffix(fileName));
		attachment.setFileExt(fileSuffix);
		attachment.setUrl(CMSConstants.DOWNLOAD_SERVLET_URL);
		attachment.setArticleId(articleId);
		
		// 对附件进行重命名
		fileName = System.currentTimeMillis() + "." + fileSuffix;
		String newPath = file.getParent() + "/" + fileName;
        file.renameTo(new File(newPath));
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
		
		articleDao.createObject(attachment);

		return attachment;
	}
    
	public void createArticle(Article article, Long channelId, String attachList, Long tempArticleId) {
		Channel channel = channelDao.getEntity(channelId);
		article.setChannel(channel);
 
		// set over date
		Date calculateOverDate = ArticleHelper.calculateOverDate(article, channel);
        if( calculateOverDate != null ) {
			article.setOverdueDate(calculateOverDate);
        }
		articleDao.saveArticle(article);
        
		// 处理附件
        List<String> attachSeqNos = new LinkedList<String>();
		if ( !EasyUtils.isNullOrEmpty(attachList) ) {			
            StringTokenizer st = new StringTokenizer(attachList, ",");
            while (st.hasMoreTokens()) {
                attachSeqNos.add(st.nextToken());
            }
        }
		
		List<Attachment> attachments = articleDao.getArticleAttachments(tempArticleId); // 后台查找的新建文章时上传的附件列表
        for ( Attachment attachment : attachments ) {
			Integer seqNo = attachment.getSeqNo();
            if (attachSeqNos.contains(seqNo.toString())) { // 判断附件在文章保存时是否还存在
				translatePath(attachment, article, channelId);
				
				attachment.setArticleId(article.getId());
				attachment.setSeqNo(seqNo);
				
				articleDao.update(attachment);
			}
            else {
				// 删除附件
				String[] uploadName = ArticleHelper.getAttachUploadPath(channel.getSite(), attachment);
                new File(uploadName[0]).delete();
                
                articleDao.delete(attachment);
			}
		}
	}
 
	/**
	 * 将文章内容中的临时地址替换成真实地址。
     * 主要是将临时生成的附件ID替换成附件所属文章的ID。
	 */
	private void translatePath(Attachment attachment, Article article, Long channelId) {
		// download.fun?id=1&seqNo=1
		String relateDownloadUrl = attachment.getRelateDownloadUrl(); 
        relateDownloadUrl = relateDownloadUrl.replaceAll("&", "&amp;"); //将&替换成&amp;
        
		StringBuffer sb = new StringBuffer(article.getContent());
		int index = sb.indexOf(relateDownloadUrl);
		while (index != -1) {
			StringBuffer buffer = new StringBuffer();
			int idIndex = relateDownloadUrl.indexOf("?id=");
			String realAttUploadName;
			if (idIndex != -1) {
				realAttUploadName = relateDownloadUrl.substring(0, idIndex + 4);
				realAttUploadName += article.getId() + relateDownloadUrl.substring(relateDownloadUrl.indexOf("&amp;"));
				buffer.append(sb.substring(0, index)).append(realAttUploadName).append(sb.substring(index + relateDownloadUrl.length()));
				sb = buffer;
			}
			index = sb.indexOf(relateDownloadUrl);
		}
		article.setContent(sb.toString());
	}
    
    public void updateArticle(Article article, Long channelId, String attachList) {
    	
        articleDao.update(article);
        
        // 处理附件, attachList为剩余的附件列表
        if ( !EasyUtils.isNullOrEmpty(attachList) ) {
            StringTokenizer st = new StringTokenizer(attachList, ",");
            List<String> attachSeqNos = new LinkedList<String>();
            while (st.hasMoreTokens()) {
                attachSeqNos.add(st.nextToken());
            }
            
            List<Attachment> attachments = articleDao.getArticleAttachments(article.getId());
            for ( Attachment attachment : attachments ) {
                if (attachSeqNos.contains(attachment.getSeqNo().toString())) {
                   continue;
                }
                
                // 删除附件
                articleDao.delete(attachment);
                Channel site = channelDao.getSiteByChannel(channelId);
                String path = site.getPath() + "/" + site.getAttanchmentPath(attachment) + "/" + attachment.getLocalPath();
                new File(path).delete();
            }
        }
    }

    public Article deleteArticle(Long articleId) {
        Article article = getArticleOnly(articleId);
        articleDao.deleteArticle(article);
        
        return article;
	}
    
    private Article getArticleOnly(Long articleId){
        Article article = articleDao.getEntity(articleId);
        if (article == null) {
            throw new NullPointerException("未找到文章！");
        }
        
        return article;
    }
 
    public void moveArticle(Long articleId, Long channelId) {
        Article article = articleDao.getEntity(articleId);
        Channel channel = channelDao.getEntity(channelId);
        article.setChannel(channel);
        
        articleDao.update(article);
	}
 
	public Article doTopArticle(Long articleId) {
	    Article article = getArticleOnly(articleId);
	    article.setIsTop(article.getIsTop() == 0 ? 1 : 0);
		articleDao.update(article);
		
		return article;
	}
 
    public PageInfo getChannelArticles(Long channelId, Integer page, String...orderBy) {
       if( !channelDao.checkBrowsePermission(channelId) ) {
            log.error("用户【" + Environment.getOperatorName() + "】试图访问没有文章浏览权限的栏目【" + channelId + "】");
            return new PageInfo();
        }
        return articleDao.getPageList(channelId, page, orderBy);
    }

	public Object[] searchArticleList(ArticleQueryCondition condition) {
		// 将用户有浏览权限的选定栏目下子栏目ID列表存入临时表中
		Long channelId = condition.getChannelId();
        List<Channel> children = channelDao.getChildrenById(channelId, CMSConstants.OPERATION_VIEW);
		channelDao.insertEntityIds2TempTable(children);
		condition.setChannelId(null);
 
		PageInfo page = articleDao.getSearchArticlePageList(condition);
        List<?> list = page.getItems();
        
		List<Article> articleList = new ArrayList<Article>();
		if ( !EasyUtils.isNullOrEmpty(list) ) {
			for ( Object temp : list ) {
                Article article = ArticleHelper.createArticle((Object[]) temp);
				articleList.add(article);
			}
		}
		return new Object[]{articleList, page};
	}
}