package com.jinhe.dm;

import com.jinhe.tss.framework.component.param.ParamConfig;
import com.jinhe.tss.util.EasyUtils;

public final class DMConstants {
	
	public final static String DATASOURCE_LIST   = "datasource_list";
	public final static String DEFAULT_CONN_POOL = "default_conn_pool";
	public final static String LOCAL_CONN_POOL   = "connectionpool";

	public final static String TEMP_EXPORT_PATH  = "TEMP_EXPORT_PATH";
    
	public final static String SCRIPT_MACRO = "ReportScriptMacros";
	public final static String EMAIL_MACRO  = "EmailMacros";
    
	public final static String USER_ID = "userId";
	public final static String USER_CODE = "userCode";
	public final static String FROM_USER_ID = "fromUserId";
    
    /**
     * 报表模板资源文件目录
     */
	public static final String REPORT_TL_DIR_DEFAULT = "dm/template";
	public static final String REPORT_TL_DIR = "report.template.dir";
	public static final String REPORT_TL_TYPE = "reportTL";
	
	public static String getReportTLDir() {
		String tlDir = ParamConfig.getAttribute(REPORT_TL_DIR);
		if(EasyUtils.isNullOrEmpty(tlDir)) {
			tlDir = REPORT_TL_DIR_DEFAULT;
		}
		return "modules/" + tlDir;
	}
    
}
