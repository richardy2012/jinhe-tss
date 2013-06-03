package com.jinhe.tss.framework.component.param;

import java.util.List;

import com.jinhe.tss.framework.persistence.ITreeSupportDao;
 
public interface ParamDao extends ITreeSupportDao<Param>{

	List<?> getAllParam();
	
	/**
     * 根据code值获取参数。（区分参数组、参数、参数项的概念）
	 * @param code
	 * @return
	 */
	Param getParamByCode(String code);
	
	List<?> getCanAddGroups();
}
