package com.jinhe.tss.framework.component.message;

import org.apache.log4j.Logger;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.jinhe.tss.framework.component.param.ParamConfig;

public class MailUtil {
	
	protected static Logger log = Logger.getLogger(MailUtil.class);
	
	private static JavaMailSenderImpl mailSender;
	
	// 邮箱信息配置到参数管理里
	public static String MAIL_SERVER = "email.server"; 
	public static String SEND_FROM = "email.from";
	public static String SEND_TO = "email.to";
	
	public static MailSender getMailSender() {
		if(mailSender != null) {
			return mailSender;
		}
		
		mailSender = new JavaMailSenderImpl();
		mailSender.setHost(ParamConfig.getAttribute(MAIL_SERVER));
		mailSender.setPort(25);
		
		return mailSender;
	}
	
	public static String getEmailFrom() {
		return ParamConfig.getAttribute(SEND_FROM);
	}
	
	public static String[] getEmailTo() {
		return ParamConfig.getAttribute(SEND_TO).split(",");
	}
	
	public static void send(String subject, String text) {
		send(subject, text, getEmailTo());
	}
	
	public static void send(String subject, String text, String receiver[]) {
		SimpleMailMessage mail = new SimpleMailMessage();

		try {
			mail.setTo(receiver);
			mail.setFrom(getEmailFrom()); // 发送者,这里还可以另起Email别名，不用和xml里的username一致
			mail.setSubject(subject);    // 主题
			mail.setText(text);         // 邮件内容
			getMailSender().send(mail);
		} 
		catch (Exception e) {
			log.error("发送文本邮件时出错了：", e);
		}
	}
}
