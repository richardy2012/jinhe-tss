package com.jinhe.tss.portal.module;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
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
 
    @Test
    public void testMenuModule() {
        // 新建portal
        Theme theme = new Theme();
        theme.setName("默认主题");
        
        Structure root = new Structure();
        root.setParentId(PortalConstants.ROOT_ID);
        root.setType(Structure.TYPE_PORTAL);
        root.setName("测试门户4MenuTest");
        root.setCode(System.currentTimeMillis() + "");
        root.setTheme(theme);
        portalAction.save(response, root); // create portal root
        
        List<?> list = menuService.getAllNavigator();
        assertTrue(list.size() >= 1);
        
        list = menuService.getNavigatorsByPortal(root.getId());
        assertTrue(list.size() == 1);
        Navigator rootMenu = (Navigator) list.get(0);
        Long portalId = rootMenu.getPortalId();
        Long rootMenuId = rootMenu.getId();
        Assert.assertEquals(root.getId(), portalId);
        
        menuAction.getNavigatorInfo(response, BaseActionSupport.DEFAULT_NEW_ID, Navigator.TYPE_MENU);
        menuAction.getNavigatorInfo(response, rootMenuId, Navigator.TYPE_MENU);
 
        // 创建各种类型的菜单项
        Navigator menu1 = new Navigator();
        menu1.setType(Navigator.TYPE_MENU_ITEM_3);
        menu1.setName("首页");
        menu1.setParentId(rootMenuId);
        menu1.setPortalId(portalId);
        menu1.setContent(root);
        menuAction.save(response, menu1);
        
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
        menu2_1.setContent(root);
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
        menu3.setContent(root);
        menu3.setToContent(root);
        menuService.saveNavigator(menu3);
        
        // 测试停用启用
        for(int i = 0; i < 2; i++) {
	        menuAction.disable(response, rootMenuId, PortalConstants.TRUE);
	        menuAction.disable(response, rootMenuId, PortalConstants.FALSE);
        }
        
        // 排序、移动
        menuAction.sort(response, menu2.getId(), menu3.getId(), 1);

        menuAction.getNavigatorsByPortal(response, portalId); // 移动的时候用到
 
        menuAction.moveTo(response, menu3.getId(), menu1.getId());
        
        // 查询
        menuAction.getAllNavigator4Tree(response);
        
        menuAction.getNavigatorsByPortal(response, portalId);

        menuAction.getStructuresByPortal(response, portalId, Structure.TYPE_SECTION);
        menuAction.getStructuresByPortal(response, portalId, Structure.TYPE_PORTLET_INSTANCE);
        
        // 生成菜单XML格式
        menuAction.getNavigatorXML(response, rootMenuId);
        
        // 删除
        menuAction.delete(response, rootMenuId);
        
        portalAction.delete(response, portalId);
        
        assertTrue(TestUtil.printLogs(logService) > 0);
    }
    
}
