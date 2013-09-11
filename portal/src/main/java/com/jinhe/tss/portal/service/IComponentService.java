package com.jinhe.tss.portal.service;

import java.util.List;

import com.jinhe.tss.framework.component.log.Logable;
import com.jinhe.tss.portal.entity.Component;

/**
 * 对组件（修饰器/布局器/Portlet）及其组的进行操作的service
 */
public interface IComponentService {

    /**
     * 获取（修饰器/布局器/Portlet）所有的组件及其组
     * @return
     */
    List<?> getAllComponentsAndGroups();
    
    /**
     * 获取指定类型（修饰器/布局器/Portlet）所有的启用的组件及其组.
     * （编辑 "门户结构" 选择修饰器/布局器/Portlet 的时候会用到）
     * @param type
     * @return
     */
    List<?> getEnabledComponentsAndGroups(int type);

    /**
     * 保存一个组件
     * @param obj
     * @return
     */
    @Logable(operateTable="门户组件", operateType="新建/修改", operateInfo="新建/修改了 ${returnVal} 节点")
    Component saveComponent(Component obj);

    /**
     * 删除一个组件
     * @param id
     * @return
     */
    @Logable(operateTable="门户组件", operateType="删除", operateInfo="删除了 ID为 ${args[0]} 的 节点 （${returnVal}）")
    Component deleteComponent(Long id);

    /**
     * 停用、启用一个组件
     * @param id
     * @param state
     */
    @Logable(operateTable="门户组件", operateType="停用/启用", operateInfo="<#if args[1]=1>停用<#else>启用</#if>了(ID: ${args[0]})节点")
    void disableComponent(Long id, Integer state);

    /**
     * 获取一个组件的详细信息
     * @param id
     * @return
     */
    Component getComponent(Long id);

    /**
     * 对组件进行排序
     * @param id
     * @param targetId
     * @param direction
     */
    void sort(Long id, Long targetId, int direction);

    /**
     * 将修饰器设置为默认修饰器
     * @param decoratorId
     */
    void setDecoratorAsDefault(Long decoratorId);

    /**
     * 将布局器设置为默认布局器
     * @param layoutId
     */
    void setLayoutAsDefault(Long layoutId);

    /*****************************************************************************************************************
     ************************************ 以下是对组件（修饰器/布局器/Portlet）组的操作 ************************************* 
     *****************************************************************************************************************/
    /**
     * 移动一个组件到另外一个组
     * @param id
     * @param groupId
     */
    void moveTo(Long id, Long groupId);

    /**
     * 删除一个组
     * @param id
     */
    void deleteComponentGroup(Long id);

    /**
     * 获取某个组件类型的所有分组
     * @param type
     * @return
     */
    List<?> getComponentGroups(Integer type);

    /**
     * 复制一个组件到另外一个组
     * @param id
     * @param groupId
     * @return
     */
    Component copyTo(Long id, Long groupId);
    
}
