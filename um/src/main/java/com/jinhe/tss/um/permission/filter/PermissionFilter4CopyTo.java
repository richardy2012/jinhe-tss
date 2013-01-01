package com.jinhe.tss.um.permission.filter;

import java.util.List;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.um.permission.PermissionHelper;

/**
 * 资源复制到时候的权限检查： 判断用户对复制到的目标父节点是否有新增权限。
 * 
 * 复制[只要对父节点有新增权限，就可以复制下面的子节点] 
 * 
 *  List<?> copyTo(Long id, Long targetId ......)
 * 
 */
public class PermissionFilter4CopyTo implements IPermissionFilter {
	
	@Override
	public void doFilter(Object args[], Object returnValue, PermissionTag tag, PermissionHelper helper) {
        String application = tag.application();
		String resourceType = tag.resourceType();
		String operation = tag.operation();
        
		List<Long> permitedResourceIds = helper.getResourceIdsByOperation(application, resourceType, operation);
 
        Long parentResourceId = (Long)args[1];
        
        if(!permitedResourceIds.contains(parentResourceId)) {
            throw new BusinessException("复制到失败，您对进行操作的【ID为:" + parentResourceId + " 的目标节点】下没有新增资源的权限！");
        }
	}

}
