package com.jinhe.tss;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractTransactionalJUnit38SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.component.param.Param;
import com.jinhe.tss.framework.component.param.ParamConstants;
import com.jinhe.tss.framework.component.param.ParamService;
import com.jinhe.tss.framework.sso.IdentityCard;
import com.jinhe.tss.framework.sso.TokenUtil;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.entity.Element;
import com.jinhe.tss.portal.service.IElementService;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.helper.dto.OperatorDTO;
import com.jinhe.tss.um.permission.PermissionService;
import com.jinhe.tss.um.permission.ResourcePermission;
import com.jinhe.tss.um.service.ILoginService;
import com.jinhe.tss.um.service.IResourceRegisterService;
import com.jinhe.tss.util.XMLDocUtil;

/**
 * 初始化数据库。
 * 
 * 需使用 src/main/resources目录下的配置文件，比如persistence.xml, application.properties等。
 * 另外，初始化时需要把applicationContext.xml的<property name="generateDdl" value="true" /> 设置为true
 */
@SuppressWarnings("deprecation")
@ContextConfiguration(
        locations={
          "classpath:META-INF/framework-spring.xml",  
          "classpath:META-INF/um-spring.xml",
          "classpath:META-INF/cms-spring.xml",
          "classpath:META-INF/spring.xml"
        } 
      )
@TransactionConfiguration(defaultRollback = false) // 不自动回滚，否则后续的test中没有初始化的数据
public class InitDatabase extends AbstractTransactionalJUnit38SpringContextTests { 
 
    Logger log = Logger.getLogger(this.getClass());    
    
    @Autowired private IResourceRegisterService resourceRegisterService;
    @Autowired private ResourcePermission resourcePermission;
    @Autowired private ILoginService loginSerivce;
    @Autowired private PermissionService permissionService;
    
    @Autowired private IElementService elementService;
    
    protected void setUp() throws Exception {
        super.setUp();
        Global.setContext(super.applicationContext);
    }
    
    public void testInitDatabase() {
        log.info("create tss databse schema starting......");
 
        String sqlpath = TestUtil.getInitSQLDir();
        TestUtil.excuteSQL(sqlpath);
 
        //-------------------------------初始化系統的必要数据 --------------------
        // 初始化虚拟登录用户信息
        OperatorDTO loginUser = new OperatorDTO(UMConstants.ADMIN_USER_ID, UMConstants.ADMIN_USER_NAME);
        String token = TokenUtil.createToken("1234567890", UMConstants.ADMIN_USER_ID); 
        IdentityCard card = new IdentityCard(token, loginUser);
        Context.initIdentityInfo(card);
        
        // 获取登陆用户的权限（拥有的角色）并保存到用户权限（拥有的角色）对应表
        List<Object[]> userRoles = loginSerivce.getUserRolesAfterLogin(UMConstants.ADMIN_USER_ID);
        permissionService.saveUserRolesAfterLogin(userRoles, UMConstants.ADMIN_USER_ID);
        
        initUM();
        initPortal();
        
        importSystemProperties();
        
        log.info("init tss databse base data over.");
    }
 
    /**
     * 初始化UM、CMS、Portal相关应用、资源类型、权限选型信息
     */
    private void initUM() {
        /* 初始化应用系统、资源、权限项 */
        String sqlpath = TestUtil.getInitSQLDir();
        Document doc = XMLDocUtil.createDocByAbsolutePath(sqlpath + "/../tss-application-config.xml");
        resourceRegisterService.setInitial(true);
        resourceRegisterService.applicationResourceRegister(doc, UMConstants.PLATFORM_SYSTEM_APP);
        resourceRegisterService.setInitial(false);
        
        // 补全SQL初始化出来的系统级用户组
        Long[] groupIds = new Long[] {-2L, -3L, -4L, -7L, -8L, -9L};
        for(Long groupId : groupIds) {
        	resourcePermission.addResource(groupId, UMConstants.GROUP_RESOURCE_TYPE_ID);
        }
    }
    
