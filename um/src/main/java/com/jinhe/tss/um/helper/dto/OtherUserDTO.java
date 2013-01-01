package com.jinhe.tss.um.helper.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import com.jinhe.tss.framework.web.dispaly.grid.GridAttributesMap;
import com.jinhe.tss.framework.web.dispaly.grid.IGridNode;
import com.jinhe.tss.util.BeanUtil;

public class OtherUserDTO implements IGridNode {

    // 当前系统中用户属性
    private Long   id;
    private String userName;
    private String loginName;
    private Long   groupId;
    private String groupName;

    // 对应用户属性
    private Long   appUserId;
    private String appUserName;
    private String appLoginName;
    private Long   appGroupId;
    private String appGroupName;

    public GridAttributesMap getAttributes(GridAttributesMap map) {
        Map<String, Object> properties = new LinkedHashMap<String, Object>();
        BeanUtil.addBeanProperties2Map(this, properties);
        map.putAll(properties);
        return map;
    }

    public Long getAppGroupId() {
        return appGroupId;
    }

    public String getAppGroupName() {
        return appGroupName;
    }

    public String getAppLoginName() {
        return appLoginName;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public String getAppUserName() {
        return appUserName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public Long getId() {
        return id;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setAppGroupId(Long appGroupId) {
        this.appGroupId = appGroupId;
    }

    public void setAppGroupName(String appGroupName) {
        this.appGroupName = appGroupName;
    }

    public void setAppLoginName(String appLoginName) {
        this.appLoginName = appLoginName;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    public void setAppUserName(String appUserName) {
        this.appUserName = appUserName;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
