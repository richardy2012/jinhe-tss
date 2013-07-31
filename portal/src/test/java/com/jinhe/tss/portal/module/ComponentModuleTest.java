package com.jinhe.tss.portal.module;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.TxSupportTest4Portal;
import com.jinhe.tss.portal.action.ElementAction;
import com.jinhe.tss.portal.entity.Component;

/**
 * Portlet相关模块的单元测试。
 */
public class ComponentModuleTest extends TxSupportTest4Portal {
    
    @Autowired ElementAction groupAction;
 
    public void testElementGroupModule() {
        groupAction.getElementParamsConfig(defaultLayoutId, "");
        
        try {
            groupAction.previewElement(defaultLayoutId);
 
        } catch (Exception e) {
            assertFalse("预览组件出错" + e.getMessage(), true);
        }
 
        groupAction.getGroupInfo(defaultLayoutId, PortalConstants.ROOT_ID, Component.LAYOUT_TYPE);
        
        Component elementGroup = new Component();
        elementGroup.setName("测试布局器组");
        elementGroup.setType(Component.LAYOUT_TYPE);
        elementGroup.setParentId(PortalConstants.ROOT_ID);   
        groupAction.save(elementGroup);
        
        Long groupId = elementGroup.getId();
        assertNotNull(groupId);
 
        groupAction.getGroupInfo(BaseActionSupport.DEFAULT_NEW_ID, PortalConstants.ROOT_ID, Component.LAYOUT_TYPE);
        
        groupAction.getGroupsByType(Component.LAYOUT_TYPE);
 
        groupAction.sort(Context.getResponse(), groupId, defaultLayoutGroup.getId(), -1);
        
        groupAction.copy(defaultLayoutId);
        
        groupAction.getGroupsByType(Component.LAYOUT_TYPE);
        
        groupAction.copyTo(defaultLayoutId, groupId);
        
        List<?> groups = elementService.getComponentGroups(Component.LAYOUT_TYPE);
        assertTrue(groups.size() >= 3);
 
        groupAction.moveTo(defaultLayoutId, ((Component)groups.get(2)).getId());
        
        groupAction.moveTo(defaultLayoutId, defaultLayoutGroup.getId());  // 移回去
        
        for(Object temp : groups) {
            Component group = (Component)temp;
            if( !defaultLayoutGroup.getId().equals(group.getId()) ){
                groupAction.delete(group.getId());
            }
        }
    }
}

