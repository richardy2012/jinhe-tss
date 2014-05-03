package com.jinhe.tss.cms.lucene;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.lucene.executor.DefaultIndexExecutor;
import com.jinhe.tss.cms.lucene.executor.KeywordIndexExecutor;
import com.jinhe.tss.cms.lucene.executor.TitleIndexExecutor;
import com.jinhe.tss.cms.timer.TimerStrategy;
import com.jinhe.tss.cms.timer.TimerStrategyHolder;
import com.jinhe.tss.framework.component.progress.Progress;
import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.URLUtil;

public class IndexHelperTest {
	
	@Test
	public void testCreateIndex() {
		Channel site = new Channel();
		site.setPath(URLUtil.getClassesPath().getPath() + "/temp");
		
        TimerStrategy tacticIndex = TimerStrategyHolder.getIndexStrategy();
        tacticIndex.setSite(site);
        
        String tmpDir = TestUtil.getTempDir() ;
        String path = tmpDir + "/123.xml";
        FileHelper.writeFile(new File(path), "<root/>");
        
        Set<ArticleContent> articleContentSet = new LinkedHashSet<ArticleContent>();
        for(int i = 0; i < 1000; i++ ){
			ArticleContent bean = new ArticleContent(path, "国家统计局长：中国未来五年物价上涨压力大");
            articleContentSet.add(bean);
        }
        
        IndexHelper.createIndex(tacticIndex, articleContentSet, new Progress(1000));
        
        tacticIndex.setExecutorClass(DefaultIndexExecutor.class.getName());
        IndexHelper.createIndex(tacticIndex, articleContentSet, new Progress(1000));
        
        tacticIndex.setExecutorClass(TitleIndexExecutor.class.getName());
        IndexHelper.createIndex(tacticIndex, articleContentSet, new Progress(1000));
        
        tacticIndex.setExecutorClass(KeywordIndexExecutor.class.getName());
        IndexHelper.createIndex(tacticIndex, articleContentSet, new Progress(1000));
	}

}
