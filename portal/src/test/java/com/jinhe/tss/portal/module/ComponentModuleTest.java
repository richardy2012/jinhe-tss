package com.jinhe.tss.portal.module;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
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
 
    @Test
    public void testComponentGroupFunctions() {
    	request.addParameter("paramsItem", "");
        componentAction.getComponentParamsConfig(response, request, defaultLayoutId);
        
        try {
            componentAction.previewComponent(response, defaultLayoutId);
        } catch (Exception e) {
            assertFalse("预览组件出错" + e.getMessage(), true);
        }
 
        componentAction.getComponentInfo(response, defaultLayoutId, layoutGroup.getId());
        
        Component group1 = new Component();
        group1.setName("测试布局器组1");
        group1.setIsGroup(true);
        group1.setType(Component.LAYOUT_TYPE);
        group1.setParentId(layoutGroup.getId());   
        componentAction.save(response, group1);
        
        Long groupId = group1.getId();
        assertNotNull(groupId);
        
        Component group2 = new Component();
        group2.setName("测试布局器组2");
        group2.setIsGroup(true);
        group2.setType(Component.LAYOUT_TYPE);
        group2.setParentId(layoutGroup.getId());   
        componentAction.save(response, group2);
 
        request.addParameter("type", Component.LAYOUT_TYPE + "");
        componentAction.getComponentInfo(response, BaseActionSupport.DEFAULT_NEW_ID, group1.getId());
        
        componentAction.sort(Context.getResponse(), groupId, group2.getId(), -1);
        
        String hql = "from Component o where o.isGroup = 1 and o.type=? order by o.decode";
        List<?> groups = permissionHelper.getEntities(hql, Component.LAYOUT_TYPE);
        assertTrue(groups.size() >= 3);
 
        componentAction.getAllComponents4Tree(response);
        
        for(Object temp : groups) {
            Component group = (Component)temp;
            if( !layoutGroup.getId().equals(group.getId()) ){
                componentAction.delete(response, group.getId());
            }
        }
    }

    @Test
    public void testDecoratorFunctions() {
    	Component group1 = new Component();
        group1.setName("测试修饰器组1");
        group1.setIsGroup(true);
        group1.setType(Component.DECORATOR_TYPE);
        group1.setParentId(decoratorGroup.getId());   
        componentAction.save(response, group1);
        
        Long groupId = group1.getId();
        componentAction.getComponentInfo(response, BaseActionSupport.DEFAULT_NEW_ID, groupId);
        
		String file = URLUtil.getResourceFileUrl("testdata/DemoDecorator.zip").getPath();
		super.importComponent(groupId, file);

		List<?> list = componentService.getEnabledComponentsAndGroups(Component.DECORATOR_TYPE);
		assertTrue(list.size() >= 2);
		Component decorator1 = (Component) list.get(list.size() - 1);
		Long id = decorator1.getId();
		
		componentAction.exportComponent(response, id);
		componentAction.getComponentInfo(response, id, groupId);

		Component decorator2 = new Component();

		BeanUtil.copy(decorator2, decorator1, "id,code".split(","));
		decorator2.setName("copy_" + decorator1.getName());
		componentAction.save(response, decorator2);
		componentAction.save(response, decorator2); // update

		for (int i = 0; i < 5; i++) {
			componentAction.disable(response, id, PortalConstants.TRUE);
			componentAction.disable(response, id, PortalConstants.FALSE);
		}

		componentAction.sort(Context.getResponse(), id, id + 1, 1);

		componentAction.getEnabledComponents4Tree(response, Component.DECORATOR_TYPE);

		componentAction.getDefaultParams4Xml(response, id);
		
		request.addParameter("configXML", " bgColor=red \n menuId=12");
		componentAction.saveElementParamsConfig(response, request, id);

		componentAction.delete(response, id);

		componentAction.delete(response, groupId);

		assertTrue(TestUtil.printLogs(logService) > 0);
	}
    
    @Test
    public void testLayoutFunctions() {
        Component group1 = new Component();
        group1.setName("测试布局组1");
        group1.setIsGroup(true);
        group1.setType(Component.LAYOUT_TYPE);
        group1.setParentId(layoutGroup.getId());   
        componentAction.save(response, group1);
        
        Long groupId = group1.getId();
        
        String file = URLUtil.getResourceFileUrl("testdata/DemoLayout.zip").getPath();
        super.importComponent(groupId, file);
        
        List<?> list = componentService.getEnabledComponentsAndGroups(Component.LAYOUT_TYPE);
        assertTrue(list.size() >= 2);
        Component layout1 = (Component) list.get(list.size() - 1);
        Long id = layout1.getId();
        
        componentAction.getDefaultParams4Xml(response, id);
        
        request.addParameter("configXML", "");
		componentAction.saveElementParamsConfig(response, request, id);
    }

    @Test
    public void testPortletFunctions() {
        Component group1 = new Component();
        group1.setName("测试portlet组1");
        group1.setIsGroup(true);
        group1.setType(Component.PORTLET_TYPE);
        group1.setParentId(PortalConstants.ROOT_ID);   
        componentAction.save(response, group1);
        
        Long groupId = group1.getId();
        
        String file = URLUtil.getResourceFileUrl("testdata/DemoPortlet.zip").getPath();
        super.importComponent(groupId, file);
        
        List<?> list = componentService.getEnabledComponentsAndGroups(Component.PORTLET_TYPE);
        assertTrue(list.size() >= 2);
        Component portlet1 = (Component) list.get(list.size() - 1);
        Long id = portlet1.getId();
        
        request.addParameter("configXML", "");
		componentAction.saveElementParamsConfig(response, request, id);
    }

}

