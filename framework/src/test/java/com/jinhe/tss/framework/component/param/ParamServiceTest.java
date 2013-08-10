package com.jinhe.tss.framework.component.param;

import static org.junit.Assert.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.TxTestSupport;
import com.jinhe.tss.framework.sso.appserver.AppServer;
import com.jinhe.tss.util.XMLDocUtil;

public class ParamServiceTest extends TxTestSupport {
    
    @Autowired private ParamService paramService;

    /** 导入application.properties文件 */
    @Test
    public void testImportApplicationProperties(){
        ResourceBundle resources = ResourceBundle.getBundle("application", Locale.getDefault());
        if (resources == null) return;
        
        Param group = addParamGroup(ParamConstants.DEFAULT_PARENT_ID, "系统参数");
        for (Enumeration<String> enumer = resources.getKeys(); enumer.hasMoreElements();) {
            String key = enumer.nextElement();
            String value = resources.getString(key);
            addSimpleParam(group.getId(), key, key, value);
        }
        
        printParams();
        paramService.delete(group.getId());
    }
    
    /** 导入应用服务配置文件 appServers.xml */
    @Test
    public void testImportAppServerConfig(){
        Param group = addParamGroup(ParamConstants.DEFAULT_PARENT_ID, "应用服务配置");
        
        Document doc = XMLDocUtil.createDoc("appServers.xml");
        List<?> elements = doc.getRootElement().elements();
        for (Iterator<?> it = elements.iterator(); it.hasNext();) {
            Element element = (Element) it.next();
            String name = element.attributeValue("name");
            String code = element.attributeValue("code");
            addSimpleParam(group.getId(), code, name, element.asXML());
        }
        
        printParams();
        
        AppServer appServer = new ParamAppServerStorer().getAppServer("TSS");
		assertEquals("TSS", appServer.getCode());
        
        paramService.delete(group.getId());
    }
    
    private void printParams() {
        List<?> list = paramService.getAllParams();
        assertTrue(list.size() > 0);
        
        for(Object temp :list) {
            Param p = (Param) temp;
            log.debug(p.getAttributesForXForm());
        }
    }
    
    /** CRUD/排序/移动/复制/停用启用等  */
    @Test
    public void testParamFunction() {
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
        
        printParams();
        paramService.startOrStop(treeParam.getId(), 1);
        paramService.startOrStop(treeParam.getId(), 0);
        
        paramService.sortParam(paramGroup2.getId(), paramGroup.getId(), -1);
        paramService.copyParam(treeParam.getId(), paramGroup.getId());
        
        printParams();
        
    }

    /** 建参数组 */
    private Param addParamGroup(Long parentId, String name) {
        Param param = new Param();
        param.setName(name);
        param.setParentId(parentId);
        param.setType(ParamConstants.GROUP_PARAM_TYPE);
        
        return paramService.saveParam(param);
    }

    /** 简单参数 */
    private Param addSimpleParam(Long parentId, String code, String name, String value) {
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
