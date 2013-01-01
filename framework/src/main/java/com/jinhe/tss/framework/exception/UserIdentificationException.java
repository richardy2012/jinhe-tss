/* ==================================================================   
 * Created [2009-4-27 下午11:32:55] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2009-2012 
 * ================================================================== 
 */

package com.jinhe.tss.framework.exception;

/**
 * <p>
 * 用户身份识别类业务逻辑错误
 * </p>
 */
public class UserIdentificationException extends Exception {

    private static final long serialVersionUID = 9037464113181664556L;
 
    public UserIdentificationException() {
        super();
    }
 
    public UserIdentificationException(String message, Throwable cause) {
        super(message, cause);
    }
 
    public UserIdentificationException(String message) {
        super(message);
    }
 
    public UserIdentificationException(Throwable cause) {
        super(cause);
    }

}
