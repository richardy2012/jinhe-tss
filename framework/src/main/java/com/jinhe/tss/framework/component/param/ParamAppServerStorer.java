package com.jinhe.tss.framework.component.param;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.appserver.AppServer;
import com.jinhe.tss.framework.sso.appserver.IAppServerStorer;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.XMLDocUtil;

/** 
 * UMS的appServer管理启用参数管理(ParamAppServerStorer)，以免增加应用就要修改appServer.xml文件。
 * 
 * 但是PMS/CMS等应用要用到 <bean id="RemoteOnlineUserManager" class="***.HttpInvokerProxyFactory">
 * 这样就要使用到HttpInvokerProxyFactory的配置, 而加载applicationContext.xml时会需要调用ParamAppServerStorer以获取AppServer，
 * 而ParamAppServerStorer又需要Global获取ParamService，而applicationContext.xml加载完之前Global不可用。
 * 
 * TODO 解决办法，直接通过jdbc读取？
 * 
 */
public class ParamAppServerStorer implements IAppServerStorer {
    
    private Map<String, AppServer> cache = new HashMap<String, AppServer>();

    public AppServer getAppServer(String appCode) {
        AppServer appServer = (AppServer) cache.get(appCode);
        
        // ParamManager.valueMap里没有该appCode的值，则有可能是第一次取或这参数更改后缓存刷新了，需要重新生成。
        if(appServer == null || ParamManager.valueMap.get(appCode) == null){
            cache.put(appCode, appServer = createAppServer(appCode));
        } 
        return appServer;
    }
    
    private AppServer createAppServer(String appCode){
        String appServerXML = null;
        try{
            appServerXML = ParamManager.getValueNoSpring(appCode);
        }catch(Exception e) { }
        
        if(appServerXML == null) {
            throw new BusinessException("参数管理模块中尚没有应用Code为：" + appCode + " 的应用服务配置信息"); 
        }
        
        try{
            Element appServerNode = XMLDocUtil.dataXml2Doc(appServerXML).getRootElement();
            Map<String, String> attrsMap = XMLDocUtil.dataNode2Map(appServerNode);

            AppServer bean = new AppServer();
            BeanUtil.setDataToBean(bean, attrsMap);
            return bean;
        }catch(Exception e){
            throw new BusinessException("参数管理模块中应用Code为：" + appCode + " 的应用服务配置信息有误，请检查！");
        }
    }
    
    public static void main(String[] args){
        System.out.println(new ParamAppServerStorer().getAppServer("UMS"));
    }
}

