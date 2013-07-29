package com.jinhe.tss.portal;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractTransactionalJUnit38SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.component.log.LogService;
import com.jinhe.tss.framework.sso.IdentityCard;
import com.jinhe.tss.framework.sso.TokenUtil;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.test.IH2DBServer;
import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.portal.entity.Element;
import com.jinhe.tss.portal.service.IElementService;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.helper.dto.OperatorDTO;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.um.permission.PermissionService;
import com.jinhe.tss.um.service.ILoginService;
import com.jinhe.tss.um.service.IResourceRegisterService;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.XMLDocUtil;

/**
 * Junit Test 类里执行构造函数的时候无事务，即构造函数不在单元测试方法的事物边界内。
 */
@SuppressWarnings("deprecation")
@ContextConfiguration(
        locations={
            "classpath:META-INF/portal-test-spring.xml",  
            "classpath:META-INF/framework-spring.xml",  
            "classpath:META-INF/um-spring.xml",
            "classpath:META-INF/cms-spring.xml"
        } 
        , inheritLocations = false // 是否要继承父测试用例类中的 Spring 配置文件，默认为 true
      )
@TransactionConfiguration(defaultRollback = false) // 不自动回滚，否则后续的test中没有初始化的数据
public abstract class TxSupportTest4Portal extends AbstractTransactionalJUnit38SpringContextTests { 
 
    protected Logger log = Logger.getLogger(this.getClass());    
    
    @Autowired protected IResourceRegisterService resourceRegisterService;
    @Autowired protected ILoginService loginSerivce;
    @Autowired protected PermissionService permissionService;
    @Autowired protected PermissionHelper permissionHelper;
    @Autowired protected LogService logService;
    
    @Autowired protected IH2DBServer dbserver;
 
    @Autowired protected IElementService elementService;
    
    protected void setUp() throws Exception {
        super.setUp();
        Global.setContext(super.applicationContext);
        Context.setResponse(new MockHttpServletResponse());
        
        // DB数据在一轮跑多个单元测试中初始化一次就够了。
        if( dbserver.isPrepareed() ) {
            return;
        }
        
        init();
        
        dbserver.setPrepareed(true);
    }
 
    /**
     * 初始化CMS的动态属性相关模板
     */
    protected void init() {
    	// 初始化数据库脚本
    	String sqlpath = TestUtil.getInitSQLDir();
    	log.info( " sql path : " + sqlpath);
        TestUtil.excuteSQL(sqlpath + "/framework");
        TestUtil.excuteSQL(sqlpath + "/um");
    	TestUtil.excuteSQL(sqlpath + "/cms");
    	TestUtil.excuteSQL(sqlpath + "/portal");
    	
    	// 初始化虚拟登录用户信息
        login(UMConstants.ADMIN_USER_ID, UMConstants.ADMIN_USER_NAME);
        
        /* 初始化应用系统、资源、权限项 */
        Document doc = XMLDocUtil.createDocByAbsolutePath(TestUtil.getSQLDir() + "/tss-application-config.xml");
        resourceRegisterService.setInitial(true);
        resourceRegisterService.applicationResourceRegister(doc, UMConstants.PLATFORM_SYSTEM_APP);
        resourceRegisterService.setInitial(false);
        
        // 门户浏览时，freemarker解析时需要用到request里的参数
        Context.initRequestContext(new MockHttpServletRequest()); 
        
        /* 初始化默认的修饰器，布局器 */
        initializeDefaultElement();
    }
    
    protected static String MODEL_PORTAL_DIR;
    protected static String MODEL_LAYOUT_DIR;
    protected static String MODEL_DECORATOR_DIR;
    protected static String MODEL_PORTLET_DIR;
  
    protected static Element defaultLayoutGroup;
    protected static Element defaultLayout;
    protected static Long defaultLayoutId;
    protected static Element defaultDecoratorGroup;
    protected static Element defaultDecorator;
    protected static Long defaultDecoratorId;
    
    /**
     * 初始化默认的修饰器，布局器
     */
    public void initializeDefaultElement() {
        // 初始化Portal测试的组件模型存放目录（model目录）及freemarker文件的目录
        String portalTargetPath = TestUtil.getProjectDir() + "/portal/target";
        
        MODEL_PORTAL_DIR = FileHelper.createDir(portalTargetPath + "/portal/model/portal").getPath();
        MODEL_PORTLET_DIR = FileHelper.createDir(portalTargetPath + "/portal/model/portlet").getPath();
        MODEL_LAYOUT_DIR = FileHelper.createDir(portalTargetPath + "/portal/model/layout").getPath();
        MODEL_DECORATOR_DIR = FileHelper.createDir(portalTargetPath + "/portal/model/decorator").getPath();
        
        File freemarkerDir = FileHelper.createDir(portalTargetPath + "/freemarker");
        FileHelper.writeFile(new File(freemarkerDir + "/common.ftl"), "");
        
        defaultLayoutGroup = new Element();
        defaultLayoutGroup.setName("默认布局器组");
        defaultLayoutGroup.setType(Element.LAYOUT_TYPE);
        defaultLayoutGroup.setParentId(PortalConstants.ROOT_ID);   
        defaultLayoutGroup = elementService.saveElement(defaultLayoutGroup);
        
        defaultLayout = new Element();
        defaultLayout.setIsDefault(PortalConstants.TRUE);
        defaultLayout.setParentId(defaultLayoutGroup.getId());   
        Document document = XMLDocUtil.createDoc("template/initialize/defaultLayout.xml");
        org.dom4j.Element propertyElement = document.getRootElement().element("property");
        String layoutName = propertyElement.elementText("name");
        defaultLayout.setName(layoutName);
        defaultLayout.setPortNumber(new Integer(propertyElement.elementText("portNumber")));
        defaultLayout.setDefinition(document.asXML());
        elementService.saveElement(defaultLayout);
        defaultLayoutId = defaultLayout.getId();
        
        defaultDecoratorGroup = new Element();
        defaultDecoratorGroup.setName("默认修饰器组");
        defaultDecoratorGroup.setType(Element.DECORATOR_TYPE);
        defaultDecoratorGroup.setParentId(PortalConstants.ROOT_ID);  
        defaultDecoratorGroup = elementService.saveElement(defaultDecoratorGroup);
        
        defaultDecorator = new Element();
        defaultDecorator.setIsDefault(PortalConstants.TRUE);
        defaultDecorator.setParentId(defaultDecoratorGroup.getId());
        
        document = XMLDocUtil.createDoc("template/initialize/defaultDecorator.xml");
        propertyElement = document.getRootElement().element("property");
        String decoratorName = propertyElement.elementText("name");
        defaultDecorator.setName(decoratorName);
        defaultDecorator.setDefinition(document.asXML());
        elementService.saveElement(defaultDecorator);
        defaultDecoratorId = defaultDecorator.getId();
    }
    
    protected void login(Long userId, String loginName) {
    	OperatorDTO loginUser = new OperatorDTO(userId, loginName);
    	String token = TokenUtil.createToken("1234567890", userId); 
        IdentityCard card = new IdentityCard(token, loginUser);
        Context.initIdentityInfo(card);
        
        // 获取登陆用户的权限（拥有的角色）并保存到用户权限（拥有的角色）对应表
        List<Object[]> userRoles = loginSerivce.getUserRolesAfterLogin(userId);
        permissionService.saveUserRolesAfterLogin(userRoles, userId);
    }
}
