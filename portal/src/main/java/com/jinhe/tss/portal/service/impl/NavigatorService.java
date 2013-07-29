package com.jinhe.tss.portal.service.impl;

import java.util.List;

import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinhe.tss.cache.Cacheable;
import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.Pool;
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
	
	public Navigator saveMenu(Navigator entity){
		if( entity.getId() == null ) {
		    Long parentId = entity.getParentId();
            Integer nextSeqNo = dao.getNextSeqNo(parentId);
            entity.setSeqNo(nextSeqNo);
		}
		
		if(entity.getContentId() != null) {
		    
		}
		if(entity.getTargetId() != null) {
		    // 把门户ID设置到菜单信息里
//		    entity.setPortalId(portalId);
		}
		
		return dao.saveMenu(entity);
	}
	
	public void deleteMenu(Long id){
        List<Navigator> children = dao.getChildrenById(id);
        for( Navigator child : children ){
            dao.deleteMenu(child);
        }
	}
	
	public Navigator getNavigatorInfo(Long id){
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

    public List<?> getMenusByPortal(Long portalId) {
        return dao.getMenusByPortal(portalId);
    }

    public void moveMenu(Long id, Long targetId) {
        Navigator menu = dao.getEntity(id);
        
        menu.setPortalId(dao.getEntity(targetId).getPortalId());
        menu.setParentId(targetId);
        menu.setSeqNo(dao.getNextSeqNo(targetId));
                   
        dao.saveMenu(menu);
    }
    
    public String getMenuXML(Long id) {
    	if( !Context.getIdentityCard().isAnonymous() ) {
    		return createMenuXML(id);
    	}
    	
    	// 缓存只针对匿名用户访问进行缓存
        Pool menuPool = JCache.getInstance().getCachePool(PortalConstants.MENU_CACHE);
        Cacheable cachedMenu = menuPool.getObject(id);
        if( cachedMenu == null ){
        	cachedMenu = menuPool.putObject(id, createMenuXML(id));
        }
        return (String) cachedMenu.getValue();
    }

	private String createMenuXML(Long id) {
		Navigator menu = getNavigatorInfo(id);   
		List<Navigator> menuItems = dao.getMenuItemListByMenu(id);
		
		Element node = menu.composeMenuNode(menuItems);
		String menuStr = node == null ? "<MainMenu/>" : node.asXML();
		return menuStr;
	}
}

	