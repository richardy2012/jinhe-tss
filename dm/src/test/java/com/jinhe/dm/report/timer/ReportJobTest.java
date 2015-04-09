package com.jinhe.dm.report.timer;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.dm.DMConstants;
import com.jinhe.dm.TxTestSupport4DM;
import com.jinhe.dm.report.Report;
import com.jinhe.dm.report.ReportService;
import com.jinhe.tss.framework.component.param.Param;
import com.jinhe.tss.framework.component.param.ParamConstants;

public class ReportJobTest extends TxTestSupport4DM {
	
	@Autowired private ReportService service;

	@Test
	public void testReportJob() {
		
		Param paramL = addParamGroup(ParamConstants.DEFAULT_PARENT_ID, DMConstants.EMAIL_MACRO, "常用收件人组");
        addParamItem(paramL.getId(), "pjjin@800best.com", "JK", ParamConstants.COMBO_PARAM_MODE);
        
        Report report1 = new Report();
        report1.setType(Report.TYPE1);
        report1.setParentId(Report.DEFAULT_PARENT_ID);
        report1.setName("report-job-test");
        report1.setScript(" select id, name from dm_report " +
        		" where id > ? <#if param2??> and type <> ${param2} <#else> and type = 1 </#if>" +
        		"	and createTime > ?");
        
        String paramsConfig = 
        		"[ {'label':'报表ID', 'type':'Number', 'nullable':'false', 'jsonUrl':'../xxx/list', 'multiple':'true'}," +
        		  "{'label':'报表类型', 'type':'String'}," +
        		  "{'label':'创建时间', 'type':'date'}]"	;
        report1.setParam(paramsConfig);
        
        service.saveReport(report1);
        
        ReportJob job = new ReportJob();
        
        String jobConfig = report1.getId() + ":报表一:pjjin@800best.com,BL00618,-1@tssRole,-2@tssGroup,${JK}:param1=0,param2=0,param3=today-0\n" + 
        		           report1.getId() + ":报表二:BL00618,pjjin@800best.com:param1=0,param3=today-1\n" +
        		           report1.getId() + ":报表三:BL00618,pjjin@800best.com:param1=0,param3=today-1";
		try{
        	job.excuteJob(jobConfig);
		} catch(Exception e) {
        	log.error(e.getCause());
        	
        }
	}
	
    protected String getDefaultSource(){
    	return "connectionpool";
    }
}
