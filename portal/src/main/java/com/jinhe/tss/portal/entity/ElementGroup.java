package com.jinhe.tss.portal.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.persistence.IEntity;
import com.jinhe.tss.framework.persistence.entityaop.IDecodable;
import com.jinhe.tss.framework.persistence.entityaop.OperateInfo;
import com.jinhe.tss.framework.web.dispaly.tree.ILevelTreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.TreeAttributesMap;
import com.jinhe.tss.framework.web.dispaly.xform.IXForm;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.util.BeanUtil;

/**
 * 组对象实体：包括布局器组、修饰器组、Portlet组等
 */
@Entity
@Table(name = "pms_element_group", uniqueConstraints = { 
        @UniqueConstraint(name="MULTI_NAME_ElementGroup", columnNames = { "parentId", "type", "name" })
})
@SequenceGenerator(name = "elementGroup_sequence", sequenceName = "elementGroup_sequence", initialValue = 1, allocationSize = 1)
public class ElementGroup extends OperateInfo implements IEntity, ILevelTreeNode, IXForm, IDecodable {

	public final static int LAYOUT_TYPE    = 1;
    public final static int DECORATOR_TYPE = 2;
    public final static int PORTLET_TYPE   = 3;
    
    public final static String LAYOUT    = "Layout";
    public final static String DECORATOR = "Decorator";
    public final static String PORTLET   = "Portlet";
    
    public final static Class<Layout>    LAYOUT_CLASS    = Layout.class;
    public final static Class<Decorator> DECORATOR_CLASS = Decorator.class;
    public final static Class<Portlet>   PORTLET_CLASS   = Portlet.class;

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "elementGroup_sequence")
	private Long    id;
    
	private Long    parentId;    // 父组的编码：根节点的parentId=0;
	private String  name;        // 组名称
	private Integer type;        // 组类别：区分不同的组 1-布局器组, 2-修饰器组 3-Portlet组
	private String  description; // 组的描述
	
    private Integer seqNo;     // 顺序号 
    private String  decode;    // 层码
    private Integer levelNo;   // 层次值
    
    public Class<ElementGroup> getParentClass() { 
        return ElementGroup.class; 
    }

    public TreeAttributesMap getAttributes() {
        TreeAttributesMap map = new TreeAttributesMap(id, name);
        map.put("type", type);
        map.put("parentId", parentId);
        map.put("seqNo", seqNo);    
        
        switch(this.type){
        case 1:
            map.put("icon","../platform/images/icon/layout_group.gif");
            break;
        case 2:
            map.put("icon","../platform/images/icon/decorator_group.gif");
            break;
        case 3:
            map.put("icon","../platform/images/icon/portlet_group.gif");
            break;
        }
        
        super.putOperateInfo2Map(map);
        return map;
    }

    public Map<String, Object> getAttributesForXForm() {
        Map<String, Object> map = new HashMap<String, Object>();
        BeanUtil.addBeanProperties2Map(this, map);

        return map;
    }
    
    public Class<?> getClassByType(){
        return getClassByType(type);
    }
    public String getClassNameByType(){    
        return getClassNameByType(this.type);
    }
    public String getBasePathByType(){
        return getBasePathByType(this.type);    
    }
    
    public static Class<?> getClassByType(Integer type){
        switch(type){
        case ElementGroup.LAYOUT_TYPE:     //布局器
            return Layout.class;
        case ElementGroup.DECORATOR_TYPE:  //修饰器
            return Decorator.class;
        case ElementGroup.PORTLET_TYPE:    // Portlet
            return Portlet.class;
        default:
            throw new BusinessException("组的类型不正确！");
        }        
    }
    public static String getClassNameByType(Integer type){
        switch(type){
        case ElementGroup.LAYOUT_TYPE: 
            return "Layout";
        case ElementGroup.DECORATOR_TYPE: 
            return "Decorator";
        case ElementGroup.PORTLET_TYPE: 
            return "Portlet";
        default:
            throw new BusinessException("组的类型不正确！");
        }        
    }
    public static String getBasePathByType(Integer type){
        switch(type){
        case ElementGroup.LAYOUT_TYPE: 
            return PortalConstants.LAYOUT_MODEL_DIR;
        case ElementGroup.DECORATOR_TYPE: 
            return PortalConstants.DECORATOR_MODEL_DIR;
        case ElementGroup.PORTLET_TYPE: 
            return PortalConstants.PORTLET_MODEL_DIR;
        default:
            throw new BusinessException("组的类型不正确！");
        }        
    }
    
    public boolean isPortletsGroup(){
        return ElementGroup.PORTLET_TYPE == type.intValue();
    }
 
    public String getLevelNoName() {
        return "levelNo";
    }

    public String getDecodeName() {
        return "decode";
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
 
	public Long getParentId() {
		return parentId;
	}
 
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
 
	public Integer getType() {
		return type;
	}
 
	public void setType(Integer type) {
		this.type = type;
	}
 
    public Integer getSeqNo() {
        return seqNo;
    }
 
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
 
    public String getDecode() {
        return decode;
    }
 
    public Integer getLevelNo() {
        return levelNo;
    }
 
    public void setDecode(String decode) {
        this.decode = decode;
    }
 
    public void setLevelNo(Integer levelNo) {
        this.levelNo = levelNo;
    }
 
    public String toString(){
        return "(id:" + this.id + ", name:" + this.name + ")"; 
    }
}
