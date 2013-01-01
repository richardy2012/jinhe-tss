package com.jinhe.tss.framework.mock.dao.impl;

import java.util.List;

import com.jinhe.tss.framework.mock.dao._IGroupDAO;
import com.jinhe.tss.framework.mock.model._Group;
import com.jinhe.tss.framework.persistence.TreeSupportDao;

public class _GroupDAO extends TreeSupportDao<_Group> implements _IGroupDAO {

    public _GroupDAO() {
        super(_Group.class);
    }
    
    public void deleteGroup(_Group group) {
        List<_Group> children = getChildrenById(group.getId());
        
        deleteAll(getEntities("from _User"));
        deleteAll(children);
    }

}
