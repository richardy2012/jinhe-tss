package com.jinhe.dm.record;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.dm.DMConstants;
import com.jinhe.dm.TxTestSupport4DM;
import com.jinhe.dm.data.sqlquery.SQLExcutor;
import com.jinhe.dm.record.ddl._Database;
import com.jinhe.dm.record.file.ImportCSV;
import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.framework.web.servlet.AfterUpload;
import com.jinhe.tss.util.FileHelper;

public class ImportCSVTest extends TxTestSupport4DM {
	
	@Autowired RecordService recordService;
	@Autowired _Recorder recorder;
	
	Logger log = Logger.getLogger(this.getClass());
	
	@Test
	public void test() {
		String tblDefine = 
				"[ " +
					"{'label':'类型', 'type':'number', 'nullable':'false'}," +
	        		"{'label':'名称', 'type':'string'}," +
	        		"{'label':'时间', 'type':'datetime', 'nullable':'false'}" +
        		"]";
		
		Record record = new Record();
		record.setName("record-1-csv");
		record.setType(1);
		record.setParentId(0L);
		
		record.setDatasource(DMConstants.LOCAL_CONN_POOL);
		record.setTable("x_tbl_icsv_1");
		record.setDefine(tblDefine);
		
		recordService.saveRecord(record);
		Long recordId = record.getId();
		
		importCSVData(recordId);
		
		_Database _db = _Database.getDB(record);
		SQLExcutor ex = _db.select();
		Assert.assertEquals(2, ex.result.size());
		Assert.assertEquals("hehe", ex.result.get(0).get("f2"));
		Assert.assertEquals("哈哈", ex.result.get(1).get("f2"));
		
		// 下载导入模板
		recorder.getImportTL(response, recordId);
	}
	
	static String UPLOAD_PATH = TestUtil.getTempDir() + "/upload/record/";
	
	 // 上传附件
    private void importCSVData(Long recordId) {
    	AfterUpload upload = new ImportCSV();
    	
	    IMocksControl mocksControl =  EasyMock.createControl();
	    HttpServletRequest mockRequest = mocksControl.createMock(HttpServletRequest.class);
	    
	    EasyMock.expect(mockRequest.getParameter("recordId")).andReturn(recordId.toString());
	    EasyMock.expect(mockRequest.getParameter("petName")).andReturn(null);
	    
	    try {
	    	String filename = "1.csv";
			String filepath = UPLOAD_PATH + "/" + filename;
			FileHelper.writeFile(new File(filepath), "类型,名称,时间\n" +
					"1,哈哈,2015-10-29\n" +
					"2,hehe,2015-10-19\n");
	        
	        mocksControl.replay(); 
			upload.processUploadFile(mockRequest, filepath, filename);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			Assert.assertFalse(e.getMessage(), true);
		}
    }
}
