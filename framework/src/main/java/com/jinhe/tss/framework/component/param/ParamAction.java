package com.jinhe.tss.framework.component.param;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.web.dispaly.tree.ITreeTranslator;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.util.EasyUtils;
 
public class ParamAction extends BaseActionSupport {

	private ParamService paramService;
    
	private Param param = new Param();
    
	private Integer type;
	private Integer mode;
	private Long parentId;
	private Long paramId;
	private String  toParamId;
	private Integer disabled;
	private Long targetId;
	private int direction;
    
	/** 树型展示所有已配置参数 */
	public String get2Tree(){
		TreeEncoder treeEncoder = new TreeEncoder(paramService.getAllParams(), new LevelTreeParser());
		return print("ParamTree", treeEncoder);
	}
    
    /** 刷新一下参数的缓存 */
    public String flushParamCache() {
        ParamManager.remove(paramService.getParam(paramId).getCode());       
        return printSuccessMessage();
    }

	/** 删除 */
	public String delParam() {
		paramService.delete(paramId);		
		return printSuccessMessage();
	}
	
	/**  新建、编辑 */
	public String saveParam(){
		boolean isnew = (null == param.getId());
        paramService.saveParam(param);
		return doAfterSave(isnew, param, "ParamTree");
	}
	
	/** 取参数信息 */
	public String getParamInfo() {
        boolean isnew = isNew != null && ParamConstants.IS_NEW_TAG.equals(isNew);
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
	public String startOrStopParam(){
		paramService.startOrStop(paramId, disabled);
		return printSuccessMessage();
	}
	
	/** 参数排序 */
	public String sortParam(){
		paramService.sortParam(paramId, targetId, direction);
		return printSuccessMessage();
	}
	
	/**  参数复制 */
	public String copyParam(){
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
	public String moveParam(){
		Long id = "_rootId".equals(toParamId) ? ParamConstants.DEFAULT_PARENT_ID : new Long(toParamId);
		paramService.move(paramId, id);
		return printSuccessMessage();
	}
	
	/** 取可以新增的参数树 */
	public String getCanAddParamsTree(){
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
    
    private String code;
    private String name;
    private String value;
    
    /**
     * 初始化应用系统，主要是生成appServer配置信息。
     * @return
     */
    public String initSystem(){
        value = "<server code=" + code + " userDepositoryCode=\"tss\" name="
                + code + " sessionIdName=\"JSESSIONID\" baseURL=" + value  + "/>";
        
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
        param.setValue(value);
        paramService.saveParam(param);
       
        String msg = Context.getApplicationContext().getCurrentAppCode() + "应用里设置(" + code + ")应用配置信息成功";
        return printSuccessMessage(msg);
    }
    
    
    /***************************************************************************************************************/
	
 
	public void setParamService(ParamService paramService) {
		this.paramService = paramService;
	}
 
	public Param getParam() {
		return param;
	}
 
	public void setParamId(Long paramId) {
		this.paramId = paramId;
	}
 
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
 
	public void setType(Integer type) {
		this.type = type;
	}
 
	public void setMode(Integer mode) {
		this.mode = mode;
	}	
 
	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}
 
	public void setDirection(int direction) {
		this.direction = direction;
	}
 
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
 
	public void setToParamId(String toParamId) {
		this.toParamId = toParamId;
	}
 
    public void setCode(String code) {
        this.code = code;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public void setValue(String value) {
        this.value = value;
    }
}	