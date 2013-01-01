package com.jinhe.tss.cms.lucene.executor;

/** 
 * 按文章关键字创建索引。
 */
public class KeywordIndexExecutor extends FiledIndexExecutor {
    
    protected String getFiledName() {
        return "keyword";
    }
}

