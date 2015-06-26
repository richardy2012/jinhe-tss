package com.jinhe.dm.record;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.jinhe.dm.DMConstants;
import com.jinhe.dm.TxTestSupport4DM;
import com.jinhe.tss.framework.component.log.LogService;
import com.jinhe.tss.framework.component.param.ParamConstants;
import com.jinhe.tss.framework.test.TestUtil;

public class _RecorderTest extends TxTestSupport4DM {
	
	@Autowired LogService logService;
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
		record.setNeedLog(ParamConstants.TRUE);
		
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
		
		List<Map<String, Object>> result = recorder.getDB(recordId).select().result;
		Assert.assertTrue(result.size() == 1);
		
		request = new MockHttpServletRequest();
		request.addParameter("f2", "just test"); // test as query condition
		recorder.showAsGrid(request, response, recordId, 1);
		recorder.showAsJSON(request, recordId, 1);
		
		for(int i = 0; i < 17; i++) {
			request = new MockHttpServletRequest();
			request.addParameter("f1", "12.0");
			request.addParameter("f2", "i'm " + i);
			request.addParameter("f3", "2015-04-05");
			recorder.update(request, response, recordId, 1); // 多次修改，以生成日志
		}
		
		recorder.updateBatch(response, recordId, "1,2", "f1", "1212");
		result = recorder.getDB(recordId).select().result;
		Assert.assertTrue(result.get(0).get("f1").equals(1212.0d));
		
		recorder.delete(response, recordId, 1);
		
		result = recorder.getDB(recordId).select().result;
		Assert.assertTrue(result.size() == 0);
		
		try { Thread.sleep(1000); } catch (Exception e) { } // 等待修改日志输出
		assertTrue(TestUtil.printLogs(logService) > 0);
		
		try {
			request = new MockHttpServletRequest();
			request.addParameter("f1", "12.12");
			request.addParameter("f2", "just test end");
			request.addParameter("f3", "2015-04-05");
			recorder.update(request, response, recordId, 1);
        	Assert.fail("should throw exception but didn't.");
        } catch (Exception e) {
        	Assert.assertTrue("修改出错，该记录不存在，可能已经被删除。", true);
        }
	}
	
}
