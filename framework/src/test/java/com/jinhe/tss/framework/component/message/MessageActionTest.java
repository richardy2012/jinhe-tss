package com.jinhe.tss.framework.component.message;

import org.junit.Test;

import com.jinhe.tss.framework.component.param.ParamConfig;

public class MessageActionTest {
	
	MessageAction messageAction = new MessageAction();
	
	@Test
	public void test() {
		messageAction.sendEmail("test", "test", ParamConfig.getAttribute(MailUtil.SEND_TO));
	}

}
