package com.jinhe.tss.portal.engine.releasehtml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.dom4j.Element;

import com.jinhe.tss.framework.component.progress.FeedbackProgressable;
import com.jinhe.tss.framework.component.progress.Progress;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.util.FileHelper;

/** 
 * FTP客户端上传类。
 */
public class _FtpClient implements FeedbackProgressable{
    
    protected static Logger log = Logger.getLogger(_FtpClient.class);
    
    private FTPClient ftp;
    private String remoteRoot;  // 远程FTP根目录
    
    private boolean override;   // 是否覆盖上传
    
    /**
     * 已上传的文件列表
     */
    List<String> uploadedFiles = new ArrayList<String>(); 
    
    /**
     * 上传失败的文件列表
     */
    Map<String, String> uploadFailedFiles = new HashMap<String, String>();

    public _FtpClient(boolean override) {
        this.override = override;
    }

    /**
     * 配置文件格式如下：
     *  <ftpServer url="192.168.0.31" userName="root" password="password">
     *      <filePath localDir="d:/project/PMS/webapp/html" remoteDir="temp/html"/>
     *      <filePath localDir="d:/project/PMS/webapp/core" remoteDir="temp/core"/>
     *      <filePath localDir="d:/project/PMS/webapp/pms/model" remoteDir="temp/model"/>
     *      <filter-pattern>index,articleList</filter-pattern>
     *  </ftpServer>
     * @param ftpConfigNode
     */
    public void ftpUpload(Element ftpConfigNode) {
        List<String> filterPatterns = new ArrayList<String>();
        Element filterPattern = (Element) ftpConfigNode.selectSingleNode("filter-pattern");
        filterPatterns.addAll(Arrays.asList(filterPattern.getTextTrim().split(",")));

        String url  = ftpConfigNode.attributeValue("url");
        String user = ftpConfigNode.attributeValue("userName");
        String pwd  = ftpConfigNode.attributeValue("password");
       
        try {
            // 连接
            connect2FtpServer(url, user, pwd);
            for (Iterator<?> itIn = ftpConfigNode.elementIterator("filePath"); itIn.hasNext();) {
                Element temp = (Element) itIn.next();
                String remoteDir = temp.attributeValue("remoteDir");
                String localDir  = temp.attributeValue("localDir");
                
                uploadFiles(remoteDir, localDir, filterPatterns);
            }
            // 关闭连接
            disconnectFtpServer();
        } catch (IOException e) {
            throw new BusinessException("上传文件时出错: " + e.getMessage());
        } finally {
            log.debug(getFeedback());
        }
    }
    
