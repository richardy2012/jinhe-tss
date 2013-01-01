package com.jinhe.tss.portal.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.engine.macrocode.AbstractMacrocodeContainer;
import com.jinhe.tss.portal.engine.macrocode.MacrocodeContainerFactory;
import com.jinhe.tss.portal.engine.model.AbstractElementNode;
import com.jinhe.tss.portal.engine.model.DecoratorConfigable;
import com.jinhe.tss.portal.engine.model.DecoratorNode;
import com.jinhe.tss.portal.engine.model.IPageElement;
import com.jinhe.tss.portal.engine.model.LayoutConfigable;
import com.jinhe.tss.portal.engine.model.LayoutNode;
import com.jinhe.tss.portal.engine.model.Node;
import com.jinhe.tss.portal.engine.model.PageNode;
import com.jinhe.tss.portal.engine.model.PortalNode;
import com.jinhe.tss.portal.engine.model.PortletNode;
import com.jinhe.tss.portal.entity.FlowRate;
import com.jinhe.tss.portal.helper.FlowrateManager;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.MacrocodeCompiler;

/**
 * <p> HTML页面代码生成器 </p>
 * <p>
 * 可以根据Node节点生成相应的HTML页面文件信息
 * </p>
 */
public class HTMLGenerator {
    
    Logger log = Logger.getLogger(HTMLGenerator.class);
    
    public static final String NAVIGATOR_CONTENT_INDEX = "navigatorContentIndex";

    /**
     * 页面关键字
     */
    private List<String> keyword = new NoNullLinkedList<String>();

    /**
     * 页面外挂js文件路径列表
     */
    private List<String> scriptFiles = new NoNullLinkedList<String>();

    /**
     * 页面外挂css文件路径列表
     */
    private List<String> styleFiles = new NoNullLinkedList<String>();

    /**
     * 页面脚本列表：页面下所有显示元素使用到的script脚本
     */
    private List<String> scriptCodes = new NoNullLinkedList<String>();

    /**
     * 页面样式表列表：页面下所有显示元素使用到的样式表脚本
     */
    private List<String> styleCodes = new NoNullLinkedList<String>();

    /**
     * 页面样式表列表：页面下所有显示同原型元素使用到的样式表脚本（同原型公用的样式）。
     * 注意：此处用 Map 存放原型样式脚本，{组件ID, 样式}，这样如果一个组件在某个页面上多次被引用到，
     * 原型样式CSS 输出一次就可以了 （使用Map同组件的原型样式将会相互覆盖，只留一份）。
     */
    private Map<Long, String> prototypeStyleCodes = new NoNullHashMap<Long, String>();

    /**
     * 事件脚本
     */
    private List<String[]> eventCodes = new NoNullLinkedList<String[]>();

    /**
     * 门户结构自定义的初始化脚本： 这些信息输出到html里只为了万一页面出错时检查用
     */
    private List<String> initCodes = new LinkedList<String>();

    /**
     * 预览元素的路径（门户结构树上路径）： Portlet实例，版面，……，版面，页面
     */
    private List<Node> treePath = new ArrayList<Node>();
    

    /**
     * 目标节点：生成后对象在页面上所放的父版面ID
     */
    private Long targetId;

    /**
     * 目标节点布局器的目标区域：生成后放到相应父版面布局器的相应区域中
     */
    private int targetIndex;

    /**
     * 门户加载的外部JS、CSS文件目录访问地址
     */
    private String portalResourseDir;
    
    /**
     * 页面标题
     */
    private String title; 

    /**
     * 页面对应的自定义DOM对象
     */
    private Element dom = new Element();
    
    /**
     * freemarker解析器
     */
    private FreemarkerParser freemarkerParser;
    
