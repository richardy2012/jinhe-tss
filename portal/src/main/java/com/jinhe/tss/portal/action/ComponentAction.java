package com.jinhe.tss.portal.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeTranslator;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.tree.TreeNode;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.engine.FreeMarkerSupportAction;
import com.jinhe.tss.portal.entity.Component;
import com.jinhe.tss.portal.helper.ComponentHelper;
import com.jinhe.tss.portal.helper.StrictLevelTreeParser;
import com.jinhe.tss.portal.service.IComponentService;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.MacrocodeCompiler;
import com.jinhe.tss.util.URLUtil;
import com.jinhe.tss.util.XMLDocUtil;
import com.jinhe.tss.util.XmlUtil;

import freemarker.template.TemplateException;

@Controller
@RequestMapping("/component")
public class ComponentAction extends FreeMarkerSupportAction {
 
    @Autowired private IComponentService service;

    @RequestMapping("/list")
    public void getAllComponents4Tree() {
        List<?> data = service.getAllComponentsAndGroups();
        TreeEncoder encoder = new TreeEncoder(data, new LevelTreeParser());        
        print("SourceTree", encoder);
    }

    /**
     * 所有启动的组件树型展示.（编辑门户结构的时候用到）
     */
    @RequestMapping("/list/{type}/enabled")
    public void getEnabledComponents4Tree(int type) {
        List<?> data = service.getEnabledComponentsAndGroups(type);
        
        TreeEncoder encoder = new TreeEncoder(data, new LevelTreeParser());        
        encoder.setNeedRootNode(false);
        
        encoder.setTranslator( new ITreeTranslator() {
            public Map<String, Object> translate(Map<String, Object> attributes) {
                if( Boolean.TRUE.equals(attributes.get("isGroup")) ) { // 使得组节点不可选
                    attributes.put(TreeNode.TREENODE_ATTRIBUTE_CANSELECTED, "0"); 
                }
                return attributes;
            }           
        });
        print("SourceTree", encoder);
    }
    
    /**
     * 获取组件参数,并拼装成一个xml返回
     */
    @RequestMapping("/params")
    public void getDefaultParams4Xml(Long id) {
        Component component = service.getComponent(id);
        String elementType = component.getComponentType();
        
        StringBuffer sb = new StringBuffer("<" + elementType + " ");
        String xpath = "//" + elementType + "/parameters/param";
        List<?> parameters = XMLDocUtil.dataXml2Doc(component.getDefinition()).selectNodes(xpath);
        if (parameters != null) {
            for (int i = 0; i < parameters.size(); i++) {
                Element param = (Element) parameters.get(i);
                String name = param.attributeValue("name");
                String defaultValue = param.attributeValue("defaultValue");
                sb.append(name).append("=\"").append(XmlUtil.toFormXml(defaultValue)).append("\" ");
            }
        }
        sb.append(">").append("model/" + elementType + "/" + component.getCode() + "/" + Component.PARAM_FILE);                    

        print("ComponentParams", sb.append("</" + elementType + ">").toString());
    }

    /**
     * 获取组件详细信息.
     */
    @RequestMapping("/{groupId}/{id}")
    public void getComponentInfo(Long id, Long groupId) {
        Component componentGroup = service.getComponent(groupId);
        String templatePath = componentGroup.getTemplatePath();
        
        XFormEncoder encoder;
        if ( DEFAULT_NEW_ID.equals(id) ) {   // 新增组件
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("groupId", groupId);
            encoder = new XFormEncoder(templatePath, map);
        } 
        else { // 修改组件
            encoder = new XFormEncoder(templatePath, service.getComponent(id));
        }
        print("DetailInfo", encoder);
    }

    /**
     * 新增元素（组）.
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public void save(Component element) {
        boolean isNew = element.getId() == null ? true : false;      
        element = service.saveComponent(element);
        doAfterSave(isNew, element, "SourceTree");
    }

    /**
     * 删除组件
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(Long id) {
        service.deleteComponent(id);
        printSuccessMessage("删除成功");
    }

    /**
     * 停用/启用 组件（将其disabled属性设为"1"/"0"）
     */
    @RequestMapping("/disable/{id}/{state}")
    public void disabled(Long id, int state) {
        service.disableComponent(id, state);
        printSuccessMessage();
    }
    
