package com.jinhe.tss.portal.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jinhe.tss.framework.persistence.TreeSupportDao;
import com.jinhe.tss.portal.dao.INavigatorDao;
import com.jinhe.tss.portal.entity.Navigator;
 
@Repository("NavigatorDao")
public class NavigatorDao extends TreeSupportDao<Navigator> implements INavigatorDao{

	public NavigatorDao() {
		super(Navigator.class);
	}
 
    public Navigator save(Navigator navigator) {
    	if(navigator.getId() == null) {    
            return create(navigator);
        }
        
//        update(navigator);
        return navigator;
    }
    
    public void deleteNavigator(Navigator navigator){
        delete(em.merge(navigator));
    }
    
    public List<Navigator> getMenuItems(Long id, Long userId) {
        return getChildrenById(id);
    }
    
    @SuppressWarnings("unchecked")
    public List<Navigator> getMenusByPortal(Long portalId) {
        String hql = "from Navigator o where o.disabled <> 1 and o.portalId = ? order by o.decode ";
        return (List<Navigator>) getEntities(hql, portalId);
    }

	public List<Navigator> getChildrenById(Long id, String operationId) {
		return getChildrenById(id);
	}
}

	