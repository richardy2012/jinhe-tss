package com.jinhe.tss.um.service;

import java.util.List;

import com.jinhe.tss.um.entity.PasswordRule;
 
public interface IPasswordRuleService {
	
	void saveRule(PasswordRule rule);
	
	void updateRule(PasswordRule rule);
	
	void deleteRule(Long ruleId);
	
	List<?> getAllPasswordRules();
	
	String getStrengthLevel(Long id, String password, String loginName);
	
	PasswordRule getDefaultRule();
	
	PasswordRule getRuleById(Long ruleId);
}