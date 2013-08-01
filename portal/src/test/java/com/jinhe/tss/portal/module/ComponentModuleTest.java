package com.jinhe.tss.portal.module;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.TxSupportTest4Portal;
import com.jinhe.tss.portal.action.ComponentAction;
import com.jinhe.tss.portal.entity.Component;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.URLUtil;

/**
 * Portlet相关模块的单元测试。
 */
public class ComponentModuleTest extends TxSupportTest4Portal {
    
    @Autowired ComponentAction componentAction;
 
    public void testElementGroupModule() {
        componentAction.getComponentParamsConfig(defaultLayoutId, "");
        
        try {
            componentAction.previewComponent(defaultLayoutId);
 
        } catch (Exception e) {
            assertFalse("预览组件出错" + e.getMessage(), true);
        }
 
        componentAction.getComponentGroup(defaultLayoutId, PortalConstants.ROOT_ID, Component.LAYOUT_TYPE);
        
        Component group1 = new Component();
        group1.setName("测试布局器组1");
        group1.setIsGroup(true);
        group1.setType(Component.LAYOUT_TYPE);
        group1.setParentId(defaultLayoutGroup.getId());   
        componentAction.save(group1);
        
        Long groupId = group1.getId();
        assertNotNull(groupId);
        
        Component group2 = new Component();
        group2.setName("测试布局器组2");
        group2.setIsGroup(true);
        group2.setType(Component.LAYOUT_TYPE);
        group2.setParentId(defaultLayoutGroup.getId());   
        componentAction.save(group2);
 
        componentAction.getComponentGroup(BaseActionSupport.DEFAULT_NEW_ID, PortalConstants.ROOT_ID, Component.LAYOUT_TYPE);
        
        componentAction.getGroupsByType(Component.LAYOUT_TYPE);
 
        componentAction.sort(Context.getResponse(), groupId, group2.getId(), -1);
        
        componentAction.copy(defaultLayoutId);
        
        componentAction.getGroupsByType(Component.LAYOUT_TYPE);
        
        componentAction.copyTo(defaultLayoutId, groupId);
        
        List<?> groups = componentService.getComponentGroups(Component.LAYOUT_TYPE);
        assertTrue(groups.size() >= 3);
 
        componentAction.moveTo(defaultLayoutId, ((Component)groups.get(2)).getId());
        
        componentAction.moveTo(defaultLayoutId, defaultLayoutGroup.getId());  // 移回去
        
        for(Object temp : groups) {
            Component group = (Component)temp;
            if( !defaultLayoutGroup.getId().equals(group.getId()) ){
                componentAction.delete(group.getId());
            }
        }
    }


