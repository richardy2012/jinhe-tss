package com.jinhe.tss.framework.component.param;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jinhe.tss.framework.persistence.TreeSupportDao;

@Repository("ParamDao")
public class ParamDaoImpl extends TreeSupportDao<Param> implements ParamDao {

	public ParamDaoImpl() {
        super(Param.class);
    }
	
	public List<?> getAllParam(){
		return getEntities("from Param p where p.hidden <> 1 order by p.decode");
	}
	
	public Param getParamByCode(String code){
		String hql = "from Param p where p.type = ? and p.code = ? and p.hidden <> 1 and p.disabled <> 1 order by p.decode";
        List<?> list = getEntities(hql, ParamConstants.NORMAL_PARAM_TYPE, code);
        return list.size() > 0 ? (Param) list.get(0) : null;
	}
	
	public List<?> getCanAddGroups(){
		return getEntities("from Param p where p.type = ? and p.hidden <> 1 order by p.decode", ParamConstants.GROUP_PARAM_TYPE);
	}
}