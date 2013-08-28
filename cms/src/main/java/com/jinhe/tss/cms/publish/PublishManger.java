package com.jinhe.tss.cms.publish;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.entity.Article;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.helper.ArticleHelper;
import com.jinhe.tss.cms.service.IChannelService;
import com.jinhe.tss.framework.component.progress.Progress;
import com.jinhe.tss.framework.component.progress.ProgressManager;
import com.jinhe.tss.framework.component.progress.Progressable;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.util.FileHelper;

/**
 * 文章发布，以及全文检索索引发布
 */
@Component("PublishManger")
public class PublishManger implements Progressable {

    public static final int PAGE_SIZE = 100; // 每页记录个数

    @Autowired private IChannelService channelService;

	/**
     * 发布文章（注：完全发布的话已经发布的也重新发布，增量发布则只发布流程为“待发布”的文章）
     * 
     * @param channelId 栏目ID
     * @param category  1:增量发布 2:完全发布 
     */
	public String publishArticle(Long channelId, String category) {
		// 判断是否对该栏目有发布权限
		checkPublishPermission(channelId);
        
        int totalRows = channelService.getTotalRows4Publish(channelId, category);       
        int totalPageNum = totalRows / PAGE_SIZE ;
        if( totalRows % PAGE_SIZE > 0 ) {
            totalPageNum = totalPageNum + 1;
        }
        
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("channelId", channelId);
        paramsMap.put("category", category);
        paramsMap.put("totalPageNum", totalPageNum);
            
        ProgressManager manager = new ProgressManager(this, totalRows, paramsMap);
        return manager.execute();
	}
	
	/**
	 * 判断是否对该栏目有发布权限
	 */
	public void checkPublishPermission(Long channelId) {
	    String appId = UMConstants.TSS_APPLICATION_ID;
	    String resourceType = CMSConstants.RESOURCE_TYPE_CHANNEL;
	    String operation = CMSConstants.OPERATION_PUBLISH;
	    List<Long> permitedList = PermissionHelper.getInstance().getResourceIdsByOperation(appId, resourceType, operation);
        if ( !permitedList.contains(channelId) ) {
        	Channel channel = channelService.getChannelById(channelId);
            throw new BusinessException("您没有发布本栏目（站点）【id = " + channelId + "， name = " + channel.getName() + "】的权限！");
        }
	}
    
    public void execute(Map<String, Object> params, final Progress progress) {
        Long    channelId = (Long) params.get("channelId");
        String  category = (String) params.get("category");
        Integer totalPageNum = (Integer) params.get("totalPageNum");
        
        // 分页发布文章
        for (int page = 1; page <= totalPageNum; page++) {
            List<Article> pageArticleList = channelService.getPageArticleList(channelId, page, PAGE_SIZE, category);
            
            channelService.publishArticle(pageArticleList);
            progress.add(pageArticleList.size());
        }
        // 如果循环结束了进度还没有完成，则取消进度（不取消会导致页面一直在请求进度信息）
        if( !progress.isCompleted() ) {
            progress.add(8888888); // 通过设置一个大数（远大于总数）来使进度完成
        }
    }

	/**
	 * 生成单个文章发布文件
	 * @param article 
	 * @param publishPath
	 * @return
	 */
	public static String publishOneArticle(Article article, String publishPath) {
        // 删除已发布的文章，如果有的话
        String pubUrl = article.getPubUrl();
        if(pubUrl != null) {
            new File(pubUrl).delete();
        }
        
		// 生成发布路径
		File publishDir = new File(publishPath);
		if (!publishDir.exists())
			publishDir.mkdirs();
		
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(ArticleHelper.getSystemEncoding()); //一般：windows “GBK” linux “UTF－8”
		Element articleNode = doc.addElement("Article");
		
        Map<String, Object> articleAttributes = article.getAttributesForXForm(); //包含文章的所有属性，包括动态属性
        articleAttributes.remove("content");

		String className = article.getChannel().getPublishArticleClassName();
		PublishArticleFactory.getInstance(className).publishArticle(articleNode, articleAttributes);
		Element eleCnt = articleNode.addElement("content");
		eleCnt.addCDATA(article.getContent());
        
		// 发布文章对文章附件的处理
		Element eleAtts = articleNode.addElement("Attachments");
        ArticleHelper.addPicListInfo(eleAtts, article.getAttachments());
        
        // 以 “栏目ID_文章ID.xml” 格式命名文章发布的xml文件
        String fileName = article.getChannel().getId() + "_" + article.getId() + ".xml";
		String filePathAndName = publishPath + "/" + fileName;
        FileHelper.writeXMLDoc(doc, filePathAndName);
		return filePathAndName;
	}
    
    /**
     * 文章发布，用于定时发布（也可用于手动即时发布调用）
     * @param channelIds
     * @param progress
     */
    public void publishArticle4TimerJob(List<Long> channelIds, Progress progress) {
        for ( Long channelId : channelIds ) {
            int totalRows = channelService.getPublishableArticleCount(channelId);       
            int totalPageNum = totalRows / PAGE_SIZE ;
            if( totalRows % PAGE_SIZE > 0 ) {
                totalPageNum = totalPageNum + 1;
            }
            
            for (int page = 1; page <= totalPageNum; page++) { // 逐页发布文章
                List<Article> articleList = channelService.getPagePublishableArticleList(channelId, page, PAGE_SIZE);
                channelService.publishArticle(articleList);
                
                progress.add(articleList.size()); // 更新进度条信息进度条
            }
        }
    }
    
    /**
     * 获取可发布的文章总数量，用于进度条计算。
     * @param channelIdStr
     * @param paramsMap
     * @return
     */
    public int getPublishableArticleCount4TimerJob(List<Long> channelIds, Map<String, Object> paramsMap) {
        int total = 0;
        List<Long> channelIdList = new ArrayList<Long>();
        for ( Long channelId : channelIds ) {
            try { 
            	// 判断是否对该栏目有发布权限
                checkPublishPermission(channelId);
                
                channelIdList.add(channelId);
                total += channelService.getPublishableArticleCount(channelId);
                
            } catch (Exception e) {
                // do nothing
            }
        }
        
        paramsMap.put("channelIds", channelIdList);
        
        return total;
    }
}
