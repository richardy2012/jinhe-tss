package com.jinhe.tss.um.sso;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.sso.IOperator;
import com.jinhe.tss.framework.sso.IdentityTranslator;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.um.service.ILoginService;

/**
 * <p>
 * UM用户身份转换器：根据UMS系统维护的多应用用户映射关系，进行不同应用间用户身份信息转换
 * </p>
 */
public class UMIdentityTranslator implements IdentityTranslator {
    
    ILoginService service = (ILoginService) Global.getContext().getBean("LoginService");

    /**
	 * <p>
	 * 根据标准用户Id获取用户信息
	 * </p>
	 * @param standardUserId  标准用户Id
	 * @return
	 */
	public IOperator translate(Long standardUserId) {
		String userDepositoryCode = Context.getApplicationContext().getUserDepositoryCode();
		return service.translateUser(standardUserId, userDepositoryCode);
	}
}
