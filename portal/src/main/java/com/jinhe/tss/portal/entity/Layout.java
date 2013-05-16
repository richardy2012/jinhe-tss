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
 * 布局器实体：包括基本信息和布局定义信息
 */
@Entity
@Table(name = "pms_layout", uniqueConstraints = { 
        @UniqueConstraint(name="MULTI_NAME_Layout", columnNames = { "groupId", "name" })
})
@SequenceGenerator(name = "layout_sequence", sequenceName = "layout_sequence", initialValue = 1000, allocationSize = 50)
public class Layout extends OperateInfo implements IEntity, ILevelTreeNode, IXForm, IElement, IDecodable {
   
    public static final String LAYOUT_NAME = "layout";
    
    public String getResourceBaseDir() { return PortalConstants.LAYOUT_MODEL_DIR; }
    public String getResourcePath() { return getResourceBaseDir() + this.code + this.id; }
    public String getElementName() { return LAYOUT_NAME; }
    
    public static Layout defaultLayout = null;
    
    public static void setDefaultLayout(Layout defaultLayout) {
        Layout.defaultLayout = defaultLayout;
    }
    
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "layout_sequence")
    private Long    id;
	
	@Column(nullable = false)
    private String  name; //布局器名称
	
	@Column(nullable = false)
    private String  code; //布局器代码：用于生成布局器资源文件目录及访问相对路径
	
	@Column(length = 4000, nullable = false)
    private String  definition;  //布局器内容：布局器关于展现方式的具体定义信息
	
	@Column(length = 1000)
    private String  description; //布局器的描述信息
    private String  version;     //版本号
    
    @Column(nullable = false)
    private Long    groupId;     //布局器组编号
    
    @Column(nullable = false)
    private Integer seqNo;    //顺序号：用于排序
    private String  decode;   // 层码
    private Integer levelNo;  // 层次值
    
    private Integer isDefault = PortalConstants.FALSE; //是否为默认布局器
    private Integer disabled  = PortalConstants.FALSE; //是否停用

    /**
     * 布局器中可以显示区域数量
     * <li> n > 0 － 此布局器有多少个区域可以填充子节点，用于判断是否适用版面
     * <li> -n    － 子节点循环填充每个区域；
     * 
     * 注：当portNumber > ${port*}数量时有问题，创建布局器的时候portNumber和html中${port*}数量必须相等。
     */
    @Column(nullable = false)
    private Integer portNumber = new Integer(0);

    public TreeAttributesMap getAttributes() {
        TreeAttributesMap map = new TreeAttributesMap(id, name);
        map.put("code", code);
        map.put("groupId", groupId);
        map.put("disabled", disabled);
        map.put("isDefault", isDefault);
        map.put("icon", "../framework/images/" 
        		+ (PortalConstants.TRUE.equals(isDefault) ? "default_" : "") + "layout" 
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
    
    public Class<ElementGroup> getParentClass() { return ElementGroup.class; }
    
    public String toString(){
        return "(id:" + this.id + ", name:" + this.name + ", groupId:" + this.groupId + ")"; 
    }
 
    public String getVersion() {
        return version;
    }
 
    public void setVersion(String version) {
        this.version = version;
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
 
    public Integer getPortNumber() {
        return portNumber;
    }
 
    public void setPortNumber(Integer portNumber) {
        this.portNumber = portNumber;
    }
 
    public Integer getSeqNo() {
        return seqNo;
    }
 
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
 
    public String getCode() {
        return code;
    }
 
    public void setCode(String code) {
        this.code = code;
    }
 
    public String getDecode() {
        return decode;
    }
 
    public Integer getDisabled() {
        return disabled;
    }
 
    public Integer getIsDefault() {
        return isDefault;
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
 
    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }
 
    public void setLevelNo(Integer levelNo) {
        this.levelNo = levelNo;
    }
}
