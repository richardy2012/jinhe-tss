package com.jinhe.tss.portal.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.TxSupportTest4Portal;
import com.jinhe.tss.portal.action.ComponentAction;
import com.jinhe.tss.portal.action.NavigatorAction;
import com.jinhe.tss.portal.action.PortalAction;
import com.jinhe.tss.portal.entity.Component;
import com.jinhe.tss.portal.entity.ReleaseConfig;
import com.jinhe.tss.portal.entity.Structure;
import com.jinhe.tss.portal.entity.Theme;
import com.jinhe.tss.portal.service.IPortalService;
import com.jinhe.tss.util.URLUtil;

/**
 * 门户结构相关模块的单元测试。
 */
public class PortalModuleTest extends TxSupportTest4Portal {
    
    @Autowired PortalAction portalAction;
    @Autowired ComponentAction elementAction;
    @Autowired NavigatorAction menuAction;
    
    @Autowired IPortalService portalService;
 
    @Test
    public void testPortalModule() {
        Long structureId = BaseActionSupport.DEFAULT_NEW_ID;
        Long parentId = PortalConstants.ROOT_ID;
        
        // 新建portal
        request.addParameter("type", Structure.TYPE_PORTAL + "");
        request.addParameter("parentId", parentId + "");
        portalAction.getStructureInfo(response, request, structureId);
        
        Theme theme = new Theme();
        theme.setName("默认主题");
        
        Structure root = new Structure();
        root.setParentId(parentId);
        root.setType(Structure.TYPE_PORTAL);
        root.setName("Jon的门户-1");
        root.setSupplement("<page><property><name>Jon的门户</name><description><![CDATA[]]></description></property><script><file><![CDATA[]]></file><code><![CDATA[]]></code></script><style><file><![CDATA[]]></file><code><![CDATA[]]></code></style></page>");
        root.setDescription("测试门户-2");
        root.setTheme(theme);
        root.setCode(System.currentTimeMillis() + "");
        portalAction.save(response, root); // create portal root
        
        List<?> list = portalService.getAllStructures();
        assertTrue(list.size() >= 1);

        Long portalId = root.getPortalId();
        structureId = root.getId();
        
        request.addParameter("type", Structure.TYPE_PORTAL + "");
        request.addParameter("parentId", parentId + "");
        portalAction.getStructureInfo(response, request, structureId);
  
        portalAction.save(response, root); // update portal root
        
        // 获取节点操作权限
        portalAction.getOperationsByResource(response, structureId);
        
        // 新建页面、版面
        Structure page1 = createPageOrSection(root, "页面一", "page1", Structure.TYPE_PAGE);
        Structure page2 = createPageOrSection(root, "页面二", "page2", Structure.TYPE_PAGE);
        Structure section1 = createPageOrSection(page1, "版面一", "section1", Structure.TYPE_SECTION);
        Structure section2 = createPageOrSection(page2, "版面二", "section2", Structure.TYPE_SECTION);
        Component portlet = createTestPortlet();
        createPortletInstance(section1, "portletInstance1", "portletInstance1", portlet);
        createPortletInstance(section2, "portletInstance2", "portletInstance2", portlet);
        
        List<?> data = portalService.getAllStructures();
        Assert.assertTrue( data.size() >= 7 );
        portalAction.getAllStructures4Tree(response);
        
        // 插一个Menu相关的
        menuAction.getStructuresByPortal(response, portalId, Structure.TYPE_SECTION);
        
        // 测试主题相关
        portalAction.getThemes4Tree(response, portalId);
        
        List<?> themeList = portalService.getThemesByPortal(portalId);
        assertEquals(1, themeList.size());
        Theme defaultTheme = (Theme) themeList.get(0);
        Long defaultThemeId = defaultTheme.getId();
        portalAction.saveThemeAs(response, defaultThemeId, "我的主题");
        
        themeList = portalService.getThemesByPortal(portalId);
        assertEquals(2, themeList.size());
        Theme newTheme = (Theme) themeList.get(1);
 
        portalAction.renameTheme(response, newTheme.getId(), "Jon的主题");
        portalAction.specifyDefaultTheme(response, defaultThemeId);
        try {
        	portalAction.removeTheme(response, newTheme.getId());
        } catch (Exception e) {
        	Assert.assertTrue("该主题为门户的默认主题或者当前主题，正在使用中，删除失败！", true);
        }
        
        portalAction.savePersonalTheme(response, portalId, defaultThemeId);
        
        // 测试门户发布
        portalAction.getActivePortals4Tree(response);
        
        portalAction.getActivePagesByPortal4Tree(response, portalId);
        
        portalAction.getThemesByPortal(response, portalId);

        portalAction.getReleaseConfig(response, BaseActionSupport.DEFAULT_NEW_ID);
        
        ReleaseConfig rconfig = new ReleaseConfig();
        rconfig.setName("门户发布配置");
        rconfig.setPortal(root);
        rconfig.setPage(page1);
        rconfig.setTheme(defaultTheme);
        rconfig.setVisitUrl("default.portal");
        rconfig.setRemark("~~~~~~~~~~~~~~~~");
        portalAction.saveReleaseConfig(response, rconfig); // create
        
        portalAction.getReleaseConfig(response, rconfig.getId());
        
        portalAction.saveReleaseConfig(response, rconfig); // update
        
        portalAction.getAllReleaseConfigs4Tree(response);
        
        // 测试排序
        portalAction.sort(response, page1.getId(), page2.getId(), 1);
        portalAction.getAllStructures4Tree(response);
        
        // 测试停用启用
        portalAction.disable(response, root.getId(), PortalConstants.TRUE);
        portalAction.disable(response, root.getId(), PortalConstants.FALSE);
        
        // 测试门户动态浏览、静态发布
        try {
        	request.addParameter("themeId", defaultThemeId + "");
            portalAction.previewPortal(response, request, portalId);
            
            request.addParameter("themeId", defaultThemeId + "");
            request.addParameter("pageId", page1.getId() + "");
            portalAction.previewPortal(response, request, portalId);
            
            request.addParameter("themeId", defaultThemeId + "");
            portalAction.getPortalXML(response, request, portalId, section1.getId(), page1.getId());
        } 
        catch (Exception e) {
            assertFalse(e.getMessage(), true);
        }
        
        // TODO 用jetty起一个web服务
        
        // 整站发布 【需要起web服务，通过http请求抓取页面内容】
        portalAction.staticReleasePortal(response, portalId, 1);
        
        // 单页发布
        portalAction.staticReleasePortal(response, rconfig.getId(), 2);
        
        // 测试门户缓存管理
        portalAction.cacheManage(response, portalId);
        portalAction.flushCache(response, portalId, defaultThemeId);
        
        // 测试门户流量查看
        portalAction.getFlowRate(response, portalId);
        
        // 删除新建的门户
        portalAction.removeReleaseConfig(response, rconfig.getId());
        portalAction.delete(response, root.getId());
        
        data = portalService.getActivePortals();
        assertFalse(data.contains(root));
        
        assertTrue(TestUtil.printLogs(logService) > 0);
    }
    
