package com.jinhe.tss.portal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.jinhe.tss.framework.persistence.IEntity;
import com.jinhe.tss.framework.persistence.entityaop.OperateInfo;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.TreeAttributesMap;
import com.jinhe.tss.portal.PortalConstants;

/**
 * 主题实体：包括门户结构以及相对应的布局、修饰器等配置信息
 */
@Entity
@Table(name = "pms_theme", uniqueConstraints = { 
        @UniqueConstraint(name="MULTI_NAME_Theme", columnNames = { "portalId", "name" })
})
@SequenceGenerator(name = "theme_sequence", sequenceName = "theme_sequence", initialValue = 1, allocationSize = 1)
public class Theme extends OperateInfo implements IEntity, ITreeNode {
    
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "theme_sequence")
	private Long    id;
	
	@Column(nullable = false)
	private String  name;        // 主题名称
	
	@Column(nullable = false)
    private Long    portalId;    // 主题所属的门户ID
	private String  description; // 主题的描述
	
	private Integer seqNo;     // 顺序号
    private Integer disabled = PortalConstants.FALSE;  // 是否停用：0－启用；1－停用

    public TreeAttributesMap getAttributes() {
        TreeAttributesMap map = new TreeAttributesMap(id, name);
        map.put("icon", "../platform/images/icon/theme.gif");
        return map;
    }
 
	public String getDescription() {
		return description;
	}
 
	public void setDescription(String description) {
		this.description = description;
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
 
	public Integer getSeqNo() {
		return seqNo;
	}
 
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
 
    public Long getPortalId() {
        return portalId;
    }
 
    public void setPortalId(Long portalId) {
        this.portalId = portalId;
    }
 
    public Integer getDisabled() {
        return disabled;
    }
 
    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }
}
