package com.jinhe.tss.portal.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.entity.Component;
import com.jinhe.tss.portal.helper.ElementHelper;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.URLUtil;

/**
 * 管理门户、页面、布局器、修饰器、布局器等的附件资源。
 */
public class FileAction extends BaseActionSupport {
 
    /**
     * 列出文件列表
     * 
     * @param contextPath  当前操作文件所在目录上下文
     * @param filter    文件后缀名
     * @param file
     * @param paramsMap  参数列表 paramsMap['" + param + "']"
     */
    public void listAvailableFiles(String contextPath, String filter, File file, Map<String, Object> paramsMap){
        String id   = getParamFromMap(paramsMap, "id");  
        String code = getParamFromMap(paramsMap, "code");
        String type = getParamFromMap(paramsMap, "type"); // 判断是何种类型的资源管理
        if(type == null) {
            throw new BusinessException("没有指定文件类型");
        }
        
        StringBuffer sb = new StringBuffer("<actionSet title=\"\" openednodeid=\"r1.1\">"); 
        
        // 如果contextPath不为空，则直接使用contextPath值作为根目录；否则根据type值找根目录
        File baseDir = null;
        String portalModelDir = URLUtil.getWebFileUrl(PortalConstants.PORTAL_MODEL_DIR).getPath();
        File portalRootDir = new File(portalModelDir + "/" + code + "_" + id); // 指定门户的资源根目录
        if( contextPath != null ){
            contextPath = getContextPath(contextPath);
            if( (baseDir = new File(contextPath)).equals(portalRootDir) ) {
                // 不用目录上翻的按钮，以免翻到目录外面去。
            } 
            else {
                // .. 为目录上翻的按钮
                sb.append("<treeNode id=\"-1\" name=\"..\" icon=\"../framework/images/folder.gif\" />"); 
            }
        } 
        else {
            if( "site".equalsIgnoreCase(type) ) { // 门户结构
                baseDir = portalRootDir;        
            }
            else if(Arrays.asList(Component.ELEMENTS).contains(type)) { // 门户元素
                String elementPath = URLUtil.getWebFileUrl(PortalConstants.MODEL_DIR + type).getPath();
                baseDir = new File(elementPath + "/" + code + id); 
            }
        }
                
        if(baseDir == null) {
            throw new BusinessException("路径为空");
        }
        
        filter = getFilter(filter);
        List<String> files= sortFile(baseDir, FileHelper.listFilesByType(filter, baseDir));
        
        int i = 0;
        for( String fileName : files ){
            sb.append("<treeNode id=\"").append(i++).append("\" name=\"").append(fileName);
            
            boolean isFolder = FileHelper.isFolder(baseDir, fileName);
            if(isFolder) {
                sb.append("\" isFolder=\"").append("1");
            }
            sb.append("\" icon=\"../framework/images/" + (isFolder ? "folder.gif" : "file.gif") + "\"/>");
        }
        sb.append("</actionSet>");      
        
        String path = file.getPath();
        contextPath = path.substring(path.lastIndexOf("model") + 6);
         
        print(new String[]{"ContextPath", "ResourceTree"}, new Object[]{contextPath, sb});
    }
 
    private String getParamFromMap(Map<String, Object>  paramsMap, String paramName){
        Object[] objs = (Object[]) paramsMap.get(paramName);
        if(objs != null && objs.length > 0){
            return (String) objs[0];
        }
        return null;
    }
    
    /* 将子文件夹、文件进行归类，文件夹在前 */
    private List<String> sortFile(File baseDir, List<String> files){
        List<String> dirs = new ArrayList<String>();
        for(Iterator<String> it = files.iterator(); it.hasNext();){
            String fileName = (String) it.next();
            if(FileHelper.isFolder(baseDir, fileName)){
                dirs.add(fileName);
                it.remove();
            }
        }
        dirs.addAll(files);
        return dirs;
    }
    
    /**
     * 上传文件
     */
    public void upload(String contextPath, File file) {
        File baseDir = null;
        if(contextPath != null){
            contextPath = getContextPath(contextPath);
            baseDir = new File(contextPath);
        }
        
        if(baseDir != null && file != null) {
            FileHelper.copyFile(baseDir, file);
        }
        
        print("script", "window.parent.loadFileTree();");
    }
    
    /**
     * 下载
     */
    public void download(String contextPath, String fileNames) {
        if(fileNames == null) return;
        
        // 建立临时文件夹存放要下载的所有文件
        contextPath = getContextPath(contextPath);
        File tempDir = new File(contextPath + "temp"); 
        if ( !tempDir.exists() ) {
            tempDir.mkdir();
        }
        
        String[] fNames = fileNames.split(",");
        for ( String fileName : fNames ) {
            File file = new File(contextPath + fileName);
            if (file.exists()) {
                FileHelper.copyFile(tempDir, file, true, false);  // 拷贝文件至临时文件夹
            }
        }
        
        String zipFilePath = FileHelper.exportZip(contextPath, tempDir); // 将临时文件夹里的文件打包成zip文件
        FileHelper.deleteFile(tempDir); // 删除临时文件夹
        
        ElementHelper.downloadFileByHttp(zipFilePath, "download.zip"); // 下载zip包
    }
    
    /**
     * 删除文件（文件夹）
     */
    public void deleteFile(String contextPath, String fileNames, String folderNames) {
        List<String> pathList = new ArrayList<String>();
        if(fileNames != null) {
            pathList.addAll(Arrays.asList(fileNames.split(",")));
        }
        if(folderNames != null) {
            pathList.addAll(Arrays.asList(folderNames.split(",")));
        }
        
        contextPath = getContextPath(contextPath);
        for(String fileOrDir : pathList){
            if( EasyUtils.isNullOrEmpty(fileOrDir)) continue;
            try {
                FileHelper.deleteFile(new File(contextPath + fileOrDir));
            } catch (Exception e) {
                throw new BusinessException(fileOrDir + "文件删除失败，可能文件正在使用中！", e);
            }
        }
 
        print("script", "window.parent.loadFileTree();");
    }
    
    /**
     * 重命名文件（文件夹）
     */
    public void renameFile(String contextPath, String fileName, String newFileName) {
        contextPath = getContextPath(contextPath);
        File newFile = new File(contextPath + newFileName);
        if(newFile.exists()) {
            throw new BusinessException("同名文件(夹)已经存在，重命名失败！");
        }
        
        File file = new File(contextPath + fileName);
        if( !file.renameTo(newFile) ) {
        	throw new BusinessException("重命名失败，可能文件正在使用中！");
        }
        print("script", "window.parent.loadFileTree();");
    }
    
    /**
     * 新建文件夹 
     */
    public void addDir(String contextPath, String newFileName){
        FileHelper.createDir(contextPath + newFileName);
        print("script", "window.parent.loadFileTree();");
    }
 
    private String getContextPath(String contextPath) {
        String modelDir = URLUtil.getWebFileUrl(PortalConstants.MODEL_DIR).getPath();
        return modelDir + "/" + contextPath + "/";
    }
 
    private String getFilter(String filter) {
        if(filter == null || filter.length() == 0 || "*.*".equals(filter)){
            return "";
        }
        if(filter.startsWith("*")){
            return filter.substring(1);
        }
        return filter;
    }
}

