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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * <p>
 * 时间、字符串转换工具
 * </p>
 */
public class DateUtil {
    
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    private static final String SDF_1_REG = "^\\d{2,4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$";

    public static final SimpleDateFormat SDF_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String SDF_2_REG = "^\\d{2,4}\\-\\d{1,2}\\-\\d{1,2}$";

    private static final SimpleDateFormat SDF_2 = new SimpleDateFormat(DEFAULT_DATE_PATTERN);

    private static final String SDF_3_REG = "^\\d{2,4}\\/\\d{1,2}\\/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$";

    private static final SimpleDateFormat SDF_3 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private static final String SDF_4_REG = "^\\d{2,4}\\/\\d{1,2}\\/\\d{1,2}$";

    private static final SimpleDateFormat SDF_4 = new SimpleDateFormat("yyyy/MM/dd");

    /**
     * <p>
     * 将日期字符串解析成日期对象，支持以下格式
     * <li>yyyy-MM-dd HH:mm:ss
     * <li>yyyy-MM-dd
     * <li>yyyy/MM/dd HH:mm:ss
     * <li>yyyy/MM/dd
     * </p>
     * 
     * @param str
     * @return
     */
    public static Date parse(String str) {
    	if(EasyUtils.isNullOrEmpty(str)) return null;
    	
    	if(str.indexOf(".") > 0) {
    		str = str.substring(0, str.indexOf(".")); // 截掉微秒
    	}
    	
        Date date = null;
        try {
            if (Pattern.compile(SDF_1_REG).matcher(str).matches()) {
                date = SDF_1.parse(str);
            } 
            else if (Pattern.compile(SDF_2_REG).matcher(str).matches()) {
                date = SDF_2.parse(str);
            }
            else if (Pattern.compile(SDF_3_REG).matcher(str).matches()){
                date = SDF_3.parse(str);
            }
            else if(Pattern.compile(SDF_4_REG).matcher(str).matches()){
                date = SDF_4.parse(str);
            }
        } catch (ParseException e) {
            throw new RuntimeException("非法日期字符串，解析失败：" + str, e);
        }
        return date;
    }

    /**
     * <p>
     * 将日期格式化成字符串：yyyy-MM-dd
     * </p>
     * 
     * @param date
     * @return
     */
    public static String format(Date date) {
        if(date == null) return "";
        
        return new SimpleDateFormat(DateUtil.DEFAULT_DATE_PATTERN).format(date);
    }
    
    /**
     * <p>
     * 将日期格式化成字符串：yyyy-MM-dd HH:mm:ss
     * </p>
     * 
     * @param date
     * @return
     */
    public static String formatCare2Second(Date date) {
        if(date == null) return "";
        
        return SDF_1.format(date);
    }

    /**
     * <p>
     * 将日期格式化成相应格式的字符串，如：
     * <li>yyyy-MM-dd HH:mm:ss
     * <li>yyyy-MM-dd
     * <li>yyyy/MM/dd HH:mm:ss
     * <li>yyyy/MM/dd
     * </p>
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        if(date == null) return "";
        
        if (pattern == null || "".equals(pattern)) {
            pattern = DateUtil.DEFAULT_DATE_PATTERN; 
        }
        
        return new SimpleDateFormat(pattern).format(date);
    }
}
