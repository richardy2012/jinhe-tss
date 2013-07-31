package com.jinhe.tss.portal.dao;

import java.util.List;

import com.jinhe.tss.framework.persistence.ITreeSupportDao;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.entity.Navigator;
import com.jinhe.tss.portal.permission.PermissionFilter4Navigator;
import com.jinhe.tss.um.permission.filter.PermissionTag;

public interface INavigatorDao extends ITreeSupportDao<Navigator> {

    /**
     * 保存一个菜单或菜单项。
     * @param navigator
     * @return
     */
    Navigator save(Navigator navigator);
    
    /**
     * 移动一个菜单项。
     * @param navigator
     * @return
     */
    Navigator move(Navigator navigator);
    
    /**
     * 删除一个导航栏。
     * @param navigator
     */
    void deleteNavigator(Navigator navigator);
    
    /**
     * 根据菜单获取其下的所有菜单项集合。
     * 本方法将被拦截以进行权限过滤。
     * @param menuId
     * @return
     */
    @PermissionTag(filter = PermissionFilter4Navigator.class)
    List<Navigator> getMenuItemListByMenu(Long menuId);
    
    /**
     * 获取属于指定门户的菜单列表
     * @param portalId
     * @return
     */
	@PermissionTag(
			operation = PortalConstants.NAVIGATOR_VIEW_OPERRATION, 
			resourceType = PortalConstants.NAVIGATOR_RESOURCE_TYPE)
    List<Navigator> getMenusByPortal(Long portalId);
}
