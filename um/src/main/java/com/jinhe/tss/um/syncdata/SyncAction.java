package com.jinhe.tss.um.syncdata;

import java.util.List;
import java.util.Map;

import com.jinhe.tss.framework.component.progress.ProgressManager;
import com.jinhe.tss.framework.component.progress.Progressable;
import com.jinhe.tss.framework.exception.BusinessException;
import com.jinhe.tss.framework.web.mvc.ProgressActionSupport;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.entity.Group;
import com.jinhe.tss.um.service.IGroupService;
import com.jinhe.tss.util.EasyUtils;

/**
 * 用户组织同步action
 */
public class SyncAction extends ProgressActionSupport {

    private String applicationId;  //应用ID
    private Long groupId; //需要同步的其它用户组ID
    private Long mode;    //单向同步 or 完全同步
    private Long userId;  //需要单个同步的用户ID
    
    private IGroupService groupService;
    private ISyncService  syncService;

    public String syncData() {
        Group group = groupService.getGroupById(groupId);
        String dbGroupId = group.getDbGroupId();
        if ( EasyUtils.isNullOrEmpty(dbGroupId) ) {
            throw new BusinessException("导入组的对应外部应用组的ID（dbGroupId）为空");
        }
        
        Map<String, Object> datasMap;
        if (UMConstants.ALL_SYNC.equals(mode)) {
            datasMap = syncService.getCompleteSyncGroupData(groupId, dbGroupId, applicationId);
        }
        else {
            datasMap = syncService.getUniDirectionalSyncGroupData(groupId, dbGroupId, applicationId);
        }
        
        datasMap.put("mode", mode);
        
        List<?> groups = (List<?>)datasMap.get("groups");
        List<?> users  = (List<?>)datasMap.get("users");
        int totalCount = users.size() + groups.size();
        
        // 因为同步数据会启用进度条中的线程进行，所以需要在action中启动，而不是在service，在service的话会导致事务提交不了
		ProgressManager manager = new ProgressManager((Progressable) syncService, totalCount, datasMap);
        String code = manager.execute(); 
        
        return printScheduleMessage(code);
    }

    public String syncUser() {
        syncService.syncUser(groupId, userId);
        return printSuccessMessage();
    }
 
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    public void setMode(Long mode) {
        this.mode = mode;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public void setSyncService(ISyncService service) {
        this.syncService = service;
    }
    public void setGroupService(IGroupService groupService) {
        this.groupService = groupService;
    }
}
