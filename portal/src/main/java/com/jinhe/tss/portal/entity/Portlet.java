package com.jinhe.tss.portal.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.jinhe.tss.framework.persistence.IEntity;
import com.jinhe.tss.framework.persistence.entityaop.IDecodable;
import com.jinhe.tss.framework.persistence.entityaop.OperateInfo;
import com.jinhe.tss.framework.web.dispaly.tree.ILevelTreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.TreeAttributesMap;
import com.jinhe.tss.framework.web.dispaly.xform.IXForm;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.helper.IElement;
import com.jinhe.tss.util.BeanUtil;

/**
 * Portlet实体对象，包含Portlet访问信息等
 */
@Entity
@Table(name = "pms_portlet", uniqueConstraints = { 
        @UniqueConstraint(name="MULTI_NAME_PORTLET", columnNames = { "groupId", "name" })
})
@SequenceGenerator(name = "portlet_sequence", sequenceName = "portlet_sequence", initialValue = 1, allocationSize = 1)
public class Portlet extends OperateInfo implements IEntity, ILevelTreeNode, IXForm, IElement, IDecodable {
    
    public static final String PORTLET_NAME = "portlet";
    
    public String getResourceBaseDir() { return PortalConstants.PORTLET_MODEL_DIR; }
    public String getResourcePath() { return getResourceBaseDir() + this.code + this.id; }
    public String getElementName() { return PORTLET_NAME; }
    
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "portlet_sequence")
    private Long   id;     //编号
	
	@Column(nullable = false)
    private String name;   //Portlet名称
	
	@Column(nullable = false)
    private String code;   //Portlet代码：用于生成Portlet配置文件目录
	
	@Column(length = 4000, nullable = false)
    private String definition;   //Portlet定义信息
	
	@Column(length = 1000)
    private String description; //简介信息
	private String version;     //版本号
	
	@Column(nullable = false)
    private Long   groupId;    //组编号
   
	@Column(nullable = false)
    private Integer seqNo;    // 顺序号
    private String  decode;   // 层码
    private Integer levelNo;  // 层次值
    
    private Integer disabled = PortalConstants.FALSE;  //是否停用

    public Class<ElementGroup> getParentClass() { 
        return ElementGroup.class; 
    }
    
    public TreeAttributesMap getAttributes() {
        TreeAttributesMap map = new TreeAttributesMap(id, name);
        map.put("code", code);
        map.put("groupId", groupId);
        map.put("disabled", disabled);
        map.put("icon", "../platform/images/icon/portlet"
                + (PortalConstants.TRUE.equals(disabled) ? "_2" : "") + ".gif");
        
        super.putOperateInfo2Map(map);
        return map;
    }

    public Map<String, Object> getAttributesForXForm() {
        Map<String, Object> map = new HashMap<String, Object>();
        BeanUtil.addBeanProperties2Map(this, map);

        return map;
    }

    public Long getParentId() { return this.groupId; }
 
    public void setParentId(Long parentId) {
        this.groupId = parentId;
    }
    
    public String toString(){
        return "(id:" + this.id + ", name:" + this.name + ")"; 
    }
 
    public String getDecode() {
        return decode;
    }
 
    public Integer getDisabled() {
        return disabled;
    }
 
    public Integer getLevelNo() {
        return levelNo;
    }
 
    public void setDecode(String decode) {
        this.decode = decode;
    }
 
    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }
 
    public void setLevelNo(Integer levelNo) {
        this.levelNo = levelNo;
    }
 
    public String getVersion() {
        return version;
    }
 
    public void setVersion(String version) {
        this.version = version;
    }
 
    public Long getGroupId() {
        return groupId;
    }
 
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public Integer getSeqNo() {
        return seqNo;
    }
 
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
 
    public String getDefinition() {
        return definition;
    }
 
    public void setDefinition(String definition) {
        this.definition = definition;
    }
 
    public String getDescription() {
        return description;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }
 
    public String getCode() {
        return code;
    }
 
    public void setCode(String code) {
        this.code = code;
    }

    public void setIsDefault(Integer isDefault) {
    }

    public Integer getIsDefault() {
        return PortalConstants.FALSE;
    }
}
