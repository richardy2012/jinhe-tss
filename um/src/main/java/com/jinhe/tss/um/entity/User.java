package com.jinhe.tss.um.entity;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.jinhe.tss.framework.persistence.entityaop.OperateInfo;
import com.jinhe.tss.framework.web.dispaly.grid.GridAttributesMap;
import com.jinhe.tss.framework.web.dispaly.grid.IGridNode;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.TreeAttributesMap;
import com.jinhe.tss.framework.web.dispaly.xform.IXForm;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.sso.UMSLocalUserPWDIdentifier;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.DateUtil;

/**
 * 用户域对象
 */
@Entity
@Table(name = "um_user", uniqueConstraints = { 
        @UniqueConstraint(columnNames = { "applicationId", "loginName" })
})
@SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", initialValue = 10000, allocationSize = 10)
public class User extends OperateInfo implements ITreeNode, IGridNode, IXForm {

    public final static String entityCode = "um_user";
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_sequence")
    private Long   id;              // 用户ID：用户主键id号
    
    @Column(nullable = false)  
    private String applicationId;   // 应用系统ID:主键
    
    @Column(nullable = false)  
    private String loginName;       // 用户名:即用户登陆系统的帐号
    private String userName;        // 姓名:用户的实际姓名
    private String employeeNo;      // 员工编号
    private String sex;             // 性别
    private Date   birthday;        // 出生年月
    private String mail;            // 邮件 
    private String telephone;       // 联系电话 
    private String address;         // 地址 
    private String postalCode;      // 邮编 
    private String certificateNumber;  // 证件号
    private String certificateCategory;// 证件种类 :  1：工作证  2：身份证等
    private String password;           // 密码 
    private String passwordQuestion;   // 密码提示问题 
    private String passwordAnswer;     // 密码提示答案 
    private Long   passwordRuleId;     // 密码规则
    private Date   accountUsefulLife;  // 帐户有效期限 ：用户帐户到某个指顶的期限过期
    private String authenticateMethod = UMSLocalUserPWDIdentifier.class.getName();// 认证方式,一个实现对应的认证方式的类路径
    
    private String  otherAppUserId;  // 外部应用系统用户的ID (用于【平台用户】对应【其他系统用户】，其值可以是LDAP里的DN字符串)
    private Long    appUserId;       // 对应用户ID  （用于【其他系统用户】对应【主用户组用户】，多个【其他系统用户】可对应到同一个【主用户组用户】）
    private String  appUserName;     // 对应用户
    private Long    appUserGroupId;  // 对应用户所在组ID
    private String  appUserGroupName;// 对应用户所在组
    private Integer groupType;       // 对应用户所在组类型    
    
    private Integer disabled = UMConstants.FALSE; // 帐户状态, 帐户状态(0-停用, 1-启用)
    
    // 以下值展示的时候用
    @Transient private Long   groupId;         // 用户所在组id
    @Transient private String groupName;       // 用户所在组名称
    @Transient private String applicationName; // 用户所在应用系统名称

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
 
    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer accountState) {
        this.disabled = accountState;
    }
 
    public Date getAccountUsefulLife() {
        return accountUsefulLife;
    }
 
    public void setAccountUsefulLife(Date accountUsefulLife) {
        this.accountUsefulLife = accountUsefulLife;
    }
 
    public String getAddress() {
        return address;
    }
 
    public void setAddress(String address) {
        this.address = address;
    }
 
    public String getApplicationId() {
        return applicationId;
    }
 
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
 
    public Long getAppUserId() {
        return appUserId;
    }
 
    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }
 
    public String getAuthenticateMethod() {
        return authenticateMethod;
    }
 
    public void setAuthenticateMethod(String authenticateMethod) {
        this.authenticateMethod = authenticateMethod;
    }
 
    public Date getBirthday() {
        return birthday;
    }
 
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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
 
    public String getEmployeeNo() {
        return employeeNo;
    }
 
    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }
 
    public String getLoginName() {
        return loginName;
    }
 
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
 
    public String getMail() {
        return mail;
    }
 
    public void setMail(String mail) {
        this.mail = mail;
    }
 
    public String getOtherAppUserId() {
        return otherAppUserId;
    }
 
    public void setOtherAppUserId(String otherAppUserId) {
        this.otherAppUserId = otherAppUserId;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
 
    public String getPasswordAnswer() {
        return passwordAnswer;
    }
 
    public void setPasswordAnswer(String passwordAnswer) {
        this.passwordAnswer = passwordAnswer;
    }
 
    public String getPasswordQuestion() {
        return passwordQuestion;
    }
 
    public void setPasswordQuestion(String passwordQuestion) {
        this.passwordQuestion = passwordQuestion;
    }
 
    public String getPostalCode() {
        return postalCode;
    }
 
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
 
    public String getSex() {
        return sex;
    }
 
    public void setSex(String sex) {
        this.sex = sex;
    }
 
    public String getTelephone() {
        return telephone;
    }
 
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
 
    public String getUserName() {
        return userName;
    }
 
    public void setUserName(String userName) {
        this.userName = userName;
    }
 
    public GridAttributesMap getAttributes(GridAttributesMap map) {
        Map<String, Object> properties = new LinkedHashMap<String, Object>();
        BeanUtil.addBeanProperties2Map(this, properties);
        map.putAll(properties);
        
        if (id.equals(UMConstants.ADMIN_USER_ID)) {
            if (UMConstants.TRUE.equals(disabled)) {
                map.put("icon", UMConstants.STOP_ADMIN_USER_GRID_NODE_ICON);
            } else {
                map.put("icon", UMConstants.START_ADMIN_USER_GRID_NODE_ICON);
            }
        } else {
            if (UMConstants.TRUE.equals(disabled)) {
                map.put("icon", UMConstants.STOP_USER_GRID_NODE_ICON);
            } else {
                map.put("icon", UMConstants.START_USER_GRID_NODE_ICON);
            }
        }
       
        return map;
    }
 
    public Map<String, Object> getAttributesForXForm() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        BeanUtil.addBeanProperties2Map(this, map);
        
        map.put("birthday", DateUtil.format(birthday));
        map.put("accountUsefulLife", DateUtil.format(accountUsefulLife));
        
        return map;
    }
 
    public TreeAttributesMap getAttributes() {
        TreeAttributesMap map = new TreeAttributesMap(id, userName);
        map.put("groupId", groupId);
		
		super.putOperateInfo2Map(map);
        return map;
    }
 
    public String getAppUserGroupName() {
        return appUserGroupName;
    }
 
    public void setAppUserGroupName(String appUserGroupName) {
        this.appUserGroupName = appUserGroupName;
    }
 
    public Long getAppUserGroupId() {
        return appUserGroupId;
    }
 
    public void setAppUserGroupId(Long appUserGroupId) {
        this.appUserGroupId = appUserGroupId;
    }
 
    public String getAppUserName() {
        return appUserName;
    }
 
    public void setAppUserName(String appUserName) {
        this.appUserName = appUserName;
    }
 
    public Integer getGroupType() {
        return groupType;
    }
 
    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }
 
    public String getEntityCode() {
        return User.entityCode;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
 
	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
 
	public Long getPasswordRuleId() {
		return passwordRuleId;
	}
 
	public void setPasswordRuleId(Long passwordRuleId) {
		this.passwordRuleId = passwordRuleId;
	}
    
    public String toString(){
        return "(ID:" + this.id + ", loginName:" + this.loginName + ", appCode:" + this.applicationId + ")"; 
    }
}
