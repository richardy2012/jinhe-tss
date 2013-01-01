package com.jinhe.tss.portal.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.dao.IElementDao;
import com.jinhe.tss.portal.entity.Decorator;
import com.jinhe.tss.portal.entity.ElementGroup;
import com.jinhe.tss.portal.entity.Layout;
import com.jinhe.tss.portal.helper.IElement;
import com.jinhe.tss.portal.service.IElementService;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.URLUtil;

public class ElementService implements IElementService {

    @Autowired private IElementDao dao;

    public List<?> getAllElementsAndGroups(int type) {
        List<?> groups = getGroupsByType(type);
        
        // 根据元素类型名称获取所有的元素（修饰器/布局器/Portlet）
        String elementName = ElementGroup.getClassNameByType(type);
        List<?> elements = dao.getEntities("from " + elementName + " o order by o.decode ");
        
        List<Object> returnList = new ArrayList<Object>();
        returnList.addAll(elements);
        returnList.addAll(groups);
        return returnList;
    }

    public List<?> getAllStartElementsAndGroups(int type) {
        List<?> groups = getGroupsByType(type);
        
        // 根据元素类型名称获取所有启用的元素（修饰器/布局器/Portlet）
        String elementName = ElementGroup.getClassNameByType(type);
        List<?> elements = dao.getEntities("from " + elementName + " o where (o.disabled is null or o.disabled <> 1) order by o.decode ");
        
        List<Object> returnList = new ArrayList<Object>();
        returnList.addAll(elements);
        returnList.addAll(groups);
        return returnList;
    }
 
    public IElement saveElement(IElement obj) {
        if (obj.getId() == null) {
            obj.setCode(obj.getElementName() + System.currentTimeMillis());
            
            String entityName = obj.getClass().getName();
            Integer seqNo = dao.getNextSeqNo(entityName, obj.getGroupId(), "groupId");
            obj.setSeqNo(seqNo);
        }
        return dao.saveElement(obj);
    }
    
    public IElement deleteElement(Class<?> clazz, Long id){
        IElement element = (IElement) dao.getEntity(clazz, id);
        checkElementInUse(element);
        return dao.deleteElement(element);
    }

    private void checkElementInUse(IElement element){
        if(PortalConstants.TRUE.equals(element.getIsDefault())) {
            throw new BusinessException("删除组件为默认的修饰器或布局器，删除失败！");
        }
        
        ElementGroup group = (ElementGroup) dao.getEntity(ElementGroup.class, element.getGroupId());
        String hql = null;
        switch (group.getType()) {
        case ElementGroup.PORTLET_TYPE:  // Portlet组
            hql = "from PortalStructure t where t.definerId = ? and t.type = 3 ";
            break;
        case ElementGroup.LAYOUT_TYPE:   // 布局器组
            hql = "select t from PortalStructure t, ThemeInfo ti where t.id = ti.id.portalStructureId and ti.layoutId = ? ";
            break;
        case ElementGroup.DECORATOR_TYPE: // 修饰器组
            hql = "select t from PortalStructure t, ThemeInfo ti where t.id = ti.id.portalStructureId and ti.decoratorId = ? ";
            break;
        }     
 
        Long elementId = element.getId();
        if( dao.getEntities(hql, elementId).size() > 0) {
            throw new BusinessException("结点: 【ID为：" + elementId + " 名称为： " + element.getName() + "】 在使用中，删除失败！");
        }
    }
 
    public void disableElement(Class<?> elementClass, Long id, Integer disabled) {
        IElement element = getElementInfo(elementClass, id);
        if(PortalConstants.TRUE.equals(element.getIsDefault())) {
            throw new BusinessException("停用组件为默认的修饰器或布局器，停用失败！");
        }
        
        if (!element.getDisabled().equals(disabled)) {
            element.setDisabled(disabled);
            dao.saveElement(element);
        }
    }
 
    public IElement getElementInfo(Class<?> elementClass, Long id) {
        if(!elementClass.equals(ElementGroup.LAYOUT_CLASS)
                && !elementClass.equals(ElementGroup.DECORATOR_CLASS)
                && !elementClass.equals(ElementGroup.PORTLET_CLASS) ) {
            throw new BusinessException("getElemet(Long id, Class elementClass)传入的elementClass : " + elementClass +" 非法！");
        }
        return (IElement) dao.getEntity(elementClass, id);
    }
 
    public void sortElement(Long id, Long targetId, int direction, Class<?> elementClass) {
        IElement sourceItem = getElementInfo(elementClass, id);
        IElement targetItem = getElementInfo(elementClass, targetId);

        Integer seqNo1 = sourceItem.getSeqNo();
        Integer seqNo2 = targetItem.getSeqNo();
        int tag = seqNo1 < seqNo2 ? 1 : -1;
        
        // 移动时候获取受影响的元素（修饰器/布局器/Portlet）
        String hql = "from " + elementClass.getName() + " o where o.groupId = ? and o.seqNo > ? and o.seqNo < ?";
        Object groupId = sourceItem.getGroupId();
        List<?> list = dao.getEntities(hql, seqNo1 > seqNo2 ? new Object[]{groupId , seqNo2, seqNo1} : new Object[]{groupId, seqNo1, seqNo2});
        for ( Object temp : list ) {
            IElement item = (IElement) temp;
            item.setSeqNo(item.getSeqNo() - tag);
            dao.update(item);
        }
        sourceItem.setSeqNo(new Integer(seqNo2.intValue() + (direction - tag) / 2));
        targetItem.setSeqNo(new Integer(seqNo2.intValue() - (direction + tag) / 2));
        dao.update(sourceItem);
        dao.update(targetItem);
    }

