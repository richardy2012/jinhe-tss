package com.jinhe.tss.um.permission;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.component.param.ParamConstants;
import com.jinhe.tss.framework.persistence.IEntity;
import com.jinhe.tss.framework.persistence.TreeSupportDao;
import com.jinhe.tss.framework.persistence.entityaop.IDecodable;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.web.dispaly.tree.ILevelTreeNode;
import com.jinhe.tss.framework.web.rmi.HttpInvokerProxyFactory;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.entity.permission.RoleUserMapping;
import com.jinhe.tss.um.entity.permission.RoleUserMappingId;
import com.jinhe.tss.um.permission.dispaly.ResourceTreeNode;
import com.jinhe.tss.util.BeanUtil;

/**
 * 操作资源表时相关的补全操作和删除操作。
 * PermissionHelper 配置到各个应用中。
 * 负责处理基于TSS框架的应用里的资源表、权限表（包括补齐和未补齐）的操作。
 * 针对TSS特有的操作不宜放在本接口里，比如读取角色信息等。
 */
@Component("permissionHelper")
public class PermissionHelper extends TreeSupportDao<IDecodable> {
    
    public PermissionHelper() {
		super(IDecodable.class);
	}
    
    @Autowired private RemoteResourceTypeDao resourceTypeDao;
    
    
    public static PermissionHelper getInstance(){
        return (PermissionHelper) Global.getBean("permissionHelper");
    }
    
    /**
     * 根据AppCode值获取到底是哪个应用的PermissionService
     */
    public static PermissionService getPermissionService(String applicationId, PermissionService autowired) {
        if( Config.getAttribute(Config.APPLICATION_CODE).equalsIgnoreCase(applicationId) ) {
            return autowired; // 如果是取应用本地的权限服务，则直接返回spring注入的
        }
            
        // 如果查询的是非本地应用的权限服务，则取的是资源所在的应用服务    
    	HttpInvokerProxyFactory factory = new HttpInvokerProxyFactory();
        factory.setServiceUrl(UMConstants.PERMISSION_SERVICE_URL);
        factory.setServiceInterface(PermissionService.class);
        factory.setAppCode(applicationId.toUpperCase());
        
        return (PermissionService)factory.getObject();
    }
    
    /** 获取当前应用ID */
    public static String getApplicationID() {
        return Config.getAttribute(Config.APPLICATION_CODE).toLowerCase();
    }
    
    /**
     * 获取当前用户对点击资源节点（以及父节点）的操作权限
     * @param resourceId
     * @param suppliedTable
     * @param resourceClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> getOperationsByResource(Long resourceId, String suppliedTable, Class<?> resourceClass) {
    	
        List<String> operations = new ArrayList<String>();
    	
        String hql = "select distinct p.id.operationId from " + suppliedTable + " p, RoleUserMapping ru "  +
                " where p.id.resourceId = ? and p.id.roleId = ru.id.roleId and ru.id.userId = ? ";  
        List<String> operationsOnResource = (List<String>) getEntities(hql, resourceId, Environment.getOperatorId());
        operations.addAll(operationsOnResource);  // 用户对指定节点的操作权限
        
        ILevelTreeNode resource = (ILevelTreeNode) getEntity(resourceClass, resourceId);
        List<?> parentOperations = getEntities(hql, resource.getParentId(), Environment.getOperatorId()); 
        for(Object oprationId : parentOperations){
            operations.add("p_" + oprationId);  // 指定节点的父节点的操作权限，加"p_"
        }
        
        return operations;
    }
    
    /**
     * 获取用户对一个应用中的一种资源类型中的一个资源拥有的权限选项ID集合
     * @param resourceTypeId
     * @param resourceId
     * @param userId
     * @return
     */
    public List<?> getOperationsByResource(String resourceTypeId, Long resourceId) {
        String applicationId = PermissionHelper.getApplicationID();
		String suppliedTable = resourceTypeDao.getSuppliedTable(applicationId, resourceTypeId);
        String resourceTable = resourceTypeDao.getResourceTable(applicationId, resourceTypeId);
        
        Class<?> resourceClass = BeanUtil.createClassByName(resourceTable);
        
        return getOperationsByResource(resourceId, suppliedTable, resourceClass);
    }
    
