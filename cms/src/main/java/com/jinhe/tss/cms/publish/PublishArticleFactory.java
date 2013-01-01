package com.jinhe.tss.cms.publish;

import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.EasyUtils;
 
public class PublishArticleFactory {
    
    private static IPublishArticle publishArticle = null;
    
    private PublishArticleFactory(){
    };
	
	public static IPublishArticle getInstance(String className){
	    if(publishArticle == null){	        
            if( EasyUtils.isNullOrEmpty( className )){
                return new BasePublishArticle();
            }
            
            publishArticle = (IPublishArticle) BeanUtil.newInstanceByName(className);
	    }
	    return publishArticle;
	}

}

