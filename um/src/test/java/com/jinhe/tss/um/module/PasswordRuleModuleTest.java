package com.jinhe.tss.um.module;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.action.PasswordRuleAction;
import com.jinhe.tss.um.entity.PasswordRule;
import com.jinhe.tss.um.service.IPasswordRuleService;
import com.jinhe.tss.util.BeanUtil;

/**
 * 密码策略相关模块的单元测试
 */
public class PasswordRuleModuleTest extends TxSupportTest4UM {
    
	PasswordRuleAction action;
    
    @Autowired IPasswordRuleService service;
    
    public void setUp() throws Exception {
        super.setUp();
        
        action = new PasswordRuleAction();
        action.setService(service);
    }
    
    public void testCRUD() {
        action.getRuleInfo();
        
        BeanUtil.copy(action.getRule(), PasswordRule.getDefaultPasswordRule());
        action.getRule().setName("自建密碼策略");
        action.saveRule();
        
        action.setId(action.getRule().getId());
        action.getRuleInfo();
        
        action.modifyRule();
        
        action.setId(UMConstants.ADMIN_USER_ID);
        action.setLoginName(UMConstants.ADMIN_USER_NAME);
        action.setPassword("123456");
        action.getStrengthLevel();
        
        action.setId(UMConstants.MAIN_GROUP_ID);
        action.getGroupStrengthLevel();
        
        action.getAllRules();
        
        action.getPasswordRuleInfo();
        
        action.setId(action.getRule().getId());
        action.deleteRule();
    }

}
