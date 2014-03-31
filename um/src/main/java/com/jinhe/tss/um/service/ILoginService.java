package com.jinhe.tss.um.service;

import java.util.List;
import java.util.Map;

import com.jinhe.tss.framework.component.log.Logable;
import com.jinhe.tss.um.helper.dto.GroupDTO;
import com.jinhe.tss.um.helper.dto.OperatorDTO;

/**
 * <p>
 * 用户登录系统相关业务逻辑处理接口：
 * <li>根据用户登录名获取用户名及认证方式信息等；
 * <li>根据用户ID获取用户信息；
 * <li>根据用户登录名获取用户信息；
 * </p>
 */
public interface ILoginService {

    /**
     * <p>
     * 根据用户登录名获取用户名及身份认证器类名
     * </p>
     * @param loginName 用户登录名
     * @return String[] {用户名:String,身份认证器类名（全路径）:String}
     */
	@Logable(operateObject = "用户登录", operateInfo = "${args[0]}正在进行登录验证。")
    String[] getLoginInfoByLoginName(String loginName);
    
    /**
     * <p>
     * 根据用户ID获取用户信息
     * </p>
     * @param id 用户ID
     * @return OperatorDTO 用户信息DTO
     */
    OperatorDTO getOperatorDTOByID(Long id);
    
    /**
     * <p>
     * 根据用户登录名获取用户信息
     * </p>
     * @param loginName 用户登录名
     * @return OperatorDTO 用户信息DTO
     */
    OperatorDTO getOperatorDTOByLoginName(String loginName);
    
    /**
     * <p>
     * 登陆成功后
     * 获取登陆用户的所有的角色列表。
     * </p>
     * @param userId
     * 			用户ID
     * @return
     * 		登陆用户拥有的所有权限 List(Object[]{userId, roleId})
     */
    List<Object[]> getUserRolesAfterLogin(Long userId);
    
    /**
     * <p>
     * 获取登陆用户的所有的角色列表
     * </p>
     * @param userId
     *          用户ID
     * @return
     *      登陆用户拥有的所有权限 List(roleId)
     */
    List<Long> getRoleIdsByUserId(Long userId);
    
	/**
	 * <p>
	 * 根据主用户找到所在组的最外层节点
	 * 不包括主用户组(-2)这个节点
	 * </p>
	 * @param userId
	 * @return
	 * 		Object obj[0]=groupId, obj[1]=groupName
	 */
    Object[] getRootGroupByUserId(Long userId);

    /**
     * <p>
     * 根据用户获取用户所在组织关系
     * </p>
     * @param userId
     * @return
     * 		Map:
     * 		key = Integer(x) / value = Object[](groupId, groupName)
     *		层次是从上向下,依次类推
     */
    Map<Integer, Object[]> getGroupsByUserId(Long userId);

    /**
     * <p>
     * 根据组id获取该组下的儿子结点。（供远程调用，需要转换成GroupDTO，Group只限于TSS使用）
     * 其它基于TSS的应用需要取部门列表的话可以采用本方法获取。
     * </p>
     * @param groupId
     * @return
     */
    List<GroupDTO> getGroupTreeByGroupId(Long groupId);
    
    /**
     * 取组（不包含子组）下的用户列表，转换为OperatorDTO对象列表。
     * @param groupId
     * @return
     */
    List<OperatorDTO> getUsersByGroupId(Long groupId);
 
    /**
     * 根据角色的ID 获取拥有此角色的用户
     * 注：需要同时取出“转授”关联起来的RoleUser。
     * @param roleId
     * @return List
     */
    List<OperatorDTO> getUsersByRoleId(Long roleId);
}

