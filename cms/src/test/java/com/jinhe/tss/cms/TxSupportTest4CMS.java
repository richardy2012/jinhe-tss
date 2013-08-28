package com.jinhe.tss.cms;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.junit.Assert;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.jinhe.tss.cms.dao.IArticleDao;
import com.jinhe.tss.cms.dao.IChannelDao;
import com.jinhe.tss.cms.service.IArticleService;
import com.jinhe.tss.cms.service.IChannelService;
import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.component.log.LogService;
import com.jinhe.tss.framework.sso.IdentityCard;
import com.jinhe.tss.framework.sso.TokenUtil;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.test.IH2DBServer;
import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.helper.dto.OperatorDTO;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.um.permission.PermissionService;
import com.jinhe.tss.um.service.ILoginService;
import com.jinhe.tss.um.service.IResourceService;
import com.jinhe.tss.util.FileHelper;
import com.jinhe.tss.util.URLUtil;
import com.jinhe.tss.util.XMLDocUtil;

/**
 * Junit Test 类里执行构造函数的时候无事务，即构造函数不在单元测试方法的事物边界内。
 */
@ContextConfiguration(
        locations={
                "classpath:META-INF/cms-test-spring.xml",  
                "classpath:META-INF/framework-spring.xml",  
                "classpath:META-INF/um-spring.xml",
                "classpath:META-INF/spring-mvc.xml"
              }, inheritLocations = false // 是否要继承父测试用例类中的 Spring 配置文件，默认为 true
      )
@TransactionConfiguration(defaultRollback = false) // 不自动回滚，否则后续的test中没有初始化的数据
public abstract class TxSupportTest4CMS extends AbstractTransactionalJUnit4SpringContextTests { 
 
    protected Logger log = Logger.getLogger(this.getClass());    
    
    @Autowired protected IH2DBServer dbserver;
    
    @Autowired protected IResourceService resourceService;
    @Autowired protected ILoginService loginSerivce;
    @Autowired protected PermissionService permissionService;
    @Autowired protected PermissionHelper permissionHelper;
    @Autowired protected LogService logService;
	
    @Autowired protected IChannelService channelService;
    @Autowired protected IArticleService articleService;
    @Autowired protected IChannelDao channelDao;
    @Autowired protected IArticleDao articleDao;
    
    protected MockHttpServletResponse response;
    protected MockHttpServletRequest request;
    
    protected File tempDir1;
    protected File tempDir2;
    
    @Before
    public void setUp() throws Exception {
    	
        URL url = URLUtil.getResourceFileUrl("log4j.properties");
        String log4jPath = url.getPath(); 
        File classDir = new File(log4jPath).getParentFile();
        Assert.assertTrue(FileHelper.checkFile(classDir, "log4j.properties"));
        
        tempDir1 = FileHelper.createDir(classDir + "/temp1");
        tempDir1 = FileHelper.createDir(classDir + "/temp1");
        
        Global.setContext(super.applicationContext);
		Context.setResponse(response = new MockHttpServletResponse());
        
        request = new MockHttpServletRequest();
        Context.initRequestContext(request);
        
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
    	
    	// 初始化虚拟登录用户信息
        login(UMConstants.ADMIN_USER_ID, UMConstants.ADMIN_USER_NAME);
        
        /* 初始化应用系统、资源、权限项 */
        Document doc = XMLDocUtil.createDocByAbsolutePath(TestUtil.getSQLDir() + "/cms-application-config.xml");
        resourceService.setInitial(true);
        resourceService.applicationResourceRegister(doc, UMConstants.PLATFORM_SYSTEM_APP);
        resourceService.setInitial(false);
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