    /** 初始化默认的修饰器，布局器 */
    private void initPortal() {
        Element defaultLayoutGroup = new Element();
        defaultLayoutGroup.setName("默认布局器组");
        defaultLayoutGroup.setType(Element.LAYOUT_TYPE);
        defaultLayoutGroup.setParentId(PortalConstants.ROOT_ID);   
        defaultLayoutGroup = elementService.saveElement(defaultLayoutGroup);
        
        Element defaultLayout = new Element();
        defaultLayout.setIsDefault(PortalConstants.TRUE);
        defaultLayout.setParentId(defaultLayoutGroup.getId());   
        Document document = XMLDocUtil.createDoc("template/initialize/defaultLayout.xml");
        org.dom4j.Element propertyElement = document.getRootElement().element("property");
        String layoutName = propertyElement.elementText("name");
        defaultLayout.setName(layoutName);
        defaultLayout.setPortNumber(new Integer(propertyElement.elementText("portNumber")));
        defaultLayout.setDefinition(document.asXML());
        elementService.saveElement(defaultLayout);
        
        Element defaultDecoratorGroup = new Element();
        defaultDecoratorGroup.setName("默认修饰器组");
        defaultDecoratorGroup.setType(Element.DECORATOR_TYPE);
        defaultDecoratorGroup.setParentId(PortalConstants.ROOT_ID);  
        defaultDecoratorGroup = elementService.saveElement(defaultDecoratorGroup);
        
        Element defaultDecorator = new Element();
        defaultDecorator.setIsDefault(PortalConstants.TRUE);
        defaultDecorator.setParentId(defaultDecoratorGroup.getId());
        
        document = XMLDocUtil.createDoc("template/initialize/defaultDecorator.xml");
        propertyElement = document.getRootElement().element("property");
        String decoratorName = propertyElement.elementText("name");
        defaultDecorator.setName(decoratorName);
        defaultDecorator.setDefinition(document.asXML());
        elementService.saveElement(defaultDecorator);
    }
    
    
    @Autowired private ParamService paramService;
 
    /**
     * 导入 application.properties文件 和 appServers.xml
     */
    public void importSystemProperties(){
        String name = "系统参数";
        Param param = addParam(ParamConstants.DEFAULT_PARENT_ID, name);
        ResourceBundle resources = ResourceBundle.getBundle("application", Locale.getDefault());
        if (resources == null) return;
        
        for (Enumeration<String> enumer = resources.getKeys(); enumer.hasMoreElements();) {
            String key = enumer.nextElement();
            String value = resources.getString(key);
            addParam(param.getId(), key, key, value);
        }
 
        Param paramGroup = addParam(ParamConstants.DEFAULT_PARENT_ID, "应用服务配置");
        
        Document doc = XMLDocUtil.createDoc("appServers.xml");
        List<?> elements = doc.getRootElement().elements();
        for (Iterator<?> it = elements.iterator(); it.hasNext();) {
            org.dom4j.Element element = (org.dom4j.Element) it.next();
            String appName = element.attributeValue("name");
            String appCode = element.attributeValue("code");
            addParam(paramGroup.getId(), appCode, appName, element.asXML());
        }
    }

    /**
     * 建参数组
     */
    Param addParam(Long parentId, String name) {
        Param param = new Param();
        param.setName(name);
        param.setParentId(parentId);
        param.setType(ParamConstants.GROUP_PARAM_TYPE);
        paramService.saveParam(param);
        return param;
    }

    /**
     * 简单参数
     */
    Param addParam(Long parentId, String code, String name, String value) {
        Param param = new Param();
        param.setCode(code);
        param.setName(name);
        param.setValue(value);
        param.setParentId(parentId);
        param.setType(ParamConstants.NORMAL_PARAM_TYPE);
        param.setModality(ParamConstants.SIMPLE_PARAM_MODE);
        paramService.saveParam(param);
        return param;
    }

    /**
     * 下拉型参数
     */
    Param addParam(Long parentId, String code, String name) {
        Param param = new Param();
        param.setCode(code);
        param.setName(name);
        param.setParentId(parentId);
        param.setType(ParamConstants.NORMAL_PARAM_TYPE);
        param.setModality(ParamConstants.COMBO_PARAM_MODE);
        paramService.saveParam(param);
        return param;
    }

    /**
     * 新建设参数项
     */
    Param addParamItem(Long parentId, String value, String text, Integer mode) {
        Param param = new Param();
        param.setValue(value);
        param.setText(text);
        param.setParentId(parentId);
        param.setType(ParamConstants.ITEM_PARAM_TYPE);
        param.setModality(mode);
        paramService.saveParam(param);
        return param;
    }
}
