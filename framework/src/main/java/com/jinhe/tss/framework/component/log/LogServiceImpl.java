package com.jinhe.tss.framework.component.log;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.persistence.ICommonDao;
import com.jinhe.tss.util.BeanUtil;
 
public class LogServiceImpl implements LogService {
    
    @Autowired private ICommonDao dao;

    public void createLog(Log dto){
        Log log = new Log();
        BeanUtil.copy(log, dto);
        dao.create(log);
    }

    public List<?> getAllApps() {
        return dao.getEntities("select distinct o.appCode from Log o group by o.appCode");
    }

    public Object[] getLogsByCondition(LogQueryCondition condition) {
        String orderBy = " order by o.operateTime asc";
        return dao.getEntities(condition, "Log", orderBy);
        
    }

    public Log getLogById(Long id) {
        return (Log) dao.getEntity(Log.class, id);
    }
}

