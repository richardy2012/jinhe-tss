package com.jinhe.tss.um.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.dao.IGroupDao;
import com.jinhe.tss.um.dao.IUserDao;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.helper.dto.GroupDTO;
import com.jinhe.tss.um.helper.dto.OperatorDTO;
import com.jinhe.tss.um.service.ILoginService;
import com.jinhe.tss.util.BeanUtil;

/**
 * <p>
 * 用户登录系统相关业务逻辑处理接口：
 * <li>根据用户登录名获取用户名及认证方式信息等；
 * <li>根据用户ID获取用户信息；
 * <li>根据用户登录名获取用户信息；
 * </p>
 */
public class LoginService implements ILoginService {

	@Autowired private IUserDao userDao;
	@Autowired private IGroupDao groupDao;

	public String[] getLoginInfoByLoginName(String loginName) {
		User user = getUserByLoginName(loginName);
		return new String[] { user.getUserName(), user.getAuthenticateMethod() };
	}
	
	private User getUserByLoginName(String loginName) {
       User user = userDao.getUserByLoginName(loginName);
        if (user == null) {
            throw new BusinessException("此帐号(" + loginName + ")不存在");
        } 
        else if (UMConstants.TRUE.equals(user.getDisabled())) {
            throw new BusinessException("此帐号(" + loginName + ")已被停用");
        } 
        else if (user.getAccountUsefulLife() !=  null) {
            if ( new Date().after(user.getAccountUsefulLife()) ) {
                throw new BusinessException("此帐号(" + loginName + ")已过期");
            }
        }
        return user;
	}

	public OperatorDTO getOperatorDTOByID(Long userId) {
		User user = userDao.getEntity(userId);
		return createOperatorDTO(user);
	}

	public OperatorDTO getOperatorDTOByLoginName(String loginName) {
	    User user = getUserByLoginName(loginName);
	    return createOperatorDTO(user);
	}
	
	/* 拷贝User对象的所有属性到OperatorDTO */
    private OperatorDTO createOperatorDTO(User user) {
        OperatorDTO dto = new OperatorDTO();
        
        // 共有的属性直接拷贝
        BeanUtil.copy(dto, user);

        // 其他用户对象特有的属性全部放到DTO的map里面保存
        Map<String, Object> dtoMap = dto.getAttributesMap();
        Field[] fields = user.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            try {
                dtoMap.put(fieldName, BeanUtil.getPropertyValue(user, fieldName));
            } catch (Exception e) {
            }
        }
        
        return dto;
    }

    @SuppressWarnings("unchecked")
	public List<Object[]> getUserRolesAfterLogin(Long userId) {
        String hql = "select distinct o.id.userId, o.id.roleId from ViewRoleUser o where o.id.userId = ?";
        return (List<Object[]>) userDao.getEntities(hql, userId);
	}

    public List<Long> getRoleIdsByUserId(Long userId) {
        List<Object[]> userRoles = getUserRolesAfterLogin(userId);
        List<Long> roleIds = new ArrayList<Long>();
        for( Object[] urInfo : userRoles ){
            roleIds.add((Long) urInfo[1]);
        }
        return roleIds;
    }

	public Object[] getRootGroupByUserId(Long userId) {
		List<?> list = groupDao.getFatherGroupsByUserId(userId);
		if (list.size() == 0) {
			return null;
		}
		Group group = (Group) list.get(0);
		return new Object[] {group.getId(), group.getName()};
	}

	public Map<Integer, Object[]> getGroupsByUserId(Long userId) {
		List<?> list = groupDao.getFatherGroupsByUserId(userId);
		Map<Integer, Object[]> result = new HashMap<Integer, Object[]>();
		for (int i = 1; i < list.size() + 1; i++) {
			Group group = (Group) list.get(i - 1);
			result.put(i, new Object[] {group.getId(), group.getName()});
		}
		return result;
	}

    public List<GroupDTO> getGroupTreeByGroupId(Long groupId) {
        List<Group> groups = groupDao.getChildrenById(groupId);
        
        List<GroupDTO> returnList = new ArrayList<GroupDTO>();
        for( Group group : groups ){
            GroupDTO dto = new GroupDTO();
            dto.setId(group.getId().toString());
            dto.setName(group.getName());
            dto.setParentId(group.getParentId().toString());
            returnList.add(dto);
        }
        
        return returnList;
    }
    
    public List<OperatorDTO> getUsersByGroupId(Long groupId) {
        List<User> users = groupDao.getUsersByGroupId(groupId);
        return translateUserList2DTO(users);
    }

	public OperatorDTO translateUser(Long userId, String userDepositoryCode) {
		User user = userDao.getEntity(userId);
		if (user == null) {
			throw new BusinessException("没有找到编号ID为(" + userId + ")的用户");
		}
		
		// 不同用户库； 如若相同，则直接返回用户。
		String originalUserDepositoryCode = user.getApplicationId();
		if ( !userDepositoryCode.equals(originalUserDepositoryCode) ) { 
		    // 原用户不为平台用户，则先将给用户转换为平台用户，再有平台用户转换为目标用户库的用户
			if (!UMConstants.TSS_APPLICATION_ID.equals(originalUserDepositoryCode)) {
				user = userDao.getAppUser(userId, UMConstants.TSS_APPLICATION_ID); // 取到平台用户
				if (user == null) {
					throw new BusinessException("用户（" + userId + "）没有对应的平台（"
							+ UMConstants.TSS_APPLICATION_ID + "）用户");
				}
			}
			
		    Long appUserId = user.getId();
            user = userDao.getAppUser(appUserId, userDepositoryCode); // 取到目标用户库的用户
            if (user == null) {
                throw new BusinessException("用户【" + appUserId + "】没有对应的其他用户库【" + userDepositoryCode + "】用户");
            }
		}
		
		return createOperatorDTO(user);
	}

	public void savePassword(Long userId, String password) {
		User user = userDao.getEntity(userId);
		user.setPassword(password);
		userDao.update(user);
	}
    
    @SuppressWarnings("unchecked")
    public List<OperatorDTO> getUsersByRoleId(Long roleId) {
        String hql = "select distinct u from RoleUser ru, User u" +
                " where ru.id.userId = u.id and ru.id.roleId = ? order by u.id";
       
        List<User> data = (List<User>) groupDao.getEntities( hql, roleId );
        return translateUserList2DTO(data);
    }

    @SuppressWarnings("unchecked")
    public List<Group> getGroupsByRoleId(Long roleId) {
        String hql = "select distinct g from RoleGroup rg, Group g" +
                " where rg.groupId = g.id and rg.roleId = ? order by g.decode";
        
        return (List<Group>) groupDao.getEntities( hql, roleId );
    }
    
    private List<OperatorDTO> translateUserList2DTO(List<User> users){
        List<OperatorDTO> returnList = new ArrayList<OperatorDTO>();
        for( User user : users ){
            returnList.add(createOperatorDTO(user));
        }
        return returnList;
    }
}