    /**
     * 连接FTP服务器并登陆。
     * @param url
     * @param user
     * @param pwd
     * @throws SocketException, IOException
     */
    private void connect2FtpServer(String url, String user, String pwd) throws SocketException, IOException {
        this.ftp = new FTPClient();
        ftp.connect(url);
        ftp.login(user, pwd);
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
        } 
        this.remoteRoot = ftp.printWorkingDirectory();
    }

    /**
     * 退出FTP服务器并断开连接。
     * @throws IOException
     */
    private void disconnectFtpServer() throws IOException {
        ftp.logout();
        if (ftp.isConnected()) {
            ftp.disconnect();
        }
    }
    
    /**
     * 将整个文件夹上传。
     * @param files
     *            所有需要上传文件的列表
     * @param remoteDir
     *            FTP上的目标路径
     * @param localDir
     *            本地上传文件的目录
     */
    private void uploadFiles(String remoteDir, String localDir, List<String> filterPatterns) {
        List<File> files = FileHelper.listFilesDeeply(new File(localDir));
        for ( File file : files ) {
            String filePath = file.getPath();
            try {
                makeDirectory(remoteDir, getFileRelativePath(file, localDir));
                
                String fileName = file.getName();
                FileInputStream input = new FileInputStream(file);
                if (override || hasModifiedRecently(file) || checkByPattern(fileName, filterPatterns)) { 
                    ftp.storeFile(fileName, input);
                    
                    // 如果上传成功，则将该文件记录到uploadedFiles里
                    uploadedFiles.add(filePath);
                    log.info( filePath + "上传结束");   
                }
                input.close();
                input = null;
            } catch (Exception e) {
                uploadFailedFiles.put(filePath, "远程目录（" + remoteDir + "）" + e.getMessage());
                // log.error("上传本地文件：" + path + "至远程目录 + " + remoteDir + "过程中出错", e.getCause());
            }
            updateProgressInfo();
        }
    }

    /**
     * 判断要上传的文件名是否含有特殊字符串，从而决定该文件是否需要覆盖上传。
     * 
     * @param fileName
     * @return true: 需覆盖，false：不覆盖。
     */
    private boolean checkByPattern(String fileName, List<String> filterPatterns) {
        if(filterPatterns != null && filterPatterns.size() > 0){
            for ( String filterPattern : filterPatterns ) {
                if (fileName.indexOf(filterPattern) >= 0)
                    return true;
            }
        }
        return false;
    }

    /**
     * 文件是否在近段时间内进行过修改。
     * @param file
     */
    private boolean hasModifiedRecently(File file) {
        long limitTime = 8*60*60*1000; // 3小时
        return System.currentTimeMillis() - file.lastModified() < limitTime;
    }

    /**
     * 在上传文件夹的过程中，如果服务器的目标目录中和上传文件夹的目录有区别 则创建各级目录。
     * 
     * @param remoteDir
     * @param relativePath
     */
    private void makeDirectory(String remoteDir, String relativePath) {
        try {
            ftp.changeWorkingDirectory(this.remoteRoot); // 先切换到根目录下
            
            relativePath = remoteDir + "/" + relativePath;
            String[] dir = relativePath.split("/");// 目录分级
            for (int i = 0; i < dir.length; i++) {
                if (!ftp.changeWorkingDirectory(dir[i])) {// 是否有子目录（dir[i]）
                    ftp.makeDirectory(dir[i]);
                }
                ftp.changeWorkingDirectory(dir[i]);
            }
            //log.info("Directory is " + ftp.printWorkingDirectory());
            //ftp.changeWorkingDirectory(remoteDir + "/" + relativePath);
        } catch (Exception e) {
            log.error("在ftp服务器上创建相应目录时候出错", e);
        }
    }

    /**
     * 获取上传文件相对于上传目录的文件相对路径（即 文件的绝对路径 - 上传目录路径）。
     * 
     * 例如： 需要上传的文件夹 d:/pms/html，需要上传的文件为：d:/pms/html/2008/12/31/***.htm
     * 则返回 2008/12/31/
     * 
     * @param filePath
     * @param localDir
     * @return
     */
    private String getFileRelativePath(File file, String localDir) {
        String relativePath = "";
        File ld = new File(localDir);
        
        File pfile = file;
        while(!(pfile = pfile.getParentFile()).equals(ld) && pfile.getPath().length() > localDir.length()){
            if(relativePath.length() > 0){
                relativePath = pfile.getName() + "/" + relativePath;
            } else {
                relativePath = pfile.getName();
            }
        }
        return relativePath;
    }

    public String getFeedback() {
        // 处理上传成功和不成功的文件列表信息
        StringBuffer feedback = new StringBuffer("远程返回完成后反馈信息：\n");
        feedback.append("上传成功的文件有：\n");
        for(int i = 0; i < uploadedFiles.size(); i++){
            feedback.append(uploadedFiles.get(i)).append("\n");   
        }
        feedback.append("上传失败的文件有：\n");
        for( Entry<String, String> entry : uploadFailedFiles.entrySet() ){
            feedback.append( entry.getKey() + "，原因：" + entry.getValue()).append("\n");   
        }
        return feedback.toString();
    }

    private Progress progress = new Progress(-1);
    
    public void execute(Map<String, Object> params, Progress progress) {
        this.progress = progress;
        
        Element ftpConfigNode = (Element) params.get("ftpConfig");
        if(ftpConfigNode == null){
            throw new BusinessException("ftp服务器配置不能为空");
        }
        ftpUpload(ftpConfigNode);
    }

    private int count = 0;
    
    /**
     * 更新进度信息
     */
    private void updateProgressInfo(){
        if(++count % 20 == 0){
            progress.add(20); //每20个更新一次进度信息
        } 
    }
}