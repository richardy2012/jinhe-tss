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
 
    public void testComponentGroupFunctions() {
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

    public void testDecoratorFunctions() {
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
    
    public void testLayoutFunctions() {
        Component group1 = new Component();
        group1.setName("测试布局组1");
        group1.setIsGroup(true);
        group1.setType(Component.LAYOUT_TYPE);
        group1.setParentId(defaultLayoutGroup.getId());   
        componentAction.save(group1);
        
        Long groupId = group1.getId();
        
        String file = URLUtil.getResourceFileUrl("testdata/DemoLayout.zip").getPath();
        componentAction.importComponent(groupId, new File(file));
        
        List<?> list = componentService.getEnabledComponentsAndGroups(Component.LAYOUT_TYPE);
        assertTrue(list.size() >= 2);
        Component layout1 = (Component) list.get(list.size() - 1);
        Long id = layout1.getId();
        
        componentAction.getDefaultParams4Xml(id);
        componentAction.saveElementParamsConfig(id, "");
    }

    public void testPortletFunctions() {
        Component group1 = new Component();
        group1.setName("测试portlet组1");
        group1.setIsGroup(true);
        group1.setType(Component.PORTLET_TYPE);
        group1.setParentId(PortalConstants.ROOT_ID);   
        componentAction.save(group1);
        
        Long groupId = group1.getId();
        
        String file = URLUtil.getResourceFileUrl("testdata/DemoPortlet.zip").getPath();
        componentAction.importComponent(groupId, new File(file));
        
        List<?> list = componentService.getEnabledComponentsAndGroups(Component.PORTLET_TYPE);
        assertTrue(list.size() >= 2);
        Component portlet1 = (Component) list.get(list.size() - 1);
        Long id = portlet1.getId();
        
        componentAction.getDefaultParams4Xml(id);
        componentAction.saveElementParamsConfig(id, "");
    }

}

