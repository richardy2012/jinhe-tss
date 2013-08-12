package com.jinhe.tss.portal.module;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    public void testPortalFileOperation() {
        String modelDir = URLUtil.getWebFileUrl(PortalConstants.PORTAL_MODEL_DIR).getPath();
        FileHelper.createDir(modelDir);
        
        File testFile = new File(modelDir + "/File111.txt");
        FileHelper.writeFile(testFile, "Just Test!");
        
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("type", new String[] {"site"});
        paramsMap.put("id",   new String[] {"111"});
        paramsMap.put("code", new String[] {"File"});
        fileAction.listAvailableFiles(null, "txt", testFile.getParentFile(), paramsMap);
        
        fileAction.listAvailableFiles("portal", "txt", testFile.getParentFile(), paramsMap);
        
        fileAction.download("portal", "File111.txt");
        
        fileAction.upload("portal", testFile); 
        
        modelDir = URLUtil.getWebFileUrl(PortalConstants.MODEL_DIR + "layout").getPath();
        testFile = new File(modelDir + "/File111.txt");
        FileHelper.writeFile(testFile, "Just Test!");
        
        paramsMap.put("type", new String[] {"layout"});
    	fileAction.listAvailableFiles("layout", "txt", testFile.getParentFile(), paramsMap);
    	
    	fileAction.addDir("layout", "newFolder");
    	
        fileAction.renameFile("layout", "File111.txt", "File222.txt");
    	
    	fileAction.deleteFile("layout", "File222.txt", "newFolder");
    }
}
