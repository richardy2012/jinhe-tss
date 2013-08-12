package com.jinhe.tss.framework.component.param;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.jinhe.tss.framework.persistence.entityaop.IDecodable;
import com.jinhe.tss.framework.persistence.entityaop.OperateInfo;
import com.jinhe.tss.framework.web.dispaly.tree.ILevelTreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.TreeAttributesMap;
import com.jinhe.tss.framework.web.dispaly.xform.IXForm;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.EasyUtils;

/**
 * <p>
 * 系统参数实体
 * </p>
 */
@Entity
@Table(name = "component_param")
@SequenceGenerator(name = "param_sequence", sequenceName = "param_sequence", initialValue = 1000, allocationSize = 10)
public class Param extends OperateInfo implements ILevelTreeNode, IXForm, IDecodable {
 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "param_sequence")
	private Long   	id;         // 主键
    
    @Column(length = 50)
	private String 	code; 		// 参数名称
    
    @Column(length = 50)  
	private String 	name;		// 展示名称
	
	@Column(length = 2000)  
	private String 	value;		// 参数值
	private String 	text; 		// 参数展示值
	private Integer modality;	// 参数展示方式   0:简单参数 1:下拉型 2:树型
	private Integer type;	    // 种类  0:参数组 1:参数 2:参数项
	private String 	description; 
	
	private Long    parentId;  // 父节点
	private Integer seqNo;    // 排序号
	private String  decode;  // 层码，要求唯一
	private Integer levelNo;// 层次值
	
	private Integer disabled = ParamConstants.FALSE;// 停用/启用标记
	private Integer hidden   = ParamConstants.FALSE;// 是否是系统的
 
	public String getName() {
		return name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
 
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
 
	public Integer getLevelNo() {
		return levelNo;
	}
 
	public void setLevelNo(Integer levelNo) {
		this.levelNo = levelNo;
	}
 
	public String getCode() {
		return code;
	}
 
	public void setCode(String code) {
		this.code = code;
	}
 
	public Integer getSeqNo() {
		return seqNo;
	}
 
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
 
	public String getText() {
		return text;
	}
 
	public void setText(String text) {
		this.text = text;
	}
 
	public String getValue() {
		return value;
	}
 
	public void setValue(String value) {
		this.value = value;
	}
 
	public Long getParentId() {
		return parentId;
	}
 
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
 
	public String getDecode() {
		return decode;
	}
 
	public void setDecode(String decode) {
		this.decode = decode;
	}

	public String getDescription() {
		return description;
	}
 
	public void setDescription(String description) {
		this.description = description;
	}
 
	public Integer getType() {
		return type;
	}
 
	public void setType(Integer type) {
		this.type = type;
	}
 
	public Integer getModality() {
		return modality;
	}
 
	public void setModality(Integer modality) {
		this.modality = modality;
	}

	public Class<?> getParentClass() {
		return Param.class;
	}
 
	public Integer getDisabled() {
		return disabled;
	}
 
	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}
 
	public Integer getHidden() {
		return hidden;
	}
 
	public void setHidden(Integer hidden) {
		this.hidden = hidden;
	}
 
    private boolean isStoped(){
        return ParamConstants.TRUE.equals(this.disabled);
    }
    
    public TreeAttributesMap getAttributes() {
        TreeAttributesMap map;
        String icon_path;
        if (ParamConstants.GROUP_PARAM_TYPE.equals(type)) {
            map = new TreeAttributesMap(id, name);
            icon_path = !isStoped() ? ParamConstants.PARAM_GROUP_START : ParamConstants.PARAM_GROUP_STOP;
        } 
        else if (ParamConstants.NORMAL_PARAM_TYPE.equals(type)) {
            map = new TreeAttributesMap(id, (EasyUtils.isNullOrEmpty(name)) ? code : name);
            if(ParamConstants.SIMPLE_PARAM_MODE.equals(modality)){
                icon_path = !isStoped() ? ParamConstants.PARAM_SIMPLE_START : ParamConstants.PARAM_SIMPLE_STOP;
            } 
            else if (ParamConstants.COMBO_PARAM_MODE.equals(modality)){
                icon_path = !isStoped() ? ParamConstants.PARAM_COMBO_START : ParamConstants.PARAM_COMBO_STOP;
            } 
            else {
                icon_path = !isStoped() ? ParamConstants.PARAM_TREE_START : ParamConstants.PARAM_TREE_STOP;
            }
            map.put("code", code);
            map.put("value", value);
            map.put("text", text);
        } 
        else {
            map = new TreeAttributesMap(id, (EasyUtils.isNullOrEmpty(text)) ? value : text);
            map.put("value", value);
            icon_path = !isStoped() ? ParamConstants.PARAM_ITEM_START : ParamConstants.PARAM_ITEM_STOP;
        }
        
        map.put("icon", icon_path);
        map.put("parentId", parentId);
        map.put("disabled", disabled);
        map.put("decode", decode);
        map.put("levelNo", levelNo);
        map.put("type", type);
        map.put("mode", modality);
 
        super.putOperateInfo2Map(map);
        return map;
    }

    public Map<String, Object> getAttributesForXForm() {
        Map<String, Object> map = new HashMap<String, Object>();
        BeanUtil.addBeanProperties2Map(this, map);
        return map;
    }
}
