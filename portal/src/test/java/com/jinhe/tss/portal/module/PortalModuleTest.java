package com.jinhe.tss.portal.module;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.TxSupportTest4Portal;
import com.jinhe.tss.portal.action.ComponentAction;
import com.jinhe.tss.portal.action.PortalAction;
import com.jinhe.tss.portal.entity.Component;
import com.jinhe.tss.portal.entity.IssueInfo;
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
    
    @Autowired IPortalService portalService;
 
    public void testPortalModule() {
        Long structureId = BaseActionSupport.DEFAULT_NEW_ID;
        Long parentId = PortalConstants.ROOT_ID;
        
        // 新建portal
        portalAction.getPortalStructureInfo(structureId, parentId, Structure.TYPE_PORTAL);
        
        Theme theme = new Theme();
        theme.setName("默认主题");
        
        Structure root = new Structure();
        root.setParentId(parentId);
        root.setType(Structure.TYPE_PORTAL);
        root.setName("Jon的门户");
        root.setSupplement("<page><property><name>Jon的门户</name><description><![CDATA[]]></description></property><script><file><![CDATA[]]></file><code><![CDATA[]]></code></script><style><file><![CDATA[]]></file><code><![CDATA[]]></code></style></page>");
        root.setDescription("测试门户");
        root.setTheme(theme);
        portalAction.save(root, "TempPortalCode"); // create portal root
        
        List<?> list = portalService.getAllStructures();
        assertTrue(list.size() == 1);
        root = (Structure) list.get(0);
        Long portalId = root.getPortalId();
        
        structureId = root.getId();
        portalAction.getPortalStructureInfo(structureId, parentId, Structure.TYPE_PORTAL);
  
        portalAction.save(root, root.getCode()); // update portal root
        
        // 获取节点操作权限
        portalAction.getOperationsByResource(structureId);
        
        // 新建页面、版面
        Structure page1 = createPageOrSection(root, "页面一", "page1", Structure.TYPE_PAGE);
        Structure page2 = createPageOrSection(root, "页面二", "page2", Structure.TYPE_PAGE);
        Structure section1 = createPageOrSection(page1, "版面一", "section1", Structure.TYPE_SECTION);
        Structure section2 = createPageOrSection(page2, "版面二", "section2", Structure.TYPE_SECTION);
        Component portlet = createTestPortlet();
        createPortletInstance(section1, "portletInstance1", "portletInstance1", portlet);
        createPortletInstance(section2, "portletInstance2", "portletInstance2", portlet);
        
        List<?> data = portalService.getAllStructures();
        assertEquals(7, data.size());
        portalAction.getAllPortals4Tree();
        
        // 测试主题相关
        portalAction.getThemes4Tree(portalId);
        
        List<?> themeList = portalService.getThemesByPortal(portalId);
        assertEquals(1, themeList.size());
        Theme defaultTheme = (Theme) themeList.get(0);
        Long defaultThemeId = defaultTheme.getId();
        portalAction.saveThemeAs(defaultThemeId, "我的主题");
        
        themeList = portalService.getThemesByPortal(portalId);
        assertEquals(2, themeList.size());
        Theme newTheme = (Theme) themeList.get(1);
 
        portalAction.renameTheme(newTheme.getId(), "Jon的主题");
        portalAction.specifyDefaultTheme(portalId, defaultThemeId);
        portalAction.removeTheme(portalId, newTheme.getId());
        
        // 测试门户发布
        portalAction.getActivePortals4Tree();
        
        portalAction.getActivePagesByPortal4Tree(portalId);
        
        portalAction.getThemesByPortal(portalId);

        portalAction.getIssueInfoById(BaseActionSupport.DEFAULT_NEW_ID);
        
        IssueInfo issueInfo = new IssueInfo();
        issueInfo.setName("门户发布配置");
        issueInfo.setPortal(root);
        issueInfo.setPage(page1);
        issueInfo.setTheme(defaultTheme);
        issueInfo.setVisitUrl("default.portal");
        issueInfo.setRemark("~~~~~~~~~~~~~~~~");
        portalAction.saveIssue(issueInfo); // create
        
        portalAction.getIssueInfoById(issueInfo.getId());
        
        portalAction.saveIssue(issueInfo); // update
        
        portalAction.getAllIssues4Tree();
        portalAction.removeIssue(issueInfo.getId());
        
        // 测试排序
        portalAction.sort(page1.getId(), page2.getId(), 1);
        portalAction.getAllPortals4Tree();
        
        // 测试移动
        portalAction.getActivePortalStructures4Tree(page1.getId());
        portalAction.getActivePortalStructures4Tree(section1.getId());
        portalAction.move(section2.getId(), page1.getId());
        portalAction.getAllPortals4Tree();
        
        // 测试门户结构复制
        portalAction.copyTo(section2.getId(), page2.getId());
        portalAction.getAllPortals4Tree();
        
        // 测试停用启用
        portalAction.disable(root.getId(), PortalConstants.TRUE);
        portalAction.disable(root.getId(), PortalConstants.FALSE);
        
        // 测试门户动态浏览、静态发布
        try {
            portalAction.previewPortal(portalId, defaultThemeId, "browse", null);
            portalAction.previewPortal(portalId, defaultThemeId, "browse", page1.getId());
            
            portalAction.getPortalXML(portalId, defaultThemeId, "browse", section1.getId(), page1.getId());
        } 
        catch (Exception e) {
            assertFalse(e.getMessage(), true);
        }
        
        // 整站发布 【需要起web服务，通过http请求抓取页面内容】
        portalAction.staticIssuePortal(1, portalId);
        
        // 单页发布
        portalAction.staticIssuePortal(2, issueInfo.getId());
        
        // 测试门户缓存管理
        portalAction.cacheManage(portalId);
        portalAction.flushCache(portalId, defaultThemeId);
        
        
        // 测试门户流量查看
        portalAction.getFlowRate(portalId);
        
        // 删除新建的门户
        portalAction.delete(root.getId());
        
        // 删除复制出来的门户
        data = portalService.getActivePortals();
        assertTrue(data.size() == 0);
        
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
        elementAction.importComponent(group.getId(), new File(file));
        
        List<?> list = permissionHelper.getEntities("from Component o where o.type = ? and o.isGroup = ? order by o.decode", 
                Component.PORTLET_TYPE, false);
        
        assertTrue(list.size() >= 1);
        return (Component) list.get(list.size() - 1);
    }
}
