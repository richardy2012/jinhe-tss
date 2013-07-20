package com.jinhe.tss.cms.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.entity.Article;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.persistence.TreeSupportDao;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.util.EasyUtils;

/**
 * Channel的Dao层，负责处理Channel相关的数据库操作
 */
@Repository("ChannelDao")
public class ChannelDao extends TreeSupportDao<Channel> implements IChannelDao {
    
	public ChannelDao() {
        super(Channel.class);
    }
	
	public void moveChannel(Channel channel) {
	    update(channel);
	}
	
    public Channel getSiteByChannel(Long channelId) {
        Channel channel = (Channel) getEntity(Channel.class, channelId);
        if(channel.isSite()) {
            return channel;
        }
        return (Channel) getEntity(Channel.class, channel.getSite().getId());
    }
	
	@SuppressWarnings("unchecked")
    public List<Long> getSiteChannelIDsByOperationId(String operationId) {
		String hql = "select distinct t.id from Channel t, RoleUserMapping r, ChannelPermissionsFull v"
    			+ " where v.roleId = r.id.roleId and r.id.userId = ?"
    			+ " and v.operationId = ? and v.resourceId = t.id order by t.id";
		return (List<Long>) getEntities(hql, Environment.getOperatorId(), operationId);
	}
 
    public List<?> getParentChannel4CanAdd() {
        String hql = "select distinct o from Channel o, Channel t, Temp temp "
            + " where t.id = temp.id and t.decode like o.decode ||'%' order by o.decode";
        return getEntities(hql);
    }
	
    public List<?> getAllSiteChannelList() {
		return getEntities("from Channel c order by c.decode");
	}

    public List<?> getAllStartedSiteChannelList() {
		return getEntities("from Channel c where c.disabled = 0 order by c.decode");
	}

	public List<?> getChannelsBySiteIdNoPermission(Long siteId) {
        Channel site = getEntity(siteId);
        if(site == null){
            throw new BusinessException("ID为：" + siteId + " 的站点不存在或者已经被删除");
        }
		return getEntities("from Channel c where c.site.id = c.id and c.decode like ?", site.getDecode() + "%");
	}
 
	public List<?> getChildChannels(Long parentId) {
		String hql = " from Channel t where t.parentId = ? and t.site.id <> t.id";
		return getEntities(hql, parentId);
	}
    
    private Channel getChannelById(Long channelId){
        Channel channel = getEntity(channelId);
        if(channel == null){
            throw new BusinessException("ID为：" + channelId + " 的栏目不存在或者已经被删除");
        }
        return channel;
    }
 
	public List<Channel> getChildrenById(Long channelId, String operationId) {
        return getChildrenById(channelId);
	}
	
	public List<Channel> getParentsById(Long channelId, String operationId) {
	    return getParentsById(channelId);
	}
 
	public List<?> getChannelTreeUpNoPermission(Long channelId) {
		String hql = "from Channel t where t.site.id <> t.id and ? like t.decode || '%'";
        return getEntities(hql, getChannelById(channelId).getDecode());
	}
	
	public boolean checkBrowsePermission(Long channelId) {
        String hql = "select distinct v from RoleUserMapping r, ChannelPermissionsFull v " +
                " where v.id.resourceId= ? and v.id.roleId = r.id.roleId and r.id.userId = ? and v.id.operationId = ?";
        List<?> list = getEntities(hql, channelId, Environment.getOperatorId(), CMSConstants.OPERATION_VIEW);
        return list.size() > 0 ;
    }
 
    //-------------------------------------------------- 文章发布相关 －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
    
    public int getTotalRows4Publish(Long channelId, String category ) {
        String decode = getChannelById(channelId).getDecode();
        
        String hql = "select count(*) from Article a where a.channel.decode like ?  and ";

        // 取可发布的文章总数
        List<?> list;
        if (CMSConstants.PUBLISH_ALL.equals(category)) {  //完全发布的话已经发布的也重新发布
            hql += " ( a.status = ? or a.status = ? ) ";
            list = getEntities(hql, decode + "%", CMSConstants.TOPUBLISH_STATUS, CMSConstants.XML_STATUS);
        } 
        else {
            hql += " ( a.status = ? ) ";
            list = getEntities(hql, decode + "%", CMSConstants.TOPUBLISH_STATUS);
        }
 
        return EasyUtils.convertObject2Integer(list.get(0));
    }

    @SuppressWarnings("unchecked")
    public List<Article> getPageArticleList4Publish(Long channelId, String category, int pageNum, int pageSize) {
        String decode = getChannelById(channelId).getDecode();
        
        String hql = "from Article a where a.channel.decode like ? and ";
        Query query;
        if (CMSConstants.PUBLISH_ALL.equals(category)) {
            hql += " ( a.status = ? or a.status = ? ) ";
            query = em.createQuery(hql);
            query.setParameter(3, CMSConstants.XML_STATUS);
        } 
        else {
            hql += " ( a.status = ? ) ";
            query = em.createQuery(hql);
        }
        
        query.setParameter(1, decode + "%");
        query.setParameter(2, CMSConstants.TOPUBLISH_STATUS);
        query.setFirstResult(pageSize * (pageNum - 1));
        query.setMaxResults(pageSize);
        
        return query.getResultList();
    }
    
    public Integer getPublishableArticleCount(Long channelId) {
        String hql = "select count(*) from Article a where a.channel.id = ? and a.status = ?";
        List<?> list = getEntities(hql, channelId, CMSConstants.TOPUBLISH_STATUS);
        return EasyUtils.convertObject2Integer(list.get(0));
    }

    @SuppressWarnings("unchecked")
    public List<Article> getPagePublishableArticleList(Long channelId, int pageNum, int pageSize) {
        String hql = "from Article a where a.channel.id = ? and a.status = ? ";
        
        Query query = em.createQuery(hql);
        query.setParameter(1, channelId);
        query.setParameter(2, CMSConstants.TOPUBLISH_STATUS);
        query.setFirstResult( pageSize * (pageNum - 1) );
        query.setMaxResults( pageSize );
        
        return query.getResultList();
    }
}
