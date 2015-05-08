package com.jinhe.tss.demo.crud;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.demo.TxTestSupport;
import com.jinhe.tss.framework.component.param.Param;
import com.jinhe.tss.framework.component.param.ParamConstants;

public class CRUDTest extends TxTestSupport {
	
	@Autowired DemoAction action;
	
	List<Param> list1;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		if(paramService.getParam("DemoState") == null) {
        	Param cp = addComboParam(ParamConstants.DEFAULT_PARENT_ID, "DemoState", "测试状态");
        	addComboItem( cp.getId(), "1", "停用" );
        	addComboItem( cp.getId(), "0", "启用" );
        }
		list1 = paramService.getComboParam("DemoState");
	}
	
	@Test
	public void test() {
		List<DemoEntity> list = action.getAllEntities();
		Assert.assertEquals(0, list.size());
		
		DemoEntity entity = new DemoEntity();
		entity.setCode("test 1");
		entity.setName("test 1");
		entity.setState(list1.get(0));
		entity = action.save(entity );
		
		Long id = entity.getId();
		Assert.assertNotNull(id);
		entity = action.getEntityById(id);
		Assert.assertNotNull(entity);
		Assert.assertEquals("test 1", entity.getCode());
		
		entity.setName("test 1 update");
		action.save(entity);
		entity = action.getEntityById(id);
		Assert.assertEquals("test 1 update", entity.getName());
		
		list = action.getAllEntities();
		Assert.assertEquals(1, list.size());
		
		DemoSO so = new DemoSO();
		so.setCode("test 1");
		List<?> list2 = action.search(response, so , 1);
		Assert.assertEquals(1, list2.size());
		
		so.setCode("test 22");
		List<?> list3 = action.search(response, so , 1);
		Assert.assertEquals(0, list3.size());
		
		action.delete(id);
		
		list = action.getAllEntities();
		Assert.assertEquals(0, list.size());
	}

}