    Structure createPageOrSection(Structure parent, String name, String code, int type) {
        Structure newps = new Structure();
        newps.setName(name);
        newps.setCode(code);
        newps.setType(type);
        
        if(type == 1) {
            newps.setSupplement("<page><property><name>Jon的门户</name><description><![CDATA[]]></description></property>" +
            		"<script><file><![CDATA[1.js,2.js]]></file><code><![CDATA[]]></code></script>" +
            		"<style><file><![CDATA[1.css,2.css]]></file><code><![CDATA[]]></code></style></page>");
        }
        newps.setParameters("<params><layout>model/layout/***/paramsXForm.xml</layout><decorator>model/decorator/***/paramsXForm.xml</decorator></params>");
        
        newps.setPortalId(parent.getPortalId());
        newps.setParentId(parent.getId());
        newps.setDecorator(defaultDecorator);
        newps.setDefiner(defaultLayout);
        newps = portalService.createStructure(newps);
        
        return newps;
    }
    
    Structure createPortletInstance(Structure parent, String name, String code, Component portlet) {
        Structure newps = new Structure();
        newps.setName(name);
        newps.setCode(code);
        newps.setType(Structure.TYPE_PORTLET_INSTANCE);
 
        newps.setParameters("<params><portlet>model/portlet/***/paramsXForm.xml</portlet><decorator>model/decorator/***/paramsXForm.xml</decorator></params>");
        
        newps.setPortalId(parent.getPortalId());
        newps.setParentId(parent.getId());
        newps.setDecorator(defaultDecorator);
        newps.setDefiner(portlet);
        newps = portalService.createStructure(newps);
        
        return newps;
    }
    
    private Component createTestPortlet() {
        Component group = new Component();
        group.setName("测试Portlet组");
        group.setType(Component.PORTLET_TYPE);
        group.setParentId(PortalConstants.ROOT_ID);   
        group = componentService.saveComponent(group);
        
        String file = URLUtil.getResourceFileUrl("testdata/DemoPortlet.zip").getPath();
        super.importComponent(group.getId(), file);
        
        List<?> list = permissionHelper.getEntities("from Component o where o.type = ? and o.isGroup = ? order by o.decode", 
                Component.PORTLET_TYPE, false);
        
        assertTrue(list.size() >= 1);
        return (Component) list.get(list.size() - 1);
    }
}
