package com.jinhe.tss.um.permission;

import javax.persistence.MappedSuperclass;

import com.jinhe.tss.framework.web.dispaly.tree.ITreeNode;
import com.jinhe.tss.framework.web.dispaly.tree.TreeAttributesMap;

/**
 * 补齐表超类。
 * 补齐表比未补齐表多一个资源名称字段，同时还实现了ITreeNode接口，因其要作为一个节点在权限树上展示。
 */
@MappedSuperclass
public abstract class AbstractSuppliedTable extends AbstractUnSuppliedTable implements ISuppliedPermission, ITreeNode {
	
    protected String  resourceName; // 资源名称
 
    public String getResourceName() {
        return resourceName;
    }
 
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public TreeAttributesMap getAttributes() {
        return new TreeAttributesMap(id, resourceName);
    }
    
    public String toString() {
    	return super.toString() + ", resourceName = " + resourceName;
    }
}
