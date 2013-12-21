package com.jinhe.tss.um.syncdata;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.helper.dto.GroupDTO;
import com.jinhe.tss.um.helper.dto.UserDTO;
import com.jinhe.tss.um.syncdata.dao.IOutDataDao;
import com.jinhe.tss.um.syncdata.dao.LDAPDataDao;
import com.jinhe.tss.um.syncdata.dao.DBDataDao;
import com.jinhe.tss.util.EasyUtils;

public class SyncDataHelper {
    
	public final static String DRIVER = "driver";
	public final static String DEFAULT_PWD = "123456";
    public final static String URL = "url";
    public final static String USERNAME = "user";
    public final static String PASSWORD = "password";
    public final static String SINGLE_USER = "singleUser";
    
    public final static String QUERY_GROUP_SQL_NAME = "groupSql";
    public final static String QUERY_USER_SQL_NAME  = "userSql";
    
    /**
     * <p>
     * 拷贝组DTO到实体对象
     * 只拷贝部分基本属性
     * </p>
     * @param group
     * @param groupDTO
     */
    public static void setGroupByDTO(Group group, GroupDTO groupDTO) {  
        group.setDescription(groupDTO.getDescription());
        group.setName(groupDTO.getName());
        group.setSeqNo(groupDTO.getSeqNo());
        group.setDisabled(groupDTO.getDisabled());
        group.setGroupType(groupDTO.getGroupType());
    }
    
    /**
     * <p>
     * 拷贝用户DTO到实体对象
     * 只拷贝部分基本属性
     * </p>
     * @param group
     * @param groupDTO
     */
    public static void setUserByDTO(User user, UserDTO userDTO) {
        user.setDisabled(userDTO.getDisabled());
        user.setAccountLife(userDTO.getAccountLife());
        user.setBirthday(userDTO.getBirthday());
        user.setEmployeeNo(userDTO.getEmployeeNo());
        user.setLoginName(userDTO.getLoginName());
        user.setPassword(userDTO.getPassword());
        user.setSex(userDTO.getSex());
        user.setUserName(userDTO.getUserName());
        user.setFromUserId(userDTO.getId());
        
        if( EasyUtils.isNullOrEmpty(user.getPassword()) ) {
            user.setPassword(DEFAULT_PWD);
        }
        user.setPasswordQuestion("?");
        user.setPasswordAnswer( System.currentTimeMillis() + "!" );
    }
    
    public static IOutDataDao getOutDataDao(Integer dataSourceType) {
        if (UMConstants.DATA_SOURCE_TYPE_LDAP.equals(dataSourceType)) {
            return new LDAPDataDao();
        }
        
        if (UMConstants.DATA_SOURCE_TYPE_DB.equals(dataSourceType)) {
            return new DBDataDao();
        }
        
        throw new BusinessException("同步数据时设置的数据库类型不匹配");
    }
}

