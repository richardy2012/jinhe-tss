package com.jinhe.tss.framework.exception.convert;

import java.util.ArrayList;
import java.util.List;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.EasyUtils;

/**
 * 异常转换器工厂类
 * 
 */
public class ExceptionConvertorFactory {
	
	 /** 异常信息转换器实现类类名属性名 */
    public static final String EXCEPTION_CONVERTOR = "class.name.ExceptionConvertor";
    
    private static IExceptionConvertor convertor = null;

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

/** 
 * <p>
 * 默认异常转换器：不做任何转换处理
 * </p>
 */
class DefaultExceptionConvertor implements IExceptionConvertor {

    public Exception convert(Exception exception) {
        return exception;
    }
}

/** 
 * <p>
 * 列表异常转换器：处理多异常转换器
 * </p>
 */
class ArrayExceptionConvertor implements IExceptionConvertor {

    /**
     * 转换器列表
     */
    private List<Object> convertors;

    /**
     * 转换器器类名数组
     */
    private String[] classNames;

    /**
     * 构造函数
     * @param classNames
     */
    public ArrayExceptionConvertor(String[] classNames) {
        this.classNames = classNames;
    }
 
    public Exception convert(Exception exception) {
        if (convertors == null) {
            init();
        }
        for (Object item : convertors) {
            if (item != null && item instanceof IExceptionConvertor) {
                IExceptionConvertor convertor = (IExceptionConvertor) item;
                Exception e = convertor.convert(exception);
                if (e != exception) {
                    return e;
                }
            }
        }
        return exception;
    }

    /**
     * <p>
     * 初始化异常转换器列表
     * </p>
     */
    private void init() {
        if (classNames == null) return;
        
        convertors = new ArrayList<Object>();
        for (int i = 0; i < classNames.length; i++) {
            String className = classNames[i];
            if (  !EasyUtils.isNullOrEmpty(className) ) {
                Object convertor = BeanUtil.newInstanceByName(className);
                if (convertor != null) {
                    convertors.add(convertor);
                }
            }
        }
    }

}
