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
import com.jinhe.tss.util.BeanUtil;

/**
 * 修饰器实体：修饰器基本信息及内容定义信息
 * TODO 三个元素的实体合并掉，不再实体、Action、页面各弄一套
 * 
 */
@Entity
@Table(name = "portal_element", uniqueConstraints = { 
        @UniqueConstraint(name="MULTI_NAME_ELEMENT", columnNames = { "groupId", "type", "name" })
})
@SequenceGenerator(name = "element_sequence", sequenceName = "element_sequence", initialValue = 1)
public class Element extends OperateInfo implements IEntity, ILevelTreeNode, IXForm, IDecodable {
	
	public final static int LAYOUT_TYPE    = 1;
    public final static int DECORATOR_TYPE = 2;
    public final static int PORTLET_TYPE   = 3;
    
    public final static String LAYOUT    = "layout";
    public final static String DECORATOR = "decorator";
    public final static String PORTLET   = "portlet";
    public final static String[] ELEMENTS   = new String[]{LAYOUT, DECORATOR, PORTLET};
    
    public String getResourceBaseDir() { 
    	return PortalConstants.MODEL_DIR + getElementType() + "/"; 
    }
    
    public String getResourcePath()    { 
    	return getResourceBaseDir() + this.code + this.id; 
    }
    
    public String getTemplatePath()    { 
        return "template/xform/" + getElementType() + ".xml";
    }
    
    public String getElementType()     { 
    	return ELEMENTS[type - 1]; 
    }
    
    public boolean isLayout() {
        return this.type == LAYOUT_TYPE;
    }
    
    public boolean isDecorator() {
        return this.type == DECORATOR_TYPE;
    }
    
    public boolean isportlet() {
        return this.type == PORTLET_TYPE;
    }
    
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "element_sequence")
    private Long    id;
	
	@Column(nullable = false)
    private String  name; // 元素名称
	
	/** 
	 * 元素类别： 1-布局器, 2-修饰器组 3-Portlet组 
	 */
	private Integer type;
	
	@Column(nullable = false)
	private Long    parentId; // 父组的编码：根节点的parentId = 0;
	private boolean isGroup;  // 是否为元素组
	
	@Column(nullable = false)
    private String  code; // 元素代码：用于生成元素资源文件目录及访问相对路径
    
    @Column(length = 4000, nullable = false)
    private String  definition;  // 元素内容：元素关于展现方式的具体定义信息
    
    @Column(length = 1000)
    private String  description; // 元素的描述信息
    private String  version;     // 版本号
    
    @Column(nullable = false)
    private Integer seqNo;    // 顺序号：用于排序
    private String  decode;   // 层码
    private Integer levelNo;  // 层次值
    
    private Integer isDefault = PortalConstants.FALSE; // 是否为默认（修饰器/布局器）
    private Integer disabled  = PortalConstants.FALSE; // 是否停用
    
    /**
     * 布局器中可以显示区域数量
     * <li> n > 0 － 此布局器有多少个区域可以填充子节点，用于判断是否适用版面
     * <li> -n    － 子节点循环填充每个区域；
     * 
     * 注：当portNumber > ${port*}数量时有问题，创建布局器的时候portNumber和html中${port*}数量必须相等。
     */
    @Column(nullable = false)
    private Integer portNumber = 0;
    
    public TreeAttributesMap getAttributes() {
        TreeAttributesMap map = new TreeAttributesMap(id, name);
        map.put("type", type);
        map.put("parentId", parentId);
        map.put("seqNo", seqNo);  
        map.put("isGroup", isGroup);
        
        if(isGroup) {
        	map.put("icon","../framework/images/" + getElementType() + "_group.gif");
        } else {
        	map.put("code", code);
            map.put("groupId", parentId);
            map.put("disabled", disabled);
            map.put("isDefault", isDefault);
            map.put("icon", "../framework/images/" 
            		+ (PortalConstants.TRUE.equals(isDefault) ? "default_" : "") + getElementType() 
            		+ (PortalConstants.TRUE.equals(disabled) ? "_2" : "") + ".gif");
        }
        
        super.putOperateInfo2Map(map);
        return map;
    }

    public Map<String, Object> getAttributesForXForm() {
        Map<String, Object> map = new HashMap<String, Object>();
        BeanUtil.addBeanProperties2Map(this, map);

        return map;
    }

    public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public boolean isGroup() {
		return isGroup;
	}
	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}
	public Class<Element> getParentClass() { return Element.class; }
    
    public String toString(){
        return "(id:" + this.id + ", name:" + this.name + ")"; 
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
    
    public Integer getPortNumber() {
        return portNumber;
    }
 
    public void setPortNumber(Integer portNumber) {
        this.portNumber = portNumber;
    }
}
