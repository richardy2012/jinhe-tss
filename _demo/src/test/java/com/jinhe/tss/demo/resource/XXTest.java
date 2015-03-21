package com.jinhe.tss.demo.resource;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.demo.TxTestSupport;

public class XXTest extends TxTestSupport {
	
	@Autowired XXAction  action;
	@Autowired XXService service;
	
	@Test
	public void test() {
		List<XX> list = service.getAll();
		Assert.assertEquals(0, list.size());
		
		action.getXX(response, null);
		
		XX entity = new XX();
		entity.setName("test 1");
		entity.setParentId(XX.DEFAULT_PARENT_ID);
		action.save(response, entity);
		
		Long id = entity.getId();
		Assert.assertNotNull(id);
		entity = service.getXX(id);
		Assert.assertNotNull(entity);
		Assert.assertEquals("test 1", entity.getName());
		
		entity.setName("test 1 update");
		action.save(response, entity);
		entity = service.getXX(id);
		Assert.assertEquals("test 1 update", entity.getName());
		
		action.getXXTree(response);
		action.getXX(response, id.toString());
		action.getXXList(response, 1);
		
		list = service.getAll();
		Assert.assertEquals(1, list.size());
		
		action.delete(response, id);
		
		list = service.getAll();
		Assert.assertEquals(0, list.size());
	}
}