    /**
     * HTML页面生成器构造函数：用于生成完整页面HTML代码<br>
     * <br>
     * @param portal
     *            门户结构PortalNode节点 
     * @param id
     *            所要显示门户结构中某节点ID，可能是page、section or portletInstance
     * @param freemarkerParser
     * 			  freemarker解析器
     */
    public HTMLGenerator(PortalNode portal, Long id, FreemarkerParser freemarkerParser) {
        this.title = portal.getName();
        this.portalResourseDir = getPortalResourceDir(portal);
        this.freemarkerParser = freemarkerParser;
        
        PageNode page;
        if (id == null) {
            Set<Node> children = portal.getChildren();
            page = children.size() > 0 ? (PageNode) children.toArray()[0] : null;
        } 
        else {
            Node content = portal.getNodesMap().get(id);
            if(content == null) {
                throw new BusinessException("选中预览节点【ID:" + id +"】在门户缓存中不存在，如果节点为新增节点，请先刷新缓存");
            }
            
            // 往上查找需要显示节点的父亲节点，直到门户根节点，把整个路径加入到treePath中来（不包括根节点）
            while ( !portal.equals(content) ) {
                treePath.add(content);
                content = content.getParent();
            }
            page = treePath.size() > 0 ? (PageNode) treePath.get(treePath.size() - 1) : null; // path最后为PageNode
        }
        
        if (page != null) {
            dom = new Element(page);
            title = portal.getName() + "-" + page.getName();
            keyword.add(portal.getName());
            scriptFiles.addAll(portal.getScriptFiles());
            styleFiles.addAll(portal.getStyleFiles());
            scriptCodes.add(portal.getScriptCode());
            styleCodes.add(portal.getStyleCode());
            
            // 此处加入页面流量统计
            FlowrateManager.getInstanse().output(new FlowRate(page.getId(), Environment.getClientIp()));
        }
    }
    
    /** 获取门户对应外部链接JS、CSS文件访问路径 */
    private String getPortalResourceDir(PortalNode node) {
        String dirName = node.getCode() + "_" + node.getPortalId();
        return Environment.getContextPath() + PortalConstants.PORTAL_MODEL_DIR + "/" + dirName + "/";
    }

    /**
     * HTML页面生成器构造函数：生成部分页面元素HTML代码的XML字符串<br>
     * <br>
     * @param portal   
     *                 操作门户PortalNode对象<br>
     * @param id       
     *                 对应（版面/portlet替换）菜单中的 "版面/Portlet" ID，<br>
     *                 即即将显示的节点Id<br>
     * @param targetId 
     *                 对应（版面/portlet替换）菜单中的目标版面/页面ID，<br>
     *                 即被取代的区域（对应布局器中targetIndex的区域）所在的页面或版面ID
     */
    public HTMLGenerator(PortalNode portal, Long id, Long targetId, FreemarkerParser freemarkerParser) {
        this.targetId = targetId;
        this.freemarkerParser = freemarkerParser;
        
        Node content = portal.getNodesMap().get(id);
        Node target  = portal.getNodesMap().get(targetId);
        
        if( !(content instanceof IPageElement) ) {
            throw new BusinessException("（版面/portlet替换）菜单中指定的 版面/Portlet 有误，必须是版面或Portlet实例。");
        }
        if( !(target instanceof LayoutConfigable) ) {
            throw new BusinessException("（版面/portlet替换）菜单中指定的 目标版面/页面 有误，必须是版面或页面。");
        }
        
        this.dom = new Element((IPageElement)content, false);
        this.targetIndex = getMenuPointContentIndex((LayoutConfigable) target);
    }

    /**
     * <p>
     * 生成页面HTML代码
     * </p>
     * @return String 当前页面的HTML代码
     */
    public String toHTML() {
        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n");
        sb.append("<HTML xmlns:TSS>\n");
        sb.append("<HEAD>\n");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n");
        sb.append("<TITLE>").append(title).append("</TITLE>\n");
        sb.append(formatKeywords());
        sb.append(formatStyleLinks());
        sb.append(formatScriptLinks());
        sb.append("<style>\n").append(formatStyleCodes()).append("</style>\n");
        sb.append("<script language=\"javascript\">\n").append(formatScriptCodes()).append("</script>\n");
        sb.append("</HEAD>\n");
        return sb.append(dom).append("</HTML>").toString();
    }

