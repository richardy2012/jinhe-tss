package com.jinhe.tss.portal.service.impl;

import java.util.List;

import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinhe.tss.cache.Cacheable;
import com.jinhe.tss.cache.Pool;
import com.jinhe.tss.framework.component.cache.CacheHelper;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.dao.INavigatorDao;
import com.jinhe.tss.portal.entity.Navigator;
import com.jinhe.tss.portal.service.INavigatorService;

@Service("NavigatorService")
public class NavigatorService implements INavigatorService {

	@Autowired private INavigatorDao dao;

    public List<?> getAllNavigator(){
        return dao.getEntities("from Navigator o order by o.decode");
	}
	
	public Navigator saveNavigator(Navigator entity){
		if( entity.getId() == null ) {
		    Long parentId = entity.getParentId();
            Integer nextSeqNo = dao.getNextSeqNo(parentId);
            entity.setSeqNo(nextSeqNo);
		}
		
		return dao.save(entity);
	}
	
	public void deleteNavigator(Long id){
        List<Navigator> children = dao.getChildrenById(id);
        for( Navigator child : children ){
            dao.deleteNavigator(child);
        }
	}
	
	public Navigator getNavigator(Long id){
        return dao.getEntity(id);
	}
	
	public void disable(Long id, Integer disabled) {
        List<Navigator> list;
        if(PortalConstants.TRUE.equals(disabled)) {
            list = dao.getChildrenById(id);            
        } 
        else {
            list = dao.getParentsById(id);    
        }
        
        for(  Navigator temp : list ){
            temp.setDisabled(disabled);
            dao.updateWithoutFlush(temp);
        }
	}

    public void sort(Long id, Long targetId, int direction) {
        dao.sort(id, targetId, direction);
    }

    public List<?> getNavigatorsByPortal(Long portalId) {
        return dao.getMenusByPortal(portalId);
    }

    public void moveNavigator(Long id, Long targetId) {
        Navigator navigator = dao.getEntity(id);
        
        navigator.setPortalId(dao.getEntity(targetId).getPortalId());
        navigator.setParentId(targetId);
        navigator.setSeqNo(dao.getNextSeqNo(targetId));
                   
        dao.save(navigator);
    }
    
    public String getNavigatorXML(Long id) {
    	if( !Context.getIdentityCard().isAnonymous() ) {
    		return createNavigatorXML(id);
    	}
    	
    	// 缓存只针对匿名用户访问进行缓存
        Pool navigatorPool = CacheHelper.getNoDeadCache();
        String key = PortalConstants.NAVIGATOR_CACHE + id;
        Cacheable cachedMenu = navigatorPool.getObject(key);
        if( cachedMenu == null ){
        	cachedMenu = navigatorPool.putObject(key, createNavigatorXML(id));
        }
        return (String) cachedMenu.getValue();
    }

	private String createNavigatorXML(Long id) {
		Navigator navigator = getNavigator(id);   
		List<Navigator> menuItems = dao.getMenuItemListByMenu(id);
		
		Element node = navigator.compose2Tree(menuItems);
		String menuStr = node == null ? "<MainMenu/>" : node.asXML();
		return menuStr;
	}
}

	