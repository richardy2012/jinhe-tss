package com.jinhe.tss.portal;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.jinhe.tss.framework.Config;
import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.component.log.LogService;
import com.jinhe.tss.framework.sso.IdentityCard;
import com.jinhe.tss.framework.sso.TokenUtil;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.test.IH2DBServer;
import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.portal.entity.Component;
import com.jinhe.tss.portal.service.IComponentService;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.helper.dto.OperatorDTO;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.um.permission.PermissionService;
import com.jinhe.tss.um.service.ILoginService;
import com.jinhe.tss.um.service.IResourceService;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.XMLDocUtil;

/**
 * Junit Test 类里执行构造函数的时候无事务，即构造函数不在单元测试方法的事物边界内。
 */
@ContextConfiguration(
        locations={
            "classpath:META-INF/portal-test-spring.xml",  
            "classpath:META-INF/framework-spring.xml",  
            "classpath:META-INF/um-spring.xml",
            "classpath:META-INF/spring-mvc.xml"
        } 
        , inheritLocations = false // 是否要继承父测试用例类中的 Spring 配置文件，默认为 true
      )
@TransactionConfiguration(defaultRollback = false) // 不自动回滚，否则后续的test中没有初始化的数据
public abstract class TxSupportTest4Portal extends AbstractTransactionalJUnit4SpringContextTests { 
 
    protected Logger log = Logger.getLogger(this.getClass());    
    
    @Autowired protected IResourceService resourceService;
    @Autowired protected ILoginService loginSerivce;
    @Autowired protected PermissionService permissionService;
    @Autowired protected PermissionHelper permissionHelper;
    @Autowired protected LogService logService;
    
    @Autowired protected IH2DBServer dbserver;
 
    @Autowired protected IComponentService componentService;
    
    protected HttpServletResponse response;
    protected MockHttpServletRequest request;
    
    @Before
    public void setUp() throws Exception {
        Global.setContext(super.applicationContext);
        
        Context.initRequestContext(request = new MockHttpServletRequest());
        
        Context.setResponse( response = new MockHttpServletResponse());
        
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
        resourceService.setInitial(true);
        resourceService.applicationResourceRegister(doc, UMConstants.PLATFORM_SYSTEM_APP);
        resourceService.setInitial(false);
        
        // 门户浏览时，freemarker解析时需要用到request里的参数
        Context.initRequestContext(new MockHttpServletRequest()); 
        
        /* 初始化默认的修饰器，布局器 */
        initializeDefaultElement();
    }
    
    protected static String MODEL_PORTAL_DIR;
    protected static String MODEL_LAYOUT_DIR;
    protected static String MODEL_DECORATOR_DIR;
    protected static String MODEL_PORTLET_DIR;
  
    protected static Component defaultLayoutGroup;
    protected static Component defaultLayout;
    protected static Long defaultLayoutId;
    protected static Component defaultDecoratorGroup;
    protected static Component defaultDecorator;
    protected static Long defaultDecoratorId;
    
