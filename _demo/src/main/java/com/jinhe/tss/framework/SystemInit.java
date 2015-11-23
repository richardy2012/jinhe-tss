package com.jinhe.tss.framework;

import com.jinhe.tss.framework.component.param.Param;
import com.jinhe.tss.framework.component.param.ParamConstants;
import com.jinhe.tss.framework.component.param.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/init")
public class SystemInit {
	
	@Autowired protected ParamService paramService;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Object init() {
		String[][] items = new String[][]{ 
        		{"1", "停用"}, 
        		{"0", "启用"} 
        	};
        addComboParam("EntityState", "对象状态", items);
		
		items = new String[][]{ 
				{ "1", "超级管理员"},
	        	{ "2", "管理用户"},
	        	{ "3", "实操用户"},
	        	{ "4", "网点编号"}
        	};
        addComboParam("UserType", "用户类型", items);
		
		
        
		return new Object[] { "Success" };
	}
	
	void addComboParam(String code, String name, String[][] items) {
		Param cp;
		List<Param> list;
		
		if( (cp = paramService.getParam(code)) != null) {
			list = paramService.getComboParam(code);
		}
		else {
			cp = addComboParam(ParamConstants.DEFAULT_PARENT_ID, code, name);
			list = new ArrayList<Param>();
		}
		
		L:for(String[] item : items) {
			for(Param p : list) {
				if(p.getValue().equals(item[0])) {
					p.setText(item[1]);
					paramService.saveParam(p);
					continue L;
				}
			}
			addComboItem(cp.getId(), item[0], item[1]);
		}
	}
	
	
    /** 简单参数 */
    Param addParam(Long parentId, String code, String name, String value) {
        Param param = new Param();
        param.setCode(code);
        param.setName(name);
        param.setValue(value);
        param.setParentId(parentId);
        param.setType(ParamConstants.NORMAL_PARAM_TYPE);
        param.setModality(ParamConstants.SIMPLE_PARAM_MODE);
        paramService.saveParam(param);
        return param;
    }

    /** 下拉型参数 */
    Param addComboParam(Long parentId, String code, String name) {
        Param param = new Param();
        param.setCode(code);
        param.setName(name);
        param.setParentId(parentId);
        param.setType(ParamConstants.NORMAL_PARAM_TYPE);
        param.setModality(ParamConstants.COMBO_PARAM_MODE);
        paramService.saveParam(param);
        return param;
    }

    /** 新建设参数项 */
    Param addComboItem(Long parentId, String value, String text) {
        Param param = new Param();
        param.setValue(value);
        param.setText(text);
        param.setParentId(parentId);
        param.setType(ParamConstants.ITEM_PARAM_TYPE);
        paramService.saveParam(param);
        return param;
    }
}
