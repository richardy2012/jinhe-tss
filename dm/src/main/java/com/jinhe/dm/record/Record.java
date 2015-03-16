package com.jinhe.dm.record;

public class Record {
	
	public static final int TYPE0 = 0;  // 数据录入分组
	public static final int TYPE1 = 1;  // 数据录入
    
	public static final Long DEFAULT_PARENT_ID = 0L;
    
    // 资源类型： 数据录入
    public static final String RESOURCE_TYPE = "D2"; 
    
    // 报表资源操作ID
    public static final String OPERATION_VIEW    = "1"; // 查看
    public static final String OPERATION_EDIT    = "2"; // 维护
    public static final String OPERATION_DELETE  = "3"; // 删除
    
}
