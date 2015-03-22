package com.jinhe.dm.record;

import com.jinhe.tss.framework.persistence.ITreeSupportDao;
 
public interface RecordDao extends ITreeSupportDao<Record> {

    Record deleteRecord(Record Record);
 
}
