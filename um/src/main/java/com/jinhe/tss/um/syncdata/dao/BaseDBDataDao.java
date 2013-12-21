package com.jinhe.tss.um.syncdata.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.um.helper.dto.GroupDTO;
import com.jinhe.tss.um.helper.dto.UserDTO;
import com.jinhe.tss.um.syncdata.SyncDataHelper;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.EasyUtils;
 
public abstract class BaseDBDataDao implements IOutDataDao {
    
	// 要求SQL的字段别名 和 DTO里的属性名一致
    protected static String[] groupDtoPropertyNames = new String[]{"id", "parentId", "name", "description"};
    protected static String[] userDtoPropertyNames  = new String[]{"id", "groupId", "loginName", "userName", "sex", "birthday", "email", "employeeNo" };
    
    public List<?> getOtherGroups(Map<String, String> paramsMap, String sql, String groupId) {
        if( sql == null ) {
            return new ArrayList<Object>();
        }
        sql = sql.replaceAll(":groupId", groupId);
        
        Connection conn = getConnection(paramsMap);
        return getDtosBySQL(conn, sql, groupDtoPropertyNames, GroupDTO.class);
    }

    public List<?> getOtherUsers(Map<String, String> paramsMap, String sql, String groupId, Object...otherParams) {
        if( EasyUtils.isNullOrEmpty(sql) ) { 
        	return new ArrayList<Object>();
        }
        sql = sql.replaceAll(":groupId", groupId);
 
        Connection conn = getConnection(paramsMap);
        return getDtosBySQL(conn, sql, userDtoPropertyNames, UserDTO.class);
    }
    
    public UserDTO getUser(Map<String, String> paramsMap, String fromUserId){
        String sql =  paramsMap.get(SyncDataHelper.SINGLE_USER);
        sql = sql.replaceAll(":userId", fromUserId);

        Connection conn = getConnection(paramsMap);
        List<?> list = getDtosBySQL(conn, sql, userDtoPropertyNames, UserDTO.class);
        
        if( EasyUtils.isNullOrEmpty(list) ) {
            return null;
        }
        
        return (UserDTO)list.get(0);
    }
    
    protected abstract Connection getConnection(Map<String, String> paramsMap);
    
    protected List<?> getDtosBySQL(Connection conn, String sql, String[] dtoPropertyNames, Class<?> clazz) {
        List<Object> items = new ArrayList<Object>();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Object dto = BeanUtil.newInstance(clazz);
                Map<String, String> attrsMap = new HashMap<String, String>();
                for(int i = 0; i < dtoPropertyNames.length; i++){
                    Object value = rs.getObject(dtoPropertyNames[i]);
                    attrsMap.put(dtoPropertyNames[i], value == null ? null : value.toString());
                }
                BeanUtil.setDataToBean(dto, attrsMap);
                items.add(dto);
            }
            if( null != rs ){
                rs.close();   
            }
            if( null != stmt ){
                stmt.close();   
            }
        } catch (SQLException e) {
            throw new BusinessException("数据查询错误！", e);
        } finally {
            try {
                if(null != conn){
                    conn.close();   
                    conn = null;                
                }
            } catch (SQLException e) {
                throw new BusinessException("关闭数据库连接失败！", e);
            }
        }
        return items;
    }
}