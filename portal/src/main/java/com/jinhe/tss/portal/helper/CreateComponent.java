package com.jinhe.tss.portal.helper;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.web.servlet.AfterUpload;
import com.jinhe.tss.portal.entity.Component;
import com.jinhe.tss.portal.service.IComponentService;
import com.jinhe.tss.util.URLUtil;

public class CreateComponent implements AfterUpload {

	Logger log = Logger.getLogger(this.getClass());

	public String processUploadFile(HttpServletRequest request,
			String filepath, String oldfileName) throws Exception {
            
        IComponentService service = (IComponentService) Global.getBean("ComponentService");
        Long groupId = Long.parseLong(request.getParameter("groupId"));
        Component group = service.getComponent(groupId);
        String desDir = URLUtil.getWebFileUrl(group.getResourceBaseDir()).getPath(); 
        
        File targetFile = new File(filepath);
        String eXMLFile = group.getComponentType() + ".xml";
        
        Component component = new Component();
        component.setParentId(group.getId());
        component.setType(group.getType());
        ComponentHelper.importComponent(service, targetFile, component, desDir, eXMLFile);
        
		return "parent.alert('导入成功!');parent.loadInitData();";
	}
}
