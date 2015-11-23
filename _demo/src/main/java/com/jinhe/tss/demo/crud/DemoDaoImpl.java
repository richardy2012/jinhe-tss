package com.jinhe.tss.demo.crud;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jinhe.tss.framework.persistence.BaseDao;

@Repository("DemoDao")
public class DemoDaoImpl extends BaseDao<DemoEntity> implements DemoDao {

	public DemoDaoImpl() {
        super(DemoEntity.class);
    }

	@SuppressWarnings("unchecked")
	public List<DemoEntity> getAllEntities() {
		return (List<DemoEntity>) super.getEntities("from DemoEntity");
	}
 
}