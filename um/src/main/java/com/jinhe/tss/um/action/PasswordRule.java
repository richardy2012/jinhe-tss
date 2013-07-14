package com.jinhe.tss.um.action;

import javax.persistence.Transient;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.um.UMConstants;

/**
 * 定义密码规则。
 */
@Controller
@RequestMapping("passwordrule")
public class PasswordRule extends BaseActionSupport {
	
	@Transient public static final String UNQUALIFIED_LEVEL = "0";	//不可用
	@Transient public static final String LOW_LEVEL         = "1";	//低
	@Transient public static final String MEDIUM_LEVEL      = "2";	//中
	@Transient public static final String HIGHER_LEVEL      = "3";	//高
	
	private Long leastLength;		 // 最短长度
	private Integer canEq2LoginName; // 是否可以和用户名相同
	
	private Long leastStrength;	     // 最低强度
	private Long lowStrength;	     // 低强度临界值
	private Long higherStrength;     // 高强度
	private String impermissible;    // 不允许的密码，用","隔开
 
	public static PasswordRule getDefaultPasswordRule(){
		PasswordRule rule = new PasswordRule();
		rule.canEq2LoginName = UMConstants.FALSE;
		rule.leastLength    = new Long(6);
		rule.leastStrength  = new Long(8);
		rule.lowStrength    = new Long(16);
		rule.higherStrength = new Long(60);
		return rule;
	}
 
	// 密码强度定义.
    static int factor[] = {1, 2, 3, 4};
    static int kindFactor[] = {0, 0, 30, 50, 70};
    static String[] regex = {"0123456789", 
                             "abcdefghijklmnopqrstuvwxyz", 
                             "ABCDEFGHIJKLMNOPQRSTUVWXYZ", 
                             "~`!@#$%^&*()-=_+,./<>?;:|"};

    public static int getStrengthValue(String pwd) {
        int strengthValue = 0;
        int composedKind = 0;
        for (int i = 0; i < regex.length; i++) {
            int matched = 0;
            for (int j = 0; j < pwd.length(); j++) {
                if (regex[i].indexOf(pwd.charAt(j)) >= 0)
                    matched++;
            }
 
            if (matched != 0) {
                strengthValue += matched * factor[i];
                composedKind++;
            }
        }
        strengthValue += kindFactor[composedKind];
        return strengthValue;
    }
    
	@RequestMapping(value = "/{loginName}/{password}")
	public void getStrengthLevel(@PathVariable("password") String password, 
			@PathVariable("loginName") String loginName) {
 
		PasswordRule rule = getDefaultPasswordRule();

		int flag = checkAvailable(rule, password);
		
		//如果不允许登录名和密码相同 则将相同的设为不可用
		if(UMConstants.TRUE.equals(rule.canEq2LoginName) && password.equals(loginName)){
			flag = 0;
        }
		
		print("SecurityLevel", judgeLevel(password, rule, flag));
	}
	
	private int checkAvailable(PasswordRule rule, String password){
		int flag = 1;
		
		//密码长度小于要求的最低长度 则设为不可用
		if(password.length() < rule.leastLength) {
			flag = 0;
		}
		
		//密码和禁用密码相同，则设为不可用
		if(null != rule.impermissible){
			String[] impermissibles = rule.impermissible.split(",");
			for(int i = 0; i < impermissibles.length; i++ ){
				if(password.equals(impermissibles[i])) {
					flag = 0;
				}
			}
		}
		return flag;
	}
	
	private String judgeLevel(String password, PasswordRule rule, int flag){
		String level = PasswordRule.UNQUALIFIED_LEVEL;
		int strength = PasswordRule.getStrengthValue(password);
		if(flag == 0)
			level = PasswordRule.UNQUALIFIED_LEVEL;
		else if(strength < rule.leastStrength)
			level = PasswordRule.UNQUALIFIED_LEVEL;
		else if(strength < rule.lowStrength)
			level = PasswordRule.LOW_LEVEL;
		else if(strength < rule.higherStrength)
			level = PasswordRule.MEDIUM_LEVEL;
		else 
			level = PasswordRule.HIGHER_LEVEL;
		return level;
	}
	

}
