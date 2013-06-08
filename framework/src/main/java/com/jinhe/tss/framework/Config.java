package com.jinhe.tss.framework;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

/** 
 * <p> 应用配置 </p> 
 * 
 *  配置文件默认放在config/resources下的application.properties文件。<br>
 *  支持一个参数对应多个值（多个值之间用“，”号隔开）。
 */
public class Config {
	
	public static final String TRUE = "true";
	public static final String FALSE = "false";

	/** 配置文件中应用编号属性名：系统获取Code值，如TSS、CMS */
	public static final String APPLICATION_CODE = "application.code";

	/** Spring配置文件， 默认为spring/applicationContext.xml */
	public static final String SPRING_CONTEXT_PATH = "aplication.context";

	/** 系统自定义异常处理servlet属性名（仅限普通HTTP请求，即HTML异常处理模式） */
	public static final String ERROR_HANDLE = "error.handle.servlet";

	/** session过期时间的配置名称 */
	public static final String SESSION_CYCLELIFE_CONFIG = "session.cyclelife";
	
    private static ResourceBundle resources = null;
    private static Map<String, Object> propertyMap = new HashMap<String, Object>(); //存放属性 name/value
    
    private static void getBundle(){
        if(resources == null){
            resources = ResourceBundle.getBundle("application", Locale.getDefault());
        }
    }
    
    /**
     * 获取配置参数
     * @param name
     * @return
     */
    public static String getAttribute(String name){
        String value = (String) propertyMap.get(name);
        if(value == null){
            getBundle();
            if(resources != null){
            	try{
            		value = resources.getString(name);
            	}catch(MissingResourceException exception){
            		return null;
            	}
            }
        }
        return value;
    }

    /**
     * 获取参数Set，适合一个参数有多个值的情况下使用（多个值之间用“,”号隔开）
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Set<String> getAttributesSet(String name){
        Set<String> set = (Set<String>) propertyMap.get(name);
        if(set == null){
            set = new HashSet<String>();
            getBundle();
            try{
	            String valueStr = resources.getString(name);
	            String[] values = new String[0];
	            if(valueStr != null)
	                values = valueStr.split(",");
	            
                for(int i = 0; i < values.length; i++)
                    set.add(values[i]);
                
	            propertyMap.put(name, set);
            }catch(MissingResourceException exception){
            	return null;
            }
        }
        return set;
    }
    
    /**
     * 清楚系统参数缓存信息
     */
    public static void remove(){
        resources = null;
        propertyMap.clear();
    }
    
    static String dbDriverName = Config.getAttribute("db.connection.driver_class");
    
    public static boolean isH2Database() {
    	return dbDriverName != null && dbDriverName.indexOf("h2") >= 0;
    }
    
    public static boolean isMysqlDatabase() {
        return dbDriverName != null && dbDriverName.indexOf("mysql") >= 0;
    }
    
    public static boolean isOracleDatabase() {
        return dbDriverName != null && dbDriverName.indexOf("oracle") >= 0;
    }
}
