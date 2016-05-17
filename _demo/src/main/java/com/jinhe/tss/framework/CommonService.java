package com.jinhe.tss.framework;

import java.util.List;

import com.jinhe.tss.framework.persistence.IEntity;

public interface CommonService {

	void create(IEntity entity);

	void update(IEntity entity);

	void delete(Class<?> entityClass, Long id);

	List<?> getList(String hql, Object...params);
	
	List<?> getList(String hql, String[] args, Object[] params);
	
	IEntity getEntity(Class<?> entityClass, Long id);

}
