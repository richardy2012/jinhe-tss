package com.jinhe.dm.record;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jinhe.dm.record.ddl._Database;
import com.jinhe.tss.util.EasyUtils;

@Service("RecordService")
@SuppressWarnings("unchecked")
public class RecordServiceImpl implements RecordService {
	
	Logger log = Logger.getLogger(this.getClass());
    
    @Autowired RecordDao recordDao;

	public Record getRecord(Long id) {
		Record record = recordDao.getEntity(id);
        recordDao.evict(record);
        return record;
	}

	private List<Record> getAllRecords() {
		return (List<Record>) recordDao.getEntities("from Record o order by o.decode");
	}
	
	public List<Record> getVisiables() {
		return getAllRecords();
	}
	
	public List<Record> getRecordables() {
		return getAllRecords();
	}

	public List<Record> getAllRecordGroups() {
		return (List<Record>) recordDao.getEntities("from Record o where o.type = ? order by o.decode", Record.TYPE0);
	}

	public Record saveRecord(Record record) {
		if( EasyUtils.isNullOrEmpty(record.getTable()) ) {
			record.setTable("dm_rc_" + Math.abs(record.getName().hashCode()));
		}
		
		if ( record.getId() == null ) {
            record.setSeqNo(recordDao.getNextSeqNo(record.getParentId()));
            recordDao.create(record);
            
            if(Record.TYPE1 == record.getType()) {
            	_Database _db = _Database.getDB(record);
            	_db.createTable();
            }
        }
        else {
        	Record _old = recordDao.getEntity(record.getId());
        	recordDao.evict(_old);
        	
        	String oldDatasource = _old.getDatasource();
        	_old.setDatasource(record.getDatasource());
			_Database _db = _Database.getDB(_old); // 数据库类型从MySQL变成了Oracle，这里获取到的将是_Oracle
			_db.datasource = oldDatasource;
			
        	recordDao.refreshEntity(record);
        	
        	if(Record.TYPE1 == record.getType()) {
        		_db.updateTable(record);
        	}
        }
 
        return record;
	}

	public Record delete(Long id) {
		Record record = getRecord(id);
        return recordDao.deleteRecord(record);
	}

	public void sort(Long startId, Long targetId, int direction) {
		recordDao.sort(startId, targetId, direction);
	}

	public void move(Long id, Long groupId) {
		List<Record> list  = recordDao.getChildrenById(id);
        for (Record temp : list) {
            if (temp.getId().equals(id)) { // 判断是否是移动节点（即被移动枝的根节点）
                temp.setSeqNo(recordDao.getNextSeqNo(groupId));
                temp.setParentId(groupId);
            }
 
            recordDao.moveEntity(temp);
        }
	}

}
