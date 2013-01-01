package com.jinhe.tss.cms.helper;

import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.cms.dao.IChannelDao;
import com.jinhe.tss.cms.entity.Channel;
 
/**
 * TODO　重新实现附件上传功能
 */
public class ArticleAttachPathCreator  {
	
	@Autowired IChannelDao channelDao;
	
	public String getPath(File file, Map<String, String> params) {
		Channel channel = channelDao.getEntity(Long.valueOf(params.get("channelId")));
		Channel  site   = channel.getSite();
        
        return ArticleHelper.getAttachmentPath(site, Integer.valueOf(params.get("type")));
	}

	public String getFileName(File file, Map<String, String> params) {
		String name = file.getName();
        params.put("name", name);
        
        int i = name.lastIndexOf(".");
        String fileExt = (i != -1) ? name.substring(i, name.length()) : "";
		return System.currentTimeMillis() + fileExt;
	}
}