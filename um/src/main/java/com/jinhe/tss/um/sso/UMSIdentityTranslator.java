package com.jinhe.tss.um.sso;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.sso.IOperator;
import com.jinhe.tss.framework.sso.IdentityTranslator;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.um.service.ILoginService;

/**
 * <p>
 * UMS用户身份转换器：根据UMS系统维护的多应用用户映射关系，进行不同应用间用户身份信息转换
 * </p>
 */
public class UMSIdentityTranslator implements IdentityTranslator {
    
    ILoginService service = (ILoginService) Global.getContext().getBean("LoginService");

	public IOperator translate(Long standardUserId) {
		String userDepositoryCode = Context.getApplicationContext().getUserDepositoryCode();
		return getOperator(standardUserId, userDepositoryCode);
	}

	public IOperator translate(Long standardUserId, String targetAppCode) {
		String userDepositoryCode = Context.getApplicationContext().getUserDepositoryCodeByAppCode(targetAppCode);
		return getOperator(standardUserId, userDepositoryCode);
	}

	/**
	 * <p>
	 * 根据标准用户Id及用户库Code获取用户信息
	 * </p>
	 * @param standardUserId 
	 *                     标准用户Id
	 * @param userDepositoryCode  
	 *                     用户库Code
	 * @return
	 */
    private IOperator getOperator(Long standardUserId, String userDepositoryCode) {
        return service.translateUser(standardUserId, userDepositoryCode);
    }

	public void savePassword(Long userId, String password) {
		service.savePassword(userId, password);
	}
}
