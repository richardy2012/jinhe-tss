package com.jinhe.tss.portal.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.jinhe.tss.framework.persistence.IEntity;

/**
 * 门户实体：包括基本信息及主题信息等
 */
@Entity
@Table(name = "pms_portal")
@SequenceGenerator(name = "portal_sequence", sequenceName = "portal_sequence", initialValue = 1, allocationSize = 1)
public class Portal implements IEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "portal_sequence")
	private Long    id;
	
	private Long    themeId;    // 默认主题编号
	private String  themeName;  // 默认主题名称（默认为：XXXX(门户名称)的主题）
	
    private Long    currentThemeId;    // 当前主题编号
    private String  currentThemeName;  // 当前主题名称（默认为：XXXX(门户名称)的主题）
    
    @Transient private String  name;
    @Transient private Long personalThemeId;  // 用户个性化定制的主题
 
    /**
     * 获取用户自定义的主题ID，如果为空则返回默认主题ID
     * @return
     */
    public Long getPersonalThemeId() {
        return personalThemeId != null ? personalThemeId : themeId;
    }

    public void setPersonalThemeId(Long personalThemeId) {
        this.personalThemeId = personalThemeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
	public Long getId() {
		return id;
	}
 
	public void setId(Long id) {
		this.id = id;
	}
 
	public Long getThemeId() {
		return themeId;
	}
 
	public void setThemeId(Long themeId) {
		this.themeId = themeId;
	}
 
	public String getThemeName() {
		return themeName;
	}
 
	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}
 
    public Long getCurrentThemeId() {
        return currentThemeId;
    }
 
    public String getCurrentThemeName() {
        return currentThemeName;
    }
 
    public void setCurrentThemeId(Long currentThemeId) {
        this.currentThemeId = currentThemeId;
    }
 
    public void setCurrentThemeName(String currentThemeName) {
        this.currentThemeName = currentThemeName;
    }
 
    public String getDefaultKey() {
        return this.id + "_" + themeId;
    }
}
