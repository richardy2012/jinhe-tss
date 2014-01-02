package com.jinhe.tss.um.syncdata;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.um.helper.dto.UserDTO;
import com.jinhe.tss.um.sso.UMPasswordIdentifier;
import com.jinhe.tss.um.syncdata.dao.DBDataDao;
import com.jinhe.tss.um.syncdata.dao.IOutDataDao;
import com.jinhe.tss.um.syncdata.dao.LDAPDataDao;
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
     * 拷贝用户DTO到实体对象
     * 只拷贝部分基本属性
     * </p>
     * @param user
     * @param userDTO
     */
    public static void setUserByDTO(User user, UserDTO userDTO) {
    	user.setLoginName(userDTO.getLoginName());
    	user.setUserName(userDTO.getUserName());
        user.setDisabled(userDTO.getDisabled());
        user.setAccountLife(userDTO.getAccountLife());
        user.setEmail(userDTO.getEmail());
        user.setSex(userDTO.getSex());
        user.setBirthday(userDTO.getBirthday());
        user.setEmployeeNo(userDTO.getEmployeeNo());
        
        if( !EasyUtils.isNullOrEmpty(userDTO.getAuthMethod()) ) {
        	user.setAuthMethod(userDTO.getAuthMethod());
        } else {
        	user.setAuthMethod(UMPasswordIdentifier.class.getName());
        }
        user.setPassword(DEFAULT_PWD);
        user.setPasswordQuestion("?");
        user.setPasswordAnswer( System.currentTimeMillis() + "!" );
        
        user.setFromUserId(userDTO.getId());
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

