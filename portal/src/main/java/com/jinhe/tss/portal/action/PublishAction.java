package com.jinhe.tss.portal.action;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;

import com.jinhe.tss.framework.component.param.ParamConfig;
import com.jinhe.tss.framework.component.progress.FeedbackProgressable;
import com.jinhe.tss.framework.component.progress.Progress;
import com.jinhe.tss.framework.component.progress.ProgressManager;
import com.jinhe.tss.framework.component.progress.ProgressPool;
import com.jinhe.tss.framework.component.progress.Progressable;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.web.mvc.ProgressActionSupport;
import com.jinhe.tss.portal.engine.releasehtml.MagicRobot;
import com.jinhe.tss.portal.engine.releasehtml._FtpClient;
import com.jinhe.tss.util.XMLDocUtil;

/** 
 * 门户静态发布
 */
public class PublishAction extends ProgressActionSupport {
 
    /**
     * 静态发布匿名访问的门户
     * 
     * @param id 需要发布的门户portalId
     */
    public void staticIssuePortal(Long id) {
        // 发布整个站点
        MagicRobot robot = new MagicRobot(id);

        // 此处总数是个估算值，如果是第一次发布，按5000计
        // 以后则按上次发布个数 + 100来计（即假设每次发布新增页面不超过100个）。
        int total = robot.getExsitFilesNum() <= 30 ? 5000 : robot.getExsitFilesNum() + 100;
        ProgressManager manager = new ProgressManager((Progressable) robot, total, null);
        String code = manager.execute();
        ProgressPool.putFPObject(code, robot);
        
        printScheduleMessage(code);
    }
    
    /**
     * 静态发布匿名访问的门户的某个页面以及该页面上的链接
     * 
     * @param pageUrl 需要单个发布的页面地址
     */
    public void staticIssuePortalPage(String  pageUrl) {
        //发布整个站点
        MagicRobot robot = new MagicRobot(pageUrl);

        // 此处总数是个估算值，按100计
        ProgressManager manager = new ProgressManager((Progressable) robot, 100, null);
        String code = manager.execute();
        ProgressPool.putFPObject(code, robot);
        
        printScheduleMessage(code);
    }
    
    protected void printScheduleMessage(String code){
        Progress progress = (Progress)ProgressPool.getSchedule(code);
        if(!progress.isNormal()){
            ProgressPool.removeSchedule(code);
            Throwable t = progress.getException();
            String errorMsg = "cause:" + t.getCause()+ t.getMessage() + ".\n位置:"  + t.getLocalizedMessage();
            throw new BusinessException(errorMsg);
        } 
        if(progress.isConceal()) {
            throw new BusinessException("取消进度成功");
        }
        Object[] info = progress.getProgressInfo();
        StringBuffer progressInfo = new StringBuffer("<actionSet>");
        progressInfo.append("<percent>"+ info[0] + "</percent>");
        progressInfo.append("<delay>"  + info[1] + "</delay>");
        progressInfo.append("<estimateTime>" + info[2] + "</estimateTime>");
        
        if(progress.isCompleted()){
            FeedbackProgressable fpa = (FeedbackProgressable)ProgressPool.getFPObject(code);
            String feedback = fpa.getFeedback();
            // 以是否产生了反馈信息为进度结束标志，这点比较特殊，因为total个数是估算的，有可能小于实际数量
            if(feedback != null){ 
                ProgressPool.removeSchedule(code); //执行结束则将将进度对象从池中移除
                ProgressPool.removeFPObject(code);
                // 结束进度条 并且 返回发布反馈信息
                progressInfo.append("<feedback><![CDATA[" + feedback + "]]></feedback>");
            } else {
                progressInfo = new StringBuffer("<actionSet>");
                progressInfo.append("<percent>99</percent>");
                progressInfo.append("<delay>2</delay>");
                progressInfo.append("<estimateTime>10</estimateTime>");
            }
        }
        progressInfo.append("<code>" + code + "</code>");
        print("ProgressInfo", progressInfo.append("</actionSet>"));
    }
    
    /**
     * 将发布出来站点页面拷贝到远程机器上
     * 
     * @param override 是否覆盖
     */
    public void ftpUpload2RemoteServer(boolean override) {
        /* 
         * FTP服务器配置相关：
         * 1、首先从系统参数模块读取配置信息，code：ftpConfig，参数化类型为普通参数，解析之；
         * 2、如果系统参数中没有配置，则读取配置文件，路径为：WEB-INF/classes/META-INF/ftpConfig.xml 
         */
        Document doc;
        String ftpConfigParam = ParamConfig.getAttribute("ftpConfig");
        if(ftpConfigParam != null){
            doc = XMLDocUtil.dataXml2Doc(ftpConfigParam);
        } else {
            doc = XMLDocUtil.createDoc("META-INF/ftpConfig.xml");
        }
        
        for (Iterator<?> it = doc.selectNodes("//ftpServer").iterator(); it.hasNext();) {
            final Element element = (Element) it.next();
            
            // 不使用多线程，暂时不带进度
            _FtpClient _ftp = new _FtpClient(override);
            _ftp.ftpUpload(element);
        }
        printSuccessMessage("远程发布成功");
    }
}