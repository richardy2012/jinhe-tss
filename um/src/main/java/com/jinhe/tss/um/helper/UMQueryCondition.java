package com.jinhe.tss.um.helper;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jinhe.tss.framework.persistence.pagequery.MacrocodeQueryCondition;

/**
 * 用户查询条件对象
 */
public class UMQueryCondition extends MacrocodeQueryCondition{
	
	private Long userId; //用户id
	private Long groupId; //用户组Id
	private Collection<Long> groupIds; //用户组Id
	
	private String applicationId;
	private String resourceTypeId;
	
	private String loginName;  //用户名
	private String userName;   //姓名
	private String employeeNo; //员工编号
	private String groupName;  //组名
	
	private String sex; //性别
	private Date   birthday;//出生年月
	private String certificateNumber; //证件号
	private Integer groupType; //组的类型
	
	private Integer type;    // 查询类型，按什么来查
	private String keyword;  // 关键字
	
	private Long operatorId;	// 登录人
	private String operationId; // 操作选项
	
    public Map<String, Object> getConditionMacrocodes() {
        Map<String, Object> map = new HashMap<String, Object>() ;
        map.put("${loginName}", " and u.loginName like :loginName");
        map.put("${userName}", " and u.userName like :userName");
        map.put("${employeeNo}", " and u.employeeNo like :employeeNo");
        map.put("${sex}", " and u.sex = :sex");
        map.put("${birthday}", " and u.birthday >= :birthday");
        map.put("${certificateNumber}", " and u.certificateNumber like :certificateNumber");
        return map;
    }

	public Date getBirthday() {
		return birthday;
	}
 
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
 
	public String getCertificateNumber() {
        if(certificateNumber != null){
        	certificateNumber = "%" + certificateNumber.trim() + "%";           
        }
		return certificateNumber;
	}
 
	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}
 
	public String getEmployeeNo() {
        if(employeeNo != null){
        	employeeNo = "%" + employeeNo.trim() + "%";           
        }
		return employeeNo;
	}
 
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
 
	public Long getGroupId() {
		return groupId;
	}
 
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
 
	public Integer getGroupType() {
		return groupType;
	}
 
	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}
 
	public String getLoginName() {
        if(loginName != null){
        	loginName = "%" + loginName.trim() + "%";           
        }
		return loginName;
	}
 
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
 
	public String getSex() {
		return sex;
	}
 
	public void setSex(String sex) {
		this.sex = sex;
	}
 
	public String getUserName() {
        if(userName != null){
        	userName = "%" + userName.trim() + "%";           
        }
		return userName;
	}
 
	public void setUserName(String userName) {
		this.userName = userName;
	}
    
	public Long getUserId() {
		return userId;
	}
 
	public void setUserId(Long userId) {
		this.userId = userId;
	}
 
	public Collection<Long> getGroupIds() {
		return groupIds;
	}
 
	public void setGroupIds(Collection<Long> groupIds) {
		this.groupIds = groupIds;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public String getResourceTypeId() {
		return resourceTypeId;
	}

	public String getGroupName() {
		return groupName;
	}

	public Integer getType() {
		return type;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public void setResourceTypeId(String resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
}
