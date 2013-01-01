package com.jinhe.tss.portal.module;

import java.io.File;
import java.util.List;

import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.TxSupportTest4Portal;
import com.jinhe.tss.portal.action.GroupAction;
import com.jinhe.tss.portal.action.PortletAction;
import com.jinhe.tss.portal.entity.ElementGroup;
import com.jinhe.tss.portal.entity.Portlet;
import com.jinhe.tss.util.BeanUtil;
import com.jinhe.tss.util.URLUtil;

/**
 * Portlet相关模块的单元测试。
 */
public class PortletModuleTest extends TxSupportTest4Portal {
    
    GroupAction groupAction;
    PortletAction portletAction;
    
    public void setUp() throws Exception {
        super.setUp();
        portletAction = new PortletAction();
        portletAction.setService(elementService);
        
        groupAction = new GroupAction();
        groupAction.setService(elementService);
    }
 
    public void testPortletModule() {
        ElementGroup group = new ElementGroup();
        group.setName("测试Portlet组");
        group.setType(ElementGroup.PORTLET_TYPE);
        group.setParentId(PortalConstants.ROOT_ID);   
        group = elementService.saveGroup(group);
        
        portletAction.getUploadTemplate();
        
        portletAction.setGroupId(group.getId());
        String file = URLUtil.getResourceFileUrl("testdata/DemoPortlet.zip").getPath();
        portletAction.setFile(new File(file));
        portletAction.importPortlet();
        
        List<?> list = super.permissionHelper.getEntities("from Portlet order by id");
        assertTrue(list.size() >= 1);
        Portlet newPortlet = (Portlet) list.get(list.size() - 1);
        Long portlet1Id = newPortlet.getId();
        
        portletAction.setId(portlet1Id);
        portletAction.getExportPortlet();
        
        portletAction.setId(portlet1Id);
        portletAction.getPortletInfo();
        
        BeanUtil.copy(portletAction.getPortlet(), newPortlet, new String[] {"id", "code"});
        portletAction.getPortlet().setName("copy_" + newPortlet.getName());
        portletAction.save();
        Long portlet2Id = portletAction.getPortlet().getId();
        assertNotNull(portlet2Id);
        
        portletAction.save(); // update
        
        for(int i = 0; i < 5; i++) {
            portletAction.setId(portlet1Id);
            portletAction.setDisabled(PortalConstants.TRUE);
            portletAction.disable();
            portletAction.setDisabled(PortalConstants.FALSE);
            portletAction.disable();
        }

        portletAction.setId(portlet1Id);
        portletAction.setTargetId(portlet2Id);
        portletAction.setDirection(1);
        portletAction.sort();
        
        portletAction.setId(portlet2Id);
        portletAction.copy();
        
        portletAction.getAllPortlet4Tree();
        portletAction.getAllStartPortlet4Tree();
        
        portletAction.setId(portlet1Id);
        portletAction.getDefaultParams4Xml();
        
        groupAction.setId(portlet1Id);
        groupAction.setConfigXML("");
        groupAction.setType(ElementGroup.PORTLET_TYPE);
        groupAction.saveElementParamsConfig();
        
        portletAction.setId(portlet2Id);
        portletAction.delete();
        
        groupAction.setId(group.getId());
        groupAction.delete();
        
        assertTrue(TestUtil.printLogs(logService) > 0);
    }
}
