package com.jinhe.tss.um.helper.dto;

public class GroupAutoMapping {

    /**
     * 对应类型 1-按照登陆帐号对应 2-按照工号对应 3-按照证件号码对应 4-按照姓名对应
     */
    private Integer type;

    /**
     * 要对应的组id
     */
    private Long groupId;
    
    /**
     * 对应到的组id
     */
    private Long toGroupId;

    /**
     * 对应方式 0-只对应当前组 1-只应当前组以及子组的用户
     */
    private Integer mode;
 
    public Long getGroupId() {
        return groupId;
    }
 
    public Long getToGroupId() {
        return toGroupId;
    }

    public Integer getType() {
        return type;
    }

    public Integer getMode() {
        return mode;
    }
 
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
 
    public void setToGroupId(Long toGroupId) {
        this.toGroupId = toGroupId;
    }
 
    public void setType(Integer type) {
        this.type = type;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }
}
