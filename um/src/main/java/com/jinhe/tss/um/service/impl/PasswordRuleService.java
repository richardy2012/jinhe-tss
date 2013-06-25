package com.jinhe.tss.um.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.persistence.ICommonDao;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.entity.PasswordRule;
import com.jinhe.tss.um.service.IPasswordRuleService;

public class PasswordRuleService implements IPasswordRuleService {
	
    @Autowired private ICommonDao commonDao;

	public void updateRule(PasswordRule rule){
		PasswordRule passwordRule = getRuleById(rule.getId());
		passwordRule.copyAttribute(rule);
		saveRule(passwordRule);
	}

	public String getStrengthLevel(Long userId, String password, String loginName){
		PasswordRule rule = getDefaultRule();

		int flag = checkAvailable(rule, password);
		
		//如果不允许登录名和密码相同 则将相同的设为不可用
		if(UMConstants.TRUE.equals(rule.getCanEq2LoginName()) && password.equals(loginName)){
			flag = 0;
        }
		
		return judgeLevel(password, rule, flag);
	}
	
	private int checkAvailable(PasswordRule rule, String password){
		int flag = 1;
		
		//密码长度小于要求的最低长度 则设为不可用
		if(password.length() < rule.getLeastLength()) {
			flag = 0;
		}
		
		//密码和禁用密码相同，则设为不可用
		if(null != rule.getImpermissible()){
			String[] impermissibles = rule.getImpermissible().split(",");
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
		else if(strength < rule.getLeastStrength())
			level = PasswordRule.UNQUALIFIED_LEVEL;
		else if(strength < rule.getLowStrength())
			level = PasswordRule.LOW_LEVEL;
		else if(strength < rule.getHigherStrength())
			level = PasswordRule.MEDIUM_LEVEL;
		else 
			level = PasswordRule.HIGHER_LEVEL;
		return level;
	}
 
    public void saveRule(PasswordRule rule) {
        commonDao.create(rule);
    }

    public void deleteRule(Long ruleId){
        commonDao.delete(PasswordRule.class, ruleId);
    }

    public PasswordRule getRuleById(Long ruleId) {
        return (PasswordRule)commonDao.getEntity(PasswordRule.class, ruleId);
    }
    
    public List<?> getAllPasswordRules(){
        return commonDao.getEntities(" from PasswordRule r order by r.id");
    }
    
    public PasswordRule getDefaultRule(){
        List<?> list = commonDao.getEntities(" from PasswordRule r where r.isDefault = " + UMConstants.TRUE);
        return list.size() > 0 ? (PasswordRule)list.get(0) : null;
    }
}
