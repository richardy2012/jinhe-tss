package com.jinhe.tss.cms.service;

import java.util.List;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.entity.Article;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.framework.component.log.Logable;
import com.jinhe.tss.um.permission.filter.PermissionFilter4Create;
import com.jinhe.tss.um.permission.filter.PermissionFilter4Move;
import com.jinhe.tss.um.permission.filter.PermissionFilter4Sort;
import com.jinhe.tss.um.permission.filter.PermissionFilter4Update;
import com.jinhe.tss.um.permission.filter.PermissionTag;

/**
 * Channel的Service层接口，定义Channel相关的所有业务处理接口
 */
public interface IChannelService {
    
    /**
     * <p>
     * 得到所有站点栏目列表
     * </p>
     * @return
     */
    List<?> getAllSiteChannelList();

	/**
	 * <p>
	 * 获取所有未删除状态的Channel资源
	 * </p>
	 * @param condition
	 * @return List
	 */
    @PermissionTag(
            operation = CMSConstants.OPERATION_VIEW, 
            resourceType = CMSConstants.RESOURCE_TYPE_CHANNEL
    )
	List<?> getAllChannels();
	
	/**
	 * 新增或更新普通栏目
	 * @param channel
	 * @return
	 */
    @Logable(operateTable="栏目", operateType="新增", operateInfo="新增了 ${args[0]} 节点")
    @PermissionTag(
            operation = CMSConstants.OPERATION_ADD_CHANNEL, 
            resourceType = CMSConstants.RESOURCE_TYPE_CHANNEL,
            filter = PermissionFilter4Create.class
    )
	Channel createChannel(Channel channel);
    
    @Logable(operateTable="栏目", operateType="修改", operateInfo="修改了 ${args[0]} 节点")
    @PermissionTag(
            operation = CMSConstants.OPERATION_EDIT, 
            resourceType = CMSConstants.RESOURCE_TYPE_CHANNEL,
            filter = PermissionFilter4Update.class
    )
	Channel updateChannel(Channel channel);
    
    /**
     * 新增站点
     * @param channel
     * @return
     */
    @Logable(operateTable="站点", operateType="新增", operateInfo="新增了 ${args[0]} 节点")
    @PermissionTag(
            operation = CMSConstants.OPERATION_ADD_CHANNEL, 
            resourceType = CMSConstants.RESOURCE_TYPE_CHANNEL,
            filter = PermissionFilter4Create.class
    )
    Channel createSite(Channel site);
    
    /**
     * 更新站点
     * @param channel
     * @return
     */
    @Logable(operateTable="站点", operateType="修改", operateInfo="修改了 ${args[0]} 节点")
    @PermissionTag(
            operation = CMSConstants.OPERATION_EDIT, 
            resourceType = CMSConstants.RESOURCE_TYPE_CHANNEL,
            filter = PermissionFilter4Update.class
    )
    Channel updateSite(Channel site);

	/**
	 * <p>
	 * 逻辑删除某栏目下的所有未删除Channel
	 * </p>
	 * @param channelId
	 */
    @Logable(operateTable="站点栏目", operateType="删除", operateInfo="删除了 ID为 ${args[0]} 的节点。")
	void deleteChannel(Long channelId);

	/**
	 * <p>
	 * 移动Channel到站点或栏目下
	 * </p>
	 */
    @Logable(operateTable="站点栏目", operateType="移动", operateInfo="移动(ID: ${args[0]})节点到(ID: ${args[1]})节点下")
    @PermissionTag(
            operation = CMSConstants.OPERATION_ADD_CHANNEL + "," + CMSConstants.OPERATION_DELETE, 
            resourceType = CMSConstants.RESOURCE_TYPE_CHANNEL,
            filter = PermissionFilter4Move.class
    )
	void moveChannel(Long channelId, Long targetId);

	/**
	 * 排序同一级的栏目
	 * @param channelId
	 * @param toChannelId
	 * @param direction
	 */
    @Logable(operateTable="站点栏目", operateType="排序", operateInfo="(ID: ${args[0]})节点移动到了(ID: ${args[1]})节点<#if args[2]=1>的下方<#else>的上方</#if>")
    @PermissionTag(
            operation = CMSConstants.OPERATION_ORDER, 
            resourceType = CMSConstants.RESOURCE_TYPE_CHANNEL,
            filter = PermissionFilter4Sort.class
    )
	void sortChannel(Long channelId, Long toChannelId, Integer direction);
 
	/**
	 * <p>
	 * 通过ID得到单个栏目信息
	 * </p>
	 * @param id
	 * @return
	 */
	Channel getChannelById(Long id);
	
	/**
     * <p>
     * 获取指定站点信息
     * </p>
     * @param condition
     * @return Site
     */
    Channel getSiteById(Long siteId);

    /**
     * <p>
     * 停用站点
     * </p>
     * @param condition
     * @return boolean
     */
    @Logable(operateTable="站点栏目", operateType="停用", operateInfo="停用了 （ID ：${args[0]}） 站点")
    void stopSite(Long siteId);
    
    /**
     * <p>
     * 启用站点及所有子栏目
     * <p>
     * @param condition
     * @return
     */
    @Logable(operateTable="站点栏目", operateType="启用", operateInfo="启用了 （ID ：${args[0]}） 站点及其所有子栏目")
    void startSiteAll(Long siteId);
    
    /**
     * <p>
     * 启用栏目
     * </p>
     * @param condition
     * @return boolean
     */
    @Logable(operateTable="站点栏目", operateType="启用", operateInfo="启用了 （ID ：${args[0]}） 栏目")
    void startChannel(Long siteId);

    /**
     * 获取可以新建文章的栏目列表
     * @return
     *         [channels, canAddIds]
     */
    Object[] selectCanAddArticleParentChannels();

    /**
     * 获取可以新建子栏目的栏目列表
     * @return
     *         [channels, canAddIds]
     */
    Object[] selectCanAddChannelParentChannels();
    
    /**
     * <p>
     * 得到栏目下所有栏目
     * </p>
     * @param condition
     * @return
     */
    List<Channel> getChannelTreeDown(Long channelId);
    
	/**
	 * <p>
	 * 通过栏目id取栏目下所有可发布的文章总数
	 * </p>
	 * @param channelId
	 * @return
	 */
	Integer getPublishableArticleCount(Long channelId);

	/**
	 * <p>
	 * 通过栏目id取栏目下所有可发布文章id
	 * </p>
	 * @param channelId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<Article> getPagePublishableArticleList(Long channelId, int pageNum, Integer pageSize);
	
	/**
	 * <p>
	 * 通过栏目Id得到站点Id
	 * </p>
	 * @param channelId
	 * @return
	 */
	Long getSiteIdByChannelId(Long channelId);
	
	/**
	 * <p>
	 * 发布文章
	 * </p>
	 * @param articleIdList
	 * @param siteId 
	 */
	@Logable(operateTable="站点栏目", operateType="发布", operateInfo="发布文章")
	void publishArticle(List<Article> articleList);

    /**
     * 获取需要发布的总文章数
     * @param channelId
     * @param category   1:增量发布 2: 完全发布
     * @return
     */
    int getTotalRows4Publish(Long channelId, String category);

    /**
     * 根据页码获取当前页需要发布的文章列表
     * @param channelId
     * @param pageNum
     * @param page_site
     * @param category
     * @return
     */
    List<Article> getPageArticleList(Long channelId, int pageNum, int pageSize, String category);
}
