package com.jinhe.dm.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jinhe.dm.data.sqlquery.SQLExcutor;
import com.jinhe.dm.record.ddl._Database;
import com.jinhe.tss.cache.Cacheable;
import com.jinhe.tss.cache.Pool;
import com.jinhe.tss.framework.component.cache.CacheHelper;
import com.jinhe.tss.framework.persistence.pagequery.PageInfo;
import com.jinhe.tss.framework.web.dispaly.grid.DefaultGridNode;
import com.jinhe.tss.framework.web.dispaly.grid.GridDataEncoder;
import com.jinhe.tss.framework.web.dispaly.grid.IGridNode;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;

@Controller
@RequestMapping("/auth/xdata")
public class _Recorder extends BaseActionSupport {
	
	@Autowired RecordService recordService;
	
	_Database getDB(Long recordId) {
		Pool cache = CacheHelper.getLongCache();
		
		String cacheKey = "_db_record_" + recordId;
		Cacheable cacheItem = cache.getObject(cacheKey);
				
		_Database _db;
		if(cacheItem == null) {
			Record record = recordService.getRecord(recordId);
			cache.putObject(cacheKey, _db = _Database.getDB(record));
		}
		else{
			_db = (_Database) cacheItem.getValue();
		}
		
		return _db;
	}
	
	@RequestMapping("/define/{recordId}")
    @ResponseBody
    public Object getDefine(@PathVariable("recordId") Long recordId) {
		Record record = recordService.getRecord(recordId);
        return 
        	new Object[] { 
        		getDB(recordId).getFields(), 
        		record.getCustomizeJS(), 
        		record.getCustomizeGrid() 
        	};
    }
	
	public static final int PAGE_SIZE = 50;
	
    @RequestMapping("/xml/{recordId}/{page}")
    public void showAsGrid(HttpServletRequest request, HttpServletResponse response, 
            @PathVariable("recordId") Long recordId, 
            @PathVariable("page") int page) {
 
        _Database _db = getDB(recordId);
        
        SQLExcutor ex = _db.select(page, PAGE_SIZE, getRequestMap(request));
        
        List<IGridNode> temp = new ArrayList<IGridNode>();
		for(Map<String, Object> item : ex.result) {
            DefaultGridNode gridNode = new DefaultGridNode();
            gridNode.getAttrs().putAll(item);
            temp.add(gridNode);
        }
        GridDataEncoder gEncoder = new GridDataEncoder(temp, _db.getGridTemplate());
        
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(PAGE_SIZE);
        pageInfo.setTotalRows(ex.count);
        pageInfo.setPageNum(page);
        
        print(new String[] {"RecordData", "PageInfo"}, new Object[] {gEncoder, pageInfo});
    }
    
    @RequestMapping("/json/{recordId}/{page}")
    @ResponseBody
    public List<Map<String, Object>> showAsJSON(HttpServletRequest request, 
    		@PathVariable("recordId") Long recordId, @PathVariable("page") int page) {
    	
        _Database _db = getDB(recordId);
        return _db.select(page, PAGE_SIZE, getRequestMap(request)).result;
    }
	
    @RequestMapping(value = "/{recordId}", method = RequestMethod.POST)
    public void create(HttpServletRequest request, HttpServletResponse response, 
    		@PathVariable("recordId") Long recordId) {
    	
    	Map<String, String> row = getRequestMap(request);
		getDB(recordId).insert(row);
        printSuccessMessage();
    }
    
    @RequestMapping(value = "/{recordId}/{id}", method = RequestMethod.POST)
    public void update(HttpServletRequest request, HttpServletResponse response, 
    		@PathVariable("recordId") Long recordId, 
    		@PathVariable("id") Integer id) {
    	
    	Map<String, String> row = getRequestMap(request);
		getDB(recordId).update(id, row);
        printSuccessMessage();
    }
    
    /**
     * 批量更新选中记录行的某个字段值，用在批量审批等场景
     */
    @RequestMapping(value = "/batch/{recordId}", method = RequestMethod.POST)
    public void updateBatch(HttpServletResponse response, 
    		@PathVariable("recordId") Long recordId, 
    		String ids, String field, String value) {
    	
		getDB(recordId).updateBatch(ids, field, value);
        printSuccessMessage();
    }
    
    private Map<String, String> getRequestMap(HttpServletRequest request) {
    	Map<String, String[]> parameterMap = request.getParameterMap();
    	Map<String, String> requestMap = new HashMap<String, String>();
    	for(String key : parameterMap.keySet()) {
    		String[] values = parameterMap.get(key);
    		if(values != null && values.length > 0) {
    			requestMap.put( key, values[0] );
    		}
    	}
    	
    	return requestMap;
    }
    
    @RequestMapping(value = "/{recordId}/{id}", method = RequestMethod.DELETE)
    public void delete(HttpServletResponse response, 
    		@PathVariable("recordId") Long recordId, 
    		@PathVariable("id") Integer id) {
    	
    	getDB(recordId).delete(id);
        printSuccessMessage();
    }
    
    /************************************* record attach operation **************************************/
    
	@RequestMapping("/attach/{recordId}/{itemId}")
    @ResponseBody
    public List<?> getAttachList(@PathVariable("recordId") Long recordId, @PathVariable("itemId") Long itemId) {
		List<?> list = recordService.getAttachList(recordId, itemId);
		return list;
    }
	
	@RequestMapping(value = "/attach/{id}", method = RequestMethod.DELETE)
    public void deleteAttach(HttpServletResponse response, @PathVariable("id") Long id) {
		recordService.deleteAttach(id);
        printSuccessMessage();
    }
}
