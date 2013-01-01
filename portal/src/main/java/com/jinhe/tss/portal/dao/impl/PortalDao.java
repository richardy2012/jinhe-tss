package com.jinhe.tss.portal.dao.impl;

import java.util.List;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.persistence.TreeSupportDao;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.portal.dao.IPortalDao;
import com.jinhe.tss.portal.entity.Decorator;
import com.jinhe.tss.portal.entity.Layout;
import com.jinhe.tss.portal.entity.PersonalTheme;
import com.jinhe.tss.portal.entity.Portal;
import com.jinhe.tss.portal.entity.PortalStructure;

public class PortalDao extends TreeSupportDao<PortalStructure> implements IPortalDao {

    public PortalDao() {
		super(PortalStructure.class);
	}
 
    public Portal getPortalById(Long id){
        return (Portal) getEntity(Portal.class, id);
    }
    
    public PortalStructure savePortalStructure(PortalStructure ps) {
    	if(ps.getId() == null) {
            return create(ps);
        } 
    	else {
            update(ps);
            return ps; 
        }
    }
    
    public void deletePortalStructure(PortalStructure ps) {
        super.delete(em.merge(ps));
    }
    
    public PortalStructure movePortalStructure(PortalStructure ps){
        return create(ps);
    }
 
    public List<PortalStructure> getParentsById(Long id, String operationId) {
        return  super.getParentsById(id);
    }

    public List<PortalStructure> getChildrenById(Long id, String operationId) {
        return super.getChildrenById(id);
    }
 
    public PortalStructure getRootPortalStructure(Long portalId) {
        List<?> list = getEntities("from PortalStructure o where o.type = 0 and o.portalId = ? ", portalId);
        if(list.isEmpty()) {
        	throw new BusinessException("根据portalId获取门户根节点时出错！可能不存在！");
        }
        return (PortalStructure) list.get(0);
    }

    public Object[] getPortalElements(Long portalId, Long currentThemeId){
    	//此处用了in是因为Decorator，Layout，Portlet表中各有一个属性为Long的大字段，无法直接使用distinct
        String[] hqls = new String[3];        
        hqls[0] = "from Decorator o where o.id in (select distinct ti.decoratorId from PortalStructure p, ThemeInfo ti where p.portalId=? and p.id=ti.id.portalStructureId and ti.id.themeId = ? and p.type<>0 and p.disabled<>1) or o.isDefault = 1 ";
        hqls[1] = "from Layout o    where o.id in (select distinct ti.layoutId  from PortalStructure p, ThemeInfo ti where p.portalId=? and p.id=ti.id.portalStructureId and ti.id.themeId = ? and p.type<>3 and p.disabled<>1) or o.isDefault = 1 ";
        hqls[2] = "from Portlet o   where o.id in (select distinct t.id  from PortalStructure p, Portlet t where p.definerId=t.id and p.type=3 and p.portalId=? and p.disabled<>1)";
        
        Object[] objs = new Object[3];   
        objs[0] = getEntities(hqls[0], portalId, currentThemeId); 
        objs[1] = getEntities(hqls[1], portalId, currentThemeId); 
        objs[2] = getEntities(hqls[2], currentThemeId); 
        return objs;
    }
    
    public Layout getDefaultLayout(){
        if(Layout.defaultLayout == null){
            List<?> list = getEntities("from Layout o where o.isDefault = '1'");
            if(list.isEmpty()) {
            	throw new BusinessException("没有设定一个默认的布局器或者存在多个默认布局器！");
            }
            Layout.defaultLayout = (Layout) list.get(0);
        }
        return Layout.defaultLayout;
    }
    
    public Decorator getDefaultDecorator(){
        if(Decorator.defaultDecorator == null){
            List<?> list = getEntities("from Decorator o where o.isDefault = 1");
            if(list.isEmpty()) {
            	throw new BusinessException("没有设定一个默认的修饰器或者存在多个默认修饰器！");
            }
            Decorator.defaultDecorator = (Decorator) list.get(0);
        }
        return Decorator.defaultDecorator;
    }

    public List<?> getThemesByPortal(Long portalId) {
        return getEntities("from Theme o where o.portalId = ? and o.disabled <> 1 order by o.id", portalId);
    }
    
    public PersonalTheme getPersonalTheme(Long portalId) {
        String hql = "from PersonalTheme o where o.userId = ? and o.portalId = ?";
        List<?> list = getEntities(hql, Environment.getOperatorId(), portalId);
        if( !list.isEmpty() ) {
            return (PersonalTheme) list.get(0);
        }
        return null;
    }
}