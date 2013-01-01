package com.jinhe.tss.framework.component.param;

import java.util.List;

import com.jinhe.tss.framework.persistence.TreeSupportDao;

public class ParamDaoImpl extends TreeSupportDao<Param> implements ParamDao {

	public ParamDaoImpl() {
        super(Param.class);
    }

    public Param getParamById(Long id){
		return getEntity(id);
	}
	
	public List<?> getAllParam(){
		return getEntities("from Param p where p.hidden <> 1 order by p.decode");
	}
	
	public Param getParamByCode(String code){
		String hql = "from Param p where p.type = ? and p.code = ? and p.hidden <> 1 and p.disabled <> 1 order by p.decode";
        List<?> list = getEntities(hql, ParamConstants.NORMAL_PARAM_TYPE, code);
        return list.size() > 0 ? (Param) list.get(0) : null;
	}
	
	public List<?> getCanAddParamsAndGroups(Integer mode){
		String hql = "select distinct p from Param p, Param p1 "
			+ " where p1.decode like p.decode||'%' and p1.type = ? and p1.modality = ? and p.hidden <> 1 order by p.decode";
        return getEntities(hql, ParamConstants.NORMAL_PARAM_TYPE, mode);
	}
	
	public List<?> getCanAddParams(Integer mode){
		return getEntities("select p.id from Param p where p.type = ? and p.modality = ?", ParamConstants.NORMAL_PARAM_TYPE, mode);
	}
	
	public List<?> getCanAddGroups(){
		return getParamsByType(ParamConstants.GROUP_PARAM_TYPE);
	}
    
    private List<?> getParamsByType(Integer type){
        return getEntities("from Param p where p.type = ? and p.hidden <> 1 order by p.decode", type);
    }
}