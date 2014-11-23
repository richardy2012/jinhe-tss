package com.jinhe.tss.framework.component.timer;

import org.junit.Assert;
import org.junit.Test;

import com.jinhe.tss.cache.Pool;
import com.jinhe.tss.framework.component.cache.CacheHelper;
import com.jinhe.tss.framework.component.param.Param;
import com.jinhe.tss.framework.component.param.ParamConstants;
import com.jinhe.tss.framework.component.param.TxTestSupportParam;
import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.util.EasyUtils;

public class SchedulerBeanTest extends TxTestSupportParam {
	
	String jobConfig = "com.jinhe.tss.framework.component.timer.DemoJob | 50 * * * * ? | " +
			"1:报表一:pjjin@800best.com,pjjin@800best.com:param1=0,param2=0\n" + 
            "2:报表二:pjjin@800best.com,pjjin@800best.com:param1=0"; 
	
	@Test
	public void testSchedulerBean() {
        Param paramGroup = addParamGroup(ParamConstants.DEFAULT_PARENT_ID, "我的分组");
        String comboParamCode = SchedulerBean.TIMER_PARAM_CODE;
		Param comboParam = addComboParam(paramGroup.getId(), comboParamCode, "定时Job配置");
		Long cpId = comboParam.getId();
		
		Param item1 = addParamItem(cpId, jobConfig + ",param2=1", "Job1", ParamConstants.COMBO_PARAM_MODE);
		Param item2 = addParamItem(cpId, jobConfig + ",param2=2", "Job2", ParamConstants.COMBO_PARAM_MODE);
        
		SchedulerBean scheduler = new SchedulerBean(1000*15);
		Assert.assertNotNull(scheduler);
		
		try { Thread.sleep(1000*70); } catch (InterruptedException e) { }
		
		// 修改、新增、删除定时配置
		item1.setValue(jobConfig + "1");
		paramService.saveParam(item1);
		paramService.delete(item2.getId());
		addParamItem(cpId, jobConfig + ",param2=3", "Job3", ParamConstants.COMBO_PARAM_MODE);
		
		// 清除service method cache
		Pool shortCache = CacheHelper.getShortCache();
		shortCache.removeObject("com.jinhe.tss.framework.component.param.ParamService.getComboParam(TIMER_PARAM_CODE)");
		
		try { Thread.sleep(1000*80); } catch (InterruptedException e) { }
		
		TestUtil.printLogs(logService);
	}
	
	@Test
	public void testParseConfig() {
    	String[] array = EasyUtils.split(jobConfig, "|");
		Assert.assertTrue( array.length == 3 );
		Assert.assertEquals( array[0].trim(), "com.jinhe.tss.framework.component.timer.DemoJob");
		Assert.assertEquals( array[1].trim(), "50 * * * * ?");
    	
    	array = EasyUtils.split(array[2].trim(), "\n");
    	Assert.assertTrue( array.length == 2 );
		
		Assert.assertTrue( EasyUtils.split(array[1].trim(), ":").length == 4);
	}

}
