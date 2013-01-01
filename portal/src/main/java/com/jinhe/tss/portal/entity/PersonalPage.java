package com.jinhe.tss.portal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.jinhe.tss.framework.persistence.IEntity;

/** 
 * 门户个性化定制页面信息实体
 */
@Entity
@Table(name = "pms_Personal_Page")
@SequenceGenerator(name = "personalPage_sequence", sequenceName = "personalPage_sequence", initialValue = 1, allocationSize = 1)
public class PersonalPage implements IEntity {
    
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "personalPage_sequence")
    private Long id;            // 主键
    
    @Column(nullable = false)
    private Long portalId;      // 定制页面所在门户ID
    
    @Column(nullable = false)
    private Long themeId;       // 主题ID
    
    @Column(nullable = false)
    private Long pageId;        // 定制页面ID
    
    @Column(nullable = false)
    private Long userId;        // 定制人ID
    private Date customizeTime; // 定制时间
    
    @Column(nullable = false)
    private String configInfo;  // 页面个性化定制XML配置文件信息
    
    public PersonalPage(){
    }
    
    public PersonalPage(Long portalId, Long themeId, Long pageId, Long userId, String configInfo){
        this.portalId = portalId;
        this.themeId = themeId;
        this.pageId = pageId;
        this.userId = userId;
        this.configInfo = configInfo;
        this.customizeTime = new Date();
    }
 
    public String getConfigInfo() {
        return configInfo;
    }
 
    public Date getCustomizeTime() {
        return customizeTime;
    }
 
    public Long getId() {
        return id;
    }
 
    public Long getPageId() {
        return pageId;
    }
 
    public Long getPortalId() {
        return portalId;
    }
 
    public Long getThemeId() {
        return themeId;
    }
 
    public Long getUserId() {
        return userId;
    }
 
    public void setConfigInfo(String configInfo) {
        this.configInfo = configInfo;
    }
 
    public void setCustomizeTime(Date customizeTime) {
        this.customizeTime = customizeTime;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }
 
    public void setPortalId(Long portalId) {
        this.portalId = portalId;
    }
 
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }
}
