package com.jinhe.dm.report.timer;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.jinhe.dm.DMConstants;
import com.jinhe.dm.data.sqlquery.SQLExcutor;
import com.jinhe.dm.data.util.DataExport;
import com.jinhe.dm.report.ReportService;
import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.component.message.MailUtil;
import com.jinhe.tss.framework.component.param.Param;
import com.jinhe.tss.framework.component.param.ParamManager;
import com.jinhe.tss.framework.component.timer.AbstractJob;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.um.helper.dto.OperatorDTO;
import com.jinhe.tss.um.service.ILoginService;
import com.jinhe.tss.util.DateUtil;
import com.jinhe.tss.util.EasyUtils;
import com.jinhe.tss.util.MacrocodeCompiler;

/**
 * com.jinhe.dm.report.timer.ReportJob | 0 36 10 * * ? | 268:各省日货量流向:pjjin@800best.com,BL01037:param1=today-1
 * 261:各省生产货量:BL00618,BL01037:param1=today-0
 * 262:报表三:BL00618,BL01037:param1=0,param3=today-1
 * 
 * 收件人支持方式有：email、账号、角色、用户组、参数定义，分别如下
 * pjjin@800best.com,BL01037,-1@tssRole,-2@tssGroup,${JK}
 */
public class ReportJob extends AbstractJob {
	
	ReportService reportService = (ReportService) Global.getBean("ReportService");
	ILoginService loginService  = (ILoginService) Global.getBean("LoginService");
	
	/* 
	 * jobConfig的格式为
	 *  
	 *  1:报表一:x1@x.com
     *  2:报表二:x2@x.com
	 *	3:报表三:x3@x.com,x4@x.com:param1=a,param2=b
	 */
	protected void excuteJob(String jobConfig) {
		
		String[] jobConfigs = EasyUtils.split(jobConfig, "\n");
		
		Map<String, ReceiverReports> map = new HashMap<String, ReportJob.ReceiverReports>();
		
		// 收件人一致的定时报表合并起来发送
		for(int i = 0; i < jobConfigs.length; i++) {
			if(EasyUtils.isNullOrEmpty(jobConfigs[i])) continue;
			
			String reportInfo[] = EasyUtils.split(jobConfigs[i], ":");
			if(reportInfo.length <= 2) continue;
			
			String receiverStr = reportInfo[2].trim();
			ReceiverReports rr = map.get(receiverStr);
			if(rr == null) {
				map.put(receiverStr, rr = new ReceiverReports());
			}
			
			String title = reportInfo[1];
	        rr.reportTitles.add(title);
					
	        Long reportId = EasyUtils.obj2Long(reportInfo[0]);
	    	Map<String, String> paramsMap = new HashMap<String, String>();
	    	if(reportInfo.length > 3) {
	    		String[] params = reportInfo[3].split(",");
	    		for(String param : params) {
	    			String[] keyValue = param.split("=");
	    			paramsMap.put(keyValue[0].trim(), keyValue[1].trim());
	    		}
	    	}
	        SQLExcutor ex = reportService.queryReport(reportId, paramsMap, 0, 0, System.currentTimeMillis());  
	        rr.reportResults.add(ex);
		}
		
		for(String receiverStr : map.keySet()) {
			String receiver[] = getEmails( receiverStr );
			if(receiver == null || receiver.length == 0) {
				continue;
			}
			
			ReceiverReports rr = map.get(receiverStr);
			send(receiver, rr);
		}
	}
	
	private void send(String[] receiver, ReceiverReports rr) {
		String title = EasyUtils.list2Str(rr.reportTitles);
		
		JavaMailSenderImpl sender = (JavaMailSenderImpl) MailUtil.getMailSender();
		MimeMessage mailMessage = sender.createMimeMessage();
		
		try {
			// 设置utf-8或GBK编码，否则邮件会有乱码
			MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
			messageHelper.setTo(receiver);   // 接受者
			messageHelper.setFrom(MailUtil.getEmailFrom());  // 发送者
			messageHelper.setSubject("定时报表：" + title); // 主题
			
			// 邮件内容，注意加参数true
			StringBuffer html = new StringBuffer();
			html.append("<html>");
			html.append("<head>");
			html.append("<style type='text/css'> " );
			html.append("	table { border-collapse:collapse; border-spacing:0; }");
			html.append("	td { line-height: 1.42857143; vertical-align: top;  border: 1px solid black; text-align: left;}");
			html.append("	td { margin:0; padding:0; padding: 2px 2px 2px 2px; font-family: 微软雅黑; font-size: 15px;}");
			html.append("	thead td { background-color:#E4E6F5; font-weight: bold; }");
			html.append("</style>");
			html.append("</head>");
			html.append("<body>");
			
			int index = 0;
			for(SQLExcutor ex : rr.reportResults) {
				buildEmailContent(rr.reportTitles.get(index++), ex, messageHelper, html);
			}
			
			html.append("</body>");
			html.append("</html>");
			log.debug(html);
			messageHelper.setText(html.toString(), true);
			sender.send(mailMessage);
		} 
		catch (Exception e) {
			throw new BusinessException("发送报表邮件时出错了：", e);
		}
	}

