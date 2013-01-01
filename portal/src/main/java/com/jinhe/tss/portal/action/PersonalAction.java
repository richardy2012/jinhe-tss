package com.jinhe.tss.portal.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;

import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.web.dispaly.tree.ILevelTreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeAttributesMap;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.tree.TreeNode;
import com.jinhe.tss.portal.engine.FreeMarkerSupportAction;
import com.jinhe.tss.portal.engine.HTMLGenerator;
import com.jinhe.tss.portal.engine.model.DecoratorConfigable;
import com.jinhe.tss.portal.engine.model.DecoratorNode;
import com.jinhe.tss.portal.engine.model.LayoutConfigable;
import com.jinhe.tss.portal.engine.model.LayoutNode;
import com.jinhe.tss.portal.engine.model.PageNode;
import com.jinhe.tss.portal.engine.model.PortalNode;
import com.jinhe.tss.portal.engine.model.SubNode;
import com.jinhe.tss.portal.entity.Decorator;
import com.jinhe.tss.portal.entity.ElementGroup;
import com.jinhe.tss.portal.entity.Layout;
import com.jinhe.tss.portal.entity.Theme;
import com.jinhe.tss.portal.helper.IElement;
import com.jinhe.tss.portal.service.IElementService;
import com.jinhe.tss.portal.service.IPortalService;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.URLUtil;
import com.jinhe.tss.util.XMLDocUtil;

import freemarker.template.TemplateException;

/** 
 * 门户自定义管理
 */
public class PersonalAction extends FreeMarkerSupportAction {

    private Long   id;
    private Long   newId;
    
    private Long   themeId; 

    private String method; // browse/view/maintain
    
    private String personalXML;
    private String elementType; //组件类型
    private String newElementType; //组件类型
    
    private IPortalService  portalService;
    private IElementService elementService;
    
    public String addElement() throws IOException, TemplateException{
        return null;
    }
    
    public String changeElement() throws IOException, TemplateException{
        PortalNode portalNode = (PortalNode) portalService.getPortal(portalId, themeId, method).clone();
        SubNode content = null;
        if("Portlet".equals(elementType) && "Portlet".equals(newElementType)){
            content = (SubNode) portalNode.getNodesMap().get(newId); //newId: 新Portlet实例ID
        }
        else if("Layout".equals(elementType) && "Portlet".equals(newElementType)){
            content = (SubNode) portalNode.getNodesMap().get(newId); //newId: 新Portlet实例ID
        }
        else if("Layout".equals(elementType) && "Layout".equals(newElementType)){
            //id : 版面或页面的ID    //newId: 新布局器ID
            content = (SubNode) portalNode.getNodesMap().get(id);
            Layout d = (Layout) elementService.getElementInfo(Layout.class, newId);
            ((LayoutConfigable)content).setLayoutNode(new LayoutNode(d, (SubNode)content));
        }
        else if("Decorator".equals(elementType) && "Decorator".equals(newElementType)){
            //id : 修饰器所在门户结构ID    //newId: 新布局器ID
            content = (SubNode) portalNode.getNodesMap().get(id);
            Decorator d = (Decorator) elementService.getElementInfo(Decorator.class, newId);
            ((DecoratorConfigable)content).setDecoratorNode(new DecoratorNode(d, (SubNode)content));
        }
        
        if(content instanceof PageNode){
            HTMLGenerator gen = new HTMLGenerator(portalNode, content.getId(), getFreemarkerParser());
            return printHTML(gen.toHTML(), false);
        }
        
        HTMLGenerator gen = new HTMLGenerator(portalNode, content.getId(), content.getParent().getId(), null);
        return print("Change", gen.toXML());
    }
    
    public String getThemeList4Personal(){
        List<?> list = portalService.getThemesByPortal(portalId);
        StringBuffer sb = new StringBuffer("<data>");
        for( Object temp : list ){
            Theme theme = (Theme) temp;
            sb.append("<row themeId=\"" + theme.getId()+ "\" themeName=\"" + theme.getName() + "\"/>");
        }
        return print("ThemeList", sb.append("</data>"));
    }
    
    public String removePersonalInfo() {
        portalService.removePersonalInfo(portalId, themeId, Environment.getOperatorId(), id);
        return printSuccessMessage("成功还原为默认");
    }

