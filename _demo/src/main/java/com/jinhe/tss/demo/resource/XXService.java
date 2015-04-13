package com.jinhe.tss.demo.resource;

import java.util.List;

import com.jinhe.tss.framework.component.log.Logable;
import com.jinhe.tss.um.permission.filter.PermissionFilter4Create;
import com.jinhe.tss.um.permission.filter.PermissionTag;

public interface XXService {

    XX getXX(Long id);
    
    @PermissionTag(
    		application = XX.APPLICATION,
    		resourceType = XX.RESOURCE_TYPE,
	        operation = XX.OPERATION_VIEW
	)
    List<XX> getAll();

    @PermissionTag(
    		application = XX.APPLICATION,
            resourceType = XX.RESOURCE_TYPE,
            operation = XX.OPERATION_EDIT , 
            filter = PermissionFilter4Create.class)
    @Logable(operateObject="XX资源",  operateInfo="新增/更新了：${args[0]?default(\"\")}")
    XX save(XX entity);
    
    @Logable(operateObject="XX资源", operateInfo="删除了：${returnVal?default(\"\")}")
    XX delete(Long id);
 
    @Logable(operateObject="XX资源", operateInfo="(ID: ${args[0]})节点移动到了(ID: ${args[1]})节点<#if args[2]=1>之下<#else>之上</#if>")
    void sort(Long startId, Long targetId, int direction);

    @Logable(operateObject="XX资源", operateInfo="移动(ID: ${args[0]}) XX资源至 (ID: ${args[1]}) XX资源下。")
    void move(Long id, Long targetId);
}
