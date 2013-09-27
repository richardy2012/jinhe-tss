package com.jinhe.tss.framework.component.param;

import java.util.List;

import com.jinhe.tss.framework.component.cache.Cached;
 
public interface ParamService {

	/** 保存参数 */
	Param saveParam(Param param);
	
	/** 停用、启用参数 */
	void startOrStop(Long paramId, Integer disabled);
	
	/** 删除参数 */
	void delete(Long paramId);

	/** 取所有参数 */
	List<?> getAllParams();
	
	/** 根据ID取参数 */
	Param getParam(Long id);
	
	/** 根据code取参数。供ParamManager使用 */
	@Cached
	Param getParam(String code);
	
	/** 取下拉型参数的值 */
	@Cached
	List<Param> getComboParam(String code);
	
	/** 取树型参数的值 */
	@Cached
	List<Param> getTreeParam(String code);
	
	/**
	 * 参数排序
	 * @param paramId
	 * @param toParamId
	 * @param direction
	 */
	void sortParam(Long paramId, Long toParamId, int direction);
	
	/**
	 * 复制参数
	 * @param paramId
	 * @param toParamId
	 */
	List<?> copyParam(Long paramId, Long toParamId);
	
	/**
	 * 移动参数
	 * @param paramId
	 * @param toParamId
	 */
	void move(Long paramId, Long toParamId);
	
	/** 取可以添加参数或者参数组的参数组 */
	List<?> getCanAddGroups();
}
