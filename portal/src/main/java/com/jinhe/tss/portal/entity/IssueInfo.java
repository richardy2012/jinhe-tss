package com.jinhe.tss.portal.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.jinhe.tss.framework.persistence.IEntity;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.TreeAttributesMap;
import com.jinhe.tss.framework.web.dispaly.xform.IXForm;
import com.jinhe.tss.util.BeanUtil;

/** 
 * 门户发布信息表
 */
@Entity
@Table(name = "pms_issue_info", uniqueConstraints = { 
        @UniqueConstraint(name="MULTI_NAME_IssueInfo", columnNames = { "name" })
})
@SequenceGenerator(name = "issueInfo_sequence", sequenceName = "issueInfo_sequence", initialValue = 1, allocationSize = 1)
public class IssueInfo implements IEntity, ITreeNode, IXForm {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "issueInfo_sequence")
    private Long   id;
	
	@Column(nullable = false)
    private String name;
	
	@Column(nullable = false)
    private Long   portalId;  // 对应门户ID
    private String portalName;
    
    @Column(nullable = false)
    private String visitUrl;  // 门户访问地址
    
    private Long   themeId;   // 指定的主题
    
    private Long   pageId;    // 对应版面的门户结构ID
    private String pageName;  
    private String pageCode;  
    
    private String remark;
 
    public String getName() {
        return name;
    }
 
    public String getRemark() {
        return remark;
    }
 
    public Long getId() {
        return id;
    }
 
    public String getVisitUrl() {
        return visitUrl;
    }
 
    public String getPageCode() {
        return pageCode;
    }
 
    public Long getPortalId() {
        return portalId;
    }    
 
    public String getPortalName() {
        return portalName;
    }
 
    public Long getPageId() {
        return pageId;
    }
 
    public String getPageName() {
        return pageName;
    }
 
    public Long getThemeId() {
        return themeId;
    }
 
    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }
 
    public void setPortalId(Long portalId) {
        this.portalId = portalId;
    }
 
    public void setVisitUrl(String visitUrl) {
        this.visitUrl = visitUrl;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public void setRemark(String reamrk) {
        this.remark = reamrk;
    }
 
    public void setPortalName(String portalName) {
        this.portalName = portalName;
    }
 
    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }
 
    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
    
    public Map<String, Object> getAttributesForXForm() {
        Map<String, Object> map = new HashMap<String, Object>();
        BeanUtil.addBeanProperties2Map(this, map);
        return map;
    }
    
    public TreeAttributesMap getAttributes() {
        TreeAttributesMap map = new TreeAttributesMap(id, name);
        map.put("icon", "../platform/images/icon/url.gif");
        return map;
    }
}

