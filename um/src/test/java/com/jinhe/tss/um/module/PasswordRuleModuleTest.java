package com.jinhe.tss.um.module;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.action.PasswordRuleAction;
import com.jinhe.tss.um.entity.PasswordRule;
import com.jinhe.tss.util.BeanUtil;

/**
 * 密码策略相关模块的单元测试
 */
public class PasswordRuleModuleTest extends TxSupportTest4UM {
    
	@Autowired PasswordRuleAction action;
    
    public void testCRUD() {
        action.getRuleInfo(0L);
        
        PasswordRule rule = new PasswordRule();
        BeanUtil.copy(rule, PasswordRule.getDefaultPasswordRule());
        rule.setName("自建密碼策略");
        action.saveRule(rule);
        
        action.getRuleInfo(rule.getId());
        
        action.modifyRule(rule);
        
        action.getStrengthLevel(UMConstants.ADMIN_USER_ID, UMConstants.ADMIN_USER_NAME, "123456");
        
        action.getAllRules();
        
        action.getPasswordRuleInfo();
        
        action.deleteRule(rule.getId());
    }

}
