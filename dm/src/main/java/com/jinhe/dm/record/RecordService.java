package com.jinhe.dm.record;

import java.util.List;

import com.jinhe.tss.framework.component.log.Logable;
import com.jinhe.tss.um.permission.filter.PermissionFilter4Create;
import com.jinhe.tss.um.permission.filter.PermissionFilter4Sort;
import com.jinhe.tss.um.permission.filter.PermissionTag;

public interface RecordService {

    Record getRecord(Long id);
    
    List<Record> getAllRecords();
    
    @PermissionTag(
    		resourceType = Record.RESOURCE_TYPE,
	        operation = Record.OPERATION_CDATA
	)
    List<Record> getRecordables();
    
    @PermissionTag(
    		resourceType = Record.RESOURCE_TYPE,
	        operation = Record.OPERATION_VDATA
	)
    List<Record> getVisiables();

    @PermissionTag(
    		resourceType = Record.RESOURCE_TYPE,
	        operation = Record.OPERATION_CDATA
	)
    List<Record> getAllRecordGroups();

    @PermissionTag(
            resourceType = Record.RESOURCE_TYPE,
            operation = Record.OPERATION_EDIT , 
            filter = PermissionFilter4Create.class)
    @Logable(operateObject="数据录入",  operateInfo="新增/更新了：${args[0]?default(\"\")}")
    Record saveRecord(Record record);
    
    @Logable(operateObject="数据录入", operateInfo="删除了：${returnVal?default(\"\")}")
    Record delete(Long id);
 
    @PermissionTag(
            resourceType = Record.RESOURCE_TYPE,
            operation = Record.OPERATION_EDIT, 
            filter = PermissionFilter4Sort.class)
    @Logable(operateObject="数据录入", operateInfo="(ID: ${args[0]})节点移动到了(ID: ${args[1]})节点<#if args[2]=1>之下<#else>之上</#if>")
    void sort(Long startId, Long targetId, int direction);
 
    @Logable(operateObject="数据录入", operateInfo="移动(ID: ${args[0]}) 节点至 (ID: ${args[1]}) 组下。")
    void move(Long id, Long groupId);
}