    public String toXML() {
        StringBuffer sb = new StringBuffer();
        sb.append("<segment targetIndex=\"").append(targetIndex).append("\">");
        sb.append("<html><![CDATA[").append(dom).append("]]></html>");
        sb.append("<script><![CDATA[").append(formatSimpleScirptCodes()).append("]]></script>");
        sb.append("<style>").append(formatStyleCodes()).append("</style>");
        sb.append("<event>").append(formatEvents4XML()).append("</event>");
        return sb.append("</segment>").toString();
    }

    /**
     * 返回门户页面的关键字字符串
     */
    private String formatKeywords() {
        return "<META NAME=\"Keywords\" CONTENT=\"" + EasyUtils.list2Str(keyword) + "\">\n";
    }

    /**
     * 导入返回外挂样式表文件标签
     */
    private StringBuffer formatStyleLinks() {
        StringBuffer sb = new StringBuffer();
        for ( String filePath : styleFiles ) {
            sb.append("<link href=\"" + (portalResourseDir + filePath) + "\" rel=\"stylesheet\" type=\"text/css\">\n");
        }
        return sb;
    }

    /**
     * 导入返回外挂脚本文件标签
     */
    private StringBuffer formatScriptLinks() {
        StringBuffer sb = new StringBuffer();
        // 默认挂载的js
        String commonJSPath = Environment.getContextPath() + "/core/js/";
        sb.append("<script language=\"javascript\" src=\"" + commonJSPath + "common.js\"></script>\n");
        sb.append("<script language=\"javascript\" src=\"" + commonJSPath + "element.js\"></script>\n");
        sb.append("<script language=\"javascript\" src=\"" + commonJSPath + "event.js\"></script>\n");
        sb.append("<script language=\"javascript\" src=\"" + commonJSPath + "xml.js\"></script>\n");
        sb.append("<script language=\"javascript\" src=\"" + commonJSPath + "xmlhttp.js\"></script>\n");
        sb.append("<script language=\"javascript\" src=\"" + Environment.getContextPath() + "/pms/js/diy.js\"></script>\n");
        
        for ( String filePath : scriptFiles ) {
            sb.append("<script language=\"javascript\" src=\"" + (portalResourseDir + filePath) + "\"></script>\n");
        }
        return sb;
    }

    /**
     * 获取所有事件的XML代码
     */
    public StringBuffer formatEvents4XML() {
        StringBuffer sb = new StringBuffer();
        for( String[] codes : eventCodes ) {
            sb.append("<attach event=\"" + codes[0] + "\" onevent=\"" + codes[1] + "\"/>\n");
        }
        return sb;
    }

    /**
     * 所有JS脚本代码
     */
    private StringBuffer formatScriptCodes() {
        // 简单脚本代码：不包含事件部分代码
        StringBuffer sb = new StringBuffer();
        for( String script : scriptCodes ) {
            sb.append(script + "\n");
        }
        sb.append("\n");
        
        StringBuffer onloadEvent = new StringBuffer("window.onload = function() { \n");
        for( String initCode : initCodes ) {
            onloadEvent.append(initCode).append("\n"); // 将每个node生成的js code换行分隔开来
        }
        sb.append("\n");
        
        for( String[] codes : eventCodes ) {
            if ("onload".equals(codes[0])) {
                onloadEvent.append("  " + codes[1] + "();").append("\n");
            } 
            else {
                sb.append("window." + codes[0] + "=" + codes[1]).append("\n"); 
            }
        }
        onloadEvent.append("};").append("\n"); 
        
        return sb.append(onloadEvent);
    }

