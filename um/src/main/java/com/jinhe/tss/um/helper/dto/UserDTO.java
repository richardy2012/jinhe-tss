package com.jinhe.tss.um.helper.dto;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.jinhe.tss.um.UMConstants;

public class UserDTO {
	
	private String  id;           // 用户主键id号
	private String  loginName;    // 用户登陆系统的帐号
	private String  userName;     // 用户的实际姓名
	private String  employeeNo;   // 员工编号
	private String  sex;          // 姓名
	private Date    birthday;     // 出生年月 
    private String  email;        // 邮件
    private String  groupId;      // 对应用户所在组id  
    
	private Integer disabled = UMConstants.FALSE; // 帐户状态 
	private Date    accountLife = new Date();     // 帐户有效期限：用户帐户到某个指顶的期限过期
    
    public Date getAccountLife() {
        // 默认有效期50年
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.YEAR, 50);
        accountLife = calendar.getTime();
        return accountLife;
    }
 
	public Integer getDisabled() {
		return disabled;
	}
 
	public void setDisabled(Integer accountState) {
		this.disabled = accountState;
	}
 
	public void setAccountLife(Date accountLife) {
		this.accountLife = accountLife;
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
	
	public String toString(){
		return "登陆名:[" + loginName + "] 姓名:[" + userName + "] 员工编号:["
				+ employeeNo + "] 用户所在组id:[" + groupId + "]";
	}
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
}

	