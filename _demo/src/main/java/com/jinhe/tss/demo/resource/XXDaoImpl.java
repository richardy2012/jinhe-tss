package com.jinhe.tss.demo.resource;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jinhe.tss.framework.persistence.TreeSupportDao;

@Repository("XXDao")
public class XXDaoImpl extends TreeSupportDao<XX> implements XXDao {

	public XXDaoImpl() {
        super(XX.class);
    }
 
	public XX deleteXX(XX XX) {
		Long id = XX.getId();
        List<XX> list = getChildrenById(id);
        for(XX entity : list) {
            delete(entity);
        }
        return XX;
	}

	public List<XX> getChildrenById(Long id, String operationId) {
		return getChildrenById(id);
	}
}