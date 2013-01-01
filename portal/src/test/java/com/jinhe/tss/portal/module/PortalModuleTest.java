package com.jinhe.tss.portal.module;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.TxSupportTest4Portal;
import com.jinhe.tss.portal.action.PortalAction;
import com.jinhe.tss.portal.action.PortletAction;
import com.jinhe.tss.portal.entity.ElementGroup;
import com.jinhe.tss.portal.entity.IssueInfo;
import com.jinhe.tss.portal.entity.PortalStructure;
import com.jinhe.tss.portal.entity.Portlet;
import com.jinhe.tss.portal.entity.Theme;
import com.jinhe.tss.portal.helper.PortalStructureWrapper;
import com.jinhe.tss.portal.service.IPortalService;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.URLUtil;

/**
 * 门户结构相关模块的单元测试。
 */
public class PortalModuleTest extends TxSupportTest4Portal {
    
    PortalAction portalAction;
    PortletAction portletAction;
    
    @Autowired IPortalService portalService;
	
    public void setUp() throws Exception {
        super.setUp();
        portalAction = new PortalAction();
        portalAction.setService(portalService);
        
        portletAction = new PortletAction();
        portletAction.setService(elementService);
    }
 
    public void testPortalModule() {
        // 新建portal
        portalAction.setType(PortalStructure.TYPE_PORTAL);
        portalAction.setParentId(PortalConstants.ROOT_ID);
        portalAction.setIsNew(PortalConstants.TRUE);
        portalAction.getPortalStructureInfo();
        
        PortalStructureWrapper psw = portalAction.getPs();
        psw.setParentId(PortalConstants.ROOT_ID);
        psw.setType(PortalStructure.TYPE_PORTAL);
        psw.setName("Jon的门户");
        psw.setSupplement("<page><property><name>Jon的门户</name><description><![CDATA[]]></description></property><script><file><![CDATA[]]></file><code><![CDATA[]]></code></script><style><file><![CDATA[]]></file><code><![CDATA[]]></code></style></page>");
        psw.setDescription("测试门户");
        psw.setThemeName("默认主题");
        portalAction.save(); // create portal root
        
        List<?> list = portalService.getAllPortalStructures();
        assertTrue(list.size() == 1);
        PortalStructure root = (PortalStructure) list.get(0);
        Long portalId = root.getPortalId();
        
        portalAction.setIsNew(PortalConstants.FALSE);
        portalAction.setId(root.getId());
        portalAction.getPortalStructureInfo();
        
        BeanUtil.copy(psw, root);
        psw.setId(root.getId());
        portalAction.setIsNew(PortalConstants.FALSE);
        portalAction.save(); // update portal root
        
        // 获取节点操作权限
        portalAction.setResourceId(root.getId());
        portalAction.getOperationsByResource();
        
        // 新建页面、版面
        PortalStructure page1 = createPageOrSection(root, "页面一", "page1", PortalStructure.TYPE_PAGE);
        PortalStructure page2 = createPageOrSection(root, "页面二", "page2", PortalStructure.TYPE_PAGE);
        PortalStructure section1 = createPageOrSection(page1, "版面一", "section1", PortalStructure.TYPE_SECTION);
        PortalStructure section2 = createPageOrSection(page2, "版面二", "section2", PortalStructure.TYPE_SECTION);
        Portlet portlet = createTestPortlet();
        createPortletInstance(section1, "portletInstance1", "portletInstance1", portlet);
        createPortletInstance(section2, "portletInstance2", "portletInstance2", portlet);
        
        List<?> data = portalService.getAllPortalStructures();
        assertEquals(7, data.size());
        portalAction.getAllPortals4Tree();
        
        // 测试主题相关
        portalAction.setId(root.getId());
        portalAction.getThemes4Tree();
        
        List<?> themeList = portalService.getThemesByPortal(portalId);
        assertEquals(1, themeList.size());
        Long defaultThemeId = ((Theme) themeList.get(0)).getId();
        portalAction.setThemeId( defaultThemeId );
        portalAction.setName("我的主题");
        portalAction.saveThemeAs();
        
        themeList = portalService.getThemesByPortal(portalId);
        assertEquals(2, themeList.size());
        Theme newTheme = (Theme) themeList.get(1);
        
        portalAction.setThemeId( newTheme.getId() );
        portalAction.setName("Jon的主题");
        portalAction.renameTheme();
        
        portalAction.setThemeId( defaultThemeId );
        portalAction.setPortalId(portalId);
        portalAction.specifyDefaultTheme();
        
        portalAction.setThemeId( newTheme.getId() );
        portalAction.setPortalId(portalId);
        portalAction.removeTheme();
        
        // 测试门户发布
        portalAction.getActivePortals4Tree();
        
        portalAction.setPortalId(portalId);
        portalAction.getActivePagesByPortal4Tree();
        
        portalAction.setPortalId(portalId);
        portalAction.getThemesByPortal();

        portalAction.setIsNew(PortalConstants.TRUE);
        portalAction.getIssueInfoById();
        
        IssueInfo issueInfo = portalAction.getIssueInfo();
        issueInfo.setName("门户发布配置");
        issueInfo.setPortalId(portalId);
        issueInfo.setPortalName(psw.getName());
        issueInfo.setPageId(page1.getId());
        issueInfo.setPageCode(page1.getCode());
        issueInfo.setPageName(page1.getName());
        issueInfo.setThemeId(defaultThemeId);
        issueInfo.setVisitUrl("default.portal");
        issueInfo.setRemark("~~~~~~~~~~~~~~~~");
        portalAction.saveIssue(); // create
        
        portalAction.setIsNew(PortalConstants.FALSE);
        portalAction.setId(issueInfo.getId());
        portalAction.getIssueInfoById();
        
        portalAction.saveIssue();// update
        
        portalAction.getAllIssues4Tree();
        portalAction.removeIssue();
        
        // 测试排序
        portalAction.setId(page1.getId());
        portalAction.setTargetId(page2.getId());
        portalAction.setDirection(1);
        portalAction.order();
        portalAction.getAllPortals4Tree();
        
        // 测试移动
        portalAction.setId(page1.getId());
        portalAction.getActivePortalStructures4Tree();
        
        portalAction.setId(section1.getId());
        portalAction.getActivePortalStructures4Tree();
        
        portalAction.setId(section2.getId());
        portalAction.setTargetId(page1.getId());
        portalAction.setPortalId(portalId);
        portalAction.move();
        portalAction.getAllPortals4Tree();
        
        // 测试门户结构复制
        portalAction.setId(section2.getId());
        portalAction.setTargetId(page2.getId());
        portalAction.setPortalId(portalId);
        portalAction.copyTo();
        portalAction.getAllPortals4Tree();
        
        // 测试门户复制
        portalAction.setId(root.getId());
        portalAction.copyPortal();
        portalAction.getAllPortals4Tree();

        portalAction.setId(root.getId());
        portalAction.setDisabled(PortalConstants.TRUE);
        portalAction.disable();
        
        portalAction.setDisabled(PortalConstants.FALSE);
        portalAction.disable();
        
        // 测试门户动态浏览、静态发布
        portalAction.setPortalId(portalId);
        portalAction.setThemeId(defaultThemeId);
        portalAction.setMethod("browse");
        try {
            portalAction.setId(null);
            portalAction.previewPortal();
            
            portalAction.setId(page1.getId());
            portalAction.previewPortal();
            
            portalAction.setId(section1.getId());
            portalAction.getPortalXML();
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        }
        
//        // 整站发布 【需要起web服务，通过http请求抓取页面内容】
//        portalAction.setType(1);
//        portalAction.setId(portalId);
//        portalAction.staticIssuePortal();
//        
//        // 单页发布
//        portalAction.setType(2);
//        portalAction.setId(issueInfo.getId());
//        portalAction.staticIssuePortal();
        
        // 测试门户缓存管理
        portalAction.setPortalId(portalId);
        portalAction.cacheManage();
        
        portalAction.setPortalId(portalId);
        portalAction.setThemeId(defaultThemeId);
        portalAction.flushCache();
        
        
        // 测试门户流量查看
        portalAction.setPortalId(portalId);
        portalAction.getFlowRate();
        
        // 删除新建的门户
        portalAction.setId(root.getId());
        portalAction.delete();
        
        // 删除复制出来的门户
        data = portalService.getActivePortals();
        assertEquals(1, data.size());
        portalAction.setId(((PortalStructure) data.get(0)).getId());
        portalAction.delete();
        
        assertTrue(TestUtil.printLogs(logService) > 0);
    }
    
