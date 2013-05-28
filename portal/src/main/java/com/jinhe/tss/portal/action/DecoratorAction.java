package com.jinhe.tss.portal.action;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.jinhe.tss.framework.web.dispaly.tree.ITreeTranslator;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.tree.TreeNode;
import com.jinhe.tss.framework.web.dispaly.xform.IXForm;
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

public class DecoratorAction extends BaseActionSupport {

    private Long    decoratorId;
    private Long    groupId;
    private Integer disabled;
    private Long    targetId;
    private int     direction;
    private File    file;
    
    private Decorator decorator = new Decorator();
    
    private IElementService service;

    /**
     * <p>修饰器树型展示.维护修饰器的时候用到</p>
     * @return
     */
    public String getAllDecorator4Tree() {
        List<?> data = service.getAllElementsAndGroups(ElementGroup.DECORATOR_TYPE);
        TreeEncoder encoder = new TreeEncoder(data, new ElementTreeParser());        
        return print("DecoratorTree", encoder);
    }

    /**
     * 所有启动的修饰器树型展示.（编辑门户结构的时候会用到）
     * @return
     */
    public String getAllStartDecorator4Tree() {
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
        return print("DecoratorTree", encoder);
    }

    /**
     * 获取布局器参数,并拼装成一个xml返回
     * @return
     */
    public String getDefaultParams4Xml() {
        Decorator decorator = (Decorator) service.getElementInfo(ElementGroup.DECORATOR_CLASS, decoratorId);
        
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

        return print("DecoratorParameters", sb.append("</decorator>").toString());
    }

    /**
     * 获取修饰器详细信息.
     * @return
     */
    public String getDecoratorInfo() {
        XFormEncoder encoder;
        if (isCreateNew()) {   // 新增修饰器
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("groupId", groupId);
            encoder = new XFormEncoder(PortalConstants.DECORATOR_XFORM_TEMPLET_PATH, map);
        } 
        else { // 修改修饰器
            Decorator decorator = (Decorator) service.getElementInfo(ElementGroup.DECORATOR_CLASS, decoratorId);
            encoder = new XFormEncoder(PortalConstants.DECORATOR_XFORM_TEMPLET_PATH, (IXForm) decorator);
        }
        return print("DecoratorInfo", encoder);
    }

    /**
     * 新增修饰器.
     * @return
     */
    public String save() {
        boolean isNew = decorator.getId() == null ? true : false;      
        decorator = (Decorator) service.saveElement(decorator);
        return doAfterSave(isNew, decorator, "DecoratorTree");
    }

    /**
     * 删除修饰器
     * @return
     */
    public String delete() {
        service.deleteElement(ElementGroup.DECORATOR_CLASS, decoratorId);
        return printSuccessMessage("删除修饰器成功");
    }

    /**
     * 停用/启用 修饰器（将其下的disabled属性设为"1"/"0"）
     * @return
     */
    public String disabled() {
        service.disableElement(ElementGroup.DECORATOR_CLASS, decoratorId, disabled);
        return printSuccessMessage();
    }
    
    /**
     * 修饰器排序
     * @return
     */
    public String sort() {
        service.sortElement(decoratorId, targetId, direction, ElementGroup.DECORATOR_CLASS);
        return printSuccessMessage();
    }
    
    /**
     * 复制修饰器
     * @return
     */
    public String copy() {      
        String desDir = URLUtil.getWebFileUrl(PortalConstants.DECORATOR_MODEL_DIR).getPath(); 
        IElement copy =  service.copyElement(decoratorId, new File(desDir), ElementGroup.DECORATOR_CLASS);
        return doAfterSave(true, copy, "DecoratorTree");
    }
    
    /**
     * 设置修饰器为默认修饰器
     * @return
     */
    public String setAsDefault() {      
        service.setDecorator4Default(decoratorId);
        return printSuccessMessage();
    }
    
    /**
     * 获取上传修饰器的页面
     * @return
     */
    public String getUploadTemplate() {
        XFormEncoder encoder = new XFormEncoder(PortalConstants.IMPORT_DECORATOR_XFORM_PATH);
        return print("DecoratorInfo", encoder);
    }

    /**
     * 导入修饰器
     * @return
     */
    public String importDecorator() {
        String desDir = URLUtil.getWebFileUrl(PortalConstants.DECORATOR_MODEL_DIR).getPath(); 
        
        Decorator decorator = new Decorator();
        decorator.setGroupId(groupId);
        ElementHelper.importElement(service, file, decorator, desDir, "decorator.xml");
        
        return print("script", "parent.loadInitData();alert(\"导入成功!!!\");var ws = parent.$(\"ws\");ws.closeActiveTab();");
    }
 
    /**
     * 导出修饰器
     * @return
     */
    public String getExportDecorator() {   
        String desDir = URLUtil.getWebFileUrl(PortalConstants.DECORATOR_MODEL_DIR).getPath(); 
        Decorator info = (Decorator) service.getElementInfo(ElementGroup.DECORATOR_CLASS, decoratorId);
        
        ElementHelper.exportElement(desDir, info, "decorator.xml");
        return XML;
    }
    
    public void setService(IElementService service) {
        this.service = service;
    }
 
    public void setDecoratorId(Long decoratorId) {
        this.decoratorId = decoratorId;
    }
    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    public void setDirection(int direction) {
        this.direction = direction;
    }
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
    public void setFile(File file) {
        this.file = file;
    }
    public Decorator getDecorator() {
        return decorator;
    }
}
