package com.jinhe.tss.um.module;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.MailUtil;
import com.jinhe.tss.framework.component.param.ParamConfig;
import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.action.MessageAction;

public class MessageActionTest extends TxSupportTest4UM {
	
	@Autowired MessageAction messageAction;
	
	@Test
	public void test() {
		messageAction.sendEmail("test", "<html><body><h3>生命变的厚重</h3></body></html>", ParamConfig.getAttribute(MailUtil.SEND_TO));
	}
	
	@Test
	public void test2() {
		messageAction.sendHtmlEmail("test", "<html><body><h3>生命变的厚重</h3></body></html>", ParamConfig.getAttribute(MailUtil.SEND_TO));
	}

}
