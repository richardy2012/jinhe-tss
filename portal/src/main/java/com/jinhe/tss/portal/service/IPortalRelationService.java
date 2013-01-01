package com.jinhe.tss.portal.service;

import java.util.List;

import com.jinhe.tss.framework.component.log.Logable;
import com.jinhe.tss.portal.entity.IssueInfo;
import com.jinhe.tss.portal.entity.Theme;

/** 
 * 门户相关的（包括门户主题、门户发布等）的相关维护
 */
public interface IPortalRelationService {
    
    //******************************* 以下为主题管理 ***************************************************************
    /**
     * 门户下的当前主题另存为。。。
     * @param themeId 
     * @param themeName
     */
    @Logable(operateTable="门户主题", operateType="复制", 
            operateInfo="复制(ID: ${args[1]})主题"
        )
    Theme saveThemeAs(Long themeId, String themeName);

    /**
     * 获取一个Portal的所有主题
     * @param portalId
     * @return
     */
    List<?> getThemesByPortal(Long portalId);

    /**
     * 设置默认主题
     * @param portalId
     * @param themeId
     */
    @Logable(operateTable="门户主题", operateType="设为默认", 
            operateInfo="将(ID: ${args[1]})主题设置为默认主题"
        )
    void specifyDefaultTheme(Long portalId, Long themeId);

    /**
     * 删除主题，如果删除的主题是当前门户的默认主题或者当前主题，则删除失败
     * @param portalId
     * @param themeId
     */
    @Logable(operateTable="门户主题", operateType="删除", 
            operateInfo="删除了(ID: ${args[1]})主题"
        )
    void removeTheme(Long portalId, Long themeId);

    /**
     * 重命名主题名字
     * @param themeId
     * @param name
     */
    @Logable(operateTable="门户主题", operateType="重命名", 
            operateInfo="将(ID: ${args[0]})主题重新命名为 ${args[1]}"
        )
    void renameTheme(Long themeId, String name);
    
    
    //******************************* 以下为门户发布管理 ***************************************************************
    /**
     * 根据访问地址或者门户的真实地址
     * @param visitUrl
     * @return
     */
    IssueInfo getIssueInfo(String visitUrl);

    /**
     * 获取所有的门户发布信息
     * @return
     */
    List<?> getAllIssues();

    /**
     * 保存发布信息
     * @param issueInfo
     * @return
     */
    IssueInfo saveIssue(IssueInfo issueInfo);

    /**
     * 移除发布信息
     * @param id
     */
    void removeIssue(Long id);

    /**
     * 获取发布信息
     * @param id
     * @return
     */
    IssueInfo getIssueInfo(Long id);
    
    
    //******************************** 以下为门户自定义管理 ***************************************************************
    /**
     * <p>
     * 保存用户自定义信息。<br>
     * 当用户执行以下自定义操作后，其自定义信息将被保存：<br>
     * 1.portlet实例的更改、交换、删除、参数更改<br>
     * 2.修饰器的更改、参数更改<br>
     * 3.布局器的更改、交换（版面交换）、删除（版面删除）、添加、参数更改<br>
     * 4.还原<br>
     * </p>
     * @param portalId
     * @param userId
     * @param pageId
     * @param personalXML
     */
    @Logable(operateTable="门户自定义", operateType="门户自定义操作", 
            operateInfo="保存了 ID为 ${args[0]} 的门户的自定义信息"
        )
    void savePersonalInfo(Long portalId, Long themeId, Long userId, Long pageId, String personalXML);
    
    /**
     * 删除用户自定义信息。<br>
     * 当用户选择还原为默认布局时，删除该用户的自定义信息和自定义主题信息。<br>
     * 
     * @param portalId
     * @param userId
     * @param pageId
     */
    @Logable(operateTable="门户自定义", operateType="门户自定义操作", 
            operateInfo="删除了 ID为 ${args[0]} 的门户的自定义信息"
        )
    void removePersonalInfo(Long portalId, Long themeId, Long userId, Long pageId);
    
    /**
     * 保存用户自定义主题信息。
     * 
     * @param portalId
     * @param userId
     * @param themeId
     */
    @Logable(operateTable="门户主题", operateType="门户自定义操作", 
            operateInfo="重新设置了 ID为 ${args[0]} 的门户的自定义主题 "
        )
    void savePersonalTheme(Long portalId, Long userId, Long themeId);
    
    /**
     * 获取可以用以portlet实例替换的
     * @param portalId
     * @return
     */
    List<?> getPortletInstansesInPortal(Long portalId);
    
    
    //***********************************  门户流量统计获取 ***************************************************************
    /**
     * 获取门户下页面的访问流量
     * @param portalId
     * @return
     */
    List<?> getFlowRate(Long portalId);
}

