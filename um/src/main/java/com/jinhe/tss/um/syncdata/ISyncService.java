package com.jinhe.tss.um.syncdata;

import java.util.Map;
 
public interface ISyncService {

    /**
     * 同步单个其它应用组下的用户
     * 
     * @param groupId
     * @param userId
     */
    void syncUser(Long groupId, Long userId);

    /**
     * 获取完全同步组时候需要用到的数据
     * @param applicationId 应用ID
     * @param groupId 选中进行同步的组ID
     * @param dbGroupId 选中进行同步的组对应外部应用的ID
     * @return
     */
    Map<String, Object> getCompleteSyncGroupData(Long groupId, String dbGroupId, String applicationId);

    /**
     * 获取单向同步组时候需要用到的数据
     * @param applicationId 应用ID
     * @param groupId 选中进行同步的组ID
     * @param dbGroupId 选中进行同步的组对应外部应用的ID
     * @return
     */
    Map<String, Object> getUniDirectionalSyncGroupData(Long groupId, String dbGroupId, String applicationId);

}