    /**
     * 组件排序
     */
    @RequestMapping(value = "/sort/{id}/{targetId}/{direction}", method = RequestMethod.POST)
    public void sort(HttpServletResponse response, 
            @PathVariable("id") Long id, 
            @PathVariable("targetId") Long targetId,  
            @PathVariable("direction") int direction) {
        
        service.sort(id, targetId, direction);
        printSuccessMessage();
    }
    
    /**
     * 复制组件
     */
    @RequestMapping(value = "/copy/{id}", method = RequestMethod.POST)
    public void copy(Long id) {    
        Component component = service.getComponent(id);
        String desDir = URLUtil.getWebFileUrl(component.getResourceBaseDir()).getPath(); 
        Component copy =  service.copyComponent(id, new File(desDir));
        doAfterSave(true, copy, "SourceTree");
    }
    
    /**
     * 设置修饰器为默认修饰器
     */
    @RequestMapping(value = "/decorator/default/{id}", method = RequestMethod.POST)
    public void setDecoratorAsDefault(Long id) {      
        service.setDecoratorAsDefault(id);
        printSuccessMessage();
    }
    
    /**
     * 设置布局器为默认布局器
     */      
    @RequestMapping(value = "/layout/default/{id}", method = RequestMethod.POST)
    public void setLayoutAsDefault(Long id) {      
        service.setLayoutAsDefault(id);
        printSuccessMessage();
    }
    
    /**
     * 获取上传组件的页面
     */
    @RequestMapping("/upload")
    public void getUploadTemplate() {
        XFormEncoder encoder = new XFormEncoder(PortalConstants.IMPORT_ELEMENT_XFORM_PATH);
        print("ComponentInfo", encoder);
    }

    /**
     * 导入组件
     */
    public void importComponent(Long groupId, File file) {
        Component group = service.getComponent(groupId);
        String desDir = URLUtil.getWebFileUrl(group.getResourceBaseDir()).getPath(); 
        
        Component component = new Component();
        component.setParentId(groupId);
        component.setType(group.getType());
        ComponentHelper.importComponent(service, file, component, desDir, group.getComponentType() + ".xml");
        
        printImportSuccessMessage();
    }
 
    /**
     * 导出组件
     */
    public void exportComponent(Long id) {   
        Component component = service.getComponent(id);
        String desDir = URLUtil.getWebFileUrl(component.getResourceBaseDir()).getPath(); 
        
        ComponentHelper.exportComponent(desDir, component, component.getComponentType() + ".xml");
    }
 
	public void getComponentGroup(Long id, Long parentId, int type) {
		XFormEncoder encoder;
        if( DEFAULT_NEW_ID.equals(id) ) {  //如果是新增,则返回一个空的无数据的模板
        	Map<String, Object> map = new HashMap<String, Object>();
        	map.put("parentId", parentId);
        	map.put("type", type);
        	map.put("isGroup", true);
            encoder = new XFormEncoder(PortalConstants.GROUP_XFORM_TEMPLET_PATH, map);           
        }
        else {
        	Component group = service.getComponent(id);            
            encoder = new XFormEncoder(PortalConstants.GROUP_XFORM_TEMPLET_PATH, group);
        }        
        print("GroupDetail", encoder);
	}
 
	/**  删除组 */
	public void deleteComponentGroup(Long id){
		service.deleteComponentGroup(id);		
        printSuccessMessage();
	}
    
    /** 获取某个组件类型的所有分组 */
    public void getGroupsByType(int type){
        List<?> data = service.getComponentGroups(type);
        TreeEncoder encoder = new TreeEncoder(data, new StrictLevelTreeParser());
        encoder.setNeedRootNode(false);
        print("SourceTree", encoder);
    }
    
