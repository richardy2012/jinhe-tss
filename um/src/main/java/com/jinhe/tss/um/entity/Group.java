package com.jinhe.tss.um.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.jinhe.tss.framework.persistence.entityaop.IDecodable;
import com.jinhe.tss.framework.persistence.entityaop.OperateInfo;
import com.jinhe.tss.framework.web.dispaly.tree.ILevelTreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.TreeAttributesMap;
import com.jinhe.tss.framework.web.dispaly.xform.IXForm;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.permission.IResource;
import com.jinhe.tss.util.BeanUtil;

/**
 * 用户组域对象
 */
@Entity
@Table(name = "um_group", uniqueConstraints = { 
        @UniqueConstraint(name = "MULTI_NAME_GROUP", columnNames = { "parentId", "name" })
})
@SequenceGenerator(name = "group_sequence", sequenceName = "group_sequence", initialValue = 1000, allocationSize = 10)
public class Group extends OperateInfo implements ILevelTreeNode, IDecodable, IXForm, IResource {

	public static final Integer MAIN_GROUP_TYPE                     = 1; // 主组类型
	public static final Integer ASSISTANT_GROUP_TYPE                = 2; // 辅助组类型
	public static final Integer OTHER_GROUP_TYPE                    = 3; // 其他应用组类型
	public static final Integer SELF_REGISTER_GROUP_TYPE            = 4; // 自注册用户组类型
	public static final Integer SELF_REGISTER_GROUP_AUTHEN_TYPE     = 5; // 自注册用户组已认证类型
	public static final Integer SELF_REGISTER_GROUP_NOT_AUTHEN_TYPE = 6; // 自注册用户组未认证类型
  
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "group_sequence")
	private Long    id;       // 用户组ID , 用户组主键
	private Long    parentId; // 父节点ID  
	
	@Column(nullable = false)  
	private String  name;          // 组名:用户组名称
	private String  description;   // 描述:用户组信息描述
	
	@Column(nullable = false)  
	private String  applicationId; // 应用系统Code
	private Integer groupType;     // 用户组类型(1-主用户组类型,2-辅助组类型,3-其他应用组类型)
	
	// 树信息begin
	private String  decode;   // 层码
	private Integer levelNo;  // 层次值
	private Integer seqNo;    // 序号,用户组编号

	private Integer disabled = UMConstants.FALSE; // 停用/启用标记

	// 和其他用户管理系统的同步时的对应信息
	private String  dbGroupId;   // 外部应用用户组id:要同步的系统中对应的节点的编号，针对db数据源、ldap数据源的同步中使用
 
	public Long getId() {
		return id;
	}
 
	public void setId(Long id) {
		this.id = id;
	}
 
	public String getApplicationId() {
		return applicationId;
	}
 
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
 
	public String getDbGroupId() {
		return dbGroupId;
	}
 
	public void setDbGroupId(String dbGroupId) {
		this.dbGroupId = dbGroupId;
	}
 
	public String getDescription() {
		return description;
	}
 
	public void setDescription(String description) {
		this.description = description;
	}
 
	public String getName() {
		return name;
	}
 
	public void setName(String groupName) {
		this.name = groupName;
	}
 
	public Integer getSeqNo() {
		return seqNo;
	}
 
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
 
	public Integer getDisabled() {
		return disabled;
	}
 
	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}
 
	public Integer getGroupType() {
		return groupType;
	}
 
	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
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
 
	public Integer getLevelNo() {
		return levelNo;
	}
 
	public void setLevelNo(Integer levelNo) {
		this.levelNo = levelNo;
	}
 
	public Class<?> getParentClass() {
		return getClass();
	}

	public String getResourceType() {
		return UMConstants.GROUP_RESOURCE_TYPE_ID;
	}
   
	public TreeAttributesMap getAttributes() {
		TreeAttributesMap map = new TreeAttributesMap(id, name);
		map.put("parentId", parentId);
		map.put("disabled", disabled);
		map.put("dbGroupId", dbGroupId);
		map.put("groupType", groupType);
		map.put("applicationId", applicationId);
		map.put("resourceTypeId", getResourceType());
		
		// 特殊组不显示图标 (特殊组指系统初始化的主用户、辅助用户组、其他用户组等根节点，它们的ID为负值)
		if (id.longValue() > 0) {
			if (UMConstants.FALSE.equals(disabled)) {
				map.put("icon", UMConstants.START_GROUP_TREENODE_ICON);
			} else {
				map.put("icon", UMConstants.STOP_GROUP_TREENODE_ICON);
			}
		}
		
		super.putOperateInfo2Map(map);
		return map;
	}
 
	public Map<String, Object> getAttributesForXForm() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        BeanUtil.addBeanProperties2Map(this, map);
        
		return map;
	}
 
    public String toString(){
        return "(ID:" + this.id + ", Name:" + this.name  + ", Decode:" + this.decode + ")"; 
    }

}
