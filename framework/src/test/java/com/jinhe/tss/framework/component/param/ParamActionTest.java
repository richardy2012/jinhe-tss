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
        Param treeParam = addTreeParam(paramGroup2.getId(), "group", "用户组织");
        
        Param temp = addParamItem(treeParam.getId(), "group1", "研发部", ParamConstants.TREE_PARAM_MODE);
        addParamItem(temp.getId(), "group2", "IT部", ParamConstants.TREE_PARAM_MODE);
        addParamItem(treeParam.getId(), "group3", "财务部", ParamConstants.TREE_PARAM_MODE);
        
		action.get2Tree(response);
		
		action.copyParam(response, treeParam.getId(), paramGroup.getId().toString());
		
		action.flushParamCache(response, treeParam.getId());
		
		action.getCanAddParamsTree(response, treeParam.getId());
		
		action.getParamInfo(request, response, ParamConstants.NORMAL_PARAM_TYPE);
		action.getParamInfo(request, response, ParamConstants.GROUP_PARAM_TYPE);
		action.getParamInfo(request, response, ParamConstants.ITEM_PARAM_TYPE);
		
		request.addParameter("mode", ParamConstants.SIMPLE_PARAM_MODE.toString());
		request.addParameter("paramId", treeParam.getId().toString());
		action.getParamInfo(request, response, ParamConstants.NORMAL_PARAM_TYPE);
		
		action.saveParam(response, comboParam);
		action.initAppConfig(response, "test", "test", "test");
		
		Param paramGroup3 = addParamGroup(ParamConstants.DEFAULT_PARENT_ID, "测试参数组3");
		action.moveParam(response, treeParam.getId(), paramGroup3.getId().toString());
		
		action.sortParam(response, paramGroup.getId(), paramGroup3.getId(), 1);
		action.sortParam(response, paramGroup.getId(), paramGroup3.getId(), -1);
		
		action.startOrStopParam(response, paramGroup.getId(), 1);
		action.startOrStopParam(response, comboParam.getId(), 0);
		
		action.get2Tree(response);
		
		action.delParam(response, paramGroup.getId());
		action.delParam(response, paramGroup2.getId());
		
		action.get2Tree(response);
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
