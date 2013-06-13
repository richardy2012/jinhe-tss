package com.jinhe.tss.framework.component.param;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.persistence.connpool._Connection;

/**
 *  调用参数管理功能入口
 */
public class ParamManager {

	private static Map<String, Object> paramMap = new HashMap<String, Object>();
	
	public  static Map<String, String> valueMap = new HashMap<String, String>();
    
    private static ParamService getService() {
        return (ParamService) Global.getContext().getBean("ParamService");
    }
    
    /**
     * 获取简单类型参数
     * @param code
     * @return
     */
    public static Param getSimpleParam(String code){
    	try{
            Param param = (Param) paramMap.get(code);
    		if(param == null) {
    			paramMap.put(code, param = getService().getParam(code));
    		}
            return param;
    	} catch (ClassCastException e) {
			throw new BusinessException("获取参数信息失败，指定的code：" + code + " 不是简单型参数!");
		}
    }
    
    /**
     * 获取下拉类型参数列表
     * @param code
     * @return
     */
    @SuppressWarnings("unchecked")
	public static List<Param> getComboParam(String code){
    	try{
    		List<Param> list = (List<Param>) paramMap.get(code);
    		if(list == null) {
    			paramMap.put(code, list = getService().getComboParam(code));
    		}
            return list;
            
    	}catch (ClassCastException e) {
    		throw new BusinessException("获取参数信息失败，指定的code：" + code + " 不是下拉型参数!");
		}
    }
    
    /**
     * 获取树型类型参数列表
     * @param code
     * @return
     */
    @SuppressWarnings("unchecked")
	public static List<Param> getTreeParam(String code){
    	try{
            List<Param> list = (List<Param>) paramMap.get(code);
    		if( list == null ) {
    			paramMap.put(code, list = getService().getTreeParam(code));
    		}
            return list;
            
    	} catch (ClassCastException e) {
    		throw new BusinessException("获取参数信息失败，指定的code：" + code + " 不是树型参数!");
		}
    }
    
    /**
     * 根据参数Code读取参数值
     * @param code
     * @return
     */
    public static String getValue(String code){
    	String value = (String)valueMap.get(code);
    	if(value == null){
    		Param param = (Param)getService().getParam(code);
            if(param == null) { 
                throw new BusinessException("code:" + code + " 的参数没有被创建");
            }
            
    		valueMap.put(code, value = param.getValue());
    	} 
    	return value;
    }
    
    public static String getValueNoSpring(String code){
        String value = (String)valueMap.get(code);
        if(value == null){
            String sql = "select p.value from tbl_param p where p.type = " + ParamConstants.NORMAL_PARAM_TYPE
                       + " and p.code='" + code + "' and p.hidden <> 1 and p.disabled <> 1";
            
            Connection conn = _Connection.getInstanse().getConnection();
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    value = rs.getString("value");
                    break;
                } 
                if(value == null){
                    throw new BusinessException("code:" + code + " 的参数没有被创建");
                }
                valueMap.put(code, value);
            } catch(Exception e){
                throw new BusinessException("读取code:" + code + " 的参数出错", e);
            } finally {
                _Connection.getInstanse().releaseConnection(conn);
            }
        }
        return value;
    }
	
    /**
     * 清除所有系统参数缓存信息
     */
    public static void removeAll(){
    	paramMap.clear();
        valueMap.clear();
    }

    /**
     * <p>
     * 根据名称清除参数缓存信息
     * </p>
	 * @param name 参数名
	 * @return
	 */
    public static void remove(String code){
    	paramMap.remove(code);
        valueMap.remove(code);
    }
}

	