package com.jinhe.tss.cms.action;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.entity.permission.ChannelPermissionsFull;
import com.jinhe.tss.cms.entity.permission.ChannelResourceView;
import com.jinhe.tss.cms.publish.PublishManger;
import com.jinhe.tss.cms.service.IChannelService;
import com.jinhe.tss.cms.timer.SchedulerBean;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.ProgressActionSupport;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.util.EasyUtils;
 
@Controller
@RequestMapping("/auth/channel")
public class ChannelAction extends ProgressActionSupport {

	@Autowired private IChannelService  channelService;
	@Autowired private PublishManger    publishManger;
	@Autowired private SchedulerBean    schedulerBean;

	/**
	 * 获取所有的栏目树结构
	 */
	@RequestMapping("/list")
	public void getChannelAll(HttpServletResponse response) {
		List<?> list = channelService.getAllSiteChannelList();
		if(list.size() > 0) {
			schedulerBean.init(); // TODO SchedulerBean在此初始化，此方法不调用岂不是不初始化了？
		}
		
		TreeEncoder treeEncoder = new TreeEncoder(list, new LevelTreeParser());
		treeEncoder.setNeedRootNode(false);
		print("ChannelTree", treeEncoder);
	}

	/**
	 * 获取栏目详细信息
	 */
	@RequestMapping("/detail/{id}/{parentId}")
	public void getChannelDetail(HttpServletResponse response, 
			@PathVariable("id") Long id, 
			@PathVariable("parentId") Long parentId) {
		
		Channel channel;
		if ( CMSConstants.DEFAULT_NEW_ID.equals(id) ) {
            channel = new Channel();
            
            Channel parent = (Channel) channelService.getChannelById(parentId);
            channel.setOverdueDate(parent.getOverdueDate());
            channel.setPublishArticleClassName(parent.getPublishArticleClassName());
            channel.setSite(parent.isSite() ? parent : parent.getSite());
            channel.setParentId(parentId);
		} 
		else {
			channel = channelService.getChannelById(id);
		}
 
		XFormEncoder xEncoder = new XFormEncoder(CMSConstants.XFORM_CHANNEL, channel);
		print("ChannelInfo", xEncoder);
	}
	
    /**
     * 获得站点的详细信息
     */
	@RequestMapping("/detail/site/{siteId}")
    public void getSiteDetail(HttpServletResponse response, @PathVariable("id") Long siteId) {
    	Channel channel;
        if ( CMSConstants.DEFAULT_NEW_ID.equals(siteId)) {
            channel = new Channel();
            channel.setDocPath("doc");
            channel.setImagePath("img");
        } 
        else {
            channel = channelService.getChannelById(siteId);
        }
 
        XFormEncoder xEncoder = new XFormEncoder(CMSConstants.XFORM_SITE, channel);
        print("SiteInfo", xEncoder);
    }

	/**
	 * 新增/更新栏目
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void saveChannel(HttpServletResponse response, Channel channel) {
		if( channel.getId() == null ) {
			channelService.createChannel(channel);
			doAfterSave(true, channel, "ChannelTree");
		}
		else {
			channelService.updateChannel(channel);
			printSuccessMessage("修改成功！");
		}
	}
	
    /**
     * 新建或更新站点
     */
	@RequestMapping(value = "/site", method = RequestMethod.POST)
    public void saveSite(HttpServletResponse response, Channel channel) {
    	if( channel.getId() == null ) {
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
	 * 逻辑删除栏目
	 */
	@RequestMapping(value = "/{id}/", method = RequestMethod.DELETE)
	public void delete(HttpServletResponse response, @PathVariable("id") Long id) {
		channelService.deleteChannel(id);
        printSuccessMessage("删除成功！");
	}

	/**
	 * 栏目排序
	 */
	@RequestMapping(value = "/sort/{id}/{targetId}/{direction}", method = RequestMethod.POST)
	public void sortChannel(HttpServletResponse response, 
			@PathVariable("id") Long id, 
			@PathVariable("targetId") Long targetId, 
			@PathVariable("direction") int direction) {
		
		channelService.sortChannel(id, targetId, direction);
        printSuccessMessage("排序成功！");
	}

	/**
	 * 栏目移动
	 */
	@RequestMapping(value = "/move/{id}/{toParentId}", method = RequestMethod.POST)
	public void moveChannel(HttpServletResponse response, 
			@PathVariable("id") Long id, 
			@PathVariable("toParentId") Long toParentId) {
		
		channelService.moveChannel(id, toParentId);
        printSuccessMessage("移动成功！");
	}
 
	/**
	 * 带有进度条的栏目(站点)发布
	 * @param category 1:增量发布  2:完全发布
	 */
	@RequestMapping(value = "/publish/{id}/{category}", method = RequestMethod.POST)
	public void publish(HttpServletResponse response, 
			@PathVariable("id") Long id, 
			@PathVariable("category") String category) {
		
        String code = publishManger.publishArticle(id, category);
        printScheduleMessage(code);  
	}
 
    /**
     * 启用栏目
     */
	@RequestMapping(value = "/enable/{id}", method = RequestMethod.POST)
    public void enable(HttpServletResponse response, @PathVariable("id") Long id) {
        // 如果启用的是站点，则启用全部子节点
        Channel channel = channelService.getChannelById(id);
		if( channel.isSite() ) {
        	channelService.enableSite(id);
        }
        else {
        	 channelService.enableChannel(id);
        }
        printSuccessMessage("启用成功！");
    }
 
    /**
     * 停用站点
     */
	@RequestMapping(value = "/disable/{id}", method = RequestMethod.POST)
    public void disable(HttpServletResponse response, @PathVariable("id") Long id) {
        channelService.disable(id);
        printSuccessMessage("停用成功！");
    }

    /**
     * 根据栏目资源id来获取对栏目的操作权限
     */
	@RequestMapping("/operations/{resourceId}")
    public void getOperations(HttpServletResponse response, @PathVariable("resourceId") Long resourceId) {
        List<String> list = PermissionHelper.getInstance().getOperationsByResource(resourceId,
                        ChannelPermissionsFull.class.getName(), ChannelResourceView.class);

        print("Operation", EasyUtils.list2Str(list));
    }
}
