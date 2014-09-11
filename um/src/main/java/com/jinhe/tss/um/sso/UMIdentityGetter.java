package com.jinhe.tss.um.sso;

import org.apache.log4j.Logger;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.sso.IOperator;
import com.jinhe.tss.framework.sso.IPWDOperator;
import com.jinhe.tss.framework.sso.IdentityGetter;
import com.jinhe.tss.um.service.ILoginService;

/**
 * <p>
 * UM原始用户身份（未认证）读取器：告诉SSO框架如何读取用户信息。
 * </p>
 */
public class UMIdentityGetter implements IdentityGetter {
	
	protected Logger log = Logger.getLogger(this.getClass());
    
	protected ILoginService service = (ILoginService) Global.getContext().getBean("LoginService");

    /**
	 * <p>
	 * 根据标准用户Id获取用户信息
	 * </p>
	 * @param userId  标准用户Id
	 * @return
	 */
	public IOperator getOperator(Long userId) {
		return service.getOperatorDTOByID(userId);
	}

	public boolean indentify(IPWDOperator operator, String password) {
		return false;
	}
}
