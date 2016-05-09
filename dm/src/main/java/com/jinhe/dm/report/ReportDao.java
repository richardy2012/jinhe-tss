package com.jinhe.dm.report;

import java.util.List;

import com.jinhe.tss.framework.persistence.ITreeSupportDao;
import com.jinhe.tss.um.permission.filter.PermissionFilter4Branch;
import com.jinhe.tss.um.permission.filter.PermissionFilter4Check;
import com.jinhe.tss.um.permission.filter.PermissionTag;
 
public interface ReportDao extends ITreeSupportDao<Report> {
	
    @PermissionTag(
    		resourceType = Report.RESOURCE_TYPE,
            filter = PermissionFilter4Branch.class)
    List<Report> getChildrenById(Long id, String operationId);
    
    Report deleteReport(Report report);
    
    @PermissionTag(
            operation = Report.OPERATION_VIEW, 
            resourceType = Report.RESOURCE_TYPE,
            filter = PermissionFilter4Check.class)
    Report getReportById(Long id);
 
}