    /**
     *  新增一条授权信息（未补齐表）
     * @param roleId
     * @param resourceId
     * @param operationId
     * @param permissionState
     * @param isGrant
     * @param isPass
     * @param suppliedTable
     */
    public IUnSuppliedPermission insertUnSuppliedTable(Long roleId, Long resourceId, String operationId, 
            Integer permissionState, Integer isGrant, Integer isPass, String suppliedTable) {
        
        IUnSuppliedPermission usPermission = (IUnSuppliedPermission) BeanUtil.newInstanceByName(suppliedTable);         
        usPermission.setRoleId(roleId);
        usPermission.setResourceId(resourceId);
        usPermission.setOperationId(operationId);
        usPermission.setPermissionState(permissionState);
        usPermission.setIsGrant(isGrant);
        usPermission.setIsPass(isPass);
        
        return (IUnSuppliedPermission) createObject(usPermission);
    }
    
    public IUnSuppliedPermission insertUnSuppliedTable(IUnSuppliedPermission permission, Long resourceId, String suppliedTable) {
        IUnSuppliedPermission usPermission = (IUnSuppliedPermission) BeanUtil.newInstanceByName(suppliedTable);         
        usPermission.setRoleId(permission.getRoleId());
        usPermission.setOperationId(permission.getOperationId());
        usPermission.setPermissionState(permission.getPermissionState());
        usPermission.setIsGrant(permission.getIsGrant());
        usPermission.setIsPass(permission.getIsPass());
        
        usPermission.setResourceId(resourceId);
        
        return (IUnSuppliedPermission) createObject(usPermission);
    }

    /**
     * 根据已有授权信息，新增一条授权信息（补齐表）。 新增前会先判断授权信息是否已经存在。
     * @param permission 
     * 				节点的未补齐授权信息 或 父节点授权信息
     * @param resource 
     * 				节点资源
     * @param suppliedTable
     * 				补齐表实体
     * 
     * TODO 保存权限前要在当前授权级别的上一级去查找有没有已经授过的，如果授过的，则删掉。
     *      保存当前授权级别(一个资源对同一个权限选项只能有一种授权级别)
     */
    public void insertSuppliedTable(IUnSuppliedPermission permission, IResource resource, String suppliedTable){
        insertSuppliedTable(permission, resource, BeanUtil.createClassByName(suppliedTable));
    }
    
    public void insertSuppliedTable(IUnSuppliedPermission permission, IResource resource, Class<?> suppliedTableClass){
        Long resourceId = resource.getId(); 
        String operationId = permission.getOperationId(); 
        Long roleId = permission.getRoleId();
        Integer permissionState = permission.getPermissionState();
        Integer isGrant = permission.getIsGrant(); 
        Integer isPass = permission.getIsPass();
        
    	//先判断授权信息是否已经存在
        String hql = "from " + suppliedTableClass.getName() + " t  where t.resourceId = ? and t.operationId = ?"
                + " and t.roleId = ? and t.permissionState = ? and t.isGrant = ? and t.isPass = ?";
        List<?> list = getEntities(hql, new Object[]{resourceId, operationId, roleId, permissionState, isGrant, isPass});
        if( list.size() == 0 ){
            ISuppliedPermission newPermission = (ISuppliedPermission) BeanUtil.newInstance(suppliedTableClass);   
            newPermission.setResourceId(resourceId);
            newPermission.setOperationId(operationId);
            newPermission.setRoleId(roleId);
            newPermission.setPermissionState(permissionState);
            newPermission.setIsGrant(isGrant);
            newPermission.setIsPass(isPass);
            
            newPermission.setResourceName(resource.getName());
            
            createObjectWithoutFlush(newPermission);
        }
    }
    
