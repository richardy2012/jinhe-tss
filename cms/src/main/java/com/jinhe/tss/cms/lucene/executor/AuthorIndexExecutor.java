package com.jinhe.tss.cms.lucene.executor;

/** 
 * 按文章作者创建索引。
 */
public class AuthorIndexExecutor extends FiledIndexExecutor {
    
    protected String getFiledName() {
        return "author";
    }
}

