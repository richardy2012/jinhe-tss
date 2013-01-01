package com.jinhe.tss.um.permission;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

import com.jinhe.tss.framework.persistence.IEntity;
import com.jinhe.tss.um.UMConstants;

/** 
 * 未补齐表超类
 */
@MappedSuperclass
public abstract class AbstractUnSuppliedTable implements IEntity, IUnSuppliedPermission{
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "permission_sequence")
    @SequenceGenerator(name = "permission_sequence", sequenceName = "permission_sequence", initialValue = 1000, allocationSize = 10)
    protected Long    id;           // 主键Id
	
    @Column(nullable = false)  
    protected Long    resourceId;   // 资源Id
    
    @Column(nullable = false)  
    protected Long    roleId;       // 角色Id
    
    @Column(nullable = false)  
    protected String  operationId;  // 操作选项Id
    
    protected Integer permissionState;             // 权限维护状态(1-仅此节点,2-该节点及所有下层节点)
    protected Integer isGrant = UMConstants.FALSE; // 是否可授权（0-不可授权,1-可授权）
    protected Integer isPass  = UMConstants.FALSE; // 是否可传递（0-不可传递,1-可传递）
    
    public String toString() {
    	return "resource = " + resourceId + ", role = " + roleId 
    		   + ", operationId = " + operationId + ", permissionState = " + permissionState
    		   + ", isGrant = " + isGrant + ", isPass = " + isPass;
    }
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public Integer getIsGrant() {
        return isGrant;
    }
 
    public void setIsGrant(Integer isGrant) {
        this.isGrant = isGrant;
    }
 
    public Integer getIsPass() {
        return isPass;
    }
 
    public void setIsPass(Integer isPass) {
        this.isPass = isPass;
    }
 
    public String getOperationId() {
        return operationId;
    }
 
    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
 
    public Integer getPermissionState() {
        return permissionState;
    }
 
    public void setPermissionState(Integer permissionState) {
        this.permissionState = permissionState;
    }
 
    public Long getResourceId() {
        return resourceId;
    }
 
    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }
 
    public Long getRoleId() {
        return roleId;
    }
 
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}

