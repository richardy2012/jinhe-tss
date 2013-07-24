package com.jinhe.tss.portal.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.engine.FreeMarkerSupportAction;
import com.jinhe.tss.portal.entity.ElementGroup;
import com.jinhe.tss.portal.helper.IElement;
import com.jinhe.tss.portal.helper.StrictLevelTreeParser;
import com.jinhe.tss.portal.service.IElementService;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.MacrocodeCompiler;
import com.jinhe.tss.util.URLUtil;
import com.jinhe.tss.util.XMLDocUtil;

import freemarker.template.TemplateException;

public class ElementGroupAction extends FreeMarkerSupportAction {
 
    @Autowired private IElementService service;
	
	/**
	 * Group的详细信息
	 */
	public void getGroupInfo(Long id, Long parentId, int type){
		XFormEncoder encoder;
        if( DEFAULT_NEW_ID.equals(id) ) {  //如果是新增,则返回一个空的无数据的模板
        	Map<String, Object> map = new HashMap<String, Object>();
        	map.put("parentId", parentId);
        	map.put("type", type);
            encoder = new XFormEncoder(PortalConstants.GROUP_XFORM_TEMPLET_PATH, map);           
        }
        else{
        	ElementGroup group = service.getGroupInfo(id);            
            encoder = new XFormEncoder(PortalConstants.GROUP_XFORM_TEMPLET_PATH, group);
        }        
        print("GroupInfo", encoder);
	}
	
	/**
	 * 同类型组的排序
	 */
	public void sortByType(Long id, Long targetId, int direction){
		service.sortByType(id, targetId, direction);        
		printSuccessMessage();
	}
	
	/**
	 * 保存Group
	 */
	public void save(ElementGroup group){        
        boolean isNew = group.getId() == null ? true : false;      
        group = service.saveGroup(group);
        doAfterSave(isNew, group, group.getClassNameByType() + "Tree");
	}
	
	/**
	 * 删除组.
	 */
	public void delete(Long id){
		service.deleteGroupById(id);		
        printSuccessMessage();
	}
    
    /**
     * 获取某个元素类型的所有分组
     */
    public void getGroupsByType(int type){
        List<?> data = service.getGroupsByType(type);
        TreeEncoder encoder = new TreeEncoder(data, new StrictLevelTreeParser());
        encoder.setNeedRootNode(false);
        print("SiteTree", encoder);
    }
    
    /**
     * 复制元素到另外一个组
     */
    public void copyTo(Long id, Long targetId){
        Object[] returnValues = service.copyTo(id, targetId);                
        doAfterSave(true, returnValues[0], (returnValues[1] + "Tree"));
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
    public void getElementParamsConfig(Long id, int type, String paramsItem){
        IElement element = service.getElementInfo(ElementGroup.getClassByType(type), id);
       
        String configFilePath = URLUtil.getWebFileUrl(element.getResourcePath() + "/paramsXForm.xml").getFile();
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
    
    public void saveElementParamsConfig(Long id, int type, String configXML){
        IElement element = service.getElementInfo(ElementGroup.getClassByType(type), id);
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
    public void  previewElement(Long id, int type) throws IOException, TemplateException{
        String elementName = ElementGroup.getClassNameByType(type).toLowerCase();
        IElement element = service.getElementInfo(ElementGroup.getClassByType(type), id);
        
        Document doc = XMLDocUtil.dataXml2Doc(element.getDefinition());
        
        org.dom4j.Node htmlNode = doc.selectSingleNode("/" + elementName + "/html");
        org.dom4j.Node scriptNode = doc.selectSingleNode("/" + elementName + "/script");
        org.dom4j.Node prototypeStyleNode = doc.selectSingleNode("/" + elementName + "/prototypeStyle");
        org.dom4j.Node styleNode = doc.selectSingleNode("/" + elementName + "/style");
        
        String html = (htmlNode == null? null : htmlNode.getText());
        String script = (scriptNode == null? null : scriptNode.getText());
        String prototypeStyle = (prototypeStyleNode == null? null : prototypeStyleNode.getText());
        String style = (styleNode == null? null : styleNode.getText());
        
        Map<String, String> events = new HashMap<String, String>();
        List<?> eventNodes = doc.selectNodes("/" + elementName + "/events/attach");
        if(eventNodes != null){
            for(Iterator<?> it = eventNodes.iterator(); it.hasNext();){
                Element eventNode = (Element) it.next();
                events.put(eventNode.attributeValue("event"), eventNode.attributeValue("onevent"));
            }   
        }
        
        Map<String, String> parameters = new HashMap<String, String>();
        List<?> paramNodes = doc.selectNodes("/" + elementName + "/parameters/param");
        if(paramNodes != null){
            for(Iterator<?> it = paramNodes.iterator(); it.hasNext();){
                Element paramNode = (Element) it.next();
                parameters.put("#{" + paramNode.attributeValue("name") + "}", paramNode.attributeValue("defaultValue"));
            } 
        }
        parameters.put("${id}", elementName.substring(0, 1) + element.getId());
        parameters.put("${content}", "");
        parameters.put("${basepath}", Environment.getContextPath() + "/" + element.getResourcePath() + "/");

        // 直接预览门户组件
        String data = toHTML(html, script, prototypeStyle, style, events, parameters);
        if(ElementGroup.PORTLET_TYPE == type){
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