    /**
     * 获取简单脚本代码：不包含事件部分代码。
     * 用于toXML()方法。
     */
    public StringBuffer formatSimpleScirptCodes() {
        StringBuffer sb = new StringBuffer();
        for( String script : scriptCodes ) {
            sb.append(script).append("\n");
        }
        sb.append("\n");
        
        for( String initCode : initCodes ) {
            sb.append(initCode).append("\n"); // 将每个node生成的js code换行分隔开来
        }
        return sb;
    }

    /**
     * <p>
     * 所有样式表代码
     * </p>
     * @return StringBuffer
     */
    public StringBuffer formatStyleCodes() {
        StringBuffer sb = new StringBuffer();       
        for (  String prototypeStyle : prototypeStyleCodes.values() ) {
            sb.append(prototypeStyle).append("\n");
        }
        sb.append("\n");
        
        for ( String style : styleCodes ) {
            sb.append(style).append("\n");
        }
        return sb;
    }

    /**
     * <p>
     * 【菜单所指内容】显示区域的索引号。<br>
     * 获取所在版面、页面默认菜单所指内容显示区域在相应布局器的序号。
     * </p>
     * @param node
     * @return
     */
    private int getMenuPointContentIndex(LayoutConfigable node) {
        LayoutNode layoutNode = node.getLayoutNode();
        int absPortNumber = Math.abs( layoutNode.getPortNumber() );
        
        // 【菜单所指内容】显示区域默认为布局器的【最后一个区域】
        int menuPointContentIndex = absPortNumber - 1; 
        
        // 取布局器参数里设置好了的菜单区域
        String index = layoutNode.getParameters().get(NAVIGATOR_CONTENT_INDEX); 
        if (index != null) { // 如果设置了默认替换区域的参数
            try { 
                int definedIndex = Integer.parseInt(index);
                if(definedIndex < absPortNumber) {
                	menuPointContentIndex = definedIndex;
                }
            } catch (NumberFormatException e) { 
                // 出错则取布局器的最后一个区域
            }
        }
        return menuPointContentIndex;
    }

    /**
     * <p>
     * 页面生成时使用的自定义Element对象，用于生成页面HTML代码
     * </p>
     */
    public class Element {

        /**
         * 节点HTML片段对象
         */
        private List<String> htmlFragments = new ArrayList<String>();

        /**
         * 空对象，用于空门户的展示
         */
        public Element() {
            this.htmlFragments.add("<body></body>");
        }

        /**
         * 生成局部页面为XML时，为生成某个版面或Portlet实例HTML而实例化DOM对象。
         *
         * @param node
         *             版面或Portlet实例
         * @param isPreviewPage
         *             true:  预览页面， 根据页面、版面下的子节点（IPageElement）创建Element对象 
         *             false: 生成(局部页面:IPageElement)XML
         */
        public Element(IPageElement node, boolean isPreviewPage) { 
        	Long id = node.getId();
            String elementType = node.getPageElementType();
           
            if(isPreviewPage){
                HTMLGenerator.this.keyword.add(node.getName());
                
                // 创建页面元素（包括版面和portletInstance）上下文关系的初始化代码， 定义门户结构关系
                HTMLGenerator.this.initCodes.add(appentElementType(id, elementType)); 
                HTMLGenerator.this.initCodes.add(appentElementParent(id, node.getParent().getId())); 
            }
            else {
                // 创建页面上版面/Portlet实例对象上下文关系的修正代码
                HTMLGenerator.this.initCodes.add(appentElementType(id, elementType));  
                HTMLGenerator.this.initCodes.add(appentElementParent(id, node.getParent().getId())); 
                
                HTMLGenerator.this.initCodes.add(appentElementIndex(id, targetIndex));
                HTMLGenerator.this.initCodes.add(appentElementCIndex(id, 0));
                HTMLGenerator.this.initCodes.add("document.getElementById('" + targetId + "').subset[" + targetIndex + "] = [document.getElementById('" + id + "')];\n");
            }
            
            createIPageElement(node);
        }

