package com.jinhe.tss.framework.exception;

import javax.servlet.ServletException;

/**
 * 业务逻辑异常（用于web层抛出异常）。
 * 
 */
public class BusinessServletException extends ServletException implements IBusinessException {

    private static final long serialVersionUID = -2924685196019749948L;

    /**
     * 是否需重新登录系统:
     *  0-无需登录；
     *  1-需要重新登录平台；
     *  2-需要重新输入密码，登录其他系统
	 */
    private int relogin = 0;
    
    /**
     * 是否需显示错误信息给用户看
	 */
    private boolean popup = true; 
 
    public BusinessServletException(Exception e) {
        super(e.getMessage());
        initCause(e);
        if (e instanceof IBusinessException) {
            IBusinessException be = (IBusinessException) e;
            relogin = be.getRelogin();
            popup = be.isPopup();
        }
    }
 
    public BusinessServletException(Exception e, boolean relogin) {
        super(e.getMessage());
        initCause(e);
        if (relogin) {
            this.relogin = 1;
        }
        if (e instanceof IBusinessException) {
            IBusinessException be = (IBusinessException) e;
            popup = be.isPopup();
        }
    }

    /**
     * @param string 描述信息
     */
    public BusinessServletException(String msg) {
        super(msg);
    }

    /**
     * @param msg 描述信息
     * @param url 信息提示后返回的路径
     */
    public BusinessServletException(String msg, boolean relogin) {
        super(msg);
        if (relogin) {
            this.relogin = 1;
        }
    }

    /**
     * @param msg 描述信息
     * @param url 信息提示后返回的路径
     */
    public BusinessServletException(String msg, int relogin) {
        super(msg);
        this.relogin = relogin;
    }

    /**
     * @param msg 描述信息
     * @param t 异常原因
     */
    public BusinessServletException(String msg, Throwable t) {
        super(msg);
        initCause(t);
        if (t instanceof IBusinessException) {
            IBusinessException be = (IBusinessException) t;
            relogin = be.getRelogin();
        }
    }

    /**
     * @param msg 描述信息
     * @param t 异常原因
     * @param url 信息提示后返回的路径
     */
    public BusinessServletException(String msg, Throwable t, boolean relogin) {
        super(msg);
        initCause(t);
        if (relogin) {
            this.relogin = 1;
        }
        if (t instanceof IBusinessException) {
            IBusinessException be = (IBusinessException) t;
            popup = be.isPopup();
        }
    }

    public boolean isPopup() {
        return this.popup;
    }

    public int getRelogin() {
        return this.relogin;
    }
}
