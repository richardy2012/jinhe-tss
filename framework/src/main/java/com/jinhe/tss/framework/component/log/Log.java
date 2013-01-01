package com.jinhe.tss.framework.component.log;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.jinhe.tss.framework.persistence.IEntity;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.web.dispaly.grid.GridAttributesMap;
import com.jinhe.tss.framework.web.dispaly.grid.IGridNode;
import com.jinhe.tss.framework.web.dispaly.xform.IXForm;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.DateUtil;

/** 
 * 日志表
 */
@Entity
@Table(name = "component_log")
@SequenceGenerator(name = "log_sequence", sequenceName = "log_sequence", initialValue = 1000, allocationSize = 10)
public class Log implements IEntity, IXForm, IGridNode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "log_sequence")
    private Long    id;
    
    @Column(nullable = false)  
    private String appCode;       // 应用Code
    private Long   operatorId;    // 操作者ID
    private String operatorName;  // 操作者Name
    private String operatorIP;    // 操作者IP
    private String operationCode; // 操作Code
    private String operateTable;  // 操作的表
    private Date   operateTime;   // 操作时间
    
    @Column(length = 4000)  
    private String  content;        //操作内容
    
    public Log() {
    }

    public Log(String appCode, Long operatorId, String operatorName,
            String operatorIP, String operationCode, String operateTable,
            String content) {
        this.appCode = appCode;
        this.operatorId = operatorId;
        this.operatorName = operatorName;
        this.operatorIP = operatorIP;
        this.operationCode = operationCode;
        this.operateTable = operateTable;
        this.content = content;
        this.operateTime = new Date();
    }

    public Log(String operationCode, Object entity) {
        this.appCode = Context.getApplicationContext().getCurrentAppCode();
        this.operatorId = Environment.getOperatorId();
        this.operatorName = Environment.getOperatorName();
        this.operatorIP   = Environment.getClientIp();
        this.operationCode = operationCode;
        this.operateTable = entity.getClass().getName();
        this.content = BeanUtil.toXml(entity);
        this.operateTime = new Date();
    }
 
    public String getAppCode() {
        return appCode;
    }
 
    public String getContent() {
        return content;
    }
 
    public Long getId() {
        return id;
    }
 
    public Date getOperateTime() {
        return operateTime;
    }
 
    public String getOperationCode() {
        return operationCode;
    }
 
    public Long getOperatorId() {
        return operatorId;
    }
 
    public String getOperatorIP() {
        return operatorIP;
    }
 
    public String getOperatorName() {
        return operatorName;
    }
 
    public String getOperateTable() {
        return operateTable;
    }
 
    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }
 
    public void setContent(String content) {
        this.content = content;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
 
    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }
 
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }
 
    public void setOperatorIP(String operatorIP) {
        this.operatorIP = operatorIP;
    }
 
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
 
    public void setOperateTable(String table) {
        this.operateTable = table;
    }
    
    public Map<String, Object> getAttributesForXForm() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", this.id);
        map.put("appCode", this.appCode);
        map.put("content", this.content);
        map.put("operateTable", this.operateTable);
        map.put("operateTime", DateUtil.formatCare2Second(this.operateTime));
        map.put("operationCode", this.operationCode);
        map.put("operatorId", this.operatorId);
        map.put("operatorIP", this.operatorIP);
        map.put("operatorName", this.operatorName);
        
        return map;
    }
    
    public GridAttributesMap getAttributes(GridAttributesMap map) {
        map.put("id", this.id);
        map.put("appCode", this.appCode);
        map.put("operateTable", this.operateTable);
        map.put("operateTime", DateUtil.formatCare2Second(this.operateTime));
        map.put("operationCode", this.operationCode);
        map.put("operatorIP", this.operatorIP);
        map.put("operatorName", this.operatorName);
        return map;
    }
    
    public String toString() {
        return appCode + ":" + operatorName + ":" + content;
    }
}