	private void buildEmailContent(String title, SQLExcutor ex,
			MimeMessageHelper messageHelper, StringBuffer html) throws Exception {
		
		if(ex.result.size() > 100) {
			html.append("<h1>报表【" + title + "】的内容详细请参见附件。</h1>");
		} 
		else {
			html.append("<h1>" + title + "</h1>");
			html.append("<table>");
			if(ex.selectFields != null) {
				html.append("<thead><tr>");
		    	for(String field : ex.selectFields) {
		    		html.append("<td>").append("&nbsp;").append(field).append("&nbsp;").append("</td>");
		    	}
		    	html.append("</tr></thead>");
		    	
		    	html.append("<tbody>");
		    	for( Map<String, Object> row : ex.result) {
					html.append("<tr>");
					for(String field : ex.selectFields) {
			    		html.append("<td>").append(row.get(field)).append("</td>");
			    	}
					html.append("</tr>");
				}
		    	html.append("</tbody>");
		    }
			
			html.append("</table><br>");
		}
		
		// 附件内容
		String fileName = title + "-" + DateUtil.format(new Date()) + ".csv";
		String exportPath = DataExport.exportCSV(fileName, ex.result, ex.selectFields);
		
		fileName = MimeUtility.encodeWord(fileName); // 使用MimeUtility.encodeWord()来解决附件名称的中文问题
		messageHelper.addAttachment(MimeUtility.encodeWord(fileName), new File(exportPath));
	}
	
	
	/**
	 * 收件人对报表的映射，当一组收件人对应多个报表时，将这些报表合并成一个邮件发送
	 */
	class ReceiverReports {
		List<String> reportTitles = new ArrayList<String>();
		List<SQLExcutor> reportResults = new ArrayList<SQLExcutor>();
	}
	
	/**
	 * 支持loginName，email，角色，用户组，辅助组、参数宏
	 */
	private String[] getEmails(String receiverStr) {
		Map<String, Object> fmDataMap = new HashMap<String, Object>();
		List<Param> macroParams = ParamManager.getComboParam(DMConstants.EMAIL_MACRO);
		if(macroParams != null) {
			for(Param p : macroParams) {
				fmDataMap.put(p.getText(), p.getValue());
			}
		}
		
		receiverStr = MacrocodeCompiler.runLoop(receiverStr, fmDataMap, true);
		String[] receiver = receiverStr.split(",");
		
		// 将登陆账号转换成该用户的邮箱
		Set<String> emails = new HashSet<String>();
		for(int j = 0; j < receiver.length; j++) {
			String temp = receiver[j];
			
			// 判断配置的是否已经是email，如不是，作为loginName处理
			if(temp.endsWith("@tssRole")) {
				List<OperatorDTO> list = loginService.getUsersByRoleId(parseID(temp));
				addUsersEmail2List(list, emails);
			} 
			else if(temp.endsWith("@tssGroup")) {
				List<OperatorDTO> list = loginService.getUsersByGroupId(parseID(temp));
				addUsersEmail2List(list, emails);
			} 
			else if(temp.indexOf("@") < 0) {
				addUserEmail2List(temp, emails);
			}
			else if(temp.indexOf("@") > 0 && temp.indexOf(".") > 0) {
				emails.add(temp);
			}
		}
		receiver = new String[emails.size()];
		receiver = emails.toArray(receiver);
		
		return receiver;
	}
	
	private Long parseID(String temp) {
		try {
			return EasyUtils.obj2Long( temp.split("@")[0] );
		} catch(Exception e) {
			return 0L;
		}
	}
	
	private void addUserEmail2List(String loginName, Set<String> emails) {
		try {
			OperatorDTO user = loginService.getOperatorDTOByLoginName(loginName);
			addUserEmail2List(user, emails);
		} 
		catch(Exception e) {
		}
	}
	
	private void addUserEmail2List(OperatorDTO user, Set<String> emails) {
		String email = (String) user.getAttribute("email");
		if( !EasyUtils.isNullOrEmpty(email) ) {
			emails.add( email );
		}
	}
	
	private void addUsersEmail2List(List<OperatorDTO> list, Set<String> emails) {
		for(OperatorDTO user : list) {
			addUserEmail2List(user, emails);
		}
	}
}