    public String savePersonalInfo() throws IOException, TemplateException {
        portalService.savePersonalInfo(portalId, themeId, Environment.getOperatorId(), id, personalXML);
        return printSuccessMessage("保存自定义成功");
    }

    public String savePersonalTheme() throws IOException, TemplateException {
        portalService.savePersonalTheme(portalId, Environment.getOperatorId(), themeId);
        return printSuccessMessage("更改主题成功");
    }
    
    public String getElementParamTemplate() {
        Integer type = translateElementType(elementType);
        String desDir = URLUtil.getWebFileUrl(ElementGroup.getBasePathByType(type)).getPath();
        IElement element = (IElement) elementService.getElementInfo(ElementGroup.getClassByType(type), id);
        String templateUri = desDir + element.getCode() + element.getId() + "/paramsXForm.xml";  
        
        if(!new File(templateUri).exists())
            return print("Settings", "");
        Document doc = XMLDocUtil.openDocument(templateUri);
        return print("Settings", doc.selectSingleNode("//xform").asXML());
    }
    
    private Integer translateElementType(String elementType){
        Integer type = null;
        if("Portlet".equals(elementType)){
            type = new Integer(ElementGroup.PORTLET_TYPE);
        }
        if("Decorator".equals(elementType)){
            type = new Integer(ElementGroup.DECORATOR_TYPE);
        }
        if("Layout".equals(elementType)){
            type = new Integer(ElementGroup.LAYOUT_TYPE);
        }
        return type;
    }
    
    public String getSimilarElements() {
        List<?> layouts = new ArrayList<Object>();
        if("Layout".equals(elementType))
            layouts = elementService.getLayouts();
        
        List<?> decorators = new ArrayList<Object>();
        if("Decorator".equals(elementType))
            decorators = elementService.getDecorators();
        
        List<?> portletInstanses = new ArrayList<Object>();
        if("Layout".equals(elementType) || "Portlet".equals(elementType))
            portletInstanses = portalService.getPortletInstansesInPortal(portalId);
        
        TreeEncoder encoder = new TreeEncoder(new Object[]{layouts, decorators, portletInstanses}, new ITreeParser(){
            public TreeNode parse(Object data) {
                TreeNode root = new TreeNode();
                String[] types = new String[]{"Layout", "Decorator", "Portlet"};
                TreeNode secondRoots[] = new TreeNode[3];
                
                secondRoots[0] = createTreeNode(new Long(-1), "布局器", "0");
                secondRoots[1] = createTreeNode(new Long(-2), "修饰器", "0");
                secondRoots[2] = createTreeNode(new Long(-3), "portlet实例", "0");
                
                for(int i = 0; i < secondRoots.length; i++){
                    List<?> list = (List<?>) ((Object[])data)[i];
                    for(Iterator<?> it = list.iterator(); it.hasNext();){  
                        ILevelTreeNode temp = (ILevelTreeNode) it.next();
                        String name = (String)BeanUtil.getPropertyValue(temp, "name");
                        TreeNode treeNode = createTreeNode(temp.getId(), name, types[i]);
                        secondRoots[i].addChild(treeNode);
                    }
                }
                root.getChildren().addAll(Arrays.asList(secondRoots));
                return root;
            }
        });
        encoder.setNeedRootNode(false);
        return print("ListTree", encoder.toXml());
    }
    
    private TreeNode createTreeNode(final Long id, final String name, final String type){
        return new TreeNode(new ITreeNode(){
            public TreeAttributesMap getAttributes() { 
                TreeAttributesMap map = new TreeAttributesMap(id, name);
                map.put("type", type);
                return map;
            }
        });
    }
    

    public void setPortalService(IPortalService service) { 
        this.portalService = service; 
    }
    public void setElementService(IElementService elementService) { 
        this.elementService = elementService; 
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public void setPersonalXML(String personalXML) {
        this.personalXML = personalXML;
    }
    public void setElementType(String elementType) {
        this.elementType = elementType;
    }
    public void setNewElementType(String newElementType) {
        this.newElementType = newElementType;
    }
    public void setNewId(Long newId) {
        this.newId = newId;
    }
}