    PortalStructure createPageOrSection(PortalStructure parent, String name, String code, int type) {
        PortalStructure newps = new PortalStructure();
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
        newps.setDecoratorId(defaultDecoratorId);
        newps.setDecoratorName(defaultDecorator.getName());
        newps.setDefinerId(defaultLayoutId);
        newps.setDefinerName(defaultLayout.getName());
        newps = portalService.createPortalStructure(new PortalStructureWrapper(newps, null));
        
        return newps;
    }
    
    PortalStructure createPortletInstance(PortalStructure parent, String name, String code, Portlet portlet) {
        PortalStructure newps = new PortalStructure();
        newps.setName(name);
        newps.setCode(code);
        newps.setType(PortalStructure.TYPE_PORTLET_INSTANCE);
 
        newps.setParameters("<params><portlet>model/portlet/***/paramsXForm.xml</portlet><decorator>model/decorator/***/paramsXForm.xml</decorator></params>");
        
        newps.setPortalId(parent.getPortalId());
        newps.setParentId(parent.getId());
        newps.setDecoratorId(defaultDecoratorId);
        newps.setDecoratorName(defaultDecorator.getName());
        newps.setDefinerId(portlet.getId());
        newps.setDefinerName(portlet.getName());
        newps = portalService.createPortalStructure(new PortalStructureWrapper(newps, null));
        
        return newps;
    }
    
    private Portlet createTestPortlet() {
        ElementGroup group = new ElementGroup();
        group.setName("测试Portlet组");
        group.setType(ElementGroup.PORTLET_TYPE);
        group.setParentId(PortalConstants.ROOT_ID);   
        group = elementService.saveGroup(group);
        
        portletAction.setGroupId(group.getId());
        String file = URLUtil.getResourceFileUrl("testdata/DemoPortlet.zip").getPath();
        portletAction.setFile(new File(file));
        portletAction.importPortlet();
        
        List<?> list = super.permissionHelper.getEntities("from Portlet order by id");
        assertTrue(list.size() >= 1);
        return (Portlet) list.get(list.size() - 1);
    }
}
