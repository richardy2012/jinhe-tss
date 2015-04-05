package com.jinhe.dm.report;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.jinhe.dm.DMConstants;
import com.jinhe.dm.TxTestSupport4DM;
import com.jinhe.tss.framework.component.param.Param;
import com.jinhe.tss.framework.component.param.ParamConstants;
import com.jinhe.tss.framework.sso.context.Context;

public class ScriptParseTest extends TxTestSupport4DM {
    
    @Autowired private ReportAction action;
    @Autowired private _Reporter display;

    @Test
    public void test1() {      
    	Param paramL = addParamGroup(ParamConstants.DEFAULT_PARENT_ID, DMConstants.SCRIPT_MACRO, "常用脚本段");
        addParamItem(paramL.getId(), "1=1", "testMacro", ParamConstants.COMBO_PARAM_MODE);
    	
        HttpServletResponse response = Context.getResponse();
        MockHttpServletRequest  request = new MockHttpServletRequest();
        
        Report report1 = new Report();
        report1.setType(Report.TYPE1);
        report1.setParentId(Report.DEFAULT_PARENT_ID);
        report1.setName("report-1");
        report1.setScript(" select id, name from dm_report where ${testMacro} and createTime > ? ");
        
        String paramsConfig = "[ {'label':'起始时间', 'type':'date', 'nullable':'false'} ]"	;
        report1.setParam(paramsConfig);
        
        action.saveReport(response, report1);
        
        request.addParameter("param1", "2013-10-01");
        Long reportId = report1.getId();
        display.showAsJson(request, reportId.toString());
    }
    
}