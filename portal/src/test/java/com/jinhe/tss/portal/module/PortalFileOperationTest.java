package com.jinhe.tss.portal.module;

import java.io.File;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.TxSupportTest4Portal;
import com.jinhe.tss.portal.action.FileAction;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.URLUtil;

/**
 * 管理门户、页面、布局器、修饰器、布局器等的附件资源的模块的单元测试。
 */
public class PortalFileOperationTest extends TxSupportTest4Portal {
    
	@Autowired FileAction fileAction;
 
	@Test
    public void test() {
        String modelDir = URLUtil.getWebFileUrl(PortalConstants.MODEL_DIR).getPath();
        FileHelper.createDir(modelDir);
        
        File testFile = new File(modelDir + "/layout/layout12/test.txt");
        FileHelper.writeFile(testFile, "Just Test!");
        
        request = new MockHttpServletRequest();
        request.addParameter("type", "layout");
        request.addParameter("code", "layout12");
        request.addParameter("filter", "txt");
        fileAction.listAvailableFiles(response, request);
        
        String layoutPath = PortalConstants.MODEL_DIR + "layout/layout12";
        
        request = new MockHttpServletRequest();
		request.addParameter("contextPath", layoutPath);
    	request.addParameter("fileNames", "test.txt");
        fileAction.download(response, request);
        
        File testFile2 = new File(modelDir + "/layout/xxx.txt");
        FileHelper.writeFile(testFile2, "Just Test 222222!");
        
        super.uploadFile(layoutPath, testFile2);
        
        request = new MockHttpServletRequest();
    	request.addParameter("contextPath", layoutPath);
    	request.addParameter("newFileName", "temp");
    	fileAction.addDir(response, request);
    	
    	request = new MockHttpServletRequest();
    	request.addParameter("contextPath", layoutPath);
    	request.addParameter("fileName", "temp");
    	request.addParameter("newFileName", "temp2");
        fileAction.renameFile(response, request);
        
        request = new MockHttpServletRequest();
        request.addParameter("type", "layout");
        request.addParameter("code", "layout12");
        fileAction.listAvailableFiles(response, request);
        
        request = new MockHttpServletRequest();
        request.addParameter("contextPath", layoutPath);
    	request.addParameter("fileNames", "xxx.txt");
    	request.addParameter("folderNames", "temp2");
    	fileAction.deleteFile(response, request);
    }
	
    
}
