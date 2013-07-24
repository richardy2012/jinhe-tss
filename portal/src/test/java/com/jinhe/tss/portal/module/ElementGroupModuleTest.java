//package com.jinhe.tss.portal.module;
//
//import java.io.IOException;
//import java.util.List;
//
//import com.jinhe.tss.portal.PortalConstants;
//import com.jinhe.tss.portal.TxSupportTest4Portal;
//import com.jinhe.tss.portal.action.ElementGroupAction;
//import com.jinhe.tss.portal.entity.ElementGroup;
//
//import freemarker.template.TemplateException;
//
///**
// * Portlet相关模块的单元测试。
// */
//public class ElementGroupModuleTest extends TxSupportTest4Portal {
//    
//    ElementGroupAction groupAction;
//	
//    public void setUp() throws Exception {
//        super.setUp();
//        groupAction = new ElementGroupAction();
//        groupAction.setService(elementService);
//    }
// 
//    public void testElementGroupModule() {
//        groupAction.setType(ElementGroup.LAYOUT_TYPE);
//        groupAction.setId(defaultLayoutId);
//        groupAction.getElementParamsConfig();
//        
//        try {
//            groupAction.setType(ElementGroup.LAYOUT_TYPE);
//            groupAction.setId(defaultLayoutId);
//            groupAction.previewElement();
//            
//            groupAction.setDataType("XML");
//            groupAction.previewElement();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (TemplateException e) {
//            e.printStackTrace();
//        }
//        
//        groupAction.setParentId(PortalConstants.ROOT_ID);
//        groupAction.setType(ElementGroup.LAYOUT_TYPE);
//        groupAction.setIsNew(PortalConstants.TRUE);
//        groupAction.getGroupInfo();
//        
//        ElementGroup elementGroup = groupAction.getGroup();
//        elementGroup.setName("测试布局器组");
//        elementGroup.setType(ElementGroup.LAYOUT_TYPE);
//        elementGroup.setParentId(PortalConstants.ROOT_ID);   
//        groupAction.save();
//        
//        Long groupId = elementGroup.getId();
//        assertNotNull(groupId);
//        
//        groupAction.setIsNew(PortalConstants.FALSE);
//        groupAction.setId(groupId);
//        groupAction.getGroupInfo();
//        
//        elementGroup.setType(ElementGroup.LAYOUT_TYPE);
//        groupAction.getGroupsByType();
//        
//        groupAction.setId(groupId);
//        groupAction.setTargetId(defaultLayoutGroup.getId());
//        groupAction.setDirection(-1);
//        groupAction.sortByType();
//        
//        groupAction.setType(ElementGroup.LAYOUT_TYPE);
//        groupAction.setId(groupId);
//        groupAction.copyByType();
//        
//        groupAction.getGroupsByType();
//        
//        groupAction.setId(defaultLayoutId);
//        groupAction.setTargetId(groupId);
//        groupAction.copyTo();
//        
//        List<?> groups = elementService.getGroupsByType(ElementGroup.LAYOUT_TYPE);
//        assertTrue(groups.size() >= 3);
//        
//        groupAction.setId(defaultLayoutId);
//        groupAction.setTargetId(((ElementGroup)groups.get(2)).getId());
//        groupAction.moveTo();
//        
//        groupAction.setTargetId(defaultLayoutGroup.getId()); // 移回去
//        groupAction.moveTo();
//        
//        for(Object temp : groups) {
//            ElementGroup group = (ElementGroup)temp;
//            if( !defaultLayoutGroup.getId().equals(group.getId()) ){
//                groupAction.setId(group.getId());
//                groupAction.delete();
//            }
//        }
//    }
//}
