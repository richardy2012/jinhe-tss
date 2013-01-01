package com.jinhe.tss.framework.mock.dao.impl;

import com.jinhe.tss.framework.mock.dao._IUserDAO;
import com.jinhe.tss.framework.mock.model._User;
import com.jinhe.tss.framework.persistence.BaseDao;

public class _UserDAO extends BaseDao<_User> implements _IUserDAO {

    public _UserDAO() {
        super(_User.class);
    }

}
