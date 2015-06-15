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
		messageAction.sendEmail("test", "test", ParamConfig.getAttribute(MailUtil.SEND_TO));
	}

}
