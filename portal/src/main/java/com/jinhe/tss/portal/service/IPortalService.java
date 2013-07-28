package com.jinhe.tss.portal.service;

import java.util.List;

import com.jinhe.tss.framework.component.log.Logable;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.engine.model.PortalNode;
import com.jinhe.tss.portal.entity.IssueInfo;
import com.jinhe.tss.portal.entity.Structure;
import com.jinhe.tss.portal.entity.Theme;
import com.jinhe.tss.portal.helper.PortalStructureWrapper;
import com.jinhe.tss.portal.permission.PermissionFilter4Check;
import com.jinhe.tss.portal.permission.PermissionFilter4Portal;
import com.jinhe.tss.um.permission.filter.PermissionFilter4CopyTo;
import com.jinhe.tss.um.permission.filter.PermissionFilter4Create;
import com.jinhe.tss.um.permission.filter.PermissionFilter4Move;
import com.jinhe.tss.um.permission.filter.PermissionFilter4Sort;
import com.jinhe.tss.um.permission.filter.PermissionFilter4Update;
import com.jinhe.tss.um.permission.filter.PermissionTag;
 
public interface IPortalService {

    /**
     * <p>
     * 获取所有门户结构PortalStructure
     * </p>
     */
    @PermissionTag(
            operation = PortalConstants.PORTAL_VIEW_OPERRATION, 
            resourceType = PortalConstants.PORTAL_RESOURCE_TYPE)
    List<?> getAllPortalStructures();

    /**
     * <p>
     * 获取除portlet应用外的门户结构。移动到...，复制到...的时候将调用本方法。将根据“新增”操作选项过滤
     * </p>
     */
    @PermissionTag(
            operation = PortalConstants.PORTAL_ADD_OPERRATION, 
            resourceType = PortalConstants.PORTAL_RESOURCE_TYPE)
    List<?> getTargetPortalStructures();
    
    /**
     * 获取所有启用门户
     */
    @PermissionTag(
            operation = PortalConstants.PORTAL_VIEW_OPERRATION, 
            resourceType = PortalConstants.PORTAL_RESOURCE_TYPE)
    List<?> getActivePortals();

    /**
     * 获取指定门户下启用的页面和版面
     * @param portalId
     */
    @PermissionTag(
            operation = PortalConstants.PORTAL_VIEW_OPERRATION, 
            resourceType = PortalConstants.PORTAL_RESOURCE_TYPE)
    List<?> getActivePagesByPortal(Long portalId);
    
    /**
     * 获取门户下所有可用的门户结构节点列表（不包括门户根节点）
     * @param portalId
     */
    @PermissionTag(
            operation = PortalConstants.PORTAL_VIEW_OPERRATION, 
            resourceType = PortalConstants.PORTAL_RESOURCE_TYPE)
    List<?> getPortalStructuresByPortal(Long portalId);

    /**
     * <p>
     * 获取单个门户结构PortalStructure的节点信息，<p></p>
     * 如果该门户结构PortalStructure是根节点，则要一块取出其对应门户Portal的信息
     * </p>
     * @param id
     *         门户结构主键ID值
     * @return
     *         门户结构对象
     */
    @PermissionTag(
            operation = PortalConstants.PORTAL_VIEW_OPERRATION, 
            resourceType = PortalConstants.PORTAL_RESOURCE_TYPE,
            filter = PermissionFilter4Check.class)
    Structure getPoratalStructure(Long id);
    
    /**
     * 只取门户结构节点
     * @param id
     * @return
     */
    @PermissionTag(
            operation = PortalConstants.PORTAL_VIEW_OPERRATION, 
            resourceType = PortalConstants.PORTAL_RESOURCE_TYPE,
            filter = PermissionFilter4Check.class)
    Structure getPortalStructureById(Long id);

    /**
     * <p>
     * 保存门户结构信息，如果该门户结构PortalStructure是根节点，则要一块保存其门户Portal的信息。<p></p>
     * 更新操作也一样。
     * 
     * save()结束返回 该门户结构PortalStructure 对象
     * </p>
     * 
     * @param portalStructure
     *            要保存的门户结构实体（如果是根节点，则实体类型为其子类RootPortalStructure
     * @return
     *            保存成功以后的门户结构实体
     */
    @Logable(operateTable="门户结构", operateType="新建", operateInfo="新建了 ${returnVal} 节点")
    @PermissionTag(
            operation = PortalConstants.PORTAL_ADD_OPERRATION , 
            resourceType = PortalConstants.PORTAL_RESOURCE_TYPE,
            filter = PermissionFilter4Create.class)
    Structure createPortalStructure(PortalStructureWrapper psw);
    
