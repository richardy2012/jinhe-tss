package com.jinhe.tss.cms.lucene.executor;

/** 
 * 按文章标题创建索引。
 */
public class TitleIndexExecutor extends FiledIndexExecutor {
    
    protected String getFiledName() {
        return "title";
    }
}