        /**
         * 创建页面元素（包括版面和portletInstance）的HTML代码
         */
        private void createIPageElement(IPageElement node) {
            String pageElementType = node.getPageElementType();
            this.htmlFragments.add("\n<!-- (" + pageElementType + ")" + ((Node)node).getName() + " start-->\n");
            this.htmlFragments.add("<div class=\"" + pageElementType + "\" id=\"" + ((Node)node).getId() + "\">");
            
            DecoratorNode decoratorNode = ( (DecoratorConfigable) node).getDecoratorNode();
			this.htmlFragments.add(new Element(decoratorNode).toHTML());
            this.htmlFragments.add("</div>\n");
            this.htmlFragments.add("\n<!-- (" + pageElementType + ")" + ((Node)node).getName() + " end-->\n");
        }
 
        // 以下这些信息输出到html里为了万一页面出错时检查用
        private String appentElementType( Object id, String type ) { // 定义门户结构的类型
        	return "document.getElementById('" + id + "').type = '" + type + "';\n"; 
        }
        private String appentElementParent( Object id, Long parentId ) { // 定义门户结构父子关系
        	return "document.getElementById('" + id + "').parent = document.getElementById('" + parentId + "');\n";  
        }
        private String appentElementIndex( Object id, int index ) {
        	return "document.getElementById('" + id + "').index = " + index + ";\n";
        }
        private String appentElementCIndex( Object id, int cIndex ) {
        	return "document.getElementById('" + id + "').cIndex = " + cIndex + ";\n";
        }
 
        /**
         * Element对象构造函数，根据PageNode创建Element对象
         */
        public Element(PageNode node) {
            HTMLGenerator.this.keyword.add(node.getName());
            HTMLGenerator.this.scriptFiles.addAll(node.getScriptFiles());
            HTMLGenerator.this.styleFiles.addAll(node.getStyleFiles());
            HTMLGenerator.this.scriptCodes.add(node.getScriptCode());
            HTMLGenerator.this.styleCodes.add(node.getStyleCode());
            
            HTMLGenerator.this.initCodes.add(appentElementType(node.getId(), "Page"));  
            HTMLGenerator.this.initCodes.add("document.getElementById('" + node.getId() + "').portalId = " + node.getPortalId() + ";\n");
            
            this.htmlFragments.add("<body id=\"" + node.getId() + "\">" + new Element(node.getDecoratorNode()) + "</body>");
        }

        /**
         * Element对象构造函数，根据DecoratorNode创建Element对象
         */
        public Element(DecoratorNode node) {
        	Long id = node.getId();
        	Long parentId = node.getParent().getId();
        	
            HTMLGenerator.this.scriptCodes.add(MacrocodeContainerFactory.newInstance(node.getScript(), node).compile());
            HTMLGenerator.this.styleCodes.add (MacrocodeContainerFactory.newInstance(node.getStyle(),  node).compile());
            HTMLGenerator.this.prototypeStyleCodes.put(id, MacrocodeContainerFactory.newInstance(node.getPrototypeStyle(), node).compile());
            
            // 创建页面修饰器上下文关系的初始化代码
            HTMLGenerator.this.initCodes.add(appentElementType("D" + parentId, "Decorator"));  
            HTMLGenerator.this.initCodes.add(appentElementParent("D" + parentId, parentId)); 
            HTMLGenerator.this.initCodes.add("document.getElementById('"  + parentId + "').decorator = document.getElementById('D" + parentId + "');\n");
            HTMLGenerator.this.initCodes.add("document.getElementById('D" + parentId + "').decoratorId = " + id + ";\n");
            
            // 添加事件
            appendEventCodes(node); 
            
            this.htmlFragments.add("\n<!-- (修饰器)" + node.getName() + " start-->\n");
            DecoratorConfigable parent = (DecoratorConfigable) node.getParent();
            Element content = createDecorateContent(parent.getDecoratorContent()); // 被修饰的内容
            this.htmlFragments.add(MacrocodeContainerFactory.newInstance(node.getHtml(), node, content).compile());
            this.htmlFragments.add("\n<!-- (修饰器)" + node.getName() + " end-->\n");
        }
        
