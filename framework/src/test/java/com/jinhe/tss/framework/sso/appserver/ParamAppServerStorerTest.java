package com.jinhe.tss.framework.sso.appserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.TxTestSupport;
import com.jinhe.tss.framework.component.param.Param;
import com.jinhe.tss.framework.component.param.ParamConstants;
import com.jinhe.tss.framework.component.param.ParamService;
import com.jinhe.tss.util.XMLDocUtil;

public class ParamAppServerStorerTest extends TxTestSupport {
    
    @Autowired private ParamService paramService;
    
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
        
        List<?> list = paramService.getAllParams();
        assertTrue(list.size() > 0);
        
        AppServer appServer = new ParamAppServerStorer().getAppServer("TSS");
		assertEquals("TSS", appServer.getCode());
        
        paramService.delete(group.getId());
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
}
