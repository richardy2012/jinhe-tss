package com.jinhe.tss.portal.engine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.tree.DefaultDocument;
import org.dom4j.tree.DefaultElement;

import com.jinhe.tss.portal.dao.IPortalDao;
import com.jinhe.tss.portal.engine.model.AbstractElementNode;
import com.jinhe.tss.portal.engine.model.DecoratorConfigable;
import com.jinhe.tss.portal.engine.model.DecoratorNode;
import com.jinhe.tss.portal.engine.model.LayoutConfigable;
import com.jinhe.tss.portal.engine.model.LayoutNode;
import com.jinhe.tss.portal.engine.model.Node;
import com.jinhe.tss.portal.engine.model.PageNode;
import com.jinhe.tss.portal.engine.model.PortalNode;
import com.jinhe.tss.portal.engine.model.PortletInstanceNode;
import com.jinhe.tss.portal.engine.model.PortletNode;
import com.jinhe.tss.portal.engine.model.SectionNode;
import com.jinhe.tss.portal.engine.model.SubNode;
import com.jinhe.tss.portal.entity.Decorator;
import com.jinhe.tss.portal.entity.Layout;
import com.jinhe.tss.portal.entity.Portlet;
import com.jinhe.tss.portal.helper.IElement;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.XMLDocUtil;

/**
 * <p> PersonalManager.java </p>
 * 
 * 门户个性化定制管理器。 PortalNode.clone + 个性化定制信息 ==> 个性化PortalNode ==> 个性化页面
 * 
 * PS: 门户缓存池中：
 * 	   默认portalNode和用户自定义的personalNode不能放在同一个缓存池中，因为他们的reload方法不一样。
 * 
 */
public class PersonalManager {
    
    public final static String STRUCTURE_NODE_NAME = "structure";
    public final static String PAGE_NODE_NAME = "page";
    public final static String SECTION_NODE_NAME = "section";
    public final static String PORTLETINSTACE_NODE_NAME = "portletInstanse";
    
    public final static String ELEMENT_TYPE_NAME = "class";
    public final static String DECORATOR_TYPE = "Decorator";
    public final static String LAYOUT_TYPE    = "Layout";
    public final static String PORTLET_TYPE   = "Portlet";
    
    public final static String ID = "id";
    
    public final static String PARAMS_NODE_NAME = "params";
    public final static String PARAM_NODE_NAME = "param";
    public final static String STRUCTURE_ID = "structureId";
    public final static String ELEMENT_ID = "elementId";

    
    private IPortalDao dao;

    private PortalNode portal;

    private List<Document> personalPageXMLs;  //页面个性化定制信息XML列表

    public PersonalManager(PortalNode portal, List<Document> personalPageXMLs, IPortalDao dao) {
        this.portal = portal;
        this.personalPageXMLs = personalPageXMLs;
        this.dao = dao;
    }

    /**
     * 解析用户的自定义页面。
     * 解析成功后返回用户自定义门户PortalNode节点。
     * @return
     */
    public PortalNode generatePersonalPortal() {
        if (personalPageXMLs != null && personalPageXMLs.size() > 0) {
            for ( Document personalPageXML : personalPageXMLs ) {
                generatorPersonalPage(personalPageXML);
            }
        }
        return portal;
    }

    /**
     * 解析单个自定义页面。
     * 
     * @param doc
     */
    private void generatorPersonalPage(Document doc) {
        Element structureNode = (Element) doc.selectSingleNode("//" + STRUCTURE_NODE_NAME);
        Element pageElement = (Element) structureNode.element(PAGE_NODE_NAME);
        Element paramsNode = (Element) doc.selectSingleNode("//" + PARAMS_NODE_NAME);
        Map<String, Map<String, String>> params = parsePersonalParams(paramsNode);
        parseStructure(pageElement, params);
    }
    
