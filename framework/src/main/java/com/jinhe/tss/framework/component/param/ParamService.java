package com.jinhe.tss.framework.component.param;

import java.util.List;
 
public interface ParamService {

	/** 保存参数 */
	Param saveParam(Param param);
	
	/** 停用、启用参数 */
	void startOrStop(Long paramId, Integer disabled);
	
	/** 删除参数 */
	void delete(Long paramId);

	/** 取所有参数 */
	List<?> getAllParams();
	
	/** 根据code取参数。供ParamManager使用 */
	Param getParam(String code);
	
	/** 根据ID取参数 */
	Param getParam(Long id);
	
	/** 根据code取简单参数的值 */
	String getSimpleParamValue(String code);
	
	/** 取下拉型参数的值 */
	List<Param> getComboParam(String code);
	
	/** 取树型参数的值 */
	List<Param> getTreeParam(String code);
	
	/**
	 * <p>
	 * 参数排序
	 * </p>
	 * @param paramId
	 * @param toParamId
	 * @param direction
	 */
	void sortParam(Long paramId, Long toParamId, int direction);
	
	/**
	 * <p>
	 * 复制参数
	 * </p>
	 * @param paramId
	 * @param toParamId
	 */
	List<?> copyParam(Long paramId, Long toParamId);
	
	/**
	 * <p>
	 * 移动参数
	 * </p>
	 * @param paramId
	 * @param toParamId
	 */
	void move(Long paramId, Long toParamId);
	
	/** 取可以加参数项的所有参数 */
	Object[] getCanAddParams(Integer mode);
	
	/** 取可以添加参数或者参数组的参数组 */
	Object[] getCanAddGroups();
}
