package com.jinhe.tss.um.permission.filter;

import java.util.List;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.um.permission.PermissionHelper;

/**
 * 资源复制时候的权限检查： 判断用户对复制节点的父亲节点是否有新增权限。
 * 
 * 复制[只要对父节点有新增权限，就可以复制下面的子节点] 
 * 
 *  List<?> copy(Long id)
 * 
 */
public class PermissionFilter4Copy implements IPermissionFilter {
	
	@Override
	public void doFilter(Object args[], Object returnValue, PermissionTag tag, PermissionHelper helper) {
        String application = tag.application();
		String resourceType = tag.resourceType();
		String operation = tag.operation();
        
		List<Long> permitedResourceIds = helper.getResourceIdsByOperation(application, resourceType, operation);
 
        Long parentResourceId = helper.getParentResourceId(application, resourceType, (Long)args[0]);
        
        if(!permitedResourceIds.contains(parentResourceId)) {
            throw new BusinessException("复制失败，您对进行操作的【ID为:" + parentResourceId + " 的节点】下没有新增资源的权限！");
        }
	}

}
