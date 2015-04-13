package com.jinhe.tss.demo.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jinhe.tss.framework.persistence.pagequery.PageInfo;
import com.jinhe.tss.framework.web.dispaly.grid.GridDataEncoder;
import com.jinhe.tss.framework.web.dispaly.tree.LevelTreeParser;
import com.jinhe.tss.framework.web.dispaly.tree.TreeEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.um.permission.PermissionHelper;
import com.jinhe.tss.util.EasyUtils;

@Controller
@RequestMapping("/auth/xx")
public class XXAction extends BaseActionSupport {
    
    @Autowired private XXService xxService;
    
    @RequestMapping("/tree")
    public void getXXTree(HttpServletResponse response) {
        List<?> list = xxService.getAll();
        TreeEncoder treeEncoder = new TreeEncoder(list, new LevelTreeParser());
        print("SourceTree", treeEncoder);
    }
 
	@RequestMapping("/grid/{page}")
    public void getXXList(HttpServletResponse response, @PathVariable("page") int page) {
		
		List<?> list = xxService.getAll();
		
		PageInfo pageInfo = new PageInfo();
		pageInfo.setItems(list);
		pageInfo.setPageNum(page);
		pageInfo.setPageSize(100);
		pageInfo.setTotalRows(list.size());
		
        GridDataEncoder gridEncoder = new GridDataEncoder(pageInfo.getItems(), "template/xx_grid.xml");
		print(new String[]{"SourceList", "PageInfo"}, new Object[]{gridEncoder, pageInfo});
    }
 
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public void getXX(HttpServletResponse response, @PathVariable("id") String id) {
    	XFormEncoder xformEncoder;
        String uri = "template/xx_xform.xml";
        
        if( "new".equals(id) ) {
            xformEncoder = new XFormEncoder(uri);
        } 
        else {
            Long xxId = EasyUtils.obj2Long(id);
            xformEncoder = new XFormEncoder(uri, xxService.getXX(xxId));
        }
 
        print("SourceInfo", xformEncoder);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void save(HttpServletResponse response, XX entity) {
        boolean isnew = (null == entity.getId());
        xxService.save(entity);
        doAfterSave(isnew, entity, "SourceTree");
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(HttpServletResponse response, @PathVariable("id") Long id) {
        xxService.delete(id);
        printSuccessMessage();
    }
 
    @RequestMapping(value = "/sort/{startId}/{targetId}/{direction}", method = RequestMethod.POST)
    public void sort(HttpServletResponse response, 
            @PathVariable("startId") Long startId, 
            @PathVariable("targetId") Long targetId, 
            @PathVariable("direction") int direction) {
        
        xxService.sort(startId, targetId, direction);
        printSuccessMessage();
    }
 
    @RequestMapping(value = "/move/{id}/{targetId}", method = RequestMethod.POST)
    public void move(HttpServletResponse response, 
            @PathVariable("XXId") Long id, 
            @PathVariable("targetId") Long targetId) {
        
        xxService.move(id, targetId);
        printSuccessMessage();
    }
    
	@RequestMapping("/operations/{resourceId}")
    public void getOperations(HttpServletResponse response, @PathVariable("resourceId") Long resourceId) {
        List<String> list = PermissionHelper.getInstance().getOperationsByResource(resourceId,
                        XXPermission.class.getName(), XXResource.class);

        print("Operation", EasyUtils.list2Str(list));
    }
}
