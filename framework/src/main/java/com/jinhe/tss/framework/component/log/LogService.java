package com.jinhe.tss.framework.component.log;

import java.util.List;
 
public interface LogService {
    
    /**
     * 新建一条日志信息
     * @param log
     */
    void createLog(Log log);

    /**
     * 获取日志中所有的系统
     * @return
     */
    List<?> getAllApps();

    /**
     * 根据查询条件和分页信息获取日志列表
     * @param condition
     * @param page_size
     * @param currentPageNum
     * @return
     */
    Object[] getLogsByCondition(LogQueryCondition condition);

    /**
     * 根据主键值ID获取对象
     * @param id
     * @return
     */
    Log getLogById(Long id);
}

