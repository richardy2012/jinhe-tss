package com.jinhe.tss.cms.action;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.entity.permission.ChannelPermissionsFull;
import com.jinhe.tss.cms.entity.permission.ChannelResourceView;
import com.jinhe.tss.cms.helper.translator.ChannelCanSelectTranslator;
import com.jinhe.tss.cms.publish.PublishManger;
import com.jinhe.tss.cms.service.IChannelService;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeTranslator;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.ProgressActionSupport;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.util.EasyUtils;
 
@Controller
@RequestMapping("channel")
public class ChannelAction extends ProgressActionSupport {

	@Autowired private IChannelService  channelService;
	@Autowired private PublishManger    publishManger;

	/**
	 * 获取所有的栏目树结构
	 */
	public void getChannelAll() {
		List<?> list = channelService.getAllChannels();
		TreeEncoder channelTreeEncoder = new TreeEncoder(list, new LevelTreeParser());
		channelTreeEncoder.setNeedRootNode(false);
		print("ChannelTree", channelTreeEncoder);
	}

	/**
	 * 获取栏目xform信息
	 */
	public void getChannelDetail(Long channelId, Long parentId) {
		Channel channel;
		if ( CMSConstants.DEFAULT_NEW_ID.equals(channelId) ) {
            channel = new Channel();
            
            Channel parent = (Channel) channelService.getChannelById(parentId);
            channel.setOverdueDate(parent.getOverdueDate());
            channel.setPublishArticleClassName(parent.getPublishArticleClassName());
            channel.setSite(parent.isSite() ? parent : parent.getSite());
            channel.setParentId(parentId);
		} 
		else {
			channel = channelService.getChannelById(channelId);
		}
 
		XFormEncoder xEncoder = new XFormEncoder(CMSConstants.XFORM_CHANNEL, channel);
		print("ChannelInfo", xEncoder);
	}

	/**
	 * 新增/更新栏目
	 */
	public void saveChannel(Channel channel) {
		if( DEFAULT_NEW_ID.equals(channel.getId()) ) {
			channelService.createChannel(channel);
			doAfterSave(true, channel, "ChannelTree");
		}
		else {
			channelService.updateChannel(channel);
			printSuccessMessage("修改成功！");
		}
	}
 
	/**
	 * 逻辑删除栏目
	 */
	public void deleteSiteOrChannel(Long id) {
		channelService.deleteChannel(id);
        printSuccessMessage("删除成功！");
	}

	/**
	 * 栏目排序
	 */
	public void sortChannel(Long channelId, Long toChannelId, int direction) {
		channelService.sortChannel(channelId, toChannelId, direction);
        printSuccessMessage("排序成功！");
	}

	/**
	 * 栏目移动
	 */
	public void moveChannel(Long channelId, Long toChannelId) {
		channelService.moveChannel(channelId, toChannelId);
        printSuccessMessage("移动成功！");
	}
 
	/**
	 * 带有进度条的栏目发布
	 * @param category 1:增量发布  2:完全发布
	 */
	public void publishChannel(Long channelId, String  category) {
        String code = publishManger.publishArticle(channelId, category);
        printScheduleMessage(code);  
	}
	
    /**
     *  带有进度条的站点发布
     */
    public void publishSite(Long channelId, String  category) {
        String code = publishManger.publishArticle(channelId, category);
        printScheduleMessage(code);  
    }

    /**
     * 获得站点和栏目树(包括所有的树节点过滤情况)
     */
    public void getSiteChannelTree(Long channelId, String action) {
        TreeEncoder treeEncoder = null;
        if ("moveArticle".equals(action)) {
            Object[] object = channelService.selectCanAddArticleParentChannels();
            treeEncoder = new TreeEncoder(object[0], new LevelTreeParser());
            treeEncoder.setRootCanSelect(false);
            treeEncoder.setTranslator(new ChannelCanSelectTranslator((String) object[1], channelId));

        } else if ("moveChannel".equals(action)) {
            Object[] object = channelService.selectCanAddChannelParentChannels();
            treeEncoder = new TreeEncoder(object[0], new LevelTreeParser());
            treeEncoder.setRootCanSelect(false);

            final List<Channel> selectedList = channelService.getChannelTreeDown(channelId);

            final List<String> canAddChannelIds = Arrays.asList(((String) object[1]).split(","));

            // 树栏目是否可选 转换器 (专门为移动节点时的目标树结构用，移动节点的子节点都不可选)
            treeEncoder.setTranslator(new ITreeTranslator() {
                public Map<String, Object> translate(Map<String, Object> attributes) {
                    Object channelId = attributes.get("id");
                    for (Channel channel : selectedList) {
                        if (channelId.equals(channel.getId())) {
                            attributes.put("canselected", "0");
                        }
                    }

                    if (!canAddChannelIds.contains(String.valueOf(channelId))) {
                        attributes.put("canselected", "0");
                    }
                    return attributes;
                }
            });

        } else {
            treeEncoder = new TreeEncoder(channelService.getAllSiteChannelList(), new LevelTreeParser());
        }
        print("SiteTree", treeEncoder);
    }

    /**
     * 新建或更新站点
     */
    public void saveSite(Channel channel) {
    	if( DEFAULT_NEW_ID.equals(channel.getId()) ) {
    		channel.setParentId(CMSConstants.HEAD_NODE_ID);
            channelService.createSite(channel);

            doAfterSave(true, channel, "SiteTree");
    	}
    	else {
    		channelService.updateSite(channel);
            printSuccessMessage("修改成功！");
    	}
    }
 
    /**
     * 获得站点的详细信息
     */
    public void getSiteDetail(Long siteId) {
    	Channel channel;
        if ( CMSConstants.DEFAULT_NEW_ID.equals(siteId)) {
            channel = new Channel();
            channel.setDocPath("doc");
            channel.setImagePath("img");
        } 
        else {
            channel = channelService.getSiteById(siteId);
        }
 
        XFormEncoder xEncoder = new XFormEncoder(CMSConstants.XFORM_SITE, channel);
        print("SiteInfo", xEncoder);
    }

    /**
     * 启用栏目
     */
    public void enable(Long id) {
        // 如果启用的是站点，则启用全部子节点
        if( channelService.getChannelById(id).isSite() ) {
        	channelService.startSiteAll(id);
        }
        else {
        	 channelService.startChannel(id);
        }
        printSuccessMessage("启用成功！");
    }
 
    /**
     * 停用站点
     */
    public void disable(Long id) {
        channelService.disable(id);
        printSuccessMessage("停用成功！");
    }

    /**
     * 根据栏目资源id来获取对栏目的操作权限
     */
    public void getOperations(Long resourceId) {
        List<String> list = PermissionHelper.getInstance().getOperationsByResource(resourceId,
                        ChannelPermissionsFull.class.getName(), ChannelResourceView.class);

        String permissionAll = "p1,p2,cd1,cd2,cd3,cd4,cd5,ca1,ca2,ca3,ca4,ca5，" + EasyUtils.list2Str(list);
        print("Operation", permissionAll);
    }
}
