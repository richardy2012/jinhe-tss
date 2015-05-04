package com.jinhe.dm.record.ddl;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import com.jinhe.tss.util.DateUtil;
import com.jinhe.tss.util.EasyUtils;

public class _Util {
	
	static Logger log = Logger.getLogger(_Util.class);
	
  	public static Object preTreatValue(String value, Object type) {
  		if(type == null || value == null) {
  			return value;
  		}
  		
  		type = type.toString().toLowerCase();
  		if("number".equals(type)) {
  			if(value.indexOf(".") > 0) {
  				return EasyUtils.obj2Double(value);
  			}
  			return EasyUtils.obj2Int(value);
  		}
  		else if("date".equals(type) || "datetime".equals(type)) {
			try {
				Date dateObj = DateUtil.parse(value);
				return new Timestamp(dateObj.getTime());
			} catch(Exception e) {
				log.error("Date type param'value【" + value + "】  is wrong. " + e.getMessage());
				return null;
			}
  		}
  		else {
  			return value;
  		}
  	} 
  	
  	// oracle的TIMESTAMP类型的字段，转换为json时会报错，需要先转换为字符串
  	public static Object preTreatValue(Object value) {
  		if(value == null) return null;
  				
  		String valueCN = value.getClass().getName();
		if(valueCN.indexOf("oracle.sql.TIMESTAMP") >=0 ) {
  			return value.toString();
  		}
  		return value;
  	}
}
