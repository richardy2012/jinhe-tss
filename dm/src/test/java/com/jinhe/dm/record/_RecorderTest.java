package com.jinhe.dm.record;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.dm.DMConstants;
import com.jinhe.dm.TxTestSupport4DM;

public class _RecorderTest extends TxTestSupport4DM {
	
	@Autowired RecordService recordService;
	@Autowired _Recorder recorder;
	
	Long recordId;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		String tblDefine = "[ {'label':'类型', 'code':'f1', 'type':'number', 'nullable':'false'}," +
        		"{'label':'名称', 'code':'f2', 'type':'string'}," +
        		"{'label':'时间', 'code':'f3', 'type':'datetime', 'nullable':'false'}]";
		
		Record record = new Record();
		record.setName("record-1-1");
		record.setType(1);
		record.setParentId(0L);
		
		record.setDatasource(DMConstants.LOCAL_CONN_POOL);
		record.setTable("x_tbl_12");
		record.setDefine(tblDefine);
		
		String customizeSQL = "select f1,f2,f3,createtime,creator,updatetime,updator,version,id from x_tbl_12 where creator = ?";
		record.setCustomizeSQL(customizeSQL);
		
		recordService.saveRecord(record);
		recordId = record.getId();
	}

	@Test
	public void test() {
		Map<String, String> valuesMap = new HashMap<String, String>();
		valuesMap.put("f1", "10.9");
		valuesMap.put("f2", "just test");
		valuesMap.put("f3", "2015-04-05");
		
		Assert.assertNotNull(recorder.getDefine(recordId));
		
		recorder.create(response, recordId, valuesMap);
		recorder.update(response, recordId, 1, valuesMap);
		
		recorder.showAsGrid(request, response, recordId, 1);
		recorder.showAsJSON(recordId, 1);
		
		recorder.delete(response, recordId, 1);
	}
	
}
