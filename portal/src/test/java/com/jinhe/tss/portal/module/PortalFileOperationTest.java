//package com.jinhe.tss.portal.module;
//
//import java.io.File;
//
//import com.jinhe.tss.portal.PortalConstants;
//import com.jinhe.tss.portal.TxSupportTest4Portal;
//import com.jinhe.tss.portal.action.FileAction;
//import com.jinhe.tss.util.FileHelper;
//import com.jinhe.tss.util.URLUtil;
//
///**
// * 管理门户、页面、布局器、修饰器、布局器等的附件资源的模块的单元测试。
// */
//public class PortalFileOperationTest extends TxSupportTest4Portal {
//    
//	FileAction fileAction;
//	
//    public void setUp() throws Exception {
//        super.setUp();
//        fileAction = new FileAction();
//    }
// 
//    public void testPortalFileOperation() {
//        String modelDir = URLUtil.getWebFileUrl(PortalConstants.PORTAL_MODEL_DIR).getPath();
//        FileHelper.createDir(modelDir);
//        
//        File testFile = new File(modelDir + "/File111.txt");
//        FileHelper.writeFile(testFile, "Just Test!");
//        
//        fileAction.setFile(testFile.getParentFile());
//        fileAction.setFilter("txt");
//        fileAction.getParamsMap().put("type", new String[] {"site"});
//        fileAction.getParamsMap().put("id",   new String[] {"111"});
//        fileAction.getParamsMap().put("code", new String[] {"File"});
//        fileAction.listAvailableFiles();
//        
//        fileAction.setContextPath("portal");
//        fileAction.listAvailableFiles();
//        
//        fileAction.setContextPath("portal");
//        fileAction.setFileNames("File111.txt");
//        fileAction.download();
//        
//        fileAction.setContextPath("portal");
//        fileAction.setFile(testFile);
//        fileAction.upload(); 
//        
//        modelDir = URLUtil.getWebFileUrl(PortalConstants.LAYOUT_MODEL_DIR).getPath();
//        testFile = new File(modelDir + "/File111.txt");
//        FileHelper.writeFile(testFile, "Just Test!");
//        fileAction.setFile(testFile.getParentFile());
//        fileAction.getParamsMap().put("type", new String[] {"layout"});
//    	fileAction.listAvailableFiles();
//    	
//    	fileAction.setContextPath("layout");
//    	fileAction.setNewFileName("newFolder");
//    	fileAction.addDir();
//    	
//    	fileAction.setContextPath("layout");
//        fileAction.setFileName("File111.txt");
//        fileAction.setNewFileName("File222.txt");
//        fileAction.renameFile();
//    	
//    	fileAction.setContextPath("layout");
//    	fileAction.setFileNames("File222.txt");
//    	fileAction.setFolderNames("newFolder");
//    	fileAction.deleteFile();
//    }
//    
//}
