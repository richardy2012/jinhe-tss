package com.jinhe.tss.portal.engine;
 
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.jinhe.tss.cms.service.IRemoteArticleService;
import com.jinhe.tss.framework.sso.IdentityCard;
import com.jinhe.tss.framework.sso.context.ApplicationContext;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.portal.PortalConstants;
import com.jinhe.tss.portal.TxSupportTest4Portal;
import com.jinhe.tss.portal.dao.INavigatorDao;
import com.jinhe.tss.portal.entity.Navigator;
import com.jinhe.tss.um.helper.dto.OperatorDTO;
import com.jinhe.tss.util.XMLDocUtil;

public class FreeMarkerParserTest extends TxSupportTest4Portal {
	
	@Autowired INavigatorDao navigatorDao;
	
	Navigator menu;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		Context.initApplicationContext(new ApplicationContext());
 
		menu = new Navigator();
        menu.setType(Navigator.TYPE_MENU);
        menu.setName("测试菜单" + System.currentTimeMillis());
        menu.setPortalId(0L);
        menu.setParentId(PortalConstants.ROOT_ID);
        menu.setSeqNo(navigatorDao.getNextSeqNo(menu.getParentId()));
        navigatorDao.save(menu);
	}
    
    @Test
    public void test0() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Context.initRequestContext(request);
        IdentityCard card = new IdentityCard("token", OperatorDTO.ADMIN);
        Context.initIdentityInfo(card);
        
        String templateStr = "<#assign Environment = statics[\"com.jinhe.tss.framework.sso.Environment\"] />" +
                "<#setting number_format=\"0\">" +
                "${Environment.getOperatorId()?string.number}";
        
        FreemarkerParser parser = new FreemarkerParser(null);
        parser.parseTemplateTwice(templateStr, new OutputStreamWriter(System.out));
    }

    @Test
    public void test1() throws Exception {
        String templateStr = "<#assign manager = statics[\"com.jinhe.tss.portal.engine.StaticManager\"] />" +
                "<#assign data = manager.listFiles(\"d:/temp\") />" +
                "${data.get(2)} \n <#list data as file> ${file} \n </#list> ";

        FreemarkerParser parser = new FreemarkerParser(null);
        parser.parseTemplateTwice(templateStr, new OutputStreamWriter(System.out));
    }
    
    @Test
    public void test2() throws Exception {
        String div = "<DIV id=span_div_27 style=\"FONT-SIZE: 1px; Z-INDEX: 10; LEFT: 0px; VISIBILITY: hidden;" +
                " POSITION: relative; TOP: 7px; HEIGHT: 1px\"></DIV>";
        
        String templateStr = "" +
             "<#assign manager = statics[\"com.jinhe.tss.portal.engine.StaticManager\"] />" +
             "<#assign data = manager.getNavigatorService().getNavigatorXML(" + menu.getId() + ") />" +
             "<#assign doc  = statics[\"freemarker.ext.dom.NodeModel\"].parse(manager.translateValue(data)) />" + 
             "<#assign menu = doc.Menu>" + 
             "<TABLE height=33 cellSpacing=0 cellPadding=0 width=\"90%\" align=center><TBODY><TR> \n" + 
             "<#list menu.MenuItem as item>" + 
             "<TD class=mainTd id=td_${item.@id}><SPAN class=menuSpan id=manegeMenu_${item.@id}>" +
             "<a href='${item.@url}'>${item.@name}</a>" +
             "</SPAN>" + div + "</TD> \n" + 
             "</#list>" +
             "</TR></TBODY></TABLE>";

        FreemarkerParser parser = new FreemarkerParser(null);
        parser.parseTemplateTwice(templateStr, new OutputStreamWriter(System.out));
    }
    
    @Test
    public void test3() throws Exception {
        String tempalteStr = "<@common.showMenu menuId=" + menu.getId() + "/>";
        FreemarkerParser parser = new FreemarkerParser(null);
        parser.parseTemplateTwice(tempalteStr, new OutputStreamWriter(System.out));
    }
    
    @Test
    public void test4() throws Exception {
    	FreemarkerParser parser = new FreemarkerParser(null);
        parser.getDataModel().put("article", 
                FreemarkerParser.translateValue(XMLDocUtil.createDoc("testdata/article.xml").asXML()));
        parser.getDataModel().put("articleList", 
                FreemarkerParser.translateValue(XMLDocUtil.createDoc("testdata/articleList.xml").asXML()));
        
        String templateStr = "<#assign channel = articleList.ArticleList.channel>" +
                "<#list channel.item as item>" +
                "<a href='/tss/article2.portal?articleId=${item.id}'>* ${item.title}</a><br>\n" +
                "</#list>";
        
        parser.parseTemplateTwice(templateStr, new OutputStreamWriter(System.out));
    }
    
    @Test
    public void test5() throws Exception {
        IRemoteArticleService obj = StaticManager.getArticleService();
        
        obj.getArticleListByChannel(48L, 1, 5, true);
        
        String templateStr = "<@common.getArticleListXML 3, 1, 5/>";
        
        FreemarkerParser parser = new FreemarkerParser(null);
        parser.parseTemplateTwice(templateStr, new OutputStreamWriter(System.out));
    }
    
    @Test
    public void test6() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("channelId", "12");
        FreemarkerParser parser = new FreemarkerParser(null);
        parser.putParameters(map);
        
        String templateStr = "${channelId}" +
                "<#assign id = channelId/>${id}";
        parser.parseTemplateTwice(templateStr, new OutputStreamWriter(System.out));
    }
    
    @Test
    public void test7() throws Exception{
        Map<String, String> map = new HashMap<String, String>();
        map.put("channelId", "22");
        FreemarkerParser parser = new FreemarkerParser();
        parser.putParameters(map);
        
        String templateStr = "<#assign Long = statics[\"java.lang.Long\"] />" +
                             "<#assign Test = statics[\"com.jinhe.tss.portal.engine.FreeMarkerParserTest\"] />" +
                             "<#macro show channelId>" +
                             "   <#assign num = Test.increase(channelId)/>" +
                             "   ${num}\n" +
                             "</#macro>" +
                             "<@show channelId/>" +
                             "<@show Long.valueOf(channelId)/>" +
                             "";
        
        parser.parseTemplateTwice(templateStr, new OutputStreamWriter(System.out));
        
        StringWriter out = new StringWriter();
        parser.parseTemplateTwice(templateStr, out);
        System.out.println(out.toString());
    }
    
    
    public static Long increase(Long num){
        return new Long(num.longValue() + 1);
    }
    
    public static Long increase(String num){
        return new Long(num);
    }
}  


