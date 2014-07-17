package com.jinhe.tss.cms.lucene;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.cms.job.JobStrategy;
import com.jinhe.tss.cms.lucene.executor.DefaultIndexExecutor;
import com.jinhe.tss.cms.lucene.executor.KeywordIndexExecutor;
import com.jinhe.tss.cms.lucene.executor.TitleIndexExecutor;
import com.jinhe.tss.framework.component.progress.Progress;
import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.URLUtil;

public class IndexHelperTest {
	
	@Test
	public void testCreateIndex() {
		Channel site = new Channel();
		site.setPath(URLUtil.getClassesPath().getPath() + "/temp");
		
        JobStrategy tacticIndex = JobStrategy.getIndexStrategy();
        tacticIndex.site = site;

        IndexHelper.createIndex(tacticIndex, prepareData(1000), new Progress(1000));
        
        tacticIndex.executorClass = DefaultIndexExecutor.class.getName();
        IndexHelper.createIndex(tacticIndex, prepareData(100), new Progress(1000));
        
        tacticIndex.executorClass = TitleIndexExecutor.class.getName();
        IndexHelper.createIndex(tacticIndex, prepareData(100), new Progress(1000));
        
        tacticIndex.executorClass = KeywordIndexExecutor.class.getName();
        IndexHelper.createIndex(tacticIndex, prepareData(100), new Progress(1000));
	}
	
	private Set<ArticleContent> prepareData(int size) {
        String tmpDir = TestUtil.getTempDir() ;
        String path = tmpDir + "/123.xml";
        FileHelper.writeFile(new File(path), "<root/>");
        
		Set<ArticleContent> articleContentSet = new LinkedHashSet<ArticleContent>();
        for(int i = 0; i < size; i++ ){
			ArticleContent bean = new ArticleContent(path, "国家统计局长：中国未来五年物价上涨压力大");
			
			bean.getArticleAttributes().put("issueDate", "2012-12-12");
			bean.getArticleAttributes().put("createTime", "2012-12-12");
			bean.getArticleAttributes().put("content", "国家统计局长：中国未来五年物价上涨压力大");
			bean.getArticleAttributes().put("keyword", "物价 上涨");
			bean.getArticleAttributes().put("title", "物价上涨");
			bean.getArticleAttributes().put("other", "XXXXXX");
            
			articleContentSet.add(bean);
        }
        
        return articleContentSet;
	}
	

}