        /**
         * 创建修饰器修饰的元素（或叫修饰器的内容： layout或portlet)的Element对象
         */
        private Element createDecorateContent(AbstractElementNode node){
            return (node instanceof LayoutNode) ? new Element((LayoutNode)node) : new Element((PortletNode)node);
        }
 
        /**
         * 设置布局器/修饰器/portlet相关事件脚本。
         * 注意：各个门户结构的事件执行顺序有先后，按门户结构从上往下的顺序执行。
         */
        private void appendEventCodes(AbstractElementNode node) {
            for( Entry<String, String> entry : node.getEvents().entrySet() ){
                AbstractMacrocodeContainer macrocodeContainer = MacrocodeContainerFactory.newInstance(entry.getValue(), node);
				HTMLGenerator.this.eventCodes.add(new String[] { entry.getKey(), macrocodeContainer.compile() } );
            }
        }

        /**
         * Element对象构造函数，根据PortletNode创建Element对象
         */
        public Element(PortletNode node) {
        	Long id = node.getId();
        	Long parentId = node.getParent().getId();
        	
            HTMLGenerator.this.keyword.add(node.getName());
            HTMLGenerator.this.scriptCodes.add(MacrocodeContainerFactory.newInstance(node.getScript(), node).compile());
            HTMLGenerator.this.styleCodes.add (MacrocodeContainerFactory.newInstance(node.getStyle(),  node).compile());
            HTMLGenerator.this.prototypeStyleCodes.put(id, MacrocodeContainerFactory.newInstance(node.getPrototypeStyle(), node).compile());
            
            // 创建页面Portlet上下文关系的初始化代码
            HTMLGenerator.this.initCodes.add(appentElementType("P" + parentId, "Portlet"));  
            HTMLGenerator.this.initCodes.add(appentElementParent("P" + parentId, parentId)); 
            HTMLGenerator.this.initCodes.add("document.getElementById('"  + parentId + "').portlet = document.getElementById('P" + parentId + "');\n");
            HTMLGenerator.this.initCodes.add("document.getElementById('P" + parentId + "').portletId = " + id + ";\n");
            
            // 添加事件
            appendEventCodes(node); 
            
            this.htmlFragments.add("\n<!-- (Portlet)" + node.getName() + " start-->\n");
            node.setFreemarkerParser(freemarkerParser);
            this.htmlFragments.add(MacrocodeContainerFactory.newInstance(node.getHtml(), node).compile());
            this.htmlFragments.add("\n<!-- (Portlet)" + node.getName() + " end-->\n");
        }

        /**
         * Element对象构造函数，根据LayoutNode创建Element对象
         */
        public Element(LayoutNode node) {
            Node parent = node.getParent();
        	Long id = node.getId();
        	Long parentId = parent.getId();

            HTMLGenerator.this.scriptCodes.add(MacrocodeContainerFactory.newInstance(node.getScript(), node).compile());
            HTMLGenerator.this.styleCodes.add (MacrocodeContainerFactory.newInstance(node.getStyle(),  node).compile());
            HTMLGenerator.this.prototypeStyleCodes.put(id, MacrocodeContainerFactory.newInstance(node.getPrototypeStyle(), node).compile());
            
            // 创建页面布局器上下文关系的初始化代码
            HTMLGenerator.this.initCodes.add(appentElementType("L" + parentId, "Layout"));  
            HTMLGenerator.this.initCodes.add(appentElementParent("L" + parentId, parentId)); 
            HTMLGenerator.this.initCodes.add("document.getElementById('"  + parentId + "').layout = document.getElementById('L" + parentId + "');\n");
            HTMLGenerator.this.initCodes.add("document.getElementById('L" + parentId + "').layoutId = " + id + ";\n");
            
            appendEventCodes(node); // 事件           
                       
            // 创建所有子节点对应的Element对象集合
            int menuPointContentIndex = getMenuPointContentIndex((LayoutConfigable) parent);
            int portNumber = node.getPortNumber();
            List<List<String>> childIds = new ArrayList<List<String>>();  // 二维List，数据如  ： [[child1, child2], [child3], [child4]]
            Map<String, Element> portMappingElement = createChildElements(parent, portNumber, menuPointContentIndex, childIds);
           
            // 补上子节点所在版面位置索引号及子索引号定义脚本
            for (int i = 0; i < childIds.size(); i++) {
                List<?> items = childIds.get(i);
                for (int j = 0; j < items.size(); j++) {
                    HTMLGenerator.this.initCodes.add(appentElementIndex(items.get(j), i));
                    HTMLGenerator.this.initCodes.add(appentElementCIndex(items.get(j), j));
                }
            }
            
            // 补上布局器所在版面（或页面）与子节点的父子关系定义脚本
            HTMLGenerator.this.initCodes.add(getChildrenRelations(childIds, parent.getId())); 
            
            this.htmlFragments.add("\n<!-- (布局器)" + node.getName() + " start-->\n");
            this.htmlFragments.add(MacrocodeContainerFactory.newInstance(node.getHtml(), node, portMappingElement).compile());
            this.htmlFragments.add("\n<!-- (布局器)" + node.getName() + " end-->\n");
        }

