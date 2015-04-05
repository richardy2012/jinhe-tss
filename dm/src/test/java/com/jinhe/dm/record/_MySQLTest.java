package com.jinhe.dm.record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jinhe.dm.DMConstants;
import com.jinhe.dm.data.sqlquery.SQLExcutor;
import com.jinhe.dm.record.ddl._Database;
import com.jinhe.dm.record.ddl._MySQL;
import com.jinhe.tss.framework.sso.IdentityCard;
import com.jinhe.tss.framework.sso.TokenUtil;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.um.helper.dto.OperatorDTO;

public class _MySQLTest {
	
	@Before
	public void setUp() {
		OperatorDTO loginUser = new OperatorDTO(12L, "JK");
    	String token = TokenUtil.createToken("1234567890", 12L); 
        IdentityCard card = new IdentityCard(token, loginUser);
        Context.initIdentityInfo(card);
	}
	
	@Test
	public void test() {
		String tblDefine = "[ {'label':'类型', 'code':'f1', 'type':'number', 'nullable':'false'}," +
        		"{'label':'名称', 'code':'f2', 'type':'string'}," +
        		"{'label':'时间', 'code':'f3', 'type':'datetime', 'nullable':'false'}]";
		
		Record record = new Record();
		record.setDatasource(DMConstants.LOCAL_CONN_POOL);
		record.setTable("x_tbl_2");
		record.setDefine(tblDefine);
		
		_Database _db = new _MySQL(record);
		_db.createTable();
		
		// test update table with row = 0
		record = new Record();
		record.setDatasource(DMConstants.LOCAL_CONN_POOL);
		record.setTable("x_tbl_3");
		record.setDefine(tblDefine);
		
		_db.updateTable(record);
		
		// test insert
		Map<String, String> valuesMap = new HashMap<String, String>();
		valuesMap.put("f1", "10.9");
		valuesMap.put("f2", "just test");
		valuesMap.put("f3", "2015-04-05");
		_db.insert(valuesMap);
		
		List<Map<String, Object>> result = _db.select();
		Assert.assertTrue(result.size() == 1);
		
		Map<String, Object> row = result.get(0);
		Integer id = (Integer) row.get("id");
		Assert.assertEquals(10.9, row.get("f1"));
		Assert.assertNotNull(row.get("createtime"));
		Assert.assertEquals(0, row.get("version"));
		
		// test update
		valuesMap = new HashMap<String, String>();
		valuesMap.put("f1", "12");
		_db.update(id, valuesMap);
		
		result = _db.select();
		Assert.assertTrue(result.size() == 1);
		row = result.get(0);
		Assert.assertEquals(12.0, row.get("f1"));
		Assert.assertEquals("just test", row.get("f2"));
		Assert.assertNotNull(row.get("updatetime"));
		Assert.assertEquals(1, row.get("version"));
		
		// test update table with row > 0
		tblDefine = "[ {'label':'类型', 'code':'f1', 'type':'number', 'nullable':'false'}," +
        		"{'label':'名称', 'code':'f2', 'type':'string'}," +
        		"{'label':'时间2', 'code':'f4', 'type':'datetime', 'nullable':'false'}]";
		
		record = new Record();
		record.setDatasource(DMConstants.LOCAL_CONN_POOL);
		record.setTable("x_tbl_3");
		record.setDefine(tblDefine);
		
		_db.updateTable(record);
		result = _db.select();
		Assert.assertTrue(result.size() == 0);
		
		SQLExcutor ex = new SQLExcutor(false);
		ex.excuteQuery("select id from x_tbl_3_old", DMConstants.LOCAL_CONN_POOL);
		Assert.assertTrue(ex.result.size() == 1);
		
		// test insert
		valuesMap = new HashMap<String, String>();
		valuesMap.put("f1", "10.9");
		valuesMap.put("f2", "just test");
		valuesMap.put("f4", "2015-04-05");
		_db.insert(valuesMap);
		
		result = _db.select();
		Assert.assertTrue(result.size() == 1);
		id = (Integer) row.get("id");
		
		// test delete
		_db.delete(id);
		Assert.assertTrue(_db.select().size() == 0);
	}
}
