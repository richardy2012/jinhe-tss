package com.jinhe.tss.um.permission;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.jinhe.tss.framework.persistence.IEntity;
import com.jinhe.tss.framework.persistence.entityaop.IDecodable;
import com.jinhe.tss.framework.web.dispaly.tree.ILevelTreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.TreeAttributesMap;

/** 
 * 可授权资源实体的抽象类
 */
@MappedSuperclass
public abstract class AbstractResourcesView implements IEntity, IDecodable, ILevelTreeNode, IResource{
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long    id;       // 主键Id
    protected Long    parentId; // 父节点ID 
    
    @Column(nullable = false)  
    protected String  name;   // 名称:资源名称
    
    protected Integer seqNo;  // 资源编号 
    protected String  decode; // 层码
    protected Integer levelNo;// 层次值
    
    public String toString() {
    	return "id = " + id + ", parentId = " + parentId + ", name = " + name ;
    }
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public Long getParentId() {
        return parentId;
    }
 
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }

    public TreeAttributesMap getAttributes() {
        return new TreeAttributesMap(id, name);
    }
 
    public Integer getSeqNo() {
        return seqNo;
    }
 
    public void setSeqNo(Integer userOrder) {
        this.seqNo = userOrder;
    }
 
    public String getDecode() {
        return decode;
    }
 
    public void setDecode(String decode) {
        this.decode = decode;
    }
 
    public Integer getLevelNo() {
        return levelNo;
    }
 
    public void setLevelNo(Integer levelNo) {
        this.levelNo = levelNo;
    }

    public Class<?> getParentClass() {
        return getClass();
    }

    public abstract String getResourceType();

}

