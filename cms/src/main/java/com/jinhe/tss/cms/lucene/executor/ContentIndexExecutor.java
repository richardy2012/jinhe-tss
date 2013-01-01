package com.jinhe.tss.cms.lucene.executor;

/** 
 * 按文章内容创建索引。
 * 
 */
public class ContentIndexExecutor extends FiledIndexExecutor {
    
    protected String getFiledName() {
        return "content";
    }
}

