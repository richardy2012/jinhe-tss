package com.jinhe.tss.portal.action;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jinhe.tss.framework.web.dispaly.tree.ITreeTranslator;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.tree.TreeNode;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.entity.Decorator;
import com.jinhe.tss.portal.entity.ElementGroup;
import com.jinhe.tss.portal.helper.ElementHelper;
import com.jinhe.tss.portal.helper.ElementTreeParser;
import com.jinhe.tss.portal.helper.IElement;
import com.jinhe.tss.portal.service.IElementService;
import com.jinhe.tss.util.URLUtil;
import com.jinhe.tss.util.XMLDocUtil;
import com.jinhe.tss.util.XmlUtil;

@Controller
@RequestMapping("decorator")
public class DecoratorAction extends BaseActionSupport {

    @Autowired private IElementService service;

    /**
     * <p>修饰器树型展示.维护修饰器的时候用到</p>
     */
    @RequestMapping("/list")
    public void getAllDecorator4Tree() {
        List<?> data = service.getAllElementsAndGroups(ElementGroup.DECORATOR_TYPE);
        TreeEncoder encoder = new TreeEncoder(data, new ElementTreeParser());        
        print("DecoratorTree", encoder);
    }

    /**
     * 所有启动的修饰器树型展示.（编辑门户结构的时候会用到）
     */
    @RequestMapping("/list/enabled")
    public void getAllStartDecorator4Tree() {
        List<?> data = service.getAllStartElementsAndGroups(ElementGroup.DECORATOR_TYPE);
        
        TreeEncoder encoder = new TreeEncoder(data, new ElementTreeParser());        
        encoder.setNeedRootNode(false);
        
        encoder.setTranslator( new ITreeTranslator() {
            public Map<String, Object> translate(Map<String, Object> attributes) {
                if(attributes.get("type") != null) { // 使得组节点不可选
                    attributes.put(TreeNode.TREENODE_ATTRIBUTE_CANSELECTED, "0"); 
                }
                return attributes;
            }           
        });
        print("DecoratorTree", encoder);
    }

    /**
     * 获取布局器参数,并拼装成一个xml返回
     */
    @RequestMapping("/params")
    public void getDefaultParams4Xml(Long id) {
        Decorator decorator = (Decorator) service.getElementInfo(ElementGroup.DECORATOR_CLASS, id);
        
        StringBuffer sb = new StringBuffer("<decorator ");
        List<?> parameters = XMLDocUtil.dataXml2Doc(decorator.getDefinition()).selectNodes("//decorator/parameters/param");
        if (parameters != null) {
            for (int i = 0; i < parameters.size(); i++) {
                Element param = (Element) parameters.get(i);
                String name = param.attributeValue("name");
                String defaultValue = param.attributeValue("defaultValue");
                sb.append(name).append("=\"").append(XmlUtil.toFormXml(defaultValue)).append("\" ");
            }
        }
        sb.append(">").append("model/" + Decorator.DECORATOR_NAME + "/" + decorator.getCode() + decorator.getId() + "/paramsXForm.xml");                    

        print("DecoratorParameters", sb.append("</decorator>").toString());
    }

    /**
     * 获取修饰器详细信息.
     */
    @RequestMapping("/{groupId}/{id}")
    public void getDecoratorInfo(Long id, Long groupId) {
        XFormEncoder encoder;
        if ( DEFAULT_NEW_ID.equals(id) ) {   // 新增修饰器
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("groupId", groupId);
            encoder = new XFormEncoder(PortalConstants.DECORATOR_XFORM_TEMPLET_PATH, map);
        } 
        else { // 修改修饰器
            Decorator decorator = (Decorator) service.getElementInfo(ElementGroup.DECORATOR_CLASS, id);
            encoder = new XFormEncoder(PortalConstants.DECORATOR_XFORM_TEMPLET_PATH, decorator);
        }
        print("DecoratorInfo", encoder);
    }

    /**
     * 新增修饰器.
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public void save(Decorator decorator) {
        boolean isNew = decorator.getId() == null ? true : false;      
        decorator = (Decorator) service.saveElement(decorator);
        doAfterSave(isNew, decorator, "DecoratorTree");
    }

    /**
     * 删除修饰器
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(Long id) {
        service.deleteElement(ElementGroup.DECORATOR_CLASS, id);
        printSuccessMessage("删除修饰器成功");
    }

    /**
     * 停用/启用 修饰器（将其下的disabled属性设为"1"/"0"）
     */
    @RequestMapping("/disable/{id}/{state}")
    public void disabled(Long id, int state) {
        service.disableElement(ElementGroup.DECORATOR_CLASS, id, state);
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
    	
        service.sortElement(id, targetId, direction, ElementGroup.DECORATOR_CLASS);
        printSuccessMessage();
    }
    
    /**
     * 复制修饰器
     */
    @RequestMapping(value = "/copy/{id}", method = RequestMethod.POST)
    public void copy(Long id) {      
        String desDir = URLUtil.getWebFileUrl(PortalConstants.DECORATOR_MODEL_DIR).getPath(); 
        IElement copy =  service.copyElement(id, new File(desDir), ElementGroup.DECORATOR_CLASS);
        doAfterSave(true, copy, "DecoratorTree");
    }
    
    /**
     * 设置修饰器为默认修饰器
     */
    @RequestMapping(value = "/default/{id}", method = RequestMethod.POST)
    public void setAsDefault(Long id) {      
        service.setDecorator4Default(id);
        printSuccessMessage();
    }
    
    /**
     * 获取上传修饰器的页面
     */
    @RequestMapping("/upload")
    public void getUploadTemplate() {
        XFormEncoder encoder = new XFormEncoder(PortalConstants.IMPORT_DECORATOR_XFORM_PATH);
        print("DecoratorInfo", encoder);
    }

    /**
     * 导入修饰器
     */
    public void importDecorator(Long groupId, File file) {
        String desDir = URLUtil.getWebFileUrl(PortalConstants.DECORATOR_MODEL_DIR).getPath(); 
        
        Decorator decorator = new Decorator();
        decorator.setGroupId(groupId);
        ElementHelper.importElement(service, file, decorator, desDir, "decorator.xml");
        
        print("script", "parent.loadInitData();alert(\"导入成功!!!\");var ws = parent.$(\"ws\");ws.closeActiveTab();");
    }
 
    /**
     * 导出修饰器
     */
    public void getExportDecorator(Long id) {   
        String desDir = URLUtil.getWebFileUrl(PortalConstants.DECORATOR_MODEL_DIR).getPath(); 
        Decorator info = (Decorator) service.getElementInfo(ElementGroup.DECORATOR_CLASS, id);
        
        ElementHelper.exportElement(desDir, info, "decorator.xml");
    }
}