    public IElement copyElement(Long id, File path, Class<?> elementClass) {
        IElement element = getElementInfo(elementClass, id);
        return copyElement(element, path, PortalConstants.COPY_PREFIX);
    }
    
    private IElement copyElement(IElement element, File path, String prefix) {
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
    
    public void setDecorator4Default(Long decoratorId) {
        List<?> list = dao.getEntities("from Decorator o where o.isDefault = 1");
        for ( Object temp : list ) {
            Decorator decorator = (Decorator) temp;
            decorator.setIsDefault(PortalConstants.FALSE);
            dao.update(decorator);     
        }     
        
        Decorator decorator = (Decorator) dao.getEntity(Decorator.class, decoratorId);
        decorator.setIsDefault(PortalConstants.TRUE);
        decorator.setDisabled(PortalConstants.FALSE); //设置为默认布局器则启用该修饰器（不管有没有停用掉）
        dao.update(decorator);       
        
        Decorator.setDefaultDecorator(decorator);  //更新的缓存的默认修饰器
    }
    
    public void setLayout4Default(Long layoutId) {
        List<?> list = dao.getEntities("from Layout o where o.isDefault = 1");
        for ( Object temp : list ) {
            Layout layout = (Layout) temp;
            layout.setIsDefault(PortalConstants.FALSE);
            dao.update(layout);     
        }        
        
        Layout layout = (Layout) dao.getEntity(Layout.class, layoutId);
        layout.setIsDefault(PortalConstants.TRUE);
        layout.setDisabled(PortalConstants.FALSE); //设置为默认布局器则启用该布局器（不管有没有停用掉）
        dao.update(layout);       
        
        Layout.setDefaultLayout(layout);  // 更新的缓存的默认布局器
    }
    
    /*****************************************************************************************************************
     ************************************ 以下是对元素（修饰器/布局器/Portlet）组的操作 ************************************* 
     *****************************************************************************************************************/   
    public void moveTo(Long elementId, Long groupId) {
        ElementGroup group = dao.getEntity(groupId);
        IElement element = (IElement) dao.getEntity(group.getClassByType(), elementId);
        element.setSeqNo(dao.getNextSeqNo(group.getClassNameByType(), groupId, "groupId"));
        element.setGroupId(groupId);
        
        dao.moveTo(element);  
    }
    
    public void deleteGroupById(Long groupId) {
        //检查组下是否有元素尚在使用中
        List<?> elements = dao.getAllElementsByGroup(groupId);
        for ( Object element : elements ) {
            checkElementInUse((IElement)element);
        }
            
        ElementGroup entity = dao.getEntity( groupId );
        dao.deleteGroup(entity);
    }

    public void sortByType(Long id, Long targetId, int direction) {
        List<ElementGroup> sortedList = dao.sort(id, targetId, direction);
        
        // 修复组下叶子节点（即门户元素）的decode值
        for(ElementGroup group : sortedList) {
        	List<?> elements = dao.getElementsByGroup(group.getId());
        	for(Object temp : elements) {
        		dao.update(temp);  // 保存一边以重新生成decode值
        	}
        }
    }
    
    public ElementGroup saveGroup(ElementGroup group) {     
        if(group.getId() == null) {    
            group.setSeqNo(dao.getNextSeqNo(group.getParentId()));
        }
        return dao.saveGroup(group);
    }
    
    public ElementGroup getGroupInfo(Long groupId){
        return dao.getEntity( groupId );
    }
    
    public List<Object> copyGroup(Long groupId, int type, String path) {
    	List<Object> returnList = new ArrayList<Object>();
    	
        List<ElementGroup> groups = dao.getChildrenById(groupId); 
        Map<Long, Long> idMapping = new HashMap<Long, Long>();
        for ( ElementGroup group : groups ) {
            Long oldGroupId = group.getId();
            dao.evict(group);
            group.setId(null);          
            if(oldGroupId.equals(groupId)) {
                group.setName(PortalConstants.COPY_PREFIX + group.getName());
            } 
            else {          
                group.setParentId( idMapping.get(group.getParentId()));
            }
            
            group = saveGroup(group);
            returnList.add(group);
            idMapping.put(oldGroupId, group.getId());
        }      
        
        List<?> elements = dao.getAllElementsByGroup(groupId);
        for( Object temp :  elements){
            IElement element = (IElement) temp;
            element.setGroupId(idMapping.get(element.getGroupId()));
            
            IElement newElement = copyElement(element, new File(path), "");
            returnList.add(newElement);
        }         
        return returnList;
    }

    public List<?> getGroupsByType(Integer type){
        return dao.getEntities("from ElementGroup t where t.type = ? order by t.decode", type);
    }

    public Object[] copyTo(Long elementId, Long groupId) {
        ElementGroup group = dao.getEntity( groupId );
        IElement element = (IElement) dao.getEntity(group.getClassByType(), elementId);
        
        //如果目标节点和原父节点是同一个，则当"复制"操作处理，name前面加前缀
        String prefix = "";
        if(element.getGroupId().equals(groupId)) {
            prefix = PortalConstants.COPY_PREFIX;
        }
        
        element.setGroupId(groupId);
        
        File path = new File(URLUtil.getWebFileUrl(group.getBasePathByType()).getPath());
        copyElement(element, path, prefix);
        
        return new Object[]{element, group.getClassNameByType()};
    }

    public List<?> getDecorators() {
        return dao.getEntities("from Decorator o where (o.disabled is null or o.disabled <> 1) order by o.id");
    }

    public List<?> getLayouts() {
        return dao.getEntities("from Layout o where (o.disabled is null or o.disabled <> 1) order by o.id");
    }
}
