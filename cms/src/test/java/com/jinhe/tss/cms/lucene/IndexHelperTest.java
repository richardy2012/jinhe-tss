package com.jinhe.tss.cms.lucene;

import java.util.LinkedHashSet;
import java.util.Set;

import junit.framework.TestCase;

import com.jinhe.tss.cms.entity.TimerStrategy;
import com.jinhe.tss.framework.component.progress.Progress;

public class IndexHelperTest extends TestCase {
	
	public void testCreateIndex() {
        TimerStrategy tacticIndex = new TimerStrategy();
        tacticIndex.setId(new Long(23));
        tacticIndex.setIndexPath("d:/temp/cms/index");
        
        Set<ArticleContent> articleContentSet = new LinkedHashSet<ArticleContent>();
        for(int i = 0; i < 1000; i++ ){
            ArticleContent bean = new ArticleContent("D:/temp/cms/zjcz/2008/03/29/1207065898437.xml", "国家统计局长：中国未来五年物价上涨压力大");
            articleContentSet.add(bean);
        }
        
        IndexHelper.createIndex(tacticIndex, articleContentSet, new Progress(1000));
	}

}
