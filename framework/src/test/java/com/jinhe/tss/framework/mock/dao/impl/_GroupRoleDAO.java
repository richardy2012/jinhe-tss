package com.jinhe.tss.framework.mock.dao.impl;

import com.jinhe.tss.framework.mock.dao._IGroupRoleDAO;
import com.jinhe.tss.framework.mock.model._GroupRole;
import com.jinhe.tss.framework.persistence.BaseDao;

public class _GroupRoleDAO extends BaseDao<_GroupRole> implements _IGroupRoleDAO {

    public _GroupRoleDAO() {
        super(_GroupRole.class);
    }

}