    /**
     * 复制组件到另外一个组
     */
    public void copyTo(Long id, Long targetId){
        Component component = service.copyTo(id, targetId);                
        doAfterSave(true, component, "SourceTree");
    }
    
    /**
     * 移动组件到另外一个组
     */
    public void moveTo(Long id, Long targetId){
        service.moveTo(id, targetId);     
        printSuccessMessage();
    }
    
    /**********************************************      在线编辑组件参数配置    *******************************************/
    
    /**
     * 
     * @param paramsItem  类似 ：bgColor=red 回车 menuId=12
     */
    public void getComponentParamsConfig(Long id, String paramsItem){
        Component component = service.getComponent(id);
       
        String configFilePath = URLUtil.getWebFileUrl(component.getResourcePath() + "/" + Component.PARAM_FILE).getFile();
        if( !new File(configFilePath).exists() ){
            // 如果是第一次配置，且配置参数项不为空，则根据需要的参数项自动生成一个默认模板
            Document doc = DocumentHelper.createDocument();
            Element xformNode = doc.addElement("Response").addElement("ConfigParams").addElement("xform");
            Element declareNode = xformNode.addElement("declare");
            Element layoutNode = xformNode.addElement("layout");
            xformNode.addElement("data");
            
            if( !EasyUtils.isNullOrEmpty(paramsItem) ){
                StringTokenizer stk = new StringTokenizer(paramsItem);
                while(stk.hasMoreTokens()){
                    String itemName = stk.nextToken(); 
                    int index = itemName.indexOf("=");
                    if(index > 0){
                        itemName = itemName.substring(0, index);
                        
                        //<column name="tableWidth" caption="XX" mode="string"/>
                        Element itemcolumn = declareNode.addElement("column");
                        itemcolumn.addAttribute("name", itemName);
                        itemcolumn.addAttribute("caption", itemName);
                        itemcolumn.addAttribute("mode", "string");
                        
                        //<TR>
                        //   <TD width="50"><label binding="tableWidth"/></TD>
                        //   <TD><input binding="tableWidth" type="text"/></TD>
                        //</TR>
                        Element trNode = layoutNode.addElement("TR");
                        Element tdNode1 = trNode.addElement("TD");
                        tdNode1.addAttribute("width", "50");
                        Element labelNode = tdNode1.addElement("label");
                        labelNode.addAttribute("binding", itemName);
                        
                        Element tdNode2 = trNode.addElement("TD");
                        Element inputNode = tdNode2.addElement("input");
                        inputNode.addAttribute("binding", itemName);
                        inputNode.addAttribute("type", "text");
                    }
                }
                File dir = new File(configFilePath).getParentFile();
                if(!dir.exists()) {
                    dir.mkdir();
                }
                FileHelper.writeXMLDoc(doc, configFilePath);
                print(XMLDocUtil.createDocByAbsolutePath(configFilePath).asXML());
            } else {
                print(doc.asXML());
            }
        } else {
            print(XMLDocUtil.createDocByAbsolutePath(configFilePath).asXML());
        }
    }
    
    public void saveElementParamsConfig(Long id, String configXML){
        Component component = service.getComponent(id);
        String configFile = URLUtil.getWebFileUrl(component.getResourcePath() + "/paramsXForm.xml").getFile();
        
        Document doc = XMLDocUtil.dataXml2Doc("<Response>\n<ConfigParams>\n" + configXML + "\n</ConfigParams>\n</Response>");
        FileHelper.writeXMLDoc(doc, configFile);
        
        printSuccessMessage("保存成功");
    }
    
    /********************************************************************************************************************/
    /***************************************************      预览组件    ************************************************/
    /********************************************************************************************************************/

