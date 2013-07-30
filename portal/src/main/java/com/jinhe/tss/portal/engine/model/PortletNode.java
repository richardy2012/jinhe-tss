package com.jinhe.tss.portal.engine.model;

import java.util.Map;

import com.jinhe.tss.portal.engine.FreemarkerParser;
import com.jinhe.tss.portal.entity.Component;

/**
 * Portlet节点对象：用于解析门户中使用到的Portlet
 */
public class PortletNode extends AbstractElementNode {
	
	/**
	 * Freemarker模板引擎解析器
	 */
	private FreemarkerParser freemarkerParser;
 
    /**
     * 将Portlet的实体转换为 PortletNode 对象，
     * @param obj
     * 			Portlet Entity
     * @param parent
     * 			Portlet所在的门户结构Node
     * @param parameterOnPs
     * 			门户结构上定义的参数内容（Portlet实例化时自定义参数值）
     */
    public PortletNode(Component obj, SubNode parent, String parametersOnPs){
        super(obj, parent, parametersOnPs);
    }
    
    public PortletNode(Component obj, SubNode parent, Map<String, String> configedParameters ) {
        super(obj, parent);
        getParameters().putAll(configedParameters);
    }
    
    public Object clone(){
        PortletNode copy = (PortletNode) super.clone();
        return copy;
    }
    
    public FreemarkerParser getFreemarkerParser() {
        return freemarkerParser;
    }
    
    public void setFreemarkerParser(FreemarkerParser freemarkerParser) {
        this.freemarkerParser = freemarkerParser;
    }
}