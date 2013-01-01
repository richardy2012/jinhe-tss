package com.jinhe.tss.framework.component.log;

import java.util.Date;

import com.jinhe.tss.framework.persistence.QueryCondition;

/** 
 * 日志的查询条件类
 */
public class LogQueryCondition extends QueryCondition {
    
    private String  appCode;
    private String  operatorName;  
    private String  operatorIP;
    private Date    startTime;      // 开始时间
    private Date    endTime;        // 结束时间
    private String  operationCode;  // 操作类型
    private String  operateTable;   // 操作对象    

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorIP() {
        return operatorIP;
    }

    public void setOperatorIP(String operatorIP) {
        this.operatorIP = operatorIP;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public String getOperateTable() {
        return operateTable;
    }

    public void setOperateTable(String operateTable) {
        this.operateTable = operateTable;
    }

}