package com.jinhe.tss.cms.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.framework.persistence.IEntity;
import com.jinhe.tss.framework.web.dispaly.tree.ILevelTreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.TreeAttributesMap;
import com.jinhe.tss.framework.web.dispaly.xform.IXForm;
import com.jinhe.tss.util.BeanUtil;

/** 
 * 各种定时策略类。
 * 包括有 0 时间策略 1 索引策略 2 发布策略 3 过期策略
 * 
 * 其中第一层只能是时间策略，时间策略下可以创建其它三种策略并可以使用即使执行索引功能。
 */
@Entity
@Table(name = "cms_timerStrategy", uniqueConstraints = { 
        @UniqueConstraint(name="MULTI_NAME_INDEXNAME", columnNames = { "PARENTID", "name" })
})
@SequenceGenerator(name = "timerStrategy_sequence", sequenceName = "timerStrategy_sequence", initialValue = 1, allocationSize = 1)
public class TimerStrategy implements IEntity, ILevelTreeNode, IXForm {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "timerStrategy_sequence")
	private Long    id;		 
    
    @Column(nullable = false)
    private String  name;	    // 策略名称
    private Long    parentId;   // 父节点
    private Integer status = CMSConstants.TRUE;  // 状态值：0 启用 1 停用
    
    @Column(nullable = false)
    private Integer type;       // 索引类型：0 时间策略 1 索引策略 2 发布策略 3 过期策略
    
    @Column(length = 1000)
    private String content;     // 策略内容，对于定时策略，则该字段为定时时间；对于其他策略，则一般为栏目ID列表
    private String indexPath;   // 索引文件存放目录
    private String remark;      // 备注
    
    private String indexExecutorClass; //索引实现类类名
    
    @Transient private boolean isIncrement; //是否增量操作
    
    public boolean isIncrement() {
        return isIncrement;
    }
 
    public void setIncrement(boolean isIncrement) {
        this.isIncrement = isIncrement;
    }
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public String getContent() {
        return content;
    }
 
    public void setContent(String tacticIndex) {
        this.content = tacticIndex;
    }
 
    public Long getParentId() {
        return parentId;
    }
 
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
 
    public Integer getStatus() {
        return status;
    }
 
    public void setStatus(Integer status) {
        this.status = status;
    }
 
    public Integer getType() {
        return type;
    }
 
    public void setType(Integer type) {
        this.type = type;
    }
 
    public String getIndexPath() {
        return indexPath;
    }
 
    public void setIndexPath(String indexPath) {
        this.indexPath = indexPath;
    }
 
    public String getIndexExecutorClass() {
        return indexExecutorClass;
    }
 
    public void setIndexExecutorClass(String indexExecutorClass) {
        this.indexExecutorClass = indexExecutorClass;
    }
 
	public String getRemark() {
		return remark;
	}
 
	public void setRemark(String remark) {
		this.remark = remark;
	}

    public Map<String, Object> getAttributesForXForm() {
        Map<String, Object> map = new HashMap<String, Object>();
        BeanUtil.addBeanProperties2Map(this, map);
        return map;
    }

    public TreeAttributesMap getAttributes() {
        TreeAttributesMap map = new TreeAttributesMap(id, name);
        map.put("parentId", parentId);
        map.put("status", status);
        map.put("type", type);
        
        String _disable = CMSConstants.STATUS_START.equals(status) ? "_2" : "";
        switch(type) {
        case 0:
        	map.put("icon", "../framework/images/time_tactic" + _disable + ".gif");
        	break;
        case 1:
        	map.put("icon", "../framework/images/index_tactic" + _disable + ".gif");
        	break;
        case 2:
        	map.put("icon", "../framework/images/publish_tactic" + _disable + ".gif");
        	break;
        case 3:
        	map.put("icon", "../framework/images/expire_tactic" + _disable + ".gif");
        	break;
        
        }
        return map;
    }
    
    /**
     * 生产索引策略的生成路径，格式如： D:/cms/index/12
     */
    public String createIndexPath(){
        return this.getIndexPath() + "/index/" + this.getId();
    }

}

