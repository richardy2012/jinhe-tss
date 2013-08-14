package com.jinhe.tss.cms.module;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.jinhe.tss.cms.AbstractTestSupport;
import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.framework.test.TestUtil;

/**
 * 文章站点栏目相关模块的单元测试。
 */
public class ChannelModuleTest extends AbstractTestSupport {
 
	@Test
    public void testChannelModule() {
        // 新建站点
        Channel site = new Channel();
        site.setName("交行门户");
        site.setPath("d:/Temp/cms");
        site.setDocPath("d:/Temp/cms/doc");
        site.setImagePath("d:/Temp/cms/img");
        channelAction.saveSite(site);
        Long siteId = site.getId();
        assertNotNull(siteId);
        
        channelAction.saveSite(site);
        
        channelAction.getSiteDetail(CMSConstants.DEFAULT_NEW_ID);
        
        channelAction.getSiteDetail(siteId);
        
        // 新建栏目
        channelAction.getChannelDetail(CMSConstants.DEFAULT_NEW_ID, siteId);
        Channel channel1 = new Channel();
        channel1.setName("时事评论");
        channel1.setParentId(siteId);
        channelAction.saveChannel(channel1);
        Long channelId = channel1.getId();
        assertNotNull(channelId);
        
        channelAction.getChannelDetail(channelId, channel1.getParentId());
        
        channelAction.saveChannel(channel1);
        
        Channel channel2 = super.createChannel("体育新闻", channel1, siteId);
        Channel channel3 = super.createChannel("NBA战况", channel2, channel2.getId());
        
        List<?> list = channelService.getAllChannels();
        assertTrue(list.size() >= 3);
        for(Object temp : list) {
            log.debug(temp);
        }
        
        // 栏目排序
        channelAction.sortChannel(channelId, channel2.getId(), 1);
        list = channelService.getAllChannels();
        for(Object temp : list) {
            log.debug(temp);
        }
        
        // 栏目移动
        channelAction.moveChannel(channel3.getId(), channelId);
        for(Object temp : list) {
            log.debug(temp);
        }
        
        // 停用启用
        channelAction.disable(siteId);
        channelAction.enable(siteId);
        
        channelAction.disable(channelId);
        
        channelAction.enable(channel3.getId());
        
        channelAction.getOperations(channelId);
        
        channelAction.getSiteChannelTree(channelId, "moveChannel");
        channelAction.getSiteChannelTree(channelId, "moveArticle");
        
        // 栏目站点删除
        channelAction.deleteSiteOrChannel(channel2.getId());
        channelAction.deleteSiteOrChannel(siteId);
        
        list = super.channelDao.getEntities(" from Channel ");
        assertTrue(list.size() == 0);
        
        assertTrue(TestUtil.printLogs(logService) > 0);
    }
    
}
