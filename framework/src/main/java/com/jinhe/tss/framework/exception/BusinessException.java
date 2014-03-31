package com.jinhe.tss.framework.exception;

/**
 * 业务逻辑异常。
 *
 */
public class BusinessException extends RuntimeException implements IBusinessException {

    private static final long serialVersionUID = 1759438185530697479L;
 
    public BusinessException(String msg) {
        super(msg);
    }
 
    public BusinessException(String msg, Throwable t) {
        super(msg, t);
    }
 
    public boolean needRelogin() {
        return false;
    }
}
