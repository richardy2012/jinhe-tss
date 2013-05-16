package com.jinhe.tss.um.entity;

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
import com.jinhe.tss.framework.persistence.entityaop.OperateInfo;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.TreeAttributesMap;
import com.jinhe.tss.framework.web.dispaly.xform.IXForm;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.util.BeanUtil;

/**
 * 资源类型域对象
 * unSuppliedTable 
 * suppliedTable 
 * resourceTable 
 */
@Entity
@Table(name = "um_resourcetype", uniqueConstraints = { 
        @UniqueConstraint(columnNames = { "applicationId", "resourceTypeId" })
})
@SequenceGenerator(name = "resourcetype_sequence", sequenceName = "resourcetype_sequence", initialValue = 1000, allocationSize = 10)
public class ResourceType extends OperateInfo implements IEntity, ITreeNode, IXForm {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "resourcetype_sequence")
	private Long    id; 
	
	@Column(nullable = false)  
	private String  applicationId;  // 应用系统Code 
	
	@Column(nullable = false)  
	private String  resourceTypeId; // 资源类型Key 
	
	@Column(nullable = false)  
	private String  name;           // 资源类型名称  
	private Integer seqNo;          // 资源类型编号 
	private String  description;    // 描述  
    
    private Long   rootId;          // 根节点ID
    
    @Column(nullable = false) 
    private String unSuppliedTable;// 角色资源权限未补全的表的类路径
    
    @Column(nullable = false) 
    private String suppliedTable;  // 角色资源权限补全的表的类路径
    
    @Column(nullable = false) 
    private String resourceTable;  // 资源表的类路径
 
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
 
	public String getDescription() {
		return description;
	}
 
	public void setDescription(String description) {
		this.description = description;
	}
 
	public String getResourceTypeId() {
		return resourceTypeId;
	}
 
	public void setResourceTypeId(String resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}
 
	public String getName() {
		return name;
	}
 
	public void setName(String resourceTypeName) {
		this.name = resourceTypeName;
	}
 
	public Integer getSeqNo() {
		return seqNo;
	}
 
	public void setSeqNo(Integer resourceTypeOrder) {
		this.seqNo = resourceTypeOrder;
	}
	 
	public Long getRootId() {
		return rootId;
	}

	public void setRootId(Long rootId) {
		this.rootId = rootId;
	}
 
	public String getUnSuppliedTable() {
		return unSuppliedTable;
	}
 
	public String getSuppliedTable() {
		return suppliedTable;
	}
 
	public String getResourceTable() {
		return resourceTable;
	}
 
	public void setUnSuppliedTable(String unSuppliedTable) {
        this.unSuppliedTable = unSuppliedTable;
    }

    public void setSuppliedTable(String suppliedTable) {
        this.suppliedTable = suppliedTable;
    }

    public void setResourceTable(String resourceTable) {
        this.resourceTable = resourceTable;
    }
 
	public TreeAttributesMap getAttributes() {
		TreeAttributesMap map = new TreeAttributesMap(id, name);
		map.put("appType", UMConstants.RESOURCETYPE_TREE_NODE);
		map.put("resourceTypeId", resourceTypeId);
		map.put("applicationId", applicationId);
		
		map.put("icon", "../framework/images/icon_r02.gif");
		super.putOperateInfo2Map(map);
		return map;
	}
 
	public Map<String, Object> getAttributesForXForm() {
		Map<String, Object> map = new HashMap<String, Object>();
		BeanUtil.addBeanProperties2Map(this, map);
		return map;
	}
 
	public boolean equals(Object obj) {
	    if(obj instanceof ResourceType) {
	        ResourceType temp = (ResourceType)obj;
            return this.applicationId.equals(temp.getApplicationId()) && this.resourceTypeId.equals(temp.getResourceTypeId());
	    }
	    return false;
	}
	
	public int hashCode() {
	    return (this.applicationId + "_" + this.resourceTypeId).hashCode();
	}

}