    /**
     * 获取组件的XML结构数据，用于单个组件预览。
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public void  previewComponent(Long id) throws IOException, TemplateException{
        Component component = service.getComponent(id);
        String componentType = component.getComponentType();
        
        Document doc = XMLDocUtil.dataXml2Doc(component.getDefinition());
        
        org.dom4j.Node htmlNode = doc.selectSingleNode("/" + componentType + "/html");
        org.dom4j.Node scriptNode = doc.selectSingleNode("/" + componentType + "/script");
        org.dom4j.Node prototypeStyleNode = doc.selectSingleNode("/" + componentType + "/prototypeStyle");
        org.dom4j.Node styleNode = doc.selectSingleNode("/" + componentType + "/style");
        
        String html = (htmlNode == null? null : htmlNode.getText());
        String script = (scriptNode == null? null : scriptNode.getText());
        String prototypeStyle = (prototypeStyleNode == null? null : prototypeStyleNode.getText());
        String style = (styleNode == null? null : styleNode.getText());
        
        Map<String, String> events = new HashMap<String, String>();
        List<?> eventNodes = doc.selectNodes("/" + componentType + "/events/attach");
        if(eventNodes != null){
            for(Iterator<?> it = eventNodes.iterator(); it.hasNext();){
                Element eventNode = (Element) it.next();
                events.put(eventNode.attributeValue("event"), eventNode.attributeValue("onevent"));
            }   
        }
        
        Map<String, String> parameters = new HashMap<String, String>();
        List<?> paramNodes = doc.selectNodes("/" + componentType + "/parameters/param");
        if(paramNodes != null){
            for(Iterator<?> it = paramNodes.iterator(); it.hasNext();){
                Element paramNode = (Element) it.next();
                parameters.put("#{" + paramNode.attributeValue("name") + "}", paramNode.attributeValue("defaultValue"));
            } 
        }
        parameters.put("${id}", componentType.substring(0, 1) + component.getId());
        parameters.put("${content}", "");
        parameters.put("${basepath}", Environment.getContextPath() + "/" + component.getResourcePath() + "/");

        // 直接预览门户组件
        String data = toHTML(html, script, prototypeStyle, style, events, parameters);
        if(component.isportlet()) {
            printHTML(data); // 如果是预览portlet，则需要执行模板引擎解析
        } else {
            print(data);
        }
    }
    
    private String toHTML(String html, String script, String prototypeStyle, String style, 
            Map<String, String> events, Map<String, String> parameters) {
        
        StringBuffer sb = new StringBuffer("<html>\n<head>\n");               
        sb.append("<style>\n").append(MacrocodeCompiler.run(prototypeStyle, parameters));
        sb.append(MacrocodeCompiler.run(style, parameters)).append("\n</style>\n");        
        
        String commonJSPath = Environment.getContextPath() + "/core/js/";
        sb.append("<script language=\"javascript\" src=\"" + commonJSPath + "common.js\"></script>\n");
        sb.append("<script language=\"javascript\" src=\"" + commonJSPath + "element.js\"></script>\n");
        sb.append("<script language=\"javascript\" src=\"" + commonJSPath + "event.js\"></script>\n");
        sb.append("<script language=\"javascript\" src=\"" + commonJSPath + "xml.js\"></script>\n");
        sb.append("<script language=\"javascript\" src=\"" + commonJSPath + "xmlhttp.js\"></script>\n");
        
        sb.append("<script language=\"javascript\">\n");
        sb.append(MacrocodeCompiler.run(script, parameters));
        sb.append("\n").append(getEvents4HTML(events, parameters)).append("\n</script>\n");
        sb.append("</head>\n<body>\n");
        sb.append(MacrocodeCompiler.run(html, parameters));
        sb.append("\n</body></html>");
        
        return sb.toString();
    }
    
    private StringBuffer getEvents4HTML(Map<String, String> events, Map<String, String> parameters) {
        StringBuffer sb = new StringBuffer();
        StringBuffer onloadEvent = new StringBuffer();
        for( Entry<String, String> entry : events.entrySet() ) {
            String key = entry.getKey();
            String value = entry.getValue();
            value = MacrocodeCompiler.run(value, parameters);
            
            if ("onload".equals(key)) {
                onloadEvent.append("window.onload = function(){\n").append(value + "();\n};\n");
            } 
            else {
                sb.append("window." + key + "=" + value + "\n"); 
            }
        }
        return sb.append(onloadEvent);
    }
}