    // ----------------------------------------------------- HQL builder -------------------------------------------------------------
    
    public static final String ORDER_BY = " order by o.decode ";

    public static String formatHQLFrom(String entityName, String suppliedTable) {
        return " from " + entityName + " o, RoleUserMapping ru, " + suppliedTable + " p ";
    }

    public static String permissionCondition() {
        return " where ru.id.roleId = p.roleId and ru.id.userId = ? and o.id = p.resourceId and p.operationId = ? ";
    }

    public static String permissionConditionII() {
        return " where ru.id.roleId = p.roleId and ru.id.userId = :operatorId and o.id = p.resourceId and p.operationId = :operationId ";
    }
    
    /**
     * 生成的HQL如下：
     * select distinct o from entityName o, RoleUserMapping ru, suppliedTable p
     * where ru.id.roleId = p.roleId and ru.id.userId = ? and o.id = p.resourceId and p.operationId = ?
     * order by o.decode
     */
    public static String permissionHQL(String entityName, String suppliedTable) {
        return permissionHQL(entityName, suppliedTable, true);
    }
    
    public static String permissionHQL(String entityName, String suppliedTable, boolean isLevelTree) {
        return permissionHQL(entityName, suppliedTable, "", isLevelTree);
    }

    public static String permissionHQL(String entityName, String suppliedTable, String conditon, boolean isLevelTree) {
        String hql = "select distinct o " + formatHQLFrom(entityName, suppliedTable) + permissionCondition() + conditon;
        if(isLevelTree) {
            hql += ORDER_BY;
        }
        return hql;
    }
    
    // ----------------------------------------------------- 原 PermissDao -------------------------------------------------------------
    /**
     * PermissionDao配置到各个应用中。
     * 负责处理基于TSS框架的应用里的资源表、权限表（包括补齐和未补齐）的操作。
     * 针对TSS特有的操作不宜放在本接口里，比如读取角色信息等。
     */
    
    /**
     * <p>
     * 批量删除指定角色拥有的资源授权信息。 授权时使用。
     * </p>
	 * @param roleId
	 * @param permissionRank
	 * @param unSuppliedTable
	 * @param suppliedTable
	 * @param resourceTable
	 */
	public void deletePermissionByRole(Long roleId, String permissionRank, String unSuppliedTable, String suppliedTable, String resourceTable) {
	    /* 
	     * 先按 "角色" 把要删除的记录的permissionState保存下来，用来在删除补全表时做判断。
	     * 删除只删除本级别的,其他级别已经授权过的不要改变 
	     */
        String rankCondition = genRankCondition4DeleletePermission(permissionRank);
        List<?> exsitUsPermissions = getEntities(" from " + unSuppliedTable + " p where p.roleId = ? " + rankCondition, roleId);
        deleteExistPermission(exsitUsPermissions, rankCondition, suppliedTable, resourceTable);
	}
	
    /**
     * <p>
     * 批量删除指定资源相关授权信息。 授权时使用。
     * </p>
	 * @param resourceId
	 * @param permissionRank
	 * @param unSuppliedTable
	 * @param suppliedTable
	 * @param resourceTable
	 */
	public void deletePermissionByResource(Long resourceId, String permissionRank, String unSuppliedTable, String suppliedTable, String resourceTable) {
	    /* 先按 "资源" 把要删除的记录的permissionState保存下来，用来在删除补全表时做判断。
	     * 删除只删除本级别的,其他级别已经授权过的不要改变 */
        String rankCondition = genRankCondition4DeleletePermission(permissionRank);
        List<?> exsitUsPermissions = getEntities(" from " + unSuppliedTable + " p where p.resourceId = ? " + rankCondition, resourceId);
        deleteExistPermission(exsitUsPermissions, rankCondition, suppliedTable, resourceTable);
	}
    
