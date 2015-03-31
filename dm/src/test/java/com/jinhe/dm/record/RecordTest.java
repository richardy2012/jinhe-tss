package com.jinhe.dm.record;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.jinhe.dm.DMConstants;
import com.jinhe.dm.TxTestSupport4DM;
import com.jinhe.tss.framework.component.log.LogQueryCondition;
import com.jinhe.tss.framework.component.log.LogService;
import com.jinhe.tss.framework.persistence.pagequery.PageInfo;
import com.jinhe.tss.framework.sso.context.Context;

public class RecordTest extends TxTestSupport4DM {
    
    @Autowired private RecordAction action;
    @Autowired private LogService logService;
    
    @Test
    public void testRecordCRUD() {
        
        HttpServletResponse response = Context.getResponse();
        MockHttpServletRequest  request = new MockHttpServletRequest();
        
        request.addParameter("parentId", Record.DEFAULT_PARENT_ID.toString());
        action.getRecord(request, response, Record.TYPE0);
        
        Record group1 = new Record();
        group1.setType(Record.TYPE0);
        group1.setParentId(Record.DEFAULT_PARENT_ID);
        group1.setName("record-group-1");
        action.saveRecord(response, group1);
        
        Record group2 = new Record();
        group2.setType(Record.TYPE0);
        group2.setParentId(Record.DEFAULT_PARENT_ID);
        group2.setName("record-group-2");
        action.saveRecord(response, group2);
        
        Record record1 = new Record();
        record1.setType(Record.TYPE1);
        record1.setParentId(group1.getId());
        record1.setName("record-1");
        record1.setDatasource(DMConstants.DEFAULT_CONN_POOL);
        record1.setTable("");
        record1.setDefine("id:1");
        record1.setCustomizePage("../xx.html");
        record1.setCustomizeJS(" function() f1() { } ");
        record1.setCustomizeSQL("");
        record1.setRemark("test record");
        action.saveRecord(response, record1);
        
        action.getAllRecord(response);
        action.getAllRecordGroups(response);
        
        request.addParameter("recordId", record1.getId().toString());
        action.getRecord(request, response, Record.TYPE1);
        
        action.sort(response, group1.getId(), group2.getId(), 1);
        action.move(response, record1.getId(), group2.getId());
        
        action.getAllRecord(response);
        
        action.delete(response, record1.getId());
        action.getAllRecord(response);
        
        try {
            Thread.sleep(1000); // 等待日志异步输出完毕
        } catch (InterruptedException e) {
        }
        
        LogQueryCondition condition = new LogQueryCondition();
        condition.setOperateTimeFrom(new Date(System.currentTimeMillis() - 1000*3600*3));
        PageInfo logsInfo = logService.getLogsByCondition(condition);
        List<?> logs = logsInfo.getItems();
        for(Object temp : logs) {
            log.debug(temp);
        }
    }
}
