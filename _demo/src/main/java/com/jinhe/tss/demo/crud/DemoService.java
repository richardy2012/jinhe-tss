package com.jinhe.tss.demo.crud;

import java.util.List;

import com.jinhe.tss.framework.persistence.pagequery.PageInfo;
 
public interface DemoService {

	DemoEntity getEntityById(Long id);
	
	List<DemoEntity> getAllEntities();

	DemoEntity create(DemoEntity entity);
	
	DemoEntity update(DemoEntity entity);
	
	DemoEntity delete(Long id);
	
	PageInfo search(DemoSO so);
}

