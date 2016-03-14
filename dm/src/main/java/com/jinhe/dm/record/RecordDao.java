package com.jinhe.dm.record;

import java.util.List;

import com.jinhe.tss.framework.persistence.ITreeSupportDao;
import com.jinhe.tss.um.permission.filter.PermissionFilter4Branch;
import com.jinhe.tss.um.permission.filter.PermissionTag;
 
public interface RecordDao extends ITreeSupportDao<Record> {
    
    @PermissionTag(
    		resourceType = Record.RESOURCE_TYPE,
            filter = PermissionFilter4Branch.class)
    List<Record> getChildrenById(Long id, String operationId);

    Record deleteRecord(Record Record);
    
}
