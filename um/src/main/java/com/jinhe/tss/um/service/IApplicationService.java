package com.jinhe.tss.um.service;

import java.util.List;

import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.entity.Application;
import com.jinhe.tss.um.entity.Operation;
import com.jinhe.tss.um.entity.ResourceType;
import com.jinhe.tss.um.entity.ResourceTypeRoot;
import com.jinhe.tss.um.permission.dispaly.ResourceTreeNode;
import com.jinhe.tss.um.permission.filter.PermissionFilter4Sort;
import com.jinhe.tss.um.permission.filter.PermissionTag;

public interface IApplicationService{

	/**
	 * <p>
	 * 获得用户对应用资源的权限
	 * 只判断对应用系统的权限
	 * 1.有操作权限就可查看
	 * 2.有管理权限就可作任何操作
	 * </p>
	 * @param applicationId
	 * @return
	 */
	List<?> getOperationsByResourceId(Long appId);
	
	/**
	 * <p>
	 * 根据应用系统id获得一个应用系统
	 * </p>
	 * @param applicationId
	 * @return
	 */
	Application getApplication(String applicationId);
	
	/**
	 * 根据id删除应用系统
	 * @param id
	 */
	void removeApplication(Long id);

	/**
	 * <p>
	 * 删除资源类型
	 * </p>
	 * @param id
	 */
	void removeResourceType(Long id);
	
	/**
	 * <p>
	 * 根据id删除操作选项
	 * </p>
	 * 
	 * @param id
	 */
	void removeOperation(Long id);

	/**
	 * <p>
     * 更新资源类型
     * 连同资源类型根节点ID一起更新
     * 还要更新作为根节点的资源
	 * </p>
	 * @param resourceType
	 * @return
	 */
	Object updateResourceType(ResourceType resourceType);

    /**
     * <p>
     * 保存资源类型
     * 同时还要为该类型资源建立一个根节点
     * 以资源类型名字作为根节点名字
     * </p>
     * @param resourceType
     * @return
     */
	Object createResourceType(ResourceType resourceType);

	/**
	 * <p>
	 * 创建或修改Application信息
	 * </p>
	 * 
	 * @param application
	 */
	void saveApplication(Application application);

	/**
	 * <p>
	 * 创建或修改Operation信息
	 * </p>
	 * 
	 * @param operation
	 */
	void updateOperation(Operation operation);
	
	/**
	 * <p>
	 * 新建权限选项
	 * 创建新的权限选项的时候需要让管理员角色拥有此权限.
     * 约束:
     * 1.用户类型
     * 2.主用户组类型
     * 3.辅助用户组类型
     * 4.其他用户组类型
     * 5.应用系统类型
     * 6.功能菜单类型
     * 7.自注册用户组资源类型
	 * </p>
	 * @param operation
	 * @return
	 */
	Operation saveOperation(Operation operation);
	
	/**
	 * 根据ID查询应用系统
	 * @param id
	 * @return
	 */
	Application getApplicationById(Long id);
 
	/**
	 * <p>
	 * 获取应用系统和资源类型
	 * </p>
	 * @return
	 */
	Object[] findApplicationAndResourceType();
	
	/**
	 * <p>
	 * 根据资源类型的id获取资源
	 * </p>
	 * 
	 * @param userId
	 * @param resourceTypeId
	 * @param applicationId
	 * @return List
	 */
	List<ResourceTreeNode> findResoucrcesByResourceType(String resourceTypeId, String applicationId);
	
	/**
	 * <p>
	 * 根据ID查询资源类型
	 * </p>
	 * 
	 * @param id
	 * @return Object
	 */
	ResourceType getResourceTypeById(Long id);
	
	/**
	 * <p>
	 * 根据ID查询操作选项
	 * </p>
	 * 
	 * @param id
	 * @return Object
	 */
	Operation getOperationById(Long id);

	/**
	 * <p>
	 * 获得一个应用系统的一个资源类型的根节点
	 * </p>
	 * @param applicationId
	 * @param resourceTypeId
	 * @return
	 */
    ResourceTypeRoot findResourceTypeRoot(String applicationId,String resourceTypeId);
    
    @PermissionTag(
            operation = UMConstants.APPLICATION_SORT_OPERRATION, 
            resourceType = UMConstants.APPLICATION_RESOURCE_TYPE_ID,
            filter = PermissionFilter4Sort.class
    )
    void sortApplication(Long appId, Long toAppId, int direction, Long userId);
    
    /**
     * 获得登陆用户可访问的应用系统名称列表
     */
    @PermissionTag(
            operation = UMConstants.APPLICATION_VIEW_OPERRATION, 
            resourceType = UMConstants.APPLICATION_RESOURCE_TYPE_ID
    )
    List<?> getApplications();
}
