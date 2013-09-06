package com.jinhe.tss.framework.exception.convert;

/** 
 * <p>
 * 默认异常转换器：不做任何转换处理
 * </p>
 */
public class DefaultExceptionConvertor implements IExceptionConvertor {

    public Exception convert(Exception exception) {
        return exception;
    }
}
