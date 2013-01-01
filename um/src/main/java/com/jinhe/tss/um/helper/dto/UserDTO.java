package com.jinhe.tss.um.helper.dto;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import com.jinhe.tss.um.UMConstants;

public class UserDTO {
	private String  id;           // 用户主键id号
	private Integer seqNo;        // 排序号
	private String  loginName;    // 用户登陆系统的帐号
	private String  userName;     // 用户的实际姓名
	private String  employeeNo;   // 员工编号
	private String  sex;          // 姓名
	private Date    birthday;     // 出生年月 
    private String  mail;         // 邮件
	private String  password;     // 密码 
    private String  groupId;      // 对应用户所在组id  
    private String  applicationId;// 应用系统id
    
    private String  certificateCategory;// 证件种类 :  1：工作证  2：身份证等
    private String  certificateNumber;  // 证件号 
    
	private Integer disabled = UMConstants.FALSE;     // 帐户状态 
	private Date    accountUsefulLife = new Date(); // 帐户有效期限：用户帐户到某个指顶的期限过期
    
    private Map<String, Object> dynProperties = new HashMap<String, Object>();
 
    public Date getAccountUsefulLife() {
        // 默认有效期50年
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.YEAR, 50);
        accountUsefulLife = calendar.getTime();
        return accountUsefulLife;
    }
 
	public Integer getDisabled() {
		return disabled;
	}
 
	public void setDisabled(Integer accountState) {
		this.disabled = accountState;
	}
 
	public void setAccountUsefulLife(Date accountUsefulLife) {
		this.accountUsefulLife = accountUsefulLife;
	}
 
	public Date getBirthday() {
		return birthday;
	}
 
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	public String getEmployeeNo() {
		return employeeNo;
	}
 
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
 
	public String getGroupId() {
		return groupId;
	}
 
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
 
	public String getId() {
		return id;
	}
 
	public void setId(String id) {
		this.id = id;
	}
 
	public String getLoginName() {
		return loginName;
	}
 
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
 
	public String getPassword() {
		return password;
	}
 
	public void setPassword(String password) {
		this.password = password;
	}
 
	public String getSex() {
		return sex;
	}
 
	public void setSex(String sex) {
		this.sex = sex;
	}
 
	public String getUserName() {
		return userName;
	}
 
	public void setUserName(String userName) {
		this.userName = userName;
	}
 
	public Integer getSeqNo() {
		return seqNo;
	}
 
	public void setSeqNo(Integer userOrder) {
		this.seqNo = userOrder;
	}
 
	public String getApplicationId() {
		return applicationId;
	}
 
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	
	public String toString(){
		return "登陆名:["+loginName+"] 姓名:["+userName+"] 员工编号:["+employeeNo+"] 用户所在组id:["+groupId+"]";
	}
 
    public Map<String, Object> getDynProperties() {
        return dynProperties;
    }
 
    public String getMail() {
        return mail;
    }
 
    public void setMail(String mail) {
        this.mail = mail;
    }
 
    public String getCertificateCategory() {
        return certificateCategory;
    }
 
    public void setCertificateCategory(String certificateCategory) {
        this.certificateCategory = certificateCategory;
    }
 
    public String getCertificateNumber() {
        return certificateNumber;
    }
 
    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }
}

	