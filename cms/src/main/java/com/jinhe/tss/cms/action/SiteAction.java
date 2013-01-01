package com.jinhe.tss.cms.action;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.entity.permission.ChannelPermissionsFull;
import com.jinhe.tss.cms.entity.permission.ChannelResourceView;
import com.jinhe.tss.cms.helper.translator.ChannelCanSelectTranslator;
import com.jinhe.tss.cms.service.IChannelService;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeTranslator;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.ProgressActionSupport;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.util.EasyUtils;

public class SiteAction extends ProgressActionSupport {

	private IChannelService channelService;

	private Long siteId;   // 站点ID
	private String action; // 功能名
	private Long channelId;  // 栏目ID
	private Long resourceId; // 资源id

	private Channel channel = new Channel();

	/**
	 * 获得站点和栏目树(包括所有的树节点过滤情况)
	 */
	public String getSiteAll() {
		TreeEncoder treeEncoder = null;
		if ("moveArticle".equals(action) || "copyArticle".equals(action)) {

			Object[] object = channelService
					.selectCanAddArticleParentChannels();
			treeEncoder = new TreeEncoder(object[0], new LevelTreeParser());
			treeEncoder.setRootCanSelect(false);
			treeEncoder.setTranslator(new ChannelCanSelectTranslator(
					(String) object[1], channelId));

		} else if ("moveChannel".equals(action)
				|| ("copyChannel").equals(action)) {
			Object[] object = channelService
					.selectCanAddChannelParentChannels();
			treeEncoder = new TreeEncoder(object[0], new LevelTreeParser());
			treeEncoder.setRootCanSelect(false);

			final List<Channel> selectedList = channelService
					.getChannelTreeDown(channelId);

			final List<String> canAddChannelIds = Arrays
					.asList(((String) object[1]).split(","));

			// 树栏目是否可选 转换器 (专门为移动节点时的目标树结构用，移动节点的子节点都不可选)
			treeEncoder.setTranslator(new ITreeTranslator() {
				public Map<String, Object> translate(
						Map<String, Object> attributes) {
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
			treeEncoder = new TreeEncoder(channelService
					.getAllSiteChannelList(), new LevelTreeParser());
		}
		return print("SiteTree", treeEncoder);
	}

	/**
	 * 新建站点
	 */
	public String saveSite() {
		channel.setParentId(CMSConstants.HEAD_NODE_ID);
		channelService.createSite(channel);

		return doAfterSave(true, channel, "SiteTree");
	}

	/**
	 * 更新站点信息
	 */
	public String updateSite() {
		channelService.updateSite(channel);
		return printSuccessMessage("修改成功！");
	}

	/**
	 * 获得站点的详细信息
	 */
	public String getSiteDetail() {
		if (isCreateNew()) {
			channel = new Channel();
			channel.setDocPath("doc");
			channel.setImagePath("img");
		} else {
			channel = channelService.getSiteById(siteId);
		}
 
		XFormEncoder xEncoder = new XFormEncoder(CMSConstants.XFORM_SITE, channel);
		return print("SiteInfo", xEncoder);
	}

	/**
	 * 逻辑删除站点成功
	 */
	public String deleteSite() {
		channelService.deleteChannel(siteId);
		return printSuccessMessage("删除成功！");
	}

	/**
	 * 启用栏目
	 */
	public String startSite() {
		channelService.startChannel(siteId);
		return printSuccessMessage("启用成功！");
	}

	/**
	 * 启用站点下所有栏目
	 */
	public String startAll() {
		channelService.startSiteAll(siteId);
		return printSuccessMessage("启用成功！");
	}

	/**
	 * 停用站点
	 */
	public String stopSite() {
		channelService.stopSite(siteId);
		return printSuccessMessage("停用成功！");
	}

	/**
	 * 根据资源id来获取操作权限
	 */
	public String getOperatorByResourceId() {
		List<String> list = PermissionHelper.getInstance()
				.getOperationsByResource(resourceId,
						ChannelPermissionsFull.class.getName(),
						ChannelResourceView.class);

		String permissionAll = "p1,p2," + EasyUtils.list2Str(list)
				+ "cd1,cd2,cd3,cd4,cd5,ca1,ca2,ca3,ca4,ca5";
		return print("Operation", permissionAll);
	}

	public Channel getChannel() {
		return channel;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public void setResourceId(String resourceId) {
		if ("_rootId".equals(resourceId)) {
			this.resourceId = CMSConstants.HEAD_NODE_ID;
		} else {
			this.resourceId = new Long(resourceId);
		}
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public void setChannelService(IChannelService channelService) {
		this.channelService = channelService;
	}
}