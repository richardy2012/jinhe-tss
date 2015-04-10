package com.jinhe.dm.record;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

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
		
		recordService.saveRecord(record);
		recordId = record.getId();
	}

	@Test
	public void test() {
		Assert.assertNotNull(recorder.getDefine(recordId));
		
		request.addParameter("f1", "10.9");
		request.addParameter("f2", "just test");
		request.addParameter("f3", "2015-04-05");
		recorder.create(request, response, recordId);
		recorder.update(request, response, recordId, 1);
		
		Assert.assertTrue(recorder.getDB(recordId).select().size() == 1);
		
		request = new MockHttpServletRequest();
		request.addParameter("f2", "just test"); // test as query condition
		recorder.showAsGrid(request, response, recordId, 1);
		recorder.showAsJSON(request, recordId, 1);
		
		recorder.delete(response, recordId, 1);
		
		Assert.assertTrue(recorder.getDB(recordId).select().size() == 0);
	}
	
}
