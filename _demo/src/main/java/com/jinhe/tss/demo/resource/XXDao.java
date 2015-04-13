package com.jinhe.tss.demo.resource;

import java.util.List;

import com.jinhe.tss.framework.persistence.ITreeSupportDao;
import com.jinhe.tss.um.permission.filter.PermissionFilter4Branch;
import com.jinhe.tss.um.permission.filter.PermissionTag;
 
public interface XXDao extends ITreeSupportDao<XX> {
	
    @PermissionTag(
    		application = XX.APPLICATION,
    		resourceType = XX.RESOURCE_TYPE,
            filter = PermissionFilter4Branch.class)
    List<XX> getChildrenById(Long id, String operationId);
    
    XX deleteXX(XX XX);
 
}
