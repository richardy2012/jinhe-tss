package com.jinhe.tss.cms.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.dao.IArticleDao;
import com.jinhe.tss.cms.dao.IChannelDao;
import com.jinhe.tss.cms.entity.Article;
import com.jinhe.tss.cms.entity.Attachment;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.helper.ArticleHelper;
import com.jinhe.tss.cms.publish.PublishManger;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.util.EasyUtils;

public class ChannelService implements IChannelService {

    @Autowired private IChannelDao channelDao;
    @Autowired private IArticleDao articleDao;
     
    public List<?> getAllSiteChannelList() {
        return channelDao.getAllSiteChannelList();
    }
    
    public List<?> getAllChannels() {
        String hql = "from Channel c where c.id <> c.site.id order by c.decode";
        return channelDao.getEntities(hql);
    }
    
    public Channel getSiteById(Long siteId) {
        Channel site = channelDao.getEntity( siteId );
        if (site == null) {
            throw new BusinessException("您选中站点不存在，可能已经被删除掉了！");
        }

        return site;
    }
    
	public Channel getChannelById(Long id) {
        Channel channel = channelDao.getEntity(id);
        if (channel == null) {
            throw new BusinessException("选择栏目不存在！id = " + id);
        }
        return channel;
	}
	
	public Channel createChannel(Channel channel) {
        Long parentId = channel.getParentId();
        Integer nextSeqNo = channelDao.getNextSeqNo(parentId);
		channel.setSeqNo(nextSeqNo);
		
        channel = channelDao.create(channel);
        
        if( CMSConstants.HEAD_NODE_ID.equals(parentId) ) {
            channel.setSite(channel);
        }
        else {
            Channel parent = channelDao.getEntity(parentId);
            channel.setSite(parent.getSite());
        }
        
	    return channel;
    }
	
	public Channel updateChannel(Channel channel) {
		channelDao.update(channel);
	    return channel;
    }
	
    public Channel createSite(Channel site) {
        checkPath(site.getPath(), site.getDocPath(), site.getImagePath());
        
        site.setSeqNo(channelDao.getNextSeqNo(CMSConstants.HEAD_NODE_ID));
        site = channelDao.create(site);
        
        site.setSite(site);
        return site;
    }
    
    public Channel updateSite(Channel site) {
        checkPath(site.getPath(), site.getDocPath(), site.getImagePath());
        channelDao.update(site);
        return site;
    }
    
    /** 检测输入路径的正确性 */
    private void checkPath(String path, String docPath, String imgPath) {
        checkPath(path,    "站点的发布路径填写错误，不能生成相应的发布文件路径！");
        checkPath(docPath, "站点的附件上传根路径填写错误，不能生成相应的附件上传根路径路径！");
        checkPath(imgPath, "站点的图片根路径填写错误，不能生成相应的图片根路径路径！");
    }
    
    private void checkPath(String path, String errorMSg){
        File file = new File(path);
        if (!(file.exists() == true && file.isDirectory() == true && file.canWrite() == true)
                && !(file.exists() == false && file.mkdirs() == true)) {
            throw new BusinessException(errorMSg);
        }
    }
 
	public void deleteChannel(Long channelId) {
		if (!checkDownPermission(channelId, CMSConstants.OPERATION_DELETE)) {
			throw new BusinessException("您没有删除该栏目的足够权限！");
		}
		Channel channel = channelDao.getEntity(channelId);
		
		// 先删除栏目下文章
        String hql = "from Article a where a.channel.decode like ?";
        List<?> articleList = channelDao.getEntities(hql, channel.getDecode() + "%");
		for ( Object temp : articleList ) {
			Article article = (Article) temp;
			articleDao.deleteArticle(article);
		}
		
        // 删除自己和子栏目
		List<Channel> subChannels = channelDao.getChildrenById(channelId);
		channelDao.deleteAll(subChannels);
	}
	
    /** 对栏目向下权限的判断: 如果个数不相等，说明用户某些子节点不具有指定的权限 */
    protected boolean checkDownPermission(Long resourceId, String operationId) {
        List<Channel> rsourceList  = channelDao.getChildrenById(resourceId);
        List<?> permitedList = channelDao.getChildrenById(resourceId, operationId);

        return rsourceList.size() == permitedList.size();
    }
 
	public void sortChannel(Long channelId, Long toChannelId, Integer direction) {
        channelDao.sort(channelId, toChannelId, direction);
    }
 