    /**
     * 初始化默认的修饰器，布局器
     */
    private void initializeDefaultElement() {
        // 初始化Portal测试的组件模型存放目录（model目录）及freemarker文件的目录
        String portalTargetPath = TestUtil.getProjectDir() + "/portal/target";
        
        MODEL_PORTAL_DIR = FileHelper.createDir(portalTargetPath + "/portal/model/portal").getPath();
        MODEL_PORTLET_DIR = FileHelper.createDir(portalTargetPath + "/portal/model/portlet").getPath();
        MODEL_LAYOUT_DIR = FileHelper.createDir(portalTargetPath + "/portal/model/layout").getPath();
        MODEL_DECORATOR_DIR = FileHelper.createDir(portalTargetPath + "/portal/model/decorator").getPath();
        
        File freemarkerDir = FileHelper.createDir(portalTargetPath + "/freemarker");
        FileHelper.writeFile(new File(freemarkerDir + "/common.ftl"), "");
        
        defaultLayoutGroup = new Component();
        defaultLayoutGroup.setName("布局器组");
        defaultLayoutGroup.setType(Component.LAYOUT_TYPE);
        defaultLayoutGroup.setIsGroup(true);
        defaultLayoutGroup.setParentId(PortalConstants.ROOT_ID);   
        defaultLayoutGroup = componentService.saveComponent(defaultLayoutGroup);
        
        defaultLayout = new Component();
        defaultLayout.setIsDefault(PortalConstants.TRUE);
        defaultLayout.setParentId(defaultLayoutGroup.getId());   
        Document document = XMLDocUtil.createDoc("template/initialize/defaultLayout.xml");
        org.dom4j.Element propertyElement = document.getRootElement().element("property");
        String layoutName = propertyElement.elementText("name");
        defaultLayout.setName(layoutName);
        defaultLayout.setPortNumber(new Integer(propertyElement.elementText("portNumber")));
        defaultLayout.setDefinition(document.asXML());
        componentService.saveComponent(defaultLayout);
        defaultLayoutId = defaultLayout.getId();
        
        defaultDecoratorGroup = new Component();
        defaultDecoratorGroup.setName("修饰器组");
        defaultDecoratorGroup.setIsGroup(true);
        defaultDecoratorGroup.setType(Component.DECORATOR_TYPE);
        defaultDecoratorGroup.setParentId(PortalConstants.ROOT_ID);  
        defaultDecoratorGroup = componentService.saveComponent(defaultDecoratorGroup);
        
        defaultDecorator = new Component();
        defaultDecorator.setIsDefault(PortalConstants.TRUE);
        defaultDecorator.setParentId(defaultDecoratorGroup.getId());
        
        document = XMLDocUtil.createDoc("template/initialize/defaultDecorator.xml");
        propertyElement = document.getRootElement().element("property");
        String decoratorName = propertyElement.elementText("name");
        defaultDecorator.setName(decoratorName);
        defaultDecorator.setDefinition(document.asXML());
        componentService.saveComponent(defaultDecorator);
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
    
    protected void importComponent(Long groupId, String filePath) {
    	ImportComponentServlet servlet = new ImportComponentServlet();
    	
    	File file = new File(filePath);
    	String fileName = file.getName();
    	
	    IMocksControl mocksControl =  EasyMock.createControl();
	    HttpServletRequest mockRequest = mocksControl.createMock(HttpServletRequest.class);
	    
	    EasyMock.expect(mockRequest.getParameter("groupId")).andReturn(groupId.toString());
	    
	    try {
	    	Part part = mocksControl.createMock(Part.class);
	    	EasyMock.expect(mockRequest.getPart("file")).andReturn(part).anyTimes();
	    	
	    	// 上传文本文件
			EasyMock.expect(part.getHeader("content-disposition")).andReturn("filename=\"" + fileName + "\"");
			
			// 代替part.write()
			FileHelper.copyFile(new File(Config.UPLOAD_PATH), file, true, false);
	        part.write(file.getName());
	        EasyMock.expectLastCall(); // void 类型方法的mock
	        
	        mocksControl.replay(); 
			servlet.doPost(mockRequest, response);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			Assert.assertFalse(e.getMessage(), true);
		}
    }
    
    protected void uploadFile(String contextPath, File file) {
    	UploadFileServlet servlet = new UploadFileServlet();
    	
    	String fileName = file.getName();
    	
	    IMocksControl mocksControl =  EasyMock.createControl();
	    HttpServletRequest mockRequest = mocksControl.createMock(HttpServletRequest.class);
	    
	    EasyMock.expect(mockRequest.getParameter("contextPath")).andReturn(contextPath);
	    
	    try {
	    	Part part = mocksControl.createMock(Part.class);
	    	EasyMock.expect(mockRequest.getPart("file")).andReturn(part).anyTimes();
	    	
	    	// 上传文本文件
			EasyMock.expect(part.getHeader("content-disposition")).andReturn("filename=\"" + fileName + "\"");
			
			// 代替part.write()
			FileHelper.copyFile(new File(Config.UPLOAD_PATH), file, true, false);
	        part.write(file.getName());
	        EasyMock.expectLastCall(); // void 类型方法的mock
	        
	        mocksControl.replay(); 
			servlet.doPost(mockRequest, response);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			Assert.assertFalse(e.getMessage(), true);
		}
    }
}
