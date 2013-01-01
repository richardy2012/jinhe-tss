package com.jinhe.tss.portal.helper;

/** 
 * Portlet，修饰器，布局器等门户元素的公共接口
 */
public interface IElement {
    Long    getId();
    String  getName();
    Long    getGroupId();
    Integer getSeqNo();
    Integer getDisabled();
    String  getDefinition();
    String  getCode();
    Integer getIsDefault();
    String  getResourceBaseDir();
    String  getResourcePath();
    String  getElementName();
    
    void   setId(Long Id);
    void   setGroupId(Long groupId);
    void   setSeqNo(Integer seqNo);
    void   setDisabled(Integer disabled);
    void   setIsDefault(Integer isDefault);
    void   setCode(String code);
    void   setName(String name);
    void   setDescription(String description);
    void   setDefinition(String definition);
    void   setVersion(String version);
}

