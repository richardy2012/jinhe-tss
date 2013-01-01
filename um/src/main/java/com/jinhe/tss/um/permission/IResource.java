package com.jinhe.tss.um.permission;

/**
 * <p>
 * 所有"资源视图实体"需要实现的接口。
 * （PermissionInterceptor 将根据本接口判断是否对资源进行自动补齐）
 * </p>
 */
public interface IResource {
	
	/** 资源id  */
	Long getId();
	
	/** 资源排序号 */
	Integer getSeqNo();
	
	/** 资源父节点id */
	Long getParentId();
	
	/** 资源名称 */
	String getName();
	
	/** 资源类型 */
	String getResourceType();
	
}

	