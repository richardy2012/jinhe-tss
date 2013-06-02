package com.jinhe.tss.framework.component.param;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeTranslator;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.util.EasyUtils;
 
@Controller
@RequestMapping("/param")
public class ParamAction extends BaseActionSupport {

	@Autowired private ParamService paramService;
	
    
	/** 树型展示所有已配置参数 */
	@RequestMapping("/list")
	public void get2Tree() {
		List<?> allParams = paramService.getAllParams();
		TreeEncoder treeEncoder = new TreeEncoder(allParams, new LevelTreeParser());
		print("ParamTree", treeEncoder);
	}
    
    /** 刷新一下参数的缓存 */
	@RequestMapping("/cache/{paramId}")
    public void flushParamCache(@PathVariable("paramId") Long paramId) {
        ParamManager.remove(paramService.getParam(paramId).getCode());       
        printSuccessMessage();
    }

	/** 删除 */
	@RequestMapping(value = "/list/{paramId}", method = RequestMethod.DELETE)
	public void delParam(@PathVariable("paramId") Long paramId) {
		paramService.delete(paramId);		
		printSuccessMessage();
	}
	
	/**  新建、编辑 */
	@RequestMapping(method = RequestMethod.POST)
	public void saveParam(Param param) {
		boolean isnew = (null == param.getId());
        paramService.saveParam(param);
		doAfterSave(isnew, param, "ParamTree");
	}
	
	/** 取参数信息 */
	@RequestMapping(value = "/list/{paramId}", method = RequestMethod.GET)
	public String getParamInfo(int type, int mode, Long parentId, @PathVariable("paramId") Long paramId) {
        boolean isnew = isNew != null && TRUE.equals(isNew);
        XFormEncoder xformEncoder = null;
        
        String uri = null;
        if(ParamConstants.GROUP_PARAM_TYPE.equals(type)){
        	uri = ParamConstants.XFORM_NEW_GROUP;
        } 
        else if(ParamConstants.NORMAL_PARAM_TYPE.equals(type)){
        	if(ParamConstants.SIMPLE_PARAM_MODE.equals(mode)){
            	uri = ParamConstants.XFORM_NEW_PARAM_SIMPLE;
        	} else {
        		uri = ParamConstants.XFORM_NEW_PARAM_COMPLEX;
        	}
        } else if(ParamConstants.ITEM_PARAM_TYPE.equals(type)){
        	uri = ParamConstants.XFORM_NEW_PARAM_ITEM;
        }
        
        if( isnew ){
            Map<String, Object> map = new HashMap<String, Object>();
            parentId = parentId == null ? ParamConstants.DEFAULT_PARENT_ID : parentId;
            map.put("parentId", parentId);
            map.put("type", type);
            map.put("modality", mode);
            xformEncoder = new XFormEncoder(uri, map);
        } 
        else {
        	Param param = paramService.getParam(paramId);
            xformEncoder = new XFormEncoder(uri, param);
        }
        return print("ParamInfo", xformEncoder);
	}
	
	/** 停用、启用参数 */
	public String startOrStopParam(@PathVariable("paramId") Long paramId, int disabled) {
		paramService.startOrStop(paramId, disabled);
		return printSuccessMessage();
	}
	
	/** 参数排序 */
	public String sortParam(@PathVariable("paramId") Long paramId, Long targetId, int direction) {
		paramService.sortParam(paramId, targetId, direction);
		return printSuccessMessage();
	}
	
	/**  参数复制 */
	public String copyParam(@PathVariable("paramId") Long paramId, @PathVariable("paramId") Long toParamId) {
        Long targetId = null;
		if(toParamId != null) {
            targetId = "_rootId".equals(toParamId) ? ParamConstants.DEFAULT_PARENT_ID : new Long(toParamId);
		}
		
		List<?> result = paramService.copyParam(paramId, targetId);
		TreeEncoder encoder = new TreeEncoder(result, new LevelTreeParser());
		encoder.setNeedRootNode(false);
		return print("ParamTree", encoder);
	}
	
	/** 移动参数 */
	public String moveParam(@PathVariable("paramId") Long paramId, @PathVariable("paramId") Long toParamId) {
		Long id = "_rootId".equals(toParamId) ? ParamConstants.DEFAULT_PARENT_ID : new Long(toParamId);
		paramService.move(paramId, id);
		return printSuccessMessage();
	}
	
	/** 取可以新增的参数树 */
	public String getCanAddParamsTree(int type, int mode) {
		Object[] datas = ParamConstants.ITEM_PARAM_TYPE.equals(type) ? 
                paramService.getCanAddParams(mode) : paramService.getCanAddGroups();

        final String canAddIds = (String) datas[1];
        
        TreeEncoder paramTree = new TreeEncoder(datas[0], new LevelTreeParser());
        paramTree.setTranslator(new ITreeTranslator(){ 
            public Map<String, Object> translate(Map<String, Object> attributesMap) {
                if(EasyUtils.isNullOrEmpty(canAddIds)){
                    return attributesMap;
                }
                
                List<String> canAddParamIds = Arrays.asList(canAddIds.split(","));
                if(!canAddParamIds.contains(attributesMap.get("id").toString())) {
                    attributesMap.put("canselected", "0");
                }
                return attributesMap;
            }
        });
        
        //如果移动的不是参数组而是参数项，则"全部节点(Root Node)"不可选
        if (!ParamConstants.GROUP_PARAM_TYPE.equals(type)) {
            paramTree.setRootCanSelect(false);
        }
        
        return print("ParamTree", paramTree);
	}
    
    /***************************************** 以下为应用系统初始化相关 *************************************************/
    
    /**
     * 初始化应用系统，主要是生成appServer配置信息。
     * @return
     */
    public String initSystem(String code, String name, String value) {
        Param param = paramService.getParam(code);
        if(param == null){
            param = new Param();
            param.setCode(code);
            param.setName(name);
            param.setType(ParamConstants.NORMAL_PARAM_TYPE);
            param.setModality(ParamConstants.SIMPLE_PARAM_MODE);
            param.setParentId(ParamConstants.DEFAULT_PARENT_ID);
            paramService.saveParam(param);
        }
        param.setValue("<server code=" + code + " userDepositoryCode=\"tss\" name="
                + code + " sessionIdName=\"JSESSIONID\" baseURL=" + value  + "/>");
        paramService.saveParam(param);
       
        String msg = Context.getApplicationContext().getCurrentAppCode() + "应用里设置(" + code + ")应用配置信息成功";
        return printSuccessMessage(msg);
    }
    
}	