package com.jinhe.tss.um.helper.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import com.jinhe.tss.framework.web.dispaly.grid.GridAttributesMap;
import com.jinhe.tss.framework.web.dispaly.grid.IGridNode;
import com.jinhe.tss.um.entity.Role;
import com.jinhe.tss.um.entity.User;
import com.jinhe.tss.util.BeanUtil;

public class UserRoleDTO implements IGridNode {
    
	public Long userId;
	public Long roleId;
	public String userName;
	public String roleName;
	public String description;
 
	public UserRoleDTO(User user, Role role) {
		this.userId = user.getId();
		this.userName = user.getUserName();
		this.roleId = role.getId();
		this.roleName = role.getName();
		this.description = role.getDescription();
	}

	public GridAttributesMap getAttributes(GridAttributesMap map) {
		Map<String, Object> properties = new LinkedHashMap<String, Object>();
        BeanUtil.addBeanProperties2Map(this, properties);
        map.putAll(properties);
		return map;
	}
	
}
