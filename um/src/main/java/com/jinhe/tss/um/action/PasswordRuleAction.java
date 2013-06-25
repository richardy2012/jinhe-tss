package com.jinhe.tss.um.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jinhe.tss.framework.web.dispaly.tree.SimpleTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.entity.PasswordRule;
import com.jinhe.tss.um.service.IPasswordRuleService;
import com.jinhe.tss.util.EasyUtils;

@Controller
@RequestMapping("passwordrule")
public class PasswordRuleAction extends BaseActionSupport {

	@Autowired private IPasswordRuleService service;
 
	@RequestMapping("/{id}")
	public void getRuleInfo(@PathVariable("id") Long id) {
		PasswordRule rule = null;
		if( id == 0L ){
			rule = PasswordRule.getDefaultPasswordRule();
		} else {
			rule = service.getRuleById(id);
		}
		XFormEncoder ruleEncoder = new XFormEncoder(UMConstants.PASSWORDINFO_XFORM, rule);
		print("PasswordInfo", ruleEncoder);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public void saveRule(PasswordRule rule) {
		service.saveRule(rule);
		printSuccessMessage();
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public void modifyRule(PasswordRule rule) {
		service.updateRule(rule);
		printSuccessMessage();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteRule(@PathVariable("id") Long id) {
		service.deleteRule(id);
		printSuccessMessage();
	}
	
	@RequestMapping(value = "/{id}/{loginName}/{password}")
	public void getStrengthLevel(@PathVariable("id") Long id, 
			@PathVariable("password") String password, 
			@PathVariable("loginName") String loginName) {
		
		print("SecurityLevel", service.getStrengthLevel(id, password, loginName));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public void getAllRules() {
		List<?> rules = service.getAllPasswordRules();
		TreeEncoder encoder = new TreeEncoder(rules, new SimpleTreeParser());
		print("RuleTree", encoder);
	}
	
	@RequestMapping(value = "/list")
	public void getPasswordRuleInfo() {
		List<?> passwordRules = service.getAllPasswordRules();
		String[] comboedits = EasyUtils.generateComboedit(passwordRules, "id", "name", "|");
		
		XFormEncoder encoder = new XFormEncoder(UMConstants.PASSWORD_TACTIC_XFORM);
		encoder.setColumnAttribute("passwordRuleId", "editorvalue", comboedits[0]);
		encoder.setColumnAttribute("passwordRuleId", "editortext",  comboedits[1]);
		print("PasswordRuleInfo", encoder);
	}
}
