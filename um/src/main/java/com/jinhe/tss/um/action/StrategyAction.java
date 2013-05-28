package com.jinhe.tss.um.action;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.entity.Strategy;
import com.jinhe.tss.um.service.IStrategyService;
import com.jinhe.tss.util.DateUtil;

/**
 * <p>
 * 权限转授策略相关操作。
 * 策略没有组，没有上下级关系，没有排序id
 * </p>
 */
public class StrategyAction extends BaseActionSupport {

	private IStrategyService service;

    private Long    groupId;
	private Long    strategyId;
	private String  rule2UserIds;
	private String  rule2GroupIds;
	private String  rule2RoleIds;
	private Integer disabled = UMConstants.FALSE;
	
	private Strategy strategy = new Strategy();
	
	/**
	 * 查找策略列表
	 * @param userId
	 * @return
	 */
	public String getSubAuthorizeStrategys2Tree() {
		return print("RuleTree", new TreeEncoder(service.getStrategyByCreator()));
	}
	
	/**
	 * <p>
	 * 获取一个Strategy（策略）对象的明细信息、角色对策略的信息、策略对用户的信息、策略对用户组的信息
	 * </p>
	 * @return String
	 */
	public String getSubAuthorizeStrategyInfo() {
		XFormEncoder ruleXFormEncoder;
        TreeEncoder ruleToGroupTree = null;
        TreeEncoder ruleToUserTree  = null;
        TreeEncoder ruleToRoleTree  = null;
		
        Map<String, Object> data;
		if (isCreateNew()) { // 新建策略
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
			data = service.getStrategyInfo4Update(strategyId);
			
			ruleXFormEncoder = new XFormEncoder(UMConstants.STRATEGY_XFORM, (Strategy) data.get("RuleInfo"));
			ruleToGroupTree = new TreeEncoder(data.get("Rule2GroupExistTree"));
			ruleToUserTree  = new TreeEncoder(data.get("Rule2UserExistTree"));
			ruleToRoleTree  = new TreeEncoder(data.get("Rule2RoleExistTree"));
		}
        
		TreeEncoder groupsTreeEncoder = new TreeEncoder(data.get("Rule2GroupTree"), new LevelTreeParser());
        groupsTreeEncoder.setNeedRootNode(false);

        TreeEncoder rolesTreeEncoder = new TreeEncoder(data.get("Rule2RoleTree"), new LevelTreeParser());
        rolesTreeEncoder.setNeedRootNode(false);
        
		return print(new String[]{"RuleInfo", "Rule2GroupTree", "Rule2RoleTree", "Rule2GroupExistTree", "Rule2UserExistTree", "Rule2RoleExistTree"},
                new Object[]{ruleXFormEncoder, groupsTreeEncoder, rolesTreeEncoder, ruleToGroupTree, ruleToUserTree, ruleToRoleTree});
	}

	/**
	 * <p>
	 * 修改一个Strategy对象的明细信息、策略对用户信息、策略对用户组、角色对策略的信息
	 * </p>
	 * @return String
	 */
	public String saveSubAuthorizeStrategy() {
		service.saveStrategy(strategy, rule2UserIds, rule2GroupIds, rule2RoleIds);
		return printSuccessMessage();
	}

	/**
	 * <p>
	 * 根据用户组id查找用户列表
	 * 还要通过权限过滤
	 * </p>
	 * @return
	 */
	public String getUsersByGroupId(){
		List<?> list = service.getUsersByGroupId(groupId);
		return print("Group2UserListTree", new TreeEncoder(list));
	}
	
	/**
	 * <p>
	 * 删除策略
	 * </p>
	 * @return
	 */
	public String delete(){
		service.deleteStrategy(strategyId);
        return printSuccessMessage();
	}
	
	/**
	 * <p>
	 * 停用/启用策略
	 * </p>
	 * @return
	 */
	public String disable(){
		service.disable(strategyId, disabled);
        return printSuccessMessage();
	}
 
	public void setService(IStrategyService service) {
		this.service = service;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
 
	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}
 
	public Strategy getStrategy() {
		return strategy;
	}
 
	public void setRule2GroupIds(String rule2GroupIds) {
		this.rule2GroupIds = rule2GroupIds;
	}
 
	public void setRule2RoleIds(String rule2RoleIds) {
		this.rule2RoleIds = rule2RoleIds;
	}
 
	public void setRule2UserIds(String rule2UserIds) {
		this.rule2UserIds = rule2UserIds;
	}

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }
}