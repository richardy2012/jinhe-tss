package com.jinhe.tss.um.service;

import java.util.List;

import com.jinhe.tss.um.entity.PasswordRule;
 
public interface IPasswordRuleService {
	
	public void saveRule(PasswordRule rule);
	
	public void updateRule(PasswordRule rule);
	
	public void deleteRule(Long ruleId);
	
	public List<?> getAllPasswordRules();
	
	public String getStrengthLevel(Long id, String password, String loginName);
	
	public String getStrengthLevel(Long id, String password);
	
	public PasswordRule getDefaultRule();
	
	public PasswordRule getRuleById(Long ruleId);
}