        /**
         * <p>
         * 获取布局器所在版面（或页面）与子节点的父子关系定义脚本
         * <pre>
         * document.getElementById('P1').subset = [
         *        [document.getElementById('child1'), document.getElementById('child2')],
         *        [document.getElementById('child3')],
         *        [document.getElementById('child4')]
         *     ]
         * </pre>
         * </p>
         * @param childIds
         * @param parentId
         * @return StringBuffer
         */
        private String getChildrenRelations(List<List<String>> childIds, Long parentId) {
            StringBuffer sb = new StringBuffer();
            sb.append("document.getElementById('" + parentId + "').subset = [");
            for (int i = 0; i < childIds.size(); i++) {
                if (i > 0) sb.append(", \n");
                
                List<?> items = childIds.get(i);
                sb.append("[");
                for (int j = 0; j < items.size(); j++) {
                    if (j > 0) sb.append(", \n");
                    sb.append("document.getElementById('" + items.get(j) + "')");
                }
                sb.append("]");
            }
            return sb.append("];\n").toString();
        }

        /**
         * <p>
         * 根据页面、版面下布局器，创建子Element对象集合。
         * 将布局器所在的门户结构节点下的所有子节点，逐个填入到布局器的格子中。
         * 如果布局器为循环类型布局器，则把所有子节点填入；否则只把布局器填满为止，多余的子节点将不被展示。
         * </p>
         * @param parent
         * 				布局器所在的门户结构节点
         * @param portNumber
         * @param menuPointContentIndex
         * @param childIds
         * @return Map
         */
		private Map<String, Element> createChildElements(Node parent, 
		        int portNumber, int menuPointContentIndex, List<List<String>> childIds) {
            /* 
             * 判断【选中浏览】的门户节点是否为当前parent的子节点。
             * 如果是parent在treePath存在且非第一个节点，则说明其前面还有它的子节点存在 
             * treePath结构为： portletInstance(0...n)-->section(*)-->page 
             */
		    int parentIndex = treePath.indexOf(parent);
            boolean oneChildSelected = parentIndex > 0; 
		    
            int index = -1;
            boolean flag = false; // 用来判断是否需要设置【菜单所指内容】到菜单格子
            Map<String, Element> portMappingElement = new HashMap<String, Element>();
            for ( Node child : parent.getChildren() ) {
                index++;
                if (index >= Math.abs(portNumber)) {
                    if (portNumber < 0) {
                        index = 0; // 如果布局器为循环布局器，则重新开始
                    } else {
                        break;     // 如果布局器已经满了则跳出，剩下的子节点将不会被生成放入进来
                    }
                }
                
                // 判断当前index是否为parent节点布局器中的【菜单所指内容】区域，且【选中浏览】的节点为parent的子节点
                if(index == menuPointContentIndex && oneChildSelected) {
                    flag = true;
                    continue; // 先返回，该区域留待后面单独补上
                }
                
                Element childElement = new Element((IPageElement) child, true);
                String portMacro = MacrocodeCompiler.createMacroCode("port" + index); // ${portX}
                
                // 如果portElement已经存在，则说明该index已经put过到childrenMap里。同一index再次被循环到，所以一定是循环类布局器
                if (portMappingElement.get(portMacro) != null) {  
                	// 新增HTML片段对象，直接加入到上一格portElement.htmlFragments中，即两个子节点放同一Element里在同一port里显示。
                	Element portElement = portMappingElement.get(portMacro);
                    portElement.htmlFragments.add(childElement.toHTML());  
                    childIds.get(index).add(child.getId().toString());
                } 
                // 将新生产的Element对象放入childrenMap中
                else {
                    portMappingElement.put(portMacro, childElement);
                    childIds.add( new ArrayList<String>(Arrays.asList(child.getId().toString())) );
                }
            }
            
            // 将菜单所指的内容【版面或Portlet实例】设置到父版面或页面中指定放【菜单所指内容】的布局器窗口。
            if ( flag ) {
                //【parentIndex - 1】即被【选中浏览】的节点在treePath里的序号，排在其parent前面。
                Node content = treePath.get(parentIndex - 1);
                String portMacro = MacrocodeCompiler.createMacroCode("port" + menuPointContentIndex); 
                portMappingElement.put(portMacro, new Element((IPageElement) content, true));
                childIds.set(menuPointContentIndex, Arrays.asList(content.getId().toString()));
            }
            
            // 如果布局器的单元格中还有空的，则加一些【空Element】将其填满
            int filledNum = portMappingElement.size(); // 已经填充的单元格个数
            for ( int i = Math.abs(portNumber) - 1; i >= filledNum; i-- ) {
                final String id = "E" + parent.getId() + "_" + i;
				portMappingElement.put(MacrocodeCompiler.createMacroCode("port" + i), new Element() {
                	public String toHTML() {
                        return "<div id=\"" + id + "\" style=\"display:none\"></div>";
                    }
                }); // 放入空的element
				childIds.add( Arrays.asList(id) );
            }
            
            return portMappingElement;
        }

