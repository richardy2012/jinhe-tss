package com.jinhe.tss.framework;

import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractTransactionalJUnit38SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.jinhe.tss.framework.sso.DemoOperator;
import com.jinhe.tss.framework.sso.IdentityCard;
import com.jinhe.tss.framework.sso.TokenUtil;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.framework.test.IH2DBServer;
import com.jinhe.tss.framework.test.TestUtil;

@SuppressWarnings("deprecation")
@ContextConfiguration(
	  locations={
		    "classpath:META-INF/framework-test-spring.xml",  
		    "classpath:META-INF/framework-spring.xml"
	  }   
) 
@TransactionConfiguration(defaultRollback = true) // 自动回滚设置为false，否则数据将不插进去
public abstract class TxTestSupport extends AbstractTransactionalJUnit38SpringContextTests { 
 
    protected static Logger log = Logger.getLogger(TxTestSupport.class);    
    
    @Autowired protected IH2DBServer dbserver;
    
    protected void setUp() throws Exception {
        super.setUp();
        Global.setContext(super.applicationContext);
        
        if( !dbserver.isPrepareed() ) {
            TestUtil.excuteSQL(TestUtil.getInitSQLDir() + "/framework");
            dbserver.setPrepareed(true);
        }
        
        String token = TokenUtil.createToken(new Random().toString(), new Long(12)); 
        IdentityCard card = new IdentityCard(token, new DemoOperator(12L));
        Context.initIdentityInfo(card);
        
        Context.setResponse(new MockHttpServletResponse());
    }
 
    protected void tearDown() throws Exception {
        super.tearDown();
        dbserver.stopServer();
    }

}
