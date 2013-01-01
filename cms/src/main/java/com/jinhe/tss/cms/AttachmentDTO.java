package com.jinhe.tss.cms;

import java.io.Serializable;

/** 
 * <p> 文件附件AttachmentDTO对象, 用于附件下载</p>
 */
public class AttachmentDTO implements Serializable {

    private static final long serialVersionUID = -263389438942564792L;
    
    private Integer type;						//附件类型	1：图片 2：office文档
    private String name;						//附件名称
    private String fileName;					//选择上传的文件名		text
    private String fileExt;						//文件后缀	.gif
    private String localPath;
    private String[] basePath;                  //站点存放附件的根目录
    
    public AttachmentDTO(Integer type, String name, String fileName, String fileExt, String localPath, String[] basePath){
        this.type = type;
        this.name = name;
        this.fileName = fileName;
        this.fileExt = fileExt;
        this.localPath = localPath;
        this.basePath = basePath;
    }
 
    public boolean isImage(){
        return new Integer(1).equals(type);
    }

    public boolean isOfficeDoc(){
        return new Integer(2).equals(type);
    }
 
    public String[] getBasePath() {
        return basePath;
    }
 
    public String getFileExt() {
        return fileExt;
    }
 
    public String getFileName() {
        return fileName;
    }
 
    public String getLocalPath() {
        return localPath;
    }
 
    public String getName() {
        return name;
    }
 
    public Integer getType() {
        return type;
    }
}