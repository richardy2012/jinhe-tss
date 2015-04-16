package com.jinhe.dm.report;

import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.jinhe.dm.TxTestSupport4DM;
import com.jinhe.tss.framework.sso.context.Context;

public class DisplayTest extends TxTestSupport4DM {
    
    @Autowired private ReportAction action;
    @Autowired private _Reporter display;
    
    @Test
    public void testJson2CSV() {   
    	HttpServletResponse response = Context.getResponse();
    	MockHttpServletRequest  request = new MockHttpServletRequest();
    	
    	request.addParameter("name", "网页报表");
    	request.addParameter("data", "仓库,库存\nOFC1,100\nOFC2,200");
    	
    	String fileName = display.data2CSV(request, response);
    	
    	display.download(response, fileName);
    }

    @Test
    public void testReportDisplay() {        
        HttpServletResponse response = Context.getResponse();
        MockHttpServletRequest  request = new MockHttpServletRequest();
        
        Report report1 = new Report();
        report1.setType(Report.TYPE1);
        report1.setParentId(Report.DEFAULT_PARENT_ID);
        report1.setName("报表一");
        report1.setScript(" select id, name from dm_report " +
        		" where id > ? " +
        		"  <#if param2??> and type <> ? <#else> and type = 1 </#if> " +
        		"  and (createTime > ? or createTime > ?) " +
        		"  and name in (${param5})");
        
        String paramsConfig = "[ {'label':'报表ID', 'type':'Number', 'nullable':'false', 'jsonUrl':'../xxx/list', 'multiple':'true'}," +
        		"{'label':'报表类型', 'type':'String'}," +
        		"{'label':'起始时间', 'type':'date', 'nullable':'false', 'defaultValue':'today-10'}, " +
        		"{'label':'结束时间', 'type':'date', 'nullable':'false'}," +
        		"{'label':'隐藏值', 'type':'hidden'}," +
        		"{'label':'组织列表', 'type':'String', 'nullable':'false'}]";
        report1.setParam(paramsConfig);
        
        report1.setRemark("test report");
        action.saveReport(response, report1);
        
        log.debug("开始测试报表展示：");
        request.addParameter("param1", "0");
        request.addParameter("param2", "0");
        request.addParameter("param3", "2013-10-01");
        request.addParameter("param4", "2013/10/01 11:11:11");
        request.addParameter("param5", "报表一,report-1");
        
        Long reportId = report1.getId();
        display.showAsGrid(request, response, reportId, 1, 10);
        display.showAsJson(request, reportId.toString());
        
        display.showAsJson(request, report1.getName());
        
        display.exportAsCSV(request, response, reportId, 1, 0); // 测试导出
        
        request = new MockHttpServletRequest();
        request.addParameter("param1", "0");
        request.addParameter("param3", "today - 100");
        request.addParameter("param4", "today + 10");
        request.addParameter("param5", "report-1,report-1");
        display.showAsJson(request, report1.getName());
        
        // test nocache
        request.addParameter("noCache", "noCache");
        display.showAsJson(request, report1.getName());
        request.removeParameter("noCache");
        
        // test jsonp
        request.addParameter("jsonpCallback", "func1");
        display.showAsJson(request, report1.getName());
        request.removeParameter("jsonpCallback");
        
        // 测试导出超阀值(导出时，前台限定50万行，超过该值将提示要求缩短查询条件，分批导出)
        Report reportGruop = new Report();
        reportGruop.setName("reportGruop1");
        reportGruop.setParentId(Report.DEFAULT_PARENT_ID);
        reportGruop.setType(Report.TYPE0);
		action.saveReport(response, reportGruop );
		
		reportGruop = new Report();
        reportGruop.setName("reportGruop2");
        reportGruop.setParentId(Report.DEFAULT_PARENT_ID);
        reportGruop.setType(Report.TYPE0);
		action.saveReport(response, reportGruop );
		
		request = new MockHttpServletRequest();
		request.addParameter("param1", "0");
		request.addParameter("param2", "1");
        request.addParameter("param3", "2013-10-01");
        request.addParameter("param4", "2013/10/01 11:11:11");
        request.addParameter("param5", "reportGruop1,reportGruop2");
		display.exportAsCSV(request, new MockHttpServletResponse(), reportId, 1, 1); // 阀值为1
		
        // test Customize report tree
		for(int i = 0; i < 30; i++) {
			display.showAsJson(request, report1.getName());
		}
        action.getCustomizeReports(response);
    }
    
    /**
     * 执行SQL时出错了:Parameter "#4" is not set; SQL statement:
		 select id, name from dm_report  where id > ? and type <> ? and (createTime > ? or createTime > ?)   and name in ('report-1','report-1')
		   数据源：jdbc:h2:mem:h2db,
		   参数：{1=0, 2=2013-10-01 00:00:00.0, 3=2013-10-01 11:11:11.0},
     */
    @Test
    public void testReportDisplayWithError() {        
        HttpServletResponse response = Context.getResponse();
        MockHttpServletRequest  request = new MockHttpServletRequest();
        
        Report report1 = new Report();
        report1.setType(Report.TYPE1);
        report1.setParentId(Report.DEFAULT_PARENT_ID);
        report1.setName("report-1");
        report1.setScript(" select id, name from dm_report " +
        		" where id > ? " +
        		"  <#if param2??> and type <> ? <#else> and type = 1 </#if> " +
        		"  and (createTime > ? or createTime > ?) " +
        		"  and name in (${param5})");
        
        String paramsConfig = "[ {'label':'报表ID', 'type':'Number', 'nullable':'false', 'jsonUrl':'../xxx/list', 'multiple':'true'}," +
        		"{'label':'报表类型', 'type':'String'}," +
        		"{'label':'起始时间', 'type':'date', 'nullable':'false'}, " +
        		"{'label':'结束时间', 'type':'date', 'nullable':'false'}," +
        		"{'label':'组织列表', 'type':'String', 'nullable':'false'}]"	;
        report1.setParam(paramsConfig);
        
        report1.setRemark("test report");
        action.saveReport(response, report1);
        
        log.debug("开始测试报表展示：");
//        request.addParameter("param1", "0");
        request.addParameter("param2", "0");
        request.addParameter("param3", "2013-10-01");
        request.addParameter("param4", "2013/10/01 11:11:11");
        request.addParameter("param5", "report-1,report-1");
        
        Long reportId = report1.getId();
        
		try {
			display.showAsJson(request, reportId.toString());
			Assert.fail("should throw exception but didn't.");
		} catch (Exception e) {
			log.debug(e.getMessage());
			Assert.assertTrue("参数个数不对：", true);
		}
    }
}