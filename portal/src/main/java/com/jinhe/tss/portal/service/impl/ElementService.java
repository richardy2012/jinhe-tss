package com.jinhe.tss.portal.service.impl;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.dao.IElementDao;
import com.jinhe.tss.portal.entity.Component;
import com.jinhe.tss.portal.service.IElementService;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.URLUtil;

@Service("ElementService")
public class ElementService implements IElementService {

    @Autowired private IElementDao dao;

    public List<?> getAllElementsAndGroups() {
        return dao.getEntities("from Element o order by o.decode ");
    }

    public List<?> getAllStartElementsAndGroups(int type) {
        List<?> groups = getGroupsByType(type);
        
        // 根据元素类型名称获取所有启用的元素（修饰器/布局器/Portlet）
        List<?> elements = dao.getEntities("from Element o where o.disabled <> 1  and o.type =  ? order by o.decode", type);
        
        List<Object> returnList = new ArrayList<Object>();
        returnList.addAll(elements);
        returnList.addAll(groups);
        return returnList;
    }
 
    public Component saveElement(Component obj) {
        if (obj.getId() == null) {
            obj.setCode(obj.getComponentType() + System.currentTimeMillis());
            
            Integer seqNo = dao.getNextSeqNo(obj.getParentId());
            obj.setSeqNo(seqNo);
        }
        return dao.saveElement(obj);
    }
    
    public Component deleteElement(Long id) {
        Component element = dao.getEntity(id);
        checkElementInUse(element);
        return dao.deleteElement(element);
    }

    private void checkElementInUse(Component element) {
        if(PortalConstants.TRUE.equals(element.getIsDefault())) {
            throw new BusinessException("删除组件为默认的修饰器或布局器，删除失败！");
        }
        
        Component group = dao.getEntity(element.getParentId());
        String hql = null;
        switch (group.getType()) {
        case Component.PORTLET_TYPE:  // Portlet组
            hql = "from PortalStructure t where t.definerId = ? and t.type = 3 ";
            break;
        case Component.LAYOUT_TYPE:   // 布局器组
            hql = "select t from PortalStructure t, ThemeInfo ti where t.id = ti.id.portalStructureId and ti.layoutId = ? ";
            break;
        case Component.DECORATOR_TYPE: // 修饰器组
            hql = "select t from PortalStructure t, ThemeInfo ti where t.id = ti.id.portalStructureId and ti.decoratorId = ? ";
            break;
        }     
 
        Long elementId = element.getId();
        if( dao.getEntities(hql, elementId).size() > 0) {
            throw new BusinessException("结点: 【ID为：" + elementId + " 名称为： " + element.getName() + "】 在使用中，删除失败！");
        }
    }
 
    public void disableElement(Long id, Integer disabled) {
        Component element = getElementInfo(id);
        if(PortalConstants.TRUE.equals(element.getIsDefault())) {
            throw new BusinessException("停用组件为默认的修饰器或布局器，停用失败！");
        }
        
        if (!element.getDisabled().equals(disabled)) {
            element.setDisabled(disabled);
            dao.saveElement(element);
        }
    }
 
    public Component getElementInfo(Long id) {
        return dao.getEntity(id);
    }
 
    public void sortElement(Long id, Long targetId, int direction) {
        dao.sort(id, targetId, direction);
    }

    public Component copyElement(Long id, File path) {
        Component element = getElementInfo(id);
        return copyElement(element, path, PortalConstants.COPY_PREFIX);
    }
    
    private Component copyElement(Component element, File path, String prefix) {
        //先取到拷贝原本element的资源文件目录
        File fileDir = FileHelper.findPathByName(path, element.getCode() + element.getId());
        
        dao.evict(element);
        element.setId(null);
        element.setName(prefix + element.getName());
        element.setIsDefault(PortalConstants.FALSE);
        element = saveElement(element);
        
        if (fileDir != null) {
            FileHelper.copyFolder(fileDir.toString(), path + "/" + element.getCode() + element.getId());
        }
        return element;
    }
    
    public void setDecoratorAsDefault(Long decoratorId) {
        List<?> list = dao.getEntities("from Element o where o.type = ? and o.isDefault = 1", Component.DECORATOR_TYPE);
        for ( Object temp : list ) {
            Component decorator = (Component) temp;
            decorator.setIsDefault(PortalConstants.FALSE);
            dao.update(decorator);     
        }     
        
        Component decorator = dao.getEntity(decoratorId);
        decorator.setIsDefault(PortalConstants.TRUE);
        decorator.setDisabled(PortalConstants.FALSE); //设置为默认布局器则启用该修饰器（不管有没有停用掉）
        dao.update(decorator);       
    }
    
    public void setLayoutAsDefault(Long layoutId) {
        List<?> list = dao.getEntities("from Element o where o.type = ? and o.isDefault = 1", Component.LAYOUT_TYPE);
        for ( Object temp : list ) {
            Component layout = (Component) temp;
            layout.setIsDefault(PortalConstants.FALSE);
            dao.update(layout);     
        }        
        
        Component layout = dao.getEntity(layoutId);
        layout.setIsDefault(PortalConstants.TRUE);
        layout.setDisabled(PortalConstants.FALSE); //设置为默认布局器则启用该布局器（不管有没有停用掉）
        dao.update(layout);       
    }
    
    /*****************************************************************************************************************
     ************************************ 以下是对元素（修饰器/布局器/Portlet）组的操作 ************************************* 
     *****************************************************************************************************************/   
    public void moveTo(Long elementId, Long groupId) {
        Component element = dao.getEntity(elementId);
        element.setSeqNo(dao.getNextSeqNo(groupId));
        element.setParentId(groupId);
        
        dao.moveTo(element);  
    }
    
    public void deleteGroupById(Long groupId) {
        // 检查组下是否有元素尚在使用中
        List<?> elements = dao.getAllElementsByGroup(groupId);
        for ( Object element : elements ) {
            checkElementInUse( (Component) element );
        }
            
        Component entity = dao.getEntity( groupId );
        dao.deleteGroup(entity);
    }
 
    public List<?> getGroupsByType(Integer type){
        return dao.getEntities("from Element t where t.type = ? and isGroup = ? order by t.decode", type, true);
    }

    public Component copyTo(Long elementId, Long groupId) {
        Component group = dao.getEntity( groupId );
        Component element = dao.getEntity( elementId );
        
        // 如果目标节点和原父节点是同一个，则当"复制"操作处理，name前面加前缀
        String prefix = "";
        if(element.getParentId().equals(groupId)) {
            prefix = PortalConstants.COPY_PREFIX;
        }
        
        element.setParentId(groupId);
        
        URL resourceUri = URLUtil.getWebFileUrl(group.getResourceBaseDir());
        File path = new File(resourceUri.getPath());
        copyElement(element, path, prefix);
        
        return element;
    }

    public List<?> getDecorators() {
        return dao.getEntities("from Element o where o.disabled <> 1 and o.type = ? order by o.decode", Component.DECORATOR_TYPE);
    }

    public List<?> getLayouts() {
        return dao.getEntities("from Element o where o.disabled <> 1 and o.type = ? order by o.decode", Component.LAYOUT_TYPE);
    }
}
