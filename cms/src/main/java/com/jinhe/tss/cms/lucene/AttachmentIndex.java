package com.jinhe.tss.cms.lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jinhe.tss.util.FileHelper;

/**
 * 对文章附件内容进行索引。
 * 
 * 用于读取文章的附件内容进行全文检索。
 * 附件可能为压缩文件、txt、pdf、word、excel、ppt等多种类型。
 */
public class AttachmentIndex {
    
    private static Logger log = Logger.getLogger(AttachmentIndex.class);

    private AttachmentIndex(){
    }

    private static AttachmentIndex manager;
    
    public static AttachmentIndex getInstance(){
        if(manager == null) {
            manager = new AttachmentIndex();
        }
        return manager;
    }
    
	/**
	 * 附件处理得到到附件文本信息(zip文件，解压后取文件列表文本信息)
	 * @param file
	 * @return
	 * @throws Exception 
	 */
	public String disposeAttachment(File file) {
        if ( !file.exists() || !file.isFile() ) 
            return null;
        
		StringBuffer content = new StringBuffer();
		
		// 检测attachment类型如果为zip文件先解压文件，如果普通文件直接解析。
		if ( "zip".equals(FileHelper.getFileSuffix(file.getName())) ) {
			
            // 解压压缩包，得到解压后的文件路径
            String path = FileHelper.upZip(file);

			// 判断解压后的文件列表（如果是 文件，再判断是不是压缩文件。
            // 如果是压缩文件就递归。如果是文件夹得到文夹下的文件列表。如果是普通文件直接得到文件信息）
			List<String> fileList = getFileList(new File(path));
			for ( String fileName : fileList ) {
				File tempFile = new File(fileName);
				if ( tempFile.exists() && tempFile.isDirectory() ) {
					content.append(getFileContent(tempFile));
				} 
				else if (tempFile.exists() && tempFile.isFile()) {
					if ("zip".equals(FileHelper.getFileSuffix(tempFile.getName()))) {
						content.append(disposeAttachment(tempFile)); // 解压缩递归调用
					} 
					else {
						content.append(getContentFromFile(tempFile));
					}
				}
			}
		}
		else {
			content.append(getContentFromFile(file));
		}
		return content.toString();
	}

	/**
	 * 得到文件内容（递归调用）
	 */
	private String getFileContent(File path) {
		if ( path.exists() && path.isFile() ) 
			return getContentFromFile(path);
        
        StringBuffer content = new StringBuffer();
        if ( path.exists() && path.isDirectory() ) {
			List<String> fileList = getFileList(path);
			for ( String fileName : fileList ) {
				content.append(getFileContent(new File(fileName)));
			}
		}
		return content.toString();
	}

	/**
	 * 得到文件列表
	 */
	private List<String> getFileList(File file) {
		List<String> returnList = new ArrayList<String>();
		
		List<String> fileList = FileHelper.listFiles(file);
		for ( String fileName : fileList ) {
			File tempFile = new File(file.getPath() + "/" + fileName.trim());
			if ( tempFile.isFile() ) {
				returnList.add(tempFile.getPath());
			} 
			else if (tempFile.isDirectory()) {
                returnList.addAll(getFileList(tempFile));
			}
		}
		return returnList;
	}

	/**
	 * 得到提取附件的文本内容
	 */
	private String getContentFromFile(File file) {
        if (!file.exists() || !file.isFile()) {
            return "";
        }
        
		String suffix = FileHelper.getFileSuffix(file.getName());
		if ( "doc".equals(suffix) || "docx".equals(suffix) ) { // word文档
//            return getContentFromWord(file);
		} 
		else if ( "ppt".equals(suffix) || "pptx".equals(suffix) ) { // ppt文档
//            return getContentFromPPT(file);
		} 
		else if ( "xls".equals(suffix) || "xlsx".equals(suffix) ) { // excle文档
//            return getContentFromExcel(file);
		} 
		else if ( "pdf".equals(suffix)) { // pdf文档
//            return getContentFromPDF(file);
		}
		else if ( "txt".equals(suffix)) { 
			return getContentFromText(file); // 普通文档，txt等
		}
        
		return "";
	}
	
    private String getContentFromText(File textFile) {
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            br = new BufferedReader(new FileReader(textFile));
            String temp = "";
            while (null != temp) {
                sb.append(br.readLine()).append("\n");
                temp = br.readLine();
            }
        } catch (Exception e) {
            log.error("发布索引时提取文档:" + textFile.getPath() + " 内容失败！", e);
            return "";
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                log.error("发布索引时关闭文件:" + textFile.getPath() + " 流失败！", e);
            }
        }
        return sb.toString();
    }
}