    @Logable(operateTable="门户结构", operateType="修改", operateInfo="修改了 ${returnVal} 节点")
    @PermissionTag(
            operation = PortalConstants.PORTAL_EDIT_OPERRATION, 
            resourceType = PortalConstants.PORTAL_RESOURCE_TYPE,
            filter = PermissionFilter4Update.class)
    Structure updatePortalStructure(PortalStructureWrapper psw);
    
    /**
     * <p>
     * 删除门户结构PortalStructure
     * 如果有子节点，同时删除子节点（递归过程，子节点的子节点......)
     * </p>
     * 
     * @param id
     *          操作节点的ID
     */
    @Logable(operateTable="门户结构", operateType="删除", operateInfo="删除了(ID: ${args[0]})节点")
    void delete(Long id);

    /**
     * <p>
     * 停用/启用 门户结构PortalStructure（将其下的disabled属性设为"1"/"0"）<p></p>
     * 停用时，如果有子节点，同时停用所有子节点（递归过程，子节点的子节点......)<p></p>
     * 启用时，如果有父节点且父节点为停用状态，则启用父节点（也是递归过程，父节点的父节点......）<p></p>
     * 前提：用户对被操作的节点及其所有子节点（或父节点）有停用启用的权限。
     * </p>
     * 
     * @param id
     *          操作节点的ID
     * @param disabled
     *          停用或者启用（1/0）
     */
    @Logable(operateTable="门户结构", operateType="停用/启用", operateInfo="<#if args[1]=1>停用<#else>启用</#if>了(ID: ${args[0]})节点")
    void disable(Long id, Integer disabled);

    /**
     * <p>
     * 跨父节点移动门户结构PortalStructure节点<p></p>
     * 移动到弹出窗口中选中的门户结构下（一般为"版面"节点），即将移动节点的parentId设为目标的id。<p></p>
     * 同时设置其排序属性sqpNo，放到最后面<p></p>
     * 
     * 前提：用户对被操作的节点及其所有子节点有查看权限。
     * </p>
     * 
     * @param id
     *          要移动节点的id
     * @param targetId
     *          目标节点ID
     * @param portalId 
     *          目标节点的portalId
     */
    @Logable(operateTable="门户结构", operateType="移动", operateInfo="移动(ID: ${args[0]})节点到(ID: ${args[1]})节点下")
    @PermissionTag(
            operation = PortalConstants.PORTAL_ADD_OPERRATION + "," + PortalConstants.PORTAL_DEL_OPERRATION, 
            resourceType = PortalConstants.PORTAL_RESOURCE_TYPE,
            filter = PermissionFilter4Move.class)
    void move(Long id, Long targetId, Long portalId);

    /**
     * * <p>
     * 排序，同节点下的节点排序（一次只能排一个）
     * </p>
     * 
     * @param id
     *          排序节点ID
     * @param targetId
     *          目标节点ID
     * @param direction
     *          方向
     */
    @Logable(operateTable="门户结构", operateType="排序", operateInfo="(ID: ${args[0]})节点移动到了(ID: ${args[1]})节点<#if args[2]=1>之下<#else>之上</#if>")
    @PermissionTag(
            operation = PortalConstants.PORTAL_ORDER_OPERRATION, 
            resourceType = PortalConstants.PORTAL_RESOURCE_TYPE,
            filter = PermissionFilter4Sort.class)
    void order(Long id, Long targetId, int direction);
    
    /**
     * 复制到。。。
     * @param id
     * @param name
     * @param targetId
     * @param portalId
     * @return
     */
    @Logable(operateTable="门户结构", operateType="复制到", operateInfo="复制(ID: ${args[0]})节点到(ID: ${args[1]})下")
    @PermissionTag(
            operation = PortalConstants.PORTAL_ADD_OPERRATION, 
            resourceType = PortalConstants.PORTAL_RESOURCE_TYPE,
            filter = PermissionFilter4CopyTo.class)
    List<Structure> copyTo(Long id, Long targetId, Long portalId);
    
    /**
     * 获取一个门户树的一份拷贝。
     * 方法将会被权限过滤拦截器拦截，用户没有权限的门户结构节点将会被过滤掉。
     * 如果用户是匿名用户，则应该调用本方法，因为匿名用户没有自定义门户。
     * 
     * @param portalId
     * @param method 
     *             查看方式  维护预览/浏览/查看  maintain/browse/view
     * @return
     */
    @PermissionTag(filter = PermissionFilter4Portal.class)
    PortalNode getPortal(Long portalId, Long themeId, String method);
    
    
    /****************************   门户相关的（包括门户主题、门户发布等）的相关维护  *************************************************/
    
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
    
    //***********************************  门户流量统计获取 ***************************************************************
    /**
     * 获取门户下页面的访问流量
     * @param portalId
     * @return
     */
    List<?> getFlowRate(Long portalId);

}