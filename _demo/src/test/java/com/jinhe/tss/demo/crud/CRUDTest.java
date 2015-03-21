package com.jinhe.tss.demo.crud;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.demo.TxTestSupport;
import com.jinhe.tss.demo.crud.DemoAction;
import com.jinhe.tss.demo.crud.DemoEntity;

public class CRUDTest extends TxTestSupport {
	
	@Autowired DemoAction action;
	
	@Test
	public void test() {
		List<DemoEntity> list = action.getAllEntities();
		Assert.assertEquals(0, list.size());
		
		DemoEntity entity = new DemoEntity();
		entity.setCode("test 1");
		entity.setName("test 1");
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
