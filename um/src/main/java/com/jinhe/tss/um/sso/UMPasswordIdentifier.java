package com.jinhe.tss.um.sso;

import org.apache.log4j.Logger;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.sso.IOperator;
import com.jinhe.tss.framework.sso.IPWDOperator;
import com.jinhe.tss.framework.sso.IdentityGetter;
import com.jinhe.tss.framework.sso.IdentityGetterFactory;
import com.jinhe.tss.framework.sso.PasswordPassport;
import com.jinhe.tss.framework.sso.identifier.BaseUserIdentifier;
import com.jinhe.tss.um.service.ILoginService;
import com.jinhe.tss.util.InfoEncoder;

/**
 * <p>
 * UM本地用户密码身份认证器<br>
 * 根据用户帐号、密码等信息，通过UM本地数据库进行身份认证
 * </p>
 */
public class UMPasswordIdentifier extends BaseUserIdentifier {
    
    protected Logger log = Logger.getLogger(this.getClass());
    
    ILoginService loginservice = (ILoginService) Global.getBean("LoginService");
    
    /** 对用户密码进行加密 */
    public static String encodePassword(String loginName, String password) {
    	return InfoEncoder.string2MD5(loginName + "_" + password);
    }
    
    protected IOperator validate() throws BusinessException {
        PasswordPassport passport = new PasswordPassport();
        String loginName = passport.getLoginName();
        String password = passport.getPassword();
        
        IPWDOperator operator;
		try {
            operator = loginservice.getOperatorDTOByLoginName(loginName); // mysql 不区分大小写
        } catch (BusinessException e) {
        	throw new BusinessException(e.getMessage(), false);
        }
		
		int errorCount = loginservice.checkPwdErrorCount(loginName);
       
		String md5Password1 = encodePassword(loginName, password);
		String md5Password2 = encodePassword(loginName.toUpperCase(), password); // 转换成大写再次尝试
		String md5Password3 = encodePassword(loginName.toLowerCase(), password); // 转换成小写再次尝试
        String md5Password0 = operator.getPassword();
        
        // 如果各种验证都不通过
		if ( !md5Password1.equals(md5Password0) 
				&& !md5Password2.equals(md5Password0)
				&& !md5Password3.equals(md5Password0)
				&& !customizeValidate(operator, password)) {
			
			// 记录密码连续输入错误的次数，超过10次将禁止登陆10分钟
			loginservice.recordPwdErrorCount(loginName, errorCount);
			errorCount++;
			String notice = (errorCount == 10 ? "您已累积输错了10次，账号已被锁定，请在10分钟后再尝试登录。" 
					: "连续输错10次账号将被锁定，您还可以尝试" +(10-errorCount)+ "次");
			
			log.info("【" + loginName + ", " + password + "】密码有误，已输错" + errorCount + "次");
			throw new BusinessException("密码输入有误，" + notice, false);
        }
        
    	return operator;
    }
    
    /*
     *  判断用户输入的密码是否和第三方系统的密码的一致，如果是，则将用户的平台里的密码也设置为该密码，并完成本次登录
     *  (适用于UM的用户从第三方导入的情况，因密码是加密的(且TSS里加密方式是账号 + 密码))
     */
    protected boolean customizeValidate(IPWDOperator operator, String password) {
        IdentityGetter ig = IdentityGetterFactory.getInstance();
        boolean result = ig.indentify(operator, password);
        if(result) { // 如果密码是在地方系统里验证通过，则将第三方的密码设置到UM中
        	try {
        		loginservice.resetPassword(operator.getId(), password); 
        	} catch(Exception e) {
        		log.error("resetPassword时出错了：" + e.getMessage());
        	}
        }
		return result;
    }
}
