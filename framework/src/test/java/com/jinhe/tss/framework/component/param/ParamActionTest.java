package com.jinhe.tss.framework.component.param;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.TxTestSupport;

public class ParamActionTest extends TxTestSupport {
	
	@Autowired private ParamAction action;

    @Autowired private ParamService paramService;
 
    @Test
	public void testParamAction() {
		Param paramGroup = addParamGroup(ParamConstants.DEFAULT_PARENT_ID, "测试参数组1");
        Param comboParam = addComboParam(paramGroup.getId(), "book", "可选书籍");
        
        addParamItem(comboParam.getId(), "Thinking in JAVA", "Thinking in JAVA", ParamConstants.COMBO_PARAM_MODE);
        addParamItem(comboParam.getId(), "Effictive JAVA", "Effictive JAVA", ParamConstants.COMBO_PARAM_MODE);
        addParamItem(comboParam.getId(), "Design Pattern", "Design Pattern", ParamConstants.COMBO_PARAM_MODE);
        
        Param paramGroup2 = addParamGroup(ParamConstants.DEFAULT_PARENT_ID, "测试参数组2");
        Param treeParam = addTreeParam(paramGroup2.getId(), "group", "组织");
        
        Param temp = addParamItem(treeParam.getId(), "group1", "组一", ParamConstants.TREE_PARAM_MODE);
        addParamItem(temp.getId(), "group2", "组二", ParamConstants.TREE_PARAM_MODE);
        addParamItem(treeParam.getId(), "group3", "组三", ParamConstants.TREE_PARAM_MODE);
        
		action.get2Tree(null);
	}
	
    /** 建参数组 */
    private Param addParamGroup(Long parentId, String name) {
        Param param = new Param();
        param.setName(name);
        param.setParentId(parentId);
        param.setType(ParamConstants.GROUP_PARAM_TYPE);
        
        return paramService.saveParam(param);
    }

    /** 下拉型参数 */
    private Param addComboParam(Long parentId, String code, String name) {
        Param param = new Param();
        param.setCode(code);
        param.setName(name);
        param.setParentId(parentId);
        param.setType(ParamConstants.NORMAL_PARAM_TYPE);
        param.setModality(ParamConstants.COMBO_PARAM_MODE);
        paramService.saveParam(param);
        return param;
    }
    
    /** 树型参数 */
    private Param addTreeParam(Long parentId, String code, String name) {
        Param param = new Param();
        param.setCode(code);
        param.setName(name);
        param.setParentId(parentId);
        param.setType(ParamConstants.NORMAL_PARAM_TYPE);
        param.setModality(ParamConstants.TREE_PARAM_MODE);
        paramService.saveParam(param);
        return param;
    }

    /** 新建设参数项 */
    private Param addParamItem(Long parentId, String value, String text, Integer mode) {
        Param param = new Param();
        param.setValue(value);
        param.setText(text);
        param.setParentId(parentId);
        param.setType(ParamConstants.ITEM_PARAM_TYPE);
        param.setModality(mode);
        paramService.saveParam(param);
        return param;
    }

    
}
