package com.jinhe.tss.cms.action;

import java.util.List;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.publish.PublishManger;
import com.jinhe.tss.cms.service.IChannelService;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.ProgressActionSupport;
 
public class ChannelAction extends ProgressActionSupport {

	private IChannelService  channelService;
	private PublishManger    publishManger;
    
    private Channel channel = new Channel(); // 栏目信息
    
    private Long    parentId;
	private Long    channelId;    // 栏目编号
	private Long    toChannelId; // 排序到的栏目
	private Integer direction;  // 排序方向  -1目标上方,1目标下方
	private String  category;  // 1:增量发布  2:完全发布

	/**
	 * 获取所有的栏目树结构
	 */
	public String getChannelAll() {
		List<?> list = channelService.getAllChannels();
		TreeEncoder channelTreeEncoder = new TreeEncoder(list, new LevelTreeParser());
		channelTreeEncoder.setNeedRootNode(false);
		return print("ChannelTree", channelTreeEncoder);
	}

	/**
	 * 获取栏目xform信息
	 */
	public String getChannelDetail() {
		if ( isCreateNew() ) {
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
		return print("ChannelInfo", xEncoder);
	}

	/**
	 * 新增栏目
	 */
	public String saveChannel() {
	    channelService.createChannel(channel);
		return doAfterSave(true, channel, "ChannelTree");
	}

	/**
	 * 更新栏目
	 */
	public String updateChannel() {
		channelService.updateChannel(channel);
		return printSuccessMessage("修改成功！");
	}

	/**
	 * 逻辑删除栏目
	 */
	public String deleteChannel() {
		channelService.deleteChannel(channelId);
        return printSuccessMessage("删除成功！");
	}

	/**
	 * 栏目排序
	 */
	public String sortChannel() {
		channelService.sortChannel(channelId, toChannelId, direction);
        return printSuccessMessage("排序成功！");
	}

	/**
	 * 栏目移动
	 */
	public String moveChannel() {
		channelService.moveChannel(channelId, toChannelId);
        return printSuccessMessage("移动成功！");
	}
 
	/**
	 * 带有进度条的栏目发布
	 */
	public String publishChannel() {
        String code = publishManger.publishArticle(channelId, category);
        return printScheduleMessage(code);  
	}
    /**
     *  带有进度条的站点发布
     */
    public String publishSite(){
        String code = publishManger.publishArticle(channelId, category);
        return printScheduleMessage(code);  
    }
    
    public Channel getChannel() {  return channel; }
 
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setDirection(Integer direction) {
        this.direction = direction;
    }
    public void setToChannelId(Long toChannelId) {
        this.toChannelId = toChannelId;
    }
    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
    
    public void setChannelService(IChannelService channelService) {
        this.channelService = channelService;
    }
 
	public void setPublishManger(PublishManger publishManger) {
		this.publishManger = publishManger;
	}
}
