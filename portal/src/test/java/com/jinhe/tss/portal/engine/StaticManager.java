package com.jinhe.tss.portal.engine;

import java.io.File;
import java.io.StringReader;
import java.util.List;

import org.springframework.mock.web.MockHttpServletRequest;
import org.xml.sax.InputSource;

import com.jinhe.tss.cms.service.IRemoteArticleService;
import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.sso.IdentityCard;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.portal.service.INavigatorService;
import com.jinhe.tss.um.helper.dto.OperatorDTO;
import com.jinhe.tss.util.FileHelper;

public class StaticManager {
	
	public static InputSource translateValue(String value){
        return new InputSource(new StringReader(value));
    }
    
    private static void initWebContent(){   	
    	MockHttpServletRequest request = new MockHttpServletRequest();
        Context.initRequestContext(request);
        IdentityCard card = new IdentityCard("token", OperatorDTO.ADMIN);
        Context.initIdentityInfo(card);
    }
    
    public static INavigatorService getNavigatorService(){
        initWebContent();
        return (INavigatorService) Global.getBean("NavigatorService");
    }
    
    public static IRemoteArticleService getArticleService(){
        initWebContent();
        return (IRemoteArticleService) Global.getBean("RemoteArticleService");
    }

    public static List<String> listFiles(String dirName){
        return FileHelper.listFiles(new File(dirName));
    }
    
}