///**
//* 修饰器相关模块的单元测试。
//*/
//public class DecoratorModuleTest extends TxSupportTest4Portal {
// 
// ElementGroupAction groupAction;
// DecoratorAction decoratorAction;
// 
// public void setUp() throws Exception {
//     super.setUp();
//     decoratorAction = new DecoratorAction();
//     decoratorAction.setService(elementService);
//     
//     groupAction = new ElementGroupAction();
//     groupAction.setService(elementService);
// }
//
// public void testDecoratorModule() {
//     Element group = new Element();
//     group.setName("测试修饰器组");
//     group.setType(Element.DECORATOR_TYPE);
//     group.setParentId(PortalConstants.ROOT_ID);   
//     group = elementService.saveGroup(group);
//     
//     decoratorAction.getUploadTemplate();
//     
//     decoratorAction.setGroupId(group.getId());
//     String file = URLUtil.getResourceFileUrl("testdata/DemoDecorator.zip").getPath();
//     decoratorAction.setFile(new File(file));
//     decoratorAction.importDecorator();
//     
//     List<?> list = elementService.getDecorators();
//     assertTrue(list.size() >= 2);
//     Decorator newDecorator = (Decorator) list.get(list.size() - 1);
//     Long decorator1Id = newDecorator.getId();
//     
//     decoratorAction.setDecoratorId(decorator1Id);
//     decoratorAction.getExportDecorator();
//     
//     decoratorAction.setDecoratorId(decorator1Id);
//     decoratorAction.getDecoratorInfo();
//     
//     BeanUtil.copy(decoratorAction.getDecorator(), newDecorator, new String[] {"id", "code"});
//     decoratorAction.getDecorator().setName("copy_" + newDecorator.getName());
//     decoratorAction.save();
//     Long decorator2Id = decoratorAction.getDecorator().getId();
//     assertNotNull(decorator2Id);
//     
//     decoratorAction.save(); // update
//     
//     for(int i = 0; i < 5; i++) {
//         decoratorAction.setDecoratorId(decorator1Id);
//         decoratorAction.setDisabled(PortalConstants.TRUE);
//         decoratorAction.disabled();
//         decoratorAction.setDisabled(PortalConstants.FALSE);
//         decoratorAction.disabled();
//     }
//   
//     decoratorAction.setDecoratorId(decorator1Id);
//     decoratorAction.setAsDefault();
//     decoratorAction.setDecoratorId(defaultDecoratorId);
//     decoratorAction.setAsDefault();
//
//     decoratorAction.setDecoratorId(decorator1Id);
//     decoratorAction.setTargetId(decorator2Id);
//     decoratorAction.setDirection(1);
//     decoratorAction.sort();
//     
//     decoratorAction.setDecoratorId(decorator2Id);
//     decoratorAction.copy();
//     
//     decoratorAction.getAllDecorator4Tree();
//     decoratorAction.getAllStartDecorator4Tree();
//     
//     decoratorAction.setDecoratorId(decorator1Id);
//     decoratorAction.getDefaultParams4Xml();
//     
//     groupAction.setId(decorator1Id);
//     groupAction.setConfigXML("");
//     groupAction.setType(Element.DECORATOR_TYPE);
//     groupAction.saveElementParamsConfig();
//     
//     decoratorAction.setDecoratorId(decorator2Id);
//     decoratorAction.delete();
//     
//     groupAction.setId(group.getId());
//     groupAction.delete();
//     
//     assertTrue(TestUtil.printLogs(logService) > 0);
// }
// 
//}

///**
//* 布局器相关模块的单元测试。
//*/
//public class LayoutModuleTest extends TxSupportTest4Portal {
// 
// ElementGroupAction groupAction;
// LayoutAction layoutAction;
//
// public void setUp() throws Exception {
//     super.setUp();
//     layoutAction = new LayoutAction();
//     layoutAction.setService(elementService);
//     
//     groupAction = new ElementGroupAction();
//     groupAction.setService(elementService);
// }
//
// public void testLayoutModule() {
//     Element group = new Element();
//     group.setName("测试布局器组");
//     group.setType(Element.LAYOUT_TYPE);
//     group.setParentId(PortalConstants.ROOT_ID);   
//     group = elementService.saveGroup(group);
//     
//     layoutAction.getUploadTemplate();
//     
//     layoutAction.setGroupId(group.getId());
//     String file = URLUtil.getResourceFileUrl("testdata/DemoLayout.zip").getPath();
//     layoutAction.setFile(new File(file));
//     layoutAction.importLayout();
//     
//     List<?> list = elementService.getLayouts();
//     assertTrue(list.size() >= 2);
//     Layout newLayout = (Layout) list.get(list.size() - 1);
//     Long layout1Id = newLayout.getId();
//     
//     layoutAction.setLayoutId(layout1Id);
//     layoutAction.getExportLayout();
//     
//     layoutAction.setLayoutId(layout1Id);
//     layoutAction.getLayoutInfo();
//     
//     BeanUtil.copy(layoutAction.getLayout(), newLayout, new String[] {"id", "code"});
//     layoutAction.getLayout().setName("copy_" + newLayout.getName());
//     layoutAction.save();
//     Long layout2Id = layoutAction.getLayout().getId();
//     assertNotNull(layout2Id);
//     
//     layoutAction.save(); // update
//     
//     for(int i = 0; i < 5; i++) {
//         layoutAction.setLayoutId(layout1Id);
//         layoutAction.setDisabled(PortalConstants.TRUE);
//         layoutAction.disabled();
//         layoutAction.setDisabled(PortalConstants.FALSE);
//         layoutAction.disabled();
//     }
//   
//     layoutAction.setLayoutId(layout1Id);
//     layoutAction.setAsDefault();
//     layoutAction.setLayoutId(defaultLayoutId);
//     layoutAction.setAsDefault();
//
//     layoutAction.setLayoutId(layout1Id);
//     layoutAction.setTargetId(layout2Id);
//     layoutAction.setDirection(1);
//     layoutAction.sort();
//     
//     layoutAction.setLayoutId(layout2Id);
//     layoutAction.copy();
//     
//     layoutAction.getAllLayout4Tree();
//     layoutAction.getAllStartLayout4Tree();
//     
//     layoutAction.setLayoutId(layout1Id);
//     layoutAction.getDefaultParams4Xml();
//     
//     groupAction.setId(layout1Id);
//     groupAction.setConfigXML("");
//     groupAction.setType(Element.LAYOUT_TYPE);
//     groupAction.saveElementParamsConfig();
//     
//     layoutAction.setLayoutId(layout2Id);
//     layoutAction.delete();
//     
//     groupAction.setId(group.getId());
//     groupAction.delete();
//     
//     assertTrue(TestUtil.printLogs(logService) > 0);
// }
// 
//}

