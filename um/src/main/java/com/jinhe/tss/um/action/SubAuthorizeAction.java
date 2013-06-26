package com.jinhe.tss.um.action;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.entity.SubAuthorize;
import com.jinhe.tss.um.service.ISubAuthorizeService;
import com.jinhe.tss.util.DateUtil;

/**
 * <p>
 * 权限转授策略相关操作。
 * 策略没有组，没有上下级关系，没有排序id
 * </p>
 */
@Controller
@RequestMapping("subauthorize")
public class SubAuthorizeAction extends BaseActionSupport {

	private ISubAuthorizeService service;
	
	/**
	 * 查找策略列表
	 */
	@RequestMapping("/")
	public void getSubAuthorizeStrategys2Tree() {
		print("RuleTree", new TreeEncoder(service.getStrategyByCreator()));
	}
	
	/**
	 * 获取一个Strategy（策略）对象的明细信息、角色对策略的信息、策略对用户的信息、策略对用户组的信息
	 */
	@RequestMapping("/{id}")
	public void getSubAuthorizeStrategyInfo(Long id) {
		XFormEncoder ruleXFormEncoder;
        TreeEncoder ruleToGroupTree = null;
        TreeEncoder ruleToUserTree  = null;
        TreeEncoder ruleToRoleTree  = null;
		
        Map<String, Object> data;
		if (UMConstants.IS_NEW.equals(id)) { // 新建策略
            data = service.getStrategyInfo4Create();
            
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("startDate", DateUtil.format(new Date()));
			
			// 默认的有效时间, 结束时间向后推迟7天
			Calendar calendar = new GregorianCalendar();
			calendar.add(UMConstants.STRATEGY_LIFE_TYPE, UMConstants.STRATEGY_LIFE_TIME);
			map.put("endDate", DateUtil.format(calendar.getTime()));
            
			ruleXFormEncoder = new XFormEncoder(UMConstants.STRATEGY_XFORM, map);
		} 
		else { // 编辑策略
			data = service.getStrategyInfo4Update(id);
			
			ruleXFormEncoder = new XFormEncoder(UMConstants.STRATEGY_XFORM, (SubAuthorize) data.get("RuleInfo"));
			ruleToGroupTree = new TreeEncoder(data.get("Rule2GroupExistTree"));
			ruleToUserTree  = new TreeEncoder(data.get("Rule2UserExistTree"));
			ruleToRoleTree  = new TreeEncoder(data.get("Rule2RoleExistTree"));
		}
        
		TreeEncoder groupsTreeEncoder = new TreeEncoder(data.get("Rule2GroupTree"), new LevelTreeParser());
        groupsTreeEncoder.setNeedRootNode(false);

        TreeEncoder rolesTreeEncoder = new TreeEncoder(data.get("Rule2RoleTree"), new LevelTreeParser());
        rolesTreeEncoder.setNeedRootNode(false);
        
		print(new String[]{"RuleInfo", "Rule2GroupTree", "Rule2RoleTree", "Rule2GroupExistTree", "Rule2UserExistTree", "Rule2RoleExistTree"},
                new Object[]{ruleXFormEncoder, groupsTreeEncoder, rolesTreeEncoder, ruleToGroupTree, ruleToUserTree, ruleToRoleTree});
	}

	/**
	 * 修改一个Strategy对象的明细信息、策略对用户信息、策略对用户组、角色对策略的信息
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void saveSubAuthorizeInfo(SubAuthorize strategy, String rule2UserIds, String rule2GroupIds, String rule2RoleIds) {
		service.saveStrategy(strategy, rule2UserIds, rule2GroupIds, rule2RoleIds);
		printSuccessMessage();
	}

	/**
	 * 删除策略
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Long id) {
		service.deleteStrategy(id);
        printSuccessMessage();
	}
	
	/**
	 * 停用/启用策略
	 */
	@RequestMapping(value = "/disable/{id}/{disabled}")
	public void disable(@PathVariable("id") Long id, @PathVariable("state") int state) {
		service.disable(id, state);
        printSuccessMessage();
	}
}