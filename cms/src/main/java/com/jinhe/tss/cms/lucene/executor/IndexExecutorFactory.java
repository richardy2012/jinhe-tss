package com.jinhe.tss.cms.lucene.executor;

import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.EasyUtils;
 
public class IndexExecutorFactory {

    private IndexExecutorFactory() {
    }

    public static IIndexExecutor create(String className) {
        if ( EasyUtils.isNullOrEmpty( className )) {
            return new DefaultIndexExecutor();
        }
        
        return (IIndexExecutor) BeanUtil.newInstanceByName(className);
    }
}

