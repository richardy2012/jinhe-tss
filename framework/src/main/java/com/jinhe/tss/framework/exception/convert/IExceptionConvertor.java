package com.jinhe.tss.framework.exception.convert;

/**
 * 异常变换处理器：可以对特殊异常进行自定义信息处理等操作
 * 
 */
public interface IExceptionConvertor {

    public Exception convert(Exception be);

}
