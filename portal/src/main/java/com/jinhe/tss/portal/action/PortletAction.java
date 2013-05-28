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
import com.jinhe.tss.portal.entity.ElementGroup;
import com.jinhe.tss.portal.entity.Portlet;
import com.jinhe.tss.portal.helper.ElementHelper;
import com.jinhe.tss.portal.helper.ElementTreeParser;
import com.jinhe.tss.portal.helper.IElement;
import com.jinhe.tss.portal.service.IElementService;
import com.jinhe.tss.util.URLUtil;
import com.jinhe.tss.util.XMLDocUtil;
import com.jinhe.tss.util.XmlUtil;
 
public class PortletAction extends BaseActionSupport {

    private Long    id;   
    private Integer disabled;
    private Long    groupId; // 组编号
    private Long    targetId; // 移动或者排序的目标节点ID
    private int     direction; // 分＋1（向下），和－1（向上）
    private File    file;
    
    private Portlet portlet = new Portlet();

    private IElementService service;

    /**
     * Portlet的树型展示
     */
    public String getAllPortlet4Tree() {
        List<?> data = service.getAllElementsAndGroups(ElementGroup.PORTLET_TYPE);
        TreeEncoder encoder = new TreeEncoder(data, new ElementTreeParser());    
        return print("PortletTree", encoder);
    }
    
    /**
     * Portlet所有启用的树
     */
    public String getAllStartPortlet4Tree() {
        List<?> data = service.getAllStartElementsAndGroups(ElementGroup.PORTLET_TYPE);
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
        return print("PortletTree", encoder);
    }
    
    /**
     * 获取portlet参数,并拼装成一个xml返回
     */
    public String getDefaultParams4Xml() {
        Portlet portlet = (Portlet) service.getElementInfo(ElementGroup.PORTLET_CLASS, id);

        StringBuffer sb = new StringBuffer("<portlet ");
        List<?> parameters = XMLDocUtil.dataXml2Doc(portlet.getDefinition()).selectNodes("//portlet/parameters/param");
        if (parameters != null) {
            for (int i = 0; i < parameters.size(); i++) {
                Element param = (Element) parameters.get(i);
                String name = param.attributeValue("name");
                String defaultValue = param.attributeValue("defaultValue");
                sb.append(name).append("=\"").append(XmlUtil.toFormXml(defaultValue)).append("\" ");
            }
        }
        sb.append(">").append("model/" + Portlet.PORTLET_NAME + "/" + portlet.getCode() + portlet.getId() + "/paramsXForm.xml");                    

        return print("PortletParameters", sb.append("</portlet>").toString());
    }

    /**
     * Portlet的详细信息
     */
    public String getPortletInfo() {
        XFormEncoder encoder;
        if (isCreateNew()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("groupId", groupId);
            encoder = new XFormEncoder(PortalConstants.PORTLET_XFORM_TEMPLET_PATH, map);
        } else {
            Portlet portlet = (Portlet) service.getElementInfo(ElementGroup.PORTLET_CLASS, id);
            encoder = new XFormEncoder(PortalConstants.PORTLET_XFORM_TEMPLET_PATH, (IXForm) portlet);
        }
        return print("PortletInfo", encoder);
    }
    /**
     * 
     * 保存Portlet
     */
    public String save() {
        boolean isNew = portlet.getId() == null ? true : false;
        portlet = (Portlet) service.saveElement(portlet);        
        return doAfterSave(isNew, portlet, "PortletTree");
    }

    /**
     * 删除Portlet.
     */
    public String delete() {
        service.deleteElement(ElementGroup.PORTLET_CLASS, id);
        return printSuccessMessage("删除Portlet成功");
    }

    /**
     * 停用/启用 Portlet（将其下的disabled属性设为"1"/"0"）
     */
    public String disable() {
        service.disableElement(ElementGroup.PORTLET_CLASS, id, disabled);
        return printSuccessMessage();
    }

    /**
     * 同组下的Portlet排序
     */
    public String sort() {
        service.sortElement(id, targetId, direction, ElementGroup.PORTLET_CLASS);
        return printSuccessMessage();
    }

    /**
     * 复制portlet
     */
    public String copy() {
        String desDir = URLUtil.getWebFileUrl(PortalConstants.PORTLET_MODEL_DIR).getPath(); 
        IElement copy = service.copyElement(id, new File(desDir), ElementGroup.PORTLET_CLASS);    
        return doAfterSave(true, copy, "PortletTree");
    }
    
    /**
     * 获取上传Portlet模板
     */
    public String getUploadTemplate() {
        XFormEncoder encoder = new XFormEncoder(PortalConstants.UPLOAD_PORTLET_XFORM_PATH);
        return print("PortletInfo", encoder);
    }

    /**
     * 保存上传Portlet
     */
    public String importPortlet() {
        String desDir = URLUtil.getWebFileUrl(PortalConstants.PORTLET_MODEL_DIR).getPath(); 
        
        Portlet portlet = new Portlet();
        portlet.setGroupId(groupId);
        ElementHelper.importElement(service, file, portlet, desDir, "portlet.xml");

        return print("script", "parent.loadInitData();alert(\"导入成功!!!\");var ws = parent.$(\"ws\");ws.closeActiveTab();");
    }
  
    /**
     * portlet导出
     * @return
     */
    public String getExportPortlet() {
        String desDir = URLUtil.getWebFileUrl(PortalConstants.PORTLET_MODEL_DIR).getPath(); 
        Portlet info = (Portlet) service.getElementInfo(ElementGroup.PORTLET_CLASS, id);
        
        ElementHelper.exportElement(desDir, info, "portlet.xml");
        return XML;
    }
    
    public void setService(IElementService service) {
        this.service = service;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }
    public void setId(Long id) {
        this.id = id;
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
    public Portlet getPortlet() {
        return portlet;
    }
}
