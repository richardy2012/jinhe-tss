package com.jinhe.tss.portal.module;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.component.param.ParamConstants;
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
import com.jinhe.tss.um.UMConstants;

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
        
//        Structure page2 = createPageOrSection(root, "页面二-2", "page2-2", Structure.TYPE_PAGE);
//        createPageOrSection(page2, "版面二-2", "section2-2", Structure.TYPE_SECTION);
        
        List<?> list = menuService.getAllNavigator();
        assertTrue(list.size() >= 1);
        
        list = menuService.getNavigatorsByPortal(root.getId());
        assertTrue(list.size() == 1);
        Navigator rootMenu = (Navigator) list.get(0);
        Long portalId = rootMenu.getPortalId();
        Long rootMenuId = rootMenu.getId();
        Assert.assertEquals(root.getId(), portalId);
        
        request.addParameter("parentId", rootMenuId + "");
        request.addParameter("type", Navigator.TYPE_MENU + "");
        menuAction.getNavigatorInfo(response, request, BaseActionSupport.DEFAULT_NEW_ID);
        
        menuAction.getNavigatorInfo(response, request, rootMenuId);
 
        // 创建各种类型的菜单项
        Navigator menu1 = new Navigator();
        menu1.setType(Navigator.TYPE_MENU_ITEM_3);
        menu1.setName("首页");
        menu1.setParentId(rootMenuId);
        menu1.setPortalId(portalId);
        menu1.setContent(root);
        menu1.setTarget("_blank");
        menu1.setDescription("unit test");
        menuAction.save(response, menu1);
        menuAction.getNavigatorInfo(response, request, menu1.getId());
        
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
	        menuAction.disable(response, rootMenuId, ParamConstants.TRUE);
	        menuAction.disable(response, rootMenuId, ParamConstants.FALSE);
        }
        
        // 排序、移动
        menuAction.sort(response, menu2.getId(), menu3.getId(), 1);

        menuAction.moveTo(response, menu3.getId(), menu1.getId());
        
        menuAction.getPortalNavigatorTree(response, menu2.getId()); // 移动的时候用到
        
        // 查询
        menuAction.getAllNavigator4Tree(response);
        menuAction.getOperations(response, rootMenuId);
        
        menuAction.getStructuresByPortal(response, portalId, Structure.TYPE_SECTION);
        menuAction.getStructuresByPortal(response, portalId, Structure.TYPE_PORTLET_INSTANCE);
        menuAction.getStructuresByPortal(response, portalId, Structure.TYPE_PAGE);
        menuAction.getStructuresByPortal(response, portalId, Structure.TYPE_PORTAL);
        
        // 生成菜单XML格式
        menuAction.getNavigatorXML(response, rootMenuId);
        
        // 匿名用户读取菜单（缓存）
        login(UMConstants.ANONYMOUS_USER_ID, "ANONYMOUS");
        menuAction.getNavigatorXML(response, rootMenuId);
        menuAction.getNavigatorXML(response, rootMenuId); // 访问两边以测试是否缓存成功
        
        menuAction.flushCache(response, rootMenuId);
        
        // 删除
        login(UMConstants.ADMIN_USER_ID, UMConstants.ADMIN_USER_NAME);
        
        menuAction.delete(response, rootMenuId);
        
        portalAction.delete(response, root.getId());
        
        assertTrue(TestUtil.printLogs(logService) > 0);
    }
    
}
