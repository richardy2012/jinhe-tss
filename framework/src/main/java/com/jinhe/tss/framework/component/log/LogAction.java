package com.jinhe.tss.framework.component.log;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jinhe.tss.framework.web.dispaly.grid.GridDataEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.IXForm;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;

@Controller
@RequestMapping("/log")
public class LogAction extends BaseActionSupport {

    /** 日志展示模板路径 */
	public static final String LOG_XFORM_TEMPLET_PATH = "template/log/Log_xform.xml";
	public static final String LOG_GRID_TEMPLET_PATH  = "template/log/Log_grid.xml";
    
    static final Integer PAGE_SIZE = 50;  

    @Autowired private LogService service;

    @RequestMapping("/apps")
    public void getAllApps4Tree(HttpServletResponse response) {
        List<?> data = service.getAllApps();
        
        StringBuffer sb = new StringBuffer("<actionSet><treeNode name=\"全部\" id=\"_rootId\">");
        for(Iterator<?> it = data.iterator(); it.hasNext();){
            String appCode = (String) it.next();
            sb.append("<treeNode id=\"" + appCode + "\" name=\"" + appCode + "\" icon=\"images/app.gif\"/>");
        }
        print("AppTree", sb.append("</treeNode></actionSet>"));
    }
    
    @RequestMapping("/{page}")
    public void queryLogs4Grid(HttpServletResponse response, LogQueryCondition condition, @PathVariable int page) {
        condition.setPagesize(PAGE_SIZE);
        condition.setCurrentPage(page);
        Object[] objs = service.getLogsByCondition(condition);
        
        GridDataEncoder encoder = new GridDataEncoder(objs[0], LOG_GRID_TEMPLET_PATH);
        
        int totalRows = (Integer) objs[1];
        int currentPageRows = ((List<?>) objs[0]).size();
        
        String pageInfo = generatePageInfo(totalRows, page, PAGE_SIZE, currentPageRows);
        print(new String[]{"LogList", "PageInfo"}, new Object[]{encoder, pageInfo});
    }
    
    @RequestMapping("/item/{id}")
    public void getLogInfo(HttpServletResponse response, @PathVariable long id) {
        Log log = service.getLogById(id);          
        print("LogInfo", new XFormEncoder(LOG_XFORM_TEMPLET_PATH, (IXForm) log));
    }
}