    // 根据这些记录的PermissionState值，来判断在删除补全表时是否删除子节点的权限
    private void deleteExistPermission(List<?> exsitUsPermissions, String rankCondition, String suppliedTable, String resourceTable){
        deleteAll(exsitUsPermissions);
        
        for ( Object temp : exsitUsPermissions ) {
            IUnSuppliedPermission usPermission = (IUnSuppliedPermission) temp;
            Long roleId = usPermission.getRoleId();
            Long resourceId = usPermission.getResourceId();
            String operationId = usPermission.getOperationId();
            
            // 如果授权状态是2(即PERMIT_SUB_TREE), 说明【其所有子节点的权限都】由该节点自动产生，需删除补全表中【其所有子节点】对应【该角色】【该权限项】的授权信息
            if (usPermission.getPermissionState().equals(UMConstants.PERMIT_SUB_TREE)) { 
                List<?> childResources = getChildrenById(resourceTable, resourceId);
                for (Object child : childResources) {
                    IResource childResource = (IResource) child;
                    delSuppliedPermission(suppliedTable, childResource.getId(), roleId, operationId, rankCondition);
                }
            } 
            // 删除补全表的单个节点
            else { 
                delSuppliedPermission(suppliedTable, resourceId, roleId, operationId, rankCondition);
            }
        }
        
        flush();
    }

    private void delSuppliedPermission(String suppliedTable, Long resourceId, Long roleId, String operationId, String rankCondition) {
        String hql = "delete " + suppliedTable + " p where" + " p.resourceId=? and p.roleId=? and p.operationId=? " + rankCondition;
        executeHQL(hql, resourceId, roleId, operationId);
    }
    
    String genRankCondition4DeleletePermission(String permissionRank){
        Integer[] isGrantAndPass = convertRank(permissionRank);
        return " and p.isGrant = " + isGrantAndPass[0] + " and p.isPass = " + isGrantAndPass[1];
    }
    
	/**
	 * 将授权级别转化为两查询条件isGrant 和 isPass 的值
	 * @param permissionRank
	 * @return
	 */
	Integer[] convertRank(String permissionRank) {
        Integer isGrant = ParamConstants.FALSE, isPass = ParamConstants.FALSE;
        if (UMConstants.LOWER_PERMISSION.equals(permissionRank)) { // 普通授权
            isGrant = isPass = ParamConstants.FALSE;
        } 
        else if (UMConstants.AUTHORISE_PERMISSION.equals(permissionRank)) { // 可授权
            isGrant = ParamConstants.TRUE;
            isPass  = ParamConstants.FALSE;
        } 
        else if (UMConstants.PASSON_AUTHORISE_PERMISSION.equals(permissionRank)) { // 可授权可传递
            isGrant = isPass = ParamConstants.TRUE;
        }
        return new Integer[] {isGrant, isPass};
	}

    /**
     * 读取权限出来授权时，要取高一级授权级别的来授予低级别的。 <br/>
     * 比如原本【可授权的权限】可以拿出来进行【普通授权】， <br/>
     * 原本【可授权授权且可传递授权】则可以拿出来进行【普通授权】或【可授权授权】， <br/>
     * 而原本就是【普通授权】的权限则不能拿出来进行授权。 <br/>
     * @param permissionRank
     * @return
     */
    String genRankCondition4SelectPermission(String permissionRank){
        if (UMConstants.LOWER_PERMISSION.equals(permissionRank)) {
            return " and p.isGrant=1 "; // 普通授权 : isGrant = 1
        }
        if(UMConstants.AUTHORISE_PERMISSION.equals(permissionRank) 
        		|| UMConstants.PASSON_AUTHORISE_PERMISSION.equals(permissionRank)) {
            return " and p.isGrant=1 and p.isPass=1";  // 可授权授权且可传递授权 : isGrant = 1 and isPass = 1
        }
        return "";
    }
    
