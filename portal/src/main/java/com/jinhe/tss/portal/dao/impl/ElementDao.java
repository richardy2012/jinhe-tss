package com.jinhe.tss.portal.dao.impl;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jinhe.tss.framework.persistence.TreeSupportDao;
import com.jinhe.tss.portal.dao.IElementDao;
import com.jinhe.tss.portal.entity.ElementGroup;
import com.jinhe.tss.portal.helper.IElement;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.URLUtil;

public class ElementDao extends TreeSupportDao<ElementGroup> implements IElementDao {
    
    public ElementDao() {
        super(ElementGroup.class);
    }
 
    public IElement saveElement(IElement obj) {  
        if(obj.getId() == null) {    
            return (IElement) createObject(obj);
        }
        
        update(obj);
        return obj;
    }
    
    public IElement deleteElement(IElement element){
        delete(element);
        
        // 删除资源文件
        URL url = URLUtil.getWebFileUrl(element.getResourceBaseDir());             
        File path = new File(url.getPath());        
        File fileDir = FileHelper.findPathByName(path, element.getCode() + element.getId());
        if (fileDir != null) {
            FileHelper.deleteFile(fileDir);
        }
        return element;
    }
    
    public IElement moveTo(IElement element) {
        return saveElement(element);  
    }
 
    /*****************************************************************************************************************
     ************************************ 以下是对元素（修饰器/布局器/Portlet）组的操作 ************************************* 
     *****************************************************************************************************************/
    
    public ElementGroup saveGroup(ElementGroup group){
        if(group.getId() == null) {    
            return create(group);
        }
        
        update(group);
        return group;
    }
    
    public void deleteGroup(ElementGroup group){
        List<ElementGroup> groups = getChildrenById(group.getId());
        
        List<Long> groupIds = new ArrayList<Long>();
        for ( ElementGroup temp : groups ){
            groupIds.add(temp.getId());
            delete(temp); //删除组
        }
        
        if(groupIds.isEmpty()) return;
        
        //删除元素
        String hql = "from " + group.getClassNameByType() + " t where t.groupId in (:groupIds) order by t.decode";
        List<?> elements =  getEntities(hql, new Object[]{"groupIds"}, new Object[]{groupIds});
        
        for ( Object temp : elements ) {
            deleteElement((IElement) temp);
        }
    }
 
    public List<ElementGroup> getChildrenById(Long groupId) {
        ElementGroup group = getEntity(groupId);
        List<ElementGroup> children = super.getChildrenById(groupId);
        for( Iterator<ElementGroup> it = children.iterator(); it.hasNext(); ) {
            if( !it.next().getType().equals(group.getType())) {
                it.remove();
            }
        }
        return children;
    }
    
    //注：子组的decode值可能和父组下的某个组件decode值相同，所以这里条件过滤要加上t.decode <> 子组decode
    public List<?> getAllElementsByGroup(Long groupId) {
        ElementGroup group = getEntity(groupId);
        String hql = "from " + group.getClassNameByType() + " t where t.decode like ?  and t.decode <> ? order by t.decode";
        return getEntities(hql, group.getDecode() + "%", group.getDecode());
    }
    
    public List<?> getElementsByGroup( Long groupId ) {
        ElementGroup group = getEntity(groupId);
        String hql = "from " + group.getClassNameByType() + " t where t.groupId = ? order by t.decode";
        return getEntities(hql, groupId);
    }
 
}