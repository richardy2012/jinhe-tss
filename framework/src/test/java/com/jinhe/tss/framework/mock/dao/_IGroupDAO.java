package com.jinhe.tss.framework.mock.dao;

import com.jinhe.tss.framework.mock.model._Group;
import com.jinhe.tss.framework.persistence.ITreeSupportDao;

public interface _IGroupDAO extends ITreeSupportDao<_Group> {
 
    void deleteGroup(_Group group);
    
}