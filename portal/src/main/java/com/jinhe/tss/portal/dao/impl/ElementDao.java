package com.jinhe.tss.portal.dao.impl;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jinhe.tss.framework.persistence.TreeSupportDao;
import com.jinhe.tss.portal.dao.IElementDao;
import com.jinhe.tss.portal.entity.Element;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.URLUtil;

public class ElementDao extends TreeSupportDao<Element> implements IElementDao {
    
    public ElementDao() {
        super(Element.class);
    }
 
    public Element saveElement(Element obj) {  
        if(obj.getId() == null) {    
            return (Element) createObject(obj);
        }
        
        update(obj);
        return obj;
    }
    
    public Element deleteElement(Element element){
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
    
    public Element moveTo(Element element) {
        return saveElement(element);  
    }
 
    /*********************************** 以下是对元素（修饰器/布局器/Portlet）组的操作 *************************************/
    
    public void deleteGroup(Element group){
        List<Element> groups = getChildrenById(group.getId());
        
        List<Long> groupIds = new ArrayList<Long>();
        for ( Element temp : groups ){
            groupIds.add(temp.getId());
            delete(temp); //删除组
        }
        
        if(groupIds.isEmpty()) return;
        
        //删除元素
        String hql = "from Element t where t.groupId in (:groupIds) order by t.decode";
        List<?> elements =  getEntities(hql, new Object[]{"groupIds"}, new Object[]{groupIds});
        
        for ( Object temp : elements ) {
            deleteElement((Element) temp);
        }
    }
 
    public List<Element> getChildrenById(Long groupId) {
    	Element group = getEntity(groupId);
        List<Element> children = super.getChildrenById(groupId);
        for( Iterator<Element> it = children.iterator(); it.hasNext(); ) {
            if( !it.next().getType().equals(group.getType())) {
                it.remove();
            }
        }
        return children;
    }
    
    //注：子组的decode值可能和父组下的某个组件decode值相同，所以这里条件过滤要加上t.decode <> 子组decode
    public List<?> getAllElementsByGroup(Long groupId) {
    	Element group = getEntity(groupId);
        String hql = "from Element t where t.decode like ?  and t.decode <> ? order by t.decode";
        return getEntities(hql, group.getDecode() + "%", group.getDecode());
    }
    
    public List<?> getElementsByGroup( Long groupId ) {
        String hql = "from Element t where t.parentId = ? order by t.decode";
        return getEntities(hql, groupId);
    }
 
}