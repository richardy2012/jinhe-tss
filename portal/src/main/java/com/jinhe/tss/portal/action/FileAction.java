package com.jinhe.tss.portal.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.web.mvc.PTActionSupport;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.helper.ElementHelper;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.URLUtil;

/**
 * 管理门户、页面、布局器、修饰器、布局器等的附件资源。
 */
public class FileAction extends PTActionSupport {

    public static final String[] ELEMENT_TYPE = new String[]{"layout", "decorator", "portlet"};
    
    /**
     * 参数列表 paramsMap['" + param + "']"
     */
    private Map<String, Object>  paramsMap = new HashMap<String, Object>(); //
    
    private File    file;
    private String  fileName;       //文件名
    private String  newFileName;   //文件名
    private String  fileNames;
    private String  folderNames;
    private String  filter = "";  //文件后缀名
    private String  contextPath; //当前操作文件所在目录上下文
    
    /**
     * 列出文件列表
     * @return
     */
    public String listAvailableFiles(){
        String id   = getParamFromMap("id");  
        String code = getParamFromMap("code");
        String type = getParamFromMap("type"); // 判断是何种类型的资源管理
        if(type == null) {
            throw new BusinessException("没有指定文件类型");
        }
        
        StringBuffer sb = new StringBuffer("<actionSet title=\"\" openednodeid=\"r1.1\">"); 
        
        // 如果contextPath不为空，则直接使用contextPath值作为根目录；否则根据type值找根目录
        File baseDir = null;
        String portalModelDir = URLUtil.getWebFileUrl(PortalConstants.PORTAL_MODEL_DIR).getPath();
        File portalRootDir = new File(portalModelDir + "/" + code + "_" + id); // 指定门户的资源根目录
        if( contextPath != null ){
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
            else if(Arrays.asList(ELEMENT_TYPE).contains(type)) { // 门户元素
                String elementPath = URLUtil.getWebFileUrl(PortalConstants.MODEL_DIR + type).getPath();
                baseDir = new File(elementPath + "/" + code + id); 
            }
        }
                
        if(baseDir == null) {
            throw new BusinessException("路径为空");
        }
        
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
        String contextPath = path.substring(path.lastIndexOf("model") + 6);
         
        return print(new String[]{"ContextPath", "ResourceTree"}, new Object[]{contextPath, sb});
    }
 
    private String getParamFromMap(String paramName){
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
     * @return
     */
    public String upload(){
        File baseDir = null;
        if(contextPath != null){
            baseDir = new File(contextPath);
        }
        
        if(baseDir != null && file != null) {
            FileHelper.copyFile(baseDir, file);
        }
        
        return print("script", "window.parent.loadFileTree();");
    }
    
    /**
     * 下载
     * @return
     */
    public String download(){
        if(fileNames == null) return XML;
        
        // 建立临时文件夹存放要下载的所有文件
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
        
        return XML;
    }
    
    /**
     * 删除文件（文件夹）
     * @return
     */
    public String deleteFile(){
        List<String> pathList = new ArrayList<String>();
        if(fileNames != null) {
            pathList.addAll(Arrays.asList(fileNames.split(",")));
        }
        if(folderNames != null) {
            pathList.addAll(Arrays.asList(folderNames.split(",")));
        }
        
        for(String fileOrDir : pathList){
            if( EasyUtils.isNullOrEmpty(fileOrDir)) continue;
            try {
                FileHelper.deleteFile(new File(contextPath + fileOrDir));
            } catch (Exception e) {
                throw new BusinessException(fileOrDir + "文件删除失败，可能文件正在使用中！", e);
            }
        }
 
        return print("script", "window.parent.loadFileTree();");
    }
    
    /**
     * 重命名文件（文件夹）
     * @return
     */
    public String renameFile(){
        File newFile = new File(contextPath + newFileName);
        if(newFile.exists()) {
            throw new BusinessException("同名文件(夹)已经存在，重命名失败！");
        }
        
        File file = new File(contextPath + fileName);
        if( file.renameTo(newFile) ) {
            return print("script", "window.parent.loadFileTree();");
        }
        
        throw new BusinessException("重命名失败，可能文件正在使用中！");
    }
    
    /**
     * 新建文件夹 
     * @return
     */
    public String addDir(){
        FileHelper.createDir(contextPath + newFileName);
        return print("script", "window.parent.loadFileTree();");
    }

    public Map<String, Object> getParamsMap() {
        return paramsMap;
    }
 
    public void setContextPath(String contextPath) {
        if(contextPath != null) {
            String modelDir = URLUtil.getWebFileUrl(PortalConstants.MODEL_DIR).getPath();
			this.contextPath = modelDir + "/" + contextPath + "/";
        }
    }
 
    public void setFile(File file) {
        this.file = file;
    }
 
    public void setFilter(String filter) {
        if(filter == null || filter.length() == 0 || "*.*".equals(filter)){
            this.filter = "";
        }
        else if(filter.startsWith("*")){
            this.filter = filter.substring(1);
        }
        else {
            this.filter = filter;
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }
    public void setFileNames(String fileNames) {
        this.fileNames = fileNames;
    }
    public void setFolderNames(String folderNames) {
        this.folderNames = folderNames;
    }
}