    public void testDecoratorModule() {
    	
    	Component group1 = new Component();
        group1.setName("测试修饰器组1");
        group1.setIsGroup(true);
        group1.setType(Component.DECORATOR_TYPE);
        group1.setParentId(defaultDecoratorGroup.getId());   
        componentAction.save(group1);
        
        Long groupId = group1.getId();
        
        componentAction.getUploadTemplate();

		String file = URLUtil.getResourceFileUrl("testdata/DemoDecorator.zip").getPath();
		componentAction.importComponent(groupId, new File(file));

		List<?> list = componentService.getEnabledComponentsAndGroups(Component.DECORATOR_TYPE);
		assertTrue(list.size() >= 2);
		Component decorator1 = (Component) list.get(list.size() - 1);
		Long id = decorator1.getId();
		
		componentAction.exportComponent(id);
		componentAction.getComponentInfo(id, groupId);

		Component decorator2 = new Component();

		BeanUtil.copy(decorator2, decorator1, "id,code".split(","));
		decorator2.setName("copy_" + decorator1.getName());
		componentAction.save(decorator2);
		componentAction.save(decorator2); // update

		for (int i = 0; i < 5; i++) {
			componentAction.disabled(id, PortalConstants.TRUE);
			componentAction.disabled(id, PortalConstants.FALSE);
		}

		componentAction.setDecoratorAsDefault(id);
		componentAction.setDecoratorAsDefault(defaultDecoratorId);

		componentAction.sort(Context.getResponse(), id, id + 1, 1);

		componentAction.copy(id);

		componentAction.getEnabledComponents4Tree(Component.DECORATOR_TYPE);
		componentAction.getGroupsByType(Component.DECORATOR_TYPE);

		componentAction.getDefaultParams4Xml(id);

		componentAction.saveElementParamsConfig(id, "");

		componentAction.delete(id);

		componentAction.delete(groupId);

		assertTrue(TestUtil.printLogs(logService) > 0);
	}

//	public void testLayoutModule() {
//		Element group = new Element();
//		group.setName("测试布局器组");
//		group.setType(Element.LAYOUT_TYPE);
//		group.setParentId(PortalConstants.ROOT_ID);
//		group = elementService.saveGroup(group);
//
//		layoutAction.getUploadTemplate();
//
//		layoutAction.setGroupId(group.getId());
//		String file = URLUtil.getResourceFileUrl("testdata/DemoLayout.zip")
//				.getPath();
//		layoutAction.setFile(new File(file));
//		layoutAction.importLayout();
//
//		List<?> list = elementService.getLayouts();
//		assertTrue(list.size() >= 2);
//		Layout newLayout = (Layout) list.get(list.size() - 1);
//		Long layout1Id = newLayout.getId();
//
//		layoutAction.setLayoutId(layout1Id);
//		layoutAction.getExportLayout();
//
//		layoutAction.setLayoutId(layout1Id);
//		layoutAction.getLayoutInfo();
//
//		BeanUtil.copy(layoutAction.getLayout(), newLayout, new String[] { "id",
//				"code" });
//		layoutAction.getLayout().setName("copy_" + newLayout.getName());
//		layoutAction.save();
//		Long layout2Id = layoutAction.getLayout().getId();
//		assertNotNull(layout2Id);
//
//		layoutAction.save(); // update
//
//		for (int i = 0; i < 5; i++) {
//			layoutAction.setLayoutId(layout1Id);
//			layoutAction.setDisabled(PortalConstants.TRUE);
//			layoutAction.disabled();
//			layoutAction.setDisabled(PortalConstants.FALSE);
//			layoutAction.disabled();
//		}
//
//		layoutAction.setLayoutId(layout1Id);
//		layoutAction.setAsDefault();
//		layoutAction.setLayoutId(defaultLayoutId);
//		layoutAction.setAsDefault();
//
//		layoutAction.setLayoutId(layout1Id);
//		layoutAction.setTargetId(layout2Id);
//		layoutAction.setDirection(1);
//		layoutAction.sort();
//
//		layoutAction.setLayoutId(layout2Id);
//		layoutAction.copy();
//
//		layoutAction.getAllLayout4Tree();
//		layoutAction.getAllStartLayout4Tree();
//
//		layoutAction.setLayoutId(layout1Id);
//		layoutAction.getDefaultParams4Xml();
//
//		componentAction.setId(layout1Id);
//		componentAction.setConfigXML("");
//		componentAction.setType(Element.LAYOUT_TYPE);
//		componentAction.saveElementParamsConfig();
//
//		layoutAction.setLayoutId(layout2Id);
//		layoutAction.delete();
//
//		componentAction.setId(group.getId());
//		componentAction.delete();
//
//		assertTrue(TestUtil.printLogs(logService) > 0);
//	}
//
//	public void testPortletModule() {
//		Element group = new Element();
//		group.setName("测试Portlet组");
//		group.setType(Element.PORTLET_TYPE);
//		group.setParentId(PortalConstants.ROOT_ID);
//		group = elementService.saveGroup(group);
//
//		portletAction.getUploadTemplate();
//
//		portletAction.setGroupId(group.getId());
//		String file = URLUtil.getResourceFileUrl("testdata/DemoPortlet.zip")
//				.getPath();
//		portletAction.setFile(new File(file));
//		portletAction.importPortlet();
//
//		List<?> list = super.permissionHelper
//				.getEntities("from Portlet order by id");
//		assertTrue(list.size() >= 1);
//		Portlet newPortlet = (Portlet) list.get(list.size() - 1);
//		Long portlet1Id = newPortlet.getId();
//
//		portletAction.setId(portlet1Id);
//		portletAction.getExportPortlet();
//
//		portletAction.setId(portlet1Id);
//		portletAction.getPortletInfo();
//
//		BeanUtil.copy(portletAction.getPortlet(), newPortlet, new String[] {
//				"id", "code" });
//		portletAction.getPortlet().setName("copy_" + newPortlet.getName());
//		portletAction.save();
//		Long portlet2Id = portletAction.getPortlet().getId();
//		assertNotNull(portlet2Id);
//
//		portletAction.save(); // update
//
//		for (int i = 0; i < 5; i++) {
//			portletAction.setId(portlet1Id);
//			portletAction.setDisabled(PortalConstants.TRUE);
//			portletAction.disable();
//			portletAction.setDisabled(PortalConstants.FALSE);
//			portletAction.disable();
//		}
//
//		portletAction.setId(portlet1Id);
//		portletAction.setTargetId(portlet2Id);
//		portletAction.setDirection(1);
//		portletAction.sort();
//
//		portletAction.setId(portlet2Id);
//		portletAction.copy();
//
//		portletAction.getAllPortlet4Tree();
//		portletAction.getAllStartPortlet4Tree();
//
//		portletAction.setId(portlet1Id);
//		portletAction.getDefaultParams4Xml();
//
//		componentAction.setId(portlet1Id);
//		componentAction.setConfigXML("");
//		componentAction.setType(Element.PORTLET_TYPE);
//		componentAction.saveElementParamsConfig();
//
//		portletAction.setId(portlet2Id);
//		portletAction.delete();
//
//		componentAction.setId(group.getId());
//		componentAction.delete();
//
//		assertTrue(TestUtil.printLogs(logService) > 0);
//	}
}

