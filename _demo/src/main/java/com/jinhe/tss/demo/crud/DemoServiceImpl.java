package com.jinhe.tss.demo.crud;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinhe.tss.framework.persistence.pagequery.PageInfo;
import com.jinhe.tss.framework.persistence.pagequery.PaginationQueryByHQL;
 
@Service("DemoService")
public class DemoServiceImpl implements DemoService {
    
    @Autowired private DemoDao dao;

	public DemoEntity getEntityById(Long id) {
		return dao.getEntityById(id);
	}

	public List<DemoEntity> getAllEntities() {
		return dao.getAllEntities();
	}

	public DemoEntity create(DemoEntity entity) {
		return dao.create(entity);
	}
	
	public DemoEntity update(DemoEntity entity) {
		return (DemoEntity) dao.update(entity);
	}
	
	public DemoEntity delete(Long id) {
		return dao.deleteById(id);
	}
 
    public PageInfo search(DemoSO so) {
        String hql = " from DemoEntity o " 
        		+ " where 1=1 " + so.toConditionString() 
        		+ " order by o.id desc ";
 
        PaginationQueryByHQL pageQuery = new PaginationQueryByHQL(dao.em(), hql, so);
        return pageQuery.getResultList();
    }
}

