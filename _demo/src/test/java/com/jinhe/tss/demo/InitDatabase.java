package com.jinhe.tss.demo;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.sso.IdentityCard;
import com.jinhe.tss.framework.sso.TokenUtil;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.helper.dto.OperatorDTO;
import com.jinhe.tss.util.URLUtil;

/**
 * 初始化数据库。
 * 
 * 执行前先把test/resources/application.properties改名。
 * 
 * 需使用 src/main/resources目录下的配置文件，比如persistence.xml, application.properties等。
 * 另外，初始化时需要把applicationContext.xml的<property name="generateDdl" value="true" /> 设置为true
 * persistence.xml 的 hibernate.dialect 设置为 org.hibernate.dialect.MySQLDialect
 */
@ContextConfiguration(
        locations={
        		"classpath:META-INF/remote/um-remote.xml",
        		"classpath:META-INF/remote/um-interceptor.xml",
        		"classpath:META-INF/framework-spring.xml",
        		"classpath:META-INF/spring-mvc.xml",
    		    "classpath:META-INF/spring.xml"
        } 
      )
@TransactionConfiguration(defaultRollback = false) // 不自动回滚，否则后续的test中没有初始化的数据
public class InitDatabase extends AbstractTransactionalJUnit4SpringContextTests { 
 
    Logger log = Logger.getLogger(this.getClass());    

    @Before
    public void setUp() throws Exception {
        Global.setContext(super.applicationContext);
    }
    
    @Test
    public void initDatabase() {
        log.info("create databse schema starting......");
 
        String sqlPath = URLUtil.getResourceFileUrl("sql/mysql").getPath();
        TestUtil.excuteSQL(sqlPath, false);
 
        OperatorDTO loginUser = new OperatorDTO(UMConstants.ADMIN_USER_ID, UMConstants.ADMIN_USER_NAME);
    	String token = TokenUtil.createToken("1234567890", UMConstants.ADMIN_USER_ID); 
        IdentityCard card = new IdentityCard(token, loginUser);
        Context.initIdentityInfo(card);
        
        log.info("init databse over.");
    }
 
}
