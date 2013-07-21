package com.jinhe.tss.portal.service;

import java.io.File;
import java.util.List;

import com.jinhe.tss.framework.component.log.Logable;
import com.jinhe.tss.portal.entity.ElementGroup;
import com.jinhe.tss.portal.helper.IElement;

/**
 * 对元素（修饰器/布局器/Portlet）及其组的进行操作的service
 */
public interface IElementService {

    /**
     * 获取指定类型（修饰器/布局器/Portlet）所有的元素及其组
     * @param elementName
     * @return
     */
    List<?> getAllElementsAndGroups(int type);
    
    /**
     * 获取指定类型（修饰器/布局器/Portlet）所有的启用的元素及其组.
     * （编辑 "门户结构" 选择修饰器/布局器/Portlet 的时候会用到）
     * @param type
     * @return
     */
    List<?> getAllStartElementsAndGroups(int type);

    /**
     * 保存一个元素
     * @param obj
     * @return
     */
    @Logable(operateTable="门户元素", operateType="新建/修改", operateInfo="新建/修改了 ${returnVal} 节点")
    IElement saveElement(IElement obj);

    /**
     * 删除一个元素
     * @param clazz
     * @param id
     * @return
     */
    @Logable(operateTable="门户元素", operateType="删除", operateInfo="删除了 ID为 ${args[1]} 的 节点 （类型为：${args[0]}）")
    IElement deleteElement(Class<?> elementClass, Long id);

    /**
     * 停用、启用一个元素
     * @param elementClass
     * @param id
     * @param disabled
     */
    @Logable(operateTable="门户元素", operateType="停用/启用", operateInfo="<#if args[2]=1>停用<#else>启用</#if>了(ID: ${args[1]})节点（类型为：${args[0]}）")
    void disableElement(Class<?> elementClass, Long id, Integer disabled);

    /**
     * 获取一个元素的详细信息
     * @param elementClass
     * @param id
     * @return
     */
    IElement getElementInfo(Class<?> elementClass, Long id);

    /**
     * 对元素进行排序
     * @param id
     * @param targetId
     * @param direction
     * @param elementClass
     */
    void sortElement(Long id, Long targetId, int direction, Class<?> elementClass);

    /**
     * 复制一个元素
     * @param id
     * @param path
     * @param elementClass
     * @return
     */
    IElement copyElement(Long id, File path, Class<?> elementClass);

    /**
     * 将修饰器设置为默认修饰器
     * @param decoratorId
     */
    void setDecorator4Default(Long decoratorId);

    /**
     * 将布局器设置为默认布局器
     * @param layoutId
     */
    void setLayout4Default(Long layoutId);

    /*****************************************************************************************************************
     ************************************ 以下是对元素（修饰器/布局器/Portlet）组的操作 ************************************* 
     *****************************************************************************************************************/
    /**
     * 移动一个元素到另外一个组
     * @param id
     * @param groupId
     */
    void moveTo(Long id, Long groupId);

    /**
     * 删除一个组
     * @param id
     */
    void deleteGroupById(Long id);

    /**
     * 对组进行排序
     * @param id
     * @param targetId
     * @param direction
     */
    void sortByType(Long id, Long targetId, int direction);

    /**
     * 保存一个组
     * @param entity
     * @return
     */
    ElementGroup saveGroup(ElementGroup entity);

    /**
     * 获取一个组的详细信息
     * @param id
     * @return
     */
    ElementGroup getGroupInfo(Long id);

    /**
     * 获取某个元素类型的所有分组
     * @param type
     * @return
     */
    List<?> getGroupsByType(Integer type);

    /**
     * 复制一个元素到另外一个组
     * @param id
     * @param groupId
     * @return
     */
    Object[] copyTo(Long id, Long groupId);
    
    /**
     * 获取布局器列表，用以门户自定义组件替换
     * @return
     */
    List<?> getLayouts();
    
    /**
     *获取修饰器列表，用以门户自定义组件替换
     * @return
     */
    List<?> getDecorators();
}