	/**
	 * <p>
	 * 获取用户对一个资源在当前授权级别能够看到的子节点的个数
	 * </p>
	 * @param resourceId
	 * @param permissionRank
	 * @param suppliedTable
	 * @param resourceTable
	 * @return
	 */
	public int getVisibleChildrenNumByPermissionRank(Long resourceId, String permissionRank, String suppliedTable, String resourceTable) {
		String hql = "select distinct r.id from " + resourceTable + " r, " + suppliedTable + " p, Temp t " +
				" where t.id = p.roleId and p.resourceId = r.id and r.decode like ? ";
		hql += genRankCondition4SelectPermission(permissionRank);

		IResource resource = (IResource) getEntity(BeanUtil.createClassByName(resourceTable), resourceId);
		return getEntities(hql, resource.getDecode() + "%").size();
	}

	/**
	 * <p>
	 * 获取用户资源树（根据登陆用户拥有的权限过滤，Temp临时表中保存了登陆用户的roleIds）
	 * </p>
	 * @param permissionRank
	 * @param suppliedTable
	 * @param resourceTable
	 * @return
	 */
	public List<ResourceTreeNode> getVisibleResourceTree(String permissionRank, String suppliedTable, String resourceTable) {
		String hql = "select distinct r.id, r.parentId, r.name, r.decode "
		    + " from " + resourceTable + " r, " + suppliedTable + " p, Temp t " 
		    + " where p.roleId = t.id and p.resourceId = r.id ";
        hql += genRankCondition4SelectPermission(permissionRank) + " order by r.decode";

		return ResourceTreeNode.genResourceTreeNodeList(getEntities(hql));
	}

	/**
	 * <p>
	 * 获取补全的用户资源权限列表（根据登陆用户拥有的权限过滤，Temp临时表中保存了登陆用户的roleIds）
	 * </p>
     * @param permissionRank
     * @param suppliedTable
     * @return
     */
    public List<SuppliedPermissionDTO> getAllResourcePermissions(String permissionRank, String suppliedTable) {
        String hql = "select distinct p.resourceId, p.operationId, p.permissionState, max(p.isGrant), max(p.isPass), p.roleId"
                + " from " + suppliedTable + " p , Temp t " 
                + " where p.roleId = t.id " + genRankCondition4SelectPermission(permissionRank) 
                + " group by p.resourceId, p.operationId, p.permissionState, p.roleId";
        
        return SuppliedPermissionDTO.genPermissionDTOList(getEntities(hql));
    }

	/**
	 * <p>
	 * 获取用户对一个资源的权限信息
	 * </p>
	 * @param permissionRank
	 * @param suppliedTable
	 * @param resourceId
	 * @return
	 */
	public List<SuppliedPermissionDTO> getOneResourcePermissions(String permissionRank, String suppliedTable, Long resourceId) {
		String hql = "select distinct p.resourceId, p.operationId, p.permissionState, max(p.isGrant), max(p.isPass), p.roleId"
				+ " from " + suppliedTable + " p, Temp t " 
				+ " where  p.roleId = t.id and p.resourceId = ? " + genRankCondition4SelectPermission(permissionRank) 
				+ " group by p.resourceId, p.operationId, p.permissionState, p.roleId";

		return SuppliedPermissionDTO.genPermissionDTOList(getEntities(hql, resourceId));
	}

	// ===========================================================================
	// 登陆相关方法
	// ===========================================================================

