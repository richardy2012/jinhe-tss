package com.jinhe.tss.portal.dao.impl;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jinhe.tss.framework.persistence.TreeSupportDao;
import com.jinhe.tss.portal.dao.IElementDao;
import com.jinhe.tss.portal.entity.Component;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.URLUtil;

@Repository("ComponentDao")
public class ElementDao extends TreeSupportDao<Component> implements IElementDao {
    
    public ElementDao() {
        super(Component.class);
    }
 
    public Component saveElement(Component obj) {  
        if(obj.getId() == null) {    
            return (Component) createObject(obj);
        }
        
        update(obj);
        return obj;
    }
    
    public Component deleteElement(Component element){
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
    
    public Component moveTo(Component element) {
        return saveElement(element);  
    }
 
    /*********************************** 以下是对元素（修饰器/布局器/Portlet）组的操作 *************************************/
    
    public void deleteGroup(Component group){
        List<Component> groups = getChildrenById(group.getId());
        
        List<Long> groupIds = new ArrayList<Long>();
        for ( Component temp : groups ){
            groupIds.add(temp.getId());
            delete(temp); //删除组
        }
        
        if(groupIds.isEmpty()) return;
        
        //删除元素
        String hql = "from Element t where t.groupId in (:groupIds) order by t.decode";
        List<?> elements =  getEntities(hql, new Object[]{"groupIds"}, new Object[]{groupIds});
        
        for ( Object temp : elements ) {
            deleteElement((Component) temp);
        }
    }
 
    public List<Component> getChildrenById(Long groupId) {
    	Component group = getEntity(groupId);
        List<Component> children = super.getChildrenById(groupId);
        for( Iterator<Component> it = children.iterator(); it.hasNext(); ) {
            if( !it.next().getType().equals(group.getType())) {
                it.remove();
            }
        }
        return children;
    }
    
    //注：子组的decode值可能和父组下的某个组件decode值相同，所以这里条件过滤要加上t.decode <> 子组decode
    public List<?> getAllElementsByGroup(Long groupId) {
    	Component group = getEntity(groupId);
        String hql = "from Element t where t.decode like ?  and t.decode <> ? order by t.decode";
        return getEntities(hql, group.getDecode() + "%", group.getDecode());
    }
    
    public List<?> getElementsByGroup( Long groupId ) {
        String hql = "from Element t where t.parentId = ? order by t.decode";
        return getEntities(hql, groupId);
    }
 
}