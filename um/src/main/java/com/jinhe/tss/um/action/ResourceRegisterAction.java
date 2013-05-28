package com.jinhe.tss.um.action;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.service.IResourceRegisterService;

public class ResourceRegisterAction extends BaseActionSupport {

	private File file;
	
	private String applicationType;
	
	private IResourceRegisterService service;
	
	// 返回一个空的无数据的模板
	public String getImportTemplate(){
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("applicationType", applicationType);
		XFormEncoder encoder = new XFormEncoder(UMConstants.IMPORT_APP_XFORM, map);
		return print("ImportApplication", encoder);
	}
	
	public String applicationRegisterByXML(){
		if (null == file) {
			throw new BusinessException("没有选择文件，请重新导入！");
		}
		
		if (!file.getName().endsWith(".xml")) {
            return print("SCRIPT", "parent.alert(\"文件格式不正确，请导入xml文件！\");");
		}
		
        try {
            Document doc = new SAXReader().read(file);
            service.applicationRegisterByXML(doc, applicationType);
        } catch (Exception e) {
            log.error("导入失败，请查看日志信息！", e);
            return print("SCRIPT", "parent.alert(\"导入失败，请查看日志信息！\");");
        }
        return print("SCRIPT", "parent.alert(\"导入成功！\");var ws = parent.$(\"ws\");ws.closeActiveTab();parent.loadInitData();");
	}
 
	public void setFile(File file) {
		this.file = file;
	}
 
	public void setService(IResourceRegisterService service) {
		this.service = service;
	}
 
	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}
}

	