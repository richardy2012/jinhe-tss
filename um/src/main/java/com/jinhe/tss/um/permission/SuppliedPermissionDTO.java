package com.jinhe.tss.um.permission;

import java.util.ArrayList;
import java.util.List;

import com.jinhe.tss.util.EasyUtils;

/**
 * 用户资源权限DTO
 */
public class SuppliedPermissionDTO {
    
    private String  resourceName;   // 资源名称
	private Long    resourceId;    // 资源Id
	private Long    roleId;       // 用户Id
	private String  operationId; // 操作选项Id
	private Integer permissionState;  // 选项状态
	private Integer isGrant;         // 是否可授权（0-不可授权,1-可授权）
	private Integer isPass;         // 是否可传递（0-不可传递,1-可传递）
	
	public SuppliedPermissionDTO(Object[] permissionInfo) {
	    this.resourceId      = EasyUtils.convertObject2Long( permissionInfo[0] );
	    this.operationId     = (String) permissionInfo[1];
        this.permissionState = EasyUtils.convertObject2Integer( permissionInfo[2] );
        this.isGrant         = EasyUtils.convertObject2Integer( permissionInfo[3] );
        this.isPass          = EasyUtils.convertObject2Integer( permissionInfo[4] );
        this.roleId          = EasyUtils.convertObject2Long( permissionInfo[5] );
	}
	
	public static List<SuppliedPermissionDTO> genPermissionDTOList(List<?> permissionList){
        List<SuppliedPermissionDTO> result = new ArrayList<SuppliedPermissionDTO>();
        for (Object permission : permissionList) {
            result.add(new SuppliedPermissionDTO((Object[]) permission));
        }
        return result;
    }

    public String getResourceName() {
        return resourceName;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public String getOperationId() {
        return operationId;
    }

    public Integer getPermissionState() {
        return permissionState;
    }

    public Integer getIsGrant() {
        return isGrant;
    }

    public Integer getIsPass() {
        return isPass;
    }
}

	