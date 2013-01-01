package com.jinhe.tss.portal.engine.model;

import java.util.Map;

import com.jinhe.tss.portal.entity.Layout;

/**
 * 布局器节点对象：用于解析门户中使用到的布局器
 */
public class LayoutNode extends AbstractElementNode {
    
    /**
     * 布局器中可以显示区域数量
     * <li>n>0 － 此布局器有多少个区域可以填充子节点，用于判断是否适用版面
     * <li> -n － 子节点循环填充每个区域；
     */
    private Integer portNumber;
    
    public Integer getPortNumber() { return portNumber; }

    /**
     * 将布局器的实体转换为 LayoutNode 对象，
     * @param obj
     * 			布局器Entity
     * @param parent
     * 			布局器所在的门户结构Node
     * @param parameterOnPs
     * 			门户结构上定义的参数内容（布局器实例化时自定义参数值）
     */
    public LayoutNode(Layout obj, SubNode parent, String parametersOnPs){
        super(obj, parent, parametersOnPs);
        
        this.portNumber = obj.getPortNumber();
    }
    
    public LayoutNode(Layout obj, SubNode parent, Map<String, String> configedParameters) {
        super(obj, parent);
        getParameters().putAll(configedParameters);
    }
    
    public LayoutNode(Layout obj, SubNode parent) {
        super(obj, parent);
    }

    public Object clone() {
        LayoutNode copy = (LayoutNode) super.clone();
        return copy;
    }
}