package com.jinhe.tss.um.action;

import java.util.List;

import com.jinhe.tss.framework.web.dispaly.tree.SimpleTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.PTActionSupport;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.entity.PasswordRule;
import com.jinhe.tss.um.service.IPasswordRuleService;
import com.jinhe.tss.util.EasyUtils;

public class PasswordRuleAction extends PTActionSupport {

	private IPasswordRuleService service;
	
	private PasswordRule rule = new PasswordRule();
	private Long id;
	private String loginName;
	private String password;
	
	public String getRuleInfo(){
		PasswordRule rule = null;
		if( id == null ){
			rule = PasswordRule.getDefaultPasswordRule();
		} else {
			rule = service.getRuleById(id);
		}
		XFormEncoder ruleEncoder = new XFormEncoder(UMConstants.PASSWORDINFO_XFORM, rule);
		return print("PasswordInfo", ruleEncoder);
	}
	
	public String saveRule(){
		service.saveRule(rule);
		return printSuccessMessage();
	}
	
	public String modifyRule(){
		service.updateRule(rule);
		return printSuccessMessage();
	}
	
	public String deleteRule(){
		service.deleteRule(id);
		return printSuccessMessage();
	}
	
	public String getStrengthLevel(){
		return print("SecurityLevel", service.getStrengthLevel(id, password, loginName));
	}
	
	public String getGroupStrengthLevel(){
		return print("SecurityLevel", service.getStrengthLevel(id, password));
	}
	
	public String getAllRules(){
		List<?> rules = service.getAllPasswordRules();
		TreeEncoder encoder = new TreeEncoder(rules, new SimpleTreeParser());
		encoder.setNeedRootNode(true);
		return print("RuleTree", encoder);
	}
	
	public String getPasswordRuleInfo(){
		List<?> passwordRules = service.getAllPasswordRules();
		String[] comboedits = EasyUtils.generateComboedit(passwordRules, "id", "name", "|");
		
		XFormEncoder encoder = new XFormEncoder(UMConstants.PASSWORD_TACTIC_XFORM);
		encoder.setColumnAttribute("passwordRuleId", "editorvalue", comboedits[0]);
		encoder.setColumnAttribute("passwordRuleId", "editortext", comboedits[1]);
		return print("PasswordRuleInfo", encoder);
	}

	public void setService(IPasswordRuleService service) {
		this.service = service;
	}
	
	public PasswordRule getRule(){
		return rule;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
