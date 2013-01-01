package com.jinhe.tss.framework.persistence.pagequery;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
 
public class PageInfoTest extends TestCase {

    private PageInfo page;
 
    protected void setUp() throws Exception {
        page = new PageInfo();
        page.setTotalRows(0);
    }
 
    protected void tearDown() throws Exception {
        page = null;
    }

    public final void testGetPageSize() {
        assertEquals("默认值", PageInfo.DEFAULT_PAGESIZE, page.getPageSize());
        page.setPageSize(12);
        assertTrue(12 ==  page.getPageSize());
    }

    public final void testGetTotalPages() {
    	assertTrue("totalRows = 0", page.getTotalPages() == 0);
        page.setTotalRows(22);
        assertTrue("totalRows % pageSize > 0", page.getTotalPages() == 2);
        page.setTotalRows(40);
        assertTrue("totalRows % PageSize = 0", page.getTotalPages() == 2);
    }

    public final void testGetPageNum() {
    	assertTrue("totalRows = 0", page.getPageNum() == 1);
        page.setTotalRows(12);
        assertTrue("totalRows > 0, pageNum = null", page.getPageNum() == 1);
        page.setPageNum(0);
        assertTrue("totalRows > 0, pageNum = 0", page.getPageNum() == 1);
        page.setPageNum(2);
        assertTrue("totalRows > 0, pageNum > totalPages", page.getPageNum() == 1);
        page.setPageNum(2);
        page.setTotalRows(42);
        assertTrue("totalRows > 0, pageNum < totalPages", page.getPageNum() == 2);
    }

    public final void testGetFirstPageNum() {
    	assertTrue("totalRows = 0", page.getFirstPageNum() == 1);
        page.setTotalRows(12);
        assertTrue("totalRows > 0, totalPages = 1", page.getFirstPageNum() == 1);
        page.setTotalRows(22);
        page.setPageNum(1);
        assertTrue("totalRows > 0, totalPages > 1, pageNum = 1", page.getFirstPageNum() == 1);
        page.setPageNum(2);
        assertTrue("totalRows > 0, totalPages > 1, pageNum > 1", page.getFirstPageNum() == 1);
    }

    public final void testGetNextPageNum() {
    	assertTrue("totalRows = 0", page.getNextPageNum() == 0);
        page.setTotalRows(12);
        assertTrue("totalRows > 0, pageNum = totalPages", page.getNextPageNum() == 0);
        page.setTotalRows(22);
        assertTrue("totalRows > 0, pageNum < totalPages", page.getNextPageNum() == 2);
        page.setPageNum(3);
        assertTrue("totalRows > 0, pageNum > totalPages", page.getNextPageNum() == 0);
    }

    public final void testGetLastPageNum() {
    	assertTrue("totalRows = 0", page.getLastPageNum() == 0);
        page.setTotalRows(12);
        assertTrue("totalRows > 0, pageNum = totalPages", page.getLastPageNum() == 1);
        page.setTotalRows(22);
        assertTrue("totalRows > 0, pageNum < totalPages", page.getLastPageNum() == 2);
        page.setPageNum(3);
        assertTrue("totalRows > 0, pageNum > totalPages", page.getLastPageNum() == 2);
    }

    public final void testGetPrePageNum() {
    	assertTrue("totalRows = 0", page.getPrePageNum() == 0);
        page.setTotalRows(22);
        assertTrue("totalRows > 0, pageNum = 1", page.getPrePageNum() == 0);
        page.setTotalRows(42);
        page.setPageNum(3);
        assertTrue("totalRows > 0, pageNum > 1", page.getPrePageNum() == 2);
        page.setPageNum(0);
        assertTrue("totalRows > 0, pageNum < 1", page.getPrePageNum() == 0);
    }

    public final void testGetPageRows() {
    	assertTrue("totalRows = 0", page.getPageRows() == 0);
        page.setTotalRows(42);
        page.setPageNum(2);
        assertEquals("totalRows > 0, pageNum < totalPages", PageInfo.DEFAULT_PAGESIZE, page.getPageRows());
        page.setPageNum(3);
        assertTrue("totalRows > 0, pageNum = totalPages", page.getPageRows() == 2);
    }

    public final void testToString() {
        String xml = "<pagelist totalpages=\"0\" totalrecords=\"0\" "
                + "currentpage=\"1\" pagesize=\"20\" firstpage=\"1\" prepage=\"0\" "
                + "nextpage=\"0\" lastpage=\"0\" pagerecords=\"0\"/>";
        assertEquals("totalRows = 0", xml, page.toString());
    }

    public final void testGetItems() {
        List<?> items = new ArrayList<Object>();
        page.setItems(items);
        assertEquals(items, page.getItems());
    }
}
