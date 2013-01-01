package com.jinhe.tss.portal.permission;

import java.util.List;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.um.permission.filter.IPermissionFilter;
import com.jinhe.tss.um.permission.filter.PermissionTag;

/**
 * 删除时候检查用户是否对删除资源有删除权限。
 */
public class PermissionFilter4Check implements IPermissionFilter {
	
	@Override
	public void doFilter(Object args[], Object returnValue, PermissionTag tag, PermissionHelper helper) {
	    if(args.length < 1 ) return;
	    
	    Long resourceId = null;
 
	    //  PortalStructure getPoratalStructure(Long id)
	    if( args[0] instanceof Long ) {
	        resourceId = (Long) args[0];
	    }
	    
	    if(resourceId == null) return;
        
        List<Long> permitedResourceIds = helper.getResourceIdsByOperation(tag.application(), tag.resourceType(), tag.operation());
 
        if(!permitedResourceIds.contains(resourceId)) {
            throw new BusinessException("操作失败，您对进行操作的ID为:" + resourceId + " 目标对象没有该操作权限！");
        }
	}

}
