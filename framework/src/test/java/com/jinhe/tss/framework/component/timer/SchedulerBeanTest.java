package com.jinhe.tss.framework.component.timer;

import org.junit.Assert;
import org.junit.Test;

import com.jinhe.tss.framework.component.param.Param;
import com.jinhe.tss.framework.component.param.ParamConstants;
import com.jinhe.tss.framework.component.param.TxTestSupportParam;
import com.jinhe.tss.util.EasyUtils;

public class SchedulerBeanTest extends TxTestSupportParam {
	
	@Test
	public void testSchedulerBean() {
        Param paramGroup = addParamGroup(ParamConstants.DEFAULT_PARENT_ID, "我的分组");
        String comboParamCode = SchedulerBean.TIMER_PARAM_CODE;
		Param comboParam = addComboParam(paramGroup.getId(), comboParamCode, "定时Job配置");
		
		String jobConfig = "com.jinhe.tss.framework.component.timer.DemoJob | 0 11 * * * ? | " +
				"1:报表一:pjjin@800best.com,pjjin@800best.com:param1=0,param2=0\n" + 
	            "1:报表二:pjjin@800best.com,pjjin@800best.com:param1=0"; 
        
        addParamItem(comboParam.getId(), jobConfig, "Job1", ParamConstants.COMBO_PARAM_MODE);
        
		SchedulerBean scheduler = new SchedulerBean();
		Assert.assertNotNull(scheduler);
		
		try {
			Thread.sleep(1000*60*10);
		} catch (InterruptedException e) {
		}
	}
	
	@Test
	public void testParseConfig() {
		String temp = "com.jinhe.tss.framework.component.timer.DemoJob | 0 02 * * * ? | " +
				"1:报表一:pjjin@800best.com,pjjin@800best.com:param1=0,param2=0\n" + 
	            "1:报表二:pjjin@800best.com,pjjin@800best.com:param1=0"; 
    	
    	String[] array = EasyUtils.split(temp, "|");
		Assert.assertTrue( array.length == 3 );
		Assert.assertEquals( array[0].trim(), "com.jinhe.tss.framework.component.timer.DemoJob");
		Assert.assertEquals( array[1].trim(), "0 02 * * * ?");
    	
    	array = EasyUtils.split(array[2].trim(), "\n");
    	Assert.assertTrue( array.length == 2 );
		
		Assert.assertTrue( EasyUtils.split(array[1].trim(), ":").length == 4);
	}

}
