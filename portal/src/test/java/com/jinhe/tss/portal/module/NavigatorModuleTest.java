package com.jinhe.tss.portal.module;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.TxSupportTest4Portal;
import com.jinhe.tss.portal.action.NavigatorAction;
import com.jinhe.tss.portal.action.PortalAction;
import com.jinhe.tss.portal.entity.Navigator;
import com.jinhe.tss.portal.entity.Structure;
import com.jinhe.tss.portal.entity.Theme;
import com.jinhe.tss.portal.service.INavigatorService;
import com.jinhe.tss.portal.service.IPortalService;

/**
 * 导航栏模块的单元测试。
 */
public class NavigatorModuleTest extends TxSupportTest4Portal {
    
    @Autowired PortalAction portalAction;
    @Autowired NavigatorAction menuAction;
    
    @Autowired IPortalService portalService;
    @Autowired INavigatorService menuService;
 
    public void testMenuModule() {
        // 新建portal
        Theme theme = new Theme();
        theme.setName("默认主题");
        
        Structure root = new Structure();
        root.setParentId(PortalConstants.ROOT_ID);
        root.setType(Structure.TYPE_PORTAL);
        root.setName("Jon的门户");
        root.setTheme(theme);
        portalAction.save(root, "TempPortalCode"); // create portal root
        
        List<?> list = menuService.getAllNavigator();
        assertTrue(list.size() == 1);
        Navigator rootMenu = (Navigator) list.get(0);
        Long portalId = rootMenu.getPortalId();
        Long rootMenuId = rootMenu.getId();
        
        menuAction.getNavigatorInfo(BaseActionSupport.DEFAULT_NEW_ID, rootMenuId, Navigator.TYPE_MENU);
        menuAction.getNavigatorInfo(rootMenuId, PortalConstants.ROOT_ID, Navigator.TYPE_MENU);
 
        // 创建各种类型的菜单项
        Navigator menu1 = new Navigator();
        menu1.setType(Navigator.TYPE_MENU_ITEM_3);
        menu1.setName("首页");
        menu1.setParentId(rootMenuId);
        menu1.setPortalId(portalId);
        menu1.setContent(null);
        menuAction.save(menu1);
        
        Navigator menu2 = new Navigator();
        menu2.setType(Navigator.TYPE_MENU_ITEM_7);
        menu2.setName("机构职责");
        menu2.setParentId(rootMenuId);
        menu2.setPortalId(portalId);
        menu2.setUrl("${common.articleListUrl}&channelId=38");
        menuService.saveNavigator(menu2);
        
        Navigator menu2_1 = new Navigator();
        menu2_1.setType(Navigator.TYPE_MENU_ITEM_6);
        menu2_1.setName("授权管理");
        menu2_1.setParentId(menu2.getId());
        menu2_1.setPortalId(portalId);
        menu2_1.setMethodName("jumpTo");
        menu2_1.setParams("appCode:\'UMS\',redirect:\'http://${PT_ip}/ums/redirect.html\',url:\'ums/permission.htm\'");
        menu2_1.setContent(null);
        menuService.saveNavigator(menu2_1);
        
        Navigator menu2_2 = new Navigator();
        menu2_2.setType(Navigator.TYPE_MENU_ITEM_4);
        menu2_2.setName("Google");
        menu2_2.setParentId(menu2.getId());
        menu2_2.setPortalId(portalId);
        menu2.setUrl("www.google.com");
        menuService.saveNavigator(menu2_2);
        
        Navigator menu3 = new Navigator();
        menu3.setType(Navigator.TYPE_MENU_ITEM_5);
        menu3.setName("二级页面");
        menu3.setParentId(rootMenuId);
        menu3.setPortalId(portalId);
        menu3.setContent(null);
        menu3.setToContent(null);
        menuService.saveNavigator(menu3);
        
        // 测试停用启用
        for(int i = 0; i < 2; i++) {
	        menuAction.disable(rootMenuId, PortalConstants.TRUE);
	        menuAction.disable(rootMenuId, PortalConstants.FALSE);
        }
        
        // 排序、移动
        menuAction.sort(menu2.getId(), menu3.getId(), 1);

        menuAction.getNavigators4Tree(menu3.getId(), portalId); // 移动的时候用到
 
        menuAction.move(menu3.getId(), menu1.getId());
        
        // 查询
        menuAction.getAllNavigator4Tree();
        
        menuAction.getNavigatorsByPortal(portalId);

        menuAction.getStructuresByPortal(portalId, Structure.TYPE_SECTION);
        menuAction.getStructuresByPortal(portalId, Structure.TYPE_PORTLET_INSTANCE);
        
        // 生成菜单XML格式
        menuAction.getNavigatorXML(rootMenuId);
        
        // 删除
        menuAction.delete(rootMenuId);
        
        portalAction.delete(portalId);
        
        assertTrue(TestUtil.printLogs(logService) > 0);
    }
    
}
