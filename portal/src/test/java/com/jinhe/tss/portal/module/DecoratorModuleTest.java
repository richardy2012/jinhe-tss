//package com.jinhe.tss.portal.module;
//
//import java.io.File;
//import java.util.List;
//
//import com.jinhe.tss.framework.test.TestUtil;
//import com.jinhe.tss.portal.PortalConstants;
//import com.jinhe.tss.portal.TxSupportTest4Portal;
//import com.jinhe.tss.portal.action.DecoratorAction;
//import com.jinhe.tss.portal.action.ElementGroupAction;
//import com.jinhe.tss.portal.entity.Decorator;
//import com.jinhe.tss.portal.entity.ElementGroup;
//import com.jinhe.tss.util.BeanUtil;
//import com.jinhe.tss.util.URLUtil;
//
///**
// * 修饰器相关模块的单元测试。
// */
//public class DecoratorModuleTest extends TxSupportTest4Portal {
//    
//    ElementGroupAction groupAction;
//    DecoratorAction decoratorAction;
//    
//    public void setUp() throws Exception {
//        super.setUp();
//        decoratorAction = new DecoratorAction();
//        decoratorAction.setService(elementService);
//        
//        groupAction = new ElementGroupAction();
//        groupAction.setService(elementService);
//    }
// 
//    public void testDecoratorModule() {
//        ElementGroup group = new ElementGroup();
//        group.setName("测试修饰器组");
//        group.setType(ElementGroup.DECORATOR_TYPE);
//        group.setParentId(PortalConstants.ROOT_ID);   
//        group = elementService.saveGroup(group);
//        
//        decoratorAction.getUploadTemplate();
//        
//        decoratorAction.setGroupId(group.getId());
//        String file = URLUtil.getResourceFileUrl("testdata/DemoDecorator.zip").getPath();
//        decoratorAction.setFile(new File(file));
//        decoratorAction.importDecorator();
//        
//        List<?> list = elementService.getDecorators();
//        assertTrue(list.size() >= 2);
//        Decorator newDecorator = (Decorator) list.get(list.size() - 1);
//        Long decorator1Id = newDecorator.getId();
//        
//        decoratorAction.setDecoratorId(decorator1Id);
//        decoratorAction.getExportDecorator();
//        
//        decoratorAction.setDecoratorId(decorator1Id);
//        decoratorAction.getDecoratorInfo();
//        
//        BeanUtil.copy(decoratorAction.getDecorator(), newDecorator, new String[] {"id", "code"});
//        decoratorAction.getDecorator().setName("copy_" + newDecorator.getName());
//        decoratorAction.save();
//        Long decorator2Id = decoratorAction.getDecorator().getId();
//        assertNotNull(decorator2Id);
//        
//        decoratorAction.save(); // update
//        
//        for(int i = 0; i < 5; i++) {
//            decoratorAction.setDecoratorId(decorator1Id);
//            decoratorAction.setDisabled(PortalConstants.TRUE);
//            decoratorAction.disabled();
//            decoratorAction.setDisabled(PortalConstants.FALSE);
//            decoratorAction.disabled();
//        }
//      
//        decoratorAction.setDecoratorId(decorator1Id);
//        decoratorAction.setAsDefault();
//        decoratorAction.setDecoratorId(defaultDecoratorId);
//        decoratorAction.setAsDefault();
//
//        decoratorAction.setDecoratorId(decorator1Id);
//        decoratorAction.setTargetId(decorator2Id);
//        decoratorAction.setDirection(1);
//        decoratorAction.sort();
//        
//        decoratorAction.setDecoratorId(decorator2Id);
//        decoratorAction.copy();
//        
//        decoratorAction.getAllDecorator4Tree();
//        decoratorAction.getAllStartDecorator4Tree();
//        
//        decoratorAction.setDecoratorId(decorator1Id);
//        decoratorAction.getDefaultParams4Xml();
//        
//        groupAction.setId(decorator1Id);
//        groupAction.setConfigXML("");
//        groupAction.setType(ElementGroup.DECORATOR_TYPE);
//        groupAction.saveElementParamsConfig();
//        
//        decoratorAction.setDecoratorId(decorator2Id);
//        decoratorAction.delete();
//        
//        groupAction.setId(group.getId());
//        groupAction.delete();
//        
//        assertTrue(TestUtil.printLogs(logService) > 0);
//    }
//    
//}
