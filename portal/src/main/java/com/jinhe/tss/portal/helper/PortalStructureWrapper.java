package com.jinhe.tss.portal.helper;

import java.util.Map;

import com.jinhe.tss.portal.entity.Portal;
import com.jinhe.tss.portal.entity.PortalStructure;
import com.jinhe.tss.util.BeanUtil;

/**
 * 门户结构节点 + 主题信息（取至Portal对象），比普通的门户结构节点多了主题信息。
 */
public class PortalStructureWrapper extends PortalStructure {

    private Long    themeId;            //主题编号
    private String  themeName;          //主题名称（默认为：XXXX(门户名称)的主题）
    private Long    currentThemeId;     //当前主题编号
    private String  currentThemeName;   //当前主题名称
    
    public PortalStructureWrapper(){       
    }
    
    public PortalStructureWrapper(PortalStructure portalStructure, Portal portal) {
        if(portal != null){
            BeanUtil.copy(this, portal);
        }        
        BeanUtil.copy(this, portalStructure);    
        
        this.setId(portalStructure.getId());
    }
    
    public Portal getPortal(){
        Portal portal = new Portal();
        BeanUtil.copy(portal, this);
        portal.setId(this.getPortalId());
        
        return portal;
    }
    
    public PortalStructure getPortalStructure(){
        PortalStructure portalStructure = new PortalStructure();
        BeanUtil.copy(portalStructure, this);
        portalStructure.setId(this.getId());
        
        return portalStructure;
    }
 
    public Long getThemeId() {
        return themeId;
    }
 
    public String getThemeName() {
        return themeName;
    }
 
    public void setThemeId(Long themeId) {
        this.themeId = themeId;
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
    
    public Map<String, Object> getAttributesForXForm() {
        Map<String, Object> map = super.getAttributesForXForm();
        
        map.put("themeId", this.themeId);
        map.put("themeName", this.themeName);
        map.put("currentThemeId", this.currentThemeId);
        map.put("currentThemeName", this.currentThemeName);
        
        return map;
    }
}

