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
 
	private static final long serialVersionUID = -4729575896596213410L;

    public UserIdentificationException() {
        super();
    }
    
	public UserIdentificationException(String message) {
        super(message);
    }
}
