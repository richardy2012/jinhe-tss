/* ==================================================================   
 * Created [2009-4-27 下午11:32:55] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2009-2012 
 * ================================================================== 
 */

package com.jinhe.tss.cms.publish;

import java.util.Map;

import org.dom4j.Element;

/**
 * <p>
 * IPublishArticle.java
 * </p>
 * 文章发布接口。 如果需要将文章发布成特定格式的XML，可以通过扩展该接口，然后在创建文章类型的时候指定其为文章发布类。
 * 
 */
public interface IPublishArticle {

    /**
     * 发布文章
     * @param articleElement
     * @param articleAttributes
     */
    public void publishArticle(Element articleElement, Map<String, Object> articleAttributes);
}
