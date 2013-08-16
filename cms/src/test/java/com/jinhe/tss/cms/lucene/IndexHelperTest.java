package com.jinhe.tss.cms.lucene;

import java.util.LinkedHashSet;
import java.util.Set;

import junit.framework.TestCase;

import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.timer.TimerStrategy;
import com.jinhe.tss.cms.timer.TimerStrategyHolder;
import com.jinhe.tss.framework.component.progress.Progress;

public class IndexHelperTest extends TestCase {
	
	public void testCreateIndex() {
		Channel site = new Channel();
		site.setPath("D:/temp/cms");
		
        TimerStrategy tacticIndex = TimerStrategyHolder.getIndexStrategy();
        tacticIndex.setSite(site);
        
        Set<ArticleContent> articleContentSet = new LinkedHashSet<ArticleContent>();
        for(int i = 0; i < 1000; i++ ){
            ArticleContent bean = new ArticleContent("D:/temp/cms/zjcz/2008/03/29/1207065898437.xml", "国家统计局长：中国未来五年物价上涨压力大");
            articleContentSet.add(bean);
        }
        
        IndexHelper.createIndex(tacticIndex, articleContentSet, new Progress(1000));
	}

}
