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

import com.jinhe.tss.framework.persistence.entityaop.IDecodable;
import com.jinhe.tss.framework.persistence.entityaop.OperateInfo;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.TreeAttributesMap;
import com.jinhe.tss.framework.web.dispaly.xform.IXForm;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.entity.permission.resources.ApplicationResources;
import com.jinhe.tss.um.permission.IResource;
import com.jinhe.tss.util.BeanUtil;

/**
 * 应用系统域对象
 */
@Entity
@Table(name = "um_application", uniqueConstraints = { 
        @UniqueConstraint(name = "MULTI_NAME_APPLICATION ", columnNames = { "name" }),
        @UniqueConstraint(name = "MULTI_ID_APPLICATION", columnNames = { "applicationId" })
})
@SequenceGenerator(name = "application_sequence", sequenceName = "application_sequence", initialValue = 1000, allocationSize = 10)
public class Application extends OperateInfo implements IDecodable, ITreeNode, IXForm, IResource {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "application_sequence")
    private Long    id; 
	private Long    parentId = UMConstants.DEFAULT_ROOT_ID; // 应用系统资源根节点ID，默认为-1
	
	@Column(nullable = false)  
	private String  applicationId;   // 应用系统Code
	
	@Column(nullable = false)  
	private String  name;            // 应用系统名称
	private String  description;     // 应用系统描述
	
	private String  applicationType = UMConstants.PLATFORM_SYSTEM_APP; // 应用系统种类，默认为平台系统
	
	private String  systemUrl;         // 应用系统Url
	private Integer dataSourceType;    // 应用系统用户库接口类型（数据库、LDAP）,0:ldap;1:oracle;2:DB2
	private String  dataSourceUrl;     // 数据接口地址Url
	private String  proxyUserName;     // 代理用户名
	private String  proxyUserPassword; // 代理用户密码
    private String  paramDesc;         // 参数描述xml格式(连接到其他应用系统的参数集合)

    private Integer seqNo;  // 应用系统排序号
    private String  decode; // 层码
    private Integer levelNo;// 层次值

	public Long getId() {
		return id;
	}
 
	public void setId(Long id) {
		this.id = id;
	}
 
	public String getApplicationId() {
		return applicationId;
	}
 
	public void setApplicationId(String applicationCode) {
		this.applicationId = applicationCode;
	}
 
	public String getName() {
		return name;
	}
 
	public void setName(String applicationName) {
		this.name = applicationName;
	}
 
	public Integer getSeqNo() {
		return seqNo;
	}
 
	public void setSeqNo(Integer applicationOrder) {
		this.seqNo = applicationOrder;
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
 
	public String getApplicationType() {
		return applicationType;
	}
 
	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}
 
	public Integer getDataSourceType() {
		return dataSourceType;
	}
 
	public void setDataSourceType(Integer dataSourceType) {
		this.dataSourceType = dataSourceType;
	}
 
	public String getDataSourceUrl() {
		return dataSourceUrl;
	}
 
	public void setDataSourceUrl(String dataSourceUrl) {
		this.dataSourceUrl = dataSourceUrl;
	}
 
	public String getDescription() {
		return description;
	}
 
	public void setDescription(String description) {
		this.description = description;
	}
 
	public String getParamDesc() {
		return paramDesc;
	}
 
	public void setParamDesc(String paramDesc) {
		this.paramDesc = paramDesc;
	}
 
	public String getProxyUserName() {
		return proxyUserName;
	}
 
	public void setProxyUserName(String proxyUserName) {
		this.proxyUserName = proxyUserName;
	}
 
	public String getProxyUserPassword() {
		return proxyUserPassword;
	}
 
	public void setProxyUserPassword(String proxyUserPassword) {
		this.proxyUserPassword = proxyUserPassword;
	}
 
	public String getSystemUrl() {
		return systemUrl;
	}
 
	public void setSystemUrl(String systemUrl) {
		this.systemUrl = systemUrl;
	}
 
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;		
	}

	public Class<?> getParentClass() {
		return ApplicationResources.class;
	}

	public String getResourceType() {
		return UMConstants.APPLICATION_RESOURCE_TYPE_ID;
	}
 
	public TreeAttributesMap getAttributes() {
		TreeAttributesMap map = new TreeAttributesMap(id, name);
		
		map.put("code", applicationId);
		map.put("applicationType", applicationType);
		map.put("appType", UMConstants.APPLICATION_TREE_NODE);
		map.put("resourceTypeId", getResourceType());
		
		map.put("icon", UMConstants.APPLICATION_TREENODE_ICON);
		super.putOperateInfo2Map(map);
		return map;
	}
 
	public Map<String, Object> getAttributesForXForm() {
		Map<String, Object> map = new HashMap<String, Object>();
		BeanUtil.addBeanProperties2Map(this, map);
		return map;
	}
 
	public String toString(){
        return "(id:" + this.id + ", name:" + this.name + ", code:" + this.applicationId + ")"; 
    }
}
