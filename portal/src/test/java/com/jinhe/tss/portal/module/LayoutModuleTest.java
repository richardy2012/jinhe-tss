//package com.jinhe.tss.portal.module;
//
//import java.io.File;
//import java.util.List;
//
//import com.jinhe.tss.framework.test.TestUtil;
//import com.jinhe.tss.portal.PortalConstants;
//import com.jinhe.tss.portal.TxSupportTest4Portal;
//import com.jinhe.tss.portal.action.ElementGroupAction;
//import com.jinhe.tss.portal.action.LayoutAction;
//import com.jinhe.tss.portal.entity.ElementGroup;
//import com.jinhe.tss.portal.entity.Layout;
//import com.jinhe.tss.util.BeanUtil;
//import com.jinhe.tss.util.URLUtil;
//
///**
// * 布局器相关模块的单元测试。
// */
//public class LayoutModuleTest extends TxSupportTest4Portal {
//    
//    ElementGroupAction groupAction;
//    LayoutAction layoutAction;
//	
//    public void setUp() throws Exception {
//        super.setUp();
//        layoutAction = new LayoutAction();
//        layoutAction.setService(elementService);
//        
//        groupAction = new ElementGroupAction();
//        groupAction.setService(elementService);
//    }
// 
//    public void testLayoutModule() {
//        ElementGroup group = new ElementGroup();
//        group.setName("测试布局器组");
//        group.setType(ElementGroup.LAYOUT_TYPE);
//        group.setParentId(PortalConstants.ROOT_ID);   
//        group = elementService.saveGroup(group);
//        
//        layoutAction.getUploadTemplate();
//        
//        layoutAction.setGroupId(group.getId());
//        String file = URLUtil.getResourceFileUrl("testdata/DemoLayout.zip").getPath();
//        layoutAction.setFile(new File(file));
//        layoutAction.importLayout();
//        
//        List<?> list = elementService.getLayouts();
//        assertTrue(list.size() >= 2);
//        Layout newLayout = (Layout) list.get(list.size() - 1);
//        Long layout1Id = newLayout.getId();
//        
//        layoutAction.setLayoutId(layout1Id);
//        layoutAction.getExportLayout();
//        
//        layoutAction.setLayoutId(layout1Id);
//        layoutAction.getLayoutInfo();
//        
//        BeanUtil.copy(layoutAction.getLayout(), newLayout, new String[] {"id", "code"});
//        layoutAction.getLayout().setName("copy_" + newLayout.getName());
//        layoutAction.save();
//        Long layout2Id = layoutAction.getLayout().getId();
//        assertNotNull(layout2Id);
//        
//        layoutAction.save(); // update
//        
//        for(int i = 0; i < 5; i++) {
//            layoutAction.setLayoutId(layout1Id);
//            layoutAction.setDisabled(PortalConstants.TRUE);
//            layoutAction.disabled();
//            layoutAction.setDisabled(PortalConstants.FALSE);
//            layoutAction.disabled();
//        }
//      
//        layoutAction.setLayoutId(layout1Id);
//        layoutAction.setAsDefault();
//        layoutAction.setLayoutId(defaultLayoutId);
//        layoutAction.setAsDefault();
//
//        layoutAction.setLayoutId(layout1Id);
//        layoutAction.setTargetId(layout2Id);
//        layoutAction.setDirection(1);
//        layoutAction.sort();
//        
//        layoutAction.setLayoutId(layout2Id);
//        layoutAction.copy();
//        
//        layoutAction.getAllLayout4Tree();
//        layoutAction.getAllStartLayout4Tree();
//        
//        layoutAction.setLayoutId(layout1Id);
//        layoutAction.getDefaultParams4Xml();
//        
//        groupAction.setId(layout1Id);
//        groupAction.setConfigXML("");
//        groupAction.setType(ElementGroup.LAYOUT_TYPE);
//        groupAction.saveElementParamsConfig();
//        
//        layoutAction.setLayoutId(layout2Id);
//        layoutAction.delete();
//        
//        groupAction.setId(group.getId());
//        groupAction.delete();
//        
//        assertTrue(TestUtil.printLogs(logService) > 0);
//    }
//    
//}