    public void moveChannel(Long channelId, Long targetId) {
        Channel channel = channelDao.getEntity(channelId);
        channel.setSeqNo(channelDao.getNextSeqNo(targetId));
        channel.setParentId(targetId);
        channelDao.moveChannel(channel);
        
        Channel target  = channelDao.getEntity(targetId);
        List<Channel> children = channelDao.getChildrenById(channelId);
        for ( Channel temp : children ) {
            temp.setSite(target.getSite());
            temp.setDisabled(target.getDisabled());
 
            channelDao.update(temp);
        }
    }
 
    public void disable(Long siteOrChannelId) {
        if ( !checkDownPermission(siteOrChannelId, CMSConstants.OPERATION_STOP_START) ) {
            throw new BusinessException("您对当前栏目没有停用权限！");
        }
 
        List<Channel> list = channelDao.getChildrenById(siteOrChannelId);
        for ( Channel child : list ) {
            child.setDisabled(CMSConstants.STATUS_STOP);
        }
        channelDao.flush();
    }

    public void startSiteAll(Long siteId) {
        // 启用站点
        Channel channel = getSiteById(siteId);
        channel.setDisabled(CMSConstants.STATUS_START);
        
        // 启用站点下栏目
        if ( channel.isSite() ) {
            List<?> list = channelDao.getChannelsBySiteIdNoPermission(siteId);
            for ( Object entity : list ) {
                Channel temp = (Channel) entity;
                temp.setDisabled(CMSConstants.STATUS_START);
            }
        } 
        channelDao.flush();
    }
 
    public void startChannel(Long channelId) {
        // 启用所有父亲节点
        List<Channel> parents = channelDao.getParentsById(channelId, CMSConstants.OPERATION_STOP_START);
        for ( Channel parent : parents ) {
            parent.setDisabled(CMSConstants.STATUS_START);
        }
        channelDao.flush();
    }
 
    public Object[] selectCanAddArticleParentChannels() {
        return selectParentChannelsByOperationId(CMSConstants.OPERATION_ADD_ARTICLE); // 新建文章
    }
    
    public Object[] selectCanAddChannelParentChannels() { 
        return selectParentChannelsByOperationId(CMSConstants.OPERATION_ADD_CHANNEL); // 新建栏目
    }

    private Object[] selectParentChannelsByOperationId(String operationId) {
        List<Long> canAddChannelIds = channelDao.getSiteChannelIDsByOperationId(operationId); 
        channelDao.insertIds2TempTable( canAddChannelIds );
        List<?> channels = channelDao.getParentChannel4CanAdd();
        return new Object[]{channels, EasyUtils.list2Str(canAddChannelIds)};
    }
    
    public List<Channel> getChannelTreeDown(Long channelId) {
        return channelDao.getChildrenById(channelId, CMSConstants.OPERATION_VIEW);
    }

 
    
    /**************************************************** 以下为栏目（站点）文章发布 *************************************************/

	public void publishArticle(List<Article> articleList) {
		for ( Article article : articleList) {
            // 更新发布日期
			Date issueDate = article.getIssueDate();
            if (issueDate == null || !issueDate.after(new Date())) {
				article.setIssueDate(new Date());
			}
			
			// 取文章附件列表
			Map<String, Attachment> attachments = articleDao.getArticleAttachments(article.getId());
			article.getAttachments().putAll(attachments);
			
			// 发布文章，根据文章 创建日期 来设置xml文件的存放路径
			Channel site = article.getChannel().getSite();
            String publishPath = site.getPath()+ "/" + ArticleHelper.getArticlePublishPath(article);
			String pubUrl = PublishManger.publishOneArticle(article, publishPath);
			
			// 在文章对象里记录发布路径
			article.setPubUrl(pubUrl);
			articleDao.saveArticle(article);
		}
	}

    public int getTotalRows4Publish(Long channelId, String category ) {
        return channelDao.getTotalRows4Publish(channelId, category);
    }
    
    public List<Article> getPageArticleList(Long channelId, int pageNum, int pageSize, String category) {
        return channelDao.getPageArticleList4Publish(channelId, category, pageNum, pageSize);
    }
    
    public Integer getPublishableArticleCount(Long channelId) {
        return channelDao.getPublishableArticleCount(channelId);
    }

    public List<Article> getPagePublishableArticleList(Long channelId, int pageNum, Integer pageSize) {
        return channelDao.getPagePublishableArticleList(channelId, pageNum, pageSize);
    }
    
    public Long getSiteIdByChannelId(Long channelId) {
        Channel channel = getChannelById(channelId);
        return channel.getSite().getId();
    }
}