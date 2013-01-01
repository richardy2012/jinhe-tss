package com.jinhe.tss.portal.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
 
@Embeddable
public class ThemeInfoId  implements Serializable {
	
    private static final long serialVersionUID = -2913245080847049534L;
    
    private Long    themeId;    //主题ID
    private Long    portalStructureId; //门户结构ID
    
    public ThemeInfoId(){
    }
    
    public ThemeInfoId(Long themeId, Long portalStructureId){
        this.themeId = themeId;
        this.portalStructureId = portalStructureId;
    }
  
    public Long getThemeId() {
		return themeId;
	}

	public Long getPortalStructureId() {
		return portalStructureId;
	}

	public void setThemeId(Long themeId) {
		this.themeId = themeId;
	}

	public void setPortalStructureId(Long portalStructureId) {
		this.portalStructureId = portalStructureId;
	}

	public boolean equals(Object obj) {
        if(obj instanceof ThemeInfoId){
            ThemeInfoId object = (ThemeInfoId) obj;
            return this.themeId.equals(object.getThemeId())
                    && this.portalStructureId.equals(object.getPortalStructureId());
        }
        return false;
    }

    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + this.themeId.hashCode();
        hash = hash * 31 + this.portalStructureId.hashCode();

        return hash;
    }
}

