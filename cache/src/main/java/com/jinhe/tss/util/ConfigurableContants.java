/* ==================================================================   
 * Created [2006-6-19] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2012-2015 
 * ================================================================== 
*/
package com.jinhe.tss.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * <p>
 * 可用Properties文件配置的Contants基类
 * <p/>
 * 子类可如下编写
 * <pre>
 * public class Constants extends ConfigurableContants {
 *   static {
 *     init("JFramework.properties");
 *   }
 * }
 * <p/>
 * public final static String ERROR_BUNDLE_KEY = getProperty("constant.error_bundle_key", "errors"); }
 * </pre>
 * 
 * <p> ConfigurableContants.java </p> 
 * 
 * @author Jon.King 2006-6-19
 *
 */
public abstract class ConfigurableContants {
	
    protected static Logger log = Logger.getLogger(ConfigurableContants.class);
    
    protected static Properties properties = new Properties();
    
    static {
        // 默认载入application.properties
        URL propertiesFile = URLUtil.getResourceFileUrl("application.properties");
        init(propertiesFile.getFile());
    }

    protected static void init(String propertyFilePath) {
        InputStream in = null;
        try {
            in = new FileInputStream(propertyFilePath);
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            log.error("load " + propertyFilePath + " into Contants error", e);
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    protected static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    protected static String getProperty(String key) {
        return properties.getProperty(key);
    }
}


