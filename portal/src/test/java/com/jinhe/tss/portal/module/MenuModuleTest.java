package com.jinhe.tss.portal.module;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.TxSupportTest4Portal;
import com.jinhe.tss.portal.action.NavigatorAction;
import com.jinhe.tss.portal.action.PortalAction;
import com.jinhe.tss.portal.entity.Navigator;
import com.jinhe.tss.portal.entity.PortalStructure;
import com.jinhe.tss.portal.helper.PortalStructureWrapper;
import com.jinhe.tss.portal.service.INavigatorService;
import com.jinhe.tss.portal.service.IPortalService;

/**
 * Menu相关模块的单元测试。
 */
public class MenuModuleTest extends TxSupportTest4Portal {
    
    PortalAction portalAction;
    NavigatorAction menuAction;
    
    @Autowired IPortalService portalService;
    @Autowired INavigatorService menuService;
	
    public void setUp() throws Exception {
        super.setUp();
        portalAction = new PortalAction();
        portalAction.setService(portalService);
        
        menuAction = new NavigatorAction();
        menuAction.setService(menuService);
        menuAction.setPortalService(portalService);
    }
 
    public void testMenuModule() {
        // 新建portal
        PortalStructureWrapper psw = portalAction.getPs();
        psw.setParentId(PortalConstants.ROOT_ID);
        psw.setType(PortalStructure.TYPE_PORTAL);
        psw.setName("Jon的门户");
        psw.setDescription("测试门户");
        psw.setThemeName("默认主题");
        portalAction.save(); // create portal root
        
        List<?> list = menuService.getAllNavigator();
        assertTrue(list.size() == 1);
        Navigator rootMenu = (Navigator) list.get(0);
        Long portalId = rootMenu.getPortalId();
        Long rootMenuId = rootMenu.getId();
        
        menuAction.setIsNew(PortalConstants.TRUE);
        menuAction.setType(Navigator.TYPE_MENU);
        menuAction.setParentId(rootMenuId);
        menuAction.setPortalId(portalId);
        menuAction.getNavigatorInfo();
        
        menuAction.setIsNew(PortalConstants.FALSE);
        menuAction.setId(rootMenuId);
        menuAction.getNavigatorInfo();
 
        // 创建各种类型的菜单项
        Navigator menu1 = menuAction.getNavigator();
        menu1.setType(Navigator.TYPE_MENU_ITEM_3);
        menu1.setName("首页");
        menu1.setParentId(rootMenuId);
        menu1.setPortalId(portalId);
        menu1.setContentId(66L);
        menuAction.save();
        
        Navigator menu2 = new Navigator();
        menu2.setType(Navigator.TYPE_MENU_ITEM_7);
        menu2.setName("机构职责");
        menu2.setParentId(rootMenuId);
        menu2.setPortalId(portalId);
        menu2.setUrl("${common.articleListUrl}&channelId=38");
        menuService.saveMenu(menu2);
        
        Navigator menu2_1 = new Navigator();
        menu2_1.setType(Navigator.TYPE_MENU_ITEM_6);
        menu2_1.setName("授权管理");
        menu2_1.setParentId(menu2.getId());
        menu2_1.setPortalId(portalId);
        menu2_1.setMethodName("jumpTo");
        menu2_1.setParams("appCode:\'UMS\',redirect:\'http://${PT_ip}/ums/redirect.html\',url:\'ums/permission.htm\'");
        menu2_1.setContentId(16L);
        menu2_1.setContentName("IFrame");
        menuService.saveMenu(menu2_1);
        
        Navigator menu2_2 = new Navigator();
        menu2_2.setType(Navigator.TYPE_MENU_ITEM_4);
        menu2_2.setName("Google");
        menu2_2.setParentId(menu2.getId());
        menu2_2.setPortalId(portalId);
        menu2.setUrl("www.google.com");
        menuService.saveMenu(menu2_2);
        
        Navigator menu3 = new Navigator();
        menu3.setType(Navigator.TYPE_MENU_ITEM_5);
        menu3.setName("二级页面");
        menu3.setParentId(rootMenuId);
        menu3.setPortalId(portalId);
        menu3.setContentId(66L);
        menu3.setTargetId(88L);
        menuService.saveMenu(menu3);
        
        // 测试停用启用
        for(int i = 0; i < 2; i++) {
	        menuAction.setId(rootMenuId);
	        menuAction.setDisabled(PortalConstants.TRUE);
	        menuAction.disable();
	        
	        menuAction.setDisabled(PortalConstants.FALSE);
	        menuAction.disable();
        }
        
        // 排序、移动
        menuAction.setId(menu2.getId());
        menuAction.setTargetId(menu3.getId());
        menuAction.setDirection(1);
        menuAction.sort();
        
        menuAction.setPortalId(portalId);
        menuAction.setId(menu3.getId());
        menuAction.getNavigators4Tree(); // 移动的时候用到
        
        menuAction.setPortalId(portalId);
        menuAction.setId(menu3.getId());
        menuAction.setTargetId(menu1.getId());
        menuAction.move();
        
        // 查询
        menuAction.getAllNavigator4Tree();
        
        menuAction.setPortalId(portalId);
        menuAction.getMenus4TreeByPortal();
        
        menuAction.setPortalId(portalId);
        menuAction.setType(PortalStructure.TYPE_SECTION);
        menuAction.getPortalStructuresByPortal4Tree();
        menuAction.setType(PortalStructure.TYPE_PORTLET_INSTANCE);
        menuAction.getPortalStructuresByPortal4Tree();
        
        // 生成菜单XML格式
        menuAction.setId(rootMenuId);
        menuAction.getMenuXML();
        
        // 删除
        menuAction.setId(rootMenuId);
        menuAction.delete();
        
        List<?> data = portalService.getActivePortals();
        assertEquals(1, data.size());
        portalAction.setId(((PortalStructure) data.get(0)).getId());
        portalAction.delete();
        
        assertTrue(TestUtil.printLogs(logService) > 0);
    }
    
}
