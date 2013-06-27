package com.jinhe.tss.um.syncdata;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
@Controller
@RequestMapping("syncdata")
public class SyncAction extends ProgressActionSupport {
    
    @Autowired private IGroupService groupService;
    @Autowired private ISyncService  syncService;

    @RequestMapping("/{groupId}")
    public void syncData(String applicationId, Long groupId, int mode) {
        Group group = groupService.getGroupById(groupId);
        String dbGroupId = group.getDbGroupId();
        if ( EasyUtils.isNullOrEmpty(dbGroupId) ) {
            throw new BusinessException("导入组的对应外部应用组的ID（dbGroupId）为空");
        }
        
        Map<String, Object> datasMap;
        if ( UMConstants.ALL_SYNC == mode ) {
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
        
        printScheduleMessage(code);
    }
    
    // 单个同步用户
    @RequestMapping("/{groupId}/{userId}")
    public void syncUser(Long groupId, Long userId) {
        syncService.syncUser(groupId, userId);
        printSuccessMessage();
    }
}