        /**
         * 将Element对象转换成HTML代码
         */
        public String toHTML() {
            StringBuffer sb = new StringBuffer();
            for ( String htmlFragment : this.htmlFragments ) {
                sb.append(htmlFragment);
            }
            return sb.toString();
        }
        
        public String toString() {
        	return toHTML();
        }
    }

    
    /**
     * 非空值HashMap对象，所有元素值不可能为空
     */
    private class NoNullHashMap<K, V> extends HashMap<K, V> {
        private static final long serialVersionUID = -4073320799481823860L;

        /* 覆写HashMap的put方法，如果值为空，则不放入map中  */
        public V put(K key, V value) {
            return EasyUtils.isNullOrEmpty(value) ? null : super.put(key, value);
        }
    }

    /**
     * 非空值LinkedList对象，所有元素不可能为空
     */
    private class NoNullLinkedList<T> extends LinkedList<T> {
        private static final long serialVersionUID = 1851415859447342905L;

        /*
         * 覆写LinkedList的add方法
         * @see java.util.LinkedList#add(java.lang.Object)
         */
        public boolean add(T obj) {
            return EasyUtils.isNullOrEmpty(obj) ? false : super.add(obj);
        }
        
        /*
         * 覆写LinkedList的addAll方法
         * @see java.util.LinkedList#addAll(java.util.Collection)
         */
        public boolean addAll(Collection<? extends T> c) {
            if(c == null) return false;
            
            for(Iterator<? extends T> it = c.iterator(); it.hasNext();){
                if(it.next() == null) it.remove();
            }
            return super.addAll(c);
        }
    }
}