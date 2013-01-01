package com.jinhe.tss.framework.exception;

/** 
 * 业务逻辑异常
 */
public interface IBusinessException {
    
	/**
     * 是否需显示错误信息给用户看
     * 
	 * @return 
	 */
	public boolean isPopup();

	/**
     * 是否需重新登录系统:
     *  0-无需登录；
     *  1-需要重新登录平台；
     *  2-需要重新输入密码，登录其他系统
     *  
	 * @return 
	 */
	public int getRelogin();
}