	/**
	 * <p>
	 * 删除旧的关系
     * 保存【用户对应角色列表】到RoleUserMapping表
	 * </p>
	 * @param roleUserList
	 * @param logonUserId
	 */
	public void saveRoles4LonginUser(List<Object[]> roleUserList, Long logonUserId){
        executeHQL("delete RoleUserMapping o where o.id.userId = ?",  logonUserId);
        
		// 检查用户是否已经匿名登录了（所有用户登陆的时候都有匿名角色），没有的话插入一条【匿名角色对用户】的记录
        if( !checkExistInList(roleUserList, logonUserId) ) {
        	roleUserList.add( new Long[]{ logonUserId, UMConstants.ANONYMOUS_ROLE_ID } );
        }
        
        for(Object[] roleUserInfo : roleUserList){
			RoleUserMappingId id = new RoleUserMappingId();
			id.setUserId( (Long) roleUserInfo[0] );
			id.setRoleId( (Long) roleUserInfo[1] );
			
			RoleUserMapping entity = new RoleUserMapping();
			entity.setId(id);
			
			createObject(entity);
		}
	}
	
	private boolean checkExistInList(List<Object[]> roleUserList, Long logonUserId){
		for(Object[] roleUserInfo : roleUserList) {
			if(logonUserId.equals(roleUserInfo[0]) && UMConstants.ANONYMOUS_ROLE_ID.equals(roleUserInfo[1])){
				return true;
			}
		}
		return false;
	}
	
	// ===========================================================================
    // 用于资源权限过滤
    // ===========================================================================
	
    /**
     * 获取操作用户有指定操作权限（维护、浏览、查看等）的所有某类型资源ID列表
     * @param suppliedTable 
     *                  资源权限表
     * @param operation    
     *                  操作权限
     * @param operatorId
     *                  操作人
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Long> getResourceIdsByOperation(String suppliedTable, String operation, Long operatorId){
        String hql = "select distinct p.id.resourceId from RoleUserMapping ur, " + suppliedTable + " p" +
              " where p.operationId = ? and p.roleId = ur.id.roleId and ur.id.userId = ? ";
        return (List<Long>) getEntities( hql, operation, operatorId );
    }
    
    public List<Long> getResourceIdsByOperation(String suppliedTable, String operationId){
        return getResourceIdsByOperation(suppliedTable, operationId, Environment.getOperatorId());
    }
    
    public List<Long> getResourceIdsByOperation(String appId, String resourceTypeId, String operationId){
		return getResourceIdsByOperation(appId, resourceTypeId, operationId, Environment.getOperatorId());
    }
    
    public List<Long> getResourceIdsByOperation(String appId, String resourceTypeId, String operationId, Long operatorId){
        String suppliedTable = resourceTypeDao.getSuppliedTable(appId, resourceTypeId);
		return getResourceIdsByOperation(suppliedTable, operationId, operatorId);
    }
    
    /**
     * 把用户没有指定操作权限的资源给过滤点，资源列表里只留下有指定操作权限的。
     */
    public void filtrateResourcesByPermission( List<Long> permitedResourceIds, List<?> resources ){
        for( Iterator<?> it = resources.iterator(); it.hasNext(); ) {
            IEntity resource = (IEntity)it.next();
            if( !permitedResourceIds.contains(resource.getPK()) ) {
                it.remove();
            }
        }
    }
    
    public void filtrateResourcesByPermission(String appId, String resourceTypeId, String operationId, List<?> resources){
        String suppliedTable = resourceTypeDao.getSuppliedTable(appId, resourceTypeId);
        List<Long> permitedResourceIds = getResourceIdsByOperation(suppliedTable, operationId);
        filtrateResourcesByPermission(permitedResourceIds, resources);
    }
    
    /**
     * 获取资源的父节点Id
     * @param appId
     * @param resourceTypeId
     * @param resourceId
     * @return
     */
    public Long getParentResourceId(String appId, String resourceTypeId, Long resourceId) {
    	String resourceTable = resourceTypeDao.getResourceTable(appId, resourceTypeId);
    	IResource resource = (IResource) getEntity(BeanUtil.createClassByName(resourceTable), resourceId);
    	return resource.getParentId();
    }
}

	