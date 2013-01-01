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
import java.util.Map.Entry;

import org.dom4j.Element;

/** 
 * <p> BasePublishArticle.java </p> 
 * 默认文章发布类。
 * 将文章的所有属性都做为节点加到发布xml文件中。
 */
public class BasePublishArticle implements IPublishArticle {
 
    public void publishArticle(Element articleElement, Map<String, Object> articleAttributes) {
        Element eleKey = null;
        for(  Entry<String, Object> entry : articleAttributes.entrySet() ) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value == null) continue;
                       
            if(value instanceof Object[]){ 
                Object[] objs = (Object[]) value;
                for ( Object temp : objs ) {
                    eleKey = articleElement.addElement(key);
                    eleKey.addCDATA(temp.toString());
                }
            } 
            else {
                eleKey = articleElement.addElement(key);
                eleKey.addCDATA(value.toString()); 
            }     
        }
    }
}

