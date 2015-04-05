package com.jinhe.dm.report;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.jinhe.dm.TxTestSupport4DM;
import com.jinhe.dm.data.sqlquery.SqlConfig;
import com.jinhe.tss.framework.sso.context.Context;

public class SqlDebugTest extends TxTestSupport4DM {
    
    @Autowired private ReportAction action;
    @Autowired private _Reporter display;
    
    @Test
    public void debugSQL() {     
        String sql = SqlConfig.getScript("test1", 1);
        
        HttpServletResponse response = Context.getResponse();
        MockHttpServletRequest  request = new MockHttpServletRequest();
        
        Report report1 = new Report();
        report1.setType(Report.TYPE1);
        report1.setParentId(Report.DEFAULT_PARENT_ID);
        report1.setName("report-1");
        report1.setScript(sql);
        action.saveReport(response, report1);
 
        Long reportId = report1.getId();
        display.showAsGrid(request, response, reportId, 1, 100);
    }


}
