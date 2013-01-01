package com.jinhe.tss.um.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.persistence.ICommonDao;
import com.jinhe.tss.um.permission.RemoteResourceTypeDao;
 
public class RemoteSearchServiceImpl implements RemoteSearchService {
	
	@Autowired private ICommonDao commonDao;
	
	@Autowired private RemoteResourceTypeDao resourceTypeDao;

	public List<?> searchPermission(Long groupId, String applicationId, String resourceTypeId) {
		
		String resourceTable = resourceTypeDao.getResourceTable(applicationId, resourceTypeId);
		String suppliedTable = resourceTypeDao.getSuppliedTable(applicationId, resourceTypeId);
		
		// 一个用户组里面的用户所拥有的对某个系统中的某个类型资源的操作选项，权限为空的资源不显示。
		String hql = "select distinct r " 
			+ " from ViewRoleUser ru, GroupUser gu, Operation opt, " + resourceTable + " r, " + suppliedTable + " p"
	        + " where ru.id.roleId = p.roleId " +
	        		" and ru.id.userId = gu.userId and gu.groupId = ? " +
	        		" and p.resourceId = r.id " +
	        		" and p.operationId = opt.operationId "
	        + " order by r.decode ";
		
		return commonDao.getEntities(hql, groupId);
	}
 
}
