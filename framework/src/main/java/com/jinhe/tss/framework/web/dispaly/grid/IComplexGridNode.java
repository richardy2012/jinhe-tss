package com.jinhe.tss.framework.web.dispaly.grid;

import java.util.List;

/** 
 * 复合节点接口
 * 
 */
public interface IComplexGridNode extends IGridNode {
    
	/**
	 * 获取节点说明信息 格式：每条信息生成一个GridAttributesMap对象，然后加到List中返回
	 * 
	 * @return
	 */
	public List<GridAttributesMap> getNodeDetails();
	
}
