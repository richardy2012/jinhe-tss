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
import org.springframework.beans.factory.annotation.Autowired;
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
import com.jinhe.tss.portal.helper.ElementHelper;
import com.jinhe.tss.portal.helper.StrictLevelTreeParser;
import com.jinhe.tss.portal.service.IElementService;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.MacrocodeCompiler;
import com.jinhe.tss.util.URLUtil;
import com.jinhe.tss.util.XMLDocUtil;
import com.jinhe.tss.util.XmlUtil;

import freemarker.template.TemplateException;

public class ElementAction extends FreeMarkerSupportAction {
 
    @Autowired private IElementService service;

    @RequestMapping("/list")
    public void getAllElements4Tree() {
        List<?> data = service.getAllElementsAndGroups();
        TreeEncoder encoder = new TreeEncoder(data, new LevelTreeParser());        
        print("SourceTree", encoder);
    }

    /**
     * 所有启动的修饰器树型展示.（编辑门户结构的时候会用到）
     */
    @RequestMapping("/list/{type}/enabled")
    public void getAllStartElements4Tree(int type) {
        List<?> data = service.getAllStartElementsAndGroups(type);
        
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
     * 获取布局器参数,并拼装成一个xml返回
     */
    @RequestMapping("/params")
    public void getDefaultParams4Xml(Long id) {
        Component element = service.getElementInfo(id);
        String elementType = element.getComponentType();
        
        StringBuffer sb = new StringBuffer("<" + elementType + " ");
        String xpath = "//" + elementType + "/parameters/param";
        List<?> parameters = XMLDocUtil.dataXml2Doc(element.getDefinition()).selectNodes(xpath);
        if (parameters != null) {
            for (int i = 0; i < parameters.size(); i++) {
                org.dom4j.Element param = (org.dom4j.Element) parameters.get(i);
                String name = param.attributeValue("name");
                String defaultValue = param.attributeValue("defaultValue");
                sb.append(name).append("=\"").append(XmlUtil.toFormXml(defaultValue)).append("\" ");
            }
        }
        sb.append(">").append("model/" + elementType + "/" + element.getCode() + element.getId() + "/paramsXForm.xml");                    

        print("ComponentParams", sb.append("</" + elementType + ">").toString());
    }

    /**
     * 获取组件详细信息.
     */
    @RequestMapping("/{groupId}/{id}")
    public void getDecoratorInfo(Long id, Long groupId) {
        XFormEncoder encoder;
        String templatePath = service.getElementInfo(groupId).getTemplatePath();
        if ( DEFAULT_NEW_ID.equals(id) ) {   // 新增组件
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("groupId", groupId);
            encoder = new XFormEncoder(templatePath, map);
        } 
        else { // 修改组件
            encoder = new XFormEncoder(templatePath, service.getElementInfo(id));
        }
        print("DetailInfo", encoder);
    }

    /**
     * 新增元素（组）.
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public void save(Component element) {
        boolean isNew = element.getId() == null ? true : false;      
        element = service.saveElement(element);
        doAfterSave(isNew, element, "ComponentTree");
    }

    /**
     * 删除修饰器
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(Long id) {
        service.deleteElement(id);
        printSuccessMessage("删除成功");
    }

    /**
     * 停用/启用 元素（将其disabled属性设为"1"/"0"）
     */
    @RequestMapping("/disable/{id}/{state}")
    public void disabled(Long id, int state) {
        service.disableElement(id, state);
        printSuccessMessage();
    }
    
    /**
     * 修饰器排序
     */
    @RequestMapping(value = "/sort/{id}/{targetId}/{direction}", method = RequestMethod.POST)
    public void sort(HttpServletResponse response, 
            @PathVariable("id") Long id, 
            @PathVariable("targetId") Long targetId,  
            @PathVariable("direction") int direction) {
        
        service.sortElement(id, targetId, direction);
        printSuccessMessage();
    }
    
    /**
     * 复制修饰器
     */
    @RequestMapping(value = "/copy/{id}", method = RequestMethod.POST)
    public void copy(Long id) {    
        Component element = service.getElementInfo(id);
        String desDir = URLUtil.getWebFileUrl(element.getResourceBaseDir()).getPath(); 
        Component copy =  service.copyElement(id, new File(desDir));
        doAfterSave(true, copy, "DecoratorTree");
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
        print("ElementInfo", encoder);
    }

    /**
     * 导入组件
     */
    public void importDecorator(Long groupId, File file) {
        Component group = service.getElementInfo(groupId);
        String desDir = URLUtil.getWebFileUrl(group.getResourceBaseDir()).getPath(); 
        
        Component decorator = new Component();
        decorator.setParentId(groupId);
        ElementHelper.importElement(service, file, decorator, desDir, "decorator.xml");
        
        print("script", "parent.loadInitData();alert(\"导入成功!!!\");var ws = parent.$(\"ws\");ws.closeActiveTab();");
    }
 
    /**
     * 导出修饰器
     */
    public void getExportDecorator(Long id) {   
        Component info = service.getElementInfo(id);
        String desDir = URLUtil.getWebFileUrl(info.getResourceBaseDir()).getPath(); 
        
        ElementHelper.exportElement(desDir, info, info.getComponentType() + ".xml");
    }
	
	/** Group的详细信息 */
	public void getGroupInfo(Long id, Long parentId, int type) {
		XFormEncoder encoder;
        if( DEFAULT_NEW_ID.equals(id) ) {  //如果是新增,则返回一个空的无数据的模板
        	Map<String, Object> map = new HashMap<String, Object>();
        	map.put("parentId", parentId);
        	map.put("type", type);
            encoder = new XFormEncoder(PortalConstants.GROUP_XFORM_TEMPLET_PATH, map);           
        }
        else{
        	Component group = service.getElementInfo(id);            
            encoder = new XFormEncoder(PortalConstants.GROUP_XFORM_TEMPLET_PATH, group);
        }        
        print("GroupInfo", encoder);
	}
 
	/**  删除组 */
	public void deleteGroup(Long id){
		service.deleteGroupById(id);		
        printSuccessMessage();
	}
    
    /** 获取某个元素类型的所有分组 */
    public void getGroupsByType(int type){
        List<?> data = service.getGroupsByType(type);
        TreeEncoder encoder = new TreeEncoder(data, new StrictLevelTreeParser());
        encoder.setNeedRootNode(false);
        print("SourceTree", encoder);
    }
    
    /**
     * 复制元素到另外一个组
     */
    public void copyTo(Long id, Long targetId){
        Component element = service.copyTo(id, targetId);                
        doAfterSave(true, element, "SourceTree");
    }
    
    /**
     * 移动元素到另外一个组
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
    public void getElementParamsConfig(Long id, String paramsItem){
        Component element = service.getElementInfo(id);
       
        String configFilePath = URLUtil.getWebFileUrl(element.getResourcePath() + "/paramsXForm.xml").getFile();
        if( !new File(configFilePath).exists() ){
            // 如果是第一次配置，且配置参数项不为空，则根据需要的参数项自动生成一个默认模板
            Document doc = DocumentHelper.createDocument();
            org.dom4j.Element xformNode = doc.addElement("Response").addElement("ConfigParams").addElement("xform");
            org.dom4j.Element declareNode = xformNode.addElement("declare");
            org.dom4j.Element layoutNode = xformNode.addElement("layout");
            xformNode.addElement("data");
            
            if( !EasyUtils.isNullOrEmpty(paramsItem) ){
                StringTokenizer stk = new StringTokenizer(paramsItem);
                while(stk.hasMoreTokens()){
                    String itemName = stk.nextToken(); 
                    int index = itemName.indexOf("=");
                    if(index > 0){
                        itemName = itemName.substring(0, index);
                        
                        //<column name="tableWidth" caption="XX" mode="string"/>
                        org.dom4j.Element itemcolumn = declareNode.addElement("column");
                        itemcolumn.addAttribute("name", itemName);
                        itemcolumn.addAttribute("caption", itemName);
                        itemcolumn.addAttribute("mode", "string");
                        
                        //<TR>
                        //   <TD width="50"><label binding="tableWidth"/></TD>
                        //   <TD><input binding="tableWidth" type="text"/></TD>
                        //</TR>
                        org.dom4j.Element trNode = layoutNode.addElement("TR");
                        org.dom4j.Element tdNode1 = trNode.addElement("TD");
                        tdNode1.addAttribute("width", "50");
                        org.dom4j.Element labelNode = tdNode1.addElement("label");
                        labelNode.addAttribute("binding", itemName);
                        
                        org.dom4j.Element tdNode2 = trNode.addElement("TD");
                        org.dom4j.Element inputNode = tdNode2.addElement("input");
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
        Component element = service.getElementInfo(id);
        String configFile = URLUtil.getWebFileUrl(element.getResourcePath() + "/paramsXForm.xml").getFile();
        
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
    public void  previewElement(Long id) throws IOException, TemplateException{
        Component element = service.getElementInfo(id);
        String elementType = element.getComponentType();
        
        Document doc = XMLDocUtil.dataXml2Doc(element.getDefinition());
        
        org.dom4j.Node htmlNode = doc.selectSingleNode("/" + elementType + "/html");
        org.dom4j.Node scriptNode = doc.selectSingleNode("/" + elementType + "/script");
        org.dom4j.Node prototypeStyleNode = doc.selectSingleNode("/" + elementType + "/prototypeStyle");
        org.dom4j.Node styleNode = doc.selectSingleNode("/" + elementType + "/style");
        
        String html = (htmlNode == null? null : htmlNode.getText());
        String script = (scriptNode == null? null : scriptNode.getText());
        String prototypeStyle = (prototypeStyleNode == null? null : prototypeStyleNode.getText());
        String style = (styleNode == null? null : styleNode.getText());
        
        Map<String, String> events = new HashMap<String, String>();
        List<?> eventNodes = doc.selectNodes("/" + elementType + "/events/attach");
        if(eventNodes != null){
            for(Iterator<?> it = eventNodes.iterator(); it.hasNext();){
                org.dom4j.Element eventNode = (org.dom4j.Element) it.next();
                events.put(eventNode.attributeValue("event"), eventNode.attributeValue("onevent"));
            }   
        }
        
        Map<String, String> parameters = new HashMap<String, String>();
        List<?> paramNodes = doc.selectNodes("/" + elementType + "/parameters/param");
        if(paramNodes != null){
            for(Iterator<?> it = paramNodes.iterator(); it.hasNext();){
                org.dom4j.Element paramNode = (org.dom4j.Element) it.next();
                parameters.put("#{" + paramNode.attributeValue("name") + "}", paramNode.attributeValue("defaultValue"));
            } 
        }
        parameters.put("${id}", elementType.substring(0, 1) + element.getId());
        parameters.put("${content}", "");
        parameters.put("${basepath}", Environment.getContextPath() + "/" + element.getResourcePath() + "/");

        // 直接预览门户组件
        String data = toHTML(html, script, prototypeStyle, style, events, parameters);
        if(element.isportlet()){
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