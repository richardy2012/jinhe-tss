package com.jinhe.tss.um.service;

import java.util.List;
import java.util.Map;

import com.jinhe.tss.framework.component.log.Logable;
import com.jinhe.tss.framework.persistence.pagequery.PageInfo;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.helper.UMQueryCondition;
 
public interface IUserService {
    
    /**
     * <p>
     * 新建用户的页面需要的初始化数据
     * </p>
     * @param groupId
     * @return
     */
    Map<String, Object> getInfo4CreateNewUser(Long groupId);

    /**
     * <p>
     * 编辑用户的页面需要的初始化数据
     * </p>
     * @param userId
     * @return
     */
    Map<String, Object> getInfo4UpdateExsitUser(Long userId);
    
    /**
     * 更改用户
     * @param user
     */
    @Logable(operateTable="用户", operateType="修改", operateInfo="修改用户 ${args[0]}")
    void updateUser(User user);
    
    /**
     * <p>
     * 设置用户的密码策略
     * </p>
     * @param userId
     * @param ruleId
     */
    void updateUserPasswordRule(Long userId, Long ruleId);

    /**
     * 根据ID查询用户
     * @param id
     * @return Object
     */
    User getUserById(Long id);

    /**
     * <p>
     * 新建/修改一个User对象的明细信息、用户对用户组信息、用户对角色的信息
     * </p>
     * @param mainGroupId
     *            用户必须对应一个主用户组
     * @param user
     * @param groupIdsStr
     * @param roleIdsStr
     */
    @Logable(operateTable="用户", operateType="新建/修改", 
            operateInfo="新建/修改了 ${args[1]} 用户（用户对用户组信息 ${args[2]}、用户对角色的信息 ${args[3]?default(\"\")}）"
        )
    void createOrUpdateUserInfo(Long mainGroupId, User user, String groupIdsStr, String roleIdsStr);

    /**
     * <p>
     * 删除用户 辅助组用户删除用户要删除关系表 辅助组以外的用户只删除用户,不删除关系表
     * </p>
     * @param groupId
     * @param userId
     * @param groupType
     */
    @Logable(operateTable="用户", operateType="删除", 
            operateInfo="删除了 (ID:${args[1]}) 用户"
        )
    void deleteUser(Long groupId, Long userId, Integer groupType);

    /**
     * <p>
     * 自动映射用户
     * </p>
     * @param groupId
     *            其他应用组Id
     * @param toGroupId
     *            主用户组Id
     * @param mappingColumn
     *            对应字段 1-按照登陆帐号对应 2-按照工号对应 3-按照证件号码对应 4-按照姓名对应
     * @param mode 
     * 			  对应方式 0-对应当前组 1-同时对应子组
     */
    void editAutoMappingInfo(Long groupId, Long toGroupId, Integer mappingColumn, Integer mode);

    /**
     * <p>
     * 用户密码统一初始化
     * </p>
     * 
     * @param groupId
     *            用户组Id
     * @param initPassword
     *            初始化的用户密码
     */
    @Logable(operateTable="用户", operateType="初始化密码", 
            operateInfo="初始化组（ID:${args[0]}）下的密码为：${args[1]}"
        )
    void initPasswordByGroupId(Long groupId, String initPassword);

    /**
     * <p>
     * 统一认证方式
     * </p>
     * @param groupId
     * @param authenticateMethod
     */
    void uniteAuthenticateMethod(Long groupId, String authenticateMethod);

    /**
     * <p>
     * 根据用户登录名获取用户实体
     * </p>
     * @param loginName
     *            登录名
     * @return User 用户实体对象
     */
    User getUserByLoginName(String loginName);

    /**
     * <p>
     * 用户自注册
     * </p>
     * @param user
     */
    @Logable(operateTable="用户", operateType="注册", 
            operateInfo=" 用户（${args[0]}）完成注册。"
        )
    void registerUser(User user);
    
    /**
     * <p>
     * 根据组的Id取的该组手动映射的信息
     * </p>
     * @param groupId
     * @return List
     */
    List<User> getManualMappingInfo(Long groupId);

    /**
     * <p>
     * 编辑手动映射的用户
     * </p>
     * @param userId
     *            需要对应的用户
     * @param appUserId
     * 			  对应的主用户组的用户
     */
    void editManualMappingInfo(Long userId, Long appUserId, String applicationId);

    /**
     * <p>
     * 移动用户。可用于用户更换部门等场景使用。
     * 其他用户组的用户暂时不允许移动
     * </p>
     * @param groupId
     * @param toGroupId
     * @param userId
     */
    @Logable(operateTable="用户", operateType="移动", 
            operateInfo=" 移动(ID: ${args[0]}) 用户组中的（ID：${args[2]}）用户至 (ID: ${args[1]}) 用户组 "
        )
    void moveUser(Long groupId, Long toGroupId, Long userId);

    /**
     * <p>
     * 导入用户到
     * 其他用户组的用户导入到主用户组，并形成对应关系
     * </p>
     * @param groupId
     * @param toGroupId
     * @param userId
     */
    @Logable(operateTable="用户", operateType="导入", 
            operateInfo=" 导入(ID: ${args[0]}) 其它用户组中的（ID：${args[2]}）用户至 (ID: ${args[1]}) 主用户组 "
        )
    void importUser(Long groupId, Long toGroupId, Long userId);

    /**
     * <p>
     * 启用停用用户
     * </p>
     * @param loginUserId
     * @param userId
     * @param accountState
     * @param groupId
     */
    @Logable(operateTable="用户", operateType="启用/停用", 
            operateInfo=" 启用/停用用户 (ID: ${args[1]}) ${args[2]} "
        )
    void startOrStopUser(Long userId, Integer accountState, Long groupId);

	/**
	 * 处理过期用户
	 */
	void overdue();
	
    /**
     * <p>
     * 根据用户组ID获取所有的用户
     * </p>
     * @param groupId
     * @param pageNum
     *            当前页数
     * @return
     */
    PageInfo getUsersByGroupId(Long groupId, Integer pageNum);

    /**
     * <p>
     * 根据用户组ID获取所有的用户
     * </p>
     * @param groupId
     * @param pageNum
     *            当前页数
     * @param fields
     * @param orderType
     * @return
     */
    PageInfo getUsersByGroupId(Long groupId, Integer pageNum, String orderBy);

    /**
     * 根据用户组ID获取所有的用户(不分页)
     * @param groupId
     * @param groupType
     * @return
     */
    List<User> getUsersByGroup(Long groupId);
    
    /**
     * <p>
     * 根据条件搜索用户(分页)
     * </p>
     * @param qyCondition
     * @param userId
     * @param pageNum
     * @return
     */
    PageInfo searchUser(UMQueryCondition qyCondition, Integer pageNum);

}
