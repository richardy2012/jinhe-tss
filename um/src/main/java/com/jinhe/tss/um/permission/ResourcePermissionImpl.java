package com.jinhe.tss.um.permission;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.persistence.TreeSupportDao;
import com.jinhe.tss.framework.persistence.entityaop.IDecodable;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.util.BeanUtil;

/**
 * <p>
 * 操作资源表时相关的补全操作和删除操作
 * </p>
 */
@Component("ResourcePermission")
public class ResourcePermissionImpl extends TreeSupportDao<IDecodable> implements ResourcePermission {
	
    public ResourcePermissionImpl() {
        super(IDecodable.class);
    }

    @Autowired private RemoteResourceTypeDao resourceTypeDao;
 
    public void addResource(Long resourceId, String resourceTypeId) {
        flush();
        String unSuppliedTable = resourceTypeDao.getUnSuppliedTable(PermissionHelper.getApplicationID(), resourceTypeId);
        String suppliedTable  = resourceTypeDao.getSuppliedTable(PermissionHelper.getApplicationID(), resourceTypeId);
        String resourceTable = resourceTypeDao.getResourceTable(PermissionHelper.getApplicationID(), resourceTypeId);

        IResource resource = (IResource) getEntity(BeanUtil.createClassByName(resourceTable), resourceId);
        if(resource == null) {
            throw new BusinessException("权限补齐时找不到资源对象，请检查资源视图（" + resourceTable + "）中是否包含该对象？或者是否过滤掉了？");
        }
        
        List<?> parentPermissions = getEntities("from " + suppliedTable + " t where t.resourceId = ?", resource.getParentId()); 
        for (Object temp : parentPermissions) {
            ISuppliedPermission parentPermission = (ISuppliedPermission) temp;
            Integer permissionState = parentPermission.getPermissionState();
            
            // 子节点的权限跟父节点的一模一样
            PermissionHelper.getInstance().insertSuppliedTable(parentPermission, resource, suppliedTable);
            
            /* 如果permission的授权状态为PERMIT_NODE_SELF（即仅此节点，不向下传递），则相应的在未补齐表中加入一条当前新增节点授权记录
                 删除的时候用的到，参考com.jinhe.tss.um.permission.impl.PermissionDao.deleteOldPermission()方法 */
            if(permissionState.equals(UMConstants.PERMIT_NODE_SELF)){
                PermissionHelper.getInstance().insertUnSuppliedTable(parentPermission, resourceId, unSuppliedTable);
            }
        }
    }

	public void delResource(Long resourceId, String resourceTypeId){	
        flush();
        
        String unSuppliedTable = resourceTypeDao.getUnSuppliedTable(PermissionHelper.getApplicationID(), resourceTypeId);
        String suppliedTable  = resourceTypeDao.getSuppliedTable(PermissionHelper.getApplicationID(), resourceTypeId);
        executeHQL("delete " + suppliedTable + " t where t.resourceId = ?", resourceId);    // 删除授权视图(补齐的表)
        executeHQL("delete " + unSuppliedTable + " t where t.resourceId = ?", resourceId);  // 删除授权表（未补齐表）   
	}
 
	public void updateResource(Long resourceId, String resourceTypeId){
		String suppliedTable = resourceTypeDao.getSuppliedTable(PermissionHelper.getApplicationID(), resourceTypeId);
		String resourceTable = resourceTypeDao.getResourceTable(PermissionHelper.getApplicationID(), resourceTypeId);
		
		List<?> children = getChildrenById(resourceTable, resourceId);
		
		// 先删除已经存在的关系
		for (Object child : children) {
			executeHQL("delete " + suppliedTable + " t where t.resourceId = ?", ((IResource) child).getId());
		}	
		
		// 建立新的关系
		for(Object child : children){	
			addResource(((IResource) child).getId(), resourceTypeId);			
		}
	}

    public List<?> getResourceIds(String applicationId, String resourceTypeId, String operationId, Long userId) {
        String suppliedTable = resourceTypeDao.getSuppliedTable(applicationId, resourceTypeId);
        String hql = "select distinct t.resourceId from " + suppliedTable + " t, RoleUserMapping ru " +
                " where t.roleId = ru.id.roleId and t.operationId = ? and ru.id.userId = ?";
        return getEntities(hql, operationId, userId);
    }
 
	public List<?> getParentResourceIds(String applicationId, String resourceTypeId, Long resourceId, 
            String operationId, Long operatorId){	
	    
        String resourceTable = resourceTypeDao.getResourceTable(PermissionHelper.getApplicationID(), resourceTypeId);
        Class<?> resourceClazz = BeanUtil.createClassByName(resourceTable);
        IDecodable decodable = (IDecodable) getEntity(resourceClazz, resourceId);
        if(decodable == null)  {  //如果资源表中找不到了该id对应的资源，则可能该资源已经被删除
            return new ArrayList<Object>();
        }
        
        String suppliedTable = resourceTypeDao.getSuppliedTable(applicationId, resourceTypeId);
        String hql = "select distinct t.id from " + resourceClazz.getName() + " t, RoleUserMapping r, " + suppliedTable + " v" +
            " where t.id = v.resourceId and v.roleId = r.id.roleId and r.id.userId = ? and v.operationId = ? and ? like t.decode || '%'";
        
        return getEntities(hql, operatorId, operationId, decodable.getDecode());
	}

	public List<?> getSubResourceIds(String applicationId, String resourceTypeId, Long resourceId, 
            String operationId, Long operatorId){	
	    
	    String resourceTable = resourceTypeDao.getResourceTable(applicationId, resourceTypeId);
	    Class<?> resourceClazz = BeanUtil.createClassByName(resourceTable);
	    IDecodable decodable = (IDecodable) getEntity(resourceClazz, resourceId);
        if(decodable == null)  {  //如果资源表中找不到了该id对应的资源，则可能该资源已经被删除
            return new ArrayList<Object>();
        }
        
        String suppliedTable = resourceTypeDao.getSuppliedTable(applicationId, resourceTypeId);
        String hql = "select distinct t.id from " + resourceTable + " t, RoleUserMapping r, " + suppliedTable + " v" +
            " where t.id = v.resourceId and v.roleId = r.id.roleId and r.id.userId = ? and v.operationId = ? and t.decode like ?";
        
        return getEntities(hql, operatorId, operationId, decodable.getDecode() + "%");	
	}
}