    /**
     * 解析每一个门户结构节点。
     * 此方法本生递归调用，直至解析完所有的叶子节点。
     * @param structure
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
	private Node parseStructure(Element structure, Map<String, Map<String, String>> params){
        Long structureId = Long.valueOf(structure.attributeValue(ID));
        SubNode node = (SubNode) portal.getNodesMap().get(structureId);
        
        if(node == null)
            return null;
        
        DecoratorConfigable decoratorConfigableNode = (DecoratorConfigable)node;
		DecoratorNode decoratorNode = (decoratorConfigableNode).getDecoratorNode();
        Object[] objs = repair(structure, params, node, DECORATOR_TYPE, decoratorNode);
        if(objs != null) {
            DecoratorNode personalDecoratorNode = new DecoratorNode((Decorator)objs[0], node, (Map<String, String>)objs[1]);
			decoratorConfigableNode.setDecoratorNode(personalDecoratorNode);
        }
            
        if( isLeaf(structure) ){
            PortletInstanceNode portletInstanceNode = (PortletInstanceNode) node;
			PortletNode portletNode = (portletInstanceNode).getPortletNode();
            objs = repair(structure, params, node, PORTLET_TYPE, portletNode);
            if(objs != null) {
                PortletNode personalPortletNode = new PortletNode((Portlet)objs[0], node, (Map<String, String>)objs[1]);
				portletInstanceNode.setPortletNode(personalPortletNode);
            }
        } 
        else {
            LayoutConfigable layoutConfigableNode = (LayoutConfigable)node;
			LayoutNode layoutNode = (layoutConfigableNode).getLayoutNode();
            objs = repair(structure, params, node, LAYOUT_TYPE, layoutNode);
            if(objs != null) {
                LayoutNode personalLayoutNode = new LayoutNode((Layout)objs[0], node, (Map<String, String>)objs[1]);
				layoutConfigableNode.setLayoutNode(personalLayoutNode);
            }
            
            node.getChildren().clear();
            for(Iterator<?> it = structure.elementIterator(); it.hasNext();){
                Element sonElement = (Element) it.next();
                
                if("emptyPortlet".equals(sonElement.getName())) continue;
                
                SubNode son = (SubNode) parseStructure(sonElement, params);
                son.setParent(node);
                node.getChildren().add(son);
            }
        }
        return node;
    }
    
    /**
     * 修复门户结构节点的元素。
     * 如果元素和默认元素的ID相等，则沿用老的元素Node，并将自定义的参数信息（有的话）覆盖到老的Node参数Map中。
     * 如果不相等，则创建一个新的元素Node，并将自定义的参数信息（有的话）覆盖到Node参数Map中。
     * 
     * @param structure
     * @param params
     * @param node
     * @param type
     * @param elementNode
     * @return
     */
    private Object[] repair(Element structure, Map<String, Map<String, String>> params, 
    		SubNode node, String type, AbstractElementNode elementNode) {
    	
        Long structureId = Long.valueOf(structure.attributeValue(ID));
        Long elementId   = Long.valueOf(structure.attributeValue(type.toLowerCase() + "Id"));
        
        Map<String, String> personalElementParams = params.get(structureId + "_" + type + "_" + elementId);
        if(elementNode.getId().equals(elementId)){
            if(personalElementParams != null) {
                elementNode.getParameters().putAll(personalElementParams);
            }
            return null;
        } 
        else {
            IElement element = (IElement) dao.getEntity(elementNode.getElement().getClass(), elementId);
            return new Object[]{ element, personalElementParams };
        }
    }
    
    /**
     * 判断门户结构是否为叶子节点。如果是portlet实例的话则返回true。
     * @param structure
     * @return
     */
    private boolean isLeaf(Element structure){
        return PORTLETINSTACE_NODE_NAME.equals(structure.getName());
    }
    
    /**
     * 解析各个元素对应的参数。
     * @param paramsNode
     * @return
     */
    private Map<String, Map<String, String>> parsePersonalParams(Element paramsNode){
        Map<String, Map<String, String>> paramsMap = new HashMap<String, Map<String, String>>();
        for(Iterator<?> it = paramsNode.elementIterator(PARAM_NODE_NAME); it.hasNext();){
            Element paramNode = (Element) it.next();
            
            Map<String, String> paramMap = new HashMap<String, String>();
            for(Iterator<?> iter = paramNode.elementIterator(); iter.hasNext();){
                Element attrNode = (Element) iter.next();
                String text = attrNode.getText();
                if( !EasyUtils.isNullOrEmpty(text) ) {
                    paramMap.put(attrNode.getName(), text);
                }
            }
            
            String structureId = paramNode.attributeValue(STRUCTURE_ID);
            String elementId = paramNode.attributeValue(ELEMENT_ID);
            String type = paramNode.attributeValue(ELEMENT_TYPE_NAME);
            paramsMap.put(structureId + "_" + type + "_" + elementId, paramMap);
        }
        return paramsMap;
    }

    /**
     * 将门户结构按页面解析成XML格式。
     * @return
     */
    public String genetateStructureXML(PageNode page) {
        Document doc = new DefaultDocument();
        doc.addElement("personal");
        Element structureElement = new DefaultElement("structure");
        Element paramsElement = new DefaultElement("params");
        
        Element pageElement = doGen(page, paramsElement);

        structureElement.add(pageElement);
        doc.getRootElement().add(structureElement);
        doc.getRootElement().add(paramsElement);
        return doc.asXML();
    }

    private Element doGen(SubNode node, Element paramsElement) {
        Element element = new DefaultElement("page");
        
        if(node instanceof PortletInstanceNode){
            element = new DefaultElement("portletInstanse");
            PortletNode portletNode = ((PortletInstanceNode)node).getPortletNode();
            element.addAttribute("portletId", portletNode.getId().toString());
            paramsElement.add(genParamNode(portletNode, "portlet"));
        }
        else {
            if(node instanceof SectionNode)
                element = new DefaultElement("section");
                
            LayoutNode layoutNode = ((LayoutConfigable)node).getLayoutNode();
            element.addAttribute("layoutId", layoutNode.getId().toString());
            paramsElement.add(genParamNode(layoutNode, "layout"));
        }
        element.addAttribute("id", node.getId().toString());
        DecoratorNode decoratorNode = ((DecoratorConfigable)node).getDecoratorNode();
        element.addAttribute("decoratorId", decoratorNode.getId().toString());
        paramsElement.add(genParamNode(decoratorNode, "decorator"));
        
        for(Iterator<?> it = node.getChildren().iterator(); it.hasNext();){
            SubNode son = (SubNode) it.next();
            element.add(doGen(son, paramsElement));
        }
        return element;
    }
    
    private Element genParamNode(AbstractElementNode node, String type) {
        Element element = XMLDocUtil.map2DataNode(node.getParameters(), "param");
        element.addAttribute("structureId", node.getParent().getId().toString());
        element.addAttribute("elementId", node.getId().toString());
        element.addAttribute("class", type);
        return element;
    }
}
