package com.jinhe.tss.cms.module;

import java.util.List;

import com.jinhe.tss.cms.AbstractTestSupport;
import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.action.SiteAction;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.framework.test.TestUtil;

/**
 * 文章站点栏目相关模块的单元测试。
 */
public class ChannelModuleTest extends AbstractTestSupport {
    
	SiteAction siteAction;
	
    public void setUp() throws Exception {
        super.setUp();
        siteAction = new SiteAction();
        siteAction.setChannelService(channelService);
    }
 
    public void testChannelModule() {
        // 新建站点
        Channel site = siteAction.getChannel();
        site.setName("交行门户");
        site.setPath("d:/Temp/cms");
        site.setDocPath("d:/Temp/cms/doc");
        site.setImagePath("d:/Temp/cms/img");
        siteAction.saveSite();
        Long siteId = site.getId();
        assertNotNull(siteId);
        
        siteAction.updateSite();
        
        siteAction.setIsNew(CMSConstants.TRUE);
        siteAction.getSiteDetail();
        
        siteAction.setIsNew(CMSConstants.FALSE);
        siteAction.setSiteId(siteId);
        siteAction.getSiteDetail();
        
        // 新建栏目
        channelAction.setIsNew(CMSConstants.TRUE);
        channelAction.setParentId(siteId);
        channelAction.getChannelDetail();
        Channel channel1 = channelAction.getChannel();
        channel1.setName("时事评论");
        channelAction.saveChannel();
        Long channelId = channel1.getId();
        assertNotNull(channelId);
        
        channelAction.setIsNew(CMSConstants.FALSE);
        channelAction.setChannelId(channelId);
        channelAction.getChannelDetail();
        
        channelAction.updateChannel();
        
        Channel channel2 = super.createChannel("体育新闻", channel1, siteId);
        Channel channel3 = super.createChannel("NBA战况", channel2, channel2.getId());
        
        List<?> list = channelService.getAllChannels();
        assertTrue(list.size() >= 3);
        for(Object temp : list) {
            log.debug(temp);
        }
        
        // 栏目排序
        channelAction.setChannelId(channelId);
        channelAction.setToChannelId(channel2.getId());
        channelAction.setDirection(1);
        channelAction.sortChannel();
        list = channelService.getAllChannels();
        for(Object temp : list) {
            log.debug(temp);
        }
        
        // 栏目移动
        channelAction.setChannelId(channel3.getId());
        channelAction.setToChannelId(channelId);
        channelAction.moveChannel();
        for(Object temp : list) {
            log.debug(temp);
        }
      
        
        // 停用启用
        siteAction.setSiteId(siteId);
        siteAction.stopSite();
        siteAction.startAll();
        
        siteAction.setSiteId(channelId);
        siteAction.stopSite();
        
        siteAction.setSiteId(channel3.getId());
        siteAction.startSite();
        
        siteAction.setResourceId(channelId.toString());
        siteAction.getOperatorByResourceId();
        
        siteAction.setAction("moveChannel");
        siteAction.setChannelId(channelId);
        siteAction.getSiteAll();
        
        siteAction.setAction("moveArticle");
        siteAction.getSiteAll();
        
        // 栏目站点删除
        channelAction.setChannelId(channel2.getId());
        channelAction.deleteChannel();
        
        siteAction.setSiteId(siteId);
        siteAction.deleteSite();
        
        list = super.channelDao.getEntities(" from Channel ");
        assertTrue(list.size() == 0);
        
        assertTrue(TestUtil.printLogs(logService) > 0);
    }
    
}
