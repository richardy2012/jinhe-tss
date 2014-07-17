package com.jinhe.tss.cms.lucene.executor;

import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.EasyUtils;
 
public class IndexExecutorFactory {

    private IndexExecutorFactory() {
    }

    public static IndexExecutor create(String className) {
        if ( EasyUtils.isNullOrEmpty( className )) {
            return new DefaultIndexExecutor();
        }
        
        return (IndexExecutor) BeanUtil.newInstanceByName(className);
    }
}

