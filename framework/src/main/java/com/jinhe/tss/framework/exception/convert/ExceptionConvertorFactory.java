package com.jinhe.tss.framework.exception.convert;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.util.BeanUtil;

/**
 * 异常转换器工厂类
 * 
 */
public class ExceptionConvertorFactory {
	
	 /** 异常信息转换器实现类类名属性名 */
    static final String EXCEPTION_CONVERTOR = "class.name.ExceptionConvertor";
    
    static IExceptionConvertor convertor = null;

    public static IExceptionConvertor getConvertor() {
        if (convertor == null) {
            String className = Config.getAttribute(EXCEPTION_CONVERTOR);
            if (className != null) {
                String[] classNames = className.split(",");
                if (classNames.length > 1) {
                    convertor = new ArrayExceptionConvertor(classNames);
                } else {
                    convertor = (IExceptionConvertor) BeanUtil.newInstanceByName(classNames[0]);
                }
            }
            if (convertor == null) {
                convertor = new DefaultExceptionConvertor();
            }
        }
        return convertor;
    }
}