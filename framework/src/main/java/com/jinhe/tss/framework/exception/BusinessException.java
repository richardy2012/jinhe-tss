package com.jinhe.tss.framework.exception;

/**
 * 业务逻辑异常。
 *
 */
public class BusinessException extends RuntimeException
        implements IBusinessException {

    private static final long serialVersionUID = 1759438185530697479L;

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
    
    /**
     * @param string 描述信息
     */
    public BusinessException(String msg) {
        super(msg);
    }

    /**
     * @param msg 描述信息
     * @param url 信息提示后返回的路径
     */
    public BusinessException(String msg, boolean relogin) {
        super(msg);
        if (relogin) {
            this.relogin = 1;
        }
    }

    /**
     * @param msg 描述信息
     * @param url 信息提示后返回的路径
     */
    public BusinessException(String msg, int relogin) {
        super(msg);
        this.relogin = relogin;
    }

    /**
     * @param msg 描述信息
     * @param t 异常原因
     */
    public BusinessException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * @param msg 描述信息
     * @param t 异常原因
     * @param url 信息提示后返回的路径
     */
    public BusinessException(String msg, Throwable t, boolean relogin) {
        super(msg, t);
        if (relogin) {
            this.relogin = 1;
        }
    }
 
    public int getRelogin() {
        return relogin;
    }

    public boolean isPopup() {
        return popup;
    }
}