///**
//* Portlet相关模块的单元测试。
//*/
//public class PortletModuleTest extends TxSupportTest4Portal {
// 
// ElementGroupAction groupAction;
// PortletAction portletAction;
// 
// public void setUp() throws Exception {
//     super.setUp();
//     portletAction = new PortletAction();
//     portletAction.setService(elementService);
//     
//     groupAction = new ElementGroupAction();
//     groupAction.setService(elementService);
// }
//
// public void testPortletModule() {
//     Element group = new Element();
//     group.setName("测试Portlet组");
//     group.setType(Element.PORTLET_TYPE);
//     group.setParentId(PortalConstants.ROOT_ID);   
//     group = elementService.saveGroup(group);
//     
//     portletAction.getUploadTemplate();
//     
//     portletAction.setGroupId(group.getId());
//     String file = URLUtil.getResourceFileUrl("testdata/DemoPortlet.zip").getPath();
//     portletAction.setFile(new File(file));
//     portletAction.importPortlet();
//     
//     List<?> list = super.permissionHelper.getEntities("from Portlet order by id");
//     assertTrue(list.size() >= 1);
//     Portlet newPortlet = (Portlet) list.get(list.size() - 1);
//     Long portlet1Id = newPortlet.getId();
//     
//     portletAction.setId(portlet1Id);
//     portletAction.getExportPortlet();
//     
//     portletAction.setId(portlet1Id);
//     portletAction.getPortletInfo();
//     
//     BeanUtil.copy(portletAction.getPortlet(), newPortlet, new String[] {"id", "code"});
//     portletAction.getPortlet().setName("copy_" + newPortlet.getName());
//     portletAction.save();
//     Long portlet2Id = portletAction.getPortlet().getId();
//     assertNotNull(portlet2Id);
//     
//     portletAction.save(); // update
//     
//     for(int i = 0; i < 5; i++) {
//         portletAction.setId(portlet1Id);
//         portletAction.setDisabled(PortalConstants.TRUE);
//         portletAction.disable();
//         portletAction.setDisabled(PortalConstants.FALSE);
//         portletAction.disable();
//     }
//
//     portletAction.setId(portlet1Id);
//     portletAction.setTargetId(portlet2Id);
//     portletAction.setDirection(1);
//     portletAction.sort();
//     
//     portletAction.setId(portlet2Id);
//     portletAction.copy();
//     
//     portletAction.getAllPortlet4Tree();
//     portletAction.getAllStartPortlet4Tree();
//     
//     portletAction.setId(portlet1Id);
//     portletAction.getDefaultParams4Xml();
//     
//     groupAction.setId(portlet1Id);
//     groupAction.setConfigXML("");
//     groupAction.setType(Element.PORTLET_TYPE);
//     groupAction.saveElementParamsConfig();
//     
//     portletAction.setId(portlet2Id);
//     portletAction.delete();
//     
//     groupAction.setId(group.getId());
//     groupAction.delete();
//     
//     assertTrue(TestUtil.printLogs(logService) > 0);